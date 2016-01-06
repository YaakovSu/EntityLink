package test;

import edu.util.Myutil;

import java.util.*;

/**
 * Created by CBD_O9 on 2014-12-12.
 */
public class Tongji {
    public static void main(String[] args) {
        Map<String, Integer> result = new HashMap<String, Integer>();
        ArrayList<String> files = Myutil.readByLine("C:\\youngsu\\CommonInOutCate_Num_InMDic1.output");
        for (String line : files) {
            String inlink = line.split("\t")[2];
            if (result.containsKey(inlink)) {
                int value = result.get(inlink);
                value = value + 1;
                result.put(inlink, value);
            } else {
                result.put(inlink, 1);
            }
        }

        sortByKey(result);
//        System.out.println(result);

    }

    private static void sortByKey(Map<String, Integer> map) {

        List<Map.Entry<String, Integer>> infoIds =
                new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
        //排序
        Collections.sort(infoIds, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                //return (o2.getValue() - o1.getValue());
                return (o1.getKey()).toString().compareTo(o2.getKey());
            }
        });

//排序后
        for (int i = 0; i < infoIds.size(); i++) {
            String id = infoIds.get(i).toString();
            System.out.println(id);
        }

    }
}
