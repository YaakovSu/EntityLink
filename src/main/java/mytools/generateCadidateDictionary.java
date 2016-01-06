package mytools;

import edu.util.Contant;
import edu.util.Myutil;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * qc->wc的主入口，融合generateDisambigation和generateRedirectSet两个集合
 * 自认为，存在一个page表（pagemapping进行映射），一个redirect文件，一个disambigation文件
 * 本class是用来生成disambigation文件的。
 * Created by youngsu on 14-10-27.
 */
public class generateCadidateDictionary {
    public static HashMap<String, HashSet<ArrayList<String>>> getCadidateDictionary(String redirectPath, String disambigationPath) {
        ArrayList<String> redirects = Myutil.readByLine(redirectPath);
        ArrayList<String> disambigations = Myutil.readByLine(disambigationPath);
        HashMap<String, HashSet<ArrayList<String>>> result = Combin(redirects, disambigations);
        return result;

    }

    public static void main(String[] args) throws IOException {
        FileWriter fw = new FileWriter(Contant.combin_disambigation_redirectOutput_Path);
        HashMap<String, HashSet<ArrayList<String>>> result = getCadidateDictionary(Contant.redirectOutput_Path, Contant.clean_disambigationOutput_Path);

        System.out.println(result);
        Iterator<String> iterator = result.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            HashSet<ArrayList<String>> values = result.get(key);
            fw.write(key + "\t");
            Iterator<ArrayList<String>> iterator1 = values.iterator();
            int N = values.size();
            int count = 0;
            while (iterator1.hasNext()) {
                count++;
                ArrayList<String> value = iterator1.next();
                if (count < N) {
                    fw.write(value + ",");
                } else {
                    fw.write(value + "\n");
                }
            }

        }
//        fw.write(result+"\n");
        fw.close();
    }


    private static HashMap<String, HashSet<ArrayList<String>>> Combin(ArrayList<String> redirects, ArrayList<String> disambigations) {
        HashMap<String, HashSet<ArrayList<String>>> result = new HashMap<String, HashSet<ArrayList<String>>>();
        int count = 0;
        for (String disambigation : disambigations) {
            count++;
            System.out.println("先在消歧义文本中已经执行了：" + count);
            HashSet<ArrayList<String>> set = new HashSet<ArrayList<String>>();
            String[] line = disambigation.split("\t");
            if (line.length > 1) {
                String key = line[0];
                String text = line[1];
                ArrayList<String> semi_Texts = Myutil.usePatterns(text); //对每一行中的所有歧义页面进行遍历，如果在redirect中有，则用redirect的list替换
                //后加入到set中。否则，将该记录直接加入到set中。
                for (String semi_text : semi_Texts) {
                    boolean notfindInRedirect = false;
                    //现在看redirect里面是不是有这个词条
                    for (String redirect : redirects) { //redirect为一行
                        if (redirect.contains(semi_text)) {
                            ArrayList<String> se_Value = new ArrayList<String>();
                            String[] lines = redirect.split("\t");
                            for (String li : lines) {
                                se_Value.add(li);
                            }
                            set.add(se_Value);
                            notfindInRedirect = true;
                            break;
                        }

                    }
                    if (notfindInRedirect) {
                        ArrayList<String> se_Value = new ArrayList<String>();
                        se_Value.add(semi_text);
                        set.add(se_Value);
                    }
                }
                result.put(key, set);
            }


        }
        return result;
    }


}
