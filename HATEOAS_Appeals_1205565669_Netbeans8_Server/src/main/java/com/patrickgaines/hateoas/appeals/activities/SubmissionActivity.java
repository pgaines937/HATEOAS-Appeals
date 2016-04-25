package com.patrickgaines.hateoas.appeals.activities;

import com.patrickgaines.hateoas.appeals.model.Identifier;
import com.patrickgaines.hateoas.appeals.model.AppealStatus;
import com.patrickgaines.hateoas.appeals.model.Submission;
import com.patrickgaines.hateoas.appeals.repositories.AppealRepository;
import com.patrickgaines.hateoas.appeals.repositories.SubmissionRepository;
import com.patrickgaines.hateoas.appeals.representations.Link;
import com.patrickgaines.hateoas.appeals.representations.SubmissionRepresentation;
import com.patrickgaines.hateoas.appeals.representations.Representation;
import com.patrickgaines.hateoas.appeals.representations.CSE564AppealsUri;

public class SubmissionActivity {
    public SubmissionRepresentation submit(Submission submission, CSE564AppealsUri submissionUri) {
        Identifier identifier = submissionUri.getId();
        
        // Don't know the appeal!
        if(!AppealRepository.current().has(identifier)) {
            throw new NoSuchAppealException();
        }
        
        // Already paid
        if(SubmissionRepository.current().has(identifier)) {
            throw new UpdateException();
        }
        
        // Business rules - if the submission amount doesn't match the amount outstanding, then reject
        //if(!SubmissionRepository.current().get(identifier).hasAppealInSubject()) {
        //    throw new InvalidSubmissionException();
        //}
        
        // If we get here, let's create the submission and update the appeal status
        AppealRepository.current().get(identifier).setStatus(AppealStatus.REVIEWING);
        SubmissionRepository.current().store(identifier, submission);
        
        return new SubmissionRepresentation(submission, new Link(Representation.RELATIONS_URI + "appeal", UriExchange.appealForSubmission(submissionUri)),
                new Link(Representation.RELATIONS_URI + "reply", UriExchange.replyForSubmission(submissionUri)));
    }
}
