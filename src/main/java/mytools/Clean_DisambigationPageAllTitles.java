package mytools;

import edu.util.Contant;
import edu.util.Myutil;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 1、统计一共有多少个Disambigation的词条
 * <p>
 * 2、读取disambigation.output文件，将其中A (描述)变成A_(描述)，并且去掉|后面的词
 * Created by youngsu on 14-10-28.
 */
public class Clean_DisambigationPageAllTitles {
    public static final String pattern = "(\\[)(.*?)(\\])"; //[]


    public static void main(String[] args) throws IOException {
        ArrayList<String> pages = Myutil.readByLine(Contant.disambigationOutput_Path);
        ArrayList<String> titles = Myutil.readByLine(Contant.combin_disambigation_redirectOutput_Path);
        countDisambigationPageAllTitles(pages); //  //统计一共有多少个Disambigation的词条
        countCombinDisambigation_RedirectPageAllTitle(titles);

//        cleanDisambigationPageAllTitles(pages);  //读取disambigation.output文件，将其中A (描述)变成A_(描述)，并且去掉|后面的词

    }

    private static void countCombinDisambigation_RedirectPageAllTitle(ArrayList<String> titles) {

        int count = 0;
        int t = 0;
        for (String title : titles) {
            t++;
            String pages = title.split("\t")[1];
            count += usePatterns(pages);
        }
        System.out.println("联合redirect和disambigation后，总共的词条数：" + count);

    }

    private static int usePatterns(String text) {
        int count = 0;
        ArrayList<String> result = new ArrayList<String>();
        Matcher m = Pattern.compile(pattern).matcher(text);
        while (m.find()) {
            String testContant = m.group(2);
            String[] lines = testContant.split(",");
            count += lines.length;
        }

        return count;
    }

    //读取disambigation.output文件，将其中A (描述)变成A_(描述)，并且去掉|后面的词
    private static void cleanDisambigationPageAllTitles(ArrayList<String> pages) throws IOException {
        FileWriter fw = new FileWriter(Contant.clean_disambigationOutput_Path);
        for (String line : pages) {
            String[] text = line.split("\t");
            String key = text[0];
            System.out.print(key + "\t");
            fw.write(key + "\t");
            String value = text[1];
            Matcher m = Pattern.compile(pattern).matcher(value);
            while (m.find()) {
                String testContant = m.group(2);
                String[] titles = testContant.split(",");
                for (int i = 0; i < titles.length; i++) {
                    String title = titles[i];
                    title = title.split("\\|")[0];
                    title = title.replace(" (", "_(");
                    titles[i] = title;
                }
                ArrayList<String> result = new ArrayList<String>();
                for (String t : titles) {
                    result.add(t);
                }
                System.out.println(result);

                fw.write(result.toString() + "\n");

            }
        }
        fw.close();
    }

    //统计联合Disambigation和redirect以后，一共有多少个词条


    //统计一共有多少个Disambigation的词条
    private static void countDisambigationPageAllTitles(ArrayList<String> pages) {
        int count = 0;
        for (String line : pages) {
            Matcher m = Pattern.compile(pattern).matcher(line);
            while (m.find()) {
                String testContant = m.group(2);
                String[] counts = testContant.split(",");
                count += counts.length;
            }
        }
        System.out.println("消歧义词条总数为：" + count);
    }
}
