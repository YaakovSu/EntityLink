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
public class testBaike {


    public static void main(String[] args) throws IOException, SQLException {
//        String baikepath_culture = "C:\\youngsu\\wikidata\\baikedump\\baikedump-culture\\data.dic";
//        String selectsql_culture = "testbaike_culture";
//
//        String baikepath_economic = "C:\\youngsu\\wikidata\\baikedump\\baikedump-economic\\data.dic";
//        String selectsql_economic = "testbaike_economic";
//
//        String baikepath_geo = "C:\\youngsu\\wikidata\\baikedump\\baikedump-geo\\data.dic";
//        String selectsql_geo = "testbaike_geo";
//
//        String baikepath_life = "C:\\youngsu\\wikidata\\baikedump\\baikedump-life\\data.dic";
//        String selectsql_life = "testbaike_life";
//
//        String baikepath_food = "C:\\youngsu\\wikidata\\baikedump\\baikedump-food\\data.dic";
//        String selectsql_food = "testbaike_food";
//
//        String baikepath_person = "C:\\youngsu\\wikidata\\baikedump\\baikedump-person\\data.dic";
//        String selectsql_person = "testbaike_person";
//
//        String baikepath_science = "C:\\youngsu\\wikidata\\baikedump\\baikedump-science\\data.dic";
//        String selectsql_science = "testbaike_science";

        String baikepath_edu = "C:\\youngsu\\wikidata\\baikedump\\baikedump-edu\\data.dic";
        String selectsql_edu = "testbaike_edu";


//        ArrayList<String> baike = Myutil.readByLine(baikepath);
//        for(String line : baike){
//            String[] words = line.split("\t");
//            for(String word :words){
//                System.out.println(word);
//
//            }
////            System.exit(0);
//        }
//        writeDisamTitles(baikepath_culture,selectsql_culture);
//        writeDisamTitles(baikepath_economic,selectsql_economic);
//        writeDisamTitles(baikepath_geo,selectsql_geo);
//        writeDisamTitles(baikepath_life,selectsql_life);
//        writeDisamTitles(baikepath_food,selectsql_food);
//        writeDisamTitles(baikepath_person,selectsql_person);
//        writeDisamTitles(baikepath_science,selectsql_science);
        writeDisamTitles(baikepath_edu, selectsql_edu);


    }


    public static void writeDisamTitles(String filepath, String tableName) throws SQLException, IOException {

        ArrayList<String> baike = Myutil.readByLine(filepath);
        Connection conn = opMysql.connSQL(Contant.testmysqlurl);
        for (String line : baike) {
            String[] words = line.split("\t");
            String title = words[0];
            String desc = words[1];
            String content = words[2];

            if (!title.contains("\'")) {
                String sql = "insert into testwiki." + tableName + "(title,des,content) values('" + title + "','" + desc + "','" + content + "');";
                System.out.println(sql);
                opMysql.insertSQL(conn, sql);
            }

//            System.exit(0);
        }
        opMysql.deconnSQL(conn);


    }

}
