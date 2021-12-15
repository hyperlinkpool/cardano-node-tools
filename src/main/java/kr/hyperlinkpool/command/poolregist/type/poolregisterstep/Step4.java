package kr.hyperlinkpool.command.poolregist.type.poolregisterstep;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import kr.hyperlinkpool.command.NodeCommandFormats;
import kr.hyperlinkpool.command.interfaces.Ordered;
import kr.hyperlinkpool.command.poolregist.type.poolregisterstep.domains.PoolRegisterDomain;
import kr.hyperlinkpool.command.poolregist.type.poolregisterstep.interfaces.PoolRegisterResult;
import kr.hyperlinkpool.constants.NodeConstants;
import kr.hyperlinkpool.constants.StepOrder;
import kr.hyperlinkpool.domain.ProcessResponse;
import kr.hyperlinkpool.domain.ProcessResultDomain;
import kr.hyperlinkpool.i18n.MessageFactory;
import kr.hyperlinkpool.interfaces.JobProcess;
import kr.hyperlinkpool.properties.NodeProperties;
import kr.hyperlinkpool.utils.CommandExecutor;
import kr.hyperlinkpool.utils.MessagePrompter;

public class Step4 implements PoolRegisterResult, Ordered, JobProcess{

	private PoolRegisterDomain poolRegisterDomain;
	
	private ProcessResultDomain<PoolRegisterDomain> result;
	
	@Override
	public int getOrder() {
		return StepOrder.STEP4.getStepOrder();
	}

	@Override
	public ProcessResultDomain<PoolRegisterDomain> getResult() {
		return result;
	}

	@Override
	public void setResult(ProcessResultDomain<PoolRegisterDomain> result) {
		this.result = result;
		this.setPoolRegisterDomain(result.getResponseData());
	}

	public PoolRegisterDomain getPoolRegisterDomain() {
		return poolRegisterDomain;
	}

	public void setPoolRegisterDomain(PoolRegisterDomain poolRegisterDomain) {
		this.poolRegisterDomain = poolRegisterDomain;
	}
	
	@Override
	public void run() {
		/**
		 * Step 4
		 * 전송 주소 입력 및 검증
		 */
		String command = null;
		String cardanoCliName = NodeProperties.getString("cardano.cli.name");
		String cardanoKeysFolderString = NodeProperties.getString("cardano.keys.folder");

		/**
		 * pool.registration.cert 검증
		 */
		String cardanoKeysPoolRegistrationCertFileString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.pool.registration.cert");
		File cardanoKeysPoolRegistrationCertFile = new File(cardanoKeysPoolRegistrationCertFileString);
		if(!cardanoKeysPoolRegistrationCertFile.exists()) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Pool Registration 인증서가 생성되지 않았습니다. 인증서 생성 후 다시 시도하세요.", "M00080"), true);
			return;
		}
		
		/**
		 * pool.delegation.cert 검증
		 */
		String cardanoKeysPoolDelegationCertFileString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.pool.delegation.cert");
		File cardanoKeysPoolDelegationCertFile = new File(cardanoKeysPoolDelegationCertFileString);
		if(!cardanoKeysPoolDelegationCertFile.exists()) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Pool Delegation 인증서가 생성되지 않았습니다. 인증서 생성 후 다시 시도하세요.", "M00081"), true);
			return;
		}
		
		/**
		 * Payment Signing Key 검증
		 */
		String cardanoKeysPaymentSkeyFileString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.payment.skey");
		File cardanoKeysPaymentSkeyFile = new File(cardanoKeysPaymentSkeyFileString);
		if(!cardanoKeysPaymentSkeyFile.exists()) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Payment Signing Key가 생성되지 않았습니다. 키 생성 후 다시 시도하세요.", "M00082"), true);
			return;
		}
		
		/**
		 * Stake Signing Key 검증
		 */
		String cardanoKeysStakeSkeyFileString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.stake.skey");
		File cardanoKeysStakeSkeyFile = new File(cardanoKeysStakeSkeyFileString);
		if(!cardanoKeysStakeSkeyFile.exists()) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Stake Signing Key가 생성되지 않습니다. 키 생성 후 다시 시도하세요.", "M00083"), true);
			return;
		}
		
		/**
		 * Cold Signing Key 검증
		 */
		String cardanoKeysColdSkeyFileString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.cold.skey");
		File cardanoKeysColdSkeyFile = new File(cardanoKeysColdSkeyFileString);
		if(!cardanoKeysColdSkeyFile.exists()) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Cold Signing Key가 생성되지 않습니다. 키 생성 후 다시 시도하세요.", "M00044"), true);
			return;
		}
		
		/**
		 * 현재 풀 주소에서 ADA TxHash 추출
		 */
		String cardanoKeysPaymentAddressPathString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.payment.addr");
		String paymentAddressString = CommandExecutor.readFile(cardanoKeysPaymentAddressPathString);
		command = CommandExecutor.generateCommand(NodeCommandFormats.PAYMENT_ADDRESS_BALANCE_CHECK, cardanoCliName, paymentAddressString);
		ProcessResponse initializeProcessBuilder = CommandExecutor.initializeProcessBuilder(command);
		MessagePrompter.promptMessage(initializeProcessBuilder.getSuccessResultString(), true);
		List<Map<String,String>> parseTxHashList = CommandExecutor.parseTxHashString(initializeProcessBuilder.getSuccessResultString());
		if(parseTxHashList.size()==0) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("인출할 ADA가 없습니다.", "M00027"), true);
			MessagePrompter.promptMessage("", true);
			poolRegisterDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
			result.setResponseData(poolRegisterDomain);
			result.setSuccess(false);
			return;
		}
		
		/**
		 * Pool 등록 / 수정은 자신에게 ADA전송하기 때문에 송, 수신자가 같다.
		 */
		poolRegisterDomain.setSenderAddress(paymentAddressString);
		poolRegisterDomain.setReceiveAddress(paymentAddressString);
		
		long sumLovelace = CommandExecutor.sumLoveLaceOnTxList(parseTxHashList);
		MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("보유 수량 : %s", "M00029", String.valueOf(sumLovelace) + NodeConstants.POSTFIX_LOVELACE), true);
		
		/**
		 * Stake Address 등록에 필요한 ADA 추출
		 */
		String cardanoKeysProtocolJsonString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.protocol.json");
		File cardanoKeysProtocolJsonFile = new File(cardanoKeysProtocolJsonString);
		if(cardanoKeysProtocolJsonFile.exists()) {
			cardanoKeysProtocolJsonFile.delete();
		}
		command = CommandExecutor.generateCommand(NodeCommandFormats.GENERATE_PROTOCOL_FILE, cardanoCliName, cardanoKeysProtocolJsonString);
		CommandExecutor.initializeProcessBuilder(command);
		
		/**
		 * Draft Transaction
		 */
		String txDraftFilePath = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.tx.draft");
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
		txOut = CommandExecutor.getTxOutData(poolRegisterDomain.getReceiveAddress(), sumLovelace);
		txOut = CommandExecutor.firstBlankSpaceRemove(txOut);
		int txOutCount = 1;
		
		/**
		 * Draft Transaction generate
		 */
		command = CommandExecutor.generateCommand(NodeCommandFormats.POOL_REGISTRATION_DRAFT_THE_TRANSACTION, cardanoCliName, txIn, txOut, txDraftFilePath, cardanoKeysPoolRegistrationCertFileString, cardanoKeysPoolDelegationCertFileString);
		CommandExecutor.initializeProcessBuilder(command);
		
		/**
		 * Caculate Fee 단위 : lovelace
		 */
		command = CommandExecutor.generateCommand(NodeCommandFormats.CALCULATE_FEE, cardanoCliName, txDraftFilePath, String.valueOf(txInCount), String.valueOf(txOutCount), "1", "0", cardanoKeysProtocolJsonString);
		ProcessResponse processResponse = CommandExecutor.initializeProcessBuilder(command);
		String txFeeString = processResponse.getSuccessResultString();
		txFeeString = txFeeString.replaceAll(NodeConstants.POSTFIX_LOVELACE, "").trim();
		
		/**
		 * 수수료가 모자라 수동입력한 경우 입력한 수수료로 계산되도록 한다.
		 */
		long txFee = Long.parseLong(txFeeString);
		if(poolRegisterDomain.isTxFeeReCalc()) {
			txFee = poolRegisterDomain.getTxFee();
		}
		
		/**
		 * Get Stake Pool Deposit amount 단위 : lovelace
		 */
		String cardanoKeysProtocolJsonFileReadFile = CommandExecutor.readFile(cardanoKeysProtocolJsonString);
		JSONObject protocolJsonObject = new JSONObject(cardanoKeysProtocolJsonFileReadFile);
		long poolDeposit = protocolJsonObject.getLong(NodeConstants.STAKE_POOL_DEPOSIT_KEY);
		
		/**
		 * Pool이 등록된 상태이면 현재 프로세스는 갱신이기 때문에 poolDeposit이 필요하지 않으므로 0으로 셋팅
		 */
		/**
		 * Cold Verification Key 검증
		 */
		String cardanoKeysColdVkeyFileString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.cold.vkey");
		File cardanoKeysColdVkeyFile = new File(cardanoKeysColdVkeyFileString);
		if(!cardanoKeysColdVkeyFile.exists()) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Cold Verification Key가 생성되지 않았습니다. 키 생성 후 다시 시도하세요.", "M00061"), true);
			return;
		}
		command = CommandExecutor.generateCommand(NodeCommandFormats.POOL_REGISTRATION_GET_POOL_ID, cardanoCliName, cardanoKeysColdVkeyFileString);
		ProcessResponse processBuilder = CommandExecutor.initializeProcessBuilder(command);
		String failureResultString = processBuilder.getFailureResultString();
		if(failureResultString != null && failureResultString.length() > 0){
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Pool Id 확인중 에러가 발생했습니다.", "M00084"), true);
			poolRegisterDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
			result.setSuccess(false);
			return;
		}
		
		/**
		 * 수수료가 모자라 수동입력한 경우 다시 Pool을 검사하지 않는다.
		 */
		if(!poolRegisterDomain.isTxFeeReCalc()) {
			String poolId = processBuilder.getSuccessResultString().trim();
			String cardanoCliNameFullPath = NodeProperties.getString("cardano.cli.path") + NodeConstants.PATH_DELIMITER + cardanoCliName;
			command = CommandExecutor.generateCommand(NodeCommandFormats.POOL_REGISTRATION_YOUR_POOLID_IN_THE_NETWORK_LEDGER_STATE, cardanoCliNameFullPath, poolId);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Pool 정보 확인 중입니다. 좀 오래 걸려요.", "M00085"), true);
			processBuilder = CommandExecutor.initializeProcessBuilderWithBash(command);
			failureResultString = processBuilder.getFailureResultString();
			if(failureResultString != null && failureResultString.length() > 0){
				MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Blockchain 확인중 에러가 발생했습니다.", "M00086"), true);
				poolRegisterDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
				result.setSuccess(false);
				return;
			}
		}
		
		/**
		 * Blockchain에 Pool정보가 존재하면 Pool 갱신으로 판단한다.
		 */
		String successResultString = processBuilder.getSuccessResultString();
		if(successResultString != null && successResultString.length() > 0) {
			poolDeposit = 0;
		}
		
		/**
		 * 현재 보유량에서 fee, keyDeposit을 제했을 때 총량이 0보다 낮으면 등록 불가. 
		 */
		long validateAmount = sumLovelace - poolDeposit - txFee;
		if(validateAmount < 0) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("ADA 보유량이 충분하지 않습니다.", "M00087"), true);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("ADA 현재 보유량 : %s", "M00088", String.valueOf(sumLovelace) + NodeConstants.POSTFIX_LOVELACE), true);
			if(poolDeposit != 0) {
				MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Pool Register 필요량 : %s", "M00089", String.valueOf(poolDeposit) + NodeConstants.POSTFIX_LOVELACE), true);
			}
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Transaction 수수료 : %s",  "M00089", String.valueOf(txFee) + NodeConstants.POSTFIX_LOVELACE), true);
			MessagePrompter.promptMessage("", true);
			
			poolRegisterDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
			result.setResponseData(poolRegisterDomain);
			result.setSuccess(false);
			return;
		}
		
		/**
		 * 수수료가 모자라 수동입력한 경우 입력한 수수료로 계산되도록 한다.
		 */
		if(poolRegisterDomain.isTxFeeReCalc()) {
			txFee = poolRegisterDomain.getTxFee();
		}
		
		long receiverTotal = 0;
		receiverTotal = sumLovelace - poolDeposit - txFee;
		txOut = CommandExecutor.getTxOutData(paymentAddressString, receiverTotal);
		txOutCount = 1;
		
		/**
		 * 첫번째 공백 제거
		 */
		if(txOut != null) {
			txOut = CommandExecutor.firstBlankSpaceRemove(txOut);
		}
		
		poolRegisterDomain.setSumLovelace(sumLovelace);
		poolRegisterDomain.setReceiverTotal(receiverTotal);
		poolRegisterDomain.setSenderAddress(paymentAddressString);
		poolRegisterDomain.setPoolDeposit(poolDeposit);
		poolRegisterDomain.setTxIn(txIn);
		poolRegisterDomain.setTxOut(txOut);
		poolRegisterDomain.setTxFee(txFee);
		poolRegisterDomain.setNextOrder(this.getOrder() + 1);
		result.setResponseData(poolRegisterDomain);
		result.setSuccess(true);
	}
}