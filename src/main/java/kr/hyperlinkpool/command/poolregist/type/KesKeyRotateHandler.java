package kr.hyperlinkpool.command.poolregist.type;

import java.io.File;
import java.util.Map;

import org.json.JSONObject;

import kr.hyperlinkpool.command.AbstractCommandHandler;
import kr.hyperlinkpool.command.NodeCommandFormats;
import kr.hyperlinkpool.constants.NodeConstants;
import kr.hyperlinkpool.domain.ProcessResponse;
import kr.hyperlinkpool.domain.ProcessResultDomain;
import kr.hyperlinkpool.i18n.MessageFactory;
import kr.hyperlinkpool.properties.NodeProperties;
import kr.hyperlinkpool.utils.CommandExecutor;
import kr.hyperlinkpool.utils.CommandListener;
import kr.hyperlinkpool.utils.HttpUtils;
import kr.hyperlinkpool.utils.MessagePrompter;

public class KesKeyRotateHandler extends AbstractCommandHandler{

	@Override
	public ProcessResultDomain<ProcessResponse> handleCommand(Object... commands) {
		ProcessResultDomain<ProcessResponse> processResultDomain = new ProcessResultDomain<>();
		processResultDomain.setSuccess(false);
		
		boolean isPreceedNotCoreNode = false;
		
		/**
		 * KES Periods 확인
		 */
		String cardanoCoreNodeMetricsUrl=NodeProperties.getString("cardano.core.node.metrics.url");
		Map<String, String> cardanoCoreNodeMetricsInfo = HttpUtils.readNodeMeticsInfo(cardanoCoreNodeMetricsUrl);
		int operationalCertificateStartKesPeriod = 0;
		int operationalCertificateExpiryKesPeriod = 0;
		int currentKesPeriod = 0;
		int remainingKesPeriods = 0;
		try {
			operationalCertificateStartKesPeriod = Integer.parseInt(cardanoCoreNodeMetricsInfo.get(NodeConstants.CARDANO_NODE_FORGE_METRICS_OPERATIONAL_CERTIFICATE_START_KES_PERIOD_INT_KEY));
			operationalCertificateExpiryKesPeriod = Integer.parseInt(cardanoCoreNodeMetricsInfo.get(NodeConstants.CARDANO_NODE_FORGE_METRICS_OPERATIONAL_CERTIFICATE_EXPIRY_KES_PERIOD_INT_KEY));
			currentKesPeriod = Integer.parseInt(cardanoCoreNodeMetricsInfo.get(NodeConstants.CARDANO_NODE_FORGE_METRICS_CURRENT_KES_PERIOD_INT_KEY));
			remainingKesPeriods = Integer.parseInt(cardanoCoreNodeMetricsInfo.get(NodeConstants.CARDANO_NODE_FORGE_METRICS_REMAINING_KES_PERIODS_INT_KEY));
		} catch (Exception e) {
			MessagePrompter.promptMessage("", true);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Block Producer가 아닙니다. Block Producer가 아닐 경우, 관련 Key(KES singing key, Cold signing key, Cold Counter key)가 있다면 KES Roation을 진행할 수 있습니다.", "M00175"), true);
			String isPreceedNotCoreNodeCommand = CommandListener.getInstance().listenCommand(MessageFactory.getInstance().getMessage("현재 Node에서 KES Rotation을 진행하시겠습니까? (Y/n) : ", "M00176"), false);
			if("Y".equalsIgnoreCase(isPreceedNotCoreNodeCommand)) {
				isPreceedNotCoreNode = true;
			}else{
				processResultDomain.setSuccess(false);
				return processResultDomain;
			}
		}
		
		String listenCommand = null;
		if(isPreceedNotCoreNode) {
			listenCommand = "Y"; 
		}else {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("* KES Key는 Block에 서명하기 위해 필요합니다. Pool 운영자는 KES Key Periods가 만료되기 전에 KES Key를 갱신해야 합니다.", "M00117"), true);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("* KES Key를 갱신하지 않으면, 만료되는 Epoch 이후에 생성되는 Block에 서명하지 못하게 되므로, 반드시 만료 Periods가 도래하면 갱신하시기 바랍니다.", "M00118"), true);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("------------------------------ KES Key 정보 ------------------------------ ", "M00119"), true);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("- KES Key를 생성한 Periods : ", "M00120") + operationalCertificateStartKesPeriod, true);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("- KES Key가 만료되는 Periods : ", "M00121") + operationalCertificateExpiryKesPeriod, true);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("- KES Key 현재 Periods : ", "M00122") + currentKesPeriod, true);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("- KES Key 잔여 Periods : ", "M00123") + remainingKesPeriods, true);
			/**
			 * 잔여 기간 10% 미만 체크
			 */
			int calcRemainEpocePercent = (remainingKesPeriods / (operationalCertificateExpiryKesPeriod - operationalCertificateStartKesPeriod))*100;
			int remainKesPercents = 0;
			if( calcRemainEpocePercent < 10) {
				remainKesPercents = calcRemainEpocePercent;
			}
			
			/**
			 * 잔여 기간 30% 미만 체크
			 */
			if( calcRemainEpocePercent < 30) {
				remainKesPercents = calcRemainEpocePercent;
			}
			
			/**
			 * 잔여 기간 50% 미만 체크
			 */
			if( calcRemainEpocePercent < 50) {
				remainKesPercents = calcRemainEpocePercent;
			}
			
			if(remainKesPercents != 0) {
				MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("- KES Key 잔여 Periods가 약 %s %% 남았습니다.", "M00124", String.valueOf(remainKesPercents)), true);
			}
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("------------------------------ KES Key 정보 ------------------------------ ", "M00119"), true);
			listenCommand = CommandListener.getInstance().listenCommand(MessageFactory.getInstance().getMessage("KES Key Rotation을 진행하시겠습니까?.(Y/n) : ", "M00125"), false);
		}
		
		if("Y".equalsIgnoreCase(listenCommand)) {
			String cardanoKeysFolderString = NodeProperties.getString("cardano.keys.folder");
			String cardanoKeysColdSkeyString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.cold.skey");
			String cardanoKeysColdCounterString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.cold.counter");
			String cardanoKeysNewKesVkeyString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.kes.vkey");
			String cardanoKeysNewKesSkeyString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.kes.skey");
			String cardanoKeysNewNodeCertOutputFilePathFilePath = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.node.cert");
					
			File cardanoKeysFolder = new File(cardanoKeysFolderString);
			if(!cardanoKeysFolder.exists()) {
				cardanoKeysFolder.mkdirs();
			}
			
			File cardanoKeysColdSkeyFile = new File(cardanoKeysColdSkeyString);
			File cardanoKeysColdCounterFile = new File(cardanoKeysColdCounterString);
			if(!cardanoKeysColdSkeyFile.exists()) {
				MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Cold Signing Key가 생성되지 않습니다. 키 생성 후 다시 시도하세요.", "M00044"), true);
				processResultDomain.setSuccess(false);
				return processResultDomain;
			}
			
			if(!cardanoKeysColdCounterFile.exists()) {
				MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Cold Counter가 생성되지 않습니다. 키 생성 후 다시 시도하세요.", "M00045"), true);
				processResultDomain.setSuccess(false);
				return processResultDomain;
			}
			
			File cardanoKeysNewKesVkeyFile = new File(cardanoKeysNewKesVkeyString);
			File cardanoKeysNewKesSkeyFile = new File(cardanoKeysNewKesSkeyString);
			
			if(cardanoKeysNewKesVkeyFile.exists() || cardanoKeysNewKesSkeyFile.exists()) {
				cardanoKeysNewKesVkeyFile.delete();
				cardanoKeysNewKesSkeyFile.delete();
				MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("기존의 KES Verification Key, KES, Signing Key를 삭제했습니다.", "M00126"), true);
			}
			
			String command = null;
			String cardanoCliName = NodeProperties.getString("cardano.cli.name");
			command = CommandExecutor.generateCommand(NodeCommandFormats.POOL_REGISTRATION_GENERATE_KES_KEY_PAIR, cardanoCliName, cardanoKeysNewKesVkeyString, cardanoKeysNewKesSkeyString);
			
			ProcessResponse processResponse = CommandExecutor.initializeProcessBuilder(command);
			processResultDomain.setResponseData(processResponse);
			if(processResponse.getFailureResultString() != null && processResponse.getFailureResultString().length() > 0) {
				processResultDomain.setSuccess(false);
				MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("프로세스 수행중 오류가 발생했습니다. : ", "M00010") + processResponse.getFailureResultString(), true);
				return processResultDomain;
			}
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("신규 KES Verification Key, KES Signing Key가 생성되었습니다.", "M00127"), true);
			
			String mainnetShelleyGenesisJsonPath = NodeProperties.getString("mainnet.shelley.genesis.json.path");
			File mainnetShelleyGenesisJsonFile = new File(mainnetShelleyGenesisJsonPath);
			if(!mainnetShelleyGenesisJsonFile.exists()) {
				MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("mainnet-shelley-genesis.json 파일이 존재하지 않습니다. 확인 후 다시 시도하세요.", "M00046"), true);
				processResultDomain.setSuccess(false);
				return processResultDomain;
			}
			
			String mainnetShelleyGenesisFileContents = CommandExecutor.readFile(mainnetShelleyGenesisJsonFile);
			JSONObject mainnetShelleyGenesisFileJsonObject = new JSONObject(mainnetShelleyGenesisFileContents);
			int slotsPerKESPeriod = mainnetShelleyGenesisFileJsonObject.getInt("slotsPerKESPeriod");
			
			String mainnetCurrentTipResultString = CommandExecutor.mainnetCurrentTip();
			JSONObject mainnetCurrentTipJsonObject = new JSONObject(mainnetCurrentTipResultString);
			int slotNo = mainnetCurrentTipJsonObject.getInt("slotNo");
			/**
			 * 1 Epoch를 빼주라고 한다.
			 */
			int resultKesPeriod = slotNo / slotsPerKESPeriod - 1;
			
			command = CommandExecutor.generateCommand(NodeCommandFormats.POOL_REGISTRATION_GENERATE_CERTIFICATE, cardanoCliName, cardanoKeysNewKesVkeyString, cardanoKeysColdSkeyString, cardanoKeysColdCounterString, String.valueOf(resultKesPeriod), cardanoKeysNewNodeCertOutputFilePathFilePath);
			processResponse = CommandExecutor.initializeProcessBuilder(command);
			processResultDomain.setResponseData(processResponse);
			if(processResponse.getFailureResultString() != null && processResponse.getFailureResultString().length() > 0) {
				processResultDomain.setSuccess(false);
				MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("프로세스 수행중 오류가 발생했습니다. : ", "M00010") + processResponse.getFailureResultString(), true);
				return processResultDomain;
			}
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("신규 KES Period가 적용된 Node Certificate파일이 생성되었습니다.", "M00128"), true);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("신규 KES Signing Key와 Node Certificate파일을 Block Producer노드 Config 경로에 복사한 후 Block Producer를 재시작 해주세요.", "M00129"), true);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("신규 KES Signing Key : ", "M00130") + cardanoKeysNewKesSkeyString, true);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("신규 Node Certificate : ", "M00131") + cardanoKeysNewNodeCertOutputFilePathFilePath, true);
			processResponse.setSuccessResultString(MessageFactory.getInstance().getMessage("정상 처리되었습니다.", "M00042"));
			processResultDomain.setSuccess(true);
		}
		return processResultDomain;
	}
	
}
