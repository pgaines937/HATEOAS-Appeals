package com.patrickgaines.hateoas.appeals.client.activities;

import java.net.URI;

import com.patrickgaines.hateoas.appeals.model.Submission;
import com.patrickgaines.hateoas.appeals.representations.SubmissionRepresentation;

public class SubmissionActivity extends Activity {

    private final URI submissionUri;
    private Submission submission;

    public SubmissionActivity(URI submissionUri) {
        this.submissionUri = submissionUri;
    }

    public void submitForAppeal(Submission submission) {        
        try {
            SubmissionRepresentation submissionRepresentation = binding.makeSubmission(submission, submissionUri);
            actions = new RepresentationHypermediaProcessor().extractNextActionsFromSubmissionRepresentation(submissionRepresentation);
            submission = submissionRepresentation.getSubmission();
        } catch (InvalidSubmissionException e) {
            actions = retryCurrentActivity();
        } catch (NotFoundException e) {
            actions = noFurtherActivities();
        } catch (DuplicateSubmissionException e) {
            actions = noFurtherActivities();
        } catch (ServiceFailureException e) {
            actions = retryCurrentActivity();            
        }
    }
    
    public Submission getSubmission() {
        return submission;
    }
}
