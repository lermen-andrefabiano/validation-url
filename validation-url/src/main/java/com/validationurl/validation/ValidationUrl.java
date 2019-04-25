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

	private static final Logger LOGEGR = LoggerFactory.getLogger(ValidationUrl.class);

	@Autowired
	private WhiteListRepository whiteListRep;

	@Autowired
	private WhiteListGlobalRepository whiteListGlobalRep;

	public PayloadValidationResponse validationUrlClient(String client, String url, Integer correlationId) {
		LOGEGR.info(">> validationUrlClient");
		PayloadValidationResponse response = new PayloadValidationResponse(correlationId);

		List<WhiteList> whiteListClient = this.whiteListRep.findByClient(client);
		LOGEGR.debug("White list size: {}", whiteListClient.size());

		List<WhiteListGlobal> whiteListClientGlobal = this.whiteListGlobalRep.findAll();
		LOGEGR.debug("White list global size: {}", whiteListClientGlobal.size());

		List<String> regexList = this.obterRegexList(whiteListClient, whiteListClientGlobal);
		LOGEGR.debug("Regex size: {}", regexList.size());

		this.validtionRegex(url, response, regexList);

		LOGEGR.info("<< validationUrlClient");
		return response;
	}

	private void validtionRegex(String url, PayloadValidationResponse response, List<String> regexList) {
		LOGEGR.info(">> validtionRegex");
		boolean urlOk = false;

		for (String regex : regexList) {
			try {
				urlOk = url.matches(regex);
			} catch (Exception e) {
				LOGEGR.error("Erro ao criar regex!");
			}

			if (urlOk) {
				response.setMatch(urlOk);
				response.setRegex(regex);
				break;
			}
		}
		LOGEGR.info("<< validtionRegex");
	}

	private List<String> obterRegexList(List<WhiteList> whiteListClient, List<WhiteListGlobal> whiteListClientGlobal) {
		LOGEGR.info("Gerando regex do client");

		List<String> regexListClient = whiteListClient.stream().map(white -> {
			return white.getRegex();
		}).collect(Collectors.toList());

		LOGEGR.info("Gerando regex global");
		List<String> regexListGlobal = whiteListClientGlobal.stream().map(white -> {
			return white.getRegex();
		}).collect(Collectors.toList());

		return Stream.concat(regexListClient.stream(), regexListGlobal.stream()).collect(Collectors.toList());

	}
}
