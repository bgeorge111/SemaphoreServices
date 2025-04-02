package org.progress.semaphore.services;

import org.progress.semaphore.models.ConceptReviewRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.smartlogic.cloud.CloudException;
import com.smartlogic.cloud.TokenFetcher;
import com.smartlogic.ontologyeditor.OEClientException;
import com.smartlogic.ontologyeditor.OEClientReadWrite;
import com.smartlogic.ontologyeditor.beans.Concept;
import com.smartlogic.ontologyeditor.beans.ConceptScheme;
import com.smartlogic.ontologyeditor.beans.Label;
import com.smartlogic.ontologyeditor.beans.MetadataValue;

import jakarta.json.Json;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class ConceptReviewService {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Value("${semaphore.proxy.address:#{null}}")
	private String proxyAddress;

	@Value("${semaphore.base.url:#{null}}")
	private String baseUrl;

	@Value("${semaphore.model.uri:#{null}}")
	private String modelUri;

	@Value("${semaphore.token:#{null}}")
	private String semaphoreToken;

	@Value("${semaphore.header.token:#{null}}")
	private String headerToken;

	@Value("${semaphore.token.url:#{null}}")
	private String tokenUrl;

	@Value("${semaphore.token.key:#{null}}")
	private String tokenKey;

	public String addConceptsToReview(ConceptReviewRequest request) 
				throws OEClientException, CloudException {
		
		OEClientReadWrite oeClient = new OEClientReadWrite();
		oeClient.setKRTClient(true);
		if (proxyAddress != null) oeClient.setProxyAddress(proxyAddress);
		if (baseUrl != null) oeClient.setBaseURL(baseUrl);
		if (modelUri != null) oeClient.setModelUri(modelUri);
		if (headerToken != null) oeClient.setHeaderToken(headerToken);
		if (semaphoreToken != null) oeClient.setToken(semaphoreToken);
		if (tokenUrl != null && tokenKey != null) {
			TokenFetcher tokenFetcher = new TokenFetcher(tokenUrl, tokenKey);
			oeClient.setCloudToken(tokenFetcher.getAccessToken());
		}

		ConceptScheme conceptScheme = oeClient.getConceptSchemeByName("Concept Review - Newly Added", "en");
		int successCount = 0;
		int failureCount = 0;
		List<String> failedConcepts = new ArrayList<>();

		for (String conceptName : request.getConcepts()) {
			try {
				List<Label> labels = Collections.singletonList(new Label("en", conceptName));

				Concept concept = new Concept(oeClient,
						"http://example.com/APITest#Concept1" + urlEncode(conceptName.replaceAll(" ", "")), labels);
				oeClient.createConcept(conceptScheme.getUri(), concept,
											Map.of("skos:note", Set.of(new MetadataValue("", request.getContributor()))));
				successCount++;
			} catch (OEClientException e) {
				logger.info("Failed to add concept " + conceptName);
				logger.debug("Failed to add concept " + e.getMessage());
				failureCount++;
				failedConcepts.add(conceptName);
			}
		}

		return String.format("Processed %d concepts: %d succeeded, %d failed. Failed Concepts: %s",
				request.getConcepts().size(), successCount, failureCount, failedConcepts);
	}

	private String urlEncode(String value) {
		try {
			return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Encoding error", e);
		}
	}
}
