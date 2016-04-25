package com.patrickgaines.hateoas.appeals.repositories;

import java.util.HashMap;
import java.util.Map.Entry;

import com.patrickgaines.hateoas.appeals.model.Identifier;
import com.patrickgaines.hateoas.appeals.model.Appeal;

public class AppealRepository {

    private static final AppealRepository theRepository = new AppealRepository();
    private HashMap<String, Appeal> backingStore = new HashMap<String, Appeal>(); // Default implementation, not suitable for production!

    public static AppealRepository current() {
        return theRepository;
    }
    
    private AppealRepository(){}
    
    public Appeal get(Identifier identifier) {
        return backingStore.get(identifier.toString());
     }
    
    public Appeal take(Identifier identifier) {
        Appeal appeal = backingStore.get(identifier.toString());
        remove(identifier);
        return appeal;
    }

    public Identifier store(Appeal appeal) {
        Identifier id = new Identifier();
        backingStore.put(id.toString(), appeal);
        return id;
    }
    
    public void store(Identifier appealIdentifier, Appeal appeal) {
        backingStore.put(appealIdentifier.toString(), appeal);
    }

    public boolean has(Identifier identifier) {
        boolean result =  backingStore.containsKey(identifier.toString());
        return result;
    }

    public void remove(Identifier identifier) {
        backingStore.remove(identifier.toString());
    }
    
    public boolean appealPlaced(Identifier identifier) {
        return AppealRepository.current().has(identifier);
    }
    
    public boolean appealNotPlaced(Identifier identifier) {
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
        backingStore = new HashMap<String, Appeal>();
    }

    public int size() {
        return backingStore.size();
    }
}
