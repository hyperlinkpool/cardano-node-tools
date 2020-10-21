package kr.hyperlinkpool.command.poolregist.type;

import kr.hyperlinkpool.command.AbstractCommandHandler;
import kr.hyperlinkpool.domain.ProcessResponse;
import kr.hyperlinkpool.domain.ProcessResultDomain;
import kr.hyperlinkpool.i18n.MessageFactory;

public class DeRegisterPoolHandler extends AbstractCommandHandler{

	@Override
	public ProcessResultDomain<ProcessResponse> handleCommand(Object... commands) {
		MessageFactory.getInstance().getMessage("개발중입니다.", "E99999");
		return null;
	}

}