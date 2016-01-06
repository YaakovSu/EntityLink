package mytools.traintools;

import edu.util.Contant;
import edu.util.opMysql;
import rsvm.qc2wc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by CBD_O9 on 2014-11-05.
 */
public class create_qc2wcTestDataSets {

    public static void insertCandidate2TestDataSet(Connection conn, String tableName, String name, String filecontent) throws SQLException {
        qc2wc cw = new qc2wc();

//        String name ="师范大学";
//        String filecontent ="简称华师，坐落于中国的经济、金融中心上海，是教育部直属、教育部与上海市共建的首批全国重点大学，是列入国家“985工程”、“211工程”、“2011计划”、“111计划”、“千人计划”重点建设的综合性研究型大学，[1] 是“长三角高校合作联盟”、“金砖国家大学联盟”和“亚太高校书院联盟”的重要成员。";

        ArrayList<String> result = cw.createCandidate_wc(filecontent, name);
        String updatesql = "";

        if (result.size() == 3) {
            String CandidateWiki_1 = result.get(0);
            String CandidateWiki_2 = result.get(1);
            String CandidateWiki_3 = result.get(2);

            updatesql = "update " + tableName + " set CandidateWiki_1=? ,CandidateWiki_2=? ,CandidateWiki_3=? where title=? ";
            opMysql.updateSQL(conn, updatesql, CandidateWiki_1, CandidateWiki_2, CandidateWiki_3, name);
        }
        if (result.size() == 2) {
            String CandidateWiki_1 = result.get(0);
            String CandidateWiki_2 = result.get(1);
            String CandidateWiki_3 = "NA";

            updatesql = "update " + tableName + " set CandidateWiki_1=? ,CandidateWiki_2=? ,CandidateWiki_3=? where title=? ";
            opMysql.updateSQL(conn, updatesql, CandidateWiki_1, CandidateWiki_2, CandidateWiki_3, name);
        }
        if (result.size() == 1) {
            String CandidateWiki_1 = result.get(0);
            String CandidateWiki_2 = "NA";
            String CandidateWiki_3 = "NA";

            updatesql = "update " + tableName + " set CandidateWiki_1=? ,CandidateWiki_2=? ,CandidateWiki_3=? where title=? ";
            opMysql.updateSQL(conn, updatesql, CandidateWiki_1, CandidateWiki_2, CandidateWiki_3, name);
        }


        System.out.println(result);
    }

    public static void main(String[] args) throws SQLException {
        Connection conn = opMysql.connSQL(Contant.testmysqlurl);
//        String tableName = "testbaike_culture";

        ArrayList<String> tableNames = new ArrayList<String>();
//        tableNames.add("testbaike_economic");
        tableNames.add("testbaike_edu");
        tableNames.add("testbaike_food");
        tableNames.add("testbaike_geo");
        tableNames.add("testbaike_life");
        tableNames.add("testbaike_person");
        tableNames.add("testbaike_science");
        for (String tableName : tableNames) {
            String selectsql = "select * from " + tableName + ";";
            ResultSet resultSet = opMysql.selectSQL(conn, selectsql);
            while (resultSet.next()) {

                String title = resultSet.getString("title");
                String content = resultSet.getString("des");
                if ((!title.contains("\'")) && (title.length() > 1) && (content.length() > 10)) {
                    insertCandidate2TestDataSet(conn, tableName, title, content);
                }

            }
        }
        opMysql.deconnSQL(conn);


    }
}
