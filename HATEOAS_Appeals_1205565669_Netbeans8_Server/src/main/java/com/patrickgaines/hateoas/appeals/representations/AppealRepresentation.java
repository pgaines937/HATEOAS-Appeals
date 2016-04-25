package com.patrickgaines.hateoas.appeals.representations;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.patrickgaines.hateoas.appeals.activities.InvalidAppealException;
import com.patrickgaines.hateoas.appeals.activities.UriExchange;
import com.patrickgaines.hateoas.appeals.model.Item;
import com.patrickgaines.hateoas.appeals.model.Appeal;
import com.patrickgaines.hateoas.appeals.model.AppealStatus;
import static com.patrickgaines.hateoas.appeals.representations.Representation.RELATIONS_URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement(name = "appeal", namespace = Representation.CSE564_APPEALS_NAMESPACE)
public class AppealRepresentation extends Representation {
    
    private static final Logger LOG = LoggerFactory.getLogger(AppealRepresentation.class);

    @XmlElement(name = "item", namespace = Representation.CSE564_APPEALS_NAMESPACE)
    private List<Item> items;
    @XmlElement(name = "points", namespace = Representation.CSE564_APPEALS_NAMESPACE)
    private double points;
    @XmlElement(name = "status", namespace = Representation.CSE564_APPEALS_NAMESPACE)
    private AppealStatus status;

    /**
     * For JAXB :-(
     */
    AppealRepresentation() {
        LOG.info("Executing AppealRepresentation constructor");
    }

    public static AppealRepresentation fromXmlString(String xmlRepresentation) {
        LOG.info("Creating an Appeal object from the XML = {}", xmlRepresentation);
                
        AppealRepresentation appealRepresentation = null;     
        try {
            JAXBContext context = JAXBContext.newInstance(AppealRepresentation.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            appealRepresentation = (AppealRepresentation) unmarshaller.unmarshal(new ByteArrayInputStream(xmlRepresentation.getBytes()));
        } catch (Exception e) {
            throw new InvalidAppealException(e);
        }
        
        LOG.info("Generated the object {}", appealRepresentation);
        return appealRepresentation;
    }
    
    public static AppealRepresentation createResponseAppealRepresentation(Appeal appeal, CSE564AppealsUri appealUri) {
        LOG.info("Creating a Response Appeal for appeal = {} and appeal URI", appeal.toString(), appealUri.toString());
        
        AppealRepresentation appealRepresentation = null; 
        
        CSE564AppealsUri submissionUri = new CSE564AppealsUri(appealUri.getBaseUri() + "/submission/" + appealUri.getId().toString());
        LOG.info("Submission URI = {}", submissionUri);
        
        if(appeal.getStatus() == AppealStatus.UNSUBMITTED) {
            LOG.info("The appeal status is {}", AppealStatus.UNSUBMITTED);
            appealRepresentation = new AppealRepresentation(appeal, 
                    new Link(RELATIONS_URI + "cancel", appealUri), 
                    new Link(RELATIONS_URI + "submission", submissionUri), 
                    new Link(RELATIONS_URI + "update", appealUri),
                    new Link(Representation.SELF_REL_VALUE, appealUri));
        } else if(appeal.getStatus() == AppealStatus.REVIEWING) {
            LOG.info("The appeal status is {}", AppealStatus.REVIEWING);
            appealRepresentation = new AppealRepresentation(appeal, new Link(Representation.SELF_REL_VALUE, appealUri));
        } else if(appeal.getStatus() == AppealStatus.RECHECKED) {
            LOG.info("The appeal status is {}", AppealStatus.RECHECKED);
            appealRepresentation = new AppealRepresentation(appeal, new Link(Representation.RELATIONS_URI + "reciept", UriExchange.replyForSubmission(submissionUri)));
        } else if(appeal.getStatus() == AppealStatus.COMPLETED) {
            LOG.info("The appeal status is {}", AppealStatus.COMPLETED);
            appealRepresentation = new AppealRepresentation(appeal);            
        } else {
            LOG.info("The appeal status is in an unknown status");
            throw new RuntimeException("Unknown Appeal Status");
        }
        
        LOG.info("The appeal representation created for the Create Response Appeal Representation is {}", appealRepresentation);
        
        return appealRepresentation;
    }

    public AppealRepresentation(Appeal appeal, Link... links) {
        LOG.info("Creating an Appeal Representation for appeal = {} and links = {}", appeal.toString(), links.toString());
        
        try {
            this.items = appeal.getItems();
            this.points = appeal.calculatePoints();
            this.status = appeal.getStatus();
            this.links = java.util.Arrays.asList(links);
        } catch (Exception ex) {
            throw new InvalidAppealException(ex);
        }
        
        LOG.info("Created the AppealRepresentation {}", this);
    }

    public String toString() {
        try {
            JAXBContext context = JAXBContext.newInstance(AppealRepresentation.class);
            Marshaller marshaller = context.createMarshaller();

            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(this, stringWriter);

            return stringWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Appeal getAppeal() {
        if (items == null) {
            throw new InvalidAppealException();
        }
        for (Item i : items) {
            if (i == null) {
                throw new InvalidAppealException();
            }
        }

        return new Appeal(status, items);
    }

    public Link getCancelLink() {
        return getLinkByName(RELATIONS_URI + "cancel");
    }

    public Link getSubmissionLink() {
        return getLinkByName(RELATIONS_URI + "submission");
    }

    public Link getUpdateLink() {
        return getLinkByName(RELATIONS_URI + "update");
    }

    public Link getSelfLink() {
        return getLinkByName("self");
    }
    
    public AppealStatus getStatus() {
        return status;
    }

    public double getPoints() {
        return points;
    }
}
