package edu.nlp;

import edu.util.Myutil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 候选词典的类，需要读入内存。
 * Created by youngsu on 14-10-29.
 */
public class CandidateDic {
    private HashMap<String, HashSet<ArrayList<String>>> dic = new HashMap<String, HashSet<ArrayList<String>>>();

    public CandidateDic(String filePath) {
        init(filePath);
    }

    /**
     * 候选词典中是否包含查询的词。
     *
     * @param name
     * @return
     */
    public boolean containGivenWord(String name) {
        Iterator<String> iterator = dic.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (key.equals(name)) {
                return true;
            }
            HashSet<ArrayList<String>> set = dic.get(key);
            Iterator<ArrayList<String>> iterator1 = set.iterator();
            while (iterator1.hasNext()) {
                ArrayList<String> list = iterator1.next();
                if (list.contains(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    //如果在候选dic中发现，才能调用下面方法
    public ArrayList<String> getAllCandidatePages(String name) {
        ArrayList<String> result = new ArrayList<String>();
        Iterator<String> iterator = dic.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            HashSet<ArrayList<String>> set = dic.get(key);
            if (key.equals(name)) { //如果查询词为key，则将所有的候选的list中的第一个加入到result中
                Iterator<ArrayList<String>> iterator1 = set.iterator();
                while (iterator1.hasNext()) {
                    ArrayList<String> list = iterator1.next();
                    if (!result.contains(list.get(0))) {
                        if (!list.get(0).contains("\'")) {
                            result.add(list.get(0)); //将每一个list中的第一个加入到result中
                        }

                    }

                }
//                return result;
            } else {// 如果key中不包含name
                Iterator<ArrayList<String>> iterator1 = set.iterator();

                while (iterator1.hasNext()) {
                    ArrayList<String> list = iterator1.next();
                    if (list.contains(name)) {
                        Iterator<ArrayList<String>> iterator2 = set.iterator();
                        while (iterator2.hasNext()) {
                            ArrayList<String> list2 = iterator2.next();
                            if (!result.contains(list2.get(0))) {
                                if (!list2.get(0).contains("\'")) {
                                    result.add(list2.get(0)); //将每一个list中的第一个加入到result中
                                }

                            }

                        }
//                        return result;
                    }

                }


            }
        }
        return result;
    }

    public static final String pattern = "(\\[)(.*?)(\\])"; //[]

    private void init(String filePath) {
        ArrayList<String> dics = Myutil.readByLine(filePath);
        for (String line : dics) {
            HashSet<ArrayList<String>> set = new HashSet<ArrayList<String>>();
            String[] titles = line.split("\t");
            String key = titles[0];
            String values = titles[1];

            Matcher m = Pattern.compile(pattern).matcher(values);
            while (m.find()) {
                ArrayList<String> semi_result = new ArrayList<String>();
                String testContant = m.group(2);
                String[] lines = testContant.split(",");
                for (String li : lines) {
                    li = li.replace(" ", "");
                    semi_result.add(li);

                }
                set.add(semi_result);
            }
            dic.put(key, set);
        }
    }  //end init


}
