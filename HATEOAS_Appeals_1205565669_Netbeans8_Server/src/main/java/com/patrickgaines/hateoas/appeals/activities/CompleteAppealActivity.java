package com.patrickgaines.hateoas.appeals.activities;

import com.patrickgaines.hateoas.appeals.model.Identifier;
import com.patrickgaines.hateoas.appeals.model.Appeal;
import com.patrickgaines.hateoas.appeals.model.AppealStatus;
import com.patrickgaines.hateoas.appeals.repositories.AppealRepository;
import com.patrickgaines.hateoas.appeals.representations.AppealRepresentation;

public class CompleteAppealActivity {

    public AppealRepresentation completeAppeal(Identifier id) {
        AppealRepository repository = AppealRepository.current();
        if (repository.has(id)) {
            Appeal appeal = repository.get(id);

            if (appeal.getStatus() == AppealStatus.RECHECKED) {
                appeal.setStatus(AppealStatus.COMPLETED);
            } else if (appeal.getStatus() == AppealStatus.COMPLETED) {
                throw new AppealAlreadyCompletedException();
            } else if (appeal.getStatus() == AppealStatus.UNSUBMITTED) {
                throw new AppealNotSubmittedException();
            }

            return new AppealRepresentation(appeal);
        } else {
            throw new NoSuchAppealException();
        }
    }
}
