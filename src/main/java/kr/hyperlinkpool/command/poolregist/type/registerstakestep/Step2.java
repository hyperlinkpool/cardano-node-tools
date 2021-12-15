package kr.hyperlinkpool.command.poolregist.type.registerstakestep;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import kr.hyperlinkpool.command.NodeCommandFormats;
import kr.hyperlinkpool.command.interfaces.Ordered;
import kr.hyperlinkpool.command.poolregist.type.registerstakestep.domains.RegisterStakeDomain;
import kr.hyperlinkpool.command.poolregist.type.registerstakestep.interfaces.RegisterStakeResult;
import kr.hyperlinkpool.constants.NodeConstants;
import kr.hyperlinkpool.constants.StepOrder;
import kr.hyperlinkpool.domain.ProcessResponse;
import kr.hyperlinkpool.domain.ProcessResultDomain;
import kr.hyperlinkpool.i18n.MessageFactory;
import kr.hyperlinkpool.interfaces.JobProcess;
import kr.hyperlinkpool.properties.NodeProperties;
import kr.hyperlinkpool.utils.CommandExecutor;
import kr.hyperlinkpool.utils.MessagePrompter;

public class Step2 implements RegisterStakeResult, Ordered, JobProcess{

	private RegisterStakeDomain registerStakeDomain;
	
	private ProcessResultDomain<RegisterStakeDomain> result;
	
	@Override
	public int getOrder() {
		return StepOrder.STEP2.getStepOrder();
	}

	@Override
	public ProcessResultDomain<RegisterStakeDomain> getResult() {
		return result;
	}

	@Override
	public void setResult(ProcessResultDomain<RegisterStakeDomain> result) {
		this.result = result;
		this.registerStakeDomain = result.getResponseData();
	}
	
	public RegisterStakeDomain getRegisterStakeDomain() {
		return result.getResponseData();
	}

	public void setRegisterStakeDomain(RegisterStakeDomain registerStakeDomain) {
		this.registerStakeDomain = registerStakeDomain;
	}
	
	@Override
	public void run() {
		/**
		 * Step 2
		 * 전송 주소 입력 및 검증
		 */
		List<Map<String,String>> parseTxHashList = registerStakeDomain.getParseTxHashList();
		long sumLovelace = CommandExecutor.sumLoveLaceOnTxList(parseTxHashList);
		MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("보유 수량 : %s", "M00028", sumLovelace + NodeConstants.POSTFIX_LOVELACE), true);
		
		/**
		 * Stake Address 등록에 필요한 ADA 추출
		 */
		String command = null;
		String cardanoKeysFolderString = NodeProperties.getString("cardano.keys.folder");
		String cardanoCliName = NodeProperties.getString("cardano.cli.name");
		String cardanoKeysProtocolJsonString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.protocol.json");
		File cardanoKeysProtocolJsonFile = new File(cardanoKeysProtocolJsonString);
		
		if(cardanoKeysProtocolJsonFile.exists()) {
			cardanoKeysProtocolJsonFile.delete();
		}
		command = CommandExecutor.generateCommand(NodeCommandFormats.GENERATE_PROTOCOL_FILE, cardanoCliName, cardanoKeysProtocolJsonString);
		CommandExecutor.initializeProcessBuilder(command);
		
		/**
		 * Stake Cert 생성
		 */
		String cardanoKeysStakeVkeyString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.stake.vkey");
		String cardanoKeysStakeCertOutputFilePathFilePath = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.stake.cert");
		command = CommandExecutor.generateCommand(NodeCommandFormats.STAKE_POOL_CREATE_A_REGISTRATION_CERTIFICATE, cardanoCliName, cardanoKeysStakeVkeyString, cardanoKeysStakeCertOutputFilePathFilePath);
		ProcessResponse processResponse = CommandExecutor.initializeProcessBuilder(command);
		if(processResponse.getFailureResultString() != null && processResponse.getFailureResultString().length() > 0) {
			registerStakeDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
			result.setSuccess(false);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("프로세스 수행중 오류가 발생했습니다. : ", "M00010") + processResponse.getFailureResultString(), true);
			return;
		}
		
		/**
		 * Draft Transaction
		 */
		String txDraftFilePath = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.tx.draft");
		String cardanoKeysStakeCertString = cardanoKeysFolderString + cardanoKeysStakeCertOutputFilePathFilePath;
		String txIn = CommandExecutor.getTxInDataOnTxList(parseTxHashList);
		String txOut = null;
		
		/**
		 * TxIn
		 */
		txIn = CommandExecutor.firstBlankSpaceRemove(txIn);
		int txInCount = parseTxHashList.size();
		
		/**
		 * TxOut
		 */
		txOut = CommandExecutor.getTxOutData(registerStakeDomain.getReceiveAddress(), sumLovelace);
		txOut = CommandExecutor.firstBlankSpaceRemove(txOut);
		int txOutCount = 1;
		
		/**
		 * Draft Transaction generate
		 */
		command = CommandExecutor.generateCommand(NodeCommandFormats.STAKE_POOL_DRAFT_TRANSACTION, cardanoCliName, txIn, txOut, txDraftFilePath, cardanoKeysStakeCertString);
		CommandExecutor.initializeProcessBuilder(command);
		
		/**
		 * Caculate Fee 단위 : lovelace
		 */
		command = CommandExecutor.generateCommand(NodeCommandFormats.CALCULATE_FEE, cardanoCliName, txDraftFilePath, String.valueOf(txInCount), String.valueOf(txOutCount), "1", "0", cardanoKeysProtocolJsonString);
		processResponse = CommandExecutor.initializeProcessBuilder(command);
		String txFeeString = processResponse.getSuccessResultString();
		txFeeString = txFeeString.replaceAll(NodeConstants.POSTFIX_LOVELACE, "").trim();
		
		/**
		 * 수수료가 모자라 수동입력한 경우 입력한 수수료로 계산되도록 한다.
		 */
		long txFee = Long.parseLong(txFeeString);
		if(registerStakeDomain.isTxFeeReCalc()) {
			txFee = registerStakeDomain.getTxFee();
		}
		
		/**
		 * Get Stake Register Deposit amount 단위 : lovelace
		 */
		String cardanoKeysProtocolJsonFileReadFile = CommandExecutor.readFile(cardanoKeysProtocolJsonString);
		JSONObject protocolJsonObject = new JSONObject(cardanoKeysProtocolJsonFileReadFile);
		long keyDeposit = protocolJsonObject.getLong(NodeConstants.STAKE_ADDRESS_DEPOSIT_KEY);
		
		/**
		 * 현재 보유량에서 fee, keyDeposit을 제했을 때 총량이 0보다 낮으면 등록 불가. 
		 */
		long validateAmount = sumLovelace - keyDeposit - txFee;
		if(validateAmount < 0) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("ADA 보유량이 충분하지 않습니다.", "M00087"), true);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("ADA 현재 보유량 : %s" , "M00088", String.valueOf(sumLovelace) + NodeConstants.POSTFIX_LOVELACE), true);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Stake Register 필요량 : %s", "M00099" ,String.valueOf(keyDeposit) + NodeConstants.POSTFIX_LOVELACE), true);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Transaction 수수료 : %s", "M00090", String.valueOf(txFee) + NodeConstants.POSTFIX_LOVELACE), true);
			MessagePrompter.promptMessage("", true);
			
			registerStakeDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
			result.setResponseData(registerStakeDomain);
			result.setSuccess(false);
			return;
		}
		
		/**
		 * 보증금과 수수료를 제외한 txOut설정
		 */
		txOut = CommandExecutor.getTxOutData(registerStakeDomain.getReceiveAddress(), validateAmount);
		txOut = CommandExecutor.firstBlankSpaceRemove(txOut);
		
		registerStakeDomain.setSumLovelace(sumLovelace);
		registerStakeDomain.setKeyDeposit(keyDeposit);
		registerStakeDomain.setTxIn(txIn);
		registerStakeDomain.setTxOut(txOut);
		registerStakeDomain.setTxFee(txFee);
		registerStakeDomain.setNextOrder(this.getOrder() + 1);
		result.setResponseData(registerStakeDomain);
	}
}