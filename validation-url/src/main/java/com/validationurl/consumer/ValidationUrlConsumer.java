package com.validationurl.consumer;

import java.io.IOException;
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.validationurl.model.ClientHistoryResponse;
import com.validationurl.payload.PayloadValidationRequest;
import com.validationurl.payload.PayloadValidationResponse;
import com.validationurl.repository.ClientHistoryResponseRepository;
import com.validationurl.validation.ValidationUrl;
import com.validationurl.validation.ValidationUrlResponse;

/**
 * Polls ${VALIDATION_QUEUE} for requests, parses them and queries the database
 * to check if a URL is valid. The response is sent to exchange
 * ${RESPONSE_EXCHANGE} with routing key ${RESPONSE_ROUTING_KEY}
 *
 * Validation requests have the following format: {"client": <string>, "url":
 * <string>, "correlationId": <integer>} The response has the following format:
 * {"match": <boolean>, "regex": <string/nullable>, "correlationId": <integer>}
 * 
 */
@Component
public class ValidationUrlConsumer implements Serializable {

	private static final long serialVersionUID = 913787565926806350L;

	private static final Logger logger = LoggerFactory.getLogger(ValidationUrlConsumer.class);

	@Autowired
	private ValidationUrl validationUrl;

	@Autowired
	private ValidationUrlResponse responseValidationUrl;

	@Autowired
	private ClientHistoryResponseRepository clientHistoryResponseRep;

	@RabbitListener(queues = { "${rabbitmq.queue.validation}" })
	public void receive(@Payload char[] payloadChars) {
		logger.info(">> ValidationUrlConsumer receive");
		logger.debug(String.format("Receive payload %s", payloadChars));

		try {

			String payload = new String(payloadChars);
			
			PayloadValidationRequest request = this.bodyToPayLoad(payload);

			this.validationPayload(request);

			logger.debug(String.format("client: %s url: %s correlationId: %s", request.getClient(), request.getUrl(),
					request.getCorrelationId()));

			PayloadValidationResponse response = this.validationUrl.validationUrlClient(request.getClient(),
					request.getUrl(), request.getCorrelationId());

			this.responseValidationUrl.responseUrlClient(response);

			this.clientHistoryResponseRep.save(new ClientHistoryResponse(response.getRegex(), request.getClient(),
					response.isMatch(), request.getUrl(), response.getCorrelationId()));
		} catch (Exception e) {
			logger.error("Erro ao consumir fila: " + e.getMessage());
		}

		logger.info("<< ValidationUrlConsumer receive");
	}

	private void validationPayload(PayloadValidationRequest request) throws Exception {
		if (request.getUrl() == null || request.getUrl().isEmpty()) {
			throw new Exception("Url invalid");
		}

		if (request.getCorrelationId() == null) {
			throw new Exception("Correlation invalid");
		}

		if (request.getClient() == null || request.getClient().isEmpty()) {
			throw new Exception("Client invalid");
		}
	}

	private PayloadValidationRequest bodyToPayLoad(final String fileBody) {
		final ObjectMapper mapper = new ObjectMapper();
		PayloadValidationRequest payloadDTO = null;

		try {
			payloadDTO = mapper.readValue(fileBody, PayloadValidationRequest.class);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

		return payloadDTO;
	}

}
