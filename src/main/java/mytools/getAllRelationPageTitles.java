package mytools;

import edu.util.Contant;
import edu.util.opMysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by CBD_O9 on 2015-03-28.
 */
public class getAllRelationPageTitles {

    //获得所有的inlink和outlink
    public static ArrayList<String> getAllRelationTitles(String wikiInputTitle) throws SQLException {
        Connection conn = opMysql.connSQL(Contant.zhmysqlurl);

        ArrayList<String> result = getAllInlinkTitle(conn, wikiInputTitle);
        ArrayList<String> adds = getAllOutlinkTitle(conn, wikiInputTitle);
        for (String add : adds) {
            result.add(add);
        }
        conn.close();
        return result;
    }

    private static ArrayList<String> getAllInlinkTitle(Connection conn, String wikiInputTitle) throws SQLException {

        ArrayList<String> result = new ArrayList<String>();
//        String selectInlinkSql = "SELECT * FROM zhwiki.page_inlinks as a,zhwiki.pagemapline as b where a.id = b.id and b.name='" + wikiInputTitle + "';";


        String selectInlinkSql = "select name from (SELECT inLinks FROM zhwiki.page_inlinks as a,zhwiki.pagemapline as b where a.id = b.id and b.name='" + wikiInputTitle + "') as a,pagemapline as b where a.inLinks = b.id;";

        ResultSet resultSet = opMysql.selectSQL(conn, selectInlinkSql);
        while (resultSet.next()) {
            String inlink = resultSet.getString("name"); //里面存的是inlink的ID
            result.add(inlink);

        }

        return result;
    }

    private static ArrayList<String> getAllOutlinkTitle(Connection conn, String wikiInputTitle) throws SQLException {

        ArrayList<String> result = new ArrayList<String>();
        String selectOutlinkSql = "select name from (SELECT outLinks FROM zhwiki.page_outlinks as a,zhwiki.pagemapline as b where a.id = b.id and b.name='" + wikiInputTitle + "') as a,pagemapline as b where a.outLinks = b.id;";
        ResultSet resultSet = opMysql.selectSQL(conn, selectOutlinkSql);
        while (resultSet.next()) {
            String outlink = resultSet.getString("name"); //里面存的是inlink的ID
            result.add(outlink);

        }

        return result;
    }


}
