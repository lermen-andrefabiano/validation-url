package com.validationurl.consumer;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.validationurl.BusinessException;
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
public class InsertUrlConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(InsertUrlConsumer.class);

	@Autowired
	private WhiteListGlobalRepository whiteListGlobalRep;

	@Autowired
	private WhiteListRepository whiteListRep;

	@RabbitListener(queues = { "${rabbitmq.queue.insertion}" })
<<<<<<< HEAD
	public void receive(@Payload char[] payloadChars) {
		LOGGER.info(">> InsertUrlConsumer receive");
=======
	public void receive(@Payload String fileBody) {
		LOGGER.info(">> InsertUrlConsumer receive");
		LOGGER.debug("Receive payload {}", fileBody);
>>>>>>> branch 'master' of https://github.com/lermen-andrefabiano/validation-url.git

		try {
			
			String payload = new String(payloadChars);
			
			PayloadInsertRequest request = this.bodyToPayLoad(payload);

			this.validationPayload(request);

			if (request.getClient() != null && !"null".equalsIgnoreCase(request.getClient())
					&& !request.getClient().isEmpty()) {
				LOGGER.info("White list");
				this.whiteListRep.save(new WhiteList(request.getClient(), request.getRegex()));
			} else {
				LOGGER.info("White list global");
				this.whiteListGlobalRep.save(new WhiteListGlobal(request.getRegex()));
			}
		} catch (Exception e) {
			LOGGER.error("Erro ao consumir fila: {}", e.getMessage());
		}

		LOGGER.info("<< InsertUrlConsumer receive");
	}

	private void validationPayload(PayloadInsertRequest request) throws BusinessException {
		if (request == null) {
<<<<<<< HEAD
			throw new BusinessException("Invalid payload");
=======
			throw new BusinessException("Invalid");
>>>>>>> branch 'master' of https://github.com/lermen-andrefabiano/validation-url.git
		}
		if (request.getRegex() == null || request.getRegex().isEmpty()) {
			throw new BusinessException("Regex invalid");
		}
	}

	private PayloadInsertRequest bodyToPayLoad(final String fileBody) {
		final ObjectMapper mapper = new ObjectMapper();
		PayloadInsertRequest payloadDTO = null;

		try {
			payloadDTO = mapper.readValue(fileBody, PayloadInsertRequest.class);
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}

		return payloadDTO;
	}

}
