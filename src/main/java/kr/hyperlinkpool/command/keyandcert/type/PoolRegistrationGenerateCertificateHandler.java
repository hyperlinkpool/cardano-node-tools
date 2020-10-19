package kr.hyperlinkpool.command.keyandcert.type;

import java.io.File;

import org.json.JSONObject;

import kr.hyperlinkpool.command.AbstractCommandHandler;
import kr.hyperlinkpool.command.NodeCommandFormats;
import kr.hyperlinkpool.constants.NodeConstants;
import kr.hyperlinkpool.domain.ProcessResponse;
import kr.hyperlinkpool.domain.ProcessResultDomain;
import kr.hyperlinkpool.i18n.MessageFactory;
import kr.hyperlinkpool.properties.NodeProperties;
import kr.hyperlinkpool.utils.CommandExecutor;
import kr.hyperlinkpool.utils.MessagePrompter;

public class PoolRegistrationGenerateCertificateHandler extends AbstractCommandHandler{
	
	@Override
	public ProcessResultDomain <ProcessResponse> handleCommand(Object ... commands) {
		ProcessResultDomain<ProcessResponse> processResultDomain = new ProcessResultDomain<>();
		
		String cardanoKeysFolderString = NodeProperties.getString("cardano.keys.folder");
		String cardanoKeysKesVkeyString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.kes.vkey");
		String cardanoKeysColdSkeyString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.cold.skey");
		String cardanoKeysColdCounterString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.cold.counter");
		String cardanoKeysNodeCertOutputFilePathFilePath = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.node.cert");
		
		File cardanoKeysFolder = new File(cardanoKeysFolderString);
		if(!cardanoKeysFolder.exists()) {
			cardanoKeysFolder.mkdirs();
		}
		
		File cardanoKeysKesVkeyFile = new File(cardanoKeysKesVkeyString);
		File cardanoKeysColdSkeyFile = new File(cardanoKeysColdSkeyString);
		File cardanoKeysColdCounterFile = new File(cardanoKeysColdCounterString);
		
		if(!cardanoKeysKesVkeyFile.exists()) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("KES Verification Key가 생성되지 않았습니다. 키 생성 후 다시 시도하세요.", "M00043"), true);
			processResultDomain.setSuccess(false);
			return processResultDomain;
		}
		
		if(!cardanoKeysColdSkeyFile.exists()) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Cold Signing Key가 생성되지 않았습니다. 키 생성 후 다시 시도하세요.", "M00044"), true);
			processResultDomain.setSuccess(false);
			return processResultDomain;
		}
		
		if(!cardanoKeysColdCounterFile.exists()) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Cold Counter가 생성되지 않았습니다. 키 생성 후 다시 시도하세요.", "M00045"), true);
			processResultDomain.setSuccess(false);
			return processResultDomain;
		}
		
		File cardanoKeysNodeCertOutputFilePathFile = new File(cardanoKeysNodeCertOutputFilePathFilePath);
		if(cardanoKeysNodeCertOutputFilePathFile.exists()) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("이미 파일이 존재합니다. 삭제 후 다시 시도하세요.", "M00039"), true);
			processResultDomain.setSuccess(false);
			return processResultDomain;
		}
		
		String mainnetShelleyGenesisJsonPath = NodeProperties.getString("mainnet.shelley.genesis.json.path");
		File mainnetShelleyGenesisJsonFile = new File(mainnetShelleyGenesisJsonPath);
		if(!mainnetShelleyGenesisJsonFile.exists()) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("mainnet-shelley-genesis.json 파일이 존재하지 않습니다. 확인 후 다시 시도하세요.", "M00046"), true);
			return null;
		}
		
		String mainnetShelleyGenesisFileContents = CommandExecutor.readFile(mainnetShelleyGenesisJsonFile);
		JSONObject mainnetShelleyGenesisFileJsonObject = new JSONObject(mainnetShelleyGenesisFileContents);
		int slotsPerKESPeriod = mainnetShelleyGenesisFileJsonObject.getInt("slotsPerKESPeriod");
		
		String mainnetCurrentTipResultString = CommandExecutor.mainnetCurrentTip();
		JSONObject mainnetCurrentTipJsonObject = new JSONObject(mainnetCurrentTipResultString);
		int slotNo = mainnetCurrentTipJsonObject.getInt("slotNo");
		int resultKesPeriod = slotNo / slotsPerKESPeriod;
		
		String command = null;
		String cardanoCliName = NodeProperties.getString("cardano.cli.name");
		command = CommandExecutor.generateCommand(NodeCommandFormats.POOL_REGISTRATION_GENERATE_CERTIFICATE, cardanoCliName, cardanoKeysKesVkeyString, cardanoKeysColdSkeyString, cardanoKeysColdCounterString, String.valueOf(resultKesPeriod), cardanoKeysNodeCertOutputFilePathFilePath);
		ProcessResponse processResponse = CommandExecutor.initializeProcessBuilder(command);
		processResultDomain.setResponseData(processResponse);
		if(processResponse.getFailureResultString() != null && processResponse.getFailureResultString().length() > 0) {
			processResultDomain.setSuccess(false);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("프로세스 수행중 오류가 발생했습니다. : ", "M00010") + processResponse.getFailureResultString(), true);
			return processResultDomain;
		}
		
		processResponse.setSuccessResultString(MessageFactory.getInstance().getMessage("정상 처리되었습니다.", "M00042"));
		processResultDomain.setSuccess(true);
		return processResultDomain;
	}
}
