package com.patrickgaines.hateoas.appeals.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import com.patrickgaines.hateoas.appeals.activities.RemoveAppealActivity;
import com.patrickgaines.hateoas.appeals.activities.CreateAppealActivity;
import com.patrickgaines.hateoas.appeals.activities.InvalidAppealException;
import com.patrickgaines.hateoas.appeals.activities.NoSuchAppealException;
import com.patrickgaines.hateoas.appeals.activities.AppealDeletionException;
import com.patrickgaines.hateoas.appeals.activities.ReadAppealActivity;
import com.patrickgaines.hateoas.appeals.activities.UpdateException;
import com.patrickgaines.hateoas.appeals.activities.UpdateAppealActivity;
import com.patrickgaines.hateoas.appeals.representations.AppealRepresentation;
import com.patrickgaines.hateoas.appeals.representations.CSE564AppealsUri;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Path("/appeal")
public class AppealResource {
    
    private static final Logger LOG = LoggerFactory.getLogger(AppealResource.class);

    private @Context UriInfo uriInfo;

    public AppealResource() {
        LOG.info("AppealResource constructor");
    }

    /**
     * Used in test cases only to allow the injection of a mock UriInfo.
     * 
     * @param uriInfo
     */
    public AppealResource(UriInfo uriInfo) {
        LOG.info("AppealResource constructor with mock uriInfo {}", uriInfo);
        this.uriInfo = uriInfo;  
    }
    
    @GET
    @Path("/{appealId}")
    @Produces("application/vnd.cse564-appeals+xml")
    public Response getAppeal() {
        LOG.info("Retrieving an Appeal Resource");
        
        Response response;
        
        try {
            AppealRepresentation responseRepresentation = new ReadAppealActivity().retrieveByUri(new CSE564AppealsUri(uriInfo.getRequestUri()));
            response = Response.status(Status.OK).entity(responseRepresentation).build();
        } catch(NoSuchAppealException nsoe) {
            LOG.info("No such appeal");
            response = Response.status(Status.NOT_FOUND).build();
        } catch (Exception ex) {
            LOG.info("Something went wrong retriveing the Appeal");
            response = Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        
        LOG.info("Retrieved the appeal resource", response);
        
        return response;
    }
    
    @POST
    @Consumes("application/vnd.cse564-appeals+xml")
    @Produces("application/vnd.cse564-appeals+xml")
    public Response createAppeal(String appealRepresentation) {
        LOG.info("Creating an Appeal Resource");
        
        Response response;
        
        try {
            AppealRepresentation responseRepresentation = new CreateAppealActivity().create(AppealRepresentation.fromXmlString(appealRepresentation).getAppeal(), new CSE564AppealsUri(uriInfo.getRequestUri()));
            response = Response.created(responseRepresentation.getUpdateLink().getUri()).entity(responseRepresentation).build();
        } catch (InvalidAppealException ioe) {
            LOG.info("Invalid Appeal - Problem with the appealrepresentation {}", appealRepresentation);
            response = Response.status(Status.BAD_REQUEST).build();
        } catch (Exception ex) {
            LOG.info("Someting went wrong creating the appeal resource");
            response = Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        
        LOG.info("Resulting response for creating the appeal resource is {}", response);
        
        return response;
    }

    @DELETE
    @Path("/{appealId}")
    @Produces("application/vnd.cse564-appeals+xml")
    public Response removeAppeal() {
        LOG.info("Removing an Appeal Reource");
        
        Response response;
        
        try {
            AppealRepresentation removedAppeal = new RemoveAppealActivity().delete(new CSE564AppealsUri(uriInfo.getRequestUri()));
            response = Response.status(Status.OK).entity(removedAppeal).build();
        } catch (NoSuchAppealException nsoe) {
            LOG.info("No such appeal resource to delete");
            response = Response.status(Status.NOT_FOUND).build();
        } catch(AppealDeletionException ode) {
            LOG.info("Problem deleting appeal resource");
            response = Response.status(Status.METHOD_NOT_ALLOWED).header("Allow", "GET").build();
        } catch (Exception ex) {
            LOG.info("Something went wrong deleting the appeal resource");
            response = Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        
        LOG.info("Resulting response for deleting the appeal resource is {}", response);
        
        return response;
    }

    @POST
    @Path("/{appealId}")
    @Consumes("application/vnd.cse564-appeals+xml")
    @Produces("application/vnd.cse564-appeals+xml")
    public Response updateAppeal(String appealRepresentation) {
        LOG.info("Updating an Appeal Resource");
        
        Response response;
        
        try {
            AppealRepresentation responseRepresentation = new UpdateAppealActivity().update(AppealRepresentation.fromXmlString(appealRepresentation).getAppeal(), new CSE564AppealsUri(uriInfo.getRequestUri()));
            response = Response.status(Status.OK).entity(responseRepresentation).build();
        } catch (InvalidAppealException ioe) {
            LOG.info("Invalid appeal in the XML representation {}", appealRepresentation);
            response = Response.status(Status.BAD_REQUEST).build();
        } catch (NoSuchAppealException nsoe) {
            LOG.info("No such appeal resource to update");
            response = Response.status(Status.NOT_FOUND).build();
        } catch(UpdateException ue) {
            LOG.info("Problem updating the appeal resource");
            response = Response.status(Status.CONFLICT).build();
        } catch (Exception ex) {
            LOG.info("Something went wrong updating the appeal resource");
            response = Response.status(Status.INTERNAL_SERVER_ERROR).build();
        } 
        
        LOG.info("Resulting response for updating the appeal resource is {}", response);
        
        return response;
     }
}
