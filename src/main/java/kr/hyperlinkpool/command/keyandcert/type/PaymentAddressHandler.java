package kr.hyperlinkpool.command.keyandcert.type;

import java.io.File;

import kr.hyperlinkpool.command.AbstractCommandHandler;
import kr.hyperlinkpool.command.NodeCommandFormats;
import kr.hyperlinkpool.constants.NodeConstants;
import kr.hyperlinkpool.domain.ProcessResponse;
import kr.hyperlinkpool.domain.ProcessResultDomain;
import kr.hyperlinkpool.i18n.MessageFactory;
import kr.hyperlinkpool.properties.NodeProperties;
import kr.hyperlinkpool.utils.CommandExecutor;
import kr.hyperlinkpool.utils.MessagePrompter;

public class PaymentAddressHandler extends AbstractCommandHandler{
	
	@Override
	public ProcessResultDomain <ProcessResponse> handleCommand(Object ... commands) {
		ProcessResultDomain<ProcessResponse> processResultDomain = new ProcessResultDomain<>();
		
		String cardanoKeysFolderString = NodeProperties.getString("cardano.keys.folder");
		String cardanoKeysPaymentVkeyString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.payment.vkey");
		String cardanoKeysStakeVkeyString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.stake.vkey");
		String cardanoKeysPaymentAddressOutputFilePath = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.payment.addr");
				
		File cardanoKeysFolder = new File(cardanoKeysFolderString);
		if(!cardanoKeysFolder.exists()) {
			cardanoKeysFolder.mkdirs();
		}
		
		File cardanoKeysPaymentAddressOutputFile = new File(cardanoKeysPaymentAddressOutputFilePath);
		if(cardanoKeysPaymentAddressOutputFile.exists()) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("이미 파일이 존재합니다. 삭제 후 다시 시도하세요.", "M00039"), true);
			return null;
		}
		
		File cardanoKeysPaymentVkeyFile = new File(cardanoKeysPaymentVkeyString);
		File cardanoKeysStakeVkeyFile = new File(cardanoKeysStakeVkeyString);
		if(!cardanoKeysPaymentVkeyFile.exists()) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Payment Verification Key가 생성되지 않았습니다. 키 생성 후 다시 시도하세요.", "M00040"), true);
			processResultDomain.setSuccess(false);
			return processResultDomain;
		}
		
		if(!cardanoKeysStakeVkeyFile.exists()) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Stake Verification Key가 생성되지 않았습니다. 키 생성 후 다시 시도하세요.", "M00041"), true);
			processResultDomain.setSuccess(false);
			return processResultDomain;
		}
		
		String command = null;
		String cardanoCliName = NodeProperties.getString("cardano.cli.name");
		command = CommandExecutor.generateCommand(NodeCommandFormats.PAYMENT_ADDRESS, cardanoCliName, cardanoKeysPaymentVkeyString, cardanoKeysStakeVkeyString, cardanoKeysPaymentAddressOutputFilePath);
		ProcessResponse processResponse = CommandExecutor.initializeProcessBuilder(command);
		if(processResponse.getFailureResultString() != null && processResponse.getFailureResultString().length() > 0) {
			processResultDomain.setSuccess(false);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("프로세스 수행중 오류가 발생했습니다. : ", "M00010") + processResponse.getFailureResultString(), true);
			return processResultDomain;
		}
		
		processResponse.setSuccessResultString(MessageFactory.getInstance().getMessage("정상 처리되었습니다.", "M00042"));
		processResultDomain.setResponseData(processResponse);
		processResultDomain.setSuccess(true);
		return processResultDomain;
	}
}
