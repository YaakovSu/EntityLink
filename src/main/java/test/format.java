package test;

import edu.util.Myutil;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * Created by CBD_O9 on 2015-03-15.
 */
public class format {
    static String inputDir = "C:\\youngsu\\PracticedMyHands\\Project_EntityLinking\\output\\testTop500_RankSVM_noOutlink\\test_pre\\feature";

    public static void main(String[] args) throws IOException {
        TreeMap<String, String> allfiles = Myutil.readfileInDic(inputDir); //key为全路径 ，value为名字
        Iterator<String> iterator = allfiles.keySet().iterator();
        while (iterator.hasNext()) {
            String semiOutputPath = inputDir + "1\\";
            String path = iterator.next();
            String name = allfiles.get(path);
            FileWriter fw = new FileWriter(semiOutputPath + name);
            ArrayList<String> text = Myutil.readByLine(path);
            for (String line : text) {
                line = line.replace("qid1:", "qid:1");
                fw.write(line + "\n");

            }
            fw.close();

        }
    }
}
