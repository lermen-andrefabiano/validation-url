package com.validationurl.validation;

import java.io.IOException;
import java.io.Serializable;
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
public class ValidationUrlResponse implements Serializable {

	private static final long serialVersionUID = -4914372162904742433L;

	private static final Logger logger = LoggerFactory.getLogger(ValidationUrlResponse.class);

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

	public void responseUrlClient(PayloadValidationResponse payloadValidationResponse) throws Exception {
		logger.info(">> responseUrlClient");

		String payloadResposne = this.payloadToResponse(payloadValidationResponse);
		
		byte[] payloadResposneAscii = payloadResposne.getBytes(StandardCharsets.US_ASCII);
		
		String payload = Arrays.toString(payloadResposneAscii).replace("[", "").replace("]", "");

		this.rabbitTemplate.convertAndSend(this.exchangeResponse, this.responseKey, payload);

		logger.info("<< validationUrlClient");
	}

	private String payloadToResponse(final PayloadValidationResponse payloadValidationResponse) {
		String jsonPayload = null;
		try {
			final ObjectMapper mapper = new ObjectMapper();
			jsonPayload = mapper.writeValueAsString(payloadValidationResponse);

			return jsonPayload;
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

		return jsonPayload;
	}

}
