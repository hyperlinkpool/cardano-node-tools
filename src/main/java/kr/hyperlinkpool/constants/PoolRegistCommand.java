package kr.hyperlinkpool.constants;

public enum PoolRegistCommand {
	REGISTER_STAKE_ADDRESS_ON_THE_BLOCKCHAIN(1)
	,DEREGISTER_STAKE_ADDRESS_ON_THE_BLOCKCHAIN(2)
	,DELEGATE_STAKE_ADDRESS_ON_POOL(3)
	,GENERATE_META_JSON(4)
	,REGISTER_POOL(5)
	,DEREGISTER_POOL(6)
	,POOL_STATUS(7)
	,KES_KEY_ROTATE(8)
	,EXIT(99)
	;
	
	private int command; 
	
	PoolRegistCommand(int command) {
		this.command = command;
	}

	public int getCommand() {
		return command;
	}

	public void setCommand(int command) {
		this.command = command;
	}
	
	public static PoolRegistCommand getKeyCommand(int command) {
		PoolRegistCommand[] values = PoolRegistCommand.values();
		for(PoolRegistCommand poolRegistCommand : values) {
			if(poolRegistCommand.getCommand() == command) {
				return poolRegistCommand;
			}
		}
		return null;
	}
}

