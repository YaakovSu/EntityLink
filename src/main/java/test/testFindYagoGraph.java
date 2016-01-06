package test;

import rsvm.we2yago;
import edu.util.Contant;
import edu.util.opMysql;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by sunkai on 2015/3/24.
 */
public class testFindYagoGraph {
    public static void main(String[] args) throws SQLException {


/*        long startTime=System.currentTimeMillis();   //获取开始时间



        System.out.println("开始读入hashMap.....");

        rsvm.we2yago yagoFact = new rsvm.we2yago(Myutil.readText2HashMap(Contant.YagoFactinputPath),Myutil.readText2HashMapFromEnd(Contant.YagoFactinputPath));

        long endTime=System.currentTimeMillis(); //获取结束时间

        System.out.println("程序运行时间： "+(endTime-startTime)+"ms");

//        rsvm.we2yago yagoType = new rsvm.we2yago(Myutil.readText2HashMap(Contant.YagoTypesinputPath));
        System.out.println("开始执行查找..........");
        String nameA = "Hong_Taiji";
//        ArrayList<String[]> testResult = yagoFact.getAllNameBandRelations(nameA);
        ArrayList<String[]> testResult = yagoFact.getAllAssociateNameRelation(nameA);
        for(String[] line : testResult){
            System.out.println(line[0]+" : "+line[1]+" : "+line[2]);
        }*/


        //测试一
        String filePath = "G:\\yago2s_tsv\\yagoLabels.tsv";
//        findAttributeInFiles(filePath);


        //测试二

        long startTime = System.currentTimeMillis();   //获取开始时间
        Connection conn = opMysql.connSQL(Contant.yagomysqlurl);

        we2yago yagoFact = new we2yago(conn);

        long endTime = System.currentTimeMillis(); //获取结束时间

        String nameA = "Chinese_embroidery";
        ArrayList<String> testResult = yagoFact.getAllAssociateNameRelation(nameA, Contant.yagofacts);
        ArrayList<String> testType = yagoFact.getAllNameBandRelations(nameA, Contant.yagotypes);
        ArrayList<String> testWikipediainfo = yagoFact.getAllAssociateNameRelation(nameA, Contant.yagowikipediainfo);

        for (String line : testResult) {
            System.out.println(line);
        }

        System.out.println("****************************************************");
        for (String line : testType) {
            System.out.println(line);
        }

        System.out.println("******************************************************");
        for (String line : testWikipediainfo) {
            System.out.println(line);
        }


        System.out.println("程序运行时间： " + (endTime - startTime) + "ms");
    }

    private static void findAttributeInFiles(String filePath) {
        File file = new File(filePath);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
//一次读一行，读入null时文件结束
            while ((tempString = reader.readLine()) != null) {
                System.out.println(tempString);
                System.exit(0);
            }
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
