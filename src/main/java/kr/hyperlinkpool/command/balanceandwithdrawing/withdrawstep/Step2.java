package kr.hyperlinkpool.command.balanceandwithdrawing.withdrawstep;

import java.util.List;
import java.util.Map;

import kr.hyperlinkpool.command.NodeCommandFormats;
import kr.hyperlinkpool.command.balanceandwithdrawing.withdrawstep.domains.WithdrawDomain;
import kr.hyperlinkpool.command.balanceandwithdrawing.withdrawstep.interfaces.WithdrawResult;
import kr.hyperlinkpool.command.interfaces.Ordered;
import kr.hyperlinkpool.constants.ExitCommand;
import kr.hyperlinkpool.constants.NodeConstants;
import kr.hyperlinkpool.constants.StepOrder;
import kr.hyperlinkpool.domain.ProcessResponse;
import kr.hyperlinkpool.domain.ProcessResultDomain;
import kr.hyperlinkpool.i18n.MessageFactory;
import kr.hyperlinkpool.properties.NodeProperties;
import kr.hyperlinkpool.utils.CommandExecutor;
import kr.hyperlinkpool.utils.CommandListener;
import kr.hyperlinkpool.utils.MessagePrompter;

public class Step2 implements WithdrawResult, Ordered, Runnable{

	private WithdrawDomain withdrawDomain;
	
	private ProcessResultDomain<WithdrawDomain> result;
	
	@Override
	public int getOrder() {
		return StepOrder.STEP2.getStepOrder();
	}

	@Override
	public ProcessResultDomain<WithdrawDomain> getResult() {
		return result;
	}

	@Override
	public void setResult(ProcessResultDomain<WithdrawDomain> result) {
		this.result = result;
		this.withdrawDomain = result.getResponseData();
	}
	
	public WithdrawDomain getWithdrawDomain() {
		return withdrawDomain;
	}

	public void setWithdrawDomain(WithdrawDomain withdrawDomain) {
		this.withdrawDomain = withdrawDomain;
	}
	
	@Override
	public void run() {
		/**
		 * Step 2
		 * 전송 주소 입력 및 검증
		 */
		List<Map<String,String>> parseTxHashList = withdrawDomain.getParseTxHashList();
		long sumLovelace = CommandExecutor.sumLoveLaceOnTxList(parseTxHashList);
		MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("보유 수량 : %s", "M00028", String.valueOf(sumLovelace) + NodeConstants.POSTFIX_LOVELACE), true);
		
		/**
		 * Draft The Transaction
		 */
		boolean loop = true;
		while(loop) {
			String address = CommandListener.getInstance().listenCommand(MessageFactory.getInstance().getMessage("보낼 주소를 입력해 주세요.(99번 뒤로 가기) : ", "M00029"), false);
			try {
				int parseInt = Integer.parseInt(address);
				switch(ExitCommand.getKeyCommand(parseInt)) {
				case EXIT:
					loop = false;
					withdrawDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
					break;
				}
			} catch (Exception e) {
				if(address != null && address.length() > 0) {
					String cardanoCliName = NodeProperties.getString("cardano.cli.name");
					String command = CommandExecutor.generateCommand(NodeCommandFormats.PAYMENT_ADDRESS_BALANCE_CHECK, cardanoCliName, address);
					ProcessResponse processResponse = CommandExecutor.initializeProcessBuilder(command);
					if(processResponse.getFailureResultString() != null && processResponse.getFailureResultString().length() > 0 && processResponse.getFailureResultString().indexOf("invalid address") > -1) {
						MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("유효한 주소가 아닙니다.", "M00024"), true);
						result.setSuccess(false);
						withdrawDomain.setNextOrder(this.getOrder()-1);
					}else {
						MessagePrompter.promptMessage(processResponse.getSuccessResultString(), true);
						result.setSuccess(true);
						withdrawDomain.setNextOrder(this.getOrder()+1);
						withdrawDomain.setReceiveAddress(address);
						loop = false;
					}
					result.setResponseData(withdrawDomain);
				}
			}
		}
	}
}