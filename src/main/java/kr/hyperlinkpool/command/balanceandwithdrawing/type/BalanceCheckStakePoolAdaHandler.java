package kr.hyperlinkpool.command.balanceandwithdrawing.type;

import java.io.File;
import java.util.List;
import java.util.Map;

import kr.hyperlinkpool.command.AbstractCommandHandler;
import kr.hyperlinkpool.command.NodeCommandFormats;
import kr.hyperlinkpool.constants.NodeConstants;
import kr.hyperlinkpool.domain.ProcessResponse;
import kr.hyperlinkpool.domain.ProcessResultDomain;
import kr.hyperlinkpool.i18n.MessageFactory;
import kr.hyperlinkpool.properties.NodeProperties;
import kr.hyperlinkpool.utils.CommandExecutor;
import kr.hyperlinkpool.utils.MessagePrompter;
import kr.hyperlinkpool.utils.QrcodeGenerator;

public class BalanceCheckStakePoolAdaHandler extends AbstractCommandHandler{

	@Override
	public ProcessResultDomain<ProcessResponse> handleCommand(Object... commands) {
		ProcessResultDomain<ProcessResponse> processResultDomain = new ProcessResultDomain<>();
		
		String cardanoKeysFolderString = NodeProperties.getString("cardano.keys.folder");
		String cardanoKeysPaymentAddressString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.payment.addr");
		
		File cardanoKeysPaymentAddressFile = new File(cardanoKeysPaymentAddressString);
		if(!cardanoKeysPaymentAddressFile.exists()) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Payment 주소가 존재하지 않습니다. Payment 주소 생성 후 다시 시도하세요.", "M00025"), true);
			processResultDomain.setSuccess(false);
			return processResultDomain;
		}
		
		String paymentAddressString = null;
		paymentAddressString = CommandExecutor.readFile(cardanoKeysPaymentAddressFile);
		String cardanoCliName = NodeProperties.getString("cardano.cli.name");
		String command = CommandExecutor.generateCommand(NodeCommandFormats.PAYMENT_ADDRESS_BALANCE_CHECK, cardanoCliName, paymentAddressString);
		ProcessResponse processResponse = CommandExecutor.initializeProcessBuilder(command);
		processResultDomain.setResponseData(processResponse);
		if(processResponse.getFailureResultString() != null && processResponse.getFailureResultString().length() > 0) {
			processResultDomain.setSuccess(false);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("프로세스 수행중 오류가 발생했습니다. : ", "M00010") + processResponse.getFailureResultString(), true);
		}else {
			List<Map<String,String>> parseTxHashString = CommandExecutor.parseTxHashString(processResponse.getSuccessResultString());
			long sumLoveLaceOnTxList = CommandExecutor.sumLoveLaceOnTxList(parseTxHashString);
			/**
			 * QR 코드 출력
			 */
			MessagePrompter.promptMessage(QrcodeGenerator.getQr(paymentAddressString), true);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Pool 주소 : ", "M00026") + paymentAddressString, true);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("보유 수량 : %s", "M00028", String.valueOf(sumLoveLaceOnTxList) + NodeConstants.POSTFIX_LOVELACE), true);
			processResponse.setSuccessResultString(null);
			processResultDomain.setSuccess(true);
		}
		
		return processResultDomain;
	}

}