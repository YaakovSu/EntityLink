package mytools;

import edu.util.Contant;
import edu.util.Myutil;
import edu.util.opMysql;
import rsvm.we2yago;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by CBD_O9 on 2015-04-07.
 */
public class find_Linkto_InenWikiPedia {
    static int N = 2000;
    private static double in0 = 0;
    private static double in10 = 0;
    private static double in20 = 0;
    private static double in30 = 0;
    private static double in40 = 0;
    private static double in50 = 0;
    private static double in60 = 0;
    private static double in70 = 0;
    private static double in80 = 0;
    private static double in90 = 0;
    private static double in100 = 0;
    private static double in110 = 0;

    private static double out0 = 0;
    private static double out10 = 0;
    private static double out20 = 0;
    private static double out30 = 0;
    private static double out40 = 0;
    private static double out50 = 0;
    private static double out60 = 0;
    private static double out70 = 0;
    private static double out80 = 0;
    private static double out90 = 0;
    private static double out100 = 0;
    private static double out110 = 0;

    private static double cat0 = 0;
    private static double cat10 = 0;
    private static double cat20 = 0;
    private static double cat30 = 0;
    private static double cat40 = 0;
    private static double cat50 = 0;
    private static double cat60 = 0;
    private static double cat70 = 0;
    private static double cat80 = 0;
    private static double cat90 = 0;
    private static double cat100 = 0;
    private static double cat110 = 0;


    public static void main(String[] args) throws SQLException, IOException {

        String MDicPath = "C:\\youngsu\\Entity_LinkingData\\en_zh_wiki_page.output";


        String inputPath = "C:\\youngsu\\Entity_LinkingData\\myLinkData1.tsv";
        getAllRelationCount(inputPath);


//        getRate(getTopNMDicEnWikiTitle(N,MDicPath));
//        getInlinkOutLinkCategoryRate(getTopNMDicEnZhWikiTitle(N,MDicPath));
    }

    private static void getAllRelationCount(String inputPath) {
        ArrayList<String> text = Myutil.readByLine(inputPath);
        for (String line : text) {
            String[] words = line.split("\t");
            int inlinkcount = Integer.parseInt(words[0]);
            int outlinkcount = Integer.parseInt(words[1]);
            int categorycount = Integer.parseInt(words[2]);
            if (inlinkcount == 0) {

                in0++;
            } else if (inlinkcount > 0 && inlinkcount <= 10) {
                in10++;
            } else if (inlinkcount > 10 && inlinkcount <= 20) {
                in20++;
            } else if (inlinkcount > 20 && inlinkcount <= 30) {
                in30++;
            } else if (inlinkcount > 30 && inlinkcount <= 40) {
                in40++;
            } else if (inlinkcount > 40 && inlinkcount <= 50) {
                in50++;
            } else if (inlinkcount > 50 && inlinkcount <= 60) {
                in60++;
            } else if (inlinkcount > 60 && inlinkcount <= 70) {
                in70++;
            } else if (inlinkcount > 70 && inlinkcount <= 80) {
                in80++;
            } else if (inlinkcount > 80 && inlinkcount <= 90) {
                in90++;
            } else if (inlinkcount > 90 && inlinkcount <= 100) {
                in100++;
            } else {
                in110++;
            }

            if (outlinkcount == 0) {
                out0++;
            } else if (outlinkcount > 0 && outlinkcount <= 10) {
                out10++;
            } else if (outlinkcount > 10 && outlinkcount <= 20) {
                out20++;
            } else if (outlinkcount > 20 && outlinkcount <= 30) {
                out30++;
            } else if (outlinkcount > 30 && outlinkcount <= 40) {
                out40++;
            } else if (outlinkcount > 40 && outlinkcount <= 50) {
                out50++;
            } else if (outlinkcount > 50 && outlinkcount <= 60) {
                out60++;
            } else if (outlinkcount > 60 && outlinkcount <= 70) {
                out70++;
            } else if (outlinkcount > 70 && outlinkcount <= 80) {
                out80++;
            } else if (outlinkcount > 80 && outlinkcount <= 90) {
                out90++;
            } else if (outlinkcount > 90 && outlinkcount <= 100) {
                out100++;
            } else {
                out110++;
            }

            if (categorycount == 0) {
                cat0++;
            } else if (categorycount > 0 && categorycount <= 10) {
                cat10++;
            } else if (categorycount > 10 && categorycount <= 20) {
                cat20++;
            } else if (categorycount > 20 && categorycount <= 30) {
                cat30++;
            } else if (categorycount > 30 && categorycount <= 40) {
                cat40++;
            } else if (categorycount > 40 && categorycount <= 50) {
                cat50++;
            } else if (categorycount > 50 && categorycount <= 60) {
                cat60++;
            } else if (categorycount > 60 && categorycount <= 70) {
                cat70++;
            } else if (categorycount > 70 && categorycount <= 80) {
                cat80++;
            } else if (categorycount > 80 && categorycount <= 90) {
                cat90++;
            } else if (categorycount > 90 && categorycount <= 100) {
                cat100++;
            } else {
                cat110++;
            }

        }
        double allIn = in0 + in10 + in20 + in30 + in40 + in50 + in60 + in70 + in80 + in90 + in100 + in110;
        double allOut = out0 + out10 + out20 + out30 + out40 + out50 + out60 + out70 + out80 + out90 + out100 + out110;
        double allcat = cat0 + cat10 + cat20 + cat30 + cat40 + cat50 + cat60 + cat70 + cat80 + cat90 + cat100 + cat110;

        System.out.println(in0 / allIn + "\t" + in10 / allIn + "\t" + in20 / allIn + "\t" + in30 / allIn + "\t" + in40 / allIn + "\t" + in50 / allIn + "\t" + in60 / allIn + "\t" + in70 / allIn + "\t" + in80 / allIn + "\t" + in90 / allIn + "\t" + in100 / allIn + "\t" + in110 / allIn);

        System.out.println();
        System.out.println(out0 / allOut + "\t" + out10 / allOut + "\t" + out20 / allOut + "\t" + out30 / allOut + "\t" + out40 / allOut + "\t" + out50 / allOut + "\t" + out60 / allOut + "\t" + out70 / allOut + "\t" + out80 / allOut + "\t" + out90 / allOut + "\t" + out100 / allOut + "\t" + out110 / allOut);
        System.out.println();
        System.out.println(cat0 / allcat + "\t" + cat10 / allcat + "\t" + cat20 / allcat + "\t" + cat30 / allcat + "\t" + cat40 / allcat + "\t" + cat50 / allcat + "\t" + cat60 / allcat + "\t" + cat70 / allcat + "\t" + cat80 / allcat + "\t" + cat90 / allcat + "\t" + cat100 / allcat + "\t" + cat110 / allcat);
    }

    public static ArrayList<String> getTopNMDicEnWikiTitle(int N, String MDicPath) {
        ArrayList<String> result = new ArrayList<String>();
        ArrayList<String> text = Myutil.readByLine(MDicPath);
        for (int i = 0; i < text.size(); i++) {
            if (i >= 500 && i < 500 + N) {
                String enName = text.get(i).split("=")[0];
                result.add(enName);
            }
        }

        return result;
    }


    public static ArrayList<String[]> getTopNMDicEnZhWikiTitle(int N, String MDicPath) {
        ArrayList<String[]> result = new ArrayList<String[]>();
        ArrayList<String> text = Myutil.readByLine(MDicPath);
        for (int i = 0; i < text.size(); i++) {
            if (i >= 500 && i < 500 + N) {
                String[] Name = text.get(i).split("=");
                result.add(Name);
            }
        }

        return result;
    }


    public static void getInlinkOutLinkCategoryRate(ArrayList<String[]> MDicNames) throws SQLException, IOException {

        FileWriter fw = new FileWriter("C:\\youngsu\\Entity_LinkingData\\myLinkData.tsv");
        Connection conn = opMysql.connSQL(Contant.enmysqlurl);
        int count = 0;
        for (String[] name : MDicNames) {


            String enWikiTitle = name[0];
            String zhWikiTitle = name[1];

            if (!enWikiTitle.contains("'")) {
                //获取中文词条的category对应的英文词条
                ArrayList<String> En_MDIc_ZhCategoryName = getEnCategoryName(zhWikiTitle);
                //获取中文词条的outlink对应的英文词条
                ArrayList<String> En_MDIc_ZhOutlinkName = getEnOutlinkName(zhWikiTitle);

                //获取一个中文词条中inlinks中在MDic中能找到的对应英文词条name
                ArrayList<String> En_MDIc_ZhPageName = getEnInLinkPageName(zhWikiTitle);
                HashSet<String> en_allInlinks = getAllInlinkTitle(conn, enWikiTitle);
                HashSet<String> en_allOutlinks = getAllOutlinkTitle(conn, enWikiTitle);

                HashSet<String> en_allCategory = getAllCategoryTitle(conn, enWikiTitle);
                int inlinkCount = 0;
                int outlinkCount = 0;
                int categoryCount = 0;
                for (String inlinkName : En_MDIc_ZhPageName) {
                    if (en_allInlinks.contains(inlinkName)) {
                        inlinkCount++;
                    }
                }
                for (String outlinkName : En_MDIc_ZhOutlinkName) {
                    if (en_allOutlinks.contains(outlinkName)) {
                        outlinkCount++;
                    }
                }

                for (String categoryName : En_MDIc_ZhCategoryName) {
                    if (en_allCategory.contains(categoryName)) {
                        categoryCount++;
                    }
                }

                count++;
                System.out.println("已经完成了：" + count);
                fw.write(inlinkCount + "\t" + outlinkCount + "\t" + categoryCount + "\n");
                fw.flush();
            }

        }
        fw.close();

    }

    private static HashSet<String> getAllCategoryTitle(Connection conn, String enWikiTitle) throws SQLException {
        HashSet<String> result = new HashSet<String>();
        String selectOutlinkSql = "SELECT a.name FROM enwiki.category as a,(select b.id,c.name from enwiki.category_pages as b,enwiki.pagemapline as c where b.pages=c.id and c.name='" + enWikiTitle + "') as d where a.id=d.id;";

        ResultSet resultSet = opMysql.selectSQL(conn, selectOutlinkSql);
        while (resultSet.next()) {
            String outlink = resultSet.getString("name"); //閲岄潰瀛樼殑鏄痠nlink鐨処D
            result.add(outlink);

        }

        return result;
    }


    /**
     * 获取一个中文词条中outlinks中在MDic中能找到的对应英文词条name
     *
     * @param zhWikiTitle
     * @return
     */
    private static ArrayList<String> getEnInLinkPageName(String zhWikiTitle) throws SQLException {

        ArrayList<String> allZhInlinkPageName = getZhInlinkName(zhWikiTitle); //获得所有的中文outlink词条名称
//        System.out.println(allZhOutlinkPageName);
        return getEnInlinks(allZhInlinkPageName);
    }

    /**
     * 利用MDic处理这些中文outlinks，提取出对应的英文词条
     *
     * @param allZhInlinkPageName
     * @return
     */
    private static ArrayList<String> getEnInlinks(ArrayList<String> allZhInlinkPageName) {
        ArrayList<String> result = new ArrayList<String>();
        ArrayList<String> MDic = Myutil.readByLine(Contant.MDicInputPath);

        for (String line : MDic) {
            String[] titles = line.split("=");
            String en_title = titles[0];
            String zh_title = titles[1];
            if (!en_title.contains("\'")) {
                if (allZhInlinkPageName.contains(zh_title)) {
                    result.add(en_title);
                }
            }


        }
        return result;
    }

    /**
     * 获取中文词条outlinks中所有中文词条
     *
     * @param zhWikiTitle
     * @return
     */
    private static ArrayList<String> getZhInlinkName(String zhWikiTitle) throws SQLException {

        System.out.println("dfdfd" + zhWikiTitle);

        Connection conn = opMysql.connSQL(Contant.zhmysqlurl);
        ArrayList<String> result = new ArrayList<String>();
        String selectZhOutlink = "select name from (SELECT b.inLinks FROM zhwiki.pagemapline as a,zhwiki.page_inlinks as b where a.name='" + zhWikiTitle + "' and a.id=b.id) as c,zhwiki.pagemapline as d where c.inLinks=d.id;";
//        System.out.println(selectZhOutlink);
        ResultSet resultSet = opMysql.selectSQL(conn, selectZhOutlink);
        while (resultSet.next()) {

            String oneOutlink = resultSet.getString("name");
//            System.out.println("嘎嘎个 "+oneOutlink);
            result.add(oneOutlink);

        }
        opMysql.deconnSQL(conn);

        return result;
    }


    private static ArrayList<String> getEnOutlinkName(String zhWikiTitle) throws SQLException {
        ArrayList<String> allZhInlinkPageName = getZhOutlinkName(zhWikiTitle); //获得所有的中文outlink词条名称
//        System.out.println(allZhOutlinkPageName);
        return getEnOutlinks(allZhInlinkPageName);
    }


    private static ArrayList<String> getEnOutlinks(ArrayList<String> allZhInlinkPageName) {
        ArrayList<String> result = new ArrayList<String>();
        ArrayList<String> MDic = Myutil.readByLine(Contant.MDicInputPath);

        for (String line : MDic) {
            String[] titles = line.split("=");
            String en_title = titles[0];
            String zh_title = titles[1];
            if (!en_title.contains("\'")) {
                if (allZhInlinkPageName.contains(zh_title)) {
                    result.add(en_title);
                }
            }


        }
        return result;
    }

    private static ArrayList<String> getZhOutlinkName(String zhWikiTitle) throws SQLException {
        Connection conn = opMysql.connSQL(Contant.zhmysqlurl);
        ArrayList<String> result = new ArrayList<String>();
        String selectZhOutlink = "select name from (SELECT b.outLinks FROM zhwiki.pagemapline as a,zhwiki.page_outlinks as b where a.name='" + zhWikiTitle + "' and a.id=b.id) as c,zhwiki.pagemapline as d where c.outLinks=d.id;";
//        System.out.println(selectZhOutlink);
        ResultSet resultSet = opMysql.selectSQL(conn, selectZhOutlink);
        while (resultSet.next()) {

            String oneOutlink = resultSet.getString("name");
//            System.out.println("嘎嘎个 "+oneOutlink);
            result.add(oneOutlink);
        }
        opMysql.deconnSQL(conn);
        return result;

    }


    private static ArrayList<String> getEnCategoryName(String zhWikiTitle) throws SQLException {
        ArrayList<String> allZhCategoryName = getZhCategoryName(zhWikiTitle); //获得所有的中文outlink词条名称
//        System.out.println(allZhOutlinkPageName);
        return getEnCategorys(allZhCategoryName);
    }

    private static ArrayList<String> getZhCategoryName(String zhWikiTitle) throws SQLException {
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


    private static ArrayList<String> getEnCategorys(ArrayList<String> allZhCategoryName) {
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


    /**
     * 获取给定的词的yago中的inlink和英文维基中的links的重复率
     *
     * @param enNames
     * @return
     * @throws SQLException
     */
    public static double getRate(ArrayList<String> enNames) throws SQLException {

        Connection yagoConn = opMysql.connSQL(Contant.yagomysqlurl);

        we2yago yagoFact = new we2yago(yagoConn);
        double count = 0;
        double countInlink = 0;
        double countOutlink = 0;
        int nn = 0;
        for (String nameA : enNames) {
            if (!nameA.contains("'")) {
                Connection enMysqlConn = opMysql.connSQL(Contant.enmysqlurl);

                HashSet<String> allEnWikiInlinks = getAllInlinkTitle(enMysqlConn, nameA);
                HashSet<String> allEnWikioutlinks = getAllOutlinkTitle(enMysqlConn, nameA);

                HashSet<String> allEnWikiLinks = getAllwikiLinks2(allEnWikiInlinks, allEnWikioutlinks);
//               HashSet<String> allEnWikiLinks = getAllwikiLinks(enMysqlConn,nameA);

                enMysqlConn.close();

                ArrayList<String> testWikipediainfo = yagoFact.getAllAssociateNameRelation(nameA, Contant.yagowikipediainfo);

//               System.out.println(testWikipediainfo.size());
                if (testWikipediainfo.size() > 0) {
                    ArrayList<String> testFact = yagoFact.getAllAssociateNameRelation(nameA, Contant.yagofacts);

                    ArrayList<String> nameInEnWiki = new ArrayList<String>();
                    for (String wikiInfo : testWikipediainfo) {

                        String[] names = wikiInfo.split("\t");
                        String name1 = names[0];
                        String name2 = names[2];
                        if (!name1.equals(nameA)) {
                            if (!nameInEnWiki.contains(name1)) {
                                nameInEnWiki.add(name1);
                            }
                            if (!nameInEnWiki.contains(name2)) {
                                nameInEnWiki.add(name2);
                            }
                        }
                    }

                    for (String fact : testFact) {

                        String[] names = fact.split("\t");
                        String name1 = names[0];
                        String name2 = names[2];
                        if (!name1.equals(nameA)) {
                            if (!nameInEnWiki.contains(name1)) {
                                nameInEnWiki.add(name1);
                            }
                            if (!nameInEnWiki.contains(name2)) {
                                nameInEnWiki.add(name2);
                            }
                        }
                    }

                    for (String yagoLinktoName : nameInEnWiki) {
                        if (allEnWikiLinks.contains(yagoLinktoName)) {
                            count++;
                            break;
                        }
                    }

                    for (String yagoLinktoName : nameInEnWiki) {
                        if (allEnWikiInlinks.contains(yagoLinktoName)) {
                            countInlink++;
                            break;
                        }
                    }

                    for (String yagoLinktoName : nameInEnWiki) {
                        if (allEnWikioutlinks.contains(yagoLinktoName)) {
                            countOutlink++;
                            break;
                        }
                    }


//           System.out.println(nameInEnWiki);
//           System.exit(0);

//           Iterator<String> iterator = allEnWikiLinks.iterator();
//           while (iterator.hasNext()){
//               String name = iterator.next();
//               System.out.println(name);
//           }
//           System.exit(0);

                    nn++;
                    System.out.println("已完成：" + nn);
                    System.out.println();
                }


            }

        }
        System.out.println(count);
        System.out.println(nn);
        double rate_all = count / nn;
        double rate_in = countInlink / nn;
        double rate_out = countOutlink / nn;
        System.out.println("所有的：" + rate_all);
        System.out.println("inlink的：" + rate_in);
        System.out.println("outlink的：" + rate_out);
        yagoConn.close();
//       enMysqlConn.close();


        return 0;
    }


    public static HashSet<String> getAllwikiLinks2(HashSet<String> result, HashSet<String> adds) throws SQLException {

//        HashSet<String> result = getAllInlinkTitle(conn,wikiInputTitle);
//        HashSet<String> adds = getAllOutlinkTitle(conn,wikiInputTitle);
        for (String add : adds) {
            result.add(add);
        }
//        conn.close();
        return result;
    }


    public static HashSet<String> getAllwikiLinks(Connection conn, String wikiInputTitle) throws SQLException {

        HashSet<String> result = getAllInlinkTitle(conn, wikiInputTitle);
        HashSet<String> adds = getAllOutlinkTitle(conn, wikiInputTitle);
        for (String add : adds) {
            result.add(add);
        }
        conn.close();
        return result;
    }

    private static HashSet<String> getAllInlinkTitle(Connection conn, String wikiInputTitle) throws SQLException {

        HashSet<String> result = new HashSet<String>();
//        String selectInlinkSql = "SELECT * FROM zhwiki.page_inlinks as a,zhwiki.pagemapline as b where a.id = b.id and b.name='" + wikiInputTitle + "';";


        String selectInlinkSql = "select name from (SELECT inLinks FROM enwiki.page_inlinks as a,enwiki.pagemapline as b where a.id = b.id and b.name='" + wikiInputTitle + "') as a,pagemapline as b where a.inLinks = b.id;";

        ResultSet resultSet = opMysql.selectSQL(conn, selectInlinkSql);
        while (resultSet.next()) {
            String inlink = resultSet.getString("name"); //閲岄潰瀛樼殑鏄痠nlink鐨処D
            result.add(inlink);

        }

        return result;
    }

    private static HashSet<String> getAllOutlinkTitle(Connection conn, String wikiInputTitle) throws SQLException {

        HashSet<String> result = new HashSet<String>();
        String selectOutlinkSql = "select name from (SELECT outLinks FROM enwiki.page_outlinks as a,enwiki.pagemapline as b where a.id = b.id and b.name='" + wikiInputTitle + "') as a,pagemapline as b where a.outLinks = b.id;";
        ResultSet resultSet = opMysql.selectSQL(conn, selectOutlinkSql);
        while (resultSet.next()) {
            String outlink = resultSet.getString("name"); //閲岄潰瀛樼殑鏄痠nlink鐨処D
            result.add(outlink);

        }

        return result;
    }


}
