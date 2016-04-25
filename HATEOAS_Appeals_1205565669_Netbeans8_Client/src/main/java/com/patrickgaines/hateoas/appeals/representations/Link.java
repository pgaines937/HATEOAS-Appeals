package com.patrickgaines.hateoas.appeals.representations;

import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement(namespace = Representation.DAP_NAMESPACE)
public class Link {
    
    private static final Logger LOG = LoggerFactory.getLogger(Link.class);
    
    @XmlAttribute(name = "rel")
    private String rel;
    @XmlAttribute(name = "uri")
    private String uri;

    @XmlAttribute(name = "mediaType")
    private String mediaType;

    /**
     * For JAXB :-(
     */
    Link() {
        LOG.info("Link Constructor");
    }

    public Link(String name, CSE564AppealsUri uri, String mediaType) {
        LOG.info("Creating a Link object");
        LOG.info("name = {}", name);
        LOG.info("uri = {}", uri);
        LOG.info("mediaType = {}", mediaType);
        
        this.rel = name;
        this.uri = uri.getFullUri().toString();
        this.mediaType = mediaType;

        LOG.info("Created Link Object {}", this);
    }

    public Link(String name, CSE564AppealsUri uri) {
        this(name, uri, Representation.CSE564_APPEALS_MEDIA_TYPE);
    }

    public String getRelValue() {
        return rel;
    }

    public URI getUri() {
        try {
            return new URI(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public String getMediaType() {
        return mediaType;
    }
}
