package kr.hyperlinkpool.command.balanceandwithdrawing.type;

import java.io.File;

import org.json.JSONArray;

import kr.hyperlinkpool.command.AbstractCommandHandler;
import kr.hyperlinkpool.command.NodeCommandFormats;
import kr.hyperlinkpool.constants.NodeConstants;
import kr.hyperlinkpool.domain.ProcessResponse;
import kr.hyperlinkpool.domain.ProcessResultDomain;
import kr.hyperlinkpool.i18n.MessageFactory;
import kr.hyperlinkpool.properties.NodeProperties;
import kr.hyperlinkpool.utils.CommandExecutor;
import kr.hyperlinkpool.utils.MessagePrompter;

public class BalanceCheckStakePoolRewardsHandler extends AbstractCommandHandler{

	@Override
	public ProcessResultDomain<ProcessResponse> handleCommand(Object... commands) {
		ProcessResultDomain<ProcessResponse> processResultDomain = new ProcessResultDomain<>();
		
		String cardanoKeysFolderString = NodeProperties.getString("cardano.keys.folder");
		String cardanoKeysStakeAddressString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.stake.addr");
		
		File cardanoKeysStakeAddressFile = new File(cardanoKeysStakeAddressString);
		if(!cardanoKeysStakeAddressFile.exists()) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Stake 주소가 없습니다. 주소 생성 후 다시 시도하세요.", "M00001"), true);
			processResultDomain.setSuccess(false);
			return processResultDomain;
		}
		
		String cardanoCliName = NodeProperties.getString("cardano.cli.name");
		String stakeAddressString = CommandExecutor.readFile(cardanoKeysStakeAddressFile);
		String command = CommandExecutor.generateCommand(NodeCommandFormats.STAKE_ADDRESS_BALANCE_CHECK, cardanoCliName, stakeAddressString);
		ProcessResponse processResponse = CommandExecutor.initializeProcessBuilder(command);
		processResultDomain.setResponseData(processResponse);
		if(processResponse.getFailureResultString() != null && processResponse.getFailureResultString().length() > 0) {
			processResultDomain.setSuccess(false);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("프로세스 수행중 오류가 발생했습니다. : ", "M00010") + processResponse.getFailureResultString(), true);
		}
		
		JSONArray rewardsData = new JSONArray(processResponse.getSuccessResultString());
		if(rewardsData.length() == 0) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Stake Key가 등록되지 않았습니다. Stake Key 등록 후 진행하세요.", "M00002"), true);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("(메인 메뉴 -> 3번 -> 1번 선택)", "M00003"), true);
			processResultDomain.setSuccess(false);
		}else {
			CommandExecutor.pringStakePoolStatus(rewardsData);
			processResponse.setSuccessResultString(null);
			processResultDomain.setSuccess(true);
		}
		
		return processResultDomain;
	}

}