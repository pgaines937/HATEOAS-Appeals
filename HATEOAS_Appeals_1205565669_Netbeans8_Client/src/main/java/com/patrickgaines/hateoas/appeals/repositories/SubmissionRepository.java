package com.patrickgaines.hateoas.appeals.repositories;

import java.util.HashMap;
import java.util.Map.Entry;

import com.patrickgaines.hateoas.appeals.model.Identifier;
import com.patrickgaines.hateoas.appeals.model.Submission;

public class SubmissionRepository {

    private static final SubmissionRepository theRepository = new SubmissionRepository();
    private HashMap<String, Submission> backingStore = new HashMap<String, Submission>(); // Default implementation, not suitable for production!

    public static SubmissionRepository current() {
        return theRepository;
    }
    
    private SubmissionRepository(){}
    
    public Submission get(Identifier identifier) {
        return backingStore.get(identifier.toString());
    }
    
    public Submission take(Identifier identifier) {
        Submission submission = backingStore.get(identifier.toString());
        remove(identifier);
        return submission;
    }

    public Identifier store(Submission submission) {
        Identifier id = new Identifier();
        backingStore.put(id.toString(), submission);
        return id;
    }
    
    public void store(Identifier submissionIdentifier, Submission submission) {
        backingStore.put(submissionIdentifier.toString(), submission);
    }

    public boolean has(Identifier identifier) {
        boolean result =  backingStore.containsKey(identifier.toString());
        return result;
    }

    public void remove(Identifier identifier) {
        backingStore.remove(identifier.toString());
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Entry<String, Submission> entry : backingStore.entrySet()) {
            sb.append(entry.getKey());
            sb.append("\t:\t");
            sb.append(entry.getValue());
            sb.append("\n");
        }
        return sb.toString();
    }

    public synchronized void clear() {
        backingStore = new HashMap<String, Submission>();
    }
}
