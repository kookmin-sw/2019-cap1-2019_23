package com.example.object;

import java.util.Date;

public class Chapter {



    private String authorAccount;
    private String fictionTitle;
    private String chapterNumber;
    private String chapterTitle;
    private String chapterContents;
    private Date chapterFinalModifieddate;

    public Chapter() {
    }


    public Chapter(String chapterNumber, String chapterTitle, String authorAccount,String fictionTitle) {
        this.chapterNumber = chapterNumber;
        this.chapterTitle = chapterTitle;
        this.authorAccount = authorAccount;
        this.fictionTitle = fictionTitle;
    }


    public Chapter(String chapterNumber, String chapterTitle) {
        this.chapterNumber = chapterNumber;
        this.chapterTitle = chapterTitle;


    }

    public Chapter(String fictionTitle, String chapterNumber, String chapterTitle, String chapterContents, Date chapterFinalModifieddate) {
        this.fictionTitle = fictionTitle;
        this.chapterNumber = chapterNumber;
        this.chapterTitle = chapterTitle;
        this.chapterContents = chapterContents;
        this.chapterFinalModifieddate = chapterFinalModifieddate;
    }


    public String getAuthorAccount() {
        return authorAccount;
    }

    public void setAuthorAccount(String authorAccount) {
        this.authorAccount = authorAccount;
    }

    public String getFictionTitle() {
        return fictionTitle;
    }

    public void setFictionTitle(String fictionTitle) {
        this.fictionTitle = fictionTitle;
    }

    public String getChapterNumber() {
        return chapterNumber;
    }

    public void setChapterNumber(String chapterNumber) {
        this.chapterNumber = chapterNumber;
    }

    public String getChapterTitle() {
        return chapterTitle;
    }

    public void setChapterTitle(String chapterTitle) {
        this.chapterTitle = chapterTitle;
    }

    public String getChapterContents() {
        return chapterContents;
    }

    public void setChapterContents(String chapterContents) {
        this.chapterContents = chapterContents;
    }

    public Date getChapterFinalModifieddate() {
        return chapterFinalModifieddate;
    }

    public void setChapterFinalModifieddate(Date chapterFinalModifieddate) {
        this.chapterFinalModifieddate = chapterFinalModifieddate;
    }

    @Override
    public String toString() {
        return "Chapter{" +
                "fictionTitle='" + fictionTitle + '\'' +
                ", chapterNumber='" + chapterNumber + '\'' +
                ", chapterTitle='" + chapterTitle + '\'' +
                ", chapterContents='" + chapterContents + '\'' +
                ", chapterFinalModifieddate=" + chapterFinalModifieddate +
                '}';
    }
}
