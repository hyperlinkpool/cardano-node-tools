package kr.hyperlinkpool.constants;

public enum PoolRegistCommand {
	REGISTER_STAKE_ADDRESS_ON_THE_BLOCKCHAIN(1)
	,GENERATE_META_JSON(2)
//	,GENERATE_META_JSON_HASH(3)
//	,GENERATE_STAKE_POOL_REGISTRATION_CERTIFICATE(4)
	,REGISTER_POOL(3)
	,POOL_STATUS(4)
	,KES_KEY_ROTATE(5)
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

