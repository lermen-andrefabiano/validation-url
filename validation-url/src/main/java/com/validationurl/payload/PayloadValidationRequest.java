package com.validationurl.payload;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PayloadValidationRequest implements Serializable {

	private static final long serialVersionUID = 7270633816821827130L;

	@JsonProperty(required = true)
	private String client;

	@JsonProperty(required = true)
	private String url;

	@JsonProperty(required = true)
	private Integer correlationId;

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(Integer correlationId) {
		this.correlationId = correlationId;
	}

}
