package rsvm;

import edu.Constant;
import edu.nlp.CandidateDic;
import edu.nlp.DirectDic;
import edu.nlp.DisamDic;
import edu.nlp.ECDic;
import edu.util.Contant;
import edu.util.Myutil;
import edu.util.opMysql;


import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static edu.nlp.Rank.rankTitles;


/**
 * Created by youngsu on 14-10-29.
 */
public class qc2wc{
    //name 为file中要查看候选
    public ArrayList<String> createCandidate_wc(String fileContent, String name) throws SQLException {
        fileContent = fileContent.replaceAll("\\d+", "");
        String pattern = "([-+*/^()\\]\\[])";
        fileContent = fileContent.replaceAll(pattern, "");
        fileContent = fileContent.replace(".", "");
        fileContent = Myutil.getCleanText(fileContent);

//        CandidateDic dic = new CandidateDic(Contant.combin_disambigation_redirectOutput_Path);
        DirectDic directDic = new DirectDic(Contant.redirectOutput_Path);
        DisamDic disamDic = new DisamDic(Contant.clean_disambigationOutput_Path);
        ArrayList<String> wcresult = new ArrayList<String>();
        if (disamDic.containsKey(name)) {
            System.out.println("在消歧义中直接出现");
            ArrayList<String> titles = disamDic.getDisamPage(name);
            ArrayList<String> ranktitles = rankTitles(titles, fileContent);  //对name的候选词条排序后的结果
            wcresult = getranktitles(ranktitles, Contant.CandidatePageNum); //取4个作为wc ->we的候选
        } else if (directDic.containGivenWord_Fuzzy(name)) {
            System.out.println("在重定向中出现");
            ArrayList<String> directNames = directDic.getRedirectPage(name);
//            for (String directName : directNames) {
            String directName = directNames.get(0);
            if (disamDic.containsKey(directName)) {
                System.out.println("且在消歧义中出现");
                ArrayList<String> titles = disamDic.getDisamPage(directName);
                ArrayList<String> finalTitles = new ArrayList<String>();
                for (String title : titles) {
                    if (directDic.containGivenWord(title)) {
                        finalTitles.add(directDic.getRedirectPage(title).get(0));
                    } else {
                        finalTitles.add(title);
                    }
                }
                System.out.println(titles);
                System.out.println(finalTitles);

                ArrayList<String> ranktitles = rankTitles(finalTitles, fileContent);  //对name的候选词条排序后的结果
                System.out.println(ranktitles);
                wcresult = getranktitles(ranktitles, Contant.CandidatePageNum); //取4个作为wc ->we的候选
            } else {
                System.out.println("在重定向中存在，然而却没有歧义");
                wcresult = useRSVM(directName, fileContent);
            }
//            }
        } else {
            wcresult = useRSVMfuzzy(name, fileContent);
        }
        return wcresult;
    }

    private ArrayList<String> useRSVM(String name, String fileContent) throws SQLException {

        ArrayList<String> wcresult = new ArrayList<String>();
        ArrayList<String> titles = new ArrayList<String>();
        String mehuChaxunSql = "SELECT * FROM zhwiki.pagemapline where name like '" + name + "';";
        Connection conn = opMysql.connSQL(Contant.zhmysqlurl);
        ResultSet resultSet = opMysql.selectSQL(conn, mehuChaxunSql);
        while (resultSet.next()) {
            String sqlname = resultSet.getString("name");
            titles.add(sqlname);
        }
        opMysql.deconnSQL(conn);
        if (!titles.isEmpty()) {
            ArrayList<String> ranktitles = rankTitles(titles, fileContent);
            wcresult = getranktitles(ranktitles, Contant.CandidatePageNum); //取4个作为wc ->we的候选
        } else {
            wcresult.add("查无此词");
        }
        return wcresult;

    }

    private ArrayList<String> useRSVMfuzzy(String name, String fileContent) throws SQLException {
        System.out.println("在重定向和消歧义中都没有,启动RSVM模型");
        ArrayList<String> wcresult = new ArrayList<String>();
        ArrayList<String> titles = new ArrayList<String>();
        String mehuChaxunSql = "SELECT * FROM zhwiki.pagemapline where name like '%" + name + "%';";
        Connection conn = opMysql.connSQL(Contant.zhmysqlurl);
        ResultSet resultSet = opMysql.selectSQL(conn, mehuChaxunSql);
        while (resultSet.next()) {
            String sqlname = resultSet.getString("name");
            titles.add(sqlname);
        }
        opMysql.deconnSQL(conn);
        if (!titles.isEmpty()) {
            ArrayList<String> ranktitles = rankTitles(titles, fileContent);
            wcresult = getranktitles(ranktitles, Contant.CandidatePageNum); //取4个作为wc ->we的候选
        } else {
            wcresult.add("查无此词");
        }
        return wcresult;

    }

    private ArrayList<String> getranktitles(ArrayList<String> ranktitles, int num) {
        ArrayList<String> result = new ArrayList<String>();
        int size = ranktitles.size();
        if (size <= num) {
            for (String ranktitle : ranktitles) {

                System.out.println(ranktitle);
                result.add(ranktitle);
            }
        } else {
            for (int i = 0; i < num; i++) {
                System.out.println(ranktitles.get(i));
                result.add(ranktitles.get(i));
            }
        }
        return result;
    }


    public TreeMap<String,String> writeOneNameSVMtrainningData(String name, String filecontent, ECDic E_CDic) throws SQLException, IOException, ClassNotFoundException {


        long timeTestStart = System.currentTimeMillis();

        boolean pre_inDic = MDicContain(name);
        TreeMap<String, String> enNames = new TreeMap<String, String>();
        if (pre_inDic) {
            String outputPath = Constant.QCWEinKCL + name + Constant.NAMETAILE;
            String enName = getEnNameInMDic(name);
            printenName(enName, outputPath);
            enNames.put(name,enName);
            return enNames;
        }
        ArrayList<String> results = createCandidate_wc(filecontent, name);

        for (String chinese : results) {
            enNames.put(chinese, "");
        }

//        因为我把rank改掉了，所以这里的result有score信息在。
        System.out.println(results);
        boolean inDic = false;
        if (results.size() > 0) {

            for (int i = 0; i < results.size(); i++) {
                String zhWikiTitle = results.get(i).split("\t")[0];
                inDic = MDicContain(zhWikiTitle);
                if (inDic) {
                    String outputPath = Constant.QCWEinKCL + zhWikiTitle + Constant.NAMETAILE;
                    String enName = getEnNameInMDic(zhWikiTitle);
                    printenName(enName, outputPath);
                    enNames.put(zhWikiTitle,enName);
//                    return enName;
                }
                if (!inDic) {
//                    for (int i = 0; i < results.size(); i++) {
                    String result = results.get(i).split("\t")[0];
                    if (!result.equals("查无此词")) {

                        wc2we f2 = new wc2we();
                        String output1 = Constant.QCWE_NAME + name + "_" + i + Constant.NAMETAILE;
                        String output2 = Constant.QCWE_FEATURE + name + "_" + i + Constant.FEATURETAILE;

                         f2.myFangAn(result, output1, output2, E_CDic, name);
//                        return enName;

                    }
//                    }
                }
            }
            String DirPath = Constant.QCWE_FEATURE;
            for (int i = 0; i < results.size(); i++) {
                String result = results.get(i).split("\t")[0];
                SVMPredict(DirPath);
                String enName = writeSVMZhengQueNames(name, i);
                System.out.println("gaga" + result + " " + enName);
                if(!enName.equals("eeror")){
                    enNames.put(result, enName);
                }

            }

        }
        long timeTestEnd = System.currentTimeMillis();
        System.out.println("运行时间是" + (timeTestEnd - timeTestStart));
        return enNames;
    }


    private String getEnNameInMDic(String zhWikiTitle) {

        ArrayList<String> lines = Myutil.readByLine(Contant.MDicInputPath);

        for (String line : lines) {

            String[] name = line.split("=");
            String enName = name[0];
            String zhName = name[1];
            if (zhName.equals(zhWikiTitle)) {
                return enName;
            }
        }

        return "";
    }


    private boolean MDicContain(String zhWikiTitle) {
        ArrayList<String> lines = Myutil.readByLine(Contant.MDicInputPath);
        for (String line : lines) {
            String[] name = line.split("=");
            String enName = name[0];
            String zhName = name[1];
            if (zhName.equals(zhWikiTitle)) {

                return true;
            }
        }

        return false;
    }

    private void printenName(String enName, String output) throws IOException {

        FileWriter fw = new FileWriter(output);
        fw.write(enName + "\n");
        fw.flush();
        fw.close();
    }


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

    public static String writeSVMZhengQueNames(String inputName,int i) throws IOException {
        String outputPath = Constant.QCWE_PREDICTNAME;
        String namePath = Constant.QCWE_NAME;
        String predictPath = Constant.QCWE_PREDICT;


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TreeMap<String, String> nameFilesPath = Myutil.readfileInDic(namePath);  //key为全路径，value为file的名字
        TreeMap<String, String> predicFilesPath = Myutil.readfileInDic(predictPath);  //key为全路径，value为file的名字

        System.out.println("eds" + nameFilesPath);
        Iterator<String> iteratorName = nameFilesPath.keySet().iterator();
        while (iteratorName.hasNext()) {
            String absolutPath = iteratorName.next();//文件的全路径
            String filename = nameFilesPath.get(absolutPath); //文件的名字

            int count = 0;
            while (!predicFilesPath.containsValue(inputName + "_"+i+ Constant.NAMETAILE)) {
                count++;
                predicFilesPath = Myutil.readfileInDic(predictPath);
                System.out.println("pp "+inputName + "_"+i+ Constant.NAMETAILE);
                System.out.println("2345" + predicFilesPath);
                if(count>100){
                    break;
                }
            }

            Iterator<String> iteratorPredict = predicFilesPath.keySet().iterator();
            System.out.println("444" + predicFilesPath);
            while (iteratorPredict.hasNext()) {
                String abPath = iteratorPredict.next();
                String fname = predicFilesPath.get(abPath);
                System.out.println("111" + filename);
                System.out.println("222" + inputName);
                System.out.println("333" + fname);
                if (filename.equals(fname) && filename.split("_"+i+"_")[0].equals(inputName)) { //如果两个文件夹下的文件名字相同
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

}
