package com.patrickgaines.hateoas.appeals.activities;

import com.patrickgaines.hateoas.appeals.model.Identifier;
import com.patrickgaines.hateoas.appeals.model.Appeal;
import com.patrickgaines.hateoas.appeals.model.AppealStatus;
import com.patrickgaines.hateoas.appeals.repositories.AppealRepository;
import com.patrickgaines.hateoas.appeals.representations.AppealRepresentation;
import com.patrickgaines.hateoas.appeals.representations.CSE564AppealsUri;

public class UpdateAppealActivity {
    public AppealRepresentation update(Appeal appeal, CSE564AppealsUri appealUri) {
        Identifier appealIdentifier = appealUri.getId();

        AppealRepository repository = AppealRepository.current();
        if (AppealRepository.current().appealNotPlaced(appealIdentifier)) { // Defensive check to see if we have the appeal
            throw new NoSuchAppealException();
        }

        if (!appealCanBeChanged(appealIdentifier)) {
            throw new UpdateException();
        }

        Appeal storedAppeal = repository.get(appealIdentifier);
        
        storedAppeal.setStatus(storedAppeal.getStatus());
        storedAppeal.calculatePoints();


        return AppealRepresentation.createResponseAppealRepresentation(storedAppeal, appealUri); 
    }
    
    private boolean appealCanBeChanged(Identifier identifier) {
        return AppealRepository.current().get(identifier).getStatus() == AppealStatus.UNSUBMITTED;
    }
}
