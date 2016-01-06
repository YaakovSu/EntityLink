package lfg;

import edu.util.Myutil;

import java.io.*;
import java.util.*;

/**
 * Created by CBD_O9 on 2015-04-17.
 */
public class test_GetTrainningData {

    public static void main(String[] args) throws IOException {
        String input = "C:\\youngsu\\Entity_LinkingData\\trainningData1000_1000.output";
        String output = "C:\\youngsu\\lijuanziData\\1000trainningInput.tsv";
        writer1000TrainningData(input, output);

//        String inputDir = "C:\\youngsu\\PracticedMyHands\\Project_EntityLinking\\output\\testTop500_RankSVM_noCoherence1\\test_pre\\name";
//        String outPut2 = "C:\\\\youngsu\\\\lijuanziData\\\\1000testDataInput.tsv";
//        String mDicPath = "C:\\youngsu\\Entity_LinkingData\\en_zh_wiki_page.output";
//        writer1000testData(inputDir,outPut2,mDicPath);

    }

    public static void writer1000TrainningData(String inputPath, String outputPath) throws IOException {
        FileWriter fw = new FileWriter(outputPath);

//        System.out.println("he");
//        ArrayList<String> inputDatas = Myutil.readByLine(inputPath);
//        System.out.println(" d");

        File file = new File(inputPath);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
//一次读一行，读入null时文件结束
            int count = 0;
            while ((tempString = reader.readLine()) != null) {
                String[] line = tempString.split("\t");
                String tag = line[0];
                String zhName = line[1];
                String enName = line[2];
                if (tag.equals("1")) {
                    fw.write(zhName + "\t" + enName + "\t" + "1" + "\n");
                    fw.flush();
                }
                System.out.println("已完成" + count);
                count++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        fw.close();

    }

    public static void writer1000testData(String inputDir, String output, String MDicPath) throws IOException {
        FileWriter fw = new FileWriter(output);
        ArrayList<String> alltestTitles = getAlltestTitlesInMDic(inputDir); //先获得所有的测试title
        HashMap<String, String> mdic = getAllTitleInMDic(MDicPath);
        for (String title : alltestTitles) {
            if (mdic.containsKey(title)) {
                String enName = mdic.get(title); //获得title在
                fw.write(title + "\t" + enName + "\t" + "0" + "\n");
                fw.flush();

            }

        }
        fw.close();

    }

    private static HashMap<String, String> getAllTitleInMDic(String mDicPath) {
        HashMap<String, String> result = new HashMap<String, String>();
        ArrayList<String> allTitles = Myutil.readByLine(mDicPath);
        for (String line : allTitles) {
            String[] names = line.split("=");
            String zhName = names[1];
            String enName = names[0];
            result.put(zhName, enName);
        }
        return result;
    }

    private static ArrayList<String> getAlltestTitlesInMDic(String inputDir) throws IOException {
        ArrayList<String> resultNames = new ArrayList<String>();
        TreeMap<String, String> allFiles = Myutil.readfileInDic(inputDir);
        Iterator<String> iterator = allFiles.keySet().iterator();
        while (iterator.hasNext()) {
            String abPath = iterator.next();
            String name = allFiles.get(abPath).split("_")[0];
            resultNames.add(name);
        }
        return resultNames;
    }
}
