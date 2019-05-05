package com.validationurl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.validationurl.model.WhiteList;
import com.validationurl.model.WhiteListGlobal;
import com.validationurl.payload.PayloadValidationResponse;
import com.validationurl.repository.WhiteListGlobalRepository;
import com.validationurl.repository.WhiteListRepository;
import com.validationurl.validation.ValidationUrl;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ValidationUrl.class })
public class ValidationUrlTest {

	@MockBean
	private WhiteListGlobalRepository whiteListGlobalRep;

	@MockBean
	private WhiteListRepository whiteListRep;

	@Autowired
	@InjectMocks
	private ValidationUrl validationUrl;

	private String client;
	
	private String clientMatch;

	private String url;
	
	private String urlGlobal;

	private String regex;

	private String regexGlobal;

	private Integer correlationId;

	@Before
	public void init() {
		this.client = "client rabbit";
		this.url = "www.google.com";
		this.correlationId = 9;
		this.regex = "(?i)www.google.com";
		this.regexGlobal = "[0-9]+";
		this.urlGlobal = "965356";
		this.clientMatch = "Client March";
	}

	@Test
	public void validationUrlClientPositive() {
		List<WhiteList> whiteListClient = this.obterWhileList();
		when(this.whiteListRep.findByClient(this.client)).thenReturn(whiteListClient);

		List<WhiteListGlobal> whiteListClientGlobal = this.obterWhileListGlobal();
		when(this.whiteListGlobalRep.findAll()).thenReturn(whiteListClientGlobal);

		PayloadValidationResponse response = this.validationUrl.validationUrlClient(this.client, this.url,
				this.correlationId);

		assertEquals(response.isMatch(), Boolean.TRUE);

	}

	@Test
	public void validationUrlClientNegative() {
		when(this.whiteListRep.findByClient(this.clientMatch)).thenReturn(new ArrayList<>());

		List<WhiteListGlobal> whiteListClientGlobal = this.obterWhileListGlobal();
		when(this.whiteListGlobalRep.findAll()).thenReturn(whiteListClientGlobal);

		PayloadValidationResponse response = this.validationUrl.validationUrlClient(this.clientMatch, this.url,
				this.correlationId);

		assertEquals(response.isMatch(), Boolean.FALSE);
		
		assertNull(response.getRegex());

	}
	
	@Test
	public void validationUrlClientPositiveGlobal() {
		when(this.whiteListRep.findByClient(this.clientMatch)).thenReturn(new ArrayList<>());

		List<WhiteListGlobal> whiteListClientGlobal = this.obterWhileListGlobal();
		when(this.whiteListGlobalRep.findAll()).thenReturn(whiteListClientGlobal);

		PayloadValidationResponse response = this.validationUrl.validationUrlClient(this.clientMatch, this.urlGlobal,
				this.correlationId);

		assertEquals(response.isMatch(), Boolean.TRUE);
		
	}

	private List<WhiteList> obterWhileList() {
		List<WhiteList> whiteListClient = new ArrayList<>();

		whiteListClient.add(new WhiteList(this.client, this.regex));
		whiteListClient.add(new WhiteList(this.client, this.regexGlobal));

		return whiteListClient;
	}

	private List<WhiteListGlobal> obterWhileListGlobal() {
		List<WhiteListGlobal> whiteListClientGlobal = new ArrayList<>();

		whiteListClientGlobal.add(new WhiteListGlobal(this.regexGlobal));

		return whiteListClientGlobal;
	}

}
