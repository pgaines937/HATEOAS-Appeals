package com.patrickgaines.hateoas.appeals.model;

public class SubmissionBuilder {
    
    private String subject = "Appeal";
    
    public static SubmissionBuilder submission() {
        return new SubmissionBuilder();
    }
    
    public SubmissionBuilder withSubject(String name) {
        this.subject = subject;
        return this;
    }

    public Submission build() {
        return new Submission(subject);
    }
}
