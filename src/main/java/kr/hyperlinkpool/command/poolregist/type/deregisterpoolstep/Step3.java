package kr.hyperlinkpool.command.poolregist.type.deregisterpoolstep;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import kr.hyperlinkpool.command.NodeCommandFormats;
import kr.hyperlinkpool.command.interfaces.Ordered;
import kr.hyperlinkpool.command.poolregist.type.deregisterpoolstep.domains.DeRegisterPoolDomain;
import kr.hyperlinkpool.command.poolregist.type.deregisterpoolstep.interfaces.DeRegisterPoolResult;
import kr.hyperlinkpool.constants.NodeConstants;
import kr.hyperlinkpool.constants.StepOrder;
import kr.hyperlinkpool.domain.ProcessResponse;
import kr.hyperlinkpool.domain.ProcessResultDomain;
import kr.hyperlinkpool.i18n.MessageFactory;
import kr.hyperlinkpool.interfaces.JobProcess;
import kr.hyperlinkpool.properties.NodeProperties;
import kr.hyperlinkpool.utils.CommandExecutor;
import kr.hyperlinkpool.utils.MessagePrompter;

public class Step3 implements DeRegisterPoolResult, Ordered, JobProcess{

	private DeRegisterPoolDomain deRegisterPoolDomain;
	
	private ProcessResultDomain<DeRegisterPoolDomain> result;
	
	@Override
	public int getOrder() {
		return StepOrder.STEP3.getStepOrder();
	}

	@Override
	public ProcessResultDomain<DeRegisterPoolDomain> getResult() {
		return result;
	}

	@Override
	public void setResult(ProcessResultDomain<DeRegisterPoolDomain> result) {
		this.result = result;
		this.deRegisterPoolDomain = result.getResponseData();
	}
	
	public DeRegisterPoolDomain getDeRegisterPoolDomain() {
		return result.getResponseData();
	}

	public void setDeRegisterPoolDomain(DeRegisterPoolDomain deRegisterPoolDomain) {
		this.deRegisterPoolDomain = deRegisterPoolDomain;
	}
	
	@Override
	public void run() {
		/**
		 * 수수료 계산, 은퇴 인증서 생성
		 */
		String command = null;
		String cardanoKeysFolderString = NodeProperties.getString("cardano.keys.folder");
		String cardanoCliName = NodeProperties.getString("cardano.cli.name");
		
		/**
		 * cold.vkey
		 */
		String cardanoKeysColdVkeyFileString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.cold.vkey");
		
		/**
		 * Retire Epoch
		 */
		long retireEpoch = deRegisterPoolDomain.getRetireEpoch();
 		String cardanoKeysPoolDeregistrationCertString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.pool.deregistration.cert");
		command = CommandExecutor.generateCommand(NodeCommandFormats.POOL_DEREGIST_CREATE_DEREGISTRATION_CERTIFICATE, cardanoCliName, cardanoKeysColdVkeyFileString, String.valueOf(retireEpoch), cardanoKeysPoolDeregistrationCertString);
		CommandExecutor.initializeProcessBuilder(command);
		
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
			deRegisterPoolDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
			result.setResponseData(deRegisterPoolDomain);
			result.setSuccess(false);
			return;
		}
		
		long sumLovelace = CommandExecutor.sumLoveLaceOnTxList(parseTxHashList);
		MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("보유 수량 : %s", "M00029", String.valueOf(sumLovelace) + NodeConstants.POSTFIX_LOVELACE), true);
		
		/**
		 * Stake Address 등록에 필요한 ADA 추출
		 */
		String cardanoKeysProtocolJsonString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.protocol.json");
		String cardanoKeysProtocolJsonFileReadFile = CommandExecutor.readFile(cardanoKeysProtocolJsonString);
		JSONObject protocolJsonObject = new JSONObject(cardanoKeysProtocolJsonFileReadFile);
		long poolDeposit = protocolJsonObject.getLong("poolDeposit");
		deRegisterPoolDomain.setPoolDeposit(poolDeposit);
		
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
		txOut = CommandExecutor.getTxOutData(paymentAddressString, sumLovelace);
		txOut = CommandExecutor.firstBlankSpaceRemove(txOut);
		int txOutCount = 1;
		
		/**
		 * Draft Transaction generate
		 */
		command = CommandExecutor.generateCommand(NodeCommandFormats.POOL_DEREGIST_DRAFT_THE_TRANSACTION, cardanoCliName, txIn, txOut, txDraftFilePath, cardanoKeysPoolDeregistrationCertString);
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
		if(deRegisterPoolDomain.isTxFeeReCalc()) {
			txFee = deRegisterPoolDomain.getTxFee();
		}
		
		/**
		 * 현재 보유량에서 fee을 제했을 때 총량이 0보다 낮으면 등록 불가. 
		 */
		long validateAmount = sumLovelace - txFee;
		if(validateAmount < 0) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("ADA 보유량이 충분하지 않습니다.", "M00087"), true);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("ADA 현재 보유량 : %s", "M00088", String.valueOf(sumLovelace) + NodeConstants.POSTFIX_LOVELACE), true);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Transaction 수수료 : %s",  "M00089", String.valueOf(txFee) + NodeConstants.POSTFIX_LOVELACE), true);
			MessagePrompter.promptMessage("", true);
			deRegisterPoolDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
			result.setResponseData(deRegisterPoolDomain);
			result.setSuccess(false);
			return;
		}
		
		/**
		 * 은퇴이기 때문에 보증금을 더한다. poolDeposit
		 * 은퇴시에는 수수료 계산을 Submit후에 알 수 있다. 실제 수수료가 적게 ㄱ
		 */
		long receiverTotal = 0;
		receiverTotal = sumLovelace - txFee;
		txOut = CommandExecutor.getTxOutData(paymentAddressString, receiverTotal);
		txOutCount = 1;
		
		/**
		 * 첫번째 공백 제거
		 */
		if(txOut != null) {
			txOut = CommandExecutor.firstBlankSpaceRemove(txOut);
		}
		
		deRegisterPoolDomain.setSumLovelace(sumLovelace);
		deRegisterPoolDomain.setReceiverTotal(receiverTotal);
		deRegisterPoolDomain.setSenderAddress(paymentAddressString);
		deRegisterPoolDomain.setTxIn(txIn);
		deRegisterPoolDomain.setTxOut(txOut);
		deRegisterPoolDomain.setTxFee(txFee);
		deRegisterPoolDomain.setNextOrder(this.getOrder() + 1);
		result.setResponseData(deRegisterPoolDomain);
		result.setSuccess(true);
	}
}