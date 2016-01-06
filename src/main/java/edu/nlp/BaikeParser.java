package edu.nlp;

import edu.Constant;
import edu.Language;
import edu.baike.Baike;
import edu.baike.EnBaike;
import edu.baike.KCLpair;
import edu.baike.ZhBaike;
import edu.util.Contant;
import edu.util.opMysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Created by youngsu on 15-12-29.
 * 用来生成zhBaike和enBaike实体
 */
public class BaikeParser {
    private Baike baike = new Baike();
    private String title;
    private String tag;
    private KCLDir pageDir = new KCLDir();
    private KCLDir catDir = new KCLDir();
    private Connection conn;

    public BaikeParser(String title, KCLDir pageDir, KCLDir catDir, String tag) throws SQLException {
        this.title = title;
        this.tag = tag;
        this.pageDir = pageDir;
        this.catDir = catDir;
        init();
    }

    public Baike getBaike() {
        return baike;
    }

    private void init() throws SQLException {
        if (tag.equals(Language.CHINESE)) {
            conn = opMysql.connSQL(Contant.zhmysqlurl);
        } else if (tag.equals(Language.ENGLISH)) {
            conn = opMysql.connSQL(Contant.enmysqlurl);
        } else {
            System.out.println("标签不符合要求！");
            System.exit(0);
        }
        System.out.println(title);
        baike.setTitle(title);
        baike.setText(findText(baike.getTitle(), tag));
//        baike.setInlinks(findInlinks(baike.getTitle(),tag));
//        baike.setOutlinks(findOutlinks(baike.getTitle(),tag));
//        baike.setCategories(findCategory(baike.getTitle(),tag));
        baike.setKCLin(findKCLin(baike.getTitle(), pageDir, tag));
        baike.setKCLout(findKCLout(baike.getTitle(), pageDir, tag));
        baike.setKCLcat(findKCLcat(baike.getTitle(), catDir, tag));
        conn.close();
    }

    private List<KCLpair> findKCLcat(String title, KCLDir dir, String tag) throws SQLException {
        List<KCLpair> cat = new ArrayList<KCLpair>();

        List<String> allCat = findCategory(title, tag);
        baike.setCategories(allCat);
        if (tag.equals(Language.ENGLISH)) {
            cat = doKCLcatEn(allCat, dir);
        } else if (tag.equals(Language.CHINESE)) {
            cat = doKCLcatZh(allCat, dir);
        } else {

        }

        return cat;
    }

    private List<KCLpair> doKCLcatZh(List<String> allCat, KCLDir dir) {
        List<KCLpair> pairs = new ArrayList<KCLpair>();
        Collection<String> zhCats = dir.getZhSet();
        for (String cat : allCat) {
            if (zhCats.contains(cat)) {
                KCLpair pair = new KCLpair();
                String enName = dir.getEnByZh(cat);
                pair.setEnname(enName);
                pair.setZhname(cat);
                pairs.add(pair);
            }
        }
        return pairs;
    }

    private List<KCLpair> doKCLcatEn(List<String> allCat, KCLDir dir) {
        List<KCLpair> pairs = new ArrayList<KCLpair>();
        HashSet<String> enCats = dir.getEnSet();
        for (String cat : allCat) {
            if (enCats.contains(cat)) {
                KCLpair pair = new KCLpair();
                String zhName = dir.getZhByEn(cat);
                pair.setZhname(zhName);
                pair.setEnname(cat);
                pairs.add(pair);
            }
        }
        return pairs;
    }

    private List<KCLpair> findKCLout(String title, KCLDir dir, String tag) throws SQLException {
        List<KCLpair> out = new ArrayList<KCLpair>();
        List<String> allOut = findOutlinks(title, tag);
        baike.setOutlinks(allOut);
        if (tag.equals(Language.ENGLISH)) {
            out = doKCLoutEn(allOut, dir);

        } else if (tag.equals(Language.CHINESE)) {
            out = doKCLoutZn(allOut, dir);

        } else {

        }
        return out;
    }

    private List<KCLpair> doKCLoutZn(List<String> allOut, KCLDir dir) {
        List<KCLpair> pairs = new ArrayList<KCLpair>();
        Collection<String> zhOuts = dir.getZhSet();
        for (String out : allOut) {
            if (zhOuts.contains(out)) {
                KCLpair pair = new KCLpair();
                String enName = dir.getEnByZh(out);
                pair.setEnname(enName);
                pair.setZhname(out);
                pairs.add(pair);
            }
        }
        return pairs;
    }

    private List<KCLpair> doKCLoutEn(List<String> allOut, KCLDir dir) {
        List<KCLpair> pairs = new ArrayList<KCLpair>();
        HashSet<String> enOuts = dir.getEnSet();
        for (String out : allOut) {
            if (enOuts.contains(out)) {
                KCLpair pair = new KCLpair();
                String zhName = dir.getZhByEn(out);
                pair.setZhname(zhName);
                pair.setEnname(out);
                pairs.add(pair);
            }
        }
        return pairs;
    }

    private List<KCLpair> findKCLin(String title, KCLDir dir, String tag) throws SQLException {
        List<KCLpair> in = new ArrayList<KCLpair>();
        List<String> allIn = findInlinks(title, tag);
        baike.setInlinks(allIn);
        if (tag.equals(Language.ENGLISH)) {

            in = doKCLinEn(allIn, dir);
        } else if (tag.equals(Language.CHINESE)) {

            in = doKCLinZh(allIn, dir);
        } else {

        }
        return in;
    }

    private List<KCLpair> doKCLinZh(List<String> allIn, KCLDir dir) {
        List<KCLpair> pairs = new ArrayList<KCLpair>();
        Collection<String> zhIns = dir.getZhSet();
        for (String in : allIn) {
            if (zhIns.contains(in)) {
                KCLpair pair = new KCLpair();
                String enName = dir.getEnByZh(in);
                pair.setEnname(enName);
                pair.setZhname(in);
                pairs.add(pair);
            }
        }
        return pairs;
    }

    private List<KCLpair> doKCLinEn(List<String> allIn, KCLDir dir) {
        List<KCLpair> pairs = new ArrayList<KCLpair>();
        HashSet<String> enIns = dir.getEnSet();
        for (String in : allIn) {
            if (enIns.contains(in)) {
                KCLpair pair = new KCLpair();
                String zhName = dir.getZhByEn(in);
                pair.setZhname(zhName);
                pair.setEnname(in);
                pairs.add(pair);
            }
        }
        return pairs;
    }


    private List<String> findCategory(String title, String tag) throws SQLException {
        List<String> cat = new ArrayList<String>();
        if (tag.equals(Language.ENGLISH)) {
            cat = doCategoryEn(title);

        } else if (tag.equals(Language.CHINESE)) {
            cat = doCategoryZh(title);

        } else {

        }
        return cat;
    }

    private List<String> doCategoryZh(String title) throws SQLException {
        List<String> res = new ArrayList<String>();
        String selectCountSql = "select b.name from (select c.id from zhwiki.category_pages as c left join zhwiki.pagemapline as d on c.pages=d.id where name = '" + title + "') as a,zhwiki.category as b where a.id=b.id;";
        ResultSet resultSet = opMysql.selectSQL(conn, selectCountSql);
        while (resultSet.next()) {
            String catName = resultSet.getString("name");
            res.add(catName);
        }
        return res;
    }

    /**
     * 做获得所有category的工作。同时，在doKCLcategory时，设置category的个数，即只查询一次
     *
     * @param title
     */
    private List<String> doCategoryEn(String title) throws SQLException {
        List<String> res = new ArrayList<String>();
        if (!title.contains("'")) {
            String categorySelectsql = "SELECT * FROM enwiki.category_page_names where pageName = '" + title + "';";
            ResultSet resultSet = opMysql.selectSQL(conn, categorySelectsql);
            while (resultSet.next()) {
                String catName = resultSet.getString("categoryName");
                res.add(catName);
            }
        }

        return res;
    }

    private List<String> findOutlinks(String title, String tag) throws SQLException {
        List<String> out = new ArrayList<String>();
        if (tag.equals(Language.ENGLISH)) {
            out = doOutlinksEn(title);

        } else if (tag.equals(Language.CHINESE)) {

            out = doOutlinksZh(title);
        } else {

        }
        return out;
    }

    private List<String> doOutlinksZh(String title) throws SQLException {
        List<String> out = new ArrayList<String>();
        String selectCountSql = "select name from (SELECT b.outLinks FROM zhwiki.pagemapline as a,zhwiki.page_outlinks as b where a.name='" + title + "' and a.id=b.id) as c,zhwiki.pagemapline as d where c.outLinks=d.id;";
        ResultSet resultSet = opMysql.selectSQL(conn, selectCountSql);
        while (resultSet.next()) {
            String catName = resultSet.getString("name");
            out.add(catName);
        }
        return out;
    }

    private List<String> doOutlinksEn(String title) throws SQLException {
        List<String> out = new ArrayList<String>();
        if (!title.contains("'")) {
            String inlinkSelectsql = "SELECT * FROM enwiki.pagenames_inlinknames where inlinkName = '" + title + "';";
            ResultSet resultSet = opMysql.selectSQL(conn, inlinkSelectsql);
            while (resultSet.next()) {
                String catName = resultSet.getString("pageName");
                out.add(catName);
            }
        }

        return out;
    }

    private List<String> findInlinks(String title, String tag) throws SQLException {
        List<String> in = new ArrayList<String>();
        if (tag.equals(Language.ENGLISH)) {
//            in = doInlinksEn(title);
            in = new ArrayList<String>();

        } else if (tag.equals(Language.CHINESE)) {
            in = doInlinksZh(title);

        } else {

        }

        return in;
    }

    private List<String> doInlinksZh(String title) throws SQLException {
        List<String> in = new ArrayList<String>();
        String selectCountSql = "select name from (SELECT b.inLinks FROM zhwiki.pagemapline as a,zhwiki.page_inlinks as b where a.name='" + title + "' and a.id=b.id) as c,zhwiki.pagemapline as d where c.inLinks=d.id;";
        ResultSet resultSet = opMysql.selectSQL(conn, selectCountSql);
        while (resultSet.next()) {
            String pageName = resultSet.getString("name");
            in.add(pageName);
        }
        return in;
    }

    private List<String> doInlinksEn(String title) throws SQLException {
        List<String> in = new ArrayList<String>();
        if (!title.contains("'")) {
            String inlinkSelectsql = "SELECT * FROM enwiki.pagenames_inlinknames where pageName = '" + title + "';";
            ResultSet resultSet = opMysql.selectSQL(conn, inlinkSelectsql);
            while (resultSet.next()) {
                String pageName = resultSet.getString("inlinkName");
                in.add(pageName);
            }
        }

        return in;
    }


    private String findText(String title, String tag) throws SQLException {
        String text = "";
        if (tag.equals(Language.ENGLISH)) {
            text = doTextEn(title);

        } else if (tag.equals(Language.CHINESE)) {

            text = doTextZh(title);
        } else {

        }
        return text;
    }

    private String doTextZh(String title) throws SQLException {
        String selectSql = "SELECT b.text FROM zhwiki.pagemapline as a,zhwiki.page as b where a.name='" + title + "' and a.pageId=b.id;";
        ResultSet resultSet = opMysql.selectSQL(conn, selectSql);
        String text = "";
        while (resultSet.next()) {
            text = resultSet.getString("text");
        }
        return text;
    }

    private String doTextEn(String title) throws SQLException {
        String texten = "";
        if (!title.contains("'")) {
            String sqlen = "SELECT * FROM enwiki.pagemapline as a,enwiki.page as b where a.name='" + title + "' and a.pageId=b.id;";

            ResultSet resultSeten = opMysql.selectSQL(conn, sqlen);
            while (resultSeten.next()) {
                texten = resultSeten.getString("text");
            }
        }

        return texten;
    }


}
