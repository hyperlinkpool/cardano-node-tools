package kr.hyperlinkpool.domain;

public class ProcessResultDomain <T> {
	private boolean success = false;
	private T responseData;
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public T getResponseData() {
		return responseData;
	}
	public void setResponseData(T responseData) {
		this.responseData = responseData;
	}

}