package com.patrickgaines.hateoas.appeals.client.activities;

import java.net.URI;

import com.patrickgaines.hateoas.appeals.client.ClientAppeal;
import com.patrickgaines.hateoas.appeals.model.Appeal;
import com.patrickgaines.hateoas.appeals.representations.AppealRepresentation;

public class PlaceAppealActivity extends Activity {

    private Appeal appeal;

    public void placeAppeal(Appeal appeal, URI appealingUri) {
        
        try {
            AppealRepresentation createdAppealRepresentation = binding.createAppeal(appeal, appealingUri);
            this.actions = new RepresentationHypermediaProcessor().extractNextActionsFromAppealRepresentation(createdAppealRepresentation);
            this.appeal = createdAppealRepresentation.getAppeal();
        } catch (MalformedAppealException e) {
            this.actions = retryCurrentActivity();
        } catch (ServiceFailureException e) {
            this.actions = retryCurrentActivity();
        }
    }
    
    public ClientAppeal getAppeal() {
        return new ClientAppeal(appeal);
    }
}
