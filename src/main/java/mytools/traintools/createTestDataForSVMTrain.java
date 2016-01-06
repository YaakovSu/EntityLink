package mytools.traintools;

import edu.nlp.ECDic;
import edu.util.Contant;
import edu.util.Myutil;
import edu.util.opMysql;
import rsvm.qc2wc;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by CBD_O9 on 2014-11-29.
 * 基于svm的model将测试数据中的education表中的数据蓉svm进行训练，并将最后的正例结果进行输出。然后进行人工标注。
 */
public class createTestDataForSVMTrain {

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        createTrainningData();
        String DirPath = "C:\\youngsu\\PracticedMyHands\\Project_EntityLinking\\output\\testTop500_SVM\\test_pre\\feature";

        SVMPredict(DirPath);

        writeSVMZhengQueNames();

        String dir_feature = "C:\\youngsu\\PracticedMyHands\\Project_EntityLinking\\output\\testTop500_SVM\\test_pre\\feature";
        String dir_name = "C:\\youngsu\\PracticedMyHands\\Project_EntityLinking\\output\\testTop500_SVM\\test_pre\\name";
        String dir_predict = "C:\\youngsu\\PracticedMyHands\\Project_EntityLinking\\output\\testTop500_SVM\\test_result";
        String outputPath1 = "C:\\youngsu\\PracticedMyHands\\Project_EntityLinking\\output\\testTop500_SVM\\final_result";
        String outputPath2 = "C:\\youngsu\\PracticedMyHands\\Project_EntityLinking\\output\\testTop500_SVM\\final_result2";

        RankCandidateZhengQue_pre(dir_feature, dir_name, dir_predict, outputPath1);

        RankCandidateZhengQue_feature8Clean(dir_feature, dir_name, dir_predict, outputPath2);

    }

    private static void RankCandidateZhengQue_feature8Clean(String dir_feature, String dir_name, String dir_predict, String outputPath1) throws IOException {
        TreeMap<String, String> featurefilesPath = Myutil.readfileInDic(dir_feature);  //key为全路径，value为file的名字

//        System.out.println(filesPath);
        Iterator<String> iterator = featurefilesPath.keySet().iterator();
        while (iterator.hasNext()) {

            String absolutPath = iterator.next();
            String featureFilename = featurefilesPath.get(absolutPath);

//            System.out.println("11111 "+filename);

            String input = absolutPath;
            String[] file = featureFilename.split("_feature");
            String file_pre = file[0];
            String file_after = file[1];
            String nameFileName = file_pre + "_name" + file_after;
            String featureUrl = dir_feature + "\\" + featureFilename;
            String nameUrl = dir_name + "\\" + nameFileName;
            String output = outputPath1 + "\\" + nameFileName; //输出文件的名字
            FileWriter fw = new FileWriter(output);
            ArrayList<String> featureList = Myutil.readByLine(featureUrl); //里面存的是一个文件内的所有候选feature
            ArrayList<String> nameList = Myutil.readByLine(nameUrl);   //里面存的是上面feature对应的name文件的所有名称
            ArrayList<String> featureScore = new ArrayList<String>();
            HashMap<String, Double> result = new HashMap<String, Double>();
            boolean hasNonZero = false;

            for (int i = 0; i < featureList.size(); i++) {
                String feature = featureList.get(i);
                String[] semiScores = feature.split(" ");
                String score8 = semiScores[8].split(":")[1];
                double score = Double.parseDouble(score8);
                if (score > 0) {
                    hasNonZero = true;
                }
            }

            if (hasNonZero) {
                for (int i = 0; i < featureList.size(); i++) {
                    String feature = featureList.get(i);
                    String[] semiScores = feature.split(" ");
                    String score1 = semiScores[1].split(":")[1];
                    String score2 = semiScores[2].split(":")[1];
                    String score3 = semiScores[3].split(":")[1];
                    String score8 = semiScores[8].split(":")[1];
                    double sc = Double.parseDouble(score8);
                    if (sc > 0) {
                        double score = Double.parseDouble(score1) + Double.parseDouble(score2) + Double.parseDouble(score3);
                        String name = nameList.get(i);
                        result.put(name, score);
                    }


                }
            }
//            else{
//
//                for(int i=0;i<featureList.size();i++){
//                    String feature = featureList.get(i);
//                    String[] semiScores = feature.split(" ");
//                    String score1 = semiScores[1].split(":")[1];
//                    String score2 = semiScores[2].split(":")[1];
//                    String score3 = semiScores[3].split(":")[1];
//                    double score = Double.parseDouble(score1)+Double.parseDouble(score2)+Double.parseDouble(score3);
//                    String name = nameList.get(i);
//                    result.put(name,score);
//                }
//
//            }


            List<Map.Entry<String, Double>> infoIds = Myutil.sort(result);
            for (int i = 0; i < infoIds.size(); i++) {
                String id = infoIds.get(i).toString();
                System.out.println(id);
                fw.write(id + "\n");
            }
            fw.close();

        }
    }

    private static void RankCandidateZhengQue_pre(String dir_feature, String dir_name, String dir_predict, String outputPath1) throws IOException {

        TreeMap<String, String> featurefilesPath = Myutil.readfileInDic(dir_feature);  //key为全路径，value为file的名字

//        System.out.println(filesPath);
        Iterator<String> iterator = featurefilesPath.keySet().iterator();
        while (iterator.hasNext()) {

            String absolutPath = iterator.next();
            String featureFilename = featurefilesPath.get(absolutPath);

//            System.out.println("11111 "+filename);

            String input = absolutPath;
            String[] file = featureFilename.split("_feature");
            String file_pre = file[0];
            String file_after = file[1];
            String nameFileName = file_pre + "_name" + file_after;
            String featureUrl = dir_feature + "\\" + featureFilename;
            String nameUrl = dir_name + "\\" + nameFileName;
            String output = outputPath1 + "\\" + nameFileName; //输出文件的名字
            FileWriter fw = new FileWriter(output);
            ArrayList<String> featureList = Myutil.readByLine(featureUrl); //里面存的是一个文件内的所有候选feature
            ArrayList<String> nameList = Myutil.readByLine(nameUrl);   //里面存的是上面feature对应的name文件的所有名称
            ArrayList<String> featureScore = new ArrayList<String>();
            HashMap<String, Double> result = new HashMap<String, Double>();
            for (int i = 0; i < featureList.size(); i++) {
                String feature = featureList.get(i);
                String[] semiScores = feature.split(" ");
                String score1 = semiScores[1].split(":")[1];
                String score2 = semiScores[2].split(":")[1];
                String score3 = semiScores[3].split(":")[1];

                double score = Double.parseDouble(score1) + Double.parseDouble(score2) + Double.parseDouble(score3);
                String name = nameList.get(i);
                result.put(name, score);
            }

            List<Map.Entry<String, Double>> infoIds = Myutil.sort(result);
            for (int i = 0; i < infoIds.size(); i++) {
                String id = infoIds.get(i).toString();
                System.out.println(id);
                fw.write(id + "\n");
            }
            fw.close();

        }
    }


    public static void createTrainningData() throws SQLException, IOException, ClassNotFoundException {
        Connection conn = opMysql.connSQL(Contant.testmysqlurl);
//        String selectSql = "SELECT * FROM testwiki.testbaike_edu where flag != 0 and flag!=20 ;";
        String selectSql = "SELECT * FROM testwiki.testbaike_food where flag = 1 ;";
        ResultSet resultSet = opMysql.selectSQL(conn, selectSql);
        qc2wc cw = new qc2wc();
        ECDic ecDic = new ECDic(Contant.E_CdicPath);
        while (resultSet.next()) {
//            count++;
//            if(count>554){

            String name = resultSet.getString("title");
            String des = resultSet.getString("des");
            System.out.println("现在执行到 " + name);
            cw.writeOneNameSVMtrainningData(name, des, ecDic);
//            }

        }

    }

    public static HashMap<String, HashSet<String>> getEnCandidateNameKeyWords(String e_cdicPath) {
        HashMap<String, HashSet<String>> result = new HashMap<String, HashSet<String>>();
        ArrayList<String> semi_E_Cdic = Myutil.readByLine(e_cdicPath);
        HashSet<String> E_Cdic = new HashSet<String>();
        for (String word : semi_E_Cdic) {
            E_Cdic.add(word);
        }


        for (String line : E_Cdic) {
            HashSet<String> enCandidateNameWords = new HashSet<String>();
            String[] text = line.split("=");
            String zhName = text[0];
            String semi_enName = text[1];
            ArrayList<String> enKeyWords = getCandidateWords(semi_enName);
            for (String enKeyWord : enKeyWords) {
                if (!enCandidateNameWords.contains(enKeyWord)) {
                    enCandidateNameWords.add(enKeyWord);
                }
            }
            result.put(zhName, enCandidateNameWords);

        }
        return result;
    }

    private static ArrayList<String> getCandidateWords(String semi_enName) {
        ArrayList<String> keywords = new ArrayList<String>();
        //先提取英文字符
        String enNames = Myutil.CleanENText(semi_enName);
        String[] names = enNames.split(", ");
        for (String name : names) {
            String[] keys = name.split("] ");
            for (String key : keys) {
                if (!key.isEmpty()) {

                    if (!keywords.contains(key)) {
                        key = key.replace(" ", "_");
                        keywords.add(key);
                    }
                }
            }
        }
        return keywords;

    }


    //将文件夹下的所有feature文件用svm训练，生成相对应的预测结果集，（需要在写一个函数，将这些预测候选结果集合中的正例选出来，并与他们的名称相融合，便于标注数据）
    public static void SVMPredict(String Dir) throws IOException {

        TreeMap<String, String> filesPath = Myutil.readfileInDic(Dir);  //key为全路径，value为file的名字

//        System.out.println(filesPath);
        Iterator<String> iterator = filesPath.keySet().iterator();
        while (iterator.hasNext()) {

            String absolutPath = iterator.next();
            String filename = filesPath.get(absolutPath);

//            System.out.println("11111 "+filename);

            String input = absolutPath;
            String[] file = filename.split("_feature");
            String file_pre = file[0];
            String file_after = file[1];
            filename = file_pre + "_name" + file_after;

//            System.out.println("22222 "+filename);
//
//            System.out.println("333333 "+input);
//            System.exit(0);


            String output = "C:\\youngsu\\PracticedMyHands\\Project_EntityLinking\\output\\testTop500_SVM\\test_result\\" + filename;

            String cmd2 = "svm-predict " + input + " C:\\youngsu\\libsvm-3.20\\windows\\9featureModel " + output;
            try {
//            Runtime.getRuntime().exec(cmd1);
                Runtime.getRuntime().exec(cmd2);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        String cmd2 = "svm-predict C:\\youngsu\\test1.output C:\\youngsu\\libsvm-3.20\\windows\\train.output.model C:\\youngsu\\libsvm-3.20\\windows\\result.output";

    }


    public static void writeSVMZhengQueNames() throws IOException {
        String outputPath = "Project_EntityLinking/output/testTop500_SVM/testSVMZhengQueNames";
        String namePath = "Project_EntityLinking/output/testTop500_SVM/test_pre/name";
        String predictPath = "Project_EntityLinking/output/testTop500_SVM/test_result";


        TreeMap<String, String> nameFilesPath = Myutil.readfileInDic(namePath);  //key为全路径，value为file的名字
        TreeMap<String, String> predicFilesPath = Myutil.readfileInDic(predictPath);  //key为全路径，value为file的名字

        Iterator<String> iteratorName = nameFilesPath.keySet().iterator();
        while (iteratorName.hasNext()) {
            String absolutPath = iteratorName.next();//文件的全路径
            String filename = nameFilesPath.get(absolutPath); //文件的名字
            Iterator<String> iteratorPredict = predicFilesPath.keySet().iterator();
            while (iteratorPredict.hasNext()) {
                String abPath = iteratorPredict.next();
                String fname = predicFilesPath.get(abPath);
                if (filename.equals(fname)) { //如果两个文件夹下的文件名字相同
                    String output = outputPath + "/" + fname;
                    FileWriter fw = new FileWriter(output);
                    ArrayList<String> namefileWords = Myutil.readByLine(absolutPath);
                    ArrayList<String> predictfileWords = Myutil.readByLine(abPath);
                    for (int i = 0; i < namefileWords.size(); i++) {
                        if (predictfileWords.get(i).equals("1")) {
                            fw.write(namefileWords.get(i) + "\n");
                        }
                        fw.flush();
                    }
                    fw.close();
                }
            }
        }

    }


}
