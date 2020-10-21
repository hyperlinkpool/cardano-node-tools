package kr.hyperlinkpool.command.poolregist.type.deregisterstakestep;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.hyperlinkpool.command.NodeCommandFormats;
import kr.hyperlinkpool.command.interfaces.Ordered;
import kr.hyperlinkpool.command.poolregist.type.deregisterstakestep.domains.DeRegisterStakeAddressDomain;
import kr.hyperlinkpool.command.poolregist.type.deregisterstakestep.interfaces.DeRegisterStakeAddressResult;
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

public class Step1 implements DeRegisterStakeAddressResult, Ordered, JobProcess{

	private DeRegisterStakeAddressDomain deRegisterStakeAddressDomain;
	
	private ProcessResultDomain<DeRegisterStakeAddressDomain> result;
	
	@Override
	public int getOrder() {
		return StepOrder.STEP1.getStepOrder();
	}

	@Override
	public ProcessResultDomain<DeRegisterStakeAddressDomain> getResult() {
		return result;
	}

	@Override
	public void setResult(ProcessResultDomain<DeRegisterStakeAddressDomain> result) {
		this.result = result;
		this.deRegisterStakeAddressDomain = result.getResponseData();
	}
	
	public DeRegisterStakeAddressDomain getDeRegisterStakeAddressDomain() {
		return result.getResponseData();
	}

	public void setDeRegisterStakeAddressDomain(DeRegisterStakeAddressDomain deRegisterStakeAddressDomain) {
		this.deRegisterStakeAddressDomain = deRegisterStakeAddressDomain;
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
		 * Payment Address 검증
		 */
		String cardanoCliName = NodeProperties.getString("cardano.cli.name");
		String cardanoKeysPaymentAddressString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.payment.addr");
		File cardanoKeysPaymentAddressFile = new File(cardanoKeysPaymentAddressString);
		if(!cardanoKeysPaymentAddressFile.exists()) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Payment 주소가 존재하지 않습니다. Payment 주소 생성 후 다시 시도하세요.", "M00025"), true);
			return;
		}
		
		/**
		 * Stake Address 검증
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
		
		/**
		 * Stake Key 검증
		 */
		JSONArray rewardsData = new JSONArray(processResponse.getSuccessResultString());
		if(rewardsData.length() == 0) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Stake Key가 등록되어 있지 않습니다. 등록 이 후에 철회할 수 있습니다.", "M00155"), true);
			deRegisterStakeAddressDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
			result.setResponseData(deRegisterStakeAddressDomain);
			result.setSuccess(false);
			return;
		}
		
		/**
		 * 풀에 위임된 상태이면, 풀 등록 철회 이후에 진행 할 수 있게 방어 
		 */
		JSONObject rewardsDataJSONObject = rewardsData.getJSONObject(0);
		if(!JSONObject.NULL.equals(rewardsDataJSONObject.get("delegation"))){
			String listenCommand = CommandListener.getInstance().listenCommand(MessageFactory.getInstance().getMessage("Pool에 위임되어 있습니다. 철회하면 다시 위임을 해야 합니다. 진행하시겠습니까? (Y/n) : ", "M00159"), false);
			if(!"Y".equalsIgnoreCase(listenCommand)) {
				deRegisterStakeAddressDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
				result.setResponseData(deRegisterStakeAddressDomain);
				result.setSuccess(false);
				return;
			}
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
			deRegisterStakeAddressDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
			result.setResponseData(deRegisterStakeAddressDomain);
			result.setSuccess(false);
			return;
		}
		
		deRegisterStakeAddressDomain.setParseTxHashList(parseTxHashList);
		deRegisterStakeAddressDomain.setNextOrder(this.getOrder() + 1);
		deRegisterStakeAddressDomain.setSenderAddress(paymentAddressString);
		deRegisterStakeAddressDomain.setReceiveAddress(paymentAddressString);
		result.setResponseData(deRegisterStakeAddressDomain);
		result.setSuccess(true);
	}
}
