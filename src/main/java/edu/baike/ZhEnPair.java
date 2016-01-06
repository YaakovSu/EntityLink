package edu.baike;

import edu.util.Contant;
import edu.util.Myutil;
import edu.util.opMysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by CBD_O9 on 2014-11-17.
 */
public class ZhEnPair {
    private String zhName;
    private String enName;
    private int commonInlink;
    private int commonOutlink;
    private int commonCategory;
    private HashSet<String> En_MDIc_ZhPageName;
    private HashSet<String> En_MDIc_ZhCategoryName;
    private Connection zhconn;
    private Connection enconn;

    public ZhEnPair(String zhName, String enName, HashSet<String> En_MDIc_ZhPageName, HashSet<String> En_MDIc_ZhCategoryName, Connection zhconn, Connection enconn) {
        this.zhName = zhName;
        this.enName = enName;
        this.En_MDIc_ZhPageName = En_MDIc_ZhPageName;
        this.En_MDIc_ZhCategoryName = En_MDIc_ZhCategoryName;
        this.zhconn = zhconn;
        this.enconn = enconn;

    }


    public int getEnCategoryCount(String pageName) throws SQLException {

        int count = 0;
        if (!pageName.contains("\'")) {
            String categorySelectsql = "SELECT count(*) FROM enwiki.category_page_names where pageName = '" + pageName + "';";
            ResultSet resultSet = opMysql.selectSQL(enconn, categorySelectsql);

            while (resultSet.next()) {
                count = resultSet.getInt(1);
//            System.out.println("en_category"+count);
            }
        }

//        conn.close();
        return count;
    }

    public int getEnOutlinkCount(String pageName) throws SQLException {

        int count = 0;
        if (!pageName.contains("\'")) {
            String inlinkSelectsql = "SELECT count(*) FROM enwiki.pagenames_inlinknames where inlinkName = '" + pageName + "';";
            ResultSet resultSet = opMysql.selectSQL(enconn, inlinkSelectsql);

            while (resultSet.next()) {
                count = resultSet.getInt(1);
//            System.out.println("en_inlink"+count);
            }
        }

//        conn.close();
        return count;
    }


    public int getEnInlinkCount(String pageName) throws SQLException {
        int count = 0;
        if (!pageName.contains("\'")) {
            String inlinkSelectsql = "SELECT count(*) FROM enwiki.pagenames_inlinknames where pageName = '" + pageName + "';";
            ResultSet resultSet = opMysql.selectSQL(enconn, inlinkSelectsql);

            while (resultSet.next()) {
                count = resultSet.getInt(1);

//            System.out.println("en_inlink"+count);
            }
        }

//        conn.close();
        return count;
    }

    public int getCommonInlink() throws SQLException {
        //获取一个中文词条中outlinks中在MDic中能找到的对应英文词条name
//        HashSet<String> En_MDIc_ZhPageName = getEnInLinkPageName(zhName);
        ArrayList<String> enInlinksName = getEnInlinkName(enName);
        int count = compaire(En_MDIc_ZhPageName, enInlinksName);
        return count;
    }

    private int compaire(HashSet<String> en_mdIc_zhPageName, ArrayList<String> enInlinksName) {

        int count = 0;
        for (String en : enInlinksName) {
            if (en_mdIc_zhPageName.contains(en)) {
                count++;
            }
        }
        return count;
    }

    /**
     * 获取一个英文name所有的inlink的名字
     *
     * @param enName
     * @return
     * @throws SQLException
     */
    private ArrayList<String> getEnInlinkName(String enName) throws SQLException {
        ArrayList<String> result = new ArrayList<String>();
        if (!enName.contains("\'")) {
            String selectEnInlink = "SELECT inlinkName FROM enwiki.pagenames_inlinknames where pageName='" + enName + "';";
            ResultSet resultSet = opMysql.selectSQL(enconn, selectEnInlink);
            while (resultSet.next()) {
                String oneinlink = resultSet.getString("inlinkName");
                result.add(oneinlink);
            }
        }
        return result;


    }

    public void setCommonInlink(int commonInlink) {
        this.commonInlink = commonInlink;
    }

    public int getCommonOutlink() throws SQLException {
        //获取一个中文词条中outlinks中在MDic中能找到的对应英文词条name
//        HashSet<String> En_MDIc_ZhPageName = getEnOutLinkPageName(zhName);
        ArrayList<String> enOUtlinksName = getEnOutlinkName(enName);
        int count = compaire(En_MDIc_ZhPageName, enOUtlinksName);

        return count;
    }

    private ArrayList<String> getEnOutlinkName(String enName) throws SQLException {
        ArrayList<String> result = new ArrayList<String>();
        if (!enName.contains("\'")) {
            String selectEnInlink = "SELECT pageName FROM enwiki.pagenames_inlinknames where inlinkName='" + enName + "';";
            ResultSet resultSet = opMysql.selectSQL(enconn, selectEnInlink);
            while (resultSet.next()) {
                String oneoutlink = resultSet.getString("pageName");
                result.add(oneoutlink);
            }
        }
        return result;
    }

    private HashSet<String> getEnOutLinkPageName(String zhName) throws SQLException {
        HashSet<String> allZhOutlinkPageName = getZhOutlinkName(zhName); //获得所有的中文outlink词条名称
        return getEnlinks(allZhOutlinkPageName);
    }

    private HashSet<String> getZhOutlinkName(String zhName) throws SQLException {
        HashSet<String> result = new HashSet<String>();
        if (!zhName.contains("\'")) {
            String selectZhOutlink = "select name from (SELECT b.outLinks FROM zhwiki.pagemapline as a,zhwiki.page_outlinks as b where a.name='" + zhName + "' and a.id=b.id) as c,zhwiki.pagemapline as d where c.outLinks=d.id;";
            ResultSet resultSet = opMysql.selectSQL(zhconn, selectZhOutlink);
            while (resultSet.next()) {
                String oneOutlink = resultSet.getString("name");
                result.add(oneOutlink);
            }
        }
        return result;
    }

    public void setCommonOutlink(int commonOutlink) {
        this.commonOutlink = commonOutlink;
    }


    public int getCommonCategory() throws SQLException {

        //获取一个中文词条中outlinks中在MDic中能找到的对应英文词条name
        ArrayList<String> enInlinksName = getEnCategoryName(enName);
        int count = compaire(En_MDIc_ZhCategoryName, enInlinksName);
        return count;
    }

    private ArrayList<String> getEnCategoryName(String enName) throws SQLException {
        ArrayList<String> result = new ArrayList<String>();

        if (!enName.contains("\'")) {
            String selectEnInlink = "SELECT * FROM enwiki.category_page_names where pageName = '" + enName + "';";
            ResultSet resultSet = opMysql.selectSQL(enconn, selectEnInlink);
            while (resultSet.next()) {

                String oneCategory = resultSet.getString("categoryName");
                result.add(oneCategory);
            }
        }
        return result;
    }

    private HashSet<String> getEnCategoryPageName(String zhName) throws SQLException {
        HashSet<String> allZhCategoryPageName = getZhCategoryName(zhName); //获得所有的中文outlink词条名称
        return getEnCategorylinks(allZhCategoryPageName);
    }

    private HashSet<String> getZhCategoryName(String zhName) throws SQLException {
        HashSet<String> result = new HashSet<String>();
        if (!zhName.contains("\'")) {
            String selectZhCategorys = "select b.name from (select c.id from zhwiki.category_pages as c left join zhwiki.pagemapline as d on c.pages=d.id where name = '" + zhName + "') as a,zhwiki.category as b where a.id=b.id;";
            ResultSet resultSet = opMysql.selectSQL(zhconn, selectZhCategorys);
            while (resultSet.next()) {
                String oneOutlink = resultSet.getString("name");
                result.add(oneOutlink);
            }
        }
        return result;
    }

    public void setCommonCategory(int commonCategory) {
        this.commonCategory = commonCategory;
    }

    /**
     * 获取一个中文词条中inlinks中在MDic中能找到的对应英文词条name
     *
     * @param zhWikiTitle
     * @return
     */
    private HashSet<String> getEnInLinkPageName(String zhWikiTitle) throws SQLException {

        HashSet<String> allZhInlinkPageName = getZhInlinkName(zhWikiTitle); //获得所有的中文outlink词条名称
        return getEnlinks(allZhInlinkPageName);
    }


    /**
     * 获取中文词条inlinks中所有中文词条
     *
     * @param zhWikiTitle
     * @return
     */
    private HashSet<String> getZhInlinkName(String zhWikiTitle) throws SQLException {

        HashSet<String> result = new HashSet<String>();
        if (!zhWikiTitle.contains("\'")) {
            String selectZhOutlink = "select name from (SELECT b.inLinks FROM zhwiki.pagemapline as a,zhwiki.page_inlinks as b where a.name='" + zhWikiTitle + "' and a.id=b.id) as c,zhwiki.pagemapline as d where c.inLinks=d.id;";
            ResultSet resultSet = opMysql.selectSQL(zhconn, selectZhOutlink);
            while (resultSet.next()) {

                String oneOutlink = resultSet.getString("name");
                result.add(oneOutlink);
            }
        }
        return result;
    }

    /**
     * 利用MDic处理这些中文links，提取出对应的英文词条
     *
     * @param allZhOutlinkPageName
     * @return
     */
    private HashSet<String> getEnlinks(HashSet<String> allZhOutlinkPageName) {
        HashSet<String> result = new HashSet<String>();
        ArrayList<String> MDic = Myutil.readByLine(Contant.MDicInputPath);

        for (String line : MDic) {
            String[] titles = line.split("=");
            String en_title = titles[0];
            String zh_title = titles[1];
            if (allZhOutlinkPageName.contains(zh_title)) {
                result.add(en_title);
            }

        }
        return result;
    }

    /**
     * 利用MDic处理这些中文links，提取出对应的英文词条
     *
     * @param allZhOutlinkPageName
     * @return
     */
    private HashSet<String> getEnCategorylinks(HashSet<String> allZhOutlinkPageName) {
        HashSet<String> result = new HashSet<String>();
        ArrayList<String> MDic = Myutil.readByLine(Contant.MDicCategoryInputPath);

        for (String line : MDic) {
            String[] titles = line.split("=");
            String en_title = titles[0];
            String zh_title = titles[1];
            if (allZhOutlinkPageName.contains(zh_title)) {
                result.add(en_title);
            }

        }
        return result;
    }


}
