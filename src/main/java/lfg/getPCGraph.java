package lfg;

import edu.util.Contant;
import edu.util.Myutil;
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

/**
 * Created by CBD_O9 on 2015-04-17.
 */
public class getPCGraph {


    public static void main(String[] args) throws SQLException, IOException {
        String allNodesPath = "C:\\youngsu\\lijuanziData\\allData.tsv";
        HashSet<String> allZhNodes = new HashSet<String>();
        HashSet<String> allEnNodes = new HashSet<String>();

        ArrayList<String> nodes = Myutil.readByLine(allNodesPath);
        for (String line : nodes) {
            String[] words = line.split("\t");
            String zhName = words[0];
            String enName = words[1];
            allZhNodes.add(zhName);
            allEnNodes.add(enName);
        }
//        HashMap<String,ArrayList<String>> zhMap = getZhOutLinks(allZhNodes);
//        HashMap<String,ArrayList<String>> enMap = getEnOutLinks(allEnNodes);
//        getMyPCGraph(zhMap,enMap);

        HashMap<String, ArrayList<String>> zhMap = getZhInLinks(allZhNodes);
        HashMap<String, ArrayList<String>> enMap = getEnInLinks(allEnNodes);

    }

    public static void getMyPCGraph(HashMap<String, ArrayList<String>> zhMap, HashMap<String, ArrayList<String>> enMap) throws IOException {
        String outputPath = "C:\\youngsu\\lijuanziData\\PCGraph.tsv";
        FileWriter fw = new FileWriter(outputPath);
        Iterator<String> zhIterator = zhMap.keySet().iterator();
        while (zhIterator.hasNext()) {
            String zhKey = zhIterator.next();
            ArrayList<String> zhValues = zhMap.get(zhKey);
            Iterator<String> enIterator = enMap.keySet().iterator();
            while (enIterator.hasNext()) {
                String enKey = enIterator.next();
                ArrayList<String> enValues = enMap.get(enKey);
                String nodeA = zhKey + "\t" + enKey;
                for (String zhValue : zhValues) {
                    for (String enValue : enValues) {
                        String nodeB = zhValue + "\t" + enValue;

                        fw.write(nodeA + "\t\t" + nodeB + "\n");
                    }

                }
                fw.flush();
            }
        }
        fw.close();

    }

    public static HashMap<String, ArrayList<String>> getZhInLinks(HashSet<String> allZhNodes) throws SQLException, IOException {

        FileWriter fw = new FileWriter("C:\\youngsu\\lijuanziData\\allzhEdge_inlink.tsv");
        HashMap<String, ArrayList<String>> result = new HashMap<String, ArrayList<String>>();

        int count = 0;
        for (String zhNode : allZhNodes) {
            System.out.println("已完成：" + count);
            count++;
            ArrayList<String> allInlinks = getAllZhInlinks(zhNode);
            ArrayList<String> values = new ArrayList<String>();
            fw.write(zhNode);

            for (String inlink : allInlinks) {
                if (allZhNodes.contains(inlink)) {

                    values.add(inlink);
                    fw.write("\t" + inlink);
                }
            }
            fw.write("\n");
            fw.flush();
            result.put(zhNode, values);
        }
        fw.close();
        return result;

    }

    private static ArrayList<String> getAllZhInlinks(String zhNode) throws SQLException {
        ArrayList<String> result = new ArrayList<String>();
        Connection conn = opMysql.connSQL(Contant.zhmysqlurl);
        if (!zhNode.contains("'")) {
            String selectCountSql = "SELECT inlinks FROM zhwiki.page_inlinks as a,zhwiki.pagemapline as b where a.id = b.id and b.name='" + zhNode + "';";
            ResultSet resultSet = opMysql.selectSQL(conn, selectCountSql);
            while (resultSet.next()) {
                int inlink = resultSet.getInt(1);
                String select = "select * from zhwiki.pagemapline  where id =" + inlink + ";";
                ResultSet resultSet1 = opMysql.selectSQL(conn, select);
                while (resultSet1.next()) {
                    String name = resultSet1.getString(2);
                    result.add(name);
                }
//            System.out.println("gao"+count);
            }
        }

        conn.close();
        return result;
    }


    private static HashMap<String, ArrayList<String>> getEnInLinks(HashSet<String> allEnNodes) throws IOException, SQLException {
        FileWriter fw = new FileWriter("C:\\youngsu\\lijuanziData\\allenEdge_inlinks.tsv");
        HashMap<String, ArrayList<String>> result = new HashMap<String, ArrayList<String>>();

        int count = 0;
        for (String enNode : allEnNodes) {
            System.out.println("已完成：" + count);
            count++;
            ArrayList<String> allInlinks = getAllEnInlinklinks(enNode);
            ArrayList<String> values = new ArrayList<String>();
            fw.write(enNode);

            for (String inlink : allInlinks) {
                if (allEnNodes.contains(inlink)) {

                    values.add(inlink);
                    fw.write("\t" + inlink);
                }
            }
            fw.write("\n");
            fw.flush();
            result.put(enNode, values);
        }
        fw.close();
        return result;
    }

    private static ArrayList<String> getAllEnInlinklinks(String enNode) throws SQLException {
        ArrayList<String> result = new ArrayList<String>();
        Connection conn = opMysql.connSQL(Contant.enmysqlurl);
        if (!enNode.contains("'")) {
            String selectCountSql = "SELECT inlinks FROM enwiki.page_inlinks as a,enwiki.pagemapline as b where a.id = b.id and b.name='" + enNode + "';";
            ResultSet resultSet = opMysql.selectSQL(conn, selectCountSql);
            while (resultSet.next()) {
                int inlink = resultSet.getInt(1);
                String select = "select * from enwiki.pagemapline  where id =" + inlink + ";";
                ResultSet resultSet1 = opMysql.selectSQL(conn, select);
                while (resultSet1.next()) {
                    String name = resultSet1.getString(2);
                    result.add(name);
                }
//            System.out.println("gao"+count);
            }
        }

        conn.close();
        return result;
    }


    private static HashMap<String, ArrayList<String>> getEnOutLinks(HashSet<String> allEnNodes) throws IOException, SQLException {
        FileWriter fw = new FileWriter("C:\\youngsu\\lijuanziData\\allenEdge.tsv");
        HashMap<String, ArrayList<String>> result = new HashMap<String, ArrayList<String>>();

        int count = 0;
        for (String enNode : allEnNodes) {
            System.out.println("已完成：" + count);
            count++;
            ArrayList<String> allOutlinks = getAllEnOutlinks(enNode);
            ArrayList<String> values = new ArrayList<String>();
            fw.write(enNode);

            for (String outlink : allOutlinks) {
                if (allEnNodes.contains(outlink)) {

                    values.add(outlink);
                    fw.write("\t" + outlink);
                }
            }
            fw.write("\n");
            fw.flush();
            result.put(enNode, values);
        }
        fw.close();
        return result;
    }

    private static ArrayList<String> getAllEnOutlinks(String enNode) throws SQLException {
        ArrayList<String> result = new ArrayList<String>();
        Connection conn = opMysql.connSQL(Contant.enmysqlurl);
        if (!enNode.contains("'")) {
            String selectCountSql = "SELECT outlinks FROM enwiki.page_outlinks as a,enwiki.pagemapline as b where a.id = b.id and b.name='" + enNode + "';";
            ResultSet resultSet = opMysql.selectSQL(conn, selectCountSql);
            while (resultSet.next()) {
                int outlink = resultSet.getInt(1);
                String select = "select * from enwiki.pagemapline  where id =" + outlink + ";";
                ResultSet resultSet1 = opMysql.selectSQL(conn, select);
                while (resultSet1.next()) {
                    String name = resultSet1.getString(2);
                    result.add(name);
                }
//            System.out.println("gao"+count);
            }
        }

        conn.close();
        return result;
    }

    public static HashMap<String, ArrayList<String>> getZhOutLinks(HashSet<String> allZhNodes) throws SQLException, IOException {

        FileWriter fw = new FileWriter("C:\\youngsu\\lijuanziData\\allzhEdge.tsv");
        HashMap<String, ArrayList<String>> result = new HashMap<String, ArrayList<String>>();

        int count = 0;
        for (String zhNode : allZhNodes) {
            System.out.println("已完成：" + count);
            count++;
            ArrayList<String> allOutlinks = getAllZhOutlinks(zhNode);
            ArrayList<String> values = new ArrayList<String>();
            fw.write(zhNode);

            for (String outlink : allOutlinks) {
                if (allZhNodes.contains(outlink)) {

                    values.add(outlink);
                    fw.write("\t" + outlink);
                }
            }
            fw.write("\n");
            fw.flush();
            result.put(zhNode, values);
        }
        fw.close();
        return result;

    }

    private static ArrayList<String> getAllZhOutlinks(String zhNode) throws SQLException {
        ArrayList<String> result = new ArrayList<String>();
        Connection conn = opMysql.connSQL(Contant.zhmysqlurl);
        if (!zhNode.contains("'")) {
            String selectCountSql = "SELECT outlinks FROM zhwiki.page_outlinks as a,zhwiki.pagemapline as b where a.id = b.id and b.name='" + zhNode + "';";
            ResultSet resultSet = opMysql.selectSQL(conn, selectCountSql);
            while (resultSet.next()) {
                int outlink = resultSet.getInt(1);
                String select = "select * from zhwiki.pagemapline  where id =" + outlink + ";";
                ResultSet resultSet1 = opMysql.selectSQL(conn, select);
                while (resultSet1.next()) {
                    String name = resultSet1.getString(2);
                    result.add(name);
                }
//            System.out.println("gao"+count);
            }
        }

        conn.close();
        return result;
    }
}
