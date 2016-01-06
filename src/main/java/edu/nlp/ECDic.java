package edu.nlp;

import edu.util.Myutil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by youngsu on 15-12-31.
 * 汉英词典数据
 */
public class ECDic {
    private HashMap<String, HashSet<String>> dic = new HashMap<String, HashSet<String>>();
    private String inputPath;

    public ECDic() {
    }

    public ECDic(String inputPath) {
        this.inputPath = inputPath;
        init();
    }

    private void init() {
        ArrayList<String> semi_E_Cdic = Myutil.readByLine(inputPath);
        HashSet<String> E_Cdic = new HashSet<String>();
        for (String word : semi_E_Cdic) {
            E_Cdic.add(word);
        }

        for (String line : E_Cdic) {
            HashSet<String> enCandidateNameWords = new HashSet<String>();
            String[] text = line.split("=");
            String zhName = text[0];
            String semi_enName = text[1];
            ArrayList<String> enKeyWords = getCandidateWords(semi_enName);
            for (String enKeyWord : enKeyWords) {
                if (!enCandidateNameWords.contains(enKeyWord)) {
                    enCandidateNameWords.add(enKeyWord);
                }
            }
//            System.out.println(zhName + "\t" + enCandidateNameWords);
            dic.put(zhName, enCandidateNameWords);
        }

    }

    public HashMap<String, HashSet<String>> getDic() {
        return dic;
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

    public boolean containsKey(String key) {
        return dic.containsKey(key);
    }

    public HashSet<String> get(String key) {
        return dic.get(key);
    }
}
