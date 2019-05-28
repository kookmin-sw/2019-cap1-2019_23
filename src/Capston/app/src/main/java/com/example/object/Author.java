package com.example.object;

import com.google.firebase.firestore.CollectionReference;

public class Author {

    private String author;
    private String authorNickName;
    private String userProfileImgPath;
    private CollectionReference myworkspace;

    public Author() {
    }
    public Author( String author,String authorNickName,String userProfileImgPath) {
        this.author = author;
        this.authorNickName = authorNickName;
        this.userProfileImgPath = userProfileImgPath;
    }
    public Author(String author, String authorNickName, String userProfileImgPath, CollectionReference myworkspace) {
        this.author = author;
        this.authorNickName = authorNickName;
        this.userProfileImgPath = userProfileImgPath;
        this.myworkspace = myworkspace;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorNickName() {
        return authorNickName;
    }

    public void setAuthorNickName(String authorNickName) {
        this.authorNickName = authorNickName;
    }

    public String getUserProfileImgPath() {
        return userProfileImgPath;
    }

    public void setUserProfileImgPath(String userProfileImgPath) {
        this.userProfileImgPath = userProfileImgPath;
    }

    public CollectionReference getMyworkspace() {
        return myworkspace;
    }

    public void setMyworkspace(CollectionReference myworkspace) {
        this.myworkspace = myworkspace;
    }

}
