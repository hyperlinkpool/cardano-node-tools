package kr.hyperlinkpool.command.balanceandwithdrawing.rewardswithdrawstep;

import java.util.List;
import java.util.Map;

import kr.hyperlinkpool.command.NodeCommandFormats;
import kr.hyperlinkpool.command.balanceandwithdrawing.rewardswithdrawstep.domains.RewardsWithdrawDomain;
import kr.hyperlinkpool.command.balanceandwithdrawing.rewardswithdrawstep.interfaces.RewardsWithdrawResult;
import kr.hyperlinkpool.command.interfaces.Ordered;
import kr.hyperlinkpool.constants.NodeConstants;
import kr.hyperlinkpool.constants.StepOrder;
import kr.hyperlinkpool.domain.ProcessResponse;
import kr.hyperlinkpool.domain.ProcessResultDomain;
import kr.hyperlinkpool.i18n.MessageFactory;
import kr.hyperlinkpool.properties.NodeProperties;
import kr.hyperlinkpool.utils.CommandExecutor;
import kr.hyperlinkpool.utils.CommandListener;
import kr.hyperlinkpool.utils.MessagePrompter;

public class Step2 implements RewardsWithdrawResult, Ordered, Runnable{

private RewardsWithdrawDomain rewardsWithdrawDomain;
	
	private ProcessResultDomain<RewardsWithdrawDomain> result;
	
	@Override
	public int getOrder() {
		return StepOrder.STEP2.getStepOrder();
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
		 * Step 2
		 * 전송 수량 입력 및 수수료 계산
		 */
		String cardanoCliName = NodeProperties.getString("cardano.cli.name");
		String cardanoKeysFolderString = NodeProperties.getString("cardano.keys.folder");
		String paymentAddressString = rewardsWithdrawDomain.getSenderAddress();
		String receiveAddressString = rewardsWithdrawDomain.getReceiveAddress();
		String stakeAddressString = rewardsWithdrawDomain.getStakeAddress();
		long withdrawableAmount = rewardsWithdrawDomain.getWithdrawableAmount();
		List<Map<String,String>> parseTxHashList = rewardsWithdrawDomain.getParseTxHashList();
		long sumLovelace = CommandExecutor.sumLoveLaceOnTxList(parseTxHashList);
		
		MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("인출 가능한 Rewards 수량 : %s" + NodeConstants.POSTFIX_LOVELACE, "M00007", String.valueOf(rewardsWithdrawDomain.getWithdrawableAmount())), true);
		String inputValue = CommandListener.getInstance().listenCommand(MessageFactory.getInstance().getMessage("전송할 수량을 입력해 주세요.(입력 단위 : lovelace, 1 ADA = 1,000,000 lovelace, 취소 : Q) : ", "M00008"), false);
		if("Q".equalsIgnoreCase(inputValue)) {
			rewardsWithdrawDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
			return;
		}else {
			try {
				long withdrawAmount = Long.parseLong(inputValue);
				/**
				 * 입력 수량이 가능 수량을 초과하면 전송 불가
				 */
				if(withdrawAmount > withdrawableAmount) {
					MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("수량이 부족합니다. 다시 입력하세요. 인출 가능 수량 : %s , 입력수량 : %s", "M00009", String.valueOf(withdrawableAmount) + NodeConstants.POSTFIX_LOVELACE, String.valueOf(withdrawableAmount) + NodeConstants.POSTFIX_LOVELACE), true);
					rewardsWithdrawDomain.setNextOrder(this.getOrder());
					return;
				}
				
				String txIn = CommandExecutor.getTxInDataOnTxList(parseTxHashList);
				/**
				 * 첫번째 공백 제거
				 */
				txIn = CommandExecutor.firstBlankSpaceRemove(txIn);
				String txOut = null;
				String withdrawal = null;
				
				int txInCount = parseTxHashList.size();
				int txOutCount = 1;
				/**
				 * 보내는 주소와 받는 주소가 같으면 TxOut은 하나. TxHash를 합친다.
				 */
				txOut = CommandExecutor.getTxOutData(receiveAddressString, sumLovelace + withdrawAmount);;
				withdrawal = stakeAddressString + "+" + String.valueOf(withdrawAmount);
				
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
				String command = CommandExecutor.generateCommand(NodeCommandFormats.WITHDRAWING_REWARDS_BUILD_DRAFT_TRANSACTION, cardanoCliName, txIn, txOut, withdrawal, txDraftFilePath);
				ProcessResponse processResponse = CommandExecutor.initializeProcessBuilder(command);
				if(processResponse.getFailureResultString() != null && processResponse.getFailureResultString().length() > 0) {
					MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("프로세스 수행중 오류가 발생했습니다. : ", "M00010") + processResponse.getFailureResultString(), true);
					rewardsWithdrawDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
					return;
				}
				
				if(processResponse.getFailureResultString() != null && processResponse.getFailureResultString().indexOf("Invalid Argument") > -1) {
					MessagePrompter.promptMessage(processResponse.getFailureResultString(), true);
					rewardsWithdrawDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
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
				if(rewardsWithdrawDomain.isTxFeeReCalc()) {
					txFee = rewardsWithdrawDomain.getTxFee();
				}
				
				long receiverTotal = sumLovelace - txFee + withdrawAmount ;
				txOut = CommandExecutor.getTxOutData(paymentAddressString, receiverTotal);
				txOutCount = 1;
				
				/**
				 * 첫번째 공백 제거
				 */
				if(txOut != null) {
					txOut = CommandExecutor.firstBlankSpaceRemove(txOut);
				}
				
				rewardsWithdrawDomain.setSumLovelace(sumLovelace);
				rewardsWithdrawDomain.setReceiverTotal(receiverTotal);
				rewardsWithdrawDomain.setTxIn(txIn);
				rewardsWithdrawDomain.setTxOut(txOut);
				rewardsWithdrawDomain.setWithdrawal(withdrawal);
				rewardsWithdrawDomain.setWithdrawAmount(withdrawAmount);
				rewardsWithdrawDomain.setTxFee(txFee);
				rewardsWithdrawDomain.setNextOrder(this.getOrder() + 1);
				result.setResponseData(rewardsWithdrawDomain);
			} catch (Exception e) {
				MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("숫자를 입력해 주세요.", "M00011"), true);
				rewardsWithdrawDomain.setNextOrder(this.getOrder());
				return;
			}
		}
	}
}