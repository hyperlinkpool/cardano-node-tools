package kr.hyperlinkpool.constants;

public enum BalanceAndWithdrawingCommand {
	BALANCE_CHECK_STAKE_POOL_ADA(1)
	,BALANCE_CHECK_ANOTHER_ADDRESS_ADA(2)
	,BALANCE_CHECK_STAKE_POOL_REWARDS(3)
	,BALANCE_CHECK_ANOTHER_STAKE_POOL_REWARDS(4)
	,CURRENT_STAKE_POOL_ADA_WITHDRAWING(5)
	,CURRENT_STAKE_POOL_REWARDS_WITHDRAWING(6)
	,EXIT(99)
	;
	
	private int command; 
	
	BalanceAndWithdrawingCommand(int command) {
		this.command = command;
	}

	public int getCommand() {
		return command;
	}

	public void setCommand(int command) {
		this.command = command;
	}
	
	public static BalanceAndWithdrawingCommand getKeyCommand(int command) {
		BalanceAndWithdrawingCommand[] values = BalanceAndWithdrawingCommand.values();
		for(BalanceAndWithdrawingCommand balanceAndWithdrawingCommand : values) {
			if(balanceAndWithdrawingCommand.getCommand() == command) {
				return balanceAndWithdrawingCommand;
			}
		}
		return null;
	}
}