package kr.hyperlinkpool.command.balanceandwithdrawing.rewardswithdrawstep.interfaces;

import kr.hyperlinkpool.command.balanceandwithdrawing.rewardswithdrawstep.domains.RewardsWithdrawDomain;
import kr.hyperlinkpool.domain.ProcessResultDomain;

public interface RewardsWithdrawResult {

	public ProcessResultDomain<RewardsWithdrawDomain> getResult();
	
	public void setResult(ProcessResultDomain<RewardsWithdrawDomain> result);
}
