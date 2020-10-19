package kr.hyperlinkpool.command;

import kr.hyperlinkpool.domain.ProcessResponse;
import kr.hyperlinkpool.domain.ProcessResultDomain;

public abstract class AbstractCommandHandler {

	public abstract ProcessResultDomain <ProcessResponse> handleCommand(Object ... commands);
	
}