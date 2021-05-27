package kr.hyperlinkpool.command.poolregist.type.deregisterpoolstep;

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
import kr.hyperlinkpool.utils.CommandListener;
import kr.hyperlinkpool.utils.MessagePrompter;

public class Step4 implements DeRegisterPoolResult, Ordered, JobProcess{

	private DeRegisterPoolDomain deRegisterPoolDomain;
	
	private ProcessResultDomain<DeRegisterPoolDomain> result;
	
	@Override
	public int getOrder() {
		return StepOrder.STEP4.getStepOrder();
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
		 * Step 5
		 * Pool 등록
		 */
		long poolDeposit = deRegisterPoolDomain.getPoolDeposit();
		long receiverTotal = deRegisterPoolDomain.getReceiverTotal();
		long inputEpoch = deRegisterPoolDomain.getInputEpoch();
		long txFee = deRegisterPoolDomain.getTxFee();
		String txIn = deRegisterPoolDomain.getTxIn();
		String txOut = deRegisterPoolDomain.getTxOut();
		
		MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Transaction 수수료 : %s","M00090", String.valueOf(txFee) + NodeConstants.POSTFIX_LOVELACE), true);
		MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("처리 완료 후 잔여 예상량 : %s","M00093", String.valueOf(receiverTotal) + NodeConstants.POSTFIX_LOVELACE), true);
		MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("은퇴 Epoch : %s","M00170", String.valueOf(inputEpoch)), true);
		MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("은퇴 Epoch 이 후 현재 주소로 반환되는 ADA 보증금 : %s","M00171", String.valueOf(poolDeposit) + NodeConstants.POSTFIX_LOVELACE), true);
		MessagePrompter.promptMessage("", true);
		String poolRegistMessage = MessageFactory.getInstance().getMessage("현재 Pool의 은퇴를 진행하시겠습니까? (Y/n) : ", "M00169");
		String inputValue = CommandListener.getInstance().listenCommand(poolRegistMessage, false);
		
		if("Y".equalsIgnoreCase(inputValue)) {
			String cardanoCliName = NodeProperties.getString("cardano.cli.name");
			String cardanoKeysFolderString = NodeProperties.getString("cardano.keys.folder");
			String mainnetCurrentTip = CommandExecutor.mainnetCurrentTip();
			JSONObject mainnetCurrentTipJsonObject = new JSONObject(mainnetCurrentTip);
			int slotNo = mainnetCurrentTipJsonObject.getInt("slot");
			String ttl = String.valueOf(slotNo+200);
			String txRawFilePath = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.tx.raw");
			String cardanoKeysPoolDeRegistrationCertFileString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.pool.deregistration.cert");
			String command = CommandExecutor.generateCommand(NodeCommandFormats.POOL_DEREGIST_BUILD_THE_TRANSACTION, cardanoCliName, txIn, txOut, ttl, String.valueOf(txFee), txRawFilePath, cardanoKeysPoolDeRegistrationCertFileString);
			ProcessResponse initializeProcessBuilder = CommandExecutor.initializeProcessBuilder(command);
			String successResultString = initializeProcessBuilder.getSuccessResultString();
			
			String cardanoKeysPaymentSkeyPathString = null;
			String cardanoKeysColdSkeyPathString = null;
			String txSignedFile = null;
			if(successResultString != null && successResultString.length() == 0) {
				cardanoKeysPaymentSkeyPathString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.payment.skey");
				cardanoKeysColdSkeyPathString= cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.cold.skey");
				txSignedFile = cardanoKeysFolderString +NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.tx.signed");
				command = CommandExecutor.generateCommand(NodeCommandFormats.POOL_DEREGIST_SIGN_THE_TRANSACTION, cardanoCliName, txRawFilePath, cardanoKeysPaymentSkeyPathString, cardanoKeysColdSkeyPathString, txSignedFile);
				initializeProcessBuilder = CommandExecutor.initializeProcessBuilder(command);
			}
			
			command = CommandExecutor.generateCommand(NodeCommandFormats.SUBMIT_TRANSACTION, cardanoCliName, txSignedFile);
			initializeProcessBuilder = CommandExecutor.initializeProcessBuilder(command);
			
			String failureResultString = initializeProcessBuilder.getFailureResultString();
			MessagePrompter.promptMessage(failureResultString, true);
			if(failureResultString.indexOf("Shelley command failed") > -1) {
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
								deRegisterPoolDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
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
								deRegisterPoolDomain.setNextOrder(this.getOrder()-1);
								deRegisterPoolDomain.setTxFeeReCalc(true);
								deRegisterPoolDomain.setTxFee(reCalcTxFee);
								return;
							} catch (Exception e) {
								MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("다시 입력해 주세요.", "M00019"), true);
							}
						}
					}else {
						MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("취소되었습니다.", "M00020"), true);
						deRegisterPoolDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
						result.setSuccess(false);
						return;
					}
				}else {
					if(failureResultString.indexOf("ValueNotConservedUTxO") > -1) {
						MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("현재 계좌에 ADA 잔고가 충분하지 않습니다. 10 ADA 정도 입금 후에 재시도 하시기 바랍니다.", "M00151"), true);
					}else {
						MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Pool 은퇴 등록에 실패하였습니다.", "M00172"), true);
					}
					deRegisterPoolDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
					result.setSuccess(false);
					return;
				}
			}
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("등록되었습니다.", "M00095"), true);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("잔고 동기화에 시간이 걸릴 수 있습니다.", "M00152"), true);
			result.setSuccess(true);
		}else {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("취소되었습니다.", "M00020"), true);
			result.setSuccess(false);
		}
		deRegisterPoolDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
		result.setResponseData(deRegisterPoolDomain);
	}
}