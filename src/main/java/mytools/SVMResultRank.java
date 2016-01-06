package mytools;

import jeasy.analysis.MMAnalyzer;
import edu.util.Contant;
import edu.util.Myutil;
import edu.util.opMysql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by CBD_O9 on 2014-11-27.
 * 借助查询文档的TF值来进行排序
 */
public class SVMResultRank {

    static Connection enconn = opMysql.connSQL(Contant.enmysqlurl);
    static Connection zhconn = opMysql.connSQL(Contant.zhmysqlurl);

    public static void main(String[] args) throws SQLException, IOException {
        SVMResultRank svmrank = new SVMResultRank();
        String namePath = "C:\\youngsu\\2_name.output";
        String featurePath = "C:\\youngsu\\ress.out";
        String zhName = "湖南师范大学"; //需要查询的中文维基名称


        //获得svm返回结果中的正例名称
        ArrayList<String> enNames = svmrank.getCandidateENNames(namePath, featurePath);
        HashMap<String, String> En_MDIc_ZhPageName = svmrank.getEnInLinkPageName(zhName);
        HashMap<String, Integer> commonInlinkCount = new HashMap<String, Integer>();


        for (String enName : enNames) {
            ArrayList<String> commonInlinkNames = svmrank.getCommonZhInlinkNames(zhName, enName, En_MDIc_ZhPageName); //获取zhName和enName在MDic中共有的Inlink名称
            for (String name : commonInlinkNames) {
                if (commonInlinkCount.containsKey(name)) {
                    int count = commonInlinkCount.get(name);
                    count = count + 1;
                    commonInlinkCount.put(name, count);
                } else {
                    commonInlinkCount.put(name, 1);
                }
            }

//            double ave_tfScore = svmrank.tfscore(commonInlinkNames, zhName,zhconn); //获取这些inlink Document中zhname的TF值
//            System.out.println(enName+" "+String.valueOf(ave_tfScore));

        }
        for (String en : enNames) {
            ArrayList<String> commonInlinkNames = svmrank.getCommonZhInlinkNames(zhName, en, En_MDIc_ZhPageName); //获取zhName和enName在MDic中共有的Inlink名称
            double ave_tfScore = svmrank.tfscore(commonInlinkNames, commonInlinkCount, zhName, zhconn); //获取这些inlink Document中zhname的TF值
            System.out.println(en + " " + String.valueOf(ave_tfScore));

        }
    }

    private double tfscore(ArrayList<String> commonInlinkNames, HashMap<String, Integer> commonInlinkCount, String zhName, Connection conn) throws SQLException, IOException {
        String zh = zhName.split("_")[0];  //对zhName进行预处理
        ArrayList<Double> scores = new ArrayList<Double>();
        System.out.println(commonInlinkNames);
        System.out.println(commonInlinkNames.size());
        double prun = 0.1 * commonInlinkNames.size();
        int count = 0;
        System.out.println("gaw " + prun);
        System.out.println();
        for (String commonInlinkName : commonInlinkNames) {

            if (commonInlinkCount.get(commonInlinkName) < prun) {
                String selectSql = "SELECT text FROM zhwiki.page where name = '" + commonInlinkName + "';";
                ResultSet resultSet = opMysql.selectSQL(conn, selectSql);
                String text = "";
                while (resultSet.next()) {
                    text = resultSet.getString("text");

                }
                String[] cutword = cutWord(text);
                double score = normalTF(cutword, zh);
                scores.add(score);
            }

//            System.out.println("已完成 "+count);
            count++;
        }
        int size = scores.size();

        System.out.println("nima" + scores);
        System.out.println("woqu" + scores.size());
        System.exit(0);
        double sum = 0;
        for (int i = 0; i < scores.size(); i++) {
            sum = sum + scores.get(i);
        }
        double result = sum / size;

        return result;
    }


    public static String[] cutWord(String text) throws IOException {
        String[] cutWordResult = null;
//        String text = ReadFiles.readFiles(file);
        MMAnalyzer analyzer = new MMAnalyzer();
//        System.out.println("file content: "+text);
//        System.out.println("cutWordResult: "+analyzer.segment(text, " "));
        String tempCutWordResult = analyzer.segment(text, " ");
        cutWordResult = tempCutWordResult.split(" ");
        return cutWordResult;
    }

    public static double normalTF(String[] cutWordResult, String zh) {
        double result = 0;
        HashMap<String, Integer> tfNormal = new HashMap<String, Integer>();//û����滯
        int wordNum = cutWordResult.length;
        System.out.println("made " + wordNum);
        int wordtf = 0;

        String name = zh;
        if (name != " ") {
            for (int j = 0; j < wordNum; j++) {
//                    System.out.println(cutWordResult[j]);
                if (j > 0) {
                    String combinWord = cutWordResult[j - 1] + cutWordResult[j];

                    System.out.println(name + "  " + combinWord);
                    if (name.equals(cutWordResult[j]) || name.equals(combinWord)) {
                        cutWordResult[j] = " ";
                        wordtf++;

                    }
                }
                if (j == 0) {

                    if (name.equals(cutWordResult[j])) {
                        cutWordResult[j] = " ";
                        wordtf++;

                    }
                }


            }


            result = ++wordtf;

        }

        return result;
    }


//    public static double tf(String[] cutWordResult,String zh) {
//        double tf = 0;
//        int wordNum = cutWordResult.length;
//        int wordtf = 0;
//            String name = zh;
//            wordtf = 0;
//            for (int j = 0; j < wordNum; j++) {
//                if (name != " ") {
//                    if (name.equals(cutWordResult[j])) {
//                        cutWordResult[j] = " ";
//                        wordtf++;
//                    }
//                }
//            }
//            if (name != " ") {
////                tf = (new Float(++wordtf)) / wordNum;
//
//                tf = (new Double(++wordtf));
//
//            }
//
//        return tf;
//    }


    private ArrayList<String> getCommonZhInlinkNames(String zhName, String enName, HashMap<String, String> En_MDIc_ZhPageName) throws SQLException {

        ArrayList<String> enInlinksName = getEnInlinkName(enName);
        ArrayList<String> zhInlinkNames = compaire(En_MDIc_ZhPageName, enInlinksName);
        return zhInlinkNames;
    }

    private ArrayList<String> compaire(HashMap<String, String> en_mdIc_zhPageName, ArrayList<String> enInlinksName) {
        ArrayList<String> result = new ArrayList<String>(); //中文inlink names
        Iterator<String> iterator = en_mdIc_zhPageName.keySet().iterator();
        while (iterator.hasNext()) {
            String zhName = iterator.next();
            String enName = en_mdIc_zhPageName.get(zhName);
            if (enInlinksName.contains(enName)) {
                result.add(zhName);
            }
        }

        return result;
    }

    private ArrayList<String> getCandidateENNames(String namePath, String featurePath) {
        ArrayList<String> result = new ArrayList<String>();
        ArrayList<String> nameFile = Myutil.readByLine(namePath);
        ArrayList<String> featureFile = Myutil.readByLine(featurePath);
        for (int i = 0; i < featureFile.size(); i++) {
            String feature = featureFile.get(i);
            if (feature.equals("1")) {
                String name = nameFile.get(i);
                result.add(name);
            }
        }
        return result;
    }


    /**
     * 获取一个中文词条中inlinks中在MDic中能找到的对应英文词条name
     *
     * @param zhWikiTitle
     * @return
     */
    private HashMap<String, String> getEnInLinkPageName(String zhWikiTitle) throws SQLException {

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
    private HashMap<String, String> getEnlinks(HashSet<String> allZhOutlinkPageName) {
        HashMap<String, String> result = new HashMap<String, String>();

        ArrayList<String> MDic = Myutil.readByLine(Contant.MDicInputPath);

        for (String line : MDic) {
            String[] titles = line.split("=");
            String en_title = titles[0];
            String zh_title = titles[1];
            if (allZhOutlinkPageName.contains(zh_title)) {
                result.put(zh_title, en_title);
            }

        }
        return result;
    }


    /**
     * 获取一个英文name所有的inlink的名字
     *
     * @param enName
     * @return
     * @throws SQLException
     */
    private ArrayList<String> getEnInlinkName(String enName) throws SQLException {
//        Connection conn = opMysql.connSQL(Contant.enmysqlurl);
        ArrayList<String> result = new ArrayList<String>();
//        String selectEnInlink = "select b.inlinkName from (select id from enwiki.pagemapline where name = '"+enName+"')as a,enwiki.page_inlinks_names as b where a.id = b.id;";

        String selectEnInlink = "SELECT inlinkName FROM enwiki.pagenames_inlinknames where pageName='" + enName + "';";

//        System.out.println(selectZhOutlink);
        ResultSet resultSet = opMysql.selectSQL(enconn, selectEnInlink);
        while (resultSet.next()) {

            String oneinlink = resultSet.getString("inlinkName");
//            System.out.println("嘎嘎个 "+oneOutlink);
            result.add(oneinlink);
        }
//        opMysql.deconnSQL(conn);
        return result;


    }


}
