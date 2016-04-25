package com.patrickgaines.hateoas.appeals.model;

import org.joda.time.DateTime;

public class Submission {

    private final String subject;
    private DateTime submissionDate;

    public Submission(String subject) {
        this.subject = subject;
        this.submissionDate = new DateTime();

    }

    public String getSubject() {
        return subject;
    }
    
    public boolean hasAppealInSubject() {       
        return subject.contains("Appeal");
    }        
    

    public DateTime getSubmissionDate() {
        return submissionDate;
    }
}
