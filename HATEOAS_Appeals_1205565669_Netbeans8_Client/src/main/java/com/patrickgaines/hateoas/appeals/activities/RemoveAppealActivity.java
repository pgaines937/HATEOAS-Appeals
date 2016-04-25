package com.patrickgaines.hateoas.appeals.activities;

import com.patrickgaines.hateoas.appeals.model.Identifier;
import com.patrickgaines.hateoas.appeals.model.Appeal;
import com.patrickgaines.hateoas.appeals.model.AppealStatus;
import com.patrickgaines.hateoas.appeals.repositories.AppealRepository;
import com.patrickgaines.hateoas.appeals.representations.AppealRepresentation;
import com.patrickgaines.hateoas.appeals.representations.CSE564AppealsUri;

public class RemoveAppealActivity {
    public AppealRepresentation delete(CSE564AppealsUri appealUri) {
        // Discover the URI of the appeal that has been cancelled
        
        Identifier identifier = appealUri.getId();

        AppealRepository appealRepository = AppealRepository.current();

        if (appealRepository.appealNotPlaced(identifier)) {
            throw new NoSuchAppealException();
        }

        Appeal appeal = appealRepository.get(identifier);

        // Can't delete a rechecked or reviewing appeal
        if (appeal.getStatus() == AppealStatus.REVIEWING || appeal.getStatus() == AppealStatus.RECHECKED) {
            throw new AppealDeletionException();
        }

        if(appeal.getStatus() == AppealStatus.UNSUBMITTED) { // An unsubmitted appeal is being cancelled 
            appealRepository.remove(identifier);
        }

        return new AppealRepresentation(appeal);
    }

}
