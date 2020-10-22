package kr.hyperlinkpool.command.poolregist.type.poolregisterstep;

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
import kr.hyperlinkpool.utils.CommandListener;
import kr.hyperlinkpool.utils.MessagePrompter;

public class Step5 implements PoolRegisterResult, Ordered, JobProcess{

	private PoolRegisterDomain poolRegisterDomain;
	
	private ProcessResultDomain<PoolRegisterDomain> result;
	
	@Override
	public int getOrder() {
		return StepOrder.STEP5.getStepOrder();
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
		 * Step 5
		 * Pool 등록
		 */
		long sumLovelace = poolRegisterDomain.getSumLovelace();
		long receiverTotal = poolRegisterDomain.getReceiverTotal();
		long poolDeposit = poolRegisterDomain.getPoolDeposit();
		long txFee = poolRegisterDomain.getTxFee();
		
		String txIn = poolRegisterDomain.getTxIn();
		String txOut = poolRegisterDomain.getTxOut();
		
		String poolRegistMessage = null;
		MessageFactory.getInstance().getMessage("ADA 현재 보유량 : %s", "M00088", String.valueOf(sumLovelace) + NodeConstants.POSTFIX_LOVELACE);
		if(poolDeposit != 0) {
			poolRegistMessage = MessageFactory.getInstance().getMessage("Pool 정보를 등록하시겠습니까?(Y/n) : ", "M00091");
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Pool Register 필요량 : %s", "M00089", String.valueOf(poolDeposit) + NodeConstants.POSTFIX_LOVELACE), true);
		}else {
			poolRegistMessage = MessageFactory.getInstance().getMessage("Pool 정보를 수정하시겠습니까?(Y/n) : ", "M00092");
		}
		MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Transaction 수수료 : %s","M00090", String.valueOf(txFee) + NodeConstants.POSTFIX_LOVELACE), true);
		MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("처리 완료 후 잔여 예상량 : %s","M00093", String.valueOf(receiverTotal) + NodeConstants.POSTFIX_LOVELACE), true);
		MessagePrompter.promptMessage("", true);
		
		String inputValue = CommandListener.getInstance().listenCommand(poolRegistMessage, false);
		
		if("Y".equalsIgnoreCase(inputValue)) {
			String cardanoCliName = NodeProperties.getString("cardano.cli.name");
			String cardanoKeysFolderString = NodeProperties.getString("cardano.keys.folder");
			
			String mainnetCurrentTip = CommandExecutor.mainnetCurrentTip();
			JSONObject mainnetCurrentTipJsonObject = new JSONObject(mainnetCurrentTip);
			int slotNo = mainnetCurrentTipJsonObject.getInt("slotNo");
			
			String ttl = String.valueOf(slotNo+200);
			
			String txRawFilePath = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.tx.raw");
			
			String cardanoKeysPoolRegistrationCertFileString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.pool.registration.cert");
			String cardanoKeysPoolDelegationCertFileString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.pool.delegation.cert");
			String command = CommandExecutor.generateCommand(NodeCommandFormats.POOL_REGISTRATION_BUILD_THE_TRANSACTION, cardanoCliName, txIn, txOut, ttl, String.valueOf(txFee), txRawFilePath, cardanoKeysPoolRegistrationCertFileString, cardanoKeysPoolDelegationCertFileString);
			ProcessResponse initializeProcessBuilder = CommandExecutor.initializeProcessBuilder(command);
			String successResultString = initializeProcessBuilder.getSuccessResultString();
			
			String cardanoKeysPaymentSkeyPathString = null;
			String cardanoKeysStakeSkeyPathString = null;
			String cardanoKeysColdSkeyPathString = null;
			String txSignedFile = null;
			if(successResultString != null && successResultString.length() == 0) {
				cardanoKeysPaymentSkeyPathString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.payment.skey");
				cardanoKeysStakeSkeyPathString= cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.stake.skey");
				cardanoKeysColdSkeyPathString= cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.cold.skey");
				txSignedFile = cardanoKeysFolderString +NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.tx.signed");
				command = CommandExecutor.generateCommand(NodeCommandFormats.POOL_REGISTRATION_SIGN_THE_TRANSACTION, cardanoCliName, txRawFilePath, cardanoKeysPaymentSkeyPathString, cardanoKeysStakeSkeyPathString, cardanoKeysColdSkeyPathString, txSignedFile);
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
								poolRegisterDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
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
								poolRegisterDomain.setNextOrder(this.getOrder()-1);
								poolRegisterDomain.setTxFeeReCalc(true);
								poolRegisterDomain.setTxFee(reCalcTxFee);
								return;
							} catch (Exception e) {
								MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("다시 입력해 주세요.", "M00019"), true);
							}
						}
					}else {
						MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("취소되었습니다.", "M00020"), true);
						poolRegisterDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
						result.setSuccess(false);
						return;
					}
				}else {
					if(failureResultString.indexOf("ValueNotConservedUTxO") > -1) {
						MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("현재 계좌에 ADA 잔고가 충분하지 않습니다. 100 ADA 정도 입금 후에 재시도 하시기 바랍니다.", "M00151"), true);
					}else {
						MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Pool 등록에 실패하였습니다.", "M00094"), true);
					}
					poolRegisterDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
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
		poolRegisterDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
		result.setResponseData(poolRegisterDomain);
	}
}