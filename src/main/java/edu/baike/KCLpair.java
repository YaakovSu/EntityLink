package edu.baike;

/**
 * Created by youngsu on 15-12-29.
 * KCL实体，包括zhname，enName
 */
public class KCLpair {
    private String zhname, enname;

    public KCLpair(String zhname, String enname) {
        this.zhname = zhname;
        this.enname = enname;
    }

    public KCLpair() {
    }

    public String getZhname() {
        return zhname;
    }

    public void setZhname(String zhname) {
        this.zhname = zhname;
    }

    public String getEnname() {
        return enname;
    }

    public void setEnname(String enname) {
        this.enname = enname;
    }
}
