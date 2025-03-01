package org.progress.semaphore.models;

import java.util.List;

public class ConceptReviewRequest {
    private List<String> concepts;
    private String contributor;

    // Getters and Setters
    public List<String> getConcepts() {
        return concepts;
    }

    public void setConcepts(List<String> concepts) {
        this.concepts = concepts;
    }

    public String getContributor() {
        return contributor;
    }

    public void setContributor(String contributor) {
        this.contributor = contributor;
    }
}
