package kr.hyperlinkpool.command.balanceandwithdrawing.type;

import java.util.List;
import java.util.Map;

import kr.hyperlinkpool.command.AbstractCommandHandler;
import kr.hyperlinkpool.command.NodeCommandFormats;
import kr.hyperlinkpool.constants.ExitCommand;
import kr.hyperlinkpool.constants.NodeConstants;
import kr.hyperlinkpool.domain.ProcessResponse;
import kr.hyperlinkpool.domain.ProcessResultDomain;
import kr.hyperlinkpool.i18n.MessageFactory;
import kr.hyperlinkpool.properties.NodeProperties;
import kr.hyperlinkpool.utils.CommandExecutor;
import kr.hyperlinkpool.utils.CommandListener;
import kr.hyperlinkpool.utils.MessagePrompter;
import kr.hyperlinkpool.utils.QrcodeGenerator;

public class BalanceCheckAnotherAddressAdaHandler extends AbstractCommandHandler{

	@Override
	public ProcessResultDomain<ProcessResponse> handleCommand(Object... commands) {
		ProcessResultDomain<ProcessResponse> processResultDomain = new ProcessResultDomain<>();
		boolean loop = true; 
		while(loop) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("ADA Wallet 주소를 입력해 주세요.(ex. Ddz... , Ae2... , addr1...)", "M00173"), true);
			String address = CommandListener.getInstance().listenCommand(MessageFactory.getInstance().getMessage("(99 : 뒤로 가기) : ", "M00023"), false);
			try {
				int parseInt = Integer.parseInt(address);
				switch(ExitCommand.getKeyCommand(parseInt)) {
				case EXIT:
					loop = false;
					break;
				}
			} catch (Exception e) {
				if(address != null && address.length() > 0) {
					String cardanoCliName = NodeProperties.getString("cardano.cli.name");
					String command = CommandExecutor.generateCommand(NodeCommandFormats.PAYMENT_ADDRESS_BALANCE_CHECK, cardanoCliName, address);
					ProcessResponse processResponse = CommandExecutor.initializeProcessBuilder(command);
					processResultDomain.setResponseData(processResponse);
					if(processResponse.getFailureResultString() != null && processResponse.getFailureResultString().length() > 0 && processResponse.getFailureResultString().indexOf("invalid address") > -1) {
						processResultDomain.setSuccess(false);
						MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("유효한 주소가 아닙니다.", "M00024"), true);
					}else {
						/**
						 * QR 코드 출력
						 */
						loop = false;
						List<Map<String,String>> parseTxHashString = CommandExecutor.parseTxHashString(processResponse.getSuccessResultString());
						long sumLoveLaceOnTxList = CommandExecutor.sumLoveLaceOnTxList(parseTxHashString);
						/**
						 * QR 코드 출력
						 */
						MessagePrompter.promptMessage(QrcodeGenerator.getQr(address), true);
						MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Pool 주소 : ", "M00026") + address, true);
						MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("보유 수량 : %s", "M00028", String.valueOf(sumLoveLaceOnTxList) + NodeConstants.POSTFIX_LOVELACE), true);
						processResponse.setSuccessResultString(null);
						processResultDomain.setResponseData(processResponse);
						processResultDomain.setSuccess(true);
					}
				}
			}
		}
		
		return processResultDomain;
	}

}
