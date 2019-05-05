package com.validationurl.payload;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PayloadInsertRequest implements Serializable {

	private static final long serialVersionUID = -8556667864135881270L;

	@JsonProperty
	private String client;

	@JsonProperty(required = true)
	private String regex;

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

}
