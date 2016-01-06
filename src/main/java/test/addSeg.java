package test;

import java.io.*;

/**
 * Created by CBD_O9 on 2014-11-07.
 */
public class addSeg {
    public static void main(String[] args) throws IOException {
        String inputPath = "C:\\youngsu\\wikidata\\page_outlinks.txt";
        String outputPath = "Project_EntityLinking/output/page_outlinks.txt";
        readByLine(inputPath, outputPath);


    }

    public static void readByLine(String filePath, String outputPath) throws IOException {
//        ArrayList<String> content = new ArrayList<String>();
        FileWriter fw = new FileWriter(outputPath);
        File file = new File(filePath);
        BufferedReader reader = null;
        try {
            int count = 0;
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
//一次读一行，读入null时文件结束
            while ((tempString = reader.readLine()) != null) {
                count++;
                tempString = tempString.replace("\t", ",");
                fw.write(tempString + "\n");
                System.out.println(count);
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

}
