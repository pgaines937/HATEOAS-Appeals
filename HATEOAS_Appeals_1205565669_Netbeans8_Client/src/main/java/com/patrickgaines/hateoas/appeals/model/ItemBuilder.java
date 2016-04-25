package com.patrickgaines.hateoas.appeals.model;

import java.util.Random;


public class ItemBuilder {
    public static ItemBuilder item() {
        return new ItemBuilder();
    }

    private int points = 5;    
    private String reason = "reason for appeal";
    
    public Item build() {
        return new Item(reason, points);
    }
    
    public ItemBuilder withPoints(int points) {
        this.points  = points;
        return this;
    }
    
    public ItemBuilder withReason(String reason) {
        this.reason = reason;
        return this;
    }

    public ItemBuilder random() {
        Random r = new Random();
        points = r.nextInt(5);
        reason = "this is the reason";

        return this;
    }
}
