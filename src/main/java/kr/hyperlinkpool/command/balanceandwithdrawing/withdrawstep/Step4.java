package kr.hyperlinkpool.command.balanceandwithdrawing.withdrawstep;

import org.json.JSONObject;

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

public class Step4 implements WithdrawResult, Ordered, JobProcess{

	private WithdrawDomain withdrawDomain;
	
	private ProcessResultDomain<WithdrawDomain> result;
	
	@Override
	public int getOrder() {
		return StepOrder.STEP4.getStepOrder();
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
		 * Step 4
		 * 에이다 전송
		 * 노드에서 수수료 계산이 잘못되었을 경우, 수동 처리 프로세스  
		 */
		long sumLovelace = withdrawDomain.getSumLovelace();
		long receiverTotal = withdrawDomain.getReceiverTotal();
		long txFee = withdrawDomain.getTxFee();
		String paymentAddressString = withdrawDomain.getSenderAddress();
		String receiveAddressString = withdrawDomain.getReceiveAddress();
		String txIn = withdrawDomain.getTxIn();
		String txOut = withdrawDomain.getTxOut();
		
		/**
		 * 보유 수량을 모두 보낼 경우 보유 수량에서 수수료를 제외한 나머지를 보낸다.는 안내문구 출력
		 */
		if(sumLovelace == (receiverTotal+txFee)) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("1. 전송수량과 수수료의 합계가 보유수량보다 많을 경우,", "M00032"), true);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("2. 전송수량과 보유수량이 같을 경우,", "M00033"), true);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("보유수량에서 수수료를 제외한 나머지를 전송합니다.", "M00034"), true);
			MessagePrompter.promptMessage("", true);
		}
		MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("보유수량 : %s , 전송수량 : %s , 수수료 : %s", "M00035", String.valueOf(sumLovelace) + NodeConstants.POSTFIX_LOVELACE, String.valueOf(receiverTotal) + NodeConstants.POSTFIX_LOVELACE, String.valueOf(txFee) + NodeConstants.POSTFIX_LOVELACE), true);
		MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Sender address : %s", "M00036", String.valueOf(paymentAddressString)), true);
		MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Receiver address : %s", "M00037", String.valueOf(receiveAddressString)), true);
		String inputValue = CommandListener.getInstance().listenCommand(MessageFactory.getInstance().getMessage("전송하시겠습니까? (Y/n) : ", "M00038"), false);
		
		if("Y".equalsIgnoreCase(inputValue)) {
			String cardanoCliName = NodeProperties.getString("cardano.cli.name");
			String cardanoKeysFolderString = NodeProperties.getString("cardano.keys.folder");
			
			String mainnetCurrentTip = CommandExecutor.mainnetCurrentTip();
			JSONObject mainnetCurrentTipJsonObject = new JSONObject(mainnetCurrentTip);
			int slotNo = mainnetCurrentTipJsonObject.getInt("slot");
			
			String ttl = String.valueOf(slotNo+200);
			
			String txRawFilePath = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.tx.raw");
			
			String command = CommandExecutor.generateCommand(NodeCommandFormats.BUILD_THE_TRANSACTION, cardanoCliName, txIn, txOut, ttl, String.valueOf(txFee), txRawFilePath);
			ProcessResponse initializeProcessBuilder = CommandExecutor.initializeProcessBuilder(command);
			String successResultString = initializeProcessBuilder.getSuccessResultString();
			
			String signingKeyFilePath = null;
			String txSignedFile = null;
			if(successResultString != null && successResultString.length() == 0) {
				signingKeyFilePath = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.payment.skey");
				txSignedFile = cardanoKeysFolderString +NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.tx.signed");
				command = CommandExecutor.generateCommand(NodeCommandFormats.SIGN_THE_TRANSACTION, cardanoCliName, txRawFilePath, signingKeyFilePath, txSignedFile);
				initializeProcessBuilder = CommandExecutor.initializeProcessBuilder(command);
			}
			
			command = CommandExecutor.generateCommand(NodeCommandFormats.SUBMIT_TRANSACTION, cardanoCliName, txSignedFile);
			initializeProcessBuilder = CommandExecutor.initializeProcessBuilder(command);
			
			String failureResultString = initializeProcessBuilder.getFailureResultString();
			MessagePrompter.promptMessage(failureResultString, true);
			if(failureResultString.indexOf("ShelleyTxValidationError") > -1) {
				if(failureResultString.indexOf("FeeTooSmallUTxO") > -1) {
					/**
					 * 수수료 부족일 경우 수수료만 입력한 수수료로 재시도한다.
					 */
					inputValue = CommandListener.getInstance().listenCommand(MessageFactory.getInstance().getMessage("수수료 계산값이 실제 전송시 소요량보다 부족합니다. 수동입력으로 진행하시겠습니까?(Y/n) : ", "M00016"), false);
					if("Y".equalsIgnoreCase(inputValue)) {
						boolean loop = true;
						while(loop) {
							inputValue = CommandListener.getInstance().listenCommand(MessageFactory.getInstance().getMessage("화면에 보이는 수수료를 입력해 주세요. (Q : 취소) : ", "M00017"), false);
							if("Q".equalsIgnoreCase(inputValue)) {
								withdrawDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
								result.setSuccess(false);
								return;
							}
							
							try {
								long reCalcTxFee = Long.parseLong(inputValue);
								
								/**
								 * 원래 수수료보다 과도하게 높게 입력할 경우, 과도한 수수료를 소요하지 않도록 방어.
								 */
								long manualFeeAllowPercentage = NodeProperties.getLong("cardano.validations.manualfeeallowpercentage");
								long manualFeeMax = txFee + (txFee * manualFeeAllowPercentage / 100);
								if(reCalcTxFee > manualFeeMax) {
									MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("수수료가 과도하게 높습니다. 다시 입력하세요. 수수료 허용치 : %s" + NodeConstants.POSTFIX_LOVELACE, "M00018", String.valueOf(manualFeeMax)), true);
									continue;
								}
								
								loop = false;
								withdrawDomain.setNextOrder(this.getOrder()-1);
								withdrawDomain.setTxFeeReCalc(true);
								withdrawDomain.setTxFee(reCalcTxFee);
								return;
							} catch (Exception e) {
								MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("다시 입력해 주세요.", "M00019"), true);
							}
						}
					}else {
						MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("취소되었습니다.", "M00020"), true);
						withdrawDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
						result.setSuccess(false);
						return;
					}
				}else {
					if(failureResultString.indexOf("ValueNotConservedUTxO") > -1) {
						MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("현재 계좌에 ADA 잔고가 충분하지 않습니다. 10 ADA 정도 입금 후에 재시도 하시기 바랍니다.", "M00151"), true);
					}else {
						MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("전송 실패하였습니다.", "M00021"), true);
					}
					withdrawDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
					result.setSuccess(false);
					return;
				}
			}
			
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("전송되었습니다.", "M00022"), true);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("잔고 동기화에 시간이 걸릴 수 있습니다.", "M00152"), true);
			result.setSuccess(true);
		}else {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("취소되었습니다.", "M00020"), true);
			result.setSuccess(false);
		}
		withdrawDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
		result.setResponseData(withdrawDomain);
	}
}