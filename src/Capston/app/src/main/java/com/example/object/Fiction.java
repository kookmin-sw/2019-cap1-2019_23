package com.example.object;

public class Fiction {

    private String author;
    private String fictionTitle;
    private String fictionCategory;
    private String fictionCreationdate;
    private String fictionImgCoverPath;
    private int fictionLikeCount;


    //빈생성자.
    public Fiction() {
    }
    //풀로찬 생성자.

    public Fiction(String author, String fictionTitle, String fictionCategory, String fictionCreationdate, String fictionImgCoverPath,int fictionLikeCount)
    {   this.author = author;
        this.fictionTitle = fictionTitle;
        this.fictionCategory = fictionCategory;
        this.fictionCreationdate = fictionCreationdate;
        this.fictionImgCoverPath = fictionImgCoverPath;
        this.fictionLikeCount = fictionLikeCount;
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

    public String getFictionCreationdate() {
        return fictionCreationdate;
    }

    public void setFictionCreationdate(String fictionCreationdate) {
        this.fictionCreationdate = fictionCreationdate;
    }

    public String getFictionImgCoverPath() {
        return fictionImgCoverPath;
    }

    public void setFictionImgCoverPath(String fictionImgCoverPath) {
        this.fictionImgCoverPath = fictionImgCoverPath;
    }

    public int getFictionLikeCount() {
        return fictionLikeCount;
    }

    public void setFictionLikeCount(int fictionLikeCount) {
        this.fictionLikeCount = fictionLikeCount;
    }

    @Override
    public String toString() {
        return "Fiction{" +
                "author='" + author + '\'' +
                ", fictionTitle='" + fictionTitle + '\'' +
                ", fictionCategory='" + fictionCategory + '\'' +
                ", fictionCreationdate='" + fictionCreationdate + '\'' +
                ", fictionImgCoverPath='" + fictionImgCoverPath + '\'' +
                ", fictionLikeCount=" + fictionLikeCount +
                '}';
    }
}
