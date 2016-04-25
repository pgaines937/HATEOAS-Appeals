package com.patrickgaines.hateoas.appeals.model;

import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Appeal {
    
    private static final Logger LOG = LoggerFactory.getLogger(Appeal.class);
    
    private final List<Item> items;
    @XmlTransient
    private AppealStatus status = AppealStatus.UNSUBMITTED;

    public Appeal(List<Item> items) {
        this(AppealStatus.UNSUBMITTED, items);
        LOG.info("Executing Appeal constructor: items = {}", items);
    }
    

    public Appeal(AppealStatus status, List<Item> items) {
        this.items = items;
        this.status = status;
        LOG.info("Executing Appeal constructor: status = {} and items = {}", status, items);
        LOG.info("appeal = {}", this);
    }
    
    public List<Item> getItems() {
        LOG.info("Executing Appeal.getItems");
        LOG.info("items = {}", items);
        return items;
    }

    public double calculatePoints() {
        LOG.info("Executing Appeal.calculatePoints");
        double total = 0.0;
        if (items != null) {
            for (Item item : items) {
                total += item.getPoints();
            }
        }
        return total;
    }

    public void setStatus(AppealStatus status) {
        LOG.info("Executing Appeal.setStatus");
        this.status = status;
    }

    public AppealStatus getStatus() {
        LOG.info("Executing Appeal.getStatus");
        return status;
    }
    
    @Override
    public String toString() {
        LOG.info("Executing Appeal.toString");
        StringBuilder sb = new StringBuilder();
        sb.append("Status: " + status + "\n");
        for(Item i : items) {
            sb.append("Item: " + i.toString()+ "\n");
        }
        return sb.toString();
    }
}