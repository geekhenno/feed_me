package com.hennonoman.blogs.model;

public class User {


    String likePosts;
    String userID;

    public User()
    {

    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getLikePosts() {
        return likePosts;
    }

    public void setLikePosts(String likePosts) {
        this.likePosts = likePosts;
    }
}
