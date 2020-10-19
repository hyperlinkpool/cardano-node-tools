package kr.hyperlinkpool.command.poolregist.type.registerstakestep.interfaces;

import kr.hyperlinkpool.command.poolregist.type.registerstakestep.domains.RegisterStakeDomain;
import kr.hyperlinkpool.domain.ProcessResultDomain;

public interface RegisterStakeResult {

	public ProcessResultDomain<RegisterStakeDomain> getResult();
	
	public void setResult(ProcessResultDomain<RegisterStakeDomain> result);
}
