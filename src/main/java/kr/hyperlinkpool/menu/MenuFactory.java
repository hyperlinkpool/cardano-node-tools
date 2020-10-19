package kr.hyperlinkpool.menu;

import kr.hyperlinkpool.interfaces.JobProcess;
import kr.hyperlinkpool.menu.balanceandwithdrawing.BalanceAndWithdrawingContextMenu;
import kr.hyperlinkpool.menu.keyandcert.KeyAndCertContextMenu;
import kr.hyperlinkpool.menu.poolregist.PoolRegistContextMenu;

public class MenuFactory implements JobProcess {
	
	private RootContextMenu rootContextMenu;
	
	private KeyAndCertContextMenu keyAndCertContextMenu;
	
	private BalanceAndWithdrawingContextMenu balanceAndWithdrawingContextMenu;
	
	private PoolRegistContextMenu poolRegistContextMenu;

	private boolean applicationRunningState = false;
	
	public static MenuFactory instance;
	
	private MenuFactory() {
	}
	
	public static MenuFactory getInstance() {
		if(instance == null) {
			instance = new MenuFactory();
		}
		return instance;
	}
	
	public RootContextMenu getRootContextMenu() {
		return rootContextMenu;
	}

	public void setRootContextMenu(RootContextMenu rootContextMenu) {
		this.rootContextMenu = rootContextMenu;
	}

	public KeyAndCertContextMenu getKeyAndCertContextMenu() {
		return keyAndCertContextMenu;
	}

	public void setKeyAndCertContextMenu(KeyAndCertContextMenu keyAndCertContextMenu) {
		this.keyAndCertContextMenu = keyAndCertContextMenu;
	}

	public BalanceAndWithdrawingContextMenu getBalanceAndWithdrawingContextMenu() {
		return balanceAndWithdrawingContextMenu;
	}

	public void setBalanceAndWithdrawingContextMenu(BalanceAndWithdrawingContextMenu balanceAndWithdrawingContextMenu) {
		this.balanceAndWithdrawingContextMenu = balanceAndWithdrawingContextMenu;
	}

	public PoolRegistContextMenu getPoolRegistContextMenu() {
		return poolRegistContextMenu;
	}

	public void setPoolRegistContextMenu(PoolRegistContextMenu poolRegistContextMenu) {
		this.poolRegistContextMenu = poolRegistContextMenu;
	}
	
	public boolean isApplicationRunningState() {
		return applicationRunningState;
	}

	public void setApplicationRunningState(boolean applicationRunningState) {
		this.applicationRunningState = applicationRunningState;
	}

	@Override
	public void run() {
		rootContextMenu.setMenuRunningState(true);
		rootContextMenu.run();
	}
}