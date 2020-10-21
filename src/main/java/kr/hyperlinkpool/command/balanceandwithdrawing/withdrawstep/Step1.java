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
import kr.hyperlinkpool.utils.MessagePrompter;

public class Step1 implements WithdrawResult, Ordered, JobProcess{

	private WithdrawDomain withdrawDomain;
	
	private ProcessResultDomain<WithdrawDomain> result;
	
	@Override
	public int getOrder() {
		return StepOrder.STEP1.getStepOrder();
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
		return result.getResponseData();
	}

	public void setWithdrawDomain(WithdrawDomain withdrawDomain) {
		this.withdrawDomain = withdrawDomain;
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
		 * 현재 풀 주소에서 ADA TxHash 추출
		 */
		String cardanoKeysPaymentAddressPathString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.payment.addr");
		File cardanoKeysPaymentAddressFile = new File(cardanoKeysPaymentAddressPathString);
		if(!cardanoKeysPaymentAddressFile.exists()) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Payment 주소가 존재하지 않습니다. Payment 주소 생성 후 다시 시도하세요.", "M00025"), true);
			withdrawDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
			result.setResponseData(withdrawDomain);
			result.setSuccess(false);
			return;
		}
		
		String paymentAddressString = CommandExecutor.readFile(cardanoKeysPaymentAddressPathString);
		command = CommandExecutor.generateCommand(NodeCommandFormats.PAYMENT_ADDRESS_BALANCE_CHECK, cardanoCliName, paymentAddressString);
		ProcessResponse initializeProcessBuilder = CommandExecutor.initializeProcessBuilder(command);
		MessagePrompter.promptMessage(initializeProcessBuilder.getSuccessResultString(), true);
		
		List<Map<String,String>> parseTxHashList = CommandExecutor.parseTxHashString(initializeProcessBuilder.getSuccessResultString());
		if(parseTxHashList.size()==0) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("인출할 ADA가 없습니다.", "M00027"), true);
			MessagePrompter.promptMessage("", true);
			withdrawDomain.setNextOrder(this.getOrder() - 1);
			result.setResponseData(withdrawDomain);
			result.setSuccess(false);
			return;
		}
		
		withdrawDomain.setParseTxHashList(parseTxHashList);
		withdrawDomain.setNextOrder(this.getOrder() + 1);
		result.setResponseData(withdrawDomain);
		result.setSuccess(true);
	}
}