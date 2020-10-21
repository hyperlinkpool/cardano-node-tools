package kr.hyperlinkpool.menu;

import kr.hyperlinkpool.i18n.MessageFactory;
import kr.hyperlinkpool.interfaces.JobProcess;
import kr.hyperlinkpool.menu.balanceandwithdrawing.BalanceAndWithdrawingContextMenu;
import kr.hyperlinkpool.menu.keyandcert.KeyAndCertContextMenu;
import kr.hyperlinkpool.menu.poolregist.PoolRegistContextMenu;
import kr.hyperlinkpool.utils.CommandListener;
import kr.hyperlinkpool.utils.MessagePrompter;

public class RootContextMenu implements JobProcess, Menu{

	public static RootContextMenu instance;
	
	private String rootContextMenus = MessageFactory.getInstance().getMessage("\n"
			+ "1. Key 생성, Stake 인증서 생성\n"
			+ "2. ADA Balance Check, Pool에서 ADA 인출\n"
			+ "3. Stake Pool 등록 / 관리\n"
			+ "99. 종료", "M00139");
	
	private boolean menuRunningState = false;
	
	private RootContextMenu() {}

	public static RootContextMenu getInstance() {
		if(instance == null) {
			instance = new RootContextMenu();
		}
		
		return instance;
	}
	
	@Override
	public String getMenu() {
		return rootContextMenus;
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
			try {
				short command = Short.parseShort(listenCommand);
				switch(command) {
				case 1:
					KeyAndCertContextMenu keyAndCertContextMenu = MenuFactory.getInstance().getKeyAndCertContextMenu();
					keyAndCertContextMenu.setMenuRunningState(true);
					keyAndCertContextMenu.run();
					break;
				case 2:
					BalanceAndWithdrawingContextMenu balanceAndWithdrawingContextMenu = MenuFactory.getInstance().getBalanceAndWithdrawingContextMenu();
					balanceAndWithdrawingContextMenu.setMenuRunningState(true);
					balanceAndWithdrawingContextMenu.run();
					break;
				case 3:
					PoolRegistContextMenu poolRegistContextMenu = MenuFactory.getInstance().getPoolRegistContextMenu();
					poolRegistContextMenu.setMenuRunningState(true);
					poolRegistContextMenu.run();
					break;
				case 99:
					this.setMenuRunningState(false);
				}
			} catch (Exception e) {
//				MessagePrompter.promptMessage("숫자를 입력해 주세요.", true);
			}
		}
	}
}