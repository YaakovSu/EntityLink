package edu.util;

import org.ansj.app.keyword.Keyword;
import edu.baike.PageKeyWord;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by youngsu on 14-10-14.
 */
public class Myutil {


    /**
     * 读取某个文件夹下的所有文件
     */
    public static TreeMap<String, String> readfileInDic(String filepath) throws FileNotFoundException, IOException {
        TreeMap<String, String> result = new TreeMap<String, String>();
        try {

            File file = new File(filepath);
            if (!file.isDirectory()) {

                String absolutepath = file.getAbsolutePath();
                String name = file.getName();
                result.put(absolutepath, name);

            } else if (file.isDirectory()) {
                System.out.println("文件夹");
                String[] filelist = file.list();

                for (int i = 0; i < filelist.length; i++) {

                    File readfile = new File(filepath + "\\" + filelist[i]);
                    if (!readfile.isDirectory()) {
                        String absolutepath = readfile.getPath();
                        String name = readfile.getName();
                        result.put(absolutepath, name);

                    } else if (readfile.isDirectory()) {
                        readfileInDic(filepath + "\\" + filelist[i]);
                    }
                }

            }

        } catch (FileNotFoundException e) {
            System.out.println("readfile()   Exception:" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除某个文件夹下的所有文件夹和文件
     */


    public static boolean deletefile(String delpath)
            throws FileNotFoundException, IOException {
        try {

            File file = new File(delpath);
            if (!file.isDirectory()) {
                System.out.println("1");
                file.delete();
            } else if (file.isDirectory()) {
                System.out.println("2");
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++) {
                    File delfile = new File(delpath + "\\" + filelist[i]);
                    if (!delfile.isDirectory()) {
                        System.out.println("path=" + delfile.getPath());
                        System.out.println("absolutepath="
                                + delfile.getAbsolutePath());
                        System.out.println("name=" + delfile.getName());
                        delfile.delete();
                        System.out.println("删除文件成功");
                    } else if (delfile.isDirectory()) {
                        deletefile(delpath + "\\" + filelist[i]);
                    }
                }
                file.delete();

            }

        } catch (FileNotFoundException e) {
            System.out.println("deletefile()   Exception:" + e.getMessage());
        }
        return true;
    }


    /**
     * 按行读取文本
     *
     * @param filePath
     * @return
     */
    public static ArrayList<String> readByLine(String filePath) {
        ArrayList<String> content = new ArrayList<String>();
        File file = new File(filePath);
        BufferedReader reader = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            reader = new BufferedReader(isr);
            String tempString = null;
//一次读一行，读入null时文件结束
            int count = 0;
            while ((tempString = reader.readLine()) != null) {
                content.add(tempString);
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

        return content;
    }

    public static HashMap<String, Double> sim_BOW(ArrayList<PageKeyWord> textKeyWords, ArrayList<Keyword> contentKeyWords, HashMap<String, Double> allKeyWordsTitles) {
        HashMap<String, Double> result = new HashMap<String, Double>();
        for (PageKeyWord textKeyWord : textKeyWords) {
            String title = textKeyWord.getTitle();
            ArrayList<Keyword> keywords = textKeyWord.getKeywords();
            ArrayList<TreeMap<String, Double>> twoVector = createVector(keywords, contentKeyWords, allKeyWordsTitles); //value里面存的是向量的值
            TreeMap<String, Double> textMap = twoVector.get(0);
            TreeMap<String, Double> contentMap = twoVector.get(1);
            ArrayList<Double> textVector = new ArrayList<Double>(textMap.values());
            ArrayList<Double> contentVector = new ArrayList<Double>(contentMap.values());
            double sim_value = sim(textVector, contentVector);
            result.put(title, sim_value);
        }
        return result;
    }

    // 求余弦相似度
    public static double sim(ArrayList<Double> textVector, ArrayList<Double> contentVector) {
        double result = 0;
        result = pointMulti(textVector, contentVector) / sqrtMulti(textVector, contentVector);

        return result;
    }

    private static double sqrtMulti(List<Double> vector1, List<Double> vector2) {
        double result = 0;
        result = squares(vector1) * squares(vector2);
        result = Math.sqrt(result);
        return result;
    }

    // 求平方和
    private static double squares(List<Double> vector) {
        double result = 0;
        for (Double dou : vector) {
            result += dou * dou;
        }
        return result;
    }

    // 点乘法
    private static double pointMulti(List<Double> vector1, List<Double> vector2) {
        double result = 0;
        for (int i = 0; i < vector1.size(); i++) {
            result += vector1.get(i) * vector2.get(i);
        }
        return result;
    }

    private static ArrayList<TreeMap<String, Double>> createVector(ArrayList<Keyword> textKeyWords, ArrayList<Keyword> contentKeyWords, HashMap<String, Double> allKeyWordsMap) {
        TreeMap<String, Double> textMap = new TreeMap<String, Double>();
        TreeMap<String, Double> contentMap = new TreeMap<String, Double>();

        ArrayList<TreeMap<String, Double>> result = new ArrayList<TreeMap<String, Double>>();

        //切忌不能用映射
        Iterator<String> iterator = allKeyWordsMap.keySet().iterator();
        while (iterator.hasNext()) {
            String name = iterator.next();
            double value = allKeyWordsMap.get(name);
            textMap.put(name, value);
            contentMap.put(name, value);
        }


        for (Keyword textKeyWord : textKeyWords) {
            String name = textKeyWord.getName();
            double score = textKeyWord.getScore();
            score = Math.log(score);
            textMap.put(name, score);
        }
        for (Keyword contentKeyWord : contentKeyWords) {
            String name = contentKeyWord.getName();
            double score = contentKeyWord.getScore();
            score = Math.log(score);

            contentMap.put(name, score);
        }

        result.add(textMap);
        result.add(contentMap);

        return result;
    }

    public static final String pattern = "(\\[)(.*?)(\\])"; //[]

    public static ArrayList<String> usePatterns(String text) {
        ArrayList<String> result = new ArrayList<String>();
        Matcher m = Pattern.compile(pattern).matcher(text);
        while (m.find()) {
            String testContant = m.group(2);
            String[] lines = testContant.split(",");
            for (String line : lines) {
                line = line.replace(" ", "");
                result.add(line);
            }
        }

        return result;
    }

    public static ArrayList<String> usePatterns_firstListWord(String text) {
        ArrayList<String> result = new ArrayList<String>();
        Matcher m = Pattern.compile(pattern).matcher(text);
        while (m.find()) {
            String testContant = m.group(2);
            String firstListWords = testContant.split(",")[0];
            firstListWords = firstListWords.replace(" ", "");
            result.add(firstListWords);
        }
        return result;
    }

    public static String getCleanText(String pre_text) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < pre_text.length(); i++) {
            char ch = pre_text.charAt(i);
            if (((ch >= 0x2000) && (ch <= 0x206F))
                    || ((ch >= 0x4E00) && (ch <= 0x9FBF))
                    || ((ch >= 0xFF00) && (ch <= 0xFFEF))
                    || ((ch >= 0x3000) && (ch <= 0x303F))
                    || ((ch >= 0xFE30) && (ch <= 0xFE4F))
                    || ((ch >= 0xFE10) && (ch <= 0xFE1F))
                    || ((ch >= 0x4E00) && (ch <= 0x9FBF))
                    || ((ch >= 'a') && (ch <= 'z'))
                    || ((ch >= 'A') && (ch <= 'Z')))
                sb.append(ch);
        }
        String result = sb.toString();
        return result;

    }


    public static String getYagoCleanText(String pre_text) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < pre_text.length(); i++) {
            char ch = pre_text.charAt(i);
            if (((ch >= 0x4E00) && (ch <= 0x9FBF))
                    || ((ch >= 0xFF00) && (ch <= 0xFFEF))
                    || ((ch >= 0x3000) && (ch <= 0x303F))
                    || ((ch >= 0xFE30) && (ch <= 0xFE4F))
                    || ((ch >= 0xFE10) && (ch <= 0xFE1F))
                    || ((ch >= 0x4E00) && (ch <= 0x9FBF)))
                sb.append(ch);
        }
        String result = sb.toString();
        return result;

    }


    //只是匹配英文和英文标点
    public static String CleanENText(String pre_text) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < pre_text.length(); i++) {
            char ch = pre_text.charAt(i);
            if (!((ch >= 0x2000) && (ch <= 0x206F))
                    && (ch != '['))
                sb.append(ch);
        }
        String result = sb.toString();
        return result;

    }

    public static List<Map.Entry<String, Double>> sort(HashMap<String, Double> result) {
        List<Map.Entry<String, Double>> infoIds =
                new ArrayList<Map.Entry<String, Double>>(result.entrySet());
        Collections.sort(infoIds, new Comparator<Map.Entry<String, Double>>() {
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                if (o2.getValue() - o1.getValue() > 0) {
                    return 1;
                } else if (o2.getValue() - o1.getValue() == 0) {
                    return 0;
                } else {
                    return -1;
                }

            }
        });

        return infoIds;
    }
}
