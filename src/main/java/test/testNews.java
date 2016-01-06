package test;

import edu.util.Contant;
import edu.util.Myutil;
import edu.util.opMysql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by CBD_O9 on 2014-11-09.
 */
public class testNews {

    public static void main(String[] args) throws IOException, SQLException {
        String newspath = "C:\\youngsu\\wikidata\\more_news.txt";
//        ArrayList<String> baike = Myutil.readByLine(baikepath);
//        for(String line : baike){
//            String[] words = line.split("\t");
//            for(String word :words){
//                System.out.println(word);
//
//            }
////            System.exit(0);
//        }
        writeDisamTitles(newspath);
    }


    public static void writeDisamTitles(String filepath) throws SQLException, IOException {

        ArrayList<String> baike = Myutil.readByLine(filepath);
        Connection conn = opMysql.connSQL(Contant.testmysqlurl);
        for (String line : baike) {
            String[] words = line.split("\\*\\*\\*");

            String title = words[0];
            String content = words[1];
            String sql = "insert into testwiki.test_news(title,content) values('" + title + "','" + content + "');";
            opMysql.insertSQL(conn, sql);
            System.exit(0);
        }


    }

}
