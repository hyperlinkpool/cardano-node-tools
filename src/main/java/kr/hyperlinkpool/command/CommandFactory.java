package kr.hyperlinkpool.command;

public class CommandFactory {

	public static CommandFactory INSTANCE = new CommandFactory();
	
	private CommandFactory (){}
	
	public AbstractCommandHandler createCommandHandler(NodeCommands nodeCommands){
		AbstractCommandHandler handler = nodeCommands.getHandler();
		return handler;
	}
	
}