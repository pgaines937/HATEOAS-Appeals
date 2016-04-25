package com.patrickgaines.hateoas.appeals.representations;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.patrickgaines.hateoas.appeals.model.Submission;
import org.joda.time.DateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement(name = "submission", namespace = Representation.CSE564_APPEALS_NAMESPACE)
public class SubmissionRepresentation extends Representation {
    
    private static final Logger LOG = LoggerFactory.getLogger(SubmissionRepresentation.class);
       
    @XmlElement(namespace = SubmissionRepresentation.CSE564_APPEALS_NAMESPACE) private String subject;  
    @XmlElement(namespace = SubmissionRepresentation.CSE564_APPEALS_NAMESPACE) private DateTime submissionDate;      
    /**
     * For JAXB :-(
     */
     SubmissionRepresentation(){
        LOG.info("In SubmissionRepresentation Constructor");
     }
    
    public SubmissionRepresentation(Submission submission, Link...links) {
        LOG.info("Creating a Submission Representation with the submission = {} and links = {}", submission, links);
        
        subject = submission.getSubject();
        submissionDate = submission.getSubmissionDate();
        this.links = java.util.Arrays.asList(links);
        
        LOG.info("Created the Submission Representation {}", this);
    }

    public Submission getSubmission() {
        return new Submission(subject);
    }
    
    public Link getReplyLink() {
        return getLinkByName(Representation.RELATIONS_URI + "reply");
    }
    
    public Link getAppealLink() {
        return getLinkByName(Representation.RELATIONS_URI + "appeal");
    }
}
