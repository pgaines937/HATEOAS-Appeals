package com.patrickgaines.hateoas.appeals.repositories;

import java.util.HashMap;
import java.util.Map.Entry;

import com.patrickgaines.hateoas.appeals.model.Identifier;
import com.patrickgaines.hateoas.appeals.model.Submission;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubmissionRepository {
    
    private static final Logger LOG = LoggerFactory.getLogger(SubmissionRepository.class);

    private static final SubmissionRepository theRepository = new SubmissionRepository();
    private HashMap<String, Submission> backingStore = new HashMap<>(); // Default implementation, not suitable for production!

    public static SubmissionRepository current() {
        return theRepository;
    }
    
    private SubmissionRepository(){
        LOG.info("SubmissionRepository Constructor");
    }
    
    public Submission get(Identifier identifier) {
        LOG.info("Retrieving Submission object for identifier {}", identifier);
        return backingStore.get(identifier.toString());
    }
    
    public Submission take(Identifier identifier) {
        LOG.info("Removing the Submission object for identifier {}", identifier);
        Submission submission = backingStore.get(identifier.toString());
        remove(identifier);
        return submission;
    }

    public Identifier store(Submission submission) {
        LOG.info("Storing a new Submission object");
        
        Identifier id = new Identifier();
        LOG.info("New submission object's id is {}", id);
        
        backingStore.put(id.toString(), submission);
        return id;
    }
    
    public void store(Identifier identifier, Submission submission) {
        LOG.info("Storing again the Appeal object with id", identifier);
        backingStore.put(identifier.toString(), submission);
    }

    public boolean has(Identifier identifier) {
        LOG.info("Checking to see if there is a submission object associated with the id {} in the Submission store", identifier);
        
        boolean result =  backingStore.containsKey(identifier.toString());
        return result;
    }

    public void remove(Identifier identifier) {
        LOG.info("Removing from storage the Submission object with id", identifier);
        backingStore.remove(identifier.toString());
    }
    
    @Override
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
        backingStore = new HashMap<>();
    }
}
