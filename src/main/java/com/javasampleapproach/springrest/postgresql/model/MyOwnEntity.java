package com.javasampleapproach.springrest.postgresql.model;

public class MyOwnEntity {
    private String name;
    private String email;
    private String link;
    private String linkToPerson;

    public MyOwnEntity(String name, String email, String link, String linkToPerson){
        this.name = name;
        this.email = email;
        this.link = link;
        this.linkToPerson = linkToPerson;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getLink() {
        return link;
    }

    public String getLinkToPerson() {
        return linkToPerson;
    }

}
