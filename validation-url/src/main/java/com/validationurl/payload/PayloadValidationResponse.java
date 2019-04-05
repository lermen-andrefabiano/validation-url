package com.validationurl.payload;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PayloadValidationResponse implements Serializable {

	private static final long serialVersionUID = 9022567930709932320L;

	@JsonProperty(required = true)
	private boolean match;

	@JsonProperty(required = true)
	private String regex;

	@JsonProperty(required = true)
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
