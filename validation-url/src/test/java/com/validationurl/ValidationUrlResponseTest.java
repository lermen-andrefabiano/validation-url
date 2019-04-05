package com.validationurl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.validationurl.payload.PayloadValidationResponse;
import com.validationurl.validation.ValidationUrlResponse;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ValidationUrlResponse.class })
public class ValidationUrlResponseTest {

	@Autowired
	@InjectMocks
	private ValidationUrlResponse validationUrlResponse;

	@MockBean
	private RabbitTemplate rabbitTemplate;

	private String regex;

	private Integer correlationId;

	@Before
	public void init() {
		this.regex = "(?i)www.google.com";
		this.correlationId = 9;
	}

	@Test
	public void responseUrlClient() throws Exception {
		PayloadValidationResponse payloadRes = this.obterValidationResponse();
		this.validationUrlResponse.responseUrlClient(payloadRes);

		verify(this.rabbitTemplate).convertAndSend(any(String.class), any(String.class), any(String.class));

	}

	private PayloadValidationResponse obterValidationResponse() {
		PayloadValidationResponse payloadRes = new PayloadValidationResponse();
		payloadRes.setCorrelationId(this.correlationId);
		payloadRes.setMatch(Boolean.TRUE);
		payloadRes.setRegex(this.regex);

		return payloadRes;
	}

}
