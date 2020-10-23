package kr.hyperlinkpool.command;

import kr.hyperlinkpool.command.keyandcert.type.*;
import kr.hyperlinkpool.command.balanceandwithdrawing.type.*;
import kr.hyperlinkpool.command.poolregist.type.*;

public enum NodeCommands {
	
	PAYMENT_KEY_PAIR_HANDLER(PaymentKeyPairHandler.class)
	,STAKE_KEY_PAIR_HANDLER(StakeKeyPairHandler.class)
	,PAYMENT_ADDRESS_HANDLER(PaymentAddressHandler.class)
	,STAKE_ADDRESS_HANDLER(StakeAddressHandler.class)
//	,STAKE_POOL_CREATE_A_REGISTRATION_CERTIFICATE_HANDLER(StakePoolCreateAregistrationCertificateHandler.class)
	,POOL_REGISTRATION_GENERATE_COLD_KEY_AND_COLD_COUNTER_HANDLER(PoolRegistrationGenerateColdKeyAndColdCounterHandler.class)
	,POOL_REGISTRATION_GENERATE_VRF_KEY_PAIR_HANDLER(PoolRegistrationGenerateVrfKeyPairHandler.class)
	,POOL_REGISTRATION_GENERATE_KES_KEY_PAIR_HANDLER(PoolRegistrationGenerateKesKeyPairHandler.class)
	,POOL_REGISTRATION_GENERATE_CERTIFICATE_HANDLER(PoolRegistrationGenerateCertificateHandler.class)
	
	,BALANCE_CHECK_STAKEPOOL_ADA_HANDLER(BalanceCheckStakePoolAdaHandler.class)
	,BALANCE_CHECK_ANOTHER_ADDRESS_ADA_HANDLER(BalanceCheckAnotherAddressAdaHandler.class)
	,BALANCE_CHECK_STAKE_POOL_REWARDS_HANDLER(BalanceCheckStakePoolRewardsHandler.class)
	,BALANCE_CHECK_ANOTHER_STAKE_POOL_REWARDS_HANDLER(BalanceCheckAnotherStakePoolRewardsHandler.class)
	,CURRENT_STAKE_POOL_ADA_WITHDRAWING_HANDLER(CurrentStakePoolAdaWithdrawingHandler.class)
	,CURRENT_STAKE_POOL_REWARDS_WITHDRAWING_HANDLER(CurrentStakePoolRewardsWithdrawingHandler.class)
	
	,REGISTER_STAKE_ADDRESS_ON_THE_BLOCKCHAIN_HANDLER(RegisterStakeAddressOnTheBlockchainHandler.class)
	,DEREGISTER_STAKE_ADDRESS_ON_THE_BLOCKCHAIN_HANDLER(DeRegisterStakeAddressOnTheBlockchainHandler.class)
	,DELEGATE_STAKE_ADDRESS_ON_POOL_HANDLER(DelegateStakeAddressOnPoolHandler.class)
	,GENERATE_META_JSON_HANDLER(GenerateMetaJsonHandler.class)
	,REGISTER_POOL_HANDLER(RegisterPoolHandler.class)
	,DEREGISTER_POOL_HANDLER(DeRegisterPoolHandler.class)
	,POOL_STATUS_HANDLER(PoolStatusHandler.class)
	,KES_KEY_ROTATE_HANDLER(KesKeyRotateHandler.class)
	;
	
	private Class<? extends AbstractCommandHandler> clazz;
	
	private NodeCommands( Class<? extends AbstractCommandHandler> clazz) {
		this.clazz = clazz;
	}
	
	public AbstractCommandHandler getHandler() {
		AbstractCommandHandler handler = null;
		try {
			handler = clazz.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return handler;
	}
}