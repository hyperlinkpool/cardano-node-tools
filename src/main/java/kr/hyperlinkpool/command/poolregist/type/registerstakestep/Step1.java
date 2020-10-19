package kr.hyperlinkpool.command.poolregist.type.registerstakestep;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

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
import kr.hyperlinkpool.utils.MessagePrompter;

public class Step1 implements RegisterStakeResult, Ordered, Runnable{

	private RegisterStakeDomain registerStakeDomain;
	
	private ProcessResultDomain<RegisterStakeDomain> result;
	
	@Override
	public int getOrder() {
		return StepOrder.STEP1.getStepOrder();
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
	
	public RegisterStakeDomain getWithdrawDomain() {
		return result.getResponseData();
	}

	public void setWithdrawDomain(RegisterStakeDomain registerStakeDomain) {
		this.registerStakeDomain = registerStakeDomain;
	}
	
	@Override
	public void run() {
		/**
		 * Step. 1
		 * validation
		 */
		String command = null;
		
		/**
		 * Stake Verification Key 생성 여부 점검
		 */
		String cardanoKeysFolderString = NodeProperties.getString("cardano.keys.folder");
		String cardanoKeysStakeVkeyString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.stake.vkey");
		File cardanoKeysStakeVkeyFile = new File(cardanoKeysStakeVkeyString);
		if(!cardanoKeysStakeVkeyFile.exists()) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Stake Verification Key가 생성되지 않았습니다. 키 파일 생성 후 시도하세요.", "M00096"), true);
			return;
		}
		
		/**
		 * Stake Certificate 생성 여부 점검
		 */
		String cardanoKeysStakeCertString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.stake.cert");
		File cardanoKeysStakeCertFile = new File(cardanoKeysStakeCertString);
		if(!cardanoKeysStakeCertFile.exists()) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Stake Certificate가 생성되지 않았습니다. 인증서 생성 후 시도하세요.", "M00097"), true);
			return;
		}
		
		/**
		 * Stake Address 검증
		 */
		String cardanoCliName = NodeProperties.getString("cardano.cli.name");
		String cardanoKeysPaymentAddressString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.payment.addr");
		File cardanoKeysPaymentAddressFile = new File(cardanoKeysPaymentAddressString);
		if(!cardanoKeysPaymentAddressFile.exists()) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Payment 주소가 존재하지 않습니다. Payment 주소 생성 후 다시 시도하세요.", "M00025"), true);
			return;
		}
		
		/**
		 * Stake Key 등록 여부 검증
		 */
		String cardanoKeysStakeAddressString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.stake.addr");
		File cardanoKeysStakeAddressFile = new File(cardanoKeysStakeAddressString);
		if(!cardanoKeysStakeAddressFile.exists()) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Stake 주소가 생성되지 않았습니다. 주소 생성 후 다시 시도하세요.", "M00001"), true);
			return;
		}
		String stakeAddressString = CommandExecutor.readFile(cardanoKeysStakeAddressFile);
		command = CommandExecutor.generateCommand(NodeCommandFormats.STAKE_ADDRESS_BALANCE_CHECK, cardanoCliName, stakeAddressString);
		ProcessResponse processResponse = CommandExecutor.initializeProcessBuilder(command);
		
		JSONArray rewardsData = new JSONArray(processResponse.getSuccessResultString());
		if(rewardsData.length() > 0) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("이미 Stake Key가 등록되어 있습니다.", "M00098"), true);
			CommandExecutor.pringStakePoolStatus(rewardsData);
			registerStakeDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
			result.setResponseData(registerStakeDomain);
			result.setSuccess(false);
			return;
		}
		
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
			registerStakeDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
			result.setResponseData(registerStakeDomain);
			result.setSuccess(false);
			return;
		}
		
		registerStakeDomain.setParseTxHashList(parseTxHashList);
		registerStakeDomain.setNextOrder(this.getOrder() + 1);
		registerStakeDomain.setSenderAddress(paymentAddressString);
		registerStakeDomain.setReceiveAddress(paymentAddressString);
		result.setResponseData(registerStakeDomain);
		result.setSuccess(true);
	}
}
