package com.patrickgaines.hateoas.appeals.client.activities;

import com.patrickgaines.hateoas.appeals.representations.AppealRepresentation;
import com.patrickgaines.hateoas.appeals.representations.SubmissionRepresentation;

class RepresentationHypermediaProcessor {

    Actions extractNextActionsFromAppealRepresentation(AppealRepresentation representation) {
        Actions actions = new Actions();

        if (representation != null) {

            if (representation.getSubmissionLink() != null) {
                actions.add(new SubmissionActivity(representation.getSubmissionLink().getUri()));
            }

            if (representation.getUpdateLink() != null) {
                actions.add(new UpdateAppealActivity(representation.getUpdateLink().getUri()));
            }

            if (representation.getSelfLink() != null) {
                actions.add(new ReadAppealActivity(representation.getSelfLink().getUri()));
            }

            if (representation.getCancelLink() != null) {
                actions.add(new CancelAppealActivity(representation.getCancelLink().getUri()));
            }
        }

        return actions;
    }

    public Actions extractNextActionsFromSubmissionRepresentation(SubmissionRepresentation representation) {
        Actions actions = new Actions();
        
        if(representation.getAppealLink() != null) {
            actions.add(new ReadAppealActivity(representation.getAppealLink().getUri()));
        }
        
        if(representation.getReplyLink() != null) {
            actions.add(new GetReplyActivity(representation.getReplyLink().getUri()));
        }
        
        return actions;
    }

}
