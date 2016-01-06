package edu.baike;

import java.util.ArrayList;

/**
 * Created by youngsu on 16-1-6.
 */
public class Yago {
    private ArrayList<String> type,result,wikiInfo;

    public Yago() {
    }

    public ArrayList<String> getType() {
        return type;
    }

    public void setType(ArrayList<String> type) {
        this.type = type;
    }

    public ArrayList<String> getResult() {
        return result;
    }

    public void setResult(ArrayList<String> result) {
        this.result = result;
    }

    public ArrayList<String> getWikiInfo() {
        return wikiInfo;
    }

    public void setWikiInfo(ArrayList<String> wikiInfo) {
        this.wikiInfo = wikiInfo;
    }
}
