package kr.hyperlinkpool.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessagePrompter {
	
	private static Logger logger = LoggerFactory.getLogger(MessagePrompter.class);
	
	public static boolean promptMessage(String message, boolean nextLine) {
		logger.info(message);
		if(nextLine) {
			System.out.println(message);
		}else {
			System.out.print(message);
			
		}
		return true;
	}
	
}