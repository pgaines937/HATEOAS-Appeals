package com.patrickgaines.hateoas.appeals.activities;

import com.patrickgaines.hateoas.appeals.model.Appeal;
import com.patrickgaines.hateoas.appeals.model.Identifier;
import com.patrickgaines.hateoas.appeals.model.AppealStatus;
import com.patrickgaines.hateoas.appeals.model.Submission;
import com.patrickgaines.hateoas.appeals.repositories.AppealRepository;
import com.patrickgaines.hateoas.appeals.repositories.SubmissionRepository;
import com.patrickgaines.hateoas.appeals.representations.Link;
import com.patrickgaines.hateoas.appeals.representations.ReplyRepresentation;
import com.patrickgaines.hateoas.appeals.representations.Representation;
import com.patrickgaines.hateoas.appeals.representations.CSE564AppealsUri;

public class ReadReplyActivity {

    public ReplyRepresentation read(CSE564AppealsUri replyUri) {
        Identifier identifier = replyUri.getId();
        if(!appealHasBeenPaid(identifier)) {
            throw new AppealNotSubmittedException();
        } else if (AppealRepository.current().has(identifier) && AppealRepository.current().get(identifier).getStatus() == AppealStatus.COMPLETED) {
            throw new AppealAlreadyCompletedException();
        }
        Appeal appeal = AppealRepository.current().get(identifier);        
        Submission submission = SubmissionRepository.current().get(identifier);
        
        return new ReplyRepresentation(appeal, submission, new Link(Representation.RELATIONS_URI + "appeal", UriExchange.appealForReply(replyUri)));
    }

    private boolean appealHasBeenPaid(Identifier id) {
        return SubmissionRepository.current().has(id);
    }

}
