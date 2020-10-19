package kr.hyperlinkpool.command.poolregist.type.registerstakestep;

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
import kr.hyperlinkpool.properties.NodeProperties;
import kr.hyperlinkpool.utils.CommandExecutor;
import kr.hyperlinkpool.utils.CommandListener;
import kr.hyperlinkpool.utils.MessagePrompter;

public class Step3 implements RegisterStakeResult, Ordered, Runnable{

private RegisterStakeDomain registerStakeDomain;
	
	private ProcessResultDomain<RegisterStakeDomain> result;
	
	@Override
	public int getOrder() {
		return StepOrder.STEP3.getStepOrder();
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
	
	public RegisterStakeDomain getregisterStakeDomain() {
		return result.getResponseData();
	}

	public void setregisterStakeDomain(RegisterStakeDomain registerStakeDomain) {
		this.registerStakeDomain = registerStakeDomain;
	}
	
	@Override
	public void run() {
		/**
		 * Step 3
		 * Stake Key 등록
		 */
		long sumLovelace = registerStakeDomain.getSumLovelace();
		long keyDeposit = registerStakeDomain.getKeyDeposit();
		long txFee = registerStakeDomain.getTxFee();
		String txIn = registerStakeDomain.getTxIn();
		String txOut = registerStakeDomain.getTxOut();
		long remainAmount = sumLovelace - keyDeposit - txFee;
		MessagePrompter.promptMessage("", true);
		MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("ADA 현재 보유량 : %s" , "M00088", String.valueOf(sumLovelace) + NodeConstants.POSTFIX_LOVELACE), true);
		MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Stake Register 필요량 : %s", "M00099" ,String.valueOf(keyDeposit) + NodeConstants.POSTFIX_LOVELACE), true);
		MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Transaction 수수료 : %s", "M00090", String.valueOf(txFee) + NodeConstants.POSTFIX_LOVELACE), true);
		MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("처리 완료 후 잔여 예상량 : %s", "M00100", String.valueOf(remainAmount) + NodeConstants.POSTFIX_LOVELACE), true);
		MessagePrompter.promptMessage("", true);
		String inputValue = CommandListener.getInstance().listenCommand(MessageFactory.getInstance().getMessage("Stake Key를 Cardano Blockchain에 등록하시겠습니까? (Y/n) : ", "M00101"), false);
		
		if("Y".equalsIgnoreCase(inputValue)) {
			String cardanoCliName = NodeProperties.getString("cardano.cli.name");
			String cardanoKeysFolderString = NodeProperties.getString("cardano.keys.folder");
			
			String mainnetCurrentTip = CommandExecutor.mainnetCurrentTip();
			JSONObject mainnetCurrentTipJsonObject = new JSONObject(mainnetCurrentTip);
			int slotNo = mainnetCurrentTipJsonObject.getInt("slotNo");
			
			String ttl = String.valueOf(slotNo+200);
			
			String txRawFilePath = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.tx.raw");
			String cardanoKeysStakeCertString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.stake.cert");
			String command = CommandExecutor.generateCommand(NodeCommandFormats.STAKE_POOL_SUBMIT_THE_CERTIFICATE_WITH_A_TRANSACTION, cardanoCliName, txIn, txOut, ttl, String.valueOf(txFee), txRawFilePath, cardanoKeysStakeCertString);
			ProcessResponse initializeProcessBuilder = CommandExecutor.initializeProcessBuilder(command);
			String successResultString = initializeProcessBuilder.getSuccessResultString();
			
			String cardanoKeysPaymentSkeyPathString = null;
			String cardanoKeysStakeSkeyPathString = null;
			String txSignedFile = null;
			if(successResultString != null && successResultString.length() == 0) {
				cardanoKeysPaymentSkeyPathString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.payment.skey");
				cardanoKeysStakeSkeyPathString= cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.stake.skey");
				txSignedFile = cardanoKeysFolderString +NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.tx.signed");
				command = CommandExecutor.generateCommand(NodeCommandFormats.STAKE_POOL_SIGN_THE_TRACSACTION, cardanoCliName, txRawFilePath, cardanoKeysPaymentSkeyPathString, cardanoKeysStakeSkeyPathString, txSignedFile);
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
								registerStakeDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
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
								registerStakeDomain.setNextOrder(this.getOrder()-1);
								registerStakeDomain.setTxFeeReCalc(true);
								registerStakeDomain.setTxFee(reCalcTxFee);
								return;
							} catch (Exception e) {
								MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("다시 입력해 주세요.", "M00019"), true);
							}
						}
					}else {
						MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("취소되었습니다.", "M00020"), true);
						registerStakeDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
						result.setSuccess(false);
						return;
					}
				}else {
					if(failureResultString.indexOf("ValueNotConservedUTxO") > -1) {
						MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("잔고가 충분하지 않습니다.", "M00151"), true);
					}else {
						MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Stake Key 등록에 실패하였습니다.", "M00102"), true);
					}
					registerStakeDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
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
		registerStakeDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
		result.setResponseData(registerStakeDomain);
	}
}