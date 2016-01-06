package mytools;

import edu.util.Contant;
import edu.util.opMysql;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by CBD_O9 on 2014-11-06.
 */
public class GetPig_OutlinkText {

    public static void main(String[] args) throws IOException, SQLException {
        String PageMaplinePath = "Project_EntityLinking/output/PageMapline.output";
        String pageOutLinkPath = "Project_EntityLinking/output/pageOutLinkPath.output";
//        System.out.println("第一步");
//        getPageMapline(PageMaplinePath);
        System.out.println("第二步");
        getPageOutLink(pageOutLinkPath);

    }

    private static void getPageMapline(String pageMaplinePath) throws SQLException, IOException {
        FileWriter fw = new FileWriter(pageMaplinePath);
        Connection conn = opMysql.connSQL(Contant.enmysqlurl);
        String selectsql = "SELECT pageID,name FROM enwiki.pagemapline;";
        ResultSet resultSet = opMysql.selectSQL(conn, selectsql);
        int count = 0;
        while (resultSet.next()) {
            count++;
            int pageID = resultSet.getInt("pageID");
            String name = resultSet.getString("name");
            System.out.println("已查了:" + count);
            String pageIDs = String.valueOf(pageID);
            fw.write(pageIDs + "," + name);
        }
        fw.close();
        conn.close();
    }

    private static void getPageOutLink(String pageOutLinkPath) throws SQLException, IOException {
        FileWriter fw = new FileWriter(pageOutLinkPath);
        Connection conn = opMysql.connSQL(Contant.enmysqlurl);

//        int times = 10000;

        for (int i = 0; i < 292180000; i += 100000000) {
            String selectsql = "SELECT id,outLinks FROM enwiki.page_outlinks limit " + i + ",100000000;";
            ResultSet resultSet = opMysql.selectSQL(conn, selectsql);
            int count = 0;
            while (resultSet.next()) {
                count++;
                int id = resultSet.getInt("id");
                int outLinks = resultSet.getInt("outLinks");
                int times = i + count;
                System.out.println("已查了:" + times);
                String ids = String.valueOf(id);
                String outLinkss = String.valueOf(outLinks);
                fw.write(ids + "," + outLinkss);
            }
        }
        String selectsql = "SELECT id,outLinks FROM enwiki.page_outlinks limit " + 200000000 + ",92180000;";
        ResultSet resultSet = opMysql.selectSQL(conn, selectsql);
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            int outLinks = resultSet.getInt("outLinks");
//                System.out.println("已查了:"+);
            String ids = String.valueOf(id);
            String outLinkss = String.valueOf(outLinks);
            fw.write(ids + "," + outLinkss);
        }


        fw.close();
        conn.close();
    }
}
