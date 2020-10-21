package kr.hyperlinkpool.command.poolregist.type.delegatestep;

import java.io.File;
import java.util.List;
import java.util.Map;

import kr.hyperlinkpool.command.NodeCommandFormats;
import kr.hyperlinkpool.command.interfaces.Ordered;
import kr.hyperlinkpool.command.poolregist.type.delegatestep.domains.DelegateStakeAddressDomain;
import kr.hyperlinkpool.command.poolregist.type.delegatestep.interfaces.DelegateStakeAddressResult;
import kr.hyperlinkpool.constants.NodeConstants;
import kr.hyperlinkpool.constants.StepOrder;
import kr.hyperlinkpool.domain.ProcessResponse;
import kr.hyperlinkpool.domain.ProcessResultDomain;
import kr.hyperlinkpool.i18n.MessageFactory;
import kr.hyperlinkpool.interfaces.JobProcess;
import kr.hyperlinkpool.properties.NodeProperties;
import kr.hyperlinkpool.utils.CommandExecutor;
import kr.hyperlinkpool.utils.CommandListener;
import kr.hyperlinkpool.utils.MessagePrompter;

public class Step2 implements DelegateStakeAddressResult, Ordered, JobProcess{

	private DelegateStakeAddressDomain delegateStakeAddressDomain;
	
	private ProcessResultDomain<DelegateStakeAddressDomain> result;
	
	@Override
	public int getOrder() {
		return StepOrder.STEP2.getStepOrder();
	}

	@Override
	public ProcessResultDomain<DelegateStakeAddressDomain> getResult() {
		return result;
	}

	@Override
	public void setResult(ProcessResultDomain<DelegateStakeAddressDomain> result) {
		this.result = result;
		this.delegateStakeAddressDomain = result.getResponseData();
	}
	
	public DelegateStakeAddressDomain getDelegateStakeAddressDomain() {
		return result.getResponseData();
	}

	public void setDelegateStakeAddressDomain(DelegateStakeAddressDomain delegateStakeAddressDomain) {
		this.delegateStakeAddressDomain = delegateStakeAddressDomain;
	}
	
	@Override
	public void run() {
		/**
		 * Step 2
		 * Pool ID 입력 및 검증
		 */
		String poolId = CommandListener.getInstance().listenCommand(MessageFactory.getInstance().getMessage("위임할 Pool ID를 입력해 주세요. (Q : 취소) : ", "M00161"), false);
		/**
		 * pool로 시작하는 id를 입력하면 Hash값으로 변환 후에 조회
		 */
		if(poolId.indexOf("pool") > -1) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Pool ID는 16진수 형태만 가능합니다. 예)[HYPER] = 263498e010c7a49bbfd7c4e1aab29809fca7ed993f9e14192a75871e", "M00162"), true);
			delegateStakeAddressDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
			result.setSuccess(false);
			return;
		}
		
		if("Q".equalsIgnoreCase(poolId)) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("취소되었습니다.", "M00020"), true);
			delegateStakeAddressDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
			result.setSuccess(false);
			return;
		}
		
		String cardanoCliName = NodeProperties.getString("cardano.cli.name");
		String cardanoCliNameFullPath = NodeProperties.getString("cardano.cli.path") + NodeConstants.PATH_DELIMITER + cardanoCliName;
		String command = null;
		command = CommandExecutor.generateCommand(NodeCommandFormats.POOL_REGISTRATION_YOUR_POOLID_IN_THE_NETWORK_LEDGER_STATE, cardanoCliNameFullPath, poolId);
		MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Pool 정보 확인 중입니다. 좀 오래 걸려요.", "M00085"), true);
		ProcessResponse processResponse = CommandExecutor.initializeProcessBuilderWithBash(command);
		String failureResultString = processResponse.getFailureResultString();
		if(failureResultString != null && failureResultString.length() > 0){
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Blockchain 확인중 에러가 발생했습니다.", "M00086"), true);
			delegateStakeAddressDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
			result.setSuccess(false);
			return;
		}
		
		String successResultString = processResponse.getSuccessResultString();
		if(successResultString != null && successResultString.length() == 0) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Pool 등록 정보를 찾을 수 없습니다.", "M00133"), true);
			delegateStakeAddressDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
			result.setSuccess(false);
		}else {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Pool 정보가 확인되었습니다. 정상 상태입니다.", "M00132"), true);
		}
		
		List<Map<String,String>> parseTxHashList = delegateStakeAddressDomain.getParseTxHashList();
		long sumLovelace = CommandExecutor.sumLoveLaceOnTxList(parseTxHashList);
		MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("보유 수량 : %s", "M00028", sumLovelace + NodeConstants.POSTFIX_LOVELACE), true);
		
		/**
		 * Stake Address 등록에 필요한 ADA 추출
		 */
		String cardanoKeysFolderString = NodeProperties.getString("cardano.keys.folder");
		String cardanoKeysProtocolJsonString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.protocol.json");
		File cardanoKeysProtocolJsonFile = new File(cardanoKeysProtocolJsonString);
		
		if(cardanoKeysProtocolJsonFile.exists()) {
			cardanoKeysProtocolJsonFile.delete();
		}
		command = CommandExecutor.generateCommand(NodeCommandFormats.GENERATE_PROTOCOL_FILE, cardanoCliName, cardanoKeysProtocolJsonString);
		CommandExecutor.initializeProcessBuilder(command);
		
		/**
		 * Delegate Stake Cert 생성
		 */
		String cardanoKeysStakeVkeyString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.stake.vkey");
		String cardanoKeysStakeDelegateCertOutputFilePathFilePath = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.stake.delegate.cert");
		command = CommandExecutor.generateCommand(NodeCommandFormats.STAKE_POOL_DELEGATE_STAKE_CERTIFICATE, cardanoCliName, cardanoKeysStakeVkeyString, poolId, cardanoKeysStakeDelegateCertOutputFilePathFilePath);
		processResponse = CommandExecutor.initializeProcessBuilder(command);
		if(processResponse.getFailureResultString() != null && processResponse.getFailureResultString().length() > 0) {
			delegateStakeAddressDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
			result.setSuccess(false);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("프로세스 수행중 오류가 발생했습니다. : ", "M00010") + processResponse.getFailureResultString(), true);
			return;
		}
		
		/**
		 * Draft Transaction
		 */
		String txDraftFilePath = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.tx.draft");
		String cardanoKeysStakeDeregisterCertString = cardanoKeysStakeDelegateCertOutputFilePathFilePath;
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
		txOut = CommandExecutor.getTxOutData(delegateStakeAddressDomain.getReceiveAddress(), sumLovelace);
		txOut = CommandExecutor.firstBlankSpaceRemove(txOut);
		int txOutCount = 1;
		
		/**
		 * Draft Transaction generate
		 */
		command = CommandExecutor.generateCommand(NodeCommandFormats.STAKE_POOL_DRAFT_TRANSACTION, cardanoCliName, txIn, txOut, txDraftFilePath, cardanoKeysStakeDeregisterCertString);
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
		if(delegateStakeAddressDomain.isTxFeeReCalc()) {
			txFee = delegateStakeAddressDomain.getTxFee();
		}
		
		/**
		 * 현재 보유량에서 fee를 제외한 총량이 0보다 낮으면 철회 불가.
		 */
		long validateAmount = sumLovelace - txFee;
		if(validateAmount < 0) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("ADA 보유량이 충분하지 않습니다.", "M00087"), true);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("ADA 현재 보유량 : %s" , "M00088", String.valueOf(sumLovelace) + NodeConstants.POSTFIX_LOVELACE), true);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Transaction 수수료 : %s", "M00090", String.valueOf(txFee) + NodeConstants.POSTFIX_LOVELACE), true);
			MessagePrompter.promptMessage("", true);
			
			delegateStakeAddressDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
			result.setResponseData(delegateStakeAddressDomain);
			result.setSuccess(false);
			return;
		}
		
		/**
		 * 수수료를 제외한 txOut 계산
		 */
		txOut = CommandExecutor.getTxOutData(delegateStakeAddressDomain.getReceiveAddress(), validateAmount);
		txOut = CommandExecutor.firstBlankSpaceRemove(txOut);
		
		/**
		 * Pool Id 셋팅
		 */
		delegateStakeAddressDomain.setPoolId(poolId);
		delegateStakeAddressDomain.setSumLovelace(sumLovelace);
		delegateStakeAddressDomain.setTxIn(txIn);
		delegateStakeAddressDomain.setTxOut(txOut);
		delegateStakeAddressDomain.setTxFee(txFee);
		delegateStakeAddressDomain.setNextOrder(this.getOrder() + 1);
		result.setResponseData(delegateStakeAddressDomain);
	}
}