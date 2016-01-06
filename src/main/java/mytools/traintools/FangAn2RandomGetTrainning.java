package mytools.traintools;

import rsvm.wc2we;
import edu.baike.ZhEnPair;
import edu.util.Contant;
import edu.util.Myutil;
import edu.util.opMysql;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * 随机生成1000*1000矩阵
 * Created by CBD_O9 on 2014-11-17.
 */
public class FangAn2RandomGetTrainning {

    String outputPath = "C:\\youngsu\\Entity_LinkingData\\trainningDataResultRate1126_6feature.output";
    String outputPath1 = "C:\\youngsu\\Entity_LinkingData\\testDataResult.output";

    public static void main(String[] args) throws SQLException, IOException {
        FangAn2RandomGetTrainning fa = new FangAn2RandomGetTrainning();
        String semi_trainInputPath = "C:\\youngsu\\Entity_LinkingData\\trainningData1.output";

        String semi_testInputPath = "C:\\youngsu\\Entity_LinkingData\\testData.output";

        ArrayList<String> data = Myutil.readByLine(semi_trainInputPath);
//        ArrayList<String> data1 = Myutil.readByLine(semi_testInputPath);

        fa.createTrainningData(data);
//        fa.createtestData(data1);
    }

    Connection zhconn;
    Connection enconn;


    private void createTrainningData(ArrayList<String> data) throws SQLException, IOException {

        FileWriter fw = new FileWriter(outputPath);
        int count = 0;
        String pre_name = "";
        HashSet<String> En_MDIc_ZhPageName = new HashSet<String>();
        HashSet<String> En_MDIc_ZhCategoryName = new HashSet<String>();
        wc2we fan = new wc2we();
        int count_zhInlinks = -1;
        int count_zhOutlinks = -1;
        int count_zhCategory = -1;

        for (String line : data) {


            if (count % 5000 == 0) {
                if (count != 0) {
                    zhconn.close();
                    enconn.close();
                }


                zhconn = opMysql.connSQL(Contant.zhmysqlurl);
                enconn = opMysql.connSQL(Contant.enmysqlurl);
            }

            System.out.println("已完成：" + count);
            String[] words = line.split("\t");
            String tag = words[0];
            String zhname = words[1];

            String enname = words[2];


            if (!pre_name.equals(zhname)) {

                count_zhInlinks = fan.getZhInlinkCount(zhname);
                count_zhOutlinks = fan.getZhOutlinkCount(zhname);
                count_zhCategory = fan.getZhCategoryCount(zhname);
                En_MDIc_ZhPageName = getEnInLinkPageName(zhname);
                En_MDIc_ZhCategoryName = getEnCategoryPageName(zhname);
                pre_name = zhname;
//                System.out.println("又执行到了！");
            }
            //获取一个中文词条中outlinks中在MDic中能找到的对应英文词条name

            if (count_zhCategory != -1) {
                ZhEnPair pair = new ZhEnPair(zhname, enname, En_MDIc_ZhPageName, En_MDIc_ZhCategoryName, zhconn, enconn);


                int commonOutlinks = pair.getCommonOutlink();
                if (commonOutlinks != 0) {
//            System.out.println("category要多少时间");
                    int commonInlinks = pair.getCommonInlink();
                    int commonCategorys = pair.getCommonCategory();
//            System.out.println("阿西坝");

                    int count_enInlinks = pair.getEnInlinkCount(enname);
                    int count_enOutlinks = pair.getEnOutlinkCount(enname);
                    int count_enCategory = pair.getEnCategoryCount(enname);


                    double inlinkCommon = Integer.valueOf(commonInlinks).doubleValue();
                    double outlinkCommon = Integer.valueOf(commonOutlinks).doubleValue();
                    double categoryCommon = Integer.valueOf(commonCategorys).doubleValue();

                    double count_enIn = Integer.valueOf(count_enInlinks).doubleValue();
                    double count_enOut = Integer.valueOf(count_enOutlinks).doubleValue();
                    double count_enCat = Integer.valueOf(count_enCategory).doubleValue();

                    double inRate = 2 * inlinkCommon / (count_enIn + count_zhInlinks);
                    double outRate = 2 * outlinkCommon / (count_enOut + count_zhOutlinks);
                    double catRate = 2 * categoryCommon / (count_enCat + count_zhCategory);
                    double all = inRate + outRate + catRate;

                    inRate = inRate / all;
                    outRate = outRate / all;
                    catRate = catRate / all;

                    String inrate = String.valueOf(inRate);
                    String outrate = String.valueOf(outRate);
                    String catrate = String.valueOf(catRate);


                    System.out.println(zhname + " " + enname + " " + commonInlinks + " " + commonOutlinks + " " + commonCategorys + " " + tag);
                    fw.write(tag + " " + "1:" + inrate + " " + "2:" + outrate + " " + "3:" + catrate + " " + "4:" + commonInlinks + " " + "5:" + commonOutlinks + " " + "6:" + commonCategorys + "\n");

                }


//            if(count==10){
//                System.exit(0);
//            }

            }
            count++;
            fw.flush();

        }


        fw.close();
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
        String selectZhCategorys = "select b.name from (select c.id from zhwiki.category_pages as c left join zhwiki.pagemapline as d on c.pages=d.id where name = '" + zhName + "') as a,zhwiki.category as b where a.id=b.id;";

//        System.out.println(selectZhOutlink);
        ResultSet resultSet = opMysql.selectSQL(zhconn, selectZhCategorys);
        while (resultSet.next()) {

            String oneOutlink = resultSet.getString("name");
//            System.out.println("嘎嘎个 "+oneOutlink);
            result.add(oneOutlink);
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
        String selectZhOutlink = "select name from (SELECT b.inLinks FROM zhwiki.pagemapline as a,zhwiki.page_inlinks as b where a.name='" + zhWikiTitle + "' and a.id=b.id) as c,zhwiki.pagemapline as d where c.inLinks=d.id;";
//        System.out.println(selectZhOutlink);
        ResultSet resultSet = opMysql.selectSQL(zhconn, selectZhOutlink);
        while (resultSet.next()) {

            String oneOutlink = resultSet.getString("name");
//            System.out.println("嘎嘎个 "+oneOutlink);
            result.add(oneOutlink);
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
