package com.patrickgaines.hateoas.appeals.activities;

import com.patrickgaines.hateoas.appeals.representations.CSE564AppealsUri;

public class UriExchange {

    public static CSE564AppealsUri submissionForAppeal(CSE564AppealsUri appealUri) {
        checkForValidAppealUri(appealUri);
        return new CSE564AppealsUri(appealUri.getBaseUri() + "/submission/" + appealUri.getId().toString());
    }
    
    public static CSE564AppealsUri appealForSubmission(CSE564AppealsUri submissionUri) {
        checkForValidSubmissionUri(submissionUri);
        return new CSE564AppealsUri(submissionUri.getBaseUri() + "/appeal/" + submissionUri.getId().toString());
    }

    public static CSE564AppealsUri replyForSubmission(CSE564AppealsUri submissionUri) {
        checkForValidSubmissionUri(submissionUri);
        return new CSE564AppealsUri(submissionUri.getBaseUri() + "/reply/" + submissionUri.getId().toString());
    }
    
    public static CSE564AppealsUri appealForReply(CSE564AppealsUri replyUri) {
        checkForValidReplyUri(replyUri);
        return new CSE564AppealsUri(replyUri.getBaseUri() + "/appeal/" + replyUri.getId().toString());
    }

    private static void checkForValidAppealUri(CSE564AppealsUri appealUri) {
        if(!appealUri.toString().contains("/appeal/")) {
            throw new RuntimeException("Invalid Appeal URI");
        }
    }
    
    private static void checkForValidSubmissionUri(CSE564AppealsUri submission) {
        if(!submission.toString().contains("/submission/")) {
            throw new RuntimeException("Invalid Submission URI");
        }
    }
    
    private static void checkForValidReplyUri(CSE564AppealsUri reply) {
        if(!reply.toString().contains("/reply/")) {
            throw new RuntimeException("Invalid Reply URI");
        }
    }
}
