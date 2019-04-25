package com.validationurl;

public class BusinessException extends Exception {

	private static final long serialVersionUID = 4050129104178537370L;

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

}
