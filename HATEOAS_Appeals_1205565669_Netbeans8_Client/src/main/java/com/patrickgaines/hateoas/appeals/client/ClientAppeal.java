package com.patrickgaines.hateoas.appeals.client;

import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.patrickgaines.hateoas.appeals.model.Item;
import com.patrickgaines.hateoas.appeals.model.Appeal;
import com.patrickgaines.hateoas.appeals.model.AppealStatus;
import com.patrickgaines.hateoas.appeals.representations.Representation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement(name = "appeal", namespace = Representation.CSE564_APPEALS_NAMESPACE)
public class ClientAppeal {
    
    private static final Logger LOG = LoggerFactory.getLogger(ClientAppeal.class);
    
    @XmlElement(name = "item", namespace = Representation.CSE564_APPEALS_NAMESPACE)
    private List<Item> items;
    @XmlElement(name = "status", namespace = Representation.CSE564_APPEALS_NAMESPACE)
    private AppealStatus status;
    
    private ClientAppeal(){}
    
    public ClientAppeal(Appeal appeal) {
        LOG.info("Executing ClientAppeal constructor");
        this.items = appeal.getItems();
    }
    
    public Appeal getAppeal() {
        LOG.info("Executing ClientAppeal.getAppeal");
        return new Appeal(status, items);
    }
    
    public List<Item> getItems() {
        LOG.info("Executing ClientAppeal.getItems");
        return items;
    }

    @Override
    public String toString() {
        LOG.info("Executing ClientAppeal.toString");
        try {
            JAXBContext context = JAXBContext.newInstance(ClientAppeal.class);
            Marshaller marshaller = context.createMarshaller();

            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(this, stringWriter);

            return stringWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public AppealStatus getStatus() {
        LOG.info("Executing ClientAppeal.getStatus");
        return status;
    }

    public double getPoints() {
        LOG.info("Executing Appeal.calculatePoints");
        double total = 0.0;
        if (items != null) {
            for (Item item : items) {
                total += item.getPoints();
            }
        }
        return total;
    }
}