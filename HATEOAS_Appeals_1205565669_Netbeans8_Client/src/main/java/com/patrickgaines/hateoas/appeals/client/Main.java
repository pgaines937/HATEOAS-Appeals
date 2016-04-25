package com.patrickgaines.hateoas.appeals.client;

import static com.patrickgaines.hateoas.appeals.model.AppealBuilder.appeal;

import java.net.URI;
import java.net.URISyntaxException;

import com.patrickgaines.hateoas.appeals.client.activities.Actions;
import com.patrickgaines.hateoas.appeals.client.activities.GetReplyActivity;
import com.patrickgaines.hateoas.appeals.client.activities.SubmissionActivity;
import com.patrickgaines.hateoas.appeals.client.activities.PlaceAppealActivity;
import com.patrickgaines.hateoas.appeals.client.activities.ReadAppealActivity;
import com.patrickgaines.hateoas.appeals.client.activities.UpdateAppealActivity;
import com.patrickgaines.hateoas.appeals.model.Appeal;
import com.patrickgaines.hateoas.appeals.model.AppealStatus;
import com.patrickgaines.hateoas.appeals.model.Submission;
import com.patrickgaines.hateoas.appeals.representations.Link;
import com.patrickgaines.hateoas.appeals.representations.AppealRepresentation;
import com.patrickgaines.hateoas.appeals.representations.SubmissionRepresentation;
import com.patrickgaines.hateoas.appeals.representations.ReplyRepresentation;
import com.patrickgaines.hateoas.appeals.representations.CSE564AppealsUri;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    
    private static final String CSE564_APPEALS_MEDIA_TYPE = "application/vnd.cse564-appeals+xml";
    private static final long ONE_MINUTE = 60000; 
    
    private static final String ENTRY_POINT_URI = "http://localhost:8080/HATEOAS_Appeals_1205565669_Netbeans8_Server/webresources/appeal";

    public static void main(String[] args) throws Exception {
        URI serviceUri = new URI(ENTRY_POINT_URI);
        happyPathTest(serviceUri);
        abandonedPathTest(serviceUri);
        forgottenPathTest(serviceUri);
        badStartPathTest(serviceUri);
        badIdPathTest(serviceUri);
    }

    private static void hangAround(long backOffTimeInMillis) {
        try {
            Thread.sleep(backOffTimeInMillis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void happyPathTest(URI serviceUri) throws Exception {
        LOG.info("Starting Happy Path Test with Service URI {}", serviceUri);
        // Place the appeal
        LOG.info("Step 1. Place the appeal");
        System.out.println(String.format("About to start happy path test. Placing appeal at [%s] via POST", serviceUri.toString()));
        Appeal appeal = appeal().withRandomItems().build();
        LOG.info("Created base appeal {}", appeal);
        Client client = Client.create();
        LOG.info("Created client {}", client);
        AppealRepresentation appealRepresentation = client.resource(serviceUri).accept(CSE564_APPEALS_MEDIA_TYPE).type(CSE564_APPEALS_MEDIA_TYPE).post(AppealRepresentation.class, new ClientAppeal(appeal));
        LOG.info("Created appealRepresentation {} denoted by the URI {}", appealRepresentation, appealRepresentation.getSelfLink().getUri().toString());
        System.out.println(String.format("Appeal placed at [%s]", appealRepresentation.getSelfLink().getUri().toString()));
       
        // Try to update a different appeal
        LOG.info("\n\nStep 2. Try to update a different appeal");
        System.out.println(String.format("About to update an appeal with bad URI [%s] via POST", appealRepresentation.getUpdateLink().getUri().toString() + "/bad-uri"));
        appeal = appeal().withRandomItems().build();
        LOG.info("Created base appeal {}", appeal);
        Link badLink = new Link("bad", new CSE564AppealsUri(appealRepresentation.getSelfLink().getUri().toString() + "/bad-uri"), CSE564_APPEALS_MEDIA_TYPE);
        LOG.info("Create bad link {}", badLink);
        ClientResponse badUpdateResponse = client.resource(badLink.getUri()).accept(badLink.getMediaType()).type(badLink.getMediaType()).post(ClientResponse.class, new AppealRepresentation(appeal));
        LOG.info("Created Bad Update Response {}", badUpdateResponse);
        System.out.println(String.format("Tried to update appeal with bad URI at [%s] via POST, outcome [%d]", badLink.getUri().toString(), badUpdateResponse.getStatus()));
         
        // Change the appeal
        LOG.info("\n\nStep 3. Change the appeal");
        System.out.println(String.format("About to update appeal at [%s] via POST", appealRepresentation.getUpdateLink().getUri().toString()));
        appeal = appeal().withRandomItems().build();
        LOG.info("Created base appeal {}", appeal);
        Link updateLink = appealRepresentation.getUpdateLink();
        LOG.info("Created appeal update link {}", updateLink);
        AppealRepresentation updatedRepresentation = client.resource(updateLink.getUri()).accept(updateLink.getMediaType()).type(updateLink.getMediaType()).post(AppealRepresentation.class, new AppealRepresentation(appeal));
        LOG.info("Created updated appeal representation link {}", updatedRepresentation);
        System.out.println(String.format("Appeal updated at [%s]", updatedRepresentation.getSelfLink().getUri().toString()));
     
        // Submit the appeal 
        LOG.info("\n\nStep 4. Submit for the appeal");
        System.out.println(String.format("About to create a submission resource at [%s] via PUT", updatedRepresentation.getSubmissionLink().getUri().toString()));
        Link submissionLink = updatedRepresentation.getSubmissionLink();
        LOG.info("Created submission link {} for updated appeal representation {}", submissionLink, updatedRepresentation);
        LOG.info("submissionLink.getRelValue() = {}", submissionLink.getRelValue());
        LOG.info("submissionLink.getUri() = {}", submissionLink.getUri());
        LOG.info("submissionLink.getMediaType() = {}", submissionLink.getMediaType());
        Submission submission = new Submission("Appeal");
        LOG.info("Created new submission object {}", submission);
        SubmissionRepresentation  submissionRepresentation = client.resource(submissionLink.getUri()).accept(submissionLink.getMediaType()).type(submissionLink.getMediaType()).put(SubmissionRepresentation.class, new SubmissionRepresentation(submission));        
        LOG.info("Created new submission representation {}", submissionRepresentation);
        System.out.println(String.format("Submission made, reply at [%s]", submissionRepresentation.getReplyLink().getUri().toString()));
       
        // Get a reply
        LOG.info("\n\nStep 5. Get a reply");
        System.out.println(String.format("About to request a reply from [%s] via GET", submissionRepresentation.getReplyLink().getUri().toString()));
        Link replyLink = submissionRepresentation.getReplyLink();
        LOG.info("Retrieved the reply link {} for submission represntation {}", replyLink, submissionRepresentation);
        ReplyRepresentation replyRepresentation = client.resource(replyLink.getUri()).get(ReplyRepresentation.class);
        System.out.println(String.format("Submission made, amount in reply [%f]", replyRepresentation.getAmountPaid()));
        
        // Check on the appeal status
        LOG.info("\n\nStep 6. Check on the appeal status");
        System.out.println(String.format("About to check appeal status at [%s] via GET", replyRepresentation.getAppealLink().getUri().toString()));
        Link appealLink = replyRepresentation.getAppealLink();
        AppealRepresentation finalAppealRepresentation = client.resource(appealLink.getUri()).accept(CSE564_APPEALS_MEDIA_TYPE).get(AppealRepresentation.class);
        System.out.println(String.format("Final appeal placed, current status [%s]", finalAppealRepresentation.getStatus()));
        
        // Allow the barista some time to make the appeal
        LOG.info("\n\nStep 7. Allow the instructor some time to make the appeal");
        System.out.println("Pausing the client, press a key to continue");
        System.in.read();
        
        // Check the appeal if possible
        LOG.info("\n\nStep 8. Check the appeal if possible");
        System.out.println(String.format("Trying to take the rechecked appeal from [%s] via DELETE. Note: the internal state machine must progress the appeal to rechecked before this should work, otherwise expect a 405 response.", replyRepresentation.getAppealLink().getUri().toString()));
        ClientResponse finalResponse = client.resource(appealLink.getUri()).delete(ClientResponse.class);
        System.out.println(String.format("Tried to take final appeal, HTTP status [%d]", finalResponse.getStatus()));
        if(finalResponse.getStatus() == 200) {
            System.out.println(String.format("Appeal status [%s], enjoy your grade", finalResponse.getEntity(AppealRepresentation.class).getStatus()));
        }
    }
    
    private static void abandonedPathTest(URI serviceUri) throws Exception {
        LOG.info("Starting Abandoned Path Test with Service URI {}", serviceUri);
        // Place the appeal
        LOG.info("Step 1. Place the appeal");
        System.out.println(String.format("About to start happy path test. Placing appeal at [%s] via POST", serviceUri.toString()));
        Appeal appeal = appeal().withRandomItems().build();
        LOG.info("Created base appeal {}", appeal);
        Client client = Client.create();
        LOG.info("Created client {}", client);
        AppealRepresentation appealRepresentation = client.resource(serviceUri).accept(CSE564_APPEALS_MEDIA_TYPE).type(CSE564_APPEALS_MEDIA_TYPE).post(AppealRepresentation.class, new ClientAppeal(appeal));
        LOG.info("Created appealRepresentation {} denoted by the URI {}", appealRepresentation, appealRepresentation.getSelfLink().getUri().toString());
        System.out.println(String.format("Appeal placed at [%s]", appealRepresentation.getSelfLink().getUri().toString()));    
        
        // Cancel the appeal
        LOG.info("Step 2. Cancel the appeal");        
        ClientResponse finalResponse = client.resource(appealRepresentation.getCancelLink().getUri()).delete(ClientResponse.class);
        LOG.info("Deleted appealRepresentation {} denoted by the URI {}", appealRepresentation, appealRepresentation.getSelfLink().getUri().toString());
        System.out.println(String.format("Appeal deleted at [%s]", appealRepresentation.getSelfLink().getUri().toString()));        
    }
    
     
    private static void forgottenPathTest(URI serviceUri) throws Exception {
        LOG.info("Starting Forgotten Path Test with Service URI {}", serviceUri);
        // Place the appeal
        LOG.info("Step 1. Place the appeal");
        System.out.println(String.format("About to start happy path test. Placing appeal at [%s] via POST", serviceUri.toString()));
        Appeal appeal = appeal().withRandomItems().build();
        LOG.info("Created base appeal {}", appeal);
        Client client = Client.create();
        LOG.info("Created client {}", client);
        AppealRepresentation appealRepresentation = client.resource(serviceUri).accept(CSE564_APPEALS_MEDIA_TYPE).type(CSE564_APPEALS_MEDIA_TYPE).post(AppealRepresentation.class, new ClientAppeal(appeal));
        LOG.info("Created appealRepresentation {} denoted by the URI {}", appealRepresentation, appealRepresentation.getSelfLink().getUri().toString());
        System.out.println(String.format("Appeal placed at [%s]", appealRepresentation.getSelfLink().getUri().toString()));    
        
        // Submit the appeal 
        LOG.info("\n\nStep 2. Submit for the appeal");
        System.out.println(String.format("About to create a submission resource at [%s] via PUT", appealRepresentation.getSubmissionLink().getUri().toString()));
        Link submissionLink = appealRepresentation.getSubmissionLink();
        LOG.info("Created submission link {} for updated appeal representation {}", submissionLink, appealRepresentation);
        LOG.info("submissionLink.getRelValue() = {}", submissionLink.getRelValue());
        LOG.info("submissionLink.getUri() = {}", submissionLink.getUri());
        LOG.info("submissionLink.getMediaType() = {}", submissionLink.getMediaType());
        Submission submission = new Submission("Appeal");
        LOG.info("Created new submission object {}", submission);
        SubmissionRepresentation  submissionRepresentation = client.resource(submissionLink.getUri()).accept(submissionLink.getMediaType()).type(submissionLink.getMediaType()).put(SubmissionRepresentation.class, new SubmissionRepresentation(submission));        
        LOG.info("Created new submission representation {}", submissionRepresentation);
        System.out.println(String.format("Submission made, reply at [%s]", submissionRepresentation.getReplyLink().getUri().toString()));  
        
        // Check on the appeal status
        LOG.info("\n\nStep 3. Check on the appeal status");
        System.out.println(String.format("About to check appeal status at [%s] via GET", appealRepresentation.getSelfLink().getUri().toString()));
        System.out.println(String.format("Current status [%s]", appealRepresentation.getStatus()));        
    }   
    
    private static void badStartPathTest(URI serviceUri) throws Exception {
        LOG.info("Starting Bad Start Test with Service URI {}", serviceUri);
        // Place the appeal
        LOG.info("Step 1. Place the appeal");
        System.out.println(String.format("About to start happy path test. Placing appeal at [%s] via POST", serviceUri.toString()));
        Appeal appeal = appeal().withRandomItems().build();
        LOG.info("Created base appeal {}", appeal);
        Client client = Client.create();
        LOG.info("Created client {}", client);
        AppealRepresentation appealRepresentation = client.resource(serviceUri).accept(CSE564_APPEALS_MEDIA_TYPE).type(CSE564_APPEALS_MEDIA_TYPE).post(AppealRepresentation.class, new ClientAppeal(appeal));
        LOG.info("Created appealRepresentation {} denoted by the URI {}", appealRepresentation, appealRepresentation.getSelfLink().getUri().toString());
        System.out.println(String.format("Appeal placed at [%s]", appealRepresentation.getSelfLink().getUri().toString()));
       
        // Try to update a different appeal
        LOG.info("\n\nStep 2. Try to update a different appeal");
        System.out.println(String.format("About to update an appeal with bad URI [%s] via POST", appealRepresentation.getUpdateLink().getUri().toString() + "/bad-uri"));
        appeal = appeal().withRandomItems().build();
        LOG.info("Created base appeal {}", appeal);
        Link badLink = new Link("bad", new CSE564AppealsUri(appealRepresentation.getSelfLink().getUri().toString() + "/bad-uri"), CSE564_APPEALS_MEDIA_TYPE);
        LOG.info("Create bad link {}", badLink);
        ClientResponse badUpdateResponse = client.resource(badLink.getUri()).accept(badLink.getMediaType()).type(badLink.getMediaType()).post(ClientResponse.class, new AppealRepresentation(appeal));
        LOG.info("Created Bad Update Response {}", badUpdateResponse);
        System.out.println(String.format("Tried to update appeal with bad URI at [%s] via POST, outcome [%d]", badLink.getUri().toString(), badUpdateResponse.getStatus()));   
    }
    
    private static void badIdPathTest(URI serviceUri) throws Exception {
        LOG.info("Starting Forgotten Path Test with Service URI {}", serviceUri);
        // Place the appeal
        LOG.info("Step 1. Place the appeal");
        System.out.println(String.format("About to start happy path test. Placing appeal at [%s] via POST", serviceUri.toString()));
        Appeal appeal = appeal().withRandomItems().build();
        LOG.info("Created base appeal {}", appeal);
        Client client = Client.create();
        LOG.info("Created client {}", client);
        AppealRepresentation appealRepresentation = client.resource(serviceUri).accept(CSE564_APPEALS_MEDIA_TYPE).type(CSE564_APPEALS_MEDIA_TYPE).post(AppealRepresentation.class, new ClientAppeal(appeal));
        LOG.info("Created appealRepresentation {} denoted by the URI {}", appealRepresentation, appealRepresentation.getSelfLink().getUri().toString());
        System.out.println(String.format("Appeal placed at [%s]", appealRepresentation.getSelfLink().getUri().toString()));    
        
        // Submit the appeal 
        LOG.info("\n\nStep 2. Submit for the appeal");
        System.out.println(String.format("About to create a submission resource at [%s] via PUT", appealRepresentation.getSubmissionLink().getUri().toString()));
        Link submissionLink = appealRepresentation.getSubmissionLink();
        LOG.info("Created submission link {} for updated appeal representation {}", submissionLink, appealRepresentation);
        LOG.info("submissionLink.getRelValue() = {}", submissionLink.getRelValue());
        LOG.info("submissionLink.getUri() = {}", submissionLink.getUri());
        LOG.info("submissionLink.getMediaType() = {}", submissionLink.getMediaType());
        Submission submission = new Submission("Appeal");
        LOG.info("Created new submission object {}", submission);
        SubmissionRepresentation  submissionRepresentation = client.resource(submissionLink.getUri()).accept(submissionLink.getMediaType()).type(submissionLink.getMediaType()).put(SubmissionRepresentation.class, new SubmissionRepresentation(submission));        
        LOG.info("Created new submission representation {}", submissionRepresentation);
        System.out.println(String.format("Submission made, reply at [%s]", submissionRepresentation.getReplyLink().getUri().toString()));  
        
        // Check on the appeal status
        LOG.info("\n\nStep 3. Check on the appeal status");
        System.out.println(String.format("About to check appeal status at [%s] via GET", appealRepresentation.getSelfLink().getUri().toString()));
        System.out.println(String.format("Current status [%s]", appealRepresentation.getStatus()));        
    }   
    
}
