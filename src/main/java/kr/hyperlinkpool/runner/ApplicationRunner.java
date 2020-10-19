package kr.hyperlinkpool.runner;

import java.io.IOException;
import java.net.URL;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import kr.hyperlinkpool.ReadMe;
import kr.hyperlinkpool.command.SystemCommands;
import kr.hyperlinkpool.constants.NodeConstants;
import kr.hyperlinkpool.interfaces.JobProcess;
import kr.hyperlinkpool.menu.MenuFactory;
import kr.hyperlinkpool.menu.RootContextMenu;
import kr.hyperlinkpool.menu.balanceandwithdrawing.BalanceAndWithdrawingContextMenu;
import kr.hyperlinkpool.menu.keyandcert.KeyAndCertContextMenu;
import kr.hyperlinkpool.menu.poolregist.PoolRegistContextMenu;
import kr.hyperlinkpool.properties.NodeProperties;
import kr.hyperlinkpool.utils.DateUtils;
import kr.hyperlinkpool.utils.MessagePrompter;

public class ApplicationRunner implements JobProcess {

	@Override
	public void run() {
		this.bindAddHookListener();
		this.loadProperties();
		this.reloadLogger();
		
		MessagePrompter.promptMessage("Application has started at " + DateUtils.getToday("yyyy-MM-dd HH:mm:ss,sss"), true);
		ReadMe.printWelcomeMessage();
		boolean applicationRunningState = true;
		MenuFactory menuFactory = MenuFactory.getInstance();
		menuFactory.setRootContextMenu(RootContextMenu.getInstance());
		menuFactory.setKeyAndCertContextMenu(KeyAndCertContextMenu.getIntance());
		menuFactory.setBalanceAndWithdrawingContextMenu(BalanceAndWithdrawingContextMenu.getInstance());
		menuFactory.setPoolRegistContextMenu(PoolRegistContextMenu.getInstance());
		menuFactory.setApplicationRunningState(applicationRunningState);
		menuFactory.run();
	}
	
	private void bindAddHookListener() {
		SystemCommands systemCommands = new SystemCommands();
		systemCommands.setupCommands();
	}

	public void loadProperties() {
		/**
		 * Custom Properties 확인
		 */
		String configPath = System.getProperty("config.path");
		NodeProperties nodeProperties = NodeProperties.getInstance();
		try {
			if (configPath != null) {
				nodeProperties.loadProp(configPath);
			} else {
				nodeProperties.loadPropBaseClasspath("/config.properties");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void reloadLogger() {
		/**
		 * System properties가 셋팅되어 있으면 다시 로드할 필요가 없으므로 리턴한다.
		 */
		String loggingPath = System.getProperty("logging.path");
		if(loggingPath != null && loggingPath.length() > 0) {
			return;
		}
		
		/**
		 * Configuration file에 logging.path가 설정되어 있으면, 해당 경로로 다시 셋팅한 후 Logback을 다시 로드한다.
		 */
		loggingPath = NodeProperties.getString("logging.path");
		if(loggingPath == null || loggingPath.length() == 0) {
			System.setProperty("logging.path", NodeConstants.DEFAULT_LOG_PATH);
		}else{
			System.setProperty("logging.path", loggingPath);
		}
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
	    ContextInitializer contextInitializer = new ContextInitializer(loggerContext);
	    URL url = contextInitializer.findURLOfDefaultConfigurationFile(true);
	    try {
	        JoranConfigurator configurator = new JoranConfigurator();
	        configurator.setContext(loggerContext);
	        loggerContext.reset();
	        configurator.doConfigure(url);
	    } catch (JoranException je) {
	    	je.printStackTrace();
	    }
	    StatusPrinter.printInCaseOfErrorsOrWarnings(loggerContext);
	}
}