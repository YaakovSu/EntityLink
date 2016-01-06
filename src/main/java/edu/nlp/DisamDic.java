package edu.nlp;

import edu.util.Myutil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by youngsu on 16-1-5.
 */
public class DisamDic {
    private String filePath;
    private Map<String, ArrayList<String>> dic = new HashMap<String, ArrayList<String>>();

    public DisamDic(String filePath) {
        this.filePath = filePath;
        init();
    }

    public Map<String, ArrayList<String>> getDic() {
        return dic;
    }

    public boolean containsKey(String key) {
        return dic.containsKey(key);

    }

    public ArrayList<String> getDisamPage(String key) {
        return dic.get(key);
    }


    private void init() {
        ArrayList<String> text = Myutil.readByLine(filePath);
        for (String line : text) {
            ArrayList<String> value = new ArrayList<String>();
            String[] terms = line.split("\t");
            if (terms.length > 1) {
                String key = terms[0];
                String term = terms[1];
                term = term.replace("[", "");
                term = term.replace(" ", "");
                String[] words = term.split(",");
                for (String word : words) {
                    value.add(word);
                }
                dic.put(key, value);
            }

        }
    }

}
