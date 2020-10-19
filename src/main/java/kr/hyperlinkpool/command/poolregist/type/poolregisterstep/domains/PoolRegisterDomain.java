package kr.hyperlinkpool.command.poolregist.type.poolregisterstep.domains;

import java.util.List;
import java.util.Map;

public class PoolRegisterDomain {
	
	private int nextOrder;
	
	private List<Map<String,String>> parseTxHashList = null;
	
	private String receiveAddress = null;
	
	private String senderAddress = null;
	
	private String successResultString = null;
	
	private String failureResultString = null;

	private long sumLovelace;
	
	private long receiverTotal;
	
	private String txIn;
	
	private String txOut;
	
	private long txFee;
	
	private boolean isTxFeeReCalc;
	
	private long poolDeposit;
	
	private String poolMetaDataJsonUrl;
	
	private String poolMetaDataHash;
	
	private long poolPledge;
	
	private long poolCost;
	
	private double poolMargin;
	
	private int relayNodeCount;
	
	private String[][] relayNodesInfo;
	
	public String getSuccessResultString() {
		return successResultString;
	}

	public void setSuccessResultString(String successResultString) {
		this.successResultString = successResultString;
	}

	public String getFailureResultString() {
		return failureResultString;
	}

	public void setFailureResultString(String failureResultString) {
		this.failureResultString = failureResultString;
	}
	
	public int getNextOrder() {
		return nextOrder;
	}

	public void setNextOrder(int nextOrder) {
		this.nextOrder = nextOrder;
	}

	public List<Map<String,String>> getParseTxHashList() {
		return parseTxHashList;
	}

	public void setParseTxHashList(List<Map<String,String>> parseTxHashList) {
		this.parseTxHashList = parseTxHashList;
	}

	public String getReceiveAddress() {
		return receiveAddress;
	}

	public void setReceiveAddress(String receiveAddress) {
		this.receiveAddress = receiveAddress;
	}

	public String getSenderAddress() {
		return senderAddress;
	}

	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}

	public long getTxFee() {
		return txFee;
	}

	public void setTxFee(long txFee) {
		this.txFee = txFee;
	}

	public long getReceiverTotal() {
		return receiverTotal;
	}

	public void setReceiverTotal(long receiverTotal) {
		this.receiverTotal = receiverTotal;
	}

	public long getSumLovelace() {
		return sumLovelace;
	}

	public void setSumLovelace(long sumLovelace) {
		this.sumLovelace = sumLovelace;
	}

	public String getTxIn() {
		return txIn;
	}

	public void setTxIn(String txIn) {
		this.txIn = txIn;
	}

	public String getTxOut() {
		return txOut;
	}

	public void setTxOut(String txOut) {
		this.txOut = txOut;
	}

	public boolean isTxFeeReCalc() {
		return isTxFeeReCalc;
	}

	public void setTxFeeReCalc(boolean isTxFeeReCalc) {
		this.isTxFeeReCalc = isTxFeeReCalc;
	}

	public String getPoolMetaDataHash() {
		return poolMetaDataHash;
	}

	public void setPoolMetaDataHash(String poolMetaDataHash) {
		this.poolMetaDataHash = poolMetaDataHash;
	}

	public long getPoolPledge() {
		return poolPledge;
	}

	public void setPoolPledge(long poolPledge) {
		this.poolPledge = poolPledge;
	}

	public long getPoolCost() {
		return poolCost;
	}

	public void setPoolCost(long poolCost) {
		this.poolCost = poolCost;
	}

	public double getPoolMargin() {
		return poolMargin;
	}

	public void setPoolMargin(double poolMargin) {
		this.poolMargin = poolMargin;
	}

	public int getRelayNodeCount() {
		return relayNodeCount;
	}

	public void setRelayNodeCount(int relayNodeCount) {
		this.relayNodeCount = relayNodeCount;
	}

	public String getPoolMetaDataJsonUrl() {
		return poolMetaDataJsonUrl;
	}

	public void setPoolMetaDataJsonUrl(String poolMetaDataJsonUrl) {
		this.poolMetaDataJsonUrl = poolMetaDataJsonUrl;
	}

	public String[][] getRelayNodesInfo() {
		return relayNodesInfo;
	}

	public void setRelayNodesInfo(String[][] relayNodesInfo) {
		this.relayNodesInfo = relayNodesInfo;
	}

	public long getPoolDeposit() {
		return poolDeposit;
	}

	public void setPoolDeposit(long poolDeposit) {
		this.poolDeposit = poolDeposit;
	}
}
