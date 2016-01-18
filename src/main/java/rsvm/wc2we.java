package rsvm;

import edu.Constant;
import edu.Language;
import edu.baike.EnBaike;
import edu.baike.KCLpair;
import edu.baike.ZhBaike;
import edu.nlp.BaikeParser;
import edu.nlp.ECDic;
import edu.nlp.FeatureParser;
import edu.nlp.KCLDir;
import edu.util.Contant;
import edu.util.Myutil;
import edu.util.opMysql;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by CBD_O9 on 2014-11-20.
 */
public class wc2we {

    private static HashMap<String, Integer> candidateEnName_outLink = new HashMap<String, Integer>();
    static int zhInlinks = 0;
    private final String firstcloumn = "pageName";
    private final String secondcloumn = "pagenames_inlinknames";
    private final String thirdcloumn = "inlinkName";

    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        wc2we f2 = new wc2we();
        String ountputpath1 = "C:\\youngsu\\libsvm-3.20\\windows\\AppleNames.out";
        String ountputpath12 = "C:\\youngsu\\libsvm-3.20\\windows\\AppleFeature.out";
    }

    public String myFangAn(String zhWikiTitle, String outputpath1, String outputpath2, ECDic E_CDic, String inputName) throws SQLException, IOException, ClassNotFoundException {
        /**
         * inMIDc放到前面的调用函数中了
         */
        KCLDir kclCatDir = new KCLDir(Contant.MDicCategoryInputPath);
        KCLDir kclDir = new KCLDir(Contant.MDicInputPath);
        boolean inMDic = kclDir.containsZh(zhWikiTitle);

        if (inMDic) {
            String outputPath = Constant.QCWEinKCL + zhWikiTitle + Constant.NAMETAILE;
            String enName = kclDir.getEnByZh(zhWikiTitle);
            System.out.println(enName);
            return enName;
        } else {
            System.out.println("获取候选英文词条");
            Connection zhconne = opMysql.connSQL(Contant.zhmysqlurl);
            Connection enconne = opMysql.connSQL(Contant.enmysqlurl);
            ZhBaike zhBaike = new ZhBaike(new BaikeParser(zhWikiTitle, kclDir, kclCatDir, Language.CHINESE));
            //获取中文词条的category对应的英文词条
//            ArrayList<String> En_MDIc_ZhCategoryName = getEnCategoryName(zhBaike.getKCLcat());
            //获取中文词条的outlink对应的英文词条
//            ArrayList<String> En_MDIc_ZhOutlinkName = getEnOutlinkName(zhBaike.getKCLout());
            //获取一个中文词条中inlinks中在MDic中能找到的对应英文词条name
            ArrayList<String> En_MDIc_ZhPageName = getEnInLinkPageName(zhBaike.getKCLin());

            //基于相等的inlinks查找出候选英文名称
            HashMap<String, Integer> semi_candidateEnName_inlink = getCandidateEnName(En_MDIc_ZhPageName, firstcloumn, secondcloumn, thirdcloumn);

            //基于inlinks获取top1000的候选集
            TreeMap<String, Integer> keySet = top500CandidateEnName(semi_candidateEnName_inlink, 25);
            Set<String> candidateEns = keySet.keySet();
            FileWriter fwTrueRanksvm_noCoherence1 = new FileWriter(outputpath1);
            FileWriter fwTrueRanksvm_noCoherence2 = new FileWriter(outputpath2);
            for (String enName : candidateEns) {
                EnBaike enBaike = new EnBaike(new BaikeParser(enName, kclDir, kclCatDir, Language.ENGLISH));
                FeatureParser parser = new FeatureParser(zhBaike, enBaike, E_CDic);
                double in_rate = parser.getIn_rate();
                double out_rate = parser.getOut_rate();
                double cat_rate = parser.getCat_rate();
                double inQujian = parser.getIn_qujian();
                double outQujian = parser.getOut_qujian();
                double catQujian = parser.getCat_qujian();
                double in_coh = parser.getIn_co();
                double out_coh = parser.getOut_co();
                double cat_coh = parser.getCat_co();
                double desScore = parser.getDesScore();
                double textScore = parser.getTextScore();
                double desOutlinkScore = parser.getDesOutlinkScore();
                fwTrueRanksvm_noCoherence1.write(enName + "\n");
                fwTrueRanksvm_noCoherence2.write("0" + " " + "qid:1"
                        + " " + "1:" + out_rate + " " + "2:"
                        + cat_rate + " " + "3:" + outQujian
                        + " " + "4:" + catQujian + " " + "5:"
                        + out_coh + " " + "6:"
                        + cat_coh + " " + "7:" + desScore
                        + " " + "8:" + textScore + " " + "9:" + desOutlinkScore + "\n");

                fwTrueRanksvm_noCoherence1.flush();
                fwTrueRanksvm_noCoherence2.flush();
            }
            fwTrueRanksvm_noCoherence1.close();
            fwTrueRanksvm_noCoherence2.close();

            /**
             * 8.29添加针对单个查询词的操作
             */
//            String DirPath = "C:\\youngsu\\PracticedMyHands\\Project_EntityLinking\\output\\QC_WE\\test_pre\\feature";

        }
        return "";
    }

    private ArrayList<String> getEnInLinkPageName(List<KCLpair> kcLin) {
        ArrayList<String> res = new ArrayList<String>();
        for (KCLpair pair : kcLin) {
            res.add(pair.getEnname());
        }
        return res;
    }

    /**
     * 8.29添加
     */

    //将文件夹下的所有feature文件用svm训练，生成相对应的预测结果集，（需要在写一个函数，将这些预测候选结果集合中的正例选出来，并与他们的名称相融合，便于标注数据）
    public static void SVMPredict(String Dir) throws IOException {

        TreeMap<String, String> filesPath = Myutil.readfileInDic(Dir);  //key为全路径，value为file的名字
        Iterator<String> iterator = filesPath.keySet().iterator();
        while (iterator.hasNext()) {

            String absolutPath = iterator.next();
            String filename = filesPath.get(absolutPath);
            String input = absolutPath;
            String[] file = filename.split("_feature");
            System.out.println(filename);
            String file_pre = file[0];
            String file_after = file[1];
            filename = file_pre + "_name" + file_after;
//            String output = "C:\\youngsu\\PracticedMyHands\\Project_EntityLinking\\output\\QC_WE\\test_result\\" + filename;
            String output = Constant.QCWE_PREDICT + filename;

            String cmd2 = "svm_rank_classify " + input + " " + Constant.RSVMMODEL + " " + output;
            System.out.println(cmd2);
            try {
                Runtime.getRuntime().exec(cmd2);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String writeSVMZhengQueNames(String inputName) throws IOException {
        String outputPath = Constant.QCWE_PREDICTNAME;
        String namePath = Constant.QCWE_NAME;
        String predictPath = Constant.QCWE_PREDICT;

        TreeMap<String, String> nameFilesPath = Myutil.readfileInDic(namePath);  //key为全路径，value为file的名字
        TreeMap<String, String> predicFilesPath = Myutil.readfileInDic(predictPath);  //key为全路径，value为file的名字

        System.out.println("eds" + nameFilesPath);
        Iterator<String> iteratorName = nameFilesPath.keySet().iterator();
        while (iteratorName.hasNext()) {
            String absolutPath = iteratorName.next();//文件的全路径
            String filename = nameFilesPath.get(absolutPath); //文件的名字


            while (!predicFilesPath.containsValue(inputName + "_0" + Constant.NAMETAILE)) {
                predicFilesPath = Myutil.readfileInDic(predictPath);
                System.out.println("2345" + predicFilesPath);
            }
            Iterator<String> iteratorPredict = predicFilesPath.keySet().iterator();
            System.out.println("444" + predicFilesPath);
            while (iteratorPredict.hasNext()) {
                String abPath = iteratorPredict.next();
                String fname = predicFilesPath.get(abPath);
                System.out.println("111" + filename);
                System.out.println("222" + inputName);
                System.out.println("333" + fname);
                if (filename.equals(fname) && filename.split("_0_")[0].equals(inputName)) { //如果两个文件夹下的文件名字相同
                    String output = outputPath + fname;
                    ArrayList<String> namefileWords = Myutil.readByLine(absolutPath);
                    ArrayList<String> predictfileWords = Myutil.readByLine(abPath);

                    String result = writeFirstOneFileResult(namefileWords, predictfileWords);
                    return result;
                }
            }
        }
        return "eeror";

    }

    private static String writeFirstOneFileResult(ArrayList<String> namefileWords, ArrayList<String> predictfileWords) throws IOException {

        ArrayList<Double> predictScore = new ArrayList<Double>();
        for (String predictWord : predictfileWords) {
            double score = Double.parseDouble(predictWord);
            predictScore.add(score);
        }
        HashMap<String, Double> input = new HashMap<String, Double>();
        for (int i = 0; i < namefileWords.size(); i++) {
            String key = namefileWords.get(i);
            double value = predictScore.get(i);
            input.put(key, value);
        }


        List<Map.Entry<String, Double>> result = sort(input);
        if (result.size() > 0) {
            return result.get(0).getKey().toString();
        } else {
            return "error";
        }
    }

    private static List<Map.Entry<String, Double>> sort(Map<String, Double> map_Data) {

        List<Map.Entry<String, Double>> list_Data = new ArrayList<Map.Entry<String, Double>>(map_Data.entrySet());
        System.out.println(list_Data.size());
        System.setProperty("java.edu.util.Arrays.useLegacyMergeSort", "true");
        Collections.sort(list_Data, new Comparator<Map.Entry<String, Double>>() {
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                if (o2.getValue() != null && o1.getValue() != null && o2.getValue().compareTo(o1.getValue()) > 0) {
                    return 1;
                } else {
                    return -1;
                }

            }
        });

        System.out.println(list_Data);
        return list_Data;

    }

    public int getZhCategoryCount(String zhWikiTitle) throws SQLException {
        Connection conn = opMysql.connSQL(Contant.zhmysqlurl);
        String selectCountSql = "SELECT count(*) FROM zhwiki.category_pages as a,zhwiki.pagemapline as b where a.pages=b.pageId and b.name='" + zhWikiTitle + "';";
        ResultSet resultSet = opMysql.selectSQL(conn, selectCountSql);
        int count = 0;
        while (resultSet.next()) {
            count = resultSet.getInt(1);
        }
        conn.close();
        return count;

    }

    public int getZhOutlinkCount(String zhWikiTitle) throws SQLException {
        Connection conn = opMysql.connSQL(Contant.zhmysqlurl);
        String selectCountSql = "SELECT count(*) FROM zhwiki.page_outlinks as a,zhwiki.pagemapline as b where a.id = b.pageId and b.name='" + zhWikiTitle + "';";
        ResultSet resultSet = opMysql.selectSQL(conn, selectCountSql);
        int count = 0;
        while (resultSet.next()) {
            count = resultSet.getInt(1);
        }
        conn.close();
        return count;
    }

    public int getZhInlinkCount(String zhWikiTitle) throws SQLException {
        Connection conn = opMysql.connSQL(Contant.zhmysqlurl);
        String selectCountSql = "SELECT count(*) FROM zhwiki.page_inlinks as a,zhwiki.pagemapline as b where a.id = b.pageId and b.name='" + zhWikiTitle + "';";
        ResultSet resultSet = opMysql.selectSQL(conn, selectCountSql);
        int count = 0;
        while (resultSet.next()) {
            count = resultSet.getInt(1);
        }
        conn.close();
        return count;
    }

    private TreeMap<String, Integer> top500CandidateEnName(final HashMap<String, Integer> semi_candidateEnName_inlink, int num) {
        ArrayList<String> keys = new ArrayList(semi_candidateEnName_inlink.keySet());//得到key集合
        TreeMap<String, Integer> result = new TreeMap<String, Integer>();
        //把keys排序，但是呢，要按照后面这个比较的规则
        Collections.sort(keys, new Comparator<Object>() {


                    public int compare(Object o1, Object o2) {

                        //按照value的值降序排列，若要升序，则这里小于号换成大于号
                        if (Double.parseDouble(semi_candidateEnName_inlink.get(o1).toString()) < Double.parseDouble(semi_candidateEnName_inlink.get(o2).toString())) {
                            return 1;
                        } else if (Double.parseDouble(semi_candidateEnName_inlink.get(o1).toString()) == Double.parseDouble(semi_candidateEnName_inlink.get(o2).toString()))
                            return 0;

                        else
                            return -1;
                    }
                }
        );

        if (keys.size() < num) {
            for (String key : keys) {
                int value = semi_candidateEnName_inlink.get(key);
                result.put(key, value);
            }

            return result;
        } else {

            for (int i = 0; i < num; i++) {
                int value = semi_candidateEnName_inlink.get(keys.get(i));
                result.put(keys.get(i), value);

            }
            return result;
        }

    }

    private HashMap<String, Integer> getCandidateEnName(ArrayList<String> en_mdIc_zhPageName, String firstcloumn, String secondcloumn, String thirdcloumn) throws SQLException {
        Connection conn = opMysql.connSQL(Contant.enmysqlurl);
        HashMap<String, Integer> result = new HashMap<String, Integer>();
        for (String mdicName : en_mdIc_zhPageName) {
            ArrayList<String> names = getcandidateName(conn, mdicName, firstcloumn, secondcloumn, thirdcloumn);
            if (names != null) {
                for (String name : names) {
                    if (!result.containsKey(name)) {
                        int count = 1;
                        result.put(name, count);
                    } else {
                        int count = result.get(name);
                        count += 1;
                        result.put(name, count);
                    }
                }
            }

        }
        return result;
    }

    private ArrayList<String> getcandidateName(Connection conn, String mdicName, String firstcloumn, String secondcloumn, String thirdcloumn) throws SQLException {
        ArrayList<String> result = new ArrayList<String>();
        if (!mdicName.contains("'")) {
            String sql = "SELECT " + firstcloumn + " FROM enwiki." + secondcloumn + " where " + thirdcloumn + "='" + mdicName + "';";
            ResultSet resultSet = opMysql.selectSQL(conn, sql);
            while (resultSet.next()) {

                String pageName = resultSet.getString(firstcloumn);
                if (!pageName.contains("\'")) {
                    result.add(pageName);
                }
            }
            return result;
        }
        return null;

    }

}
