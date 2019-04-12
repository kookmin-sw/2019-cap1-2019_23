package com.example.object;

public class Chapter {

    private String author;
    private String chapterNumber;
    private String lastModifiedDate;
    private int grade;
    private String contents;
    private String imgUri;


    public Chapter() {
    }

    public Chapter(String author, String chapterNumber, String lastModifiedDate, int grade, String contents, String imgUri) {
        this.author = author;
        this.chapterNumber = chapterNumber;
        this.lastModifiedDate = lastModifiedDate;
        this.grade = grade;
        this.contents = contents;
        this.imgUri = imgUri;
    }

    @Override
    public String toString() {
        return "Chapter{" +
                "author='" + author + '\'' +
                ", chapterNumber='" + chapterNumber + '\'' +
                ", lastModifiedDate='" + lastModifiedDate + '\'' +
                ", grade=" + grade +
                ", contents='" + contents + '\'' +
                ", imgUri='" + imgUri + '\'' +
                '}';
    }
}
