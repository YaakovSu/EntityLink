package edu.baike;

import edu.nlp.BaikeParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by youngsu on 15-12-29.
 */
public class Baike {
    private String title, text;
    private List<String> inlinks = new ArrayList<String>(), outlinks = new ArrayList<String>(), categories = new ArrayList<String>();
    private List<KCLpair> KCLin = new ArrayList<KCLpair>(), KCLout = new ArrayList<KCLpair>(), KCLcat = new ArrayList<KCLpair>();

//    public Baike(String title) {
//        this.title = title;
//    }


    public Baike() {
    }

    public Baike(BaikeParser parser) {
        this.title = parser.getBaike().title;
        this.text = parser.getBaike().text;
        this.inlinks = parser.getBaike().getInlinks();
        this.outlinks = parser.getBaike().getOutlinks();
        this.categories = parser.getBaike().getCategories();
        this.KCLin = parser.getBaike().getKCLin();
        this.KCLout = parser.getBaike().getKCLout();
        this.KCLcat = parser.getBaike().getKCLcat();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getInlinks() {
        return inlinks;
    }

    public void setInlinks(List<String> inlinks) {
        this.inlinks = inlinks;
    }

    public List<String> getOutlinks() {
        return outlinks;
    }

    public void setOutlinks(List<String> outlinks) {
        this.outlinks = outlinks;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<KCLpair> getKCLin() {
        return KCLin;
    }

    public void setKCLin(List<KCLpair> KCLin) {
        this.KCLin = KCLin;
    }

    public List<KCLpair> getKCLout() {
        return KCLout;
    }

    public void setKCLout(List<KCLpair> KCLout) {
        this.KCLout = KCLout;
    }

    public List<KCLpair> getKCLcat() {
        return KCLcat;
    }

    public void setKCLcat(List<KCLpair> KCLcat) {
        this.KCLcat = KCLcat;
    }
}
