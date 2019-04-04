package com.validationurl.payload;

import java.io.Serializable;

public class PayloadValidationResponse implements Serializable {

	private static final long serialVersionUID = 9022567930709932320L;

	private boolean match;

	private String regex;

	private Integer correlationId;

	public PayloadValidationResponse() {
	}

	public PayloadValidationResponse(Integer correlationId) {
		this.correlationId = correlationId;
	}

	public boolean isMatch() {
		return match;
	}

	public void setMatch(boolean match) {
		this.match = match;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public Integer getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(Integer correlationId) {
		this.correlationId = correlationId;
	}

}
