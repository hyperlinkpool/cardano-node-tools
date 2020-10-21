package kr.hyperlinkpool.constants;

public enum KeyAndCertCommand {
	PAYMENT_KEY_PAIR(1)
	,STAKE_KEY_PAIR(2)
	,PAYMENT_ADDRESS(3)
	,STAKE_ADDRESS(4)
	,POOL_REGISTRATION_GENERATE_COLD_KEY_AND_COLD_COUNTER(5)
	,POOL_REGISTRATION_GENERATE_VRF_KEY_PAIR(6)
	,POOL_REGISTRATION_GENERATE_KES_KEY_PAIR(7)
	,POOL_REGISTRATION_GENERATE_CERTIFICATE(8)
	,EXIT(99)
	;
	
	private int command;
	
	KeyAndCertCommand(int command) {
		this.command = command;
	}

	public int getCommand() {
		return command;
	}

	public void setCommand(int command) {
		this.command = command;
	}
	
	public static KeyAndCertCommand getKeyCommand(int command) {
		KeyAndCertCommand[] values = KeyAndCertCommand.values();
		for(KeyAndCertCommand keyAndCertCommand : values) {
			if(keyAndCertCommand.getCommand() == command) {
				return keyAndCertCommand;
			}
		}
		return null;
	}
}