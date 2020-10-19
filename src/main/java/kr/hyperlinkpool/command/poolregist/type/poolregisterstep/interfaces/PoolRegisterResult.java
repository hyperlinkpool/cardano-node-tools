package kr.hyperlinkpool.command.poolregist.type.poolregisterstep.interfaces;

import kr.hyperlinkpool.command.poolregist.type.poolregisterstep.domains.PoolRegisterDomain;
import kr.hyperlinkpool.domain.ProcessResultDomain;

public interface PoolRegisterResult {
	
	public ProcessResultDomain<PoolRegisterDomain> getResult();
	
	public void setResult(ProcessResultDomain<PoolRegisterDomain> result);
	
}
