package kr.hyperlinkpool.menu.balanceandwithdrawing;

import kr.hyperlinkpool.command.CommandFactory;
import kr.hyperlinkpool.command.NodeCommands;
import kr.hyperlinkpool.constants.BalanceAndWithdrawingCommand;
import kr.hyperlinkpool.domain.ProcessResponse;
import kr.hyperlinkpool.domain.ProcessResultDomain;
import kr.hyperlinkpool.i18n.MessageFactory;
import kr.hyperlinkpool.interfaces.JobProcess;
import kr.hyperlinkpool.menu.Menu;
import kr.hyperlinkpool.utils.CommandListener;
import kr.hyperlinkpool.utils.MessagePrompter;

public class BalanceAndWithdrawingContextMenu implements JobProcess, Menu{

	public static BalanceAndWithdrawingContextMenu instance;
	
	private String balanceAndWithdrawingContextMenus = MessageFactory.getInstance().getMessage("\n"
			+ "1. 현재 지갑의 ADA Balance Check\n"
			+ "2. 다른 지갑의 ADA Balance Check\n"
			+ "3. 현재 지갑의 ADA Reward Balance Check\n"
			+ "4. 현재 지갑에서 다른 지갑으로 ADA 인출\n"
			+ "5. 현재 지갑에서 보상분(Rewards) ADA 인출\n"
			+ "99. 뒤로 가기", "M00135");
			
	private boolean menuRunningState = false;

	private BalanceAndWithdrawingContextMenu() {}
	
	public static BalanceAndWithdrawingContextMenu getInstance() {
		if(instance == null) {
			instance = new BalanceAndWithdrawingContextMenu();
		}
		return instance;
	}

	@Override
	public String getMenu() {
		return balanceAndWithdrawingContextMenus;
	}
	
	public boolean isMenuRunningState() {
		return menuRunningState;
	}

	public void setMenuRunningState(boolean menuRunningState) {
		this.menuRunningState = menuRunningState;
	}

	@Override
	public void run() {
		while(this.isMenuRunningState()) {
			MessagePrompter.promptMessage(this.getMenu(), true);
			CommandListener instance = CommandListener.getInstance();
			String listenCommand = instance.listenCommand(MessageFactory.getInstance().getMessage("번호를 입력한 후 엔터키를 눌러주세요. : ", "M00136"), false);
			ProcessResultDomain<ProcessResponse> processResultDomain = null;
			try {
				short command = Short.parseShort(listenCommand);
				switch(BalanceAndWithdrawingCommand.getKeyCommand(command)) {
				case BALANCE_CHECK_STAKE_POOL_ADA:
					processResultDomain = CommandFactory.INSTANCE.createCommandHandler(NodeCommands.BALANCE_CHECK_STAKEPOOL_ADA_HANDLER).handleCommand(command);
					break;
				case BALANCE_CHECK_ANOTHER_ADDRESS_ADA:
					processResultDomain = CommandFactory.INSTANCE.createCommandHandler(NodeCommands.BALANCE_CHECK_ANOTHER_ADDRESS_ADA_HANDLER).handleCommand(command);
					break;
				case BALANCE_CHECK_STAKE_POOL_REWARDS:
					processResultDomain = CommandFactory.INSTANCE.createCommandHandler(NodeCommands.BALANCE_CHECK_STAKE_POOL_REWARDS_HANDLER).handleCommand(command);
					break;
				case CURRENT_STAKE_POOL_ADA_WITHDRAWING:
					processResultDomain = CommandFactory.INSTANCE.createCommandHandler(NodeCommands.CURRENT_STAKE_POOL_ADA_WITHDRAWING_HANDLER).handleCommand(command);
					break;
				case CURRENT_STAKE_POOL_REWARDS_WITHDRAWING:
					processResultDomain = CommandFactory.INSTANCE.createCommandHandler(NodeCommands.CURRENT_STAKE_POOL_REWARDS_WITHDRAWING_HANDLER).handleCommand(command);
					break;
				case EXIT:
					this.setMenuRunningState(false);
				}
				/**
				 * 실행결과 출력
				 */
				if(processResultDomain.isSuccess()) {
					if(processResultDomain.getResponseData().getSuccessResultString() != null) {
						MessagePrompter.promptMessage(processResultDomain.getResponseData().getSuccessResultString(), true);
					}
				}
			} catch (Exception e) {
			}
		}
	}
}