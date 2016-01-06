package rsvm;

import edu.util.opMysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by sunkai on 2015/3/24.
 * 定义一个对象，yagofact，yagotypeof都定义为这个类的一个对象
 */
public class we2yago {

    private Connection conn = null;

    public we2yago(Connection conn) {
        this.conn = conn;
    }

    public ArrayList<String> getRelation(String nameA, String nameB, String tag) throws SQLException {

        ArrayList<String> result = new ArrayList<String>();
        String selectSql = "SELECT nameA,relation,nameB FROM yago." + tag + " where nameA = '" + nameA + "' and nameB = '" + nameB + "';";
        ResultSet resultSet = opMysql.selectSQL(conn, selectSql);
        while (resultSet.next()) {
            nameA = resultSet.getString("nameA");
            String relation = resultSet.getString("relation");
            nameB = resultSet.getString("nameB");
            result.add(nameA + "\t" + relation + "\t" + nameB);
        }

        return result;
    }

    public ArrayList<String> getNameBbyRelation(String nameA, String relation, String tag) throws SQLException {
        ArrayList<String> result = new ArrayList<String>();
        String selectSql = "SELECT nameB FROM yago." + tag + " where nameA = '" + nameA + "' and relation = '" + relation + "';";
        ResultSet resultSet = opMysql.selectSQL(conn, selectSql);
        while (resultSet.next()) {

            String nameB = resultSet.getString("nameB");
            result.add(nameB);
        }

        return result;
    }

    public ArrayList<String> getallNameB(String nameA, String tag) throws SQLException {
        ArrayList<String> result = new ArrayList<String>();
        String selectSql = "SELECT nameB FROM yago." + tag + " where nameA = '" + nameA + "';";
        ResultSet resultSet = opMysql.selectSQL(conn, selectSql);
        while (resultSet.next()) {

            String nameB = resultSet.getString("nameB");
            result.add(nameB);
        }

        return result;
    }

    public ArrayList<String> getallRelations(String nameA, String tag) throws SQLException {
        ArrayList<String> result = new ArrayList<String>();
        String selectSql = "SELECT relation FROM yago." + tag + " where nameA = '" + nameA + "';";
        ResultSet resultSet = opMysql.selectSQL(conn, selectSql);
        while (resultSet.next()) {

            String relation = resultSet.getString("relation");
            result.add(relation);
        }

        return result;
    }

    /**
     * 获取以nameA开头的所有关系
     *
     * @param nameA
     * @param tag
     * @return
     * @throws SQLException
     */
    public ArrayList<String> getAllNameBandRelations(String nameA, String tag) throws SQLException {
        ArrayList<String> result = new ArrayList<String>();
        String selectSql = "SELECT nameA,relation,nameB FROM yago." + tag + " where nameA = '" + nameA + "';";
        ResultSet resultSet = opMysql.selectSQL(conn, selectSql);
        while (resultSet.next()) {
            nameA = resultSet.getString("nameA");
            String relation = resultSet.getString("relation");
            String nameB = resultSet.getString("nameB");
            result.add(nameA + "\t" + relation + "\t" + nameB);
        }

        return result;
    }

    /**
     * 输出name在nameA或者nameB的所有关系
     *
     * @param name
     * @param tag
     * @return
     * @throws SQLException
     */
    public ArrayList<String> getAllAssociateNameRelation(String name, String tag) throws SQLException {
        ArrayList<String> result = new ArrayList<String>();
        String selectSql = "SELECT nameA,relation,nameB FROM yago." + tag + " where nameA = '" + name + "' or nameB= '" + name + "';";
        ResultSet resultSet = opMysql.selectSQL(conn, selectSql);
        while (resultSet.next()) {
            String nameA = resultSet.getString("nameA");
            String relation = resultSet.getString("relation");
            String nameB = resultSet.getString("nameB");
            result.add(nameA + "\t" + relation + "\t" + nameB);
        }

        return result;
    }

}
