package kr.hyperlinkpool.menu.keyandcert;

import kr.hyperlinkpool.command.CommandFactory;
import kr.hyperlinkpool.command.NodeCommands;
import kr.hyperlinkpool.constants.KeyAndCertCommand;
import kr.hyperlinkpool.domain.ProcessResponse;
import kr.hyperlinkpool.domain.ProcessResultDomain;
import kr.hyperlinkpool.i18n.MessageFactory;
import kr.hyperlinkpool.interfaces.JobProcess;
import kr.hyperlinkpool.menu.Menu;
import kr.hyperlinkpool.utils.CommandListener;
import kr.hyperlinkpool.utils.MessagePrompter;

public class KeyAndCertContextMenu implements JobProcess, Menu{

	public static KeyAndCertContextMenu instance;
	
	private String keyAndCertContextMenus = MessageFactory.getInstance().getMessage("\n"
			+ "1. Payment Key Pair 생성\n"
			+ "2. Stake Key Pair 생성\n"
			+ "3. Payment Address 생성\n"
			+ "4. Stake Address 생성\n"
			+ "5. Stake Certificate 생성\n"
			+ "6. Stake Pool Cold Keys & Cold Counter 생성\n"
			+ "7. Stake Pool VRF Key Pair 생성\n"
			+ "8. Stake Pool KES Key Pair 생성\n"
			+ "9. Node Operation Certificate 생성\n"
			+ "99. 뒤로 가기", "M00137");
	
	private boolean menuRunningState = false;
	
	private KeyAndCertContextMenu() {}
	
	public static KeyAndCertContextMenu getIntance() {
		if(instance == null) {
			instance = new KeyAndCertContextMenu();
		}
		return instance;
	}

	@Override
	public String getMenu() {
		return keyAndCertContextMenus;
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
				int command = Integer.parseInt(listenCommand);
				switch(KeyAndCertCommand.getKeyCommand(command)) {
				case PAYMENT_KEY_PAIR:
					processResultDomain = CommandFactory.INSTANCE.createCommandHandler(NodeCommands.PAYMENT_KEY_PAIR_HANDLER).handleCommand(command);
					break;
				case STAKE_KEY_PAIR:
					processResultDomain = CommandFactory.INSTANCE.createCommandHandler(NodeCommands.STAKE_KEY_PAIR_HANDLER).handleCommand(command);
					break;
				case PAYMENT_ADDRESS:
					processResultDomain = CommandFactory.INSTANCE.createCommandHandler(NodeCommands.PAYMENT_ADDRESS_HANDLER).handleCommand(command);
					break;
				case STAKE_ADDRESS:
					processResultDomain = CommandFactory.INSTANCE.createCommandHandler(NodeCommands.STAKE_ADDRESS_HANDLER).handleCommand(command);
					break;
				case STAKE_POOL_CREATE_A_REGISTRATION_CERTIFICATE:
					processResultDomain = CommandFactory.INSTANCE.createCommandHandler(NodeCommands.STAKE_POOL_CREATE_A_REGISTRATION_CERTIFICATE_HANDLER).handleCommand(command);
					break;
				case POOL_REGISTRATION_GENERATE_COLD_KEY_AND_COLD_COUNTER:
					processResultDomain = CommandFactory.INSTANCE.createCommandHandler(NodeCommands.POOL_REGISTRATION_GENERATE_COLD_KEY_AND_COLD_COUNTER_HANDLER).handleCommand(command);
					break;
				case POOL_REGISTRATION_GENERATE_VRF_KEY_PAIR:
					processResultDomain = CommandFactory.INSTANCE.createCommandHandler(NodeCommands.POOL_REGISTRATION_GENERATE_VRF_KEY_PAIR_HANDLER).handleCommand(command);
					break;
				case POOL_REGISTRATION_GENERATE_KES_KEY_PAIR:
					processResultDomain = CommandFactory.INSTANCE.createCommandHandler(NodeCommands.POOL_REGISTRATION_GENERATE_KES_KEY_PAIR_HANDLER).handleCommand(command);
					break;
				case POOL_REGISTRATION_GENERATE_CERTIFICATE:
					processResultDomain = CommandFactory.INSTANCE.createCommandHandler(NodeCommands.POOL_REGISTRATION_GENERATE_CERTIFICATE_HANDLER).handleCommand(command);
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