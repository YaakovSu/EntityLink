package mytools;

import java.io.*;

/**
 * Created by CBD_O9 on 2015-04-06.
 */
public class cutYagoFiles {

    public static void main(String[] args) {

        String filePath = "G:\\mytest\\yagoLabels.tsv";
        String outpath = "G:\\mytest\\";

        File file = new File(filePath);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
//一次读一行，读入null时文件结束
            int count = 1;
            FileWriter fw = new FileWriter(outpath + count + ".tsv");
            while ((tempString = reader.readLine()) != null) {
                if (count % 5000000 == 0) {
                    fw.close();
                    fw = new FileWriter(outpath + count + ".tsv");
                    fw.write(tempString + "\n");

                } else {
                    fw.write(tempString + "\n");
                }
                count++;
                System.out.println("已完成：" + count);
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
}
