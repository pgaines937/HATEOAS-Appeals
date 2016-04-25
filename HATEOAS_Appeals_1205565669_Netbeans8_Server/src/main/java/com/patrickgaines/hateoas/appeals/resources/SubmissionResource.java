package com.patrickgaines.hateoas.appeals.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import com.patrickgaines.hateoas.appeals.activities.InvalidSubmissionException;
import com.patrickgaines.hateoas.appeals.activities.NoSuchAppealException;
import com.patrickgaines.hateoas.appeals.activities.SubmissionActivity;
import com.patrickgaines.hateoas.appeals.activities.UpdateException;
import com.patrickgaines.hateoas.appeals.model.Identifier;
import com.patrickgaines.hateoas.appeals.representations.Link;
import com.patrickgaines.hateoas.appeals.representations.SubmissionRepresentation;
import com.patrickgaines.hateoas.appeals.representations.Representation;
import com.patrickgaines.hateoas.appeals.representations.CSE564AppealsUri;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/submission/{submissionId}")
public class SubmissionResource {
    
    private static final Logger LOG = LoggerFactory.getLogger(SubmissionResource.class);
    
    private @Context UriInfo uriInfo;
    
    public SubmissionResource(){
        LOG.info("Submission Resource Constructor");
    }
    
    /**
     * Used in test cases only to allow the injection of a mock UriInfo.
     * @param uriInfo
     */
    public SubmissionResource(UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    @PUT
    @Consumes("application/vnd.cse564-appeals+xml")
    @Produces("application/vnd.cse564-appeals+xml")
    public Response submit(SubmissionRepresentation submissionRepresentation) {
        LOG.info("Making a new submission");
        
        Response response;
        
        try {
            response = Response.created(uriInfo.getRequestUri()).entity(new SubmissionActivity().submit(submissionRepresentation.getSubmission(), 
                            new CSE564AppealsUri(uriInfo.getRequestUri()))).build();
        } catch(NoSuchAppealException nsoe) {
            LOG.info("No appeal for submission {}", submissionRepresentation);
            response = Response.status(Status.NOT_FOUND).build();
        } catch(UpdateException ue) {
            LOG.info("Invalid update to submission {}", submissionRepresentation);
            Identifier identifier = new CSE564AppealsUri(uriInfo.getRequestUri()).getId();
            Link link = new Link(Representation.SELF_REL_VALUE, new CSE564AppealsUri(uriInfo.getBaseUri().toString() + "appeal/" + identifier));
            response = Response.status(Status.FORBIDDEN).entity(link).build();
        } catch(InvalidSubmissionException ipe) {
            LOG.info("Invalid Submission for Appeal");
            response = Response.status(Status.BAD_REQUEST).build();
        } catch(Exception e) {
            LOG.info("Someting when wrong with processing submission");
            response = Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        
        LOG.info("Created the new Submission activity {}", response);
        
        return response;
    }
}
