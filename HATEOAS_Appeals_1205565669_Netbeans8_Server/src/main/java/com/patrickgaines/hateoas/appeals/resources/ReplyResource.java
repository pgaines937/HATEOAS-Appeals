package com.patrickgaines.hateoas.appeals.resources;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import com.patrickgaines.hateoas.appeals.activities.CompleteAppealActivity;
import com.patrickgaines.hateoas.appeals.activities.NoSuchAppealException;
import com.patrickgaines.hateoas.appeals.activities.AppealAlreadyCompletedException;
import com.patrickgaines.hateoas.appeals.activities.AppealNotSubmittedException;
import com.patrickgaines.hateoas.appeals.activities.ReadReplyActivity;
import com.patrickgaines.hateoas.appeals.model.Identifier;
import com.patrickgaines.hateoas.appeals.representations.AppealRepresentation;
import com.patrickgaines.hateoas.appeals.representations.ReplyRepresentation;
import com.patrickgaines.hateoas.appeals.representations.CSE564AppealsUri;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/reply")
public class ReplyResource {
    
    private static final Logger LOG = LoggerFactory.getLogger(ReplyResource.class);

    private @Context
    UriInfo uriInfo;

    public ReplyResource() {
        LOG.info("Reply Resource constructor");
    }

    /**
     * Used in test cases only to allow the injection of a mock UriInfo.
     * 
     * @param uriInfo
     */
    public ReplyResource(UriInfo uriInfo) {
        this.uriInfo = uriInfo;

    }

    @GET
    @Path("/{appealId}")
    @Produces("application/vnd.cse564-appeals+xml")
    public Response getReply() {
        LOG.info("Retrieving a  Reply Resource");
        
        Response response;
        
        try {
            ReplyRepresentation responseRepresentation = new ReadReplyActivity().read(new CSE564AppealsUri(uriInfo.getRequestUri()));
            response = Response.status(Status.OK).entity(responseRepresentation).build();
        } catch (AppealAlreadyCompletedException oce) {
            LOG.info("Appeal already completed");
            response = Response.status(Status.NO_CONTENT).build();
        } catch (AppealNotSubmittedException onpe) {
            LOG.info("Appeal not paid");
            response = Response.status(Status.NOT_FOUND).build();
        } catch (NoSuchAppealException nsoe) {
            LOG.info("No such appeal");
            response = Response.status(Status.NOT_FOUND).build();
        }
        
        LOG.info("The responce for the retrieve reply request is {}", response);
        
        return response;
    }
    
    @DELETE
    @Path("/{appealId}")
    public Response completeAppeal(@PathParam("appealId")String identifier) {
        LOG.info("Retrieving a  Reply Resource");
        
        Response response;
        
        try {
            AppealRepresentation finalizedAppealRepresentation = new CompleteAppealActivity().completeAppeal(new Identifier(identifier));
            response = Response.status(Status.OK).entity(finalizedAppealRepresentation).build();
        } catch (AppealAlreadyCompletedException oce) {
            LOG.info("Appeal already completed");
            response = Response.status(Status.NO_CONTENT).build();
        } catch (NoSuchAppealException nsoe) {
            LOG.info("No such appeal");
            response = Response.status(Status.NOT_FOUND).build();
        } catch (AppealNotSubmittedException onpe) {
            LOG.info("Appeal not paid ");
            response = Response.status(Status.CONFLICT).build();
        }
        
        LOG.info("The response for the delete reply request is {}", response);
        
        return response;
    }
}
