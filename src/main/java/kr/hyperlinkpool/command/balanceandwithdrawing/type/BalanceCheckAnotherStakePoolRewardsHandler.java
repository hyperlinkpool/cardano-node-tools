package kr.hyperlinkpool.command.balanceandwithdrawing.type;

import org.json.JSONArray;

import kr.hyperlinkpool.command.AbstractCommandHandler;
import kr.hyperlinkpool.command.NodeCommandFormats;
import kr.hyperlinkpool.constants.ExitCommand;
import kr.hyperlinkpool.domain.ProcessResponse;
import kr.hyperlinkpool.domain.ProcessResultDomain;
import kr.hyperlinkpool.i18n.MessageFactory;
import kr.hyperlinkpool.properties.NodeProperties;
import kr.hyperlinkpool.utils.CommandExecutor;
import kr.hyperlinkpool.utils.CommandListener;
import kr.hyperlinkpool.utils.MessagePrompter;

public class BalanceCheckAnotherStakePoolRewardsHandler extends AbstractCommandHandler{

	@Override
	public ProcessResultDomain<ProcessResponse> handleCommand(Object... commands) {
		ProcessResultDomain<ProcessResponse> processResultDomain = new ProcessResultDomain<>();
		boolean loop = true; 
		while(loop) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Stake 주소를 입력해 주세요.(ex. stake1... )", "M00174"), true);
			String address = CommandListener.getInstance().listenCommand(MessageFactory.getInstance().getMessage("(99 : 뒤로 가기) : ", "M00023"), false);
			try {
				int parseInt = Integer.parseInt(address);
				switch(ExitCommand.getKeyCommand(parseInt)) {
				case EXIT:
					loop = false;
					break;
				}
			} catch (Exception e) {
				if(address != null && address.length() > 0) {
					String cardanoCliName = NodeProperties.getString("cardano.cli.name");
					String command = CommandExecutor.generateCommand(NodeCommandFormats.STAKE_ADDRESS_BALANCE_CHECK, cardanoCliName, address);
					ProcessResponse processResponse = CommandExecutor.initializeProcessBuilder(command);
					processResultDomain.setResponseData(processResponse);
					if(processResponse.getFailureResultString() != null && processResponse.getFailureResultString().length() > 0 && processResponse.getFailureResultString().indexOf("invalid address") > -1) {
						processResultDomain.setSuccess(false);
						MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("유효한 주소가 아닙니다.", "M00024"), true);
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
				}
			}
		}
		
		return processResultDomain;
	}

}