package edu.nlp;

import edu.util.Myutil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by youngsu on 14-10-30.
 */
public class DirectDic {
    private String filepath;
    HashSet<ArrayList<String>> dic = new HashSet<ArrayList<String>>();

    public DirectDic(String filepath) {
        this.filepath = filepath;
        init();
    }

    private void init() {
        ArrayList<String> lines = Myutil.readByLine(filepath);
        for (String line : lines) {
            String[] titles = line.split("\t");
            ArrayList<String> result = new ArrayList<String>();
            for (String title : titles) {
                result.add(title);
            }
            dic.add(result);
        }

    }

    //是否包含title
    public boolean containGivenWord(String title) {
        Iterator<ArrayList<String>> iterator = dic.iterator();
        while (iterator.hasNext()) {
            ArrayList<String> titles = iterator.next();
            if (titles.contains(title)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<String> getRedirectPage(String title) {
        String name = "";
        ArrayList<String> result = new ArrayList<String>();
        Iterator<ArrayList<String>> iterator = dic.iterator();
        while (iterator.hasNext()) {
            ArrayList<String> titles = iterator.next();
            if (titles.contains(title)) {
                name = titles.get(0);
                result.add(name);
                return result;
            }
        }
        result.add(name);
        return result;
    }

    //模糊匹配在redirect词典中能找到的词
    public ArrayList<String> getRedirectPage_Fuzzy(String title) {
        String name = "";
        ArrayList<String> result = new ArrayList<String>();
        Iterator<ArrayList<String>> iterator = dic.iterator();
        while (iterator.hasNext()) {
            ArrayList<String> titles = iterator.next();
            for (String pagename : titles) {
                String plusTitle = title + "_";
                if (pagename.startsWith(plusTitle) || pagename.equals(title)) {
//                    name = titles.get(0);
//                    result.add(name);
                    return titles;
                }

            }


        }
        return result;
    }

    public boolean containGivenWord_Fuzzy(String title) {
        Iterator<ArrayList<String>> iterator = dic.iterator();
        while (iterator.hasNext()) {
            ArrayList<String> titles = iterator.next();
            for (String ti : titles) {
                String plusTitle = title + "_";
                if (ti.startsWith(plusTitle) || ti.equals(title)) {
                    return true;
                }
            }

        }
        return false;
    }
}
