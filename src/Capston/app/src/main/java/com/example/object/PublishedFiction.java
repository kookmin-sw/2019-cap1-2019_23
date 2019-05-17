package com.example.object;

public class PublishedFiction {

    private String author;
    private String fictionTitle;
    private String fictionCategory;

    private String fictionImgCoverPath;
    private String fictionLastChapter;

    private String fictionLikeCount;
    private boolean isUserLike;

    private boolean isSubscribe;


    public PublishedFiction() {
    }

    public PublishedFiction(String author, String fictionTitle, String fictionCategory,  String fictionImgCoverPath, String fictionLastChapter, String fictionLikeCount, boolean isUserLike, boolean isSubscribe) {
        this.author = author;
        this.fictionTitle = fictionTitle;
        this.fictionCategory = fictionCategory;
        this.fictionImgCoverPath = fictionImgCoverPath;
        this.fictionLastChapter = fictionLastChapter;
        this.fictionLikeCount = fictionLikeCount;
        this.isUserLike = isUserLike;
        this.isSubscribe = isSubscribe;
    }


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getFictionTitle() {
        return fictionTitle;
    }

    public void setFictionTitle(String fictionTitle) {
        this.fictionTitle = fictionTitle;
    }

    public String getFictionCategory() {
        return fictionCategory;
    }

    public void setFictionCategory(String fictionCategory) {
        this.fictionCategory = fictionCategory;
    }

    public String getFictionImgCoverPath() {
        return fictionImgCoverPath;
    }

    public void setFictionImgCoverPath(String fictionImgCoverPath) {
        this.fictionImgCoverPath = fictionImgCoverPath;
    }

    public String getFictionLastChapter() {
        return fictionLastChapter;
    }

    public void setFictionLastChapter(String fictionLastChapter) {
        this.fictionLastChapter = fictionLastChapter;
    }

    public String getFictionLikeCount() {
        return fictionLikeCount;
    }

    public void setFictionLikeCount(String fictionLikeCount) {
        this.fictionLikeCount = fictionLikeCount;
    }

    public boolean isUserLike() {
        return isUserLike;
    }

    public void setUserLike(boolean userLike) {
        isUserLike = userLike;
    }

    public boolean isSubscribe() {
        return isSubscribe;
    }

    public void setSubscribe(boolean subscribe) {
        isSubscribe = subscribe;
    }
}
