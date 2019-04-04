package com.validationurl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.validationurl.consumer.ValidationUrlConsumer;
import com.validationurl.model.ClientHistoryResponse;
import com.validationurl.payload.PayloadValidationResponse;
import com.validationurl.repository.ClientHistoryResponseRepository;
import com.validationurl.repository.WhiteListGlobalRepository;
import com.validationurl.repository.WhiteListRepository;
import com.validationurl.validation.ValidationUrl;
import com.validationurl.validation.ValidationUrlResponse;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ValidationUrlConsumer.class })
public class ValidationUrlConsumerTest {

	@Autowired
	@InjectMocks
	private ValidationUrlConsumer validationUrlConsumer;

	@MockBean
	private ValidationUrl validationUrl;

	@MockBean
	private ValidationUrlResponse responseValidationUrl;

	@MockBean
	private WhiteListGlobalRepository whiteListGlobalRep;

	@MockBean
	private WhiteListRepository whiteListRep;

	@MockBean
	private ClientHistoryResponseRepository clientHistoryResponseRep;

	private String client;

	private String url;

	private String regex;

	private Integer correlationId;

	private static final String PAYLOAD = "{\"client\": \"client rabbit\", \"url\": \"www.google.com\", \"correlationId\": 9}";

	@Before
	public void init() {
		this.client = "client rabbit";
		this.url = "www.google.com";
		this.regex = "(?i)www.google.com";
		this.correlationId = 9;
	}

	@Test
	public void receiveSaveClientHistory() throws Exception {
		PayloadValidationResponse payloadRes = new PayloadValidationResponse();
		payloadRes.setCorrelationId(this.correlationId);
		payloadRes.setMatch(Boolean.TRUE);
		payloadRes.setRegex(this.regex);

		when(this.validationUrl.validationUrlClient(this.client, this.url, this.correlationId)).thenReturn(payloadRes);

		this.validationUrlConsumer.receive(PAYLOAD);

		verify(this.clientHistoryResponseRep).save(any(ClientHistoryResponse.class));
	}

	@Test
	public void receiveValidationUrlClient() throws Exception {
		PayloadValidationResponse payloadRes = new PayloadValidationResponse();
		payloadRes.setCorrelationId(this.correlationId);
		payloadRes.setMatch(Boolean.TRUE);
		payloadRes.setRegex(this.regex);

		when(this.validationUrl.validationUrlClient(this.client, this.url, this.correlationId)).thenReturn(payloadRes);
		when(this.clientHistoryResponseRep.save(any(ClientHistoryResponse.class))).thenReturn(null);

		this.validationUrlConsumer.receive(PAYLOAD);

		verify(this.validationUrl).validationUrlClient(this.client, this.url, this.correlationId);
	}

}
