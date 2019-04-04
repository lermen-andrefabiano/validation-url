package com.validationurl.validation;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.validationurl.model.WhiteList;
import com.validationurl.model.WhiteListGlobal;
import com.validationurl.payload.PayloadValidationResponse;
import com.validationurl.repository.WhiteListGlobalRepository;
import com.validationurl.repository.WhiteListRepository;

@Component
public class ValidationUrl implements Serializable {

	private static final long serialVersionUID = -2402764778501993873L;

	private static final Logger logger = LoggerFactory.getLogger(ValidationUrl.class);

	@Autowired
	private WhiteListRepository whiteListRep;

	@Autowired
	private WhiteListGlobalRepository whiteListGlobalRep;

	public PayloadValidationResponse validationUrlClient(String client, String url, Integer correlationId)
			throws Exception {
		logger.info(">> validationUrlClient");
		PayloadValidationResponse response = new PayloadValidationResponse(correlationId);

		List<WhiteList> whiteListClient = this.whiteListRep.findByClient(client);
		logger.debug(String.format("White list size: %s", whiteListClient.size()));

		List<WhiteListGlobal> whiteListClientGlobal = this.whiteListGlobalRep.findAll();
		logger.debug(String.format("White list global size: %s", whiteListClientGlobal.size()));

		List<String> regexList = this.obterRegexList(whiteListClient, whiteListClientGlobal);
		logger.debug(String.format("Regex size: %s", regexList.size()));

		this.validtionRegex(url, response, regexList);

		logger.info("<< validationUrlClient");
		return response;
	}

	private void validtionRegex(String url, PayloadValidationResponse response, List<String> regexList) {
		logger.info(">> validtionRegex");
		boolean urlOk = false;

		for (String regex : regexList) {
			try {
				urlOk = url.matches(regex);
			} catch (Exception e) {
				logger.error("Erro ao criar regex!");
			}

			if (urlOk) {
				response.setMatch(urlOk);
				response.setRegex(regex);
				break;
			}
		}
		logger.info("<< validtionRegex");
	}

	private List<String> obterRegexList(List<WhiteList> whiteListClient, List<WhiteListGlobal> whiteListClientGlobal)
			throws Exception {
		logger.info("Gerando regex do client");

		List<String> regexListClient = whiteListClient.stream().map(white -> {
			return white.getRegex();
		}).collect(Collectors.toList());

		logger.info("Gerando regex global");
		List<String> regexListGlobal = whiteListClientGlobal.stream().map(white -> {
			return white.getRegex();
		}).collect(Collectors.toList());

		return Stream.concat(regexListClient.stream(), regexListGlobal.stream()).collect(Collectors.toList());

	}
}
