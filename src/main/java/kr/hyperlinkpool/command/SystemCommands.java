package kr.hyperlinkpool.command;

import kr.hyperlinkpool.ReadMe;
import kr.hyperlinkpool.i18n.MessageFactory;
import kr.hyperlinkpool.utils.DateUtils;
import kr.hyperlinkpool.utils.MessagePrompter;

public class SystemCommands {

	public void setupCommands() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
					Thread.sleep(100);
					MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("프로그램이 종료되었습니다.", "M00134"), true);
					MessagePrompter.promptMessage(DateUtils.getToday("yyyy-MM-dd HH:mm:ss,sss"), true);
					ReadMe.printGoodbyeMessage();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					e.printStackTrace();
				}
			}
		});
	}
}