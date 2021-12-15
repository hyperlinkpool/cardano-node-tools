package kr.hyperlinkpool.command.poolregist.type.poolmodifystep.interfaces;

import kr.hyperlinkpool.command.poolregist.type.poolmodifystep.domains.PoolModifyDomain;
import kr.hyperlinkpool.domain.ProcessResultDomain;

public interface PoolModifyResult {
	
	public ProcessResultDomain<PoolModifyDomain> getResult();
	
	public void setResult(ProcessResultDomain<PoolModifyDomain> result);
	
}
