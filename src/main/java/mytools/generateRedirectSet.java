package mytools;

import edu.util.Contant;
import edu.util.opMysql;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import static edu.util.Contant.redirectOutput_Path;
import static edu.util.Contant.selectWikiRedirect;

/**
 * 从redirect表中提取出同一个pageid下的所有词加入list中，list中的第一个是page中能找到页面的词
 * Created by youngsu on 14-10-27.
 */
public class generateRedirectSet {

    public static HashSet<ArrayList<String>> getRedirectSet() throws SQLException {
        HashSet<ArrayList<String>> result = new HashSet<ArrayList<String>>();
        Connection conn = opMysql.connSQL(Contant.zhmysqlurl);
        ResultSet resultSet = opMysql.selectSQL(conn, selectWikiRedirect);
        HashMap<String, ArrayList<String>> semi_result = new HashMap<String, ArrayList<String>>();
        int count = 0;
        while (resultSet.next()) {
            count++;
//            String pageId=resultSet.getString("id");
            String redirects = resultSet.getString("redirects");
            String name = resultSet.getString("name");//PageId对应的词条名字，放于list的第一个
            System.out.println("正在抽取第 " + count + " 条数据！");

            semi_result = extract(semi_result, redirects, name);
        }
        result = hashMap2hashSet(semi_result); //为了效率，抽取时使用hashmap进行，然后转换为hashset
        opMysql.deconnSQL(conn);
        return result;

    }

    private static HashSet<ArrayList<String>> hashMap2hashSet(HashMap<String, ArrayList<String>> semi_result) {
        HashSet<ArrayList<String>> result = new HashSet<ArrayList<String>>();
        Iterator<String> iterator = semi_result.keySet().iterator();
        while (iterator.hasNext()) {
            ArrayList<String> allWords = new ArrayList<String>();
            String name = iterator.next();
            ArrayList<String> redirects = semi_result.get(name);
            allWords.add(name);
            for (String redirect : redirects) {
                allWords.add(redirect);
            }
            result.add(allWords);
        }
        return result;
    }

    private static HashMap<String, ArrayList<String>> extract(HashMap<String, ArrayList<String>> semi_result, String redirects, String name) {
        if (semi_result.containsKey(name)) {
            ArrayList<String> value = semi_result.get(name);
            value.add(redirects);
            semi_result.put(name, value);
        } else {
            ArrayList<String> value = new ArrayList<String>();
            value.add(redirects);
            semi_result.put(name, value);
        }
        return semi_result;
    }


    //将抽取到的一条新数据加入到hashmap中
    private static HashSet<ArrayList<String>> extracts(HashSet<ArrayList<String>> result, String redirects, String name) {


        boolean neverAppear = true;
        for (ArrayList<String> list : result) {
            if (list.contains(name)) {
                list.add(redirects); //如果该list开头为name，则将redirect加入到该list中
                neverAppear = false;
                break;
            }
        }
        if (neverAppear) {
            ArrayList<String> list = new ArrayList<String>();
            list.add(name);
            list.add(redirects);
            result.add(list);
        }
        return result;
    }

    public static void main(String[] args) throws SQLException, IOException {
        FileWriter fw = new FileWriter(redirectOutput_Path);
        HashSet<ArrayList<String>> result = getRedirectSet();

        for (ArrayList<String> list : result) {
            for (int i = 0; i < list.size(); i++) {
                if (i < list.size() - 1) {
                    String word = list.get(i);
                    fw.write(word + "\t");
                } else if (i == list.size() - 1) {
                    String word = list.get(i);
                    fw.write(word);
                }

            }
            fw.write("\n");
        }
        fw.close();
        System.out.println(result);
    }
}
