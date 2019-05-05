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
import com.validationurl.model.WhiteList;
import com.validationurl.model.WhiteListGlobal;
import com.validationurl.payload.PayloadInsertRequest;
import com.validationurl.repository.WhiteListGlobalRepository;
import com.validationurl.repository.WhiteListRepository;

/**
 * Polls ${INSERTION_QUEUE} for requests, parses them and inserts a
 * WhitelistEntry in the database if the request is valid.
 *
 * Insertion requests have the following format: {"client": <string/nullable>,
 * "regex": <string>}
 */

@Component
public class InsertUrlConsumer implements Serializable {

	private static final long serialVersionUID = 992889932136919981L;

	private static final Logger logger = LoggerFactory.getLogger(InsertUrlConsumer.class);

	@Autowired
	private WhiteListGlobalRepository whiteListGlobalRep;

	@Autowired
	private WhiteListRepository whiteListRep;

	@RabbitListener(queues = { "${rabbitmq.queue.insertion}" })
	public void receive(@Payload char[] payloadChars) {
		logger.info(">> InsertUrlConsumer receive");
		logger.debug(String.format("Receive payload %s", payloadChars));

		try {
			
			String payload = new String(payloadChars);
			
			PayloadInsertRequest request = this.bodyToPayLoad(payload);

			this.validationPayload(request);

			if (request.getClient() != null && !"null".equalsIgnoreCase(request.getClient())
					&& !request.getClient().isEmpty()) {
				logger.info("White list");
				this.whiteListRep.save(new WhiteList(request.getClient(), request.getRegex()));
			} else {
				logger.info("White list global");
				this.whiteListGlobalRep.save(new WhiteListGlobal(request.getRegex()));
			}
		} catch (Exception e) {
			logger.error("Erro ao consumir fila: " + e.getMessage());
		}

		logger.info("<< InsertUrlConsumer receive");
	}

	private void validationPayload(PayloadInsertRequest request) throws Exception {
		if (request == null) {
			throw new Exception("Invalid");
		}
		if (request.getRegex() == null || request.getRegex().isEmpty()) {
			throw new Exception("Regex invalid");
		}
	}

	private PayloadInsertRequest bodyToPayLoad(final String fileBody) {
		final ObjectMapper mapper = new ObjectMapper();
		PayloadInsertRequest payloadDTO = null;

		try {
			payloadDTO = mapper.readValue(fileBody, PayloadInsertRequest.class);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

		return payloadDTO;
	}

}
