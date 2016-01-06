package mytools;

import edu.baike.ZhEnPair;
import edu.util.Contant;
import edu.util.Myutil;
import edu.util.opMysql;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by CBD_O9 on 2014-12-11.
 */
public class ComputeCommonInOutCateInMDic {
    static String semi_trainInputPath = Contant.MDicInputPath;

    static Connection zhconn = opMysql.connSQL(Contant.zhmysqlurl);
    static Connection enconn = opMysql.connSQL(Contant.enmysqlurl);

    public static void main(String[] args) throws IOException {
        readByLine(semi_trainInputPath);
    }


    /**
     * 按行读取文本
     *
     * @param filePath
     * @return
     */
    public static ArrayList<String> readByLine(String filePath) throws IOException {
        ArrayList<String> content = new ArrayList<String>();
        File file = new File(filePath);
        FileWriter fw = new FileWriter("C:\\youngsu\\Entity_LinkingData\\CommonInOutCate_Num_InMDic1.output");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            ComputeCommonInOutCateInMDic com = new ComputeCommonInOutCateInMDic();
//一次读一行，读入null时文件结束
            int count = 0;
            while ((tempString = reader.readLine()) != null) {
                count++;
                System.out.println("已完成:" + count);
                String[] line = tempString.split("=");
                String zhName = line[1];
                String enName = line[0];


                HashSet<String> En_MDIc_ZhPageName = com.getEnInLinkPageName(zhName);
                HashSet<String> En_MDIc_ZhCategoryName = com.getEnCategoryPageName(zhName);
                ZhEnPair pair = new ZhEnPair(zhName, enName, En_MDIc_ZhPageName, En_MDIc_ZhCategoryName, zhconn, enconn);
                String comIn = String.valueOf(pair.getCommonInlink());
                String comOut = String.valueOf(pair.getCommonOutlink());
                String comCat = String.valueOf(pair.getCommonCategory());

                fw.write(zhName + "\t" + enName + "\t" + comIn + "\t" + comOut + "\t" + comCat + "\n");
                fw.flush();


            }
            fw.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }

        return content;
    }


    private HashSet<String> getEnCategoryPageName(String zhName) throws SQLException {
        HashSet<String> allZhCategoryPageName = getZhCategoryName(zhName); //获得所有的中文outlink词条名称
//        System.out.println(allZhOutlinkPageName);
        return getEnCategorylinks(allZhCategoryPageName);
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

    private HashSet<String> getZhCategoryName(String zhName) throws SQLException {
//        Connection conn = opMysql.connSQL(Contant.zhmysqlurl);
        HashSet<String> result = new HashSet<String>();
        if (!zhName.contains("\'")) {
            String selectZhCategorys = "select b.name from (select c.id from zhwiki.category_pages as c left join zhwiki.pagemapline as d on c.pages=d.id where name = '" + zhName + "') as a,zhwiki.category as b where a.id=b.id;";

//        System.out.println(selectZhOutlink);
            ResultSet resultSet = opMysql.selectSQL(zhconn, selectZhCategorys);
            while (resultSet.next()) {

                String oneOutlink = resultSet.getString("name");
//            System.out.println("嘎嘎个 "+oneOutlink);
                result.add(oneOutlink);
            }
        }

//        opMysql.deconnSQL(conn);
        return result;
    }


    /**
     * 获取一个中文词条中inlinks中在MDic中能找到的对应英文词条name
     *
     * @param zhWikiTitle
     * @return
     */
    private HashSet<String> getEnInLinkPageName(String zhWikiTitle) throws SQLException {

        HashSet<String> allZhInlinkPageName = getZhInlinkName(zhWikiTitle); //获得所有的中文outlink词条名称
//        System.out.println(allZhOutlinkPageName);
        return getEnlinks(allZhInlinkPageName);
    }

    /**
     * 获取中文词条inlinks中所有中文词条
     *
     * @param zhWikiTitle
     * @return
     */
    private HashSet<String> getZhInlinkName(String zhWikiTitle) throws SQLException {

//        Connection conn = opMysql.connSQL(Contant.zhmysqlurl);
        HashSet<String> result = new HashSet<String>();
        if (!zhWikiTitle.contains("\'")) {
            String selectZhOutlink = "select name from (SELECT b.inLinks FROM zhwiki.pagemapline as a,zhwiki.page_inlinks as b where a.name='" + zhWikiTitle + "' and a.id=b.id) as c,zhwiki.pagemapline as d where c.inLinks=d.id;";
//        System.out.println(selectZhOutlink);
            ResultSet resultSet = opMysql.selectSQL(zhconn, selectZhOutlink);
            while (resultSet.next()) {

                String oneOutlink = resultSet.getString("name");
//            System.out.println("嘎嘎个 "+oneOutlink);
                result.add(oneOutlink);
            }
        }

//        opMysql.deconnSQL(conn);
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
}
