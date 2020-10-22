package kr.hyperlinkpool.command.poolregist.type.deregisterpoolstep;

import java.io.File;
import java.util.List;
import java.util.Map;

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
import kr.hyperlinkpool.utils.MessagePrompter;

public class Step1 implements DeRegisterPoolResult, Ordered, JobProcess{

	private DeRegisterPoolDomain deRegisterPoolDomain;
	
	private ProcessResultDomain<DeRegisterPoolDomain> result;
	
	@Override
	public int getOrder() {
		return StepOrder.STEP1.getStepOrder();
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
		 * Step. 1
		 * validation
		 */
		String command = null;
		String cardanoKeysFolderString = NodeProperties.getString("cardano.keys.folder");
		String cardanoCliName = NodeProperties.getString("cardano.cli.name");
		
		/**
		 * Payment Address 검증
		 */
		String cardanoKeysPaymentAddressString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.payment.addr");
		File cardanoKeysPaymentAddressFile = new File(cardanoKeysPaymentAddressString);
		if(!cardanoKeysPaymentAddressFile.exists()) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Payment 주소가 존재하지 않습니다. Payment 주소 생성 후 다시 시도하세요.", "M00025"), true);
			deRegisterPoolDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
			result.setSuccess(false);
			return;
		}
		
		/**
		 * cold.vkey 검사
		 */
		String cardanoKeysColdVkeyFileString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.cold.vkey");
		File cardanoKeysColdVkeyFile = new File(cardanoKeysColdVkeyFileString);
		if(!cardanoKeysColdVkeyFile.exists()) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Cold Verification Key가 생성되지 않았습니다. 키 생성 후 다시 시도하세요.", "M00061"), true);
			deRegisterPoolDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
			result.setSuccess(false);
			return;
		}
		
		/**
		 * cold.skey 검사
		 */
		String cardanoKeysColdSkeyFileString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.cold.skey");
		File cardanoKeysColdSkeyFile = new File(cardanoKeysColdSkeyFileString);
		if(!cardanoKeysColdSkeyFile.exists()) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Cold Signing Key가 생성되지 않았습니다. 키 생성 후 다시 시도하세요.", "M00061"), true);
			deRegisterPoolDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
			result.setSuccess(false);
			return;
		}
		
		/**
		 * Shelley Genesis 파일 검사
		 */
		String mainnetShelleyGenesisJsonPath = NodeProperties.getString("mainnet.shelley.genesis.json.path");
		File mainnetShelleyGenesisJsonFile = new File(mainnetShelleyGenesisJsonPath);
		if(!mainnetShelleyGenesisJsonFile.exists()) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("mainnet-shelley-genesis.json 파일이 존재하지 않습니다. 확인 후 다시 시도하세요.", "M00046"), true);
			deRegisterPoolDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
			result.setSuccess(false);
			return;
		}
		
		/**
		 * Pool 등록 상태 조회
		 */
		command = CommandExecutor.generateCommand(NodeCommandFormats.POOL_REGISTRATION_GET_POOL_ID, cardanoCliName, cardanoKeysColdVkeyFileString);
		ProcessResponse processBuilder = CommandExecutor.initializeProcessBuilder(command);
		String failureResultString = processBuilder.getFailureResultString();
		if(failureResultString != null && failureResultString.length() > 0){
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Pool Id 확인중 에러가 발생했습니다.", "M00084"), true);
			deRegisterPoolDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
			result.setSuccess(false);
			return;
		}
		
		String poolId = processBuilder.getSuccessResultString().trim();
		String cardanoCliNameFullPath = NodeProperties.getString("cardano.cli.path") + NodeConstants.PATH_DELIMITER + cardanoCliName;
		command = CommandExecutor.generateCommand(NodeCommandFormats.POOL_REGISTRATION_YOUR_POOLID_IN_THE_NETWORK_LEDGER_STATE, cardanoCliNameFullPath, poolId);
		MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Pool 정보 확인 중입니다. 좀 오래 걸려요.", "M00085"), true);
		processBuilder = CommandExecutor.initializeProcessBuilderWithBash(command);
		failureResultString = processBuilder.getFailureResultString();
		if(failureResultString != null && failureResultString.length() > 0){
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Blockchain 확인중 에러가 발생했습니다.", "M00086"), true);
			deRegisterPoolDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
			result.setSuccess(false);
			return;
		}
		
		/**
		 * 등록된 Pool이 아닐 경우 철회 불가
		 */
		String successResultString = processBuilder.getSuccessResultString();
		if(successResultString != null && successResultString.length() == 0){
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Pool 등록 정보를 찾을 수 없습니다.", "M00133"), true);
			deRegisterPoolDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
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
		List<Map<String,String>> parseTxHashList = CommandExecutor.parseTxHashString(initializeProcessBuilder.getSuccessResultString());
		if(parseTxHashList.size()==0) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("인출할 ADA가 없습니다.", "M00027"), true);
			MessagePrompter.promptMessage("", true);
			deRegisterPoolDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
			result.setResponseData(deRegisterPoolDomain);
			result.setSuccess(false);
			return;
		}
		
		deRegisterPoolDomain.setNextOrder(this.getOrder() + 1);
		result.setResponseData(deRegisterPoolDomain);
		result.setSuccess(true);
	}
}
