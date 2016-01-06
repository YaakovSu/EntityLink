package edu.baike;

import org.ansj.app.keyword.Keyword;

import java.util.ArrayList;

/**
 * Created by youngsu on 14-10-30.
 */
public class PageKeyWord {
    private String title;
    private ArrayList<Keyword> keywords = new ArrayList<Keyword>();

    public PageKeyWord(String title, ArrayList<Keyword> keywords) {
        this.title = title;
        this.keywords = keywords;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<Keyword> getKeywords() {
        return keywords;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setKeywords(ArrayList<Keyword> keywords) {
        this.keywords = keywords;
    }
}
