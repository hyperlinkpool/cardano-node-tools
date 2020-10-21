package kr.hyperlinkpool.command.balanceandwithdrawing.withdrawstep;

import java.io.File;
import java.util.List;
import java.util.Map;

import kr.hyperlinkpool.command.NodeCommandFormats;
import kr.hyperlinkpool.command.balanceandwithdrawing.withdrawstep.domains.WithdrawDomain;
import kr.hyperlinkpool.command.balanceandwithdrawing.withdrawstep.interfaces.WithdrawResult;
import kr.hyperlinkpool.command.interfaces.Ordered;
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

public class Step3 implements WithdrawResult, Ordered, JobProcess{

	private WithdrawDomain withdrawDomain;
	
	private ProcessResultDomain<WithdrawDomain> result;
	
	@Override
	public int getOrder() {
		return StepOrder.STEP3.getStepOrder();
	}

	@Override
	public ProcessResultDomain<WithdrawDomain> getResult() {
		return result;
	}

	@Override
	public void setResult(ProcessResultDomain<WithdrawDomain> result) {
		this.result = result;
		this.withdrawDomain = result.getResponseData();
	}
	
	public WithdrawDomain getWithdrawDomain() {
		return withdrawDomain;
	}

	public void setWithdrawDomain(WithdrawDomain withdrawDomain) {
		this.withdrawDomain = withdrawDomain;
	}
	
	@Override
	public void run() {
		/**
		 * Step 3
		 * 전송 수량 입력 및 수수료 계산
		 */
		String cardanoCliName = NodeProperties.getString("cardano.cli.name");
		String cardanoKeysFolderString = NodeProperties.getString("cardano.keys.folder");
		String cardanoKeysPaymentAddressString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.payment.addr");
		File cardanoKeysPaymentAddressFile = new File(cardanoKeysPaymentAddressString);
		if(!cardanoKeysPaymentAddressFile.exists()) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Payment 주소가 존재하지 않습니다. Payment 주소 생성 후 다시 시도하세요.", "M00025"), true);
			withdrawDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
			return;
		}
		
		String paymentAddressString = CommandExecutor.readFile(cardanoKeysPaymentAddressFile);
		String receiveAddressString = withdrawDomain.getReceiveAddress();

		List<Map<String,String>> parseTxHashList = withdrawDomain.getParseTxHashList();
		long sumLovelace = CommandExecutor.sumLoveLaceOnTxList(parseTxHashList);
		
		String inputValue = null;
		if(receiveAddressString.equals(paymentAddressString)) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("현재 Pool 주소로 보낼 경우는 합산한 총액만 전송가능합니다. 전송 수수료를 제외한 나머지 총액이 전송됩니다.", "M00029"), true);
			inputValue = CommandListener.getInstance().listenCommand(MessageFactory.getInstance().getMessage("진행하시겠습니까? (Y/n) : ", "M00030"), false);
			if(!"Y".equalsIgnoreCase(inputValue)) {
				withdrawDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
				return;
			}
			
			/**
			 * 입력 값을 총액으로 셋팅
			 */
			inputValue = String.valueOf(sumLovelace);
		}else {
			inputValue = CommandListener.getInstance().listenCommand(MessageFactory.getInstance().getMessage("전송할 수량을 입력해 주세요.(입력 단위 : lovelace, 1 ADA = 1,000,000 lovelace, 취소 : Q) : ", "M00008"), false);
		}
//		
		if("Q".equalsIgnoreCase(inputValue)) {
			withdrawDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
			return;
		}else {
			long parseLong = Long.parseLong(inputValue);
			/**
			 * 입력 수량이 가능 수량을 초과하면 전송 불가
			 */
			if(parseLong > sumLovelace) {
				MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("수량이 부족합니다. 다시 입력하세요. 인출 가능 수량 : %s , 입력수량 : %s", "M00009", sumLovelace + NodeConstants.POSTFIX_LOVELACE, parseLong + NodeConstants.POSTFIX_LOVELACE), true);
				withdrawDomain.setNextOrder(this.getOrder());
				return;
			}
			
			String txIn = CommandExecutor.getTxInDataOnTxList(parseTxHashList);
			/**
			 * 첫번째 공백 제거
			 */
			txIn = CommandExecutor.firstBlankSpaceRemove(txIn);
			String txOut = null;
			String txOutSender = null;
			String txOutReceiver = null;
			
			int txInCount = parseTxHashList.size();
			int txOutCount = 0;
			/**
			 * 보내는 주소와 받는 주소가 같으면 TxOut은 하나. TxHash를 합친다.
			 */
			if(paymentAddressString.equals(receiveAddressString)) {
				txOut = CommandExecutor.getTxOutData(paymentAddressString, sumLovelace);
				txOutCount = 1;
			}else {
				/**
				 * 보내는 수량과 보유 수량이 같으면 Tx는 하나로 계산된다. 그리고 수수료를 제외한 나머지 총액을 전송한다.
				 */
				if(parseLong == sumLovelace) {
					txOutReceiver = CommandExecutor.getTxOutData(receiveAddressString, parseLong);
					txOut = txOutReceiver;
					txOutCount = 1;
				}else {
					txOutSender = CommandExecutor.getTxOutData(paymentAddressString, sumLovelace - parseLong);
					txOutReceiver = CommandExecutor.getTxOutData(receiveAddressString, parseLong);
					txOut = txOutSender + txOutReceiver;
					txOutCount = 2;
				}
			}
			
			/**
			 * 첫번째 공백 제거
			 */
			if(txOut != null) {
				txOut = CommandExecutor.firstBlankSpaceRemove(txOut);
			}
			
			/**
			 * Draft Transaction
			 */
			String txDraftFilePath = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.tx.draft");
			
			String command = CommandExecutor.generateCommand(NodeCommandFormats.DRAFT_TRANSACTION, cardanoCliName, txIn, txOut, txDraftFilePath);
			ProcessResponse processResponse = CommandExecutor.initializeProcessBuilder(command);
			if(processResponse.getFailureResultString() != null && processResponse.getFailureResultString().length() > 0) {
				MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("프로세스 수행중 오류가 발생했습니다. : ", "M00010") + processResponse.getFailureResultString(), true);
				withdrawDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
				return;
			}
			
			if(processResponse.getFailureResultString() != null && processResponse.getFailureResultString().indexOf("Invalid Argument") > -1) {
				MessagePrompter.promptMessage(processResponse.getFailureResultString(), true);
				withdrawDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
				return;
			}
			
			/**
			 * Calculate FEE
			 */
			String protocolJsonFileString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.protocol.json");
			command = CommandExecutor.generateCommand(NodeCommandFormats.CALCULATE_FEE, cardanoCliName, txDraftFilePath, String.valueOf(txInCount), String.valueOf(txOutCount), "1", "0", protocolJsonFileString);
			processResponse = CommandExecutor.initializeProcessBuilder(command);
			String txFeeString = processResponse.getSuccessResultString();
			txFeeString = txFeeString.replaceAll(NodeConstants.POSTFIX_LOVELACE, "").trim();
			long txFee = Long.parseLong(txFeeString);;
			
			/**
			 * 수수료가 모자라 수동입력한 경우 입력한 수수료로 계산되도록 한다.
			 */
			if(withdrawDomain.isTxFeeReCalc()) {
				txFee = withdrawDomain.getTxFee();
			}
			
			long senderTotal = 0;
			long receiverTotal = 0;
			
			if(paymentAddressString.equals(receiveAddressString)) {
				receiverTotal = sumLovelace - txFee;
				txOut = CommandExecutor.getTxOutData(paymentAddressString, receiverTotal);
				txOutCount = 1;
			}else{
				if((sumLovelace - parseLong - txFee) < 0) {
					senderTotal = 0;
					receiverTotal = sumLovelace - txFee;
				}
				
				if((sumLovelace - parseLong - txFee) >= 0) {
					senderTotal = sumLovelace - parseLong - txFee;
					receiverTotal = parseLong;
				}
				
				txOutSender = CommandExecutor.getTxOutData(paymentAddressString, senderTotal);
				txOutReceiver = CommandExecutor.getTxOutData(receiveAddressString, receiverTotal);
				
				if(senderTotal == 0) {
					txOutCount = 1;
					txOut = txOutReceiver;
				}else {
					txOutCount = 2;
					txOut = txOutSender + txOutReceiver;
				}
			}
			
			/**
			 * 첫번째 공백 제거
			 */
			if(txOut != null) {
				txOut = CommandExecutor.firstBlankSpaceRemove(txOut);
			}
			
			withdrawDomain.setSumLovelace(sumLovelace);
			withdrawDomain.setReceiverTotal(receiverTotal);
			withdrawDomain.setSenderAddress(paymentAddressString);
			withdrawDomain.setTxIn(txIn);
			withdrawDomain.setTxOut(txOut);
			withdrawDomain.setTxFee(txFee);
			withdrawDomain.setNextOrder(this.getOrder() + 1);
			result.setResponseData(withdrawDomain);
		}
	}
}