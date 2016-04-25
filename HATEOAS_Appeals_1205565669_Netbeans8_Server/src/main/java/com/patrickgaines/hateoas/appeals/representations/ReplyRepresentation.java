package com.patrickgaines.hateoas.appeals.representations;

import com.patrickgaines.hateoas.appeals.model.Appeal;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.joda.time.DateTime;

import com.patrickgaines.hateoas.appeals.model.Submission;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement(name = "reply", namespace = Representation.CSE564_APPEALS_NAMESPACE)
public class ReplyRepresentation extends Representation {
    
    private static final Logger LOG = LoggerFactory.getLogger(ReplyRepresentation.class);

    @XmlElement(name = "points", namespace = Representation.CSE564_APPEALS_NAMESPACE)
    private double pointsReturned;
    @XmlElement(name = "submissionDate", namespace = Representation.CSE564_APPEALS_NAMESPACE)
    private String submissionDate;
    
    ReplyRepresentation(){
        LOG.info("In ReplyRepresentation Constrictor");
    } // For JAXB :-(
    
    public ReplyRepresentation(Appeal appeal, Submission submission, Link appealLink) {
        LOG.info("Creating an Reply Representation with the submission = {} and links = {}", submission, links);
        
        this.pointsReturned = appeal.calculatePoints();
        this.submissionDate = submission.getSubmissionDate().toString();
        this.links = new ArrayList<Link>();
        links.add(appealLink);
        
        LOG.info("Created the Reply Representation {}", this);
    }

    public DateTime getPaidDate() {
        return new DateTime(submissionDate);
    }
    
    public double getAmountPaid() {
        return pointsReturned;
    }

    public Link getAppealLink() {
        return getLinkByName(Representation.RELATIONS_URI + "appeal");
    }
    
    public String toString() {
        try {
            JAXBContext context = JAXBContext.newInstance(ReplyRepresentation.class);
            Marshaller marshaller = context.createMarshaller();

            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(this, stringWriter);

            return stringWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
