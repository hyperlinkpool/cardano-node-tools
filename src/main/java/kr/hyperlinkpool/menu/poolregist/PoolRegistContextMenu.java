package kr.hyperlinkpool.menu.poolregist;

import kr.hyperlinkpool.command.CommandFactory;
import kr.hyperlinkpool.command.NodeCommands;
import kr.hyperlinkpool.constants.PoolRegistCommand;
import kr.hyperlinkpool.domain.ProcessResponse;
import kr.hyperlinkpool.domain.ProcessResultDomain;
import kr.hyperlinkpool.i18n.MessageFactory;
import kr.hyperlinkpool.interfaces.JobProcess;
import kr.hyperlinkpool.menu.Menu;
import kr.hyperlinkpool.utils.CommandListener;
import kr.hyperlinkpool.utils.MessagePrompter;

public class PoolRegistContextMenu implements JobProcess, Menu{

	public static PoolRegistContextMenu instance;
	
	private String poolRegistContextMenus = MessageFactory.getInstance().getMessage("\n"
			+ "1. Stake Key 등록(기본 등록은 2ADA가 소요되고, 전송 수수료가 추가 소요됩니다.)\n"
			+ "2. Stake Key 철회(보증금으로 등록한 2ADA를 돌려 받을 수 있습니다.)\n"
			+ "3. Stake Key를 Pool에 위임\n"
			+ "4. Metadata JSON 파일 생성\n"
			+ "5. Pool 등록 (Stake Key를 먼저 등록해야 하며, Pool 등록은 500ADA가 소요되고, 이 후 갱신 시부터는 전송 수수료만 추가 소요됩니다.)\n"
			+ "6. Pool 수정\n"
			+ "7. Pool 철회 (보증금으로 등록한 500ADA를 돌려 받을 수 있습니다.)\n"
			+ "8. Pool 등록 상태 확인\n"
			+ "9. KES Key Rotation\n"
			+ "99. 뒤로 가기", "M00138");
	
	private boolean menuRunningState = false;
	
	private PoolRegistContextMenu() {}
	
	public static PoolRegistContextMenu getInstance() {
		if(instance == null) {
			instance = new PoolRegistContextMenu();
		}
		
		return instance;
	}

	@Override
	public String getMenu() {
		return poolRegistContextMenus;
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
				switch(PoolRegistCommand.getKeyCommand(command)) {
				case REGISTER_STAKE_ADDRESS_ON_THE_BLOCKCHAIN:
					processResultDomain = CommandFactory.INSTANCE.createCommandHandler(NodeCommands.REGISTER_STAKE_ADDRESS_ON_THE_BLOCKCHAIN_HANDLER).handleCommand(command);
					break;
				case DEREGISTER_STAKE_ADDRESS_ON_THE_BLOCKCHAIN:
					processResultDomain = CommandFactory.INSTANCE.createCommandHandler(NodeCommands.DEREGISTER_STAKE_ADDRESS_ON_THE_BLOCKCHAIN_HANDLER).handleCommand(command);
					break;
				case DELEGATE_STAKE_ADDRESS_ON_POOL:
					processResultDomain = CommandFactory.INSTANCE.createCommandHandler(NodeCommands.DELEGATE_STAKE_ADDRESS_ON_POOL_HANDLER).handleCommand(command);
					break;
				case GENERATE_META_JSON:
					processResultDomain = CommandFactory.INSTANCE.createCommandHandler(NodeCommands.GENERATE_META_JSON_HANDLER).handleCommand(command);
					break;
				case REGISTER_POOL:
					processResultDomain = CommandFactory.INSTANCE.createCommandHandler(NodeCommands.REGISTER_POOL_HANDLER).handleCommand(command);
					break;
				case MODIFY_POOL:
					processResultDomain = CommandFactory.INSTANCE.createCommandHandler(NodeCommands.MODIFY_POOL_HANDLER).handleCommand(command);
					break;
				case DEREGISTER_POOL:
					processResultDomain = CommandFactory.INSTANCE.createCommandHandler(NodeCommands.DEREGISTER_POOL_HANDLER).handleCommand(command);
					break;
				case POOL_STATUS:
					processResultDomain = CommandFactory.INSTANCE.createCommandHandler(NodeCommands.POOL_STATUS_HANDLER).handleCommand(command);
					break;
				case KES_KEY_ROTATE:
					processResultDomain = CommandFactory.INSTANCE.createCommandHandler(NodeCommands.KES_KEY_ROTATE_HANDLER).handleCommand(command);
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