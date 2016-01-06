package edu.util;

import java.sql.*;

/**
 * Created by youngsu on 14-10-14.
 */
public class opMysql {


    // connect to MySQL
    public static Connection connSQL(String url) {
        Connection conn = null;
        String username = Contant.mysqluser;
        String password = Contant.mysqlpwd; // 加载驱动程序以连接数据库
        try {
            Class.forName(Contant.mysqlclassName);
            conn = DriverManager.getConnection(url, username, password);
        }
        //捕获加载驱动程序异常
        catch (ClassNotFoundException cnfex) {
            System.err.println(
                    "装载 JDBC/ODBC 驱动程序失败。");
            cnfex.printStackTrace();
        }
        //捕获连接数据库异常
        catch (SQLException sqlex) {
            System.err.println("无法连接数据库");
            sqlex.printStackTrace();
        }
        return conn;
    }

    // disconnect to MySQL
    public static void deconnSQL(Connection conn) {
        try {
            if (conn != null)
                conn.close();
        } catch (Exception e) {
            System.out.println("关闭数据库问题 ：");
            e.printStackTrace();
        }
    }

    // execute selection language
    public static ResultSet selectSQL(Connection conn, String sql) {
        ResultSet rs = null;
        try {
            rs = conn.prepareStatement(sql).executeQuery(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }


    // execute insertion language
    public static boolean insertSQL(Connection conn, String sql) {
        try {
            conn.prepareStatement(sql).executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("插入数据库时出错：");

            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("插入时出错：");
            e.printStackTrace();
        }
        return false;
    }

    public static void updateSQL(Connection conn, String sql, String wkFirstColum, String Baike, String BaikeFirstColum, String News, String title) {
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(sql);
            preparedStmt.setString(1, wkFirstColum);
            preparedStmt.setString(2, Baike);
            preparedStmt.setString(3, BaikeFirstColum);
            preparedStmt.setString(4, News);
            preparedStmt.setString(5, title);
            // execute the java preparedstatement
            preparedStmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("修改出错！");
        }

    }

    public static void updateSQL(Connection conn, String sql, String CandidateWiki_1, String CandidateWiki_2, String CandidateWiki_3, String title) {
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(sql);
            preparedStmt.setString(1, CandidateWiki_1);
            preparedStmt.setString(2, CandidateWiki_2);
            preparedStmt.setString(3, CandidateWiki_3);
            preparedStmt.setString(4, title);
            // execute the java preparedstatement
            preparedStmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("修改出错！");
        }

    }


}
