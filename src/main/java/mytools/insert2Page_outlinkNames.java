package mytools;

import edu.util.Contant;
import edu.util.opMysql;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by CBD_O9 on 2014-11-18.
 */
public class insert2Page_outlinkNames {

    public static void main(String[] args) {

        String filePath = "C:\\youngsu\\wikidata\\pageNames_outlinksNames.output";


        File file = new File(filePath);
        BufferedReader reader = null;
        try {


            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
//一次读一行，读入null时文件结束
            int count = 0;
            String insertsql = "";
            Connection conn = opMysql.connSQL(Contant.enmysqlurl);

            while ((tempString = reader.readLine()) != null) {
//                if(count>=150053000){

                if (count % 1000 == 0) {


                    opMysql.insertSQL(conn, insertsql);
                    conn.close();
                    conn = opMysql.connSQL(Contant.enmysqlurl);

                    String[] line = tempString.split("\t");
//                        int id = Integer.parseInt(line[0]);
//                        int outlinkID = Integer.parseInt(line[1]);
//                        String outlinkName = line[2];

                    String pageName = line[0];
                    String outlinkName = line[1];
                    insertsql = "insert into enwiki.pagenames_outlinknames(pageName,outlinkName) values('" + pageName + "','" + outlinkName + "')";
                } else if ((count % 1000 > 0) && (count < 292184596)) {
                    String[] line = tempString.split("\t");
//                        int id = Integer.parseInt(line[0]);
//                        int outlinkID = Integer.parseInt(line[1]);
//                        String outlinkName = line[2];

                    String pageName = line[0];
                    String outlinkName = line[1];

                    insertsql = insertsql + ",('" + pageName + "','" + outlinkName + "')";
                } else if (count == 292184596) {
                    String[] line = tempString.split("\t");
//                        int id = Integer.parseInt(line[0]);
//                        int outlinkID = Integer.parseInt(line[1]);
//                        String outlinkName = line[2];
                    String pageName = line[0];
                    String outlinkName = line[1];


                    insertsql = insertsql + ",('" + pageName + "','" + outlinkName + "');";
                    opMysql.insertSQL(conn, insertsql);
                }

//                }
                count++;
                System.out.println("已经完成了 " + count);


//                String[] line = tempString.split("\t");
//                int id = Integer.parseInt(line[0]);
//                int outlinkID = Integer.parseInt(line[1]);
//                String outlinkName = line[2];
//
//                String insertsql = "insert into enwiki.page_outlinks_names(id,outlinkID,outlinkName) values("+id+","+outlinkID+",'"+outlinkName+"');";
////                System.out.println(insertsql);
////                System.out.println(conn);
//                opMysql.insertSQL(conn,insertsql);


            }
            reader.close();
            opMysql.deconnSQL(conn);
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


}
