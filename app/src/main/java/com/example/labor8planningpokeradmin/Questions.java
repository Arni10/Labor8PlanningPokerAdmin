package com.example.labor8planningpokeradmin;

public class Questions {
    private String name;
    private String groupKey;
    private boolean isAvailable;

    public Questions() {

    }

    public Questions(String name, String groupKey, boolean isAvailable) {
        this.name = name;
        this.groupKey = groupKey;
        this.isAvailable = isAvailable;
    }

    public String getName() {
        return name;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}