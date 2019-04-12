package com.example.object;

public class Fiction {

    private String author;
    private String fictionTitle;
    private String fictionCategory;
    private String fictionCreationdate;
    private String fictionImgCoverPath;


    //빈생성자.
    public Fiction() {
    }
    //풀로찬 생성자.

    public Fiction(String author, String fictionTitle, String fictionCategory, String fictionCreationdate, String fictionImgCoverPath) {
        this.author = author;
        this.fictionTitle = fictionTitle;
        this.fictionCategory = fictionCategory;
        this.fictionCreationdate = fictionCreationdate;
        this.fictionImgCoverPath = fictionImgCoverPath;
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

    @Override
    public String toString() {
        return "Fiction{" +
                "author='" + author + '\'' +
                ", fictionTitle='" + fictionTitle + '\'' +
                ", fictionCategory='" + fictionCategory + '\'' +
                ", fictionCreationdate='" + fictionCreationdate + '\'' +
                ", fictionImgCoverPath='" + fictionImgCoverPath + '\'' +
                '}';
    }
}
