package kr.hyperlinkpool.command.balanceandwithdrawing.rewardswithdrawstep;

import org.json.JSONObject;

import kr.hyperlinkpool.command.NodeCommandFormats;
import kr.hyperlinkpool.command.balanceandwithdrawing.rewardswithdrawstep.domains.RewardsWithdrawDomain;
import kr.hyperlinkpool.command.balanceandwithdrawing.rewardswithdrawstep.interfaces.RewardsWithdrawResult;
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

public class Step3 implements RewardsWithdrawResult, Ordered, JobProcess{

private RewardsWithdrawDomain rewardsWithdrawDomain;
	
	private ProcessResultDomain<RewardsWithdrawDomain> result;
	
	@Override
	public int getOrder() {
		return StepOrder.STEP3.getStepOrder();
	}

	@Override
	public ProcessResultDomain<RewardsWithdrawDomain> getResult() {
		return result;
	}

	@Override
	public void setResult(ProcessResultDomain<RewardsWithdrawDomain> result) {
		this.result = result;
		this.rewardsWithdrawDomain = result.getResponseData();
	}
	
	public RewardsWithdrawDomain getWithdrawDomain() {
		return result.getResponseData();
	}

	public void setWithdrawDomain(RewardsWithdrawDomain rewardsWithdrawDomain) {
		this.rewardsWithdrawDomain = rewardsWithdrawDomain;
	}
	
	@Override
	public void run() {
		/**
		 * Step 3
		 * 보상분 인출
		 */
		long sumLovelace = rewardsWithdrawDomain.getSumLovelace();
		long withdrawableAmount = rewardsWithdrawDomain.getWithdrawableAmount();
		long withdrawAmount = rewardsWithdrawDomain.getWithdrawAmount();
		long txFee = rewardsWithdrawDomain.getTxFee();
		String txIn = rewardsWithdrawDomain.getTxIn();
		String txOut = rewardsWithdrawDomain.getTxOut();
		String withdrawal = rewardsWithdrawDomain.getWithdrawal();
		
		/**
		 * 보유 수량을 모두 보낼 경우 보유 수량에서 수수료를 제외한 나머지를 보낸다.는 안내문구 출력
		 */
		MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("보유수량 : %s , 보상분 인출 수량 : %s , 수수료 : %s", "M00012", String.valueOf(sumLovelace) + NodeConstants.POSTFIX_LOVELACE, String.valueOf(withdrawAmount) + NodeConstants.POSTFIX_LOVELACE, String.valueOf(txFee) + NodeConstants.POSTFIX_LOVELACE), true);
		MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("인출 후 잔여 보상 수량 : %s", "M00013", String.valueOf(withdrawableAmount - withdrawAmount) + NodeConstants.POSTFIX_LOVELACE), true);
		MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("인출 후 총 보유 수량 : %s", "M00014", String.valueOf(sumLovelace + withdrawAmount - txFee) + NodeConstants.POSTFIX_LOVELACE), true);
		String inputValue = CommandListener.getInstance().listenCommand(MessageFactory.getInstance().getMessage("인출하시겠습니까? (Y/n) : ", "M00015"), false);
		
		if("Y".equalsIgnoreCase(inputValue)) {
			String cardanoCliName = NodeProperties.getString("cardano.cli.name");
			String cardanoKeysFolderString = NodeProperties.getString("cardano.keys.folder");
			
			String mainnetCurrentTip = CommandExecutor.mainnetCurrentTip();
			JSONObject mainnetCurrentTipJsonObject = new JSONObject(mainnetCurrentTip);
			int slotNo = mainnetCurrentTipJsonObject.getInt("slot");
			
			String ttl = String.valueOf(slotNo+1000);
			
			String txRawFilePath = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.tx.raw");
			
			String command = CommandExecutor.generateCommand(NodeCommandFormats.WITHDRAWING_REWARDS_BUILD_THE_RAW_TRANSACTION, cardanoCliName, txIn, txOut, withdrawal, ttl, String.valueOf(txFee), txRawFilePath);
			ProcessResponse initializeProcessBuilder = CommandExecutor.initializeProcessBuilder(command);
			String successResultString = initializeProcessBuilder.getSuccessResultString();
			
			String paymentSigningKeyFilePath = null;
			String stakeSigningKeyFilePath = null;
			String txSignedFile = null;
			if(successResultString != null && successResultString.length() == 0) {
				paymentSigningKeyFilePath = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.payment.skey");
				stakeSigningKeyFilePath = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.stake.skey");
				txSignedFile = cardanoKeysFolderString +NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.tx.signed");
				command = CommandExecutor.generateCommand(NodeCommandFormats.WITHDRAWING_REWARDS_SIGN_THE_TRANSACTIONS, cardanoCliName, txRawFilePath, paymentSigningKeyFilePath, stakeSigningKeyFilePath, txSignedFile);
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
								rewardsWithdrawDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
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
								rewardsWithdrawDomain.setNextOrder(this.getOrder()-1);
								rewardsWithdrawDomain.setTxFeeReCalc(true);
								rewardsWithdrawDomain.setTxFee(reCalcTxFee);
								return;
							} catch (Exception e) {
								MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("다시 입력해 주세요.", "M00019"), true);
							}
						}
					}else {
						MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("취소되었습니다.", "M00020"), true);
						rewardsWithdrawDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
						result.setSuccess(false);
						return;
					}
				}else {
					if(failureResultString.indexOf("ValueNotConservedUTxO") > -1) {
						MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("현재 계좌에 ADA 잔고가 충분하지 않습니다. 10 ADA 정도 입금 후에 재시도 하시기 바랍니다.", "M00151"), true);
					}else {
						MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("전송 실패하였습니다.", "M00021"), true);
					}
					rewardsWithdrawDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
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
		rewardsWithdrawDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
		result.setResponseData(rewardsWithdrawDomain);
	}
}