package com.validationurl.validation;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
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
	public Queue queueResponseKey() {
		return new Queue(this.responseKey, true);
	}

	@Bean
	public DirectExchange exchange() {
		return new DirectExchange(this.exchangeResponse, true, false);
	}
	
	@Bean
	public Binding bindExcahngeToQueue() {
	    return BindingBuilder.bind(queueResponseKey()).to(exchange()).with(this.responseKey);
	}

	public void responseUrlClient(PayloadValidationResponse payloadValidationResponse) {
		LOGGER.info(">> responseUrlClient");

		String payloadResposne = this.payloadToResponse(payloadValidationResponse);
		
		if (payloadResposne != null) {
			byte[] payloadResposneAscii = payloadResposne.getBytes(StandardCharsets.US_ASCII);

			String payload = Arrays.toString(payloadResposneAscii).replace("[", "").replace("]", "");

			LOGGER.info("payload response {}", payload);
			
			if (payload != null) {
				this.rabbitTemplate.convertAndSend(this.exchangeResponse, this.responseKey, payload);
			}
		}

		LOGGER.info("<< responseUrlClient");
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
