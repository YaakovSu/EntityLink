package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by CBD_O9 on 2014-12-18.
 */
public class ZhWiki {

    public static void main(String[] args) {


        String filePath = "C:\\youngsu\\zhwiki-20141009-pages-articles.xml\\zhwiki-20141009-pages-articles.xml";


        File file = new File(filePath);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
//一次读一行，读入null时文件结束
            while ((tempString = reader.readLine()) != null) {
                System.out.println(tempString);


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


    }
}
