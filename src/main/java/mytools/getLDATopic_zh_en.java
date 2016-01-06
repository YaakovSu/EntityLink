package mytools;

import edu.util.Contant;
import edu.util.Myutil;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by CBD_O9 on 2014-12-03.
 */
public class getLDATopic_zh_en {
    static String enTopicFilePath = "Project_EntityLinking/data/en_topics.txt";
    static String zhTopicFilePath = "Project_EntityLinking/data/zh_topics.txt";

    public static void main(String[] args) throws IOException {
//        writeZh_EN();

        String zh_en_topics = "Project_EntityLinking/data/zh_en_topics.txt";
        count(zh_en_topics);
    }

    private static void count(String zh_en_topics) {
        ArrayList<String> lines = Myutil.readByLine(zh_en_topics);
        int count1 = 0;
        int count2 = 0;
        int count3 = 0;
        int countOneTopic = 0;
        for (String line : lines) {
            if (line.startsWith("Topic")) {

                if (countOneTopic == 0) {

                    count3++;
                } else {

                    System.out.println(line + "\t" + countOneTopic);
                    countOneTopic = 0;
                }
                continue;
            }
            line.replace("\t", "");
            String en = line.split(" ")[1];
            if (en.equals("NA") || en.equals("NB")) {
                count1++;
            } else {
                countOneTopic++;
                count2++;
            }
        }
        System.out.println("能够找到对应的个数为：" + count2);
        System.out.println("不能找到的个数为：" + count1);
        System.out.println("没有结果的topic个数为：" + count3);
    }


    public static void writeZh_EN() throws IOException {
        FileWriter fw = new FileWriter("Project_EntityLinking/data/zh_en_topics.txt");

        ArrayList<String> enlines = Myutil.readByLine(enTopicFilePath);
        HashSet<String> enTopicNames = new HashSet<String>();
        for (String enline : enlines) {
            if (enline.startsWith("Topic")) {
                continue;
            }
            enline = enline.replace("\t", "");
            String enname = enline;
            enTopicNames.add(enname);
        }


        ArrayList<String> zhlines = Myutil.readByLine(zhTopicFilePath);
        for (String zhline : zhlines) {
            if (zhline.startsWith("Topic")) {
                System.out.println(zhline);
                fw.write(zhline + "\n");
                continue;
            }
            zhline = zhline.replace("\t", "");
            String zhname = zhline.split(" ")[0];
            ArrayList<String> candidateEnName = getEnCandidateNameKeyWords(Contant.E_CdicPath, zhname);
            if (candidateEnName.isEmpty()) {
                System.out.println(zhname + "\t" + "NA");
                fw.write("\t" + zhname + " " + "NA" + "\n");
                fw.flush();
            } else {

                boolean hasInEnTopicNames = false;
                for (String candidateName : candidateEnName) {
                    if (enTopicNames.contains(candidateName)) {

                        if (!hasInEnTopicNames) {
                            fw.write("\t" + zhname + " " + candidateName);
                            fw.flush();
                            hasInEnTopicNames = true;
                        } else {
                            fw.write("," + candidateName);
                            fw.flush();
                        }

                        System.out.print(candidateName + " ");

                    }
                }
                if (hasInEnTopicNames) {
                    fw.write("\n");
                } else {
                    fw.write("\t" + zhname + " " + "NB" + "\n");
                    fw.flush();
                }
                System.out.println();
//                System.out.println(candidateEnName);
//                System.exit(0);
            }

        }

        fw.close();
    }


    public static ArrayList<String> getEnCandidateNameKeyWords(String e_cdicPath, String zhWikiTitle) {
        ArrayList<String> E_Cdic = Myutil.readByLine(e_cdicPath);
        ArrayList<String> enCandidateNameWords = new ArrayList<String>();

        for (String line : E_Cdic) {
            String[] text = line.split("=");
            String zhName = text[0];
            String semi_enName = text[1];
            if (zhWikiTitle.equals(zhName)) {
                ArrayList<String> enKeyWords = getCandidateWords(semi_enName);
                for (String enKeyWord : enKeyWords) {
                    if (!enCandidateNameWords.contains(enKeyWord)) {
                        enCandidateNameWords.add(enKeyWord);
                    }
                }

            }

        }
        return enCandidateNameWords;
    }

    private static ArrayList<String> getCandidateWords(String semi_enName) {
        ArrayList<String> keywords = new ArrayList<String>();
        //先提取英文字符
        String enNames = Myutil.CleanENText(semi_enName);
        String[] names = enNames.split(", ");
        for (String name : names) {
            String[] keys = name.split("] ");
            for (String key : keys) {
                if (!key.isEmpty()) {

                    if (!keywords.contains(key)) {
                        key = key.replace(" ", "_");
                        keywords.add(key);
                    }
                }
            }
        }
        return keywords;

    }

}
