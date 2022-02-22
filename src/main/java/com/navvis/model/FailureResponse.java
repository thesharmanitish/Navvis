package com.navvis.model;

public class FailureResponse implements GPPResponse{
	
	private String msg;
	
	private String errorCode;
	
	public FailureResponse(String msg, String errorCode) {
		super();
		this.msg = msg;
		this.errorCode = errorCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}


}