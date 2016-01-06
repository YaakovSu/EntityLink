package mytools;

import edu.util.Myutil;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by CBD_O9 on 2014-11-17.
 */
public class getCategoryMapping {
    public static void main(String[] args) throws IOException {

//        getCategory();
        fenGeCategory_page();
    }

    public static void fenGeCategory_page() throws IOException {
        String input1 = "C:\\youngsu\\Entity_LinkingData\\en_zh_Wiki.output";
        String input2 = "C:\\youngsu\\Entity_LinkingData\\en_zh_wiki_category.output";
        String output = "C:\\youngsu\\Entity_LinkingData\\en_zh_wiki_page.output";
        FileWriter fw = new FileWriter(output);
        ArrayList<String> page_category = Myutil.readByLine(input1);
        ArrayList<String> categorys = Myutil.readByLine(input2);
        ArrayList<String> categorynames = new ArrayList<String>();
        for (String category : categorys) {
            String name = category.split("=")[0];
            categorynames.add(name);
        }
        int count = 0;
        for (String pages : page_category) {
            count++;
            System.out.println("共有" + page_category.size() + "条!" + "已完成" + count);
            boolean flag = false;
            for (String categoryname : categorynames) {
                String cate = categoryname + "=";
                if (pages.startsWith(cate)) {
                    flag = true;
                    break;

                }
            }
            if (!flag) {
                fw.write(pages);
                fw.write("\n");
            }
        }
        fw.close();
    }


    /**
     * 获取有对应的category
     *
     * @param
     * @throws IOException
     */
    public static void getCategory() throws IOException {
        String inputpath = "C:\\youngsu\\Entity_LinkingData\\title";
        String outputpath = "C:\\youngsu\\Entity_LinkingData\\en_zh_wiki_category.output";
        FileWriter fw = new FileWriter(outputpath);

        ArrayList<String> Categorys = readByLine(inputpath);
        int count = 0;
        for (String category : Categorys) {
            count++;
            System.out.println("共有" + Categorys.size() + "条!" + "已完成" + count);
            String[] line = category.split("\t");
            String semi_zhname = line[0].replace("Category:", "");
            String zhname = semi_zhname.replace(" ", "_");
            String semi_enname = line[1].replace("Category:", "");
            String enname = semi_enname.replace(" ", "_");
            fw.write(enname + "=" + zhname);
            fw.write("\n");
        }
        fw.close();

    }


    public static ArrayList<String> readByLine(String filePath) {
        ArrayList<String> content = new ArrayList<String>();
        File file = new File(filePath);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
//一次读一行，读入null时文件结束
            while ((tempString = reader.readLine()) != null) {
                if (tempString.startsWith("Category:")) {
                    content.add(tempString);
                }

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
