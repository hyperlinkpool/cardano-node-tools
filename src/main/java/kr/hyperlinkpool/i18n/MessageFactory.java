package kr.hyperlinkpool.i18n;

import kr.hyperlinkpool.constants.NodeConstants;
import kr.hyperlinkpool.i18n.messages.MessageJP;
import kr.hyperlinkpool.i18n.messages.MessageUS;
import kr.hyperlinkpool.properties.NodeProperties;

/**
 * @author hyperlinkpool
 */
public class MessageFactory {
	
	public static MessageFactory instance;
	
	private MessageFactory() {}
	
	public static MessageFactory getInstance() {
		if(instance == null) {
			instance = new MessageFactory();
		}
		return instance;
	}
	
	public String getMessage(String defaultMessageFormat, String messageKey, Object ... parameters) {
		String userLanguage = NodeProperties.getString("user.language");
		if(userLanguage == null) {
			userLanguage = System.getProperty("user.language");
		}
		if(userLanguage == null) {
			userLanguage = NodeConstants.DEFAULT_LANGUAGE;
		}
		
		String format = null;
		String result = null;
		switch (UserLanguage.getUserLanguage(userLanguage)) {
		case KR:
			result = instance.generateFormat(defaultMessageFormat, parameters);
			break;
		case US:
			format = MessageUS.messageFormats.get(messageKey);
			result = instance.generateFormat(format, parameters);
			break;
		case JP:
			format = MessageJP.messageFormats.get(messageKey);
			result = instance.generateFormat(format, parameters); 
			break;
		default:
			format = MessageUS.messageFormats.get(messageKey);
			result = instance.generateFormat(format, parameters);
			break;
		}
		return result;
	}
	
	public String generateFormat(String format, Object ... parameters) {
		String result = String.format(format, parameters);
		return result;
	}
}
