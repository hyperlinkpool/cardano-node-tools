package kr.hyperlinkpool.command.balanceandwithdrawing.rewardswithdrawstep.domains;

import java.util.List;
import java.util.Map;

public class RewardsWithdrawDomain {

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
	
	private long withdrawAmount;
	
	private long withdrawableAmount;
	
	private String stakeAddress;
	
	private String withdrawal;
	
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

	public long getWithdrawableAmount() {
		return withdrawableAmount;
	}

	public void setWithdrawableAmount(long withdrawableAmount) {
		this.withdrawableAmount = withdrawableAmount;
	}

	public long getWithdrawAmount() {
		return withdrawAmount;
	}

	public void setWithdrawAmount(long withdrawAmount) {
		this.withdrawAmount = withdrawAmount;
	}

	public String getStakeAddress() {
		return stakeAddress;
	}

	public void setStakeAddress(String stakeAddress) {
		this.stakeAddress = stakeAddress;
	}

	public String getWithdrawal() {
		return withdrawal;
	}

	public void setWithdrawal(String withdrawal) {
		this.withdrawal = withdrawal;
	}
	
}