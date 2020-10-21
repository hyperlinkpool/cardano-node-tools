package kr.hyperlinkpool.command.poolregist.type;

import java.io.IOException;
import java.util.regex.Pattern;

import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.hyperlinkpool.command.AbstractCommandHandler;
import kr.hyperlinkpool.command.poolregist.type.generatemetastep.domains.MetadataDomain;
import kr.hyperlinkpool.domain.ProcessResponse;
import kr.hyperlinkpool.domain.ProcessResultDomain;
import kr.hyperlinkpool.i18n.MessageFactory;
import kr.hyperlinkpool.utils.CommandListener;
import kr.hyperlinkpool.utils.MessagePrompter;

public class GenerateMetaJsonHandler extends AbstractCommandHandler{

	@Override
	public ProcessResultDomain<ProcessResponse> handleCommand(Object... commands) {
		ProcessResultDomain<ProcessResponse> processResultDomain = new ProcessResultDomain<ProcessResponse>();
		MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Pool 등록용 MetaData 생성을 진행합니다.", "M00103"), true);
		MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("예시) [HYPER] Pool", "M00104"), true);
		MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Pool 이름 : Hyperlink Pool", "M00105"), true);
		MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Pool 설명 : We prepare for the era of hyperconnectivity.", "M00106"), true);
		MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Pool Ticker(영문 5자 이내) : HYPER", "M00107"), true);
		MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Pool 홍보 홈페이지 주소 : https://twitter.com/HYPERLINKPOOL", "M00108"), true);
		MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("adapools.org 에 등록할 extended url. 선택사항이며 없으면 enter키를 입력하세요.(참고형식 https://a.adapools.org/extended-example) : https://git.io/JUMjN", "M00109"), true);
		MessagePrompter.promptMessage("", true);
		
		String inputValue = null;
		MetadataDomain metadataDomain = new MetadataDomain();
		boolean mainLoop = true;
		while(mainLoop) {
			inputValue = CommandListener.getInstance().listenCommand(MessageFactory.getInstance().getMessage("Pool 이름 : ", "M00052"), false);
			metadataDomain.setName(inputValue);
			inputValue = CommandListener.getInstance().listenCommand(MessageFactory.getInstance().getMessage("Pool 설명 : ", "M00053"), false);
			metadataDomain.setDescription(inputValue);
			
			boolean tickerLoop = true;
			while(tickerLoop) {
				inputValue = CommandListener.getInstance().listenCommand(MessageFactory.getInstance().getMessage("Pool Ticker(영문 5자 이내) : ", "M00110"), false);
				boolean tickerValidation = Pattern.matches("^[a-zA-Z0-9]*$", inputValue); 
				if(!tickerValidation || inputValue.length() > 5) {
					MessagePrompter.promptMessage("영문, 숫자 5자 이내로 입력하세요.", true);
					continue;
				}
				
				metadataDomain.setTicker(inputValue.toUpperCase());
				tickerLoop = false;
			}
			inputValue = CommandListener.getInstance().listenCommand(MessageFactory.getInstance().getMessage("Pool 홍보 홈페이지 주소 : ", "M00111"), false);
			metadataDomain.setHomepage(inputValue);
			inputValue = CommandListener.getInstance().listenCommand(MessageFactory.getInstance().getMessage("선택사항) adapools.org 에 등록시킬 extended url. : ", "M00112"), false);
			metadataDomain.setExtended(inputValue);
			
			MessagePrompter.promptMessage("*******************************************************************************************************************", true);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Pool 이름 : ", "M00052") + metadataDomain.getName(), true);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Pool 설명 : ", "M00053") + metadataDomain.getDescription(), true);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Pool Ticker(영문 5자 이내) : ", "M00110") + metadataDomain.getTicker(), true);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Pool 홍보 홈페이지 주소 : ", "M00111") + metadataDomain.getHomepage(), true);
			if(metadataDomain.getExtended() == null || metadataDomain.getExtended().length() == 0) {
				MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("선택사항) adapools.org에 등록할 extended url : ", "M00112") + "-", true);
			}else {
				MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("선택사항) adapools.org에 등록할 extended url : ", "M00112") + metadataDomain.getExtended(), true);
			}
			MessagePrompter.promptMessage("*******************************************************************************************************************", true);
			inputValue = CommandListener.getInstance().listenCommand(MessageFactory.getInstance().getMessage("위에 입력한 정보가 맞습니까? (Y/n) : ", "M00057"), false);
			if("Y".equalsIgnoreCase(inputValue)) {
				processResultDomain.setSuccess(true);
				mainLoop = false;
			}else {
				inputValue = CommandListener.getInstance().listenCommand(MessageFactory.getInstance().getMessage("MetaData 생성을 계속하시겠습니까? (Y/n) : ", "M00114"), false);
				if("N".equalsIgnoreCase(inputValue)) {
					processResultDomain.setSuccess(false);
					mainLoop = false;
				}
			}
		}
		
		if(processResultDomain.isSuccess()) {
			MessagePrompter.promptMessage("*********************************************** Meta Data Json File Contents *****************************************************", true);
			JSONObject metadataDomainJsonObject = new JSONObject(metadataDomain);
			if(metadataDomain.getExtended() == null || metadataDomain.getExtended().length() == 0) {
				metadataDomainJsonObject.remove("extended");
			}
			ObjectMapper mapper = new ObjectMapper();
			Object obj;
			try {
				obj = mapper.readValue(metadataDomainJsonObject.toString(), Object.class);
				String writeValueAsString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
				MessagePrompter.promptMessage(writeValueAsString, true);
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			MessagePrompter.promptMessage("*********************************************** Meta Data Json File Contents *****************************************************", true);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("위의 Json Contents를 인터넷 환경에서 다운로드할 수 있도록 파일로 제작하여 업로드 한 후, 업로드한 파일을 다운로드하는 URL을 기억하십시오.(추천 : Github GIST)", "M00115"), true);
			MessagePrompter.promptMessage("", true);
		}
		
		return processResultDomain;
		
	}

}