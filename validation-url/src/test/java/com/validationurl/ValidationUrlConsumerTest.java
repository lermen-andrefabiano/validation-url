package com.validationurl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

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

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = { ValidationUrlConsumer.class })
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

	private static final char[] PAYLOAD = new char[] { 123, 34, 99, 108, 105, 101, 110, 116, 34, 58, 32, 34, 99, 108, 105, 101, 110, 116, 32, 114, 97, 98, 98, 105, 116, 34, 44, 32, 34, 117, 114, 108, 34, 58, 32, 34, 119, 119, 119, 46, 103, 111, 111, 103, 108, 101, 46, 99, 111, 109, 34, 44, 32, 34, 99, 111, 114, 114, 101, 108, 97, 116, 105, 111, 110, 73, 100, 34, 58, 32, 57, 125 };

	@Before
	public void init() {
		this.client = "client rabbit";
		this.url = "www.google.com";
		this.regex = "(?i)www.google.com";
		this.correlationId = 9;
	}

	//@Test
	public void receiveSaveClientHistory() throws Exception {
		PayloadValidationResponse payloadRes = new PayloadValidationResponse();
		payloadRes.setCorrelationId(this.correlationId);
		payloadRes.setMatch(Boolean.TRUE);
		payloadRes.setRegex(this.regex);

		when(this.validationUrl.validationUrlClient(this.client, this.url, this.correlationId)).thenReturn(payloadRes);

		this.validationUrlConsumer.receive(PAYLOAD);

		verify(this.clientHistoryResponseRep).save(any(ClientHistoryResponse.class));
	}

	//@Test
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
