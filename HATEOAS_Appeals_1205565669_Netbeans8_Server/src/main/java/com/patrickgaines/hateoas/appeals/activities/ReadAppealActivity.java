package com.patrickgaines.hateoas.appeals.activities;

import com.patrickgaines.hateoas.appeals.model.Identifier;
import com.patrickgaines.hateoas.appeals.model.Appeal;
import com.patrickgaines.hateoas.appeals.repositories.AppealRepository;
import com.patrickgaines.hateoas.appeals.representations.AppealRepresentation;
import com.patrickgaines.hateoas.appeals.representations.CSE564AppealsUri;

public class ReadAppealActivity {
    public AppealRepresentation retrieveByUri(CSE564AppealsUri appealUri) {
        Identifier identifier  = appealUri.getId();
        
        Appeal appeal = AppealRepository.current().get(identifier);
        
        if(appeal == null) {
            throw new NoSuchAppealException();
        }
        
        return AppealRepresentation.createResponseAppealRepresentation(appeal, appealUri);
    }
}
