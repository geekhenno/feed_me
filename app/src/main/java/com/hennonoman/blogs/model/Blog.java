package com.hennonoman.blogs.model;

/**
 * Created by paulodichone on 4/17/17.
 */

public class Blog {

    private String desc;
    private String image;
    private String timestamp;
    private String nickname;
    private String profileImg;
    private String userid;
    private String liksCounter;
    private String postID;
    private String email;

    public Blog() {
    }

    public Blog(String title, String desc, String image, String timestamp, String nickname,String profileImg) {

        this.desc = desc;
        this.image = image;
        this.timestamp = timestamp;
        this.nickname = nickname;
        this.profileImg=profileImg;

    }

    public String getEmail() {
        return email;
    }




    public void setEmail(String email) {
        this.email = email;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getLiksCounter() {
        return liksCounter;
    }

    public void setLiksCounter(String liksCounter) {
        this.liksCounter = liksCounter;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getProfileImg() {
        return profileImg;
    }


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
