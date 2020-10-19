package kr.hyperlinkpool.utils;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.hyperlinkpool.i18n.MessageFactory;

public class CommandListener {

	private static Logger logger = LoggerFactory.getLogger(CommandListener.class);
	
	public static CommandListener instance;
	
	private CommandListener() {}
	
	public static CommandListener getInstance() {
		if(instance == null) {
			instance = new CommandListener();
		}
		return instance;
	}
	
	public String listenCommand(String message, boolean lfcr) {
		MessagePrompter.promptMessage(message, lfcr);
		Scanner scanner = null;
		String listenMessage = null;
		try {
			scanner = new Scanner(System.in);
			listenMessage = scanner.nextLine();
		} catch (Exception e) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("입력 값이 잘못되었습니다.", "M00140"), true);
		}
		logger.info(listenMessage);
		return listenMessage;
	}
}