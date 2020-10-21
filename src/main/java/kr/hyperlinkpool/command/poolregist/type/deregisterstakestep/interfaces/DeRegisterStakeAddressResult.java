package kr.hyperlinkpool.command.poolregist.type.deregisterstakestep.interfaces;

import kr.hyperlinkpool.command.poolregist.type.deregisterstakestep.domains.DeRegisterStakeAddressDomain;
import kr.hyperlinkpool.domain.ProcessResultDomain;

public interface DeRegisterStakeAddressResult {

	public ProcessResultDomain<DeRegisterStakeAddressDomain> getResult();
	
	public void setResult(ProcessResultDomain<DeRegisterStakeAddressDomain> result);
}
