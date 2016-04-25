package com.patrickgaines.hateoas.appeals.model;

import javax.xml.bind.annotation.XmlEnumValue;


public enum AppealStatus {
    @XmlEnumValue(value="unsubmitted")
    UNSUBMITTED,
    @XmlEnumValue(value="reviewing")
    REVIEWING, 
    @XmlEnumValue(value="rechecked")
    RECHECKED, 
    @XmlEnumValue(value="completed")
    COMPLETED
}
