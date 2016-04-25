package com.patrickgaines.hateoas.appeals.activities;

import com.patrickgaines.hateoas.appeals.model.Identifier;
import com.patrickgaines.hateoas.appeals.model.Appeal;
import com.patrickgaines.hateoas.appeals.model.AppealStatus;
import com.patrickgaines.hateoas.appeals.repositories.AppealRepository;
import com.patrickgaines.hateoas.appeals.representations.Link;
import com.patrickgaines.hateoas.appeals.representations.AppealRepresentation;
import com.patrickgaines.hateoas.appeals.representations.Representation;
import com.patrickgaines.hateoas.appeals.representations.CSE564AppealsUri;

public class CreateAppealActivity {
    public AppealRepresentation create(Appeal appeal, CSE564AppealsUri requestUri) {
        appeal.setStatus(AppealStatus.UNSUBMITTED);
                
        Identifier identifier = AppealRepository.current().store(appeal);
        
        CSE564AppealsUri appealUri = new CSE564AppealsUri(requestUri.getBaseUri() + "/appeal/" + identifier.toString());
        CSE564AppealsUri submissionUri = new CSE564AppealsUri(requestUri.getBaseUri() + "/submission/" + identifier.toString());
        return new AppealRepresentation(appeal, 
                new Link(Representation.RELATIONS_URI + "cancel", appealUri), 
                new Link(Representation.RELATIONS_URI + "submission", submissionUri), 
                new Link(Representation.RELATIONS_URI + "update", appealUri),
                new Link(Representation.SELF_REL_VALUE, appealUri));
    }
}
