package kr.hyperlinkpool.command.poolregist.type.poolmodifystep;

import java.io.File;
import java.io.IOException;

import org.json.JSONArray;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.hyperlinkpool.command.NodeCommandFormats;
import kr.hyperlinkpool.command.interfaces.Ordered;
import kr.hyperlinkpool.command.poolregist.type.generatemetastep.domains.MetadataDomain;
import kr.hyperlinkpool.command.poolregist.type.poolmodifystep.domains.PoolModifyDomain;
import kr.hyperlinkpool.command.poolregist.type.poolmodifystep.interfaces.PoolModifyResult;
import kr.hyperlinkpool.constants.NodeConstants;
import kr.hyperlinkpool.constants.StepOrder;
import kr.hyperlinkpool.domain.ProcessResponse;
import kr.hyperlinkpool.domain.ProcessResultDomain;
import kr.hyperlinkpool.i18n.MessageFactory;
import kr.hyperlinkpool.interfaces.JobProcess;
import kr.hyperlinkpool.properties.NodeProperties;
import kr.hyperlinkpool.utils.CommandExecutor;
import kr.hyperlinkpool.utils.CommandListener;
import kr.hyperlinkpool.utils.HttpUtils;
import kr.hyperlinkpool.utils.MessagePrompter;

public class Step1 implements PoolModifyResult, Ordered, JobProcess{

	private PoolModifyDomain poolModifyDomain;
	
	private ProcessResultDomain<PoolModifyDomain> result;
	
	@Override
	public int getOrder() {
		return StepOrder.STEP1.getStepOrder();
	}

	@Override
	public ProcessResultDomain<PoolModifyDomain> getResult() {
		return result;
	}

	@Override
	public void setResult(ProcessResultDomain<PoolModifyDomain> result) {
		this.result = result;
		this.setPoolModifyDomain(result.getResponseData());
	}

	public PoolModifyDomain getPoolRegisterDomain() {
		return poolModifyDomain;
	}

	public void setPoolModifyDomain(PoolModifyDomain poolModifyDomain) {
		this.poolModifyDomain = poolModifyDomain;
	}
	
	@Override
	public void run() {
		/**
		 * Stake Key 등록 확인
		 */
		String cardanoKeysFolderString = NodeProperties.getString("cardano.keys.folder");
		String cardanoKeysStakeAddressString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.stake.addr");
		File cardanoKeysStakeAddressFile = new File(cardanoKeysStakeAddressString);
		if(!cardanoKeysStakeAddressFile.exists()) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Stake 주소가 없습니다. 주소 생성 후 다시 시도하세요.", "M00001"), true);
			poolModifyDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
			result.setSuccess(false);
			return;
		}
		
		String cardanoCliName = NodeProperties.getString("cardano.cli.name");
		String stakeAddressString = CommandExecutor.readFile(cardanoKeysStakeAddressFile);
		String command = CommandExecutor.generateCommand(NodeCommandFormats.STAKE_ADDRESS_BALANCE_CHECK, cardanoCliName, stakeAddressString);
		ProcessResponse processResponse = CommandExecutor.initializeProcessBuilder(command);
		String failureResultString = processResponse.getFailureResultString();
		if(failureResultString != null && failureResultString.length() > 0) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("프로세스 수행중 오류가 발생했습니다. : ", "M00010") + processResponse.getFailureResultString(), true);
			poolModifyDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
			result.setSuccess(false);
			return;
		}
		
		JSONArray rewardsData = new JSONArray(processResponse.getSuccessResultString());
		if(rewardsData.length() == 0) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Stake Key가 등록되지 않았습니다. Stake Key 등록 후 진행하세요.", "M00002"), true);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("(메인 메뉴 -> 3번 -> 1번 선택)", "M00003"), true);
			poolModifyDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
			result.setSuccess(false);
			return;
		}
		
		/**
		 * Step. 1
		 * Metadata json 파일 생성
		 */
		boolean metadataUrlLoop = true;
		while(metadataUrlLoop) {
			String poolMetaDataJsonUrl = null;
			String inputValue = CommandListener.getInstance().listenCommand(MessageFactory.getInstance().getMessage("Metadata JSON 파일을 다운로드할 인터넷 주소를 입력하세요.(Q : 종료) : ", "M00047"), false);
			if("Q".equalsIgnoreCase(inputValue)) {
				poolModifyDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
				result.setSuccess(false);
				return;
			}
			
			poolMetaDataJsonUrl = inputValue;
			if(!HttpUtils.validateUri(poolMetaDataJsonUrl)) {
				MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("잘못된 URL 형식입니다. 다시 입력하세요.", "M00048"), true);
				continue;
			}
			
			if(poolMetaDataJsonUrl.length() > 64) {
				MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("URL 길이는 영문, 숫자, 기호 포함 64 Bytes(64자) 이하로 작성해야 합니다. Git Shorten URL을 이용하세요. https://git.io", "M00049"), true);
				continue;
			}
			
			String urlContentsLoad = HttpUtils.urlContentsLoad(poolMetaDataJsonUrl);
			ObjectMapper objectMapper = new ObjectMapper();
			MetadataDomain metadataDomain = null;
			try {
				metadataDomain = objectMapper.readValue(urlContentsLoad, MetadataDomain.class);
			} catch (JsonParseException e) {
				MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("잘못된 JSON파일 형식입니다. JSON파일을 확인하세요.", "M00050"), true);
				continue;
			} catch (JsonMappingException e) {
				MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Pool Meta정보(Pool 이름, Pool 설명, Pool Ticker, 등)이 없는 JSON파일 형식입니다. URL을 다시 확인하세요.", "M00051"), true);
				continue;
			} catch (IOException e) {
				e.printStackTrace();
				MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("프로세스 수행중 오류가 발생했습니다.", "M00010"), true);
				continue;
			}
			
			String name = metadataDomain.getName();
			String description = metadataDomain.getDescription();
			String ticker = metadataDomain.getTicker();
			String homepage = metadataDomain.getHomepage();
			String extended = metadataDomain.getExtended();
			
			MessagePrompter.promptMessage("*******************************************************************************************************************", true);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Pool 이름 : ", "M00052") + name, true);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Pool 설명 : ", "M00053") + description, true);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Pool Ticker : ", "M00054") + ticker, true);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Pool 홍보 홈페이지 주소 : ", "M00055") + homepage, true);
			if(metadataDomain.getExtended() == null || metadataDomain.getExtended().length() == 0) {
				MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("선택사항) adapools.org 에 등록할 extended url : ", "M00056") + "-", true);
			}else {
				MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("선택사항) adapools.org 에 등록할 extended url : ", "M00056") + extended, true);
			}
			MessagePrompter.promptMessage("*******************************************************************************************************************", true);
			inputValue = CommandListener.getInstance().listenCommand(MessageFactory.getInstance().getMessage("위에 입력한 정보가 맞습니까? (Y/n) : ", "M00057"), false);
			if(!"Y".equalsIgnoreCase(inputValue)) {
				continue;
			}
			
			/**
			 * Metadata Json 파일 저장
			 */
			String cardanoKeysPoolMetadataJsonString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.pool.metadata.json");
			File cardanoKeysPoolMetadataJsonFile = new File(cardanoKeysPoolMetadataJsonString);
			if(cardanoKeysPoolMetadataJsonFile.exists()) {
				cardanoKeysPoolMetadataJsonFile.delete();
			}
			
			File resultFile = CommandExecutor.writeFile(urlContentsLoad, cardanoKeysFolderString, cardanoKeysPoolMetadataJsonString);
			if(resultFile == null) {
				MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("MetaData 파일을 저장하는 도중 에러가 발생했습니다.", "M00058"), true);
				continue;
			}
			
			if(resultFile.length() > 512) {
				MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("MetaData 파일 용량은 512 bytes 이하로 작성해야 합니다. (현재 사이즈 : %s bytes)", "M00059", String.valueOf(resultFile.length())), true);
				continue;
			}
			
			/**
			 * Metadata json 파일 Hash 생성
			 */
			command = CommandExecutor.generateCommand(NodeCommandFormats.POOL_REGISTRATION_GENERATE_METADATA_HASH, cardanoCliName, cardanoKeysPoolMetadataJsonString);
			ProcessResponse initializeProcessBuilder = CommandExecutor.initializeProcessBuilder(command);
			String cardanoKeysPoolMetadataJsonFileHash = initializeProcessBuilder.getSuccessResultString();
			failureResultString = initializeProcessBuilder.getFailureResultString();
			if(failureResultString != null && failureResultString.length() > 0) {
				MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("MetaData 파일의 Hash값을 생성하는 도중 에러가 발생했습니다.", "M00060"), true);
				MessagePrompter.promptMessage(failureResultString, true);
				continue;
			}
			
			MessagePrompter.promptMessage("Metadata Json Hash : " + cardanoKeysPoolMetadataJsonFileHash, true);
			poolModifyDomain.setPoolMetaDataJsonUrl(poolMetaDataJsonUrl);
			poolModifyDomain.setPoolMetaDataHash(cardanoKeysPoolMetadataJsonFileHash.trim());
			poolModifyDomain.setNextOrder(this.getOrder()+1);
			result.setResponseData(poolModifyDomain);
			metadataUrlLoop = false;
		}
	}
}