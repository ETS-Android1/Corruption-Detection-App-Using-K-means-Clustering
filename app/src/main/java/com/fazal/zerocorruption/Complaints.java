package com.fazal.zerocorruption;

public class Complaints {
    private String name,description,evidence;

    public Complaints(){

    }

    public Complaints(String name, String description, String evidence) {
        this.name = name;
        this.description = description;
        this.evidence = evidence;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEvidence() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }
}
