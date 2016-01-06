package lfg;

import edu.util.Contant;
import edu.util.Myutil;
import edu.util.opMysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by CBD_O9 on 2015-04-18.
 */
public class CalNodeFunctions {

    static String zhInlinkFilePath = "C:\\youngsu\\lijuanziData\\allzhEdge_inlink.tsv";
    static String zhOutlinkFilePath = "C:\\youngsu\\lijuanziData\\allzhEdge_outlink.tsv";
    static String enInlinkFilePath = "C:\\youngsu\\lijuanziData\\allenEdge_inlink.tsv";
    static String enOutlinkFilePath = "C:\\youngsu\\lijuanziData\\allenEdge_outlink.tsv";
    static String MDic = "C:\\youngsu\\lijuanziData\\allData.tsv";

    public static void main(String[] args) throws SQLException {
        String zh = "老舍";
        String en = "Lao_She";
        HashMap<String, ArrayList<String>> zhInlinkMap = getlinkFile(zhInlinkFilePath);
        HashMap<String, ArrayList<String>> enInlinkMap = getlinkFile(enInlinkFilePath);

        HashMap<String, ArrayList<String>> zhOutlinkMap = getlinkFile(zhOutlinkFilePath);
        HashMap<String, ArrayList<String>> enOutlinkMap = getlinkFile(enOutlinkFilePath);

        HashMap<String, String> myMDic = getMDic_allData(MDic);


        double cateGoryRate = getCategoryFunction(zh, en);
        double inRate = getlinkFunction(zh, en, zhInlinkMap, enInlinkMap, myMDic);
        double outRate = getlinkFunction(zh, en, zhOutlinkMap, enOutlinkMap, myMDic);

        System.out.println(cateGoryRate);
        System.out.println(inRate);
        System.out.println(outRate);

    }

    public static HashMap<String, String> getMDic_allData(String mDic) {
        HashMap<String, String> result = new HashMap<String, String>();
        ArrayList<String> text = Myutil.readByLine(mDic);
        for (String line : text) {
            String[] words = line.split("\t");
            String enName = words[1];
            String zhName = words[0];

            result.put(zhName, enName);
        }
        return result;
    }


    public static double getlinkFunction(String zhName, String enName, HashMap<String, ArrayList<String>> zhlinkMap, HashMap<String, ArrayList<String>> enlinkMap, HashMap<String, String> myMDic) {

        ArrayList<String> zhlinks = zhlinkMap.get(zhName);
        ArrayList<String> enlinks = enlinkMap.get(enName);
        double count = 0;
        for (String zhlink : zhlinks) {
            String enlin_zh = myMDic.get(zhlink);
            if (enlinks.contains(enlin_zh)) {
                count++;
            }
        }

        double enSize = enlinks.size();
        if (enSize == 0) {
            enSize = 0.0001;
        }
        double zhSize = zhlinks.size();
        if (zhSize == 0) {
            zhSize = 0.0001;
        }

        double rate = 2 * count / (enSize + zhSize);

        return rate;

    }


    public static double getCategoryFunction(String zhName, String enName) throws SQLException {

        ArrayList<String> ZhCategoryName = getZhCategoryName(zhName);

//        System.out.println(En_MDIc_ZhCategoryName);
        ArrayList<String> enName_enCategorys = getEnCategoryName(enName);
//        System.out.println(enName_enCategorys);
//        System.exit(0);

        ArrayList<String> En_MDIc_ZhCategoryName = getEnCategorys(ZhCategoryName);
        double count = 0;
        for (String en : enName_enCategorys) {
            if (En_MDIc_ZhCategoryName.contains(en)) {
                count++;
            }
        }


        double enSize = enName_enCategorys.size();
        double zhSize = ZhCategoryName.size();
        if (enSize == 0) {
            enSize = 0.0001;
        }

        double cat_rate = 2 * count / (enSize + zhSize);
        return cat_rate;
    }

    public static ArrayList<String> getEnCategorys(ArrayList<String> allZhCategoryName) {
        ArrayList<String> result = new ArrayList<String>();
        ArrayList<String> MDic = Myutil.readByLine(Contant.MDicCategoryInputPath);

        for (String line : MDic) {
            String[] titles = line.split("=");
            String en_title = titles[0];
            String zh_title = titles[1];
            if (!en_title.contains("\'")) {
                if (allZhCategoryName.contains(zh_title)) {
                    result.add(en_title);
                }
            }


        }
        return result;
    }

    public static ArrayList<String> getZhCategoryName(String zhWikiTitle) throws SQLException {
        Connection conn = opMysql.connSQL(Contant.zhmysqlurl);
        ArrayList<String> result = new ArrayList<String>();

        String selectZhCategory = "SELECT a.name FROM category as a,(select b.id,c.name from category_pages as b,pagemapline as c where b.pages=c.id and c.name='" + zhWikiTitle + "') as d where a.id=d.id;";

// System.out.println(selectZhOutlink);
        ResultSet resultSet = opMysql.selectSQL(conn, selectZhCategory);
        while (resultSet.next()) {

            String oneCategory = resultSet.getString("name");
//            System.out.println("嘎嘎个 "+oneOutlink);
            result.add(oneCategory);
        }
        opMysql.deconnSQL(conn);
        return result;
    }


    public static HashMap<String, ArrayList<String>> getlinkFile(String InlinkFilePath) {
        HashMap<String, ArrayList<String>> result = new HashMap<String, ArrayList<String>>();
        ArrayList<String> inlinks = Myutil.readByLine(InlinkFilePath);
        for (String line : inlinks) {
            ArrayList<String> values = new ArrayList<String>();
            String[] words = line.split("\t");
            String key = words[0];
            if (words.length > 1) {
                for (int i = 1; i < words.length; i++) {
                    values.add(words[i]);
                }
            }
            result.put(key, values);

        }
        return result;
    }

    public static int getZhCategoryCount(String zhWikiTitle) throws SQLException {
        Connection conn = opMysql.connSQL(Contant.zhmysqlurl);
        String selectCountSql = "SELECT count(*) FROM zhwiki.category_pages as a,zhwiki.pagemapline as b where a.pages=b.id and b.name='" + zhWikiTitle + "';";
        ResultSet resultSet = opMysql.selectSQL(conn, selectCountSql);
        int count = 0;
        while (resultSet.next()) {
            count = resultSet.getInt(1);
//            System.out.println("gaca"+count);
        }
        conn.close();
        return count;

    }

    public static ArrayList<String> getEnCategoryName(String enWikiTitle) throws SQLException {
        Connection conn = opMysql.connSQL(Contant.enmysqlurl);
        ArrayList<String> result = new ArrayList<String>();

//        String selectZhCategory = "SELECT a.name FROM category as a,(select b.id,c.name from category_pages as b,pagemapline as c where b.pages=c.id and c.name='" + enWikiTitle + "') as d where a.id=d.id;";

        if (!enWikiTitle.contains("'")) {
            String selectEnCategory = "SELECT categoryName FROM enwiki.category_page_names where pageName = '" + enWikiTitle + "';";

// System.out.println(selectZhOutlink);
            ResultSet resultSet = opMysql.selectSQL(conn, selectEnCategory);
            while (resultSet.next()) {

                String oneCategory = resultSet.getString("categoryName");
//            System.out.println("嘎嘎个 "+oneOutlink);
                result.add(oneCategory);
            }
        }

        opMysql.deconnSQL(conn);
        return result;
    }
}
