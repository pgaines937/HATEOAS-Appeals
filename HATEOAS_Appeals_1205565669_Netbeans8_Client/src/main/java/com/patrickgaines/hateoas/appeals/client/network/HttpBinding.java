package com.patrickgaines.hateoas.appeals.client.network;

import java.net.URI;

import com.patrickgaines.hateoas.appeals.client.activities.CannotCancelException;
import com.patrickgaines.hateoas.appeals.client.activities.CannotUpdateAppealException;
import com.patrickgaines.hateoas.appeals.client.activities.DuplicateSubmissionException;
import com.patrickgaines.hateoas.appeals.client.activities.InvalidSubmissionException;
import com.patrickgaines.hateoas.appeals.client.activities.MalformedAppealException;
import com.patrickgaines.hateoas.appeals.client.activities.NotFoundException;
import com.patrickgaines.hateoas.appeals.client.activities.ServiceFailureException;
import com.patrickgaines.hateoas.appeals.model.Appeal;
import com.patrickgaines.hateoas.appeals.model.Submission;
import com.patrickgaines.hateoas.appeals.representations.AppealRepresentation;
import com.patrickgaines.hateoas.appeals.representations.SubmissionRepresentation;
import com.patrickgaines.hateoas.appeals.representations.ReplyRepresentation;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;

public class HttpBinding {

    private static final String CSE564_APPEALS_MEDIA_TYPE = "application/vnd.cse564-appeals+xml";

    public AppealRepresentation createAppeal(Appeal appeal, URI appealUri) throws MalformedAppealException, ServiceFailureException {
        Client client = Client.create();
        ClientResponse response = client.resource(appealUri).accept(CSE564_APPEALS_MEDIA_TYPE).type(CSE564_APPEALS_MEDIA_TYPE).post(ClientResponse.class, new AppealRepresentation(appeal));

        int status = response.getStatus();

        if (status == 400) {
            throw new MalformedAppealException();
        } else if (status == 500) {
            throw new ServiceFailureException();
        } else if (status == 201) {
            return response.getEntity(AppealRepresentation.class);
        }

        throw new RuntimeException(String.format("Unexpected response [%d] while creating appeal resource [%s]", status, appealUri.toString()));
    }
    
    public AppealRepresentation retrieveAppeal(URI appealUri) throws NotFoundException, ServiceFailureException {
        Client client = Client.create();
        ClientResponse response = client.resource(appealUri).accept(CSE564_APPEALS_MEDIA_TYPE).get(ClientResponse.class);

        int status = response.getStatus();

        if (status == 404) {
            throw new NotFoundException ();
        } else if (status == 500) {
            throw new ServiceFailureException();
        } else if (status == 200) {
            return response.getEntity(AppealRepresentation.class);
        }

        throw new RuntimeException(String.format("Unexpected response while retrieving appeal resource [%s]", appealUri.toString()));
    }

    public AppealRepresentation updateAppeal(Appeal appeal, URI appealUri) throws MalformedAppealException, ServiceFailureException, NotFoundException,
            CannotUpdateAppealException {
        Client client = Client.create();
        ClientResponse response = client.resource(appealUri).accept(CSE564_APPEALS_MEDIA_TYPE).type(CSE564_APPEALS_MEDIA_TYPE).post(ClientResponse.class, new AppealRepresentation(appeal));

        int status = response.getStatus();

        if (status == 400) {
            throw new MalformedAppealException();
        } else if (status == 404) {
            throw new NotFoundException();
        } else if (status == 409) {
            throw new CannotUpdateAppealException();
        } else if (status == 500) {
            throw new ServiceFailureException();
        } else if (status == 200) {
            return response.getEntity(AppealRepresentation.class);
        }

        throw new RuntimeException(String.format("Unexpected response [%d] while udpating appeal resource [%s]", status, appealUri.toString()));
    }

    public AppealRepresentation deleteAppeal(URI appealUri) throws ServiceFailureException, CannotCancelException, NotFoundException {
        Client client = Client.create();
        ClientResponse response = client.resource(appealUri).accept(CSE564_APPEALS_MEDIA_TYPE).delete(ClientResponse.class);

        int status = response.getStatus();
        if (status == 404) {
            throw new NotFoundException();
        } else if (status == 405) {
            throw new CannotCancelException();
        } else if (status == 500) {
            throw new ServiceFailureException();
        } else if (status == 200) {
            return response.getEntity(AppealRepresentation.class);
        }

        throw new RuntimeException(String.format("Unexpected response [%d] while deleting appeal resource [%s]", status, appealUri.toString()));
    }

    public SubmissionRepresentation makeSubmission(Submission submission, URI submissionUri) throws InvalidSubmissionException, NotFoundException, DuplicateSubmissionException,
            ServiceFailureException {
        Client client = Client.create();
        ClientResponse response = client.resource(submissionUri).accept(CSE564_APPEALS_MEDIA_TYPE).type(CSE564_APPEALS_MEDIA_TYPE).put(ClientResponse.class, new SubmissionRepresentation(submission));

        int status = response.getStatus();
        if (status == 400) {
            throw new InvalidSubmissionException();
        } else if (status == 404) {
            throw new NotFoundException();
        } else if (status == 405) {
            throw new DuplicateSubmissionException();
        } else if (status == 500) {
            throw new ServiceFailureException();
        } else if (status == 201) {
            return response.getEntity(SubmissionRepresentation.class);
        }

        throw new RuntimeException(String.format("Unexpected response [%d] while creating submission resource [%s]", status, submissionUri.toString()));
    }

    public ReplyRepresentation retrieveReply(URI replyUri) throws NotFoundException, ServiceFailureException {
        Client client = Client.create();
        ClientResponse response = client.resource(replyUri).accept(CSE564_APPEALS_MEDIA_TYPE).get(ClientResponse.class);

        int status = response.getStatus();
        if (status == 404) {
            throw new NotFoundException();
        } else if (status == 500) {
            throw new ServiceFailureException();
        } else if (status == 200) {
            return response.getEntity(ReplyRepresentation.class);
        }
        
        throw new RuntimeException(String.format("Unexpected response [%d] while retrieving reply resource [%s]", status, replyUri.toString()));
    }
}
