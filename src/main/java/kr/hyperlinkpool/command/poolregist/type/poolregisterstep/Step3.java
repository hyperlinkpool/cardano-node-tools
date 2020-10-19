package kr.hyperlinkpool.command.poolregist.type.poolregisterstep;

import kr.hyperlinkpool.command.NodeCommandFormats;
import kr.hyperlinkpool.command.interfaces.Ordered;
import kr.hyperlinkpool.command.poolregist.type.poolregisterstep.domains.PoolRegisterDomain;
import kr.hyperlinkpool.command.poolregist.type.poolregisterstep.interfaces.PoolRegisterResult;
import kr.hyperlinkpool.constants.NodeConstants;
import kr.hyperlinkpool.constants.StepOrder;
import kr.hyperlinkpool.domain.ProcessResponse;
import kr.hyperlinkpool.domain.ProcessResultDomain;
import kr.hyperlinkpool.i18n.MessageFactory;
import kr.hyperlinkpool.properties.NodeProperties;
import kr.hyperlinkpool.utils.CommandExecutor;
import kr.hyperlinkpool.utils.CommonUtils;
import kr.hyperlinkpool.utils.MessagePrompter;

public class Step3 implements PoolRegisterResult, Ordered, Runnable{

	private PoolRegisterDomain poolRegisterDomain;
	
	private ProcessResultDomain<PoolRegisterDomain> result;
	
	@Override
	public int getOrder() {
		return StepOrder.STEP3.getStepOrder();
	}

	@Override
	public ProcessResultDomain<PoolRegisterDomain> getResult() {
		return result;
	}

	@Override
	public void setResult(ProcessResultDomain<PoolRegisterDomain> result) {
		this.result = result;
		this.setPoolRegisterDomain(result.getResponseData());
	}

	public PoolRegisterDomain getPoolRegisterDomain() {
		return poolRegisterDomain;
	}

	public void setPoolRegisterDomain(PoolRegisterDomain poolRegisterDomain) {
		this.poolRegisterDomain = poolRegisterDomain;
	}
	
	@Override
	public void run() {
		/**
		 * Step. 3
		 * Pool 인증서 발급
		 */
		String command = null;
		String cardanoCliName = NodeProperties.getString("cardano.cli.name");
		String cardanoKeysFolderString = NodeProperties.getString("cardano.keys.folder");
		
		/**
		 * cold.vkey
		 */
		String cardanoKeysColdVkeyFileString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.cold.vkey");
		
		/**
		 * vrf.vkey
		 */
		String cardanoKeysVrfVkeyFileString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.vrf.vkey");
		
		/**
		 * stake.vkey
		 */
		String cardanoKeysStakeVkeyFileString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.stake.vkey");
		
		/**
		 * Pool 정보
		 */
		long poolPledge = poolRegisterDomain.getPoolPledge();
		long poolCost = poolRegisterDomain.getPoolCost();
		double poolMargin = poolRegisterDomain.getPoolMargin();
		String nodeInfoString = CommonUtils.nodeInfoParse(poolRegisterDomain.getRelayNodesInfo());
		String poolMetaDataJsonUrl = poolRegisterDomain.getPoolMetaDataJsonUrl();
		String poolMetaDataHash = poolRegisterDomain.getPoolMetaDataHash();
		
		/**
		 * pool-registration.cert 파일 경로
		 */
		String cardanoKeysPoolRegistrationCertFileString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.pool.registration.cert");
		command = CommandExecutor.generateCommand(NodeCommandFormats.POOL_REGISTRATION_GENERATE_STAKE_POOL_REGISTRATION_CERTIFICATE
				,cardanoCliName
				,cardanoKeysColdVkeyFileString
				,cardanoKeysVrfVkeyFileString
				,String.valueOf(poolPledge)
				,String.valueOf(poolCost)
				,String.valueOf(poolMargin)
				,cardanoKeysStakeVkeyFileString
				,cardanoKeysStakeVkeyFileString
				,nodeInfoString
				,poolMetaDataJsonUrl
				,poolMetaDataHash
				,cardanoKeysPoolRegistrationCertFileString
				);
		ProcessResponse processBuilder = CommandExecutor.initializeProcessBuilder(command);
		String failureResultString = processBuilder.getFailureResultString();
		if(failureResultString != null && failureResultString.length() > 0) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Pool 등록 인증서 생성중 오류가 발생했습니다.", "M00078"), true);
			poolRegisterDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
			result.setSuccess(false);
		}
		
		/**
		 * Generate delegation certificate pledge
		 */
		String cardanoKeysPoolDelegationCertFileString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.pool.delegation.cert");
		command = CommandExecutor.generateCommand(NodeCommandFormats.POOL_REGISTRATION_GENERATE_DELEGATION_CERTIFICATE_PLEDGE, cardanoCliName, cardanoKeysStakeVkeyFileString, cardanoKeysColdVkeyFileString, cardanoKeysPoolDelegationCertFileString);
		CommandExecutor.initializeProcessBuilder(command);
		failureResultString = processBuilder.getFailureResultString();
		if(failureResultString != null && failureResultString.length() > 0) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Pool Delegation 인증서 생성중 오류가 발생했습니다.", "M00079"), true);
			poolRegisterDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
			result.setSuccess(false);
		}
		
		poolRegisterDomain.setNextOrder(this.getOrder()+1);
		result.setSuccess(true);
	}
}