package org.progress.semaphore.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.progress.semaphore.models.ConceptReviewRequest;
import org.progress.semaphore.services.ConceptReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.smartlogic.cloud.CloudException;
import com.smartlogic.ontologyeditor.OEClientException;



@RestController
@RequestMapping("s4")
public class OntologyEditorController {
	private final ConceptReviewService conceptReviewService;

    @Autowired
    public OntologyEditorController(ConceptReviewService conceptReviewService) {
        this.conceptReviewService = conceptReviewService;
    }
	
    @PostMapping(value = "/concept/review", 
            produces = MediaType.TEXT_PLAIN_VALUE, 
            consumes = MediaType.APPLICATION_JSON_VALUE)

	@ResponseBody
	 public ResponseEntity<String> addConceptsToReview(@RequestBody ConceptReviewRequest request) {
        try {
            return ResponseEntity.ok(conceptReviewService.addConceptsToReview(request));
        } catch (OEClientException | CloudException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

}
