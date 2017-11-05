package com.appdoptame.appdoptame.model;

/**
 * Created by Camilo on 29/09/2017.
 */

public class Notification {

    private String user;
    private Profile post;
    private boolean type;
    private boolean answered;

    public Notification(String user, Profile post, boolean type, boolean answered) {
        this.user = user;
        this.post = post;
        this.type = type;
        this.answered = answered;
    }

    public Notification() {}

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Profile getPost() {
        return post;
    }

    public void setPost(Profile post) {
        this.post = post;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    @Override
    public String toString() {
        return "Has enviado solicitud de adopción a " + post.getName();
    }
}
