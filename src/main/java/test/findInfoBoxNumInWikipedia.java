package test;

import edu.util.Contant;
import edu.util.Myutil;
import edu.util.opMysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by CBD_O9 on 2015-04-06.
 */
public class findInfoBoxNumInWikipedia {
    public static ArrayList<String> getAllPageTitleHasInfoBox(String filePath) {
        ArrayList<String> result = new ArrayList<String>();

        ArrayList<String> semRes = Myutil.readByLine(filePath);
        for (String line : semRes) {
            String title = line.split("\t")[1];
            result.add(title);
        }

        return result;
    }

    public static void getallPersonCategory(Connection conn, String selectSql) throws SQLException {


        ResultSet resultSet1 = opMysql.selectSQL(conn, selectSql);
        while (resultSet1.next()) {
            int id = resultSet1.getInt(1);
            String name = resultSet1.getString(2);
            String selectsql = "SELECT b.id,b.name FROM zhwiki.category_outlinks as a,zhwiki.category as b where a.id =" + id + " and a.outLinks = b.id ;";

            ResultSet resultSet = opMysql.selectSQL(conn, selectsql);
            while (resultSet.next()) {
                int id1 = resultSet.getInt(1);
                String name1 = resultSet.getString(2);
                String insertSql = "insert into zhwiki.allpersoncategory values(" + id1 + ",'" + name1 + "');";

                opMysql.insertSQL(conn, insertSql);


//            ResultSet resultSet1 = opMysql.selectSQL(conn,sql);
//            while (resultSet1.next()){
//
//            }

            }

        }

    }


    public static HashSet<String> getAllPagesInRenWu() throws SQLException {
        String selectSql = "SELECT a.id,b.pages,c.name FROM zhwiki.allpersoncategory as a,zhwiki.category_pages as b,zhwiki.pagemapline as c where a.id=b.id and b.pages=c.id;";

        HashSet<String> result = new HashSet<String>();
        Connection conn = opMysql.connSQL(Contant.zhmysqlurl);
        ResultSet resultSet = opMysql.selectSQL(conn, selectSql);
        while (resultSet.next()) {
            String pageName = resultSet.getString("name");
            if (!result.contains(pageName)) {
                result.add(pageName);
            }

        }
        return result;
    }


    public static void main(String[] args) throws SQLException {


        //测试一
        String infoxFiles = "C:\\youngsu\\InfoboxExtractor\\data\\zhwiki-20141009-pages-articles.xml.infobox.cn";
        ArrayList<String> infoboxTitles = getAllPageTitleHasInfoBox(infoxFiles);
        HashSet<String> allperson = getAllPagesInRenWu();
        double count = 0;
        int pp = 0;
        for (String infoboxtitle : infoboxTitles) {
            if (allperson.contains(infoboxtitle)) {
                count++;
            }
            pp++;
            System.out.println("已完成：" + pp);
        }
        double rate = count / allperson.size();
        System.out.println(count);
        System.out.println(allperson.size());
        System.out.println(rate);

        //测试2
//        String selectSql = "SELECT * FROM zhwiki.allpersoncategory;";
//        Connection conn = opMysql.connSQL(Contant.zhmysqlurl);
//        getallPersonCategory(conn,selectSql);
//        conn.close();

    }
}
