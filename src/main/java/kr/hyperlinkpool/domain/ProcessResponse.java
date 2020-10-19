package kr.hyperlinkpool.domain;

public class ProcessResponse {

	private String successResultString;
	
	private String failureResultString;

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
	
}
