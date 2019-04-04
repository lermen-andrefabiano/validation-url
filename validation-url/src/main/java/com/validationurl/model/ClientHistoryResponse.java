package com.validationurl.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "CLIENT_HISTORY_RESPONSE")
public class ClientHistoryResponse implements Serializable {

	private static final long serialVersionUID = -690005034250650465L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CLIENT_HISTORY_RESPONSE_SEQ")
	@SequenceGenerator(sequenceName = "CLIENT_HISTORY_RESPONSE_SEQ", allocationSize = 1, name = "CLIENT_HISTORY_RESPONSE_SEQ")
	private Long id;

	@Column(name = "REGEX", length = 128)
	private String regex;

	@Column(name = "CLIENT", length = 128)
	private String client;

	@Column(name = "MATCH_REG")
	private Boolean match;

	@Column(name = "URL", length = 128, nullable = false)
	private String url;

	@Column(name = "CORRELATION_ID", nullable = false)
	private Integer correlationId;

	public ClientHistoryResponse() {
	}

	public ClientHistoryResponse(String regex, String client, Boolean match, String url, Integer correlationId) {
		this.regex = regex;
		this.client = client;
		this.match = match;
		this.url = url;
		this.correlationId = correlationId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public Boolean getMatch() {
		return match;
	}

	public void setMatch(Boolean match) {
		this.match = match;
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
