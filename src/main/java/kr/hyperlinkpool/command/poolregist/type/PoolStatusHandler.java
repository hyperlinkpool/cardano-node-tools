package kr.hyperlinkpool.command.poolregist.type;

import java.io.File;

import kr.hyperlinkpool.command.AbstractCommandHandler;
import kr.hyperlinkpool.command.NodeCommandFormats;
import kr.hyperlinkpool.constants.NodeConstants;
import kr.hyperlinkpool.domain.ProcessResponse;
import kr.hyperlinkpool.domain.ProcessResultDomain;
import kr.hyperlinkpool.i18n.MessageFactory;
import kr.hyperlinkpool.properties.NodeProperties;
import kr.hyperlinkpool.utils.CommandExecutor;
import kr.hyperlinkpool.utils.MessagePrompter;

public class PoolStatusHandler extends AbstractCommandHandler{

	@Override
	public ProcessResultDomain<ProcessResponse> handleCommand(Object... commands) {
		ProcessResultDomain<ProcessResponse> processResultDomain = new ProcessResultDomain<ProcessResponse>();
		String command = null;
		String cardanoKeysFolderString = NodeProperties.getString("cardano.keys.folder");
		String cardanoKeysColdVkeyFileString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.cold.vkey");
		File cardanoKeysColdVkeyFile = new File(cardanoKeysColdVkeyFileString);
		if(!cardanoKeysColdVkeyFile.exists()) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Cold Verification Key가 생성되지 않았습니다. 키 생성 후 다시 시도하세요.", "M00061"), true);
			processResultDomain.setSuccess(false);
			return processResultDomain;
		}
		
		String cardanoCliName = NodeProperties.getString("cardano.cli.name");
		command = CommandExecutor.generateCommand(NodeCommandFormats.POOL_REGISTRATION_GET_POOL_ID, cardanoCliName, cardanoKeysColdVkeyFileString);
		ProcessResponse processBuilder = CommandExecutor.initializeProcessBuilder(command);
		String failureResultString = processBuilder.getFailureResultString();
		if(failureResultString != null && failureResultString.length() > 0){
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Pool Id 확인중 에러가 발생했습니다.", "M00084"), true);
			processResultDomain.setSuccess(false);
			return processResultDomain;
		}
		
		String poolId = processBuilder.getSuccessResultString().trim();
		String cardanoCliNameFullPath = NodeProperties.getString("cardano.cli.path") + NodeConstants.PATH_DELIMITER + cardanoCliName;
		command = CommandExecutor.generateCommand(NodeCommandFormats.POOL_REGISTRATION_YOUR_POOLID_IN_THE_NETWORK_LEDGER_STATE, cardanoCliNameFullPath, poolId);
		MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Pool 정보 확인 중입니다. 좀 오래 걸려요.", "M00085"), true);
		processBuilder = CommandExecutor.initializeProcessBuilderWithBash(command);
		failureResultString = processBuilder.getFailureResultString();
		if(failureResultString != null && failureResultString.length() > 0){
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Blockchain 확인중 에러가 발생했습니다.", "M00086"), true);
			processResultDomain.setSuccess(false);
			return processResultDomain;
		}
		
		String successResultString = processBuilder.getSuccessResultString();
		if(successResultString != null && successResultString.length() > 0) {
			processBuilder.setSuccessResultString(MessageFactory.getInstance().getMessage("Pool 정보가 확인되었습니다. 정상 상태입니다.", "M00132"));
		}else {
			processBuilder.setSuccessResultString(MessageFactory.getInstance().getMessage("Pool 등록 정보를 찾을 수 없습니다.", "M00133"));
		}
		
		processResultDomain.setResponseData(processBuilder);
		processResultDomain.setSuccess(true);
		return processResultDomain;
	}

}
