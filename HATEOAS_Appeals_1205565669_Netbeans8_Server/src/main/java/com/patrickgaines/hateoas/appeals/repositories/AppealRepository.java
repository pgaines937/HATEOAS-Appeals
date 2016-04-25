package com.patrickgaines.hateoas.appeals.repositories;

import java.util.HashMap;
import java.util.Map.Entry;

import com.patrickgaines.hateoas.appeals.model.Identifier;
import com.patrickgaines.hateoas.appeals.model.Appeal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppealRepository {
    
    private static final Logger LOG = LoggerFactory.getLogger(AppealRepository.class);

    private static final AppealRepository theRepository = new AppealRepository();
    private HashMap<String, Appeal> backingStore = new HashMap<>(); // Default implementation, not suitable for production!

    public static AppealRepository current() {
        return theRepository;
    }
    
    private AppealRepository(){
        LOG.info("AppealRepository Constructor");
    }
    
    public Appeal get(Identifier identifier) {
        LOG.info("Retrieving Appeal object for identifier {}", identifier);
        return backingStore.get(identifier.toString());
     }
    
    public Appeal take(Identifier identifier) {
        LOG.info("Removing the Appeal object for identifier {}", identifier);
        Appeal appeal = backingStore.get(identifier.toString());
        remove(identifier);
        return appeal;
    }

    public Identifier store(Appeal appeal) {
        LOG.info("Storing a new Appeal object");
                
        Identifier id = new Identifier();
        LOG.info("New appeal object id is {}", id);
                
        backingStore.put(id.toString(), appeal);
        return id;
    }
    
    public void store(Identifier identifier, Appeal appeal) {
        LOG.info("Storing again the Appeal object with id", identifier);
        backingStore.put(identifier.toString(), appeal);
    }

    public boolean has(Identifier identifier) {
        LOG.info("Checking to see if there is an Appeal object associated with the id {} in the Appeal store", identifier);
        
        boolean result =  backingStore.containsKey(identifier.toString());
        LOG.info("The result of the search is {}", result);
        
        return result;
    }

    public void remove(Identifier identifier) {
        LOG.info("Removing from storage the Appeal object with id", identifier);
        backingStore.remove(identifier.toString());
    }
    
    public boolean appealPlaced(Identifier identifier) {
        LOG.info("Checking to see if the appeal with id = {} has been place", identifier);
        return AppealRepository.current().has(identifier);
    }
    
    public boolean appealNotPlaced(Identifier identifier) {
        LOG.info("Checking to see if the appeal with id = {} has not been place", identifier);
        return !appealPlaced(identifier);
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Entry<String, Appeal> entry : backingStore.entrySet()) {
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

    public int size() {
        return backingStore.size();
    }
}
