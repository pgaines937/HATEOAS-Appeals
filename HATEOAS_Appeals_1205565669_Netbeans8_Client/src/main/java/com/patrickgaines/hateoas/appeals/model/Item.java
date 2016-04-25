package com.patrickgaines.hateoas.appeals.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.patrickgaines.hateoas.appeals.representations.Representation;

@XmlRootElement
public class Item {
    @XmlElement(namespace = Representation.CSE564_APPEALS_NAMESPACE)
    private int points;
    @XmlElement(namespace = Representation.CSE564_APPEALS_NAMESPACE)
    private String reason;
    
    /**
     * For JAXB :-(
     */
    Item(){}
    
    public Item(String reason, int points) {
        this.points = points;
        this.reason = reason;     
    }
    
    public int getPoints() {
        return points;
    }

    public String getString() {
        return reason;
    }
    
    public String toString() {
        return reason + " " + points;
    }
}