package lfg;

import edu.util.Myutil;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by CBD_O9 on 2015-04-19.
 */
public class WriteNodeFunction2AllPairs {

    static String zhInlinkFilePath = "C:\\youngsu\\lijuanziData\\allzhEdge_inlink.tsv";
    static String zhOutlinkFilePath = "C:\\youngsu\\lijuanziData\\allzhEdge_outlink.tsv";
    static String enInlinkFilePath = "C:\\youngsu\\lijuanziData\\allenEdge_inlink.tsv";
    static String enOutlinkFilePath = "C:\\youngsu\\lijuanziData\\allenEdge_outlink.tsv";
    static String MDic = "C:\\youngsu\\lijuanziData\\allData.tsv";

//    static String pcgPath = "C:\\youngsu\\lijuanziData\\PCGraph.tsv";

    static String pcgPath = "C:\\youngsu\\lijuanziData\\guliNode.tsv";
    //    static String outPutPath = "C:\\youngsu\\lijuanziData\\AllpcgNodeFunctionScore2.tsv";
    static String outPutPath = "C:\\youngsu\\lijuanziData\\guliNodeFunction2.tsv";


    public static void main(String[] args) throws SQLException, IOException {
        HashMap<String, ArrayList<String>> zhInlinkMap = CalNodeFunctions.getlinkFile(zhInlinkFilePath);
        HashMap<String, ArrayList<String>> enInlinkMap = CalNodeFunctions.getlinkFile(enInlinkFilePath);

        HashMap<String, ArrayList<String>> zhOutlinkMap = CalNodeFunctions.getlinkFile(zhOutlinkFilePath);
        HashMap<String, ArrayList<String>> enOutlinkMap = CalNodeFunctions.getlinkFile(enOutlinkFilePath);

        HashMap<String, String> myMDic = CalNodeFunctions.getMDic_allData(MDic);
//        HashSet<String> PCGNodes = getAllNode(pcgPath);
//        System.out.println(PCGNodes.size());
        ArrayList<String> guliNodes = Myutil.readByLine(pcgPath);
        FileWriter fw = new FileWriter(outPutPath);
        int count = 0;
        for (String line : guliNodes) {
            String[] nd = line.split("\t");
            String zh = nd[0];
            String en = nd[1];
            WriteNodeFunction(zh, en, zhInlinkMap, enInlinkMap, zhOutlinkMap, enOutlinkMap, myMDic, fw);
            count++;
            fw.flush();
            System.out.println("已完成：" + count);
        }
        fw.close();

//        int count = 0;
//        FileWriter fw = new FileWriter(outPutPath);
//        Iterator<String> iterator = PCGNodes.iterator();
//        while (iterator.hasNext()){
//            String node = iterator.next();
//            String[] zhEn = node.split("\t");
//            String zh = zhEn[0];
//            String en = zhEn[1];
//
//            WriteNodeFunction(zh,en,zhInlinkMap,enInlinkMap,zhOutlinkMap,enOutlinkMap,myMDic,fw);
//            count++;
//            System.out.println("已完成："+count);
//        }
//        fw.close();
    }

    public static HashSet<String> getAllNode(String PCGFilePath) {

        HashSet<String> result = new HashSet<String>();

        ArrayList<String> text = Myutil.readByLine(PCGFilePath);
        for (String line : text) {
            String[] Nodes = line.split("\t\t");
            String node1 = Nodes[0];
            String node2 = Nodes[1];
            if (!result.contains(node1)) {
                result.add(node1);
            }
            if (!result.contains(node2)) {
                result.add(node2);
            }
        }

        return result;


    }

    public static void WriteNodeFunction(String zh, String en, HashMap<String, ArrayList<String>> zhInlinkMap, HashMap<String, ArrayList<String>> enInlinkMap, HashMap<String, ArrayList<String>> zhOutlinkMap, HashMap<String, ArrayList<String>> enOutlinkMap, HashMap<String, String> myMDic, FileWriter fw) throws SQLException, IOException {


        double cateGoryRate = CalNodeFunctions.getCategoryFunction(zh, en);
        double inRate = CalNodeFunctions.getlinkFunction(zh, en, zhInlinkMap, enInlinkMap, myMDic);
        double outRate = CalNodeFunctions.getlinkFunction(zh, en, zhInlinkMap, enInlinkMap, myMDic);
        String in = String.valueOf(inRate);
        String out = String.valueOf(outRate);
        String cat = String.valueOf(cateGoryRate);

        fw.write(zh + "\t" + en + "\t" + in + "\t" + out + "\t" + cat + "\n");
        fw.flush();

//        System.out.println(cateGoryRate);
//        System.out.println(inRate);
//        System.out.println(outRate);

    }
}
