package kr.hyperlinkpool.command.poolregist.type.delegatestep.interfaces;

import kr.hyperlinkpool.command.poolregist.type.delegatestep.domains.DelegateStakeAddressDomain;
import kr.hyperlinkpool.domain.ProcessResultDomain;

public interface DelegateStakeAddressResult {

	public ProcessResultDomain<DelegateStakeAddressDomain> getResult();
	
	public void setResult(ProcessResultDomain<DelegateStakeAddressDomain> result);
}
