package kr.hyperlinkpool.command.balanceandwithdrawing.rewardswithdrawstep;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

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
import kr.hyperlinkpool.utils.MessagePrompter;

public class Step1 implements RewardsWithdrawResult, Ordered, JobProcess{

	private RewardsWithdrawDomain rewardsWithdrawDomain;
	
	private ProcessResultDomain<RewardsWithdrawDomain> result;
	
	@Override
	public int getOrder() {
		return StepOrder.STEP1.getStepOrder();
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
		 * Step 1
		 * Protocol Json 생성
		 * 현재 풀 주소에서 ADA TxHash 추출
		 */
		String cardanoKeysFolderString = NodeProperties.getString("cardano.keys.folder");
 		String cardanoKeysProtocolJsonString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.protocol.json");
		String cardanoCliName = NodeProperties.getString("cardano.cli.name");
		
		String command = null;
		File cardanoKeysProtocolJsonFile = new File(cardanoKeysProtocolJsonString);
		if(cardanoKeysProtocolJsonFile.exists()) {
			cardanoKeysProtocolJsonFile.delete();
		}
		command = CommandExecutor.generateCommand(NodeCommandFormats.GENERATE_PROTOCOL_FILE, cardanoCliName, cardanoKeysProtocolJsonString);
		CommandExecutor.initializeProcessBuilder(command);
		
		/**
		 * Stake 보상량 확인
		 */
		String cardanoKeysStakeAddressPathString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.stake.addr");
		File cardanoKeysStakeAddressFile = new File(cardanoKeysStakeAddressPathString);
		if(!cardanoKeysStakeAddressFile.exists()) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Stake 주소가 생성되지 않았습니다. 주소 생성 후 다시 시도하세요.", "M00001"), true);
			rewardsWithdrawDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
			result.setSuccess(false);
			return;
		}
		
		String stakeAddressString = CommandExecutor.readFile(cardanoKeysStakeAddressPathString);
		command = CommandExecutor.generateCommand(NodeCommandFormats.STAKE_ADDRESS_BALANCE_CHECK, cardanoCliName, stakeAddressString);
		ProcessResponse initializeProcessBuilder = CommandExecutor.initializeProcessBuilder(command);
		JSONArray rewardsData = new JSONArray(initializeProcessBuilder.getSuccessResultString());
		if(rewardsData.length() == 0) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Stake Key가 Blockchain에 등록되지 않았습니다. 키 생성 후 다시 시도하세요.", "M00002"), true);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("(메인 메뉴 -> 3번 -> 1번 선택)", "M00003"), true);
			rewardsWithdrawDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
			result.setSuccess(false);
			return;
		}
		
		long rewardAccountBalance = rewardsData.getJSONObject(0).getLong("rewardAccountBalance");
		MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("현재 보상량 : %s" + NodeConstants.POSTFIX_LOVELACE, "M00004", String.valueOf(rewardAccountBalance)), true);
		if(rewardAccountBalance == 0) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("인출할 수 있는 보상이 없습니다.", "M00005"), true);
			rewardsWithdrawDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
			result.setSuccess(false);
			return;
		}
		rewardsWithdrawDomain.setWithdrawableAmount(rewardAccountBalance);
		
		/**
		 * 현재 풀 주소에서 ADA TxHash 추출
		 */
		String cardanoKeysPaymentAddressPathString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.payment.addr");
		String paymentAddressString = CommandExecutor.readFile(cardanoKeysPaymentAddressPathString);
		command = CommandExecutor.generateCommand(NodeCommandFormats.PAYMENT_ADDRESS_BALANCE_CHECK, cardanoCliName, paymentAddressString);
		initializeProcessBuilder = CommandExecutor.initializeProcessBuilder(command);
		MessagePrompter.promptMessage(initializeProcessBuilder.getSuccessResultString(), true);
		
		List<Map<String,String>> parseTxHashList = CommandExecutor.parseTxHashString(initializeProcessBuilder.getSuccessResultString());
		if(parseTxHashList.size()==0) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("TxHash가 존재하지 않습니다.", "M00006"), true);
			MessagePrompter.promptMessage("", true);
			rewardsWithdrawDomain.setNextOrder(this.getOrder() - 1);
			result.setResponseData(rewardsWithdrawDomain);
			result.setSuccess(false);
			return;
		}
		
		rewardsWithdrawDomain.setSenderAddress(paymentAddressString);
		rewardsWithdrawDomain.setReceiveAddress(paymentAddressString);
		rewardsWithdrawDomain.setStakeAddress(stakeAddressString);
		rewardsWithdrawDomain.setParseTxHashList(parseTxHashList);
		rewardsWithdrawDomain.setNextOrder(this.getOrder() + 1);
		result.setResponseData(rewardsWithdrawDomain);
		result.setSuccess(true);
	}
}