package kr.hyperlinkpool.command.poolregist.type.deregisterpoolstep.domains;

import java.util.List;
import java.util.Map;

public class DeRegisterPoolDomain {
	
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
	
	private long currentEpoch;
	
	private long epochLength;
	
	private int eMax;
	
	private long retireEpoch;
	
	private long totalEpoch;
	
	private long poolDeposit;
	
	private long inputEpoch;
	
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

	public long getCurrentEpoch() {
		return currentEpoch;
	}

	public void setCurrentEpoch(long currentEpoch) {
		this.currentEpoch = currentEpoch;
	}

	public long getEpochLength() {
		return epochLength;
	}

	public void setEpochLength(long epochLength) {
		this.epochLength = epochLength;
	}

	public int geteMax() {
		return eMax;
	}

	public void seteMax(int eMax) {
		this.eMax = eMax;
	}

	public long getRetireEpoch() {
		return retireEpoch;
	}

	public void setRetireEpoch(long retireEpoch) {
		this.retireEpoch = retireEpoch;
	}

	public long getTotalEpoch() {
		return totalEpoch;
	}

	public void setTotalEpoch(long totalEpoch) {
		this.totalEpoch = totalEpoch;
	}

	public long getPoolDeposit() {
		return poolDeposit;
	}

	public void setPoolDeposit(long poolDeposit) {
		this.poolDeposit = poolDeposit;
	}

	public long getInputEpoch() {
		return inputEpoch;
	}

	public void setInputEpoch(long inputEpoch) {
		this.inputEpoch = inputEpoch;
	}

}
