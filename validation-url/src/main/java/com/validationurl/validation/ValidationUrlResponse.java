package com.validationurl.validation;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.validationurl.payload.PayloadValidationResponse;

@Component
public class ValidationUrlResponse {

	private static final Logger LOGGER = LoggerFactory.getLogger(ValidationUrlResponse.class);

	@Value("${rabbitmq.queue.validation}")
	private String queueValidation;

	@Value("${rabbitmq.queue.response.key}")
	private String responseKey;

	@Value("${rabbitmq.queue.response}")
	private String exchangeResponse;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Bean
	public Queue queueValidation() {
		return new Queue(this.queueValidation, true);
	}

	@Bean
	public TopicExchange exchange() {
		return new TopicExchange(this.exchangeResponse, true, false);
	}

	public void responseUrlClient(PayloadValidationResponse payloadValidationResponse) {
		LOGGER.info(">> responseUrlClient");

		String payloadResposne = this.payloadToResponse(payloadValidationResponse);

		if (payloadResposne != null) {
			this.rabbitTemplate.convertAndSend(this.exchangeResponse, this.responseKey, payloadResposne);

		}

		LOGGER.info("<< validationUrlClient");
	}

	private String payloadToResponse(final PayloadValidationResponse payloadValidationResponse) {
		String jsonPayload = null;
		try {
			final ObjectMapper mapper = new ObjectMapper();
			jsonPayload = mapper.writeValueAsString(payloadValidationResponse);

			return jsonPayload;
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}

		return jsonPayload;
	}

}
