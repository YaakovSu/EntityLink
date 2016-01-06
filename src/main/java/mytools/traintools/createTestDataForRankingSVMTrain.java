package mytools.traintools;

import edu.nlp.ECDic;
import rsvm.qc2wc;
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
 * Created by CBD_O9 on 2014-12-07.
 */
public class createTestDataForRankingSVMTrain {
    static String guliNodesPath = "C:\\youngsu\\lijuanziData\\guliNode.tsv";

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        createTrainningData();
        String DirPath = "C:\\youngsu\\PracticedMyHands\\Project_EntityLinking\\\\output_guli_MDic\\testTop500_RankSVM_noCoherence\\test_pre\\feature";
//
        SVMPredict(DirPath);
////
        writeSVMZhengQueNames();

    }

    public static void createTrainningData() throws SQLException, IOException, ClassNotFoundException {


        ArrayList<String> guliNodes = Myutil.readByLine(guliNodesPath);
        HashMap<String, ArrayList<String>> guli = new HashMap<String, ArrayList<String>>();
        for (String line : guliNodes) {
            String[] words = line.split("\t");
            String zhName = words[0];
            String enName = words[1];
            String tag = words[2];
            ArrayList<String> values = new ArrayList<String>();
            values.add(enName);
            values.add(tag);
            guli.put(zhName, values);
        }

        Connection conn = opMysql.connSQL(Contant.testmysqlurl);
        String selectSql = "SELECT * FROM testwiki.testbaike_person where flag = 1 ;";
        ResultSet resultSet = opMysql.selectSQL(conn, selectSql);
        qc2wc cw = new qc2wc();
        ECDic ecDic = new ECDic(Contant.E_CdicPath);

        int count = 0;
        while (resultSet.next()) {
            String name = resultSet.getString("title");

            if (guli.containsKey(name)) {
                ArrayList<String> value = guli.get(name);
                if (value.get(1).equals("0")) {
                    count++;
                    String des = resultSet.getString("des");
                    System.out.println("现在执行到 " + name + " 第" + count + "条！");
                    cw.writeOneNameSVMtrainningData(name, des, ecDic);

                }

            }


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
//            System.out.println("gagaa "+filename);
            String[] file = filename.split("_feature");
            String file_pre = file[0];
            String file_after = file[1];
            filename = file_pre + "_name" + file_after;

//            System.out.println("22222 "+filename);
//
//            System.out.println("333333 "+input);
//            System.exit(0);


            String output = "C:\\youngsu\\PracticedMyHands\\Project_EntityLinking\\output_guli\\testTop500_RankSVM_noCoherence\\test_result\\" + filename;

//            String cmd1 = "svm_rank_classify "+"C:\\youngsu\\PracticedMyHands\\Project_EntityLinking\\output\\testTop500_RankSVM\\test_pre\\feature\\test.output"+" C:\\youngsu\\svm_rank_windows\\mode "+"C:\\youngsu\\PracticedMyHands\\Project_EntityLinking\\output\\testTop500_RankSVM\\test_result\\pre.output";

            String cmd2 = "svm_rank_classify " + input + " C:\\youngsu\\libsvm-3.20\\windows\\noCoherenceModel " + output;
            System.out.println(cmd2);
            try {
//                  Runtime.getRuntime().exec(cmd1);
                Runtime.getRuntime().exec(cmd2);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        String cmd2 = "svm-predict C:\\youngsu\\test1.output C:\\youngsu\\libsvm-3.20\\windows\\train.output.model C:\\youngsu\\libsvm-3.20\\windows\\result.output";

    }


    public static void writeSVMZhengQueNames() throws IOException {
        String outputPath = "Project_EntityLinking/output_guli/testTop500_RankSVM_noCoherence/testSVMZhengQueNames";
        String namePath = "Project_EntityLinking/output_guli/testTop500_RankSVM_noCoherence/test_pre/name";
        String predictPath = "Project_EntityLinking/output_guli/testTop500_RankSVM_noCoherence/test_result";


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

                    writeOneFileResult(namefileWords, predictfileWords, fw);


//                    for(int i=0;i<namefileWords.size();i++){
//                        if(predictfileWords.get(i).equals("1")){
//                            fw.write(namefileWords.get(i)+"\n");
//                        }
//                        fw.flush();
//                    }
                    fw.close();
                }
            }
        }

    }

    private static void writeOneFileResult(ArrayList<String> namefileWords, ArrayList<String> predictfileWords, FileWriter fw) throws IOException {

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
        for (int i = 0; i < result.size(); i++) {
            String name = result.get(i).getKey();
            double firstScore = result.get(0).getValue();
            double score = result.get(i).getValue();
            String scoreString = String.valueOf(score);
            if (firstScore > 0.25) {
                fw.write(name + " " + scoreString + "\n");
            } else {
                fw.write("NIL");
                break;
            }


        }


    }

    private static List<Map.Entry<String, Double>> sort(Map<String, Double> map_Data) {


//        Iterator<String> iterator = map_Data.keySet().iterator();
//        while (iterator.hasNext()){
//            String key = iterator.next();
//            double value = map_Data.get(key);
//            System.out.println(key+" "+value);
//        }

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
