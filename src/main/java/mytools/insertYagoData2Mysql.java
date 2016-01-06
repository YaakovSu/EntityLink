package mytools;

import com.mysql.jdbc.PreparedStatement;
import edu.util.Contant;
import edu.util.opMysql;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 主义，在进行查询的
 */

/**
 * Created by CBD_O9 on 2015-03-25.
 */
public class insertYagoData2Mysql {
    static String inputPath = "G:\\mytest\\yagoLabels.tsv";

    public static void main(String[] args) throws FileNotFoundException {
        String tableName = "yagolabels";
        insertYagoData(inputPath, tableName);

//        piliangInsertYagoData(inputPath);
    }

    private static void piliangInsertYagoData(String inputPath) {


        Connection conn = opMysql.connSQL(Contant.yagomysqlurl);

        File file = new File(inputPath);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
//一次读一行，读入null时文件结束
            int count = 1;
//            String insertsql1 = "insert into rsvm.we2yago.yagofacts values('myid','mynameA','myrelation','mynamB');";


            String insertsql = "insert into yago.yagoWikipediaInfo values(?,?,?,?);";

            PreparedStatement preStmt = (PreparedStatement) conn.prepareStatement(insertsql);

            while ((tempString = reader.readLine()) != null) {
                String[] temp = tempString.split("\t");
                String id = temp[0].replace("<", "").replace(">", "");
                String nameA = temp[1].replace("<", "").replace(">", "").replace("\'", "\"");
                String relation = temp[2].replace("<", "").replace(">", "");
                String nameB = temp[3].replace("<", "").replace(">", "").replace("\'", "\"");

                System.out.println("已完成：" + count);

//                if(count%10000000==0){

//                }else{
                preStmt.setString(1, id);
                preStmt.setString(2, nameA);
                preStmt.setString(3, relation);
                preStmt.setString(4, nameB);

                preStmt.addBatch();


//                }


                count++;
            }
            preStmt.executeBatch();
            preStmt.clearBatch();

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }


    }


    private static void insertYagoData(String inputPath, String tableName) throws FileNotFoundException {
        Connection conn = opMysql.connSQL(Contant.yagomysqlurl);

        File file = new File(inputPath);
        FileInputStream in = new FileInputStream(file);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
//一次读一行，读入null时文件结束
            int count = 1;


            StringBuilder insertsql = new StringBuilder("insert into rsvm.we2yago." + tableName + " values");
            while ((tempString = reader.readLine()) != null) {
                String[] temp = tempString.split("\t");
                String id = temp[0].replace("<", "").replace(">", "");
                String nameA = temp[1].replace("<", "").replace(">", "").replace("\'", "\"");
                String relation = temp[2].replace("<", "").replace(">", "");
                String nameB = temp[3].replace("<", "").replace(">", "").replace("\'", "\"");

                if (count > 0) {
                    if (count % 20000 == 0) {

                        System.out.println("已完成：" + count);

                        insertsql.append("('" + id + "','" + nameA + "','" + relation + "','" + nameB + "');");
                        System.out.println(insertsql);
                        opMysql.insertSQL(conn, insertsql.toString());
//                    System.exit(0);
                        insertsql.delete(0, insertsql.length());

                        insertsql = new StringBuilder("insert into rsvm.we2yago." + tableName + " values");


                    } else {

                        insertsql.append("('" + id + "','" + nameA + "','" + relation + "','" + nameB + "'),");

                    }
                }

                count++;
                System.out.println("已读到：" + count);

            }
//            System.out.println(insertsql.length());
            insertsql.delete(insertsql.length() - 1, insertsql.length());
            opMysql.insertSQL(conn, insertsql.toString());
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }

    }

}
