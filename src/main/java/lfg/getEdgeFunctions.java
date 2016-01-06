package lfg;

import edu.util.Myutil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by CBD_O9 on 2015-04-18.
 */
public class getEdgeFunctions {
    public static void main(String[] args) {
        String PCGraphPath = "C:\\youngsu\\lijuanziData\\PCGraph.tsv";

        String zh = "司空曙";
        String en = "Margaret_Court";

        HashMap<String, ArrayList<String>> PCGraph = getPCG(PCGraphPath);
        int count = getEdgeCount(zh, en, PCGraph);
        System.out.println(count);
    }

    private static HashMap<String, ArrayList<String>> getPCG(String pcGraphPath) {
        HashMap<String, ArrayList<String>> pcg = new HashMap<String, ArrayList<String>>();

        ArrayList<String> text = Myutil.readByLine(pcGraphPath);
        for (String line : text) {
            ArrayList<String> newValues = new ArrayList<String>();
            String[] words = line.split("\t\t");
            String key = words[0];
            if (pcg.containsKey(key)) {
                newValues = pcg.get(key);
                newValues.add(words[1]);
                pcg.put(key, newValues);
            } else {
                newValues.add(words[1]);
                pcg.put(key, newValues);
            }

        }
        return pcg;
    }

    public static int getEdgeCount(String zhName, String enName, HashMap<String, ArrayList<String>> pcg) {
        String myKey = zhName + "\t" + enName;
        if (pcg.containsKey(myKey)) {
            ArrayList<String> value = pcg.get(myKey);
            return value.size();
        } else {
            return 0;
        }


    }
}
