package kr.hyperlinkpool.command;

public class NodeCommandFormats {

	public static final String PAYMENT_KEY_PAIR = ""
			+ "%s"
			+ " address"
			+ " key-gen"
			+ " --verification-key-file %s"
			+ " --signing-key-file %s"
			;
	
	public static final String STAKE_KEY_PAIR = ""
			+ "%s"
			+ " stake-address"
			+ " key-gen"
			+ " --verification-key-file %s"
			+ " --signing-key-file %s"
			;
	
	public static final String PAYMENT_ADDRESS = ""
			+ "%s"
			+ " address"
			+ " build"
			+ " --payment-verification-key-file %s"
			+ " --stake-verification-key-file %s"
			+ " --out-file %s"
			+ " --mainnet"
			;

	public static final String STAKE_ADDRESS = ""
			+ "%s"
			+ " stake-address"
			+ " build"
			+ " --stake-verification-key-file %s"
			+ " --out-file %s"
			+ " --mainnet"
			;
	
	public static final String PAYMENT_ADDRESS_BALANCE_CHECK = ""
			+ "%s"
			+ " query"
			+ " utxo"
			+ " --address %s"
			+ " --mainnet"
			;
	
	public static final String STAKE_ADDRESS_BALANCE_CHECK = ""
			+ "%s"
			+ " query"
			+ " stake-address-info"
			+ " --mainnet"
			+ " --address %s"
			;
	
	public static final String MAINNET_CURRENT_TIP = ""
			+ "%s"
			+ " query"
			+ " tip"
			+ " --mainnet"
			;
	
	public static final String GENERATE_PROTOCOL_FILE = ""
			+ "%s"
			+ " query"
			+ " protocol-parameters"
			+ " --mainnet"
			+ " --out-file %s"
			;

	public static final String DRAFT_TRANSACTION = ""
			+ "%s"
			+ " transaction"
			+ " build-raw"
			+ " %s"
			+ " %s"
			+ " --ttl 0"
			+ " --fee 0"
			+ " --out-file %s"
			;
	
	public static final String CALCULATE_FEE = ""
			+ "%s"
			+ " transaction"
			+ " calculate-min-fee"
			+ " --tx-body-file %s"
			+ " --tx-in-count %s"
			+ " --tx-out-count %s"
			+ " --witness-count %s"
			+ " --byron-witness-count %s"
			+ " --mainnet"
			+ " --protocol-params-file %s"
			;
	
	public static final String BUILD_THE_TRANSACTION = ""
			+ "%s"
			+ " transaction"
			+ " build-raw"
			+ " %s"
			+ " %s"
			+ " --ttl %s"
			+ " --fee %s"
			+ " --out-file %s"
			;
	
	public static final String SIGN_THE_TRANSACTION = ""
			+ "%s"
			+ " transaction"
			+ " sign"
			+ " --tx-body-file %s"
			+ " --signing-key-file %s"
			+ " --mainnet"
			+ " --out-file %s"
			;
	
	public static final String SUBMIT_TRANSACTION = ""
			+ "%s"
			+ " transaction"
			+ " submit"
			+ " --tx-file %s"
			+ " --mainnet"
			;
	
	public static final String STAKE_POOL_CREATE_A_REGISTRATION_CERTIFICATE = ""
			+ "%s"
			+ " stake-address"
			+ " registration-certificate"
			+ " --stake-verification-key-file %s"
			+ " --out-file %s"
			;
	
	public static final String STAKE_POOL_DEREGISTRATION_CERTIFICATE = ""
			+ "%s"
			+ " stake-address"
			+ " deregistration-certificate"
			+ " --stake-verification-key-file %s"
			+ " --out-file %s"
			;
	
	public static final String STAKE_POOL_DELEGATE_STAKE_CERTIFICATE = ""
			+ "%s"
			+ " stake-address"
			+ " delegation-certificate"
			+ " --stake-verification-key-file %s"
			+ " --stake-pool-id %s"
			+ " --out-file %s"
			;
			
	public static final String STAKE_POOL_DRAFT_TRANSACTION = ""
			+ "%s"
			+ " transaction"
			+ " build-raw"
			+ " %s"
			+ " %s"
			+ " --ttl 0"
			+ " --fee 0"
			+ " --out-file %s"
			+ " --certificate-file %s"
			;
	
	public static final String STAKE_POOL_SUBMIT_THE_CERTIFICATE_WITH_A_TRANSACTION = ""
			+ "%s"
			+ " transaction"
			+ " build-raw"
			+ " %s"
			+ " %s"
			+ " --ttl %s"
			+ " --fee %s"
			+ " --out-file %s"
			+ " --certificate-file %s"
			;
	
	public static final String STAKE_POOL_SIGN_THE_TRACSACTION = ""
			+ "%s"
			+ " transaction"
			+ " sign"
			+ " --tx-body-file %s"
			+ " --signing-key-file %s"
			+ " --signing-key-file %s"
			+ " --mainnet"
			+ " --out-file %s"
			;
	
	public static final String POOL_REGISTRATION_GENERATE_COLD_KEY_AND_COLD_COUNTER = ""
			+ "%s"
			+ " node"
			+ " key-gen"
			+ " --cold-verification-key-file %s"
			+ " --cold-signing-key-file %s"
			+ " --operational-certificate-issue-counter-file %s"
			;
	
	public static final String POOL_REGISTRATION_GENERATE_VRF_KEY_PAIR = ""
			+ "%s"
			+ " node"
			+ " key-gen-VRF"
			+ " --verification-key-file %s"
			+ " --signing-key-file %s"
			;
	
	public static final String POOL_REGISTRATION_GENERATE_KES_KEY_PAIR = ""
			+ "%s"
			+ " node"
			+ " key-gen-KES"
			+ " --verification-key-file %s"
			+ " --signing-key-file %s"
			;
	
	public static final String POOL_REGISTRATION_GENERATE_CERTIFICATE = ""
			+ "%s"
			+ " node"
			+ " issue-op-cert"
			+ " --kes-verification-key-file %s"
			+ " --cold-signing-key-file %s"
			+ " --operational-certificate-issue-counter %s"
			+ " --kes-period %s"
			+ " --out-file %s"
			;
	
	public static final String POOL_REGISTRATION_GENERATE_METADATA_HASH = ""
			+ "%s"
			+ " stake-pool"
			+ " metadata-hash"
			+ " --pool-metadata-file"
			+ " %s"
			;
	
	public static final String POOL_REGISTRATION_GENERATE_STAKE_POOL_REGISTRATION_CERTIFICATE = ""
			+ "%s"
			+ " stake-pool"
			+ " registration-certificate"
			+ " --cold-verification-key-file %s"
			+ " --vrf-verification-key-file %s"
			+ " --pool-pledge %s"
			+ " --pool-cost %s"
			+ " --pool-margin %s"
			+ " --pool-reward-account-verification-key-file %s"
			+ " --pool-owner-stake-verification-key-file %s"
			+ " --mainnet"
			+ " %s"
			+ " --metadata-url %s"
			+ " --metadata-hash %s"
			+ " --out-file %s"
			;

	public static final String POOL_REGISTRATION_GENERATE_DELEGATION_CERTIFICATE_PLEDGE = ""
			+ "%s"
			+ " stake-address"
			+ " delegation-certificate"
			+ " --stake-verification-key-file %s"
			+ " --cold-verification-key-file %s"
			+ " --out-file %s"
			;
	
	public static final String POOL_REGISTRATION_DRAFT_THE_TRANSACTION = ""
			+ "%s"
			+ " transaction"
			+ " build-raw"
			+ " %s"
			+ " %s"
			+ " --ttl 0"
			+ " --fee 0"
			+ " --out-file %s"
			+ " --certificate-file %s"
			+ " --certificate-file %s"
			;
	
	public static final String POOL_REGISTRATION_BUILD_THE_TRANSACTION = ""
			+ "%s"
			+ " transaction"
			+ " build-raw"
			+ " %s"
			+ " %s"
			+ " --ttl %s"
			+ " --fee %s"
			+ " --out-file %s"
			+ " --certificate-file %s"
			+ " --certificate-file %s"
			;
	
	public static final String POOL_REGISTRATION_SIGN_THE_TRANSACTION = ""
			+ "%s"
			+ " transaction"
			+ " sign"
			+ " --tx-body-file %s"
			+ " --signing-key-file %s"
			+ " --signing-key-file %s"
			+ " --signing-key-file %s"
			+ " --mainnet"
			+ " --out-file %s"
			;
	
	public static final String POOL_REGISTRATION_GET_POOL_ID = ""
			+ "%s"
			+ " stake-pool"
			+ " id"
			+ " --cold-verification-key-file %s"
			+ " --output-format hex"
			;
	
	public static final String POOL_REGISTRATION_YOUR_POOLID_IN_THE_NETWORK_LEDGER_STATE = ""
			+ "%s"
			+ " query"
			+ " ledger-state"
			+ " --mainnet"
			+ " | grep"
			+ " publicKey"
			+ " | grep"
			+ " %s"
			;
	
	public static final String WITHDRAWING_REWARDS_BALANCE_CHECK = ""
			+ "%s"
			+ " query"
			+ " stake-address-info"
			+ " --mainnet"
			+ " --address %s"
			;
	
	public static final String WITHDRAWING_REWARDS_BUILD_DRAFT_TRANSACTION = ""
			+ "%s"
			+ " transaction"
			+ " build-raw"
			+ " %s"
			+ " %s"
			+ " --withdrawal %s"
			+ " --ttl 0"
			+ " --fee 0"
			+ " --out-file %s"
			;
	
	public static final String WITHDRAWING_REWARDS_BUILD_THE_RAW_TRANSACTION = ""
			+ "%s"
			+ " transaction"
			+ " build-raw"
			+ " %s"
			+ " %s"
			+ " --withdrawal %s"
			+ " --ttl %s"
			+ " --fee %s"
			+ " --out-file %s"
			;
	
	public static final String WITHDRAWING_REWARDS_SIGN_THE_TRANSACTIONS = ""
			+ "%s"
			+ " transaction"
			+ " sign"
			+ " --mainnet"
			+ " --tx-body-file %s"
			+ " --signing-key-file %s"
			+ " --signing-key-file %s"
			+ " --out-file %s"
			;
	
	public static final String POOL_DEREGIST_CREATE_DEREGISTRATION_CERTIFICATE = ""
			+ "%s"
			+ " stake-pool"
			+ " deregistration-certificate"
			+ " --cold-verification-key-file %s"
			+ " --epoch %s"
			+ " --out-file %s"
			;
	
	public static final String POOL_DEREGIST_DRAFT_THE_TRANSACTION = ""
			+ "%s"
			+ " transaction"
			+ " build-raw"
			+ " %s"
			+ " %s"
			+ " --ttl 0"
			+ " --fee 0"
			+ " --out-file %s"
			+ " --certificate-file %s"
			;

	public static final String POOL_DEREGIST_BUILD_THE_TRANSACTION = ""
			+ "%s"
			+ " transaction"
			+ " build-raw"
			+ " %s"
			+ " %s"
			+ " --ttl %s"
			+ " --fee %s"
			+ " --out-file %s"
			+ " --certificate-file %s"
			;
	
	public static final String POOL_DEREGIST_SIGN_THE_TRANSACTION = ""
			+ "%s"
			+ " transaction"
			+ " sign"
			+ " --tx-body-file %s"
			+ " --signing-key-file %s"
			+ " --signing-key-file %s"
			+ " --mainnet"
			+ " --out-file %s"
			;
}