package mytools;

import java.io.*;
import java.util.HashMap;

/**
 * Created by CBD_O9 on 2014-11-17.
 */
public class createCombinInlinkNames {

    public static void main(String[] args) throws IOException {
        String pageInlinkPath = "C:\\youngsu\\wikidata\\wiki_en\\page_inlinks.txt";
        String pageMapLinePath = "C:\\youngsu\\wikidata\\wiki_en\\PageMapLine.txt";
        String outputPath = "C:\\youngsu\\wikidata\\wiki_en\\combin_inlink.txt";
        System.out.println("读取mapline.........");
        HashMap<String, String> mapLine = readMapLine(pageMapLinePath);
        System.out.println("Start...........");
        FileWriter fw = new FileWriter(outputPath);

        File file = new File(pageInlinkPath);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int count = 0;
//一次读一行，读入null时文件结束
            while ((tempString = reader.readLine()) != null) {
                count++;
                System.out.println("已完成：" + count);
                String[] line = tempString.split("\t");
                String id = line[0];
                String inlinkId = line[1];

                if (mapLine.containsKey(inlinkId)) {
                    String outlinName = mapLine.get(inlinkId);
                    fw.write(id + "\t" + inlinkId + "\t" + outlinName);
                    fw.write("\n");
                }

            }
            fw.close();
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
    }


    public static HashMap<String, String> readMapLine(String filePath) {

        HashMap<String, String> content = new HashMap<String, String>();
        File file = new File(filePath);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int count = 0;
//一次读一行，读入null时文件结束
            while ((tempString = reader.readLine()) != null) {
                count++;
                System.out.println("已添加mapline条数：" + count);
                String[] line = tempString.split("\t");
                String outlinkId = line[0];
                String outlinkName = line[1];
                content.put(outlinkId, outlinkName);
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

        return content;
    }

}
