package kr.hyperlinkpool.constants;

public enum ExitCommand {
	EXIT(99)
	;
	
	private int command; 
	
	ExitCommand(int command) {
		this.command = command;
	}

	public int getCommand() {
		return command;
	}

	public void setCommand(int command) {
		this.command = command;
	}
	
	public static ExitCommand getKeyCommand(int command) {
		ExitCommand[] values = ExitCommand.values();
		for(ExitCommand exitCommand : values) {
			if(exitCommand.getCommand() == command) {
				return exitCommand;
			}
		}
		return null;
	}
}