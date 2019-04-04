package com.validationurl.payload;

import java.io.Serializable;

public class PayloadInsertRequest implements Serializable {

	private static final long serialVersionUID = -8556667864135881270L;

	private String client;

	private String regex;

	public PayloadInsertRequest() {
	}

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
