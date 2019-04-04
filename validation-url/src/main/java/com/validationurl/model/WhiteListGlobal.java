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
@Table(name = "WHITE_LIST_GLOBAL")
public class WhiteListGlobal implements Serializable {

	private static final long serialVersionUID = 2286111503149711662L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WHITE_LIST_GLOBAL_SEQ")
	@SequenceGenerator(sequenceName = "WHITE_LIST_GLOBAL_SEQ", allocationSize = 1, name = "WHITE_LIST_GLOBAL_SEQ")
	private Long id;

	@Column(name = "REGEX", length = 128, nullable = false)
	private String regex;

	public WhiteListGlobal() {
	}

	public WhiteListGlobal(String regex) {
		this.regex = regex;
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

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder(this.id.toString());
		str.append(" ");
		str.append(" ");
		str.append(this.regex);
		return str.toString();
	}

}
