package kr.hyperlinkpool.command.balanceandwithdrawing.type;

import kr.hyperlinkpool.command.AbstractCommandHandler;
import kr.hyperlinkpool.command.balanceandwithdrawing.rewardswithdrawstep.Step1;
import kr.hyperlinkpool.command.balanceandwithdrawing.rewardswithdrawstep.Step2;
import kr.hyperlinkpool.command.balanceandwithdrawing.rewardswithdrawstep.Step3;
import kr.hyperlinkpool.command.balanceandwithdrawing.rewardswithdrawstep.domains.RewardsWithdrawDomain;
import kr.hyperlinkpool.constants.StepOrder;
import kr.hyperlinkpool.domain.ProcessResponse;
import kr.hyperlinkpool.domain.ProcessResultDomain;

public class CurrentStakePoolRewardsWithdrawingHandler extends AbstractCommandHandler {

	@Override
	public ProcessResultDomain<ProcessResponse> handleCommand(Object... commands) {
		Step1 step1 = new Step1();
		Step2 step2 = new Step2();
		Step3 step3 = new Step3();
		boolean loop = true;
		RewardsWithdrawDomain rewardsWithdrawDomain = new RewardsWithdrawDomain();
		ProcessResultDomain<RewardsWithdrawDomain> result = new ProcessResultDomain<RewardsWithdrawDomain>();
		result.setResponseData(rewardsWithdrawDomain);
		int start = 1;
		while (loop) {
			switch (StepOrder.getStepOrder(start)) {
			case STEP1:
				step1.setResult(result);
				step1.run();
				result = step1.getResult();
				break;
			case STEP2:
				step2.setResult(result);
				step2.run();
				result = step2.getResult();
				break;
			case STEP3:
				step3.setResult(result);
				step3.run();
				result = step3.getResult();
				break;
			case STEP4:
				break;
			case STEP5:
				break;
			case EXIT:
				loop = false;
				break;
			}
			start = result.getResponseData().getNextOrder();
		}
		
		ProcessResultDomain<ProcessResponse> processResultDomain = new ProcessResultDomain<ProcessResponse>();
		ProcessResponse processResponse = new ProcessResponse();
		processResponse.setSuccessResultString(result.getResponseData().getSuccessResultString());
		processResponse.setFailureResultString(result.getResponseData().getFailureResultString());
		processResultDomain.setSuccess(result.isSuccess());
		processResultDomain.setResponseData(processResponse);
		return processResultDomain;
	}
}