package com.patrickgaines.hateoas.appeals.client.activities;

import java.net.URI;

import com.patrickgaines.hateoas.appeals.representations.ReplyRepresentation;

public class GetReplyActivity extends Activity {
    private final URI replyUri;
    private ReplyRepresentation representation;

    public GetReplyActivity(URI replyUri) {
        this.replyUri = replyUri;
    }

    public void getReplyForAppeal() {
        try {
            representation = binding.retrieveReply(replyUri);
            actions = new Actions();
            if(representation.getAppealLink() != null) {
                actions.add(new ReadAppealActivity(representation.getAppealLink().getUri()));
            } else {
                actions =  noFurtherActivities();
            }
        } catch (NotFoundException e) {
            actions = noFurtherActivities();
        } catch (ServiceFailureException e) {
            actions = retryCurrentActivity();
        }
    }

    public ReplyRepresentation getReply() {
        return representation;
    }
}
