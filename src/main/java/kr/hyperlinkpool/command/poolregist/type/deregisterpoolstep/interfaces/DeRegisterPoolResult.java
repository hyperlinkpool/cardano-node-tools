package kr.hyperlinkpool.command.poolregist.type.deregisterpoolstep.interfaces;

import kr.hyperlinkpool.command.poolregist.type.deregisterpoolstep.domains.DeRegisterPoolDomain;
import kr.hyperlinkpool.domain.ProcessResultDomain;

public interface DeRegisterPoolResult {
	
	public ProcessResultDomain<DeRegisterPoolDomain> getResult();
	
	public void setResult(ProcessResultDomain<DeRegisterPoolDomain> result);
	
}
