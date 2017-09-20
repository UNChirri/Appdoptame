package com.appdoptame.appdoptame;

/**
 * Created by jufarangoma on 19/09/17.
 */


public  class Post{

    private String id;
    private String user;
    private String description;
    private String genre;
    private String age;
    private String name;
    private String photoUrl;
    private String location;

    public Post(String user, String description, String genre, String age, String name, String photoUrl) {
        this.user = user;
        this.description = description;
        this.genre = genre;
        this.age = age;
        this.name = name;
        this.photoUrl = photoUrl;
    }

    public Post(String id, String user, String description, String genre, String age, String photoUrl, String location, String name) {
        this.id = id;
        this.user = user;
        this.description = description;
        this.genre = genre;
        this.age = age;
        this.photoUrl = photoUrl;
        this.location = location;
        this.name = name;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
