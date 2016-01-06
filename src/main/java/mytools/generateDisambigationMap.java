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
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static edu.util.Contant.*;
import static edu.util.Patterns.*;

/**
 * 针对Page表，抽取出含有消歧义标识的词条，并利用pattern抽取出维基百科中所有歧义词条生成一个hashmap
 * Created by youngsu on 14-10-27.
 */
public class generateDisambigationMap {
    public static HashMap<String, ArrayList<String>> getAllDisambigationTitle() throws SQLException {

        Connection conn = opMysql.connSQL(Contant.zhmysqlurl);
        ResultSet resultSet = opMysql.selectSQL(conn, selectWikiDisAmbiguation); //查询出来的已经是处理好只有消歧义的词条的
        HashMap<String, ArrayList<String>> result = new HashMap<String, ArrayList<String>>();
        int count = 0;
        while (resultSet.next()) {
            count++;
            String name = resultSet.getString("name");
            String text = resultSet.getString("text");
            System.out.println("已经执行了：" + count);
            result = extractOneDisambigationTitle(result, name, text);

        }
        return result;
    }

    //从一条记录的文本中抽取出该title的消歧义页面的titile
    private static HashMap<String, ArrayList<String>> extractOneDisambigationTitle(HashMap<String, ArrayList<String>> result, String name, String text) {
        String key = name.split("_")[0];
        ArrayList<String> value = cleanText(text);
//        value.add(text);
        if ((!result.containsKey(key))) {
            result.put(key, value);
        } else if (result.get(key).size() == 0) {
            result.put(key, value);
        }

        return result;
    }

    private static ArrayList<String> cleanText(String text) {
//        System.out.println(text+"  first");
        String[] patterns = {pattern1_1, pattern1_2, pattern2_1, pattern2_2,
                pattern3_1, pattern4_1, pattern4_2, pattern5_1, pattern5_2, pattern6_1, pattern6_2, pattern7_1,
                pattern7_2, pattern8_1, pattern8_2};
        ArrayList<String> result = new ArrayList<String>();
        for (String pattern : patterns) {
            Matcher m = Pattern.compile(pattern).matcher(text);
            while (m.find()) {
                String testContant = m.group(2);
//                System.out.println(testContant+"  first");
                result.add(testContant);
//                System.out.println(testContant);
            }
        }


        return result;
    }

    public static void main(String[] args) throws SQLException, IOException {
        FileWriter fw1 = new FileWriter(disambigationSemi_Output_Path);
        FileWriter fw2 = new FileWriter(disambigationOutput_Path);
        HashMap<String, ArrayList<String>> result = getAllDisambigationTitle();
        Iterator<String> iterator = result.keySet().iterator();
        while (iterator.hasNext()) {
            String name = iterator.next();
            ArrayList<String> value = result.get(name);
            fw1.write(name + "\t" + value);
            fw1.write("\n");
            if (!value.isEmpty()) {
                fw2.write(name + "\t" + value);
                fw2.write("\n");
            }
            System.out.println(name + "\t" + value);
        }
        fw1.close();
        fw2.close();

    }
}
