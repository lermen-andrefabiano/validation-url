package com.validationurl.validation;

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
public class ValidationUrl {

	private static final Logger LOGGER = LoggerFactory.getLogger(ValidationUrl.class);

	@Autowired
	private WhiteListRepository whiteListRep;

	@Autowired
	private WhiteListGlobalRepository whiteListGlobalRep;

	public PayloadValidationResponse validationUrlClient(String client, String url, Integer correlationId) {
		LOGGER.info(">> validationUrlClient");
		
		PayloadValidationResponse response = new PayloadValidationResponse(correlationId);

		List<WhiteList> whiteListClient = this.whiteListRep.findByClient(client);
		LOGGER.debug("White list size: {}", whiteListClient.size());

		List<WhiteListGlobal> whiteListClientGlobal = this.whiteListGlobalRep.findAll();
		LOGGER.debug("White list global size: {}", whiteListClientGlobal.size());

		List<String> regexList = this.obterRegexList(whiteListClient, whiteListClientGlobal);
		LOGGER.debug("Regex size: {}", regexList.size());

		this.isValidationRegex(url, response, regexList);

		LOGGER.info("<< validationUrlClient");
		return response;
	}

	private void isValidationRegex(String url, PayloadValidationResponse response, List<String> regexList) {
		LOGGER.info(">> isValidationRegex");

		for (String regex : regexList) {
			if (this.isValidURL(url, regex)) {
				LOGGER.info("Achou regex {}", regex);
				response.setMatch(true);
				response.setRegex(regex);
				break;
			}
		}
		
		LOGGER.info("<< isValidationRegex");
	}
	
	private boolean isValidURL(String url, String regex) {
        return url.matches(regex);
    }

	private List<String> obterRegexList(List<WhiteList> whiteListClient, List<WhiteListGlobal> whiteListClientGlobal) {
		LOGGER.info("Gerando regex do client");

		List<String> regexListClient = whiteListClient.stream().map(white -> {
			return white.getRegex();
		}).collect(Collectors.toList());

		LOGGER.info("Gerando regex global");
		List<String> regexListGlobal = whiteListClientGlobal.stream().map(white -> {
			return white.getRegex();
		}).collect(Collectors.toList());

		return Stream.concat(regexListClient.stream(), regexListGlobal.stream()).collect(Collectors.toList());

	}
}
