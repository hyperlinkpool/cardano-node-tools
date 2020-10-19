package kr.hyperlinkpool.command.balanceandwithdrawing.withdrawstep.interfaces;

import kr.hyperlinkpool.command.balanceandwithdrawing.withdrawstep.domains.WithdrawDomain;
import kr.hyperlinkpool.domain.ProcessResultDomain;

public interface WithdrawResult {

	public ProcessResultDomain<WithdrawDomain> getResult();
	
	public void setResult(ProcessResultDomain<WithdrawDomain> result);
}
