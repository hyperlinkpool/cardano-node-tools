package kr.hyperlinkpool.command.poolregist.type.deregisterpoolstep;

import java.io.File;
import java.util.Map;

import org.json.JSONObject;

import kr.hyperlinkpool.command.NodeCommandFormats;
import kr.hyperlinkpool.command.interfaces.Ordered;
import kr.hyperlinkpool.command.poolregist.type.deregisterpoolstep.domains.DeRegisterPoolDomain;
import kr.hyperlinkpool.command.poolregist.type.deregisterpoolstep.interfaces.DeRegisterPoolResult;
import kr.hyperlinkpool.constants.NodeConstants;
import kr.hyperlinkpool.constants.StepOrder;
import kr.hyperlinkpool.domain.ProcessResultDomain;
import kr.hyperlinkpool.i18n.MessageFactory;
import kr.hyperlinkpool.interfaces.JobProcess;
import kr.hyperlinkpool.properties.NodeProperties;
import kr.hyperlinkpool.utils.CommandExecutor;
import kr.hyperlinkpool.utils.CommandListener;
import kr.hyperlinkpool.utils.HttpUtils;
import kr.hyperlinkpool.utils.MessagePrompter;

public class Step2 implements DeRegisterPoolResult, Ordered, JobProcess{

	private DeRegisterPoolDomain deRegisterPoolDomain;
	
	private ProcessResultDomain<DeRegisterPoolDomain> result;
	
	@Override
	public int getOrder() {
		return StepOrder.STEP2.getStepOrder();
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
		 * protocol.json 생성
		 */
		String command = null;
		String cardanoKeysFolderString = NodeProperties.getString("cardano.keys.folder");
		String cardanoCliName = NodeProperties.getString("cardano.cli.name");
 		String cardanoKeysProtocolJsonString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.protocol.json");
		File cardanoKeysProtocolJsonFile = new File(cardanoKeysProtocolJsonString);
		if(cardanoKeysProtocolJsonFile.exists()) {
			cardanoKeysProtocolJsonFile.delete();
		}
		command = CommandExecutor.generateCommand(NodeCommandFormats.GENERATE_PROTOCOL_FILE, cardanoCliName, cardanoKeysProtocolJsonString);
		CommandExecutor.initializeProcessBuilder(command);
		
		/**
		 * Epoch length 조회
		 */
		/**
		 * Shelley Genesis 파일 조회
		 */
		String mainnetShelleyGenesisJsonPath = NodeProperties.getString("mainnet.shelley.genesis.json.path");
		String mainnetShelleyGenesisJsonFileString = CommandExecutor.readFile(mainnetShelleyGenesisJsonPath);
		JSONObject mainnetShelleyGenesisJsonObject = new JSONObject(mainnetShelleyGenesisJsonFileString);
		long epochLength = mainnetShelleyGenesisJsonObject.getLong("epochLength");
		deRegisterPoolDomain.setEpochLength(epochLength);
		
		/**
		 * Mainnet Tip 조회, Shelley가 시작되고 지난 Epoch, 전체 Epoch와 차이가 있다.
		 * 가이드가 잘못된것 같다.
		 */
//		String mainnetCurrentTip = CommandExecutor.mainnetCurrentTip();
//		JSONObject mainnetCurrentTipJsonObject = new JSONObject(mainnetCurrentTip);
//		int slotNo = mainnetCurrentTipJsonObject.getInt("slot");
//		long currentEpoch = slotNo / epochLength;
		
		/**
		 * eMax 조회
		 */
		String cardanoKeysProtocolJsonFileString = CommandExecutor.readFile(cardanoKeysProtocolJsonString);
		JSONObject cardanoKeysProtocolJsonObject = new JSONObject(cardanoKeysProtocolJsonFileString);
		int eMax = cardanoKeysProtocolJsonObject.getInt("eMax");
		deRegisterPoolDomain.seteMax(eMax);
		
		/**
		 * 은퇴할 Epoch입력
		 */
		/**
		 * 현재 Epoch확인
		 */
		String cardanoCoreNodeMetricsUrl=NodeProperties.getString("cardano.core.node.metrics.url");
		Map<String, String> cardanoCoreNodeMetricsInfo = HttpUtils.readNodeMeticsInfo(cardanoCoreNodeMetricsUrl);
		int cardanoNodeChaindbMetricsEpochIntKey = Integer.parseInt(cardanoCoreNodeMetricsInfo.get(NodeConstants.CARDANO_NODE_CHAINDB_METRICS_EPOCH_INT_KEY));
		deRegisterPoolDomain.setCurrentEpoch(cardanoNodeChaindbMetricsEpochIntKey);
		deRegisterPoolDomain.setTotalEpoch(cardanoNodeChaindbMetricsEpochIntKey);
		int canInputStartEpoch = cardanoNodeChaindbMetricsEpochIntKey + 1;
		int canInputEndEpoch = cardanoNodeChaindbMetricsEpochIntKey + eMax;
		boolean loop = true;
		while(loop) {
			String listenCommand = CommandListener.getInstance().listenCommand(MessageFactory.getInstance().getMessage("은퇴할 Epoch를 입력해 주세요.(입력 가능 범위 : %s ~ %s, 현재 Epoch : %s), [Q : 취소] : ", "M00167", String.valueOf(canInputStartEpoch), String.valueOf(canInputEndEpoch),  String.valueOf(cardanoNodeChaindbMetricsEpochIntKey)), false);
			if("Q".equalsIgnoreCase(listenCommand)) {
				MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("취소되었습니다.", "M00020"), true);
				deRegisterPoolDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
				result.setSuccess(false);
				loop = false;
				return;
			}
			
			long inputValue = 0;
			try {
				inputValue = Long.parseLong(listenCommand);
				if(inputValue < canInputStartEpoch || inputValue > canInputEndEpoch) {
					MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("입력 가능 범위를 초과했습니다. 다시 입력해 주세요.", "M00168"), true);
					continue;
				}
				
				/**
				 * 은퇴할 Epoch는 Shelley가 진행한 Epoch를 기준으로 계산한다.
				 */
				long retireEpoch = inputValue;
				deRegisterPoolDomain.setInputEpoch(inputValue);
				deRegisterPoolDomain.setRetireEpoch(retireEpoch);
				loop = false;
			} catch (Exception e) {
				MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("숫자를 입력해 주세요.", "M00011"), true);
			}
		}
		
		deRegisterPoolDomain.setNextOrder(this.getOrder() + 1);
		result.setResponseData(deRegisterPoolDomain);
		result.setSuccess(true);
	}
}