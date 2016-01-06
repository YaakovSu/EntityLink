package rsvm;

import edu.Constant;
import edu.nlp.CandidateDic;
import edu.nlp.DirectDic;
import edu.nlp.DisamDic;
import edu.nlp.ECDic;
import edu.util.Contant;
import edu.util.Myutil;
import edu.util.opMysql;


import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static edu.nlp.Rank.rankTitles;


/**
 * Created by youngsu on 14-10-29.
 */
public class qc2wc {
    //name 为file中要查看候选
    public ArrayList<String> createCandidate_wc(String fileContent, String name) throws SQLException {
        fileContent = fileContent.replaceAll("\\d+", "");
        String pattern = "([-+*/^()\\]\\[])";
        fileContent = fileContent.replaceAll(pattern, "");
        fileContent = fileContent.replace(".", "");
        fileContent = Myutil.getCleanText(fileContent);

//        CandidateDic dic = new CandidateDic(Contant.combin_disambigation_redirectOutput_Path);
        DirectDic directDic = new DirectDic(Contant.redirectOutput_Path);
        DisamDic disamDic = new DisamDic(Contant.clean_disambigationOutput_Path);
        ArrayList<String> wcresult = new ArrayList<String>();
        if (disamDic.containsKey(name)) {
            System.out.println("在消歧义中直接出现");
            ArrayList<String> titles = disamDic.getDisamPage(name);
            ArrayList<String> ranktitles = rankTitles(titles, fileContent);  //对name的候选词条排序后的结果
            wcresult = getranktitles(ranktitles, Contant.CandidatePageNum); //取4个作为wc ->we的候选
        } else if (directDic.containGivenWord_Fuzzy(name)) {
            System.out.println("在重定向中出现");
            ArrayList<String> directNames = directDic.getRedirectPage(name);
//            for (String directName : directNames) {
            String directName = directNames.get(0);
            if (disamDic.containsKey(directName)) {
                System.out.println("且在消歧义中出现");
                ArrayList<String> titles = disamDic.getDisamPage(directName);
                ArrayList<String> finalTitles = new ArrayList<String>();
                for (String title : titles) {
                    if (directDic.containGivenWord(title)) {
                        finalTitles.add(directDic.getRedirectPage(title).get(0));
                    } else {
                        finalTitles.add(title);
                    }
                }
                System.out.println(titles);
                System.out.println(finalTitles);

                ArrayList<String> ranktitles = rankTitles(finalTitles, fileContent);  //对name的候选词条排序后的结果
                System.out.println(ranktitles);
                wcresult = getranktitles(ranktitles, Contant.CandidatePageNum); //取4个作为wc ->we的候选
            } else {
                System.out.println("在重定向中存在，然而却没有歧义");
                wcresult = useRSVM(directName, fileContent);
            }
//            }
        } else {
            wcresult = useRSVMfuzzy(name, fileContent);
        }
        return wcresult;
    }

    private ArrayList<String> useRSVM(String name, String fileContent) throws SQLException {

        ArrayList<String> wcresult = new ArrayList<String>();
        ArrayList<String> titles = new ArrayList<String>();
        String mehuChaxunSql = "SELECT * FROM zhwiki.pagemapline where name like '" + name + "';";
        Connection conn = opMysql.connSQL(Contant.zhmysqlurl);
        ResultSet resultSet = opMysql.selectSQL(conn, mehuChaxunSql);
        while (resultSet.next()) {
            String sqlname = resultSet.getString("name");
            titles.add(sqlname);
        }
        opMysql.deconnSQL(conn);
        if (!titles.isEmpty()) {
            ArrayList<String> ranktitles = rankTitles(titles, fileContent);
            wcresult = getranktitles(ranktitles, Contant.CandidatePageNum); //取4个作为wc ->we的候选
        } else {
            wcresult.add("查无此词");
        }
        return wcresult;

    }

    private ArrayList<String> useRSVMfuzzy(String name, String fileContent) throws SQLException {
        System.out.println("在重定向和消歧义中都没有,启动RSVM模型");
        ArrayList<String> wcresult = new ArrayList<String>();
        ArrayList<String> titles = new ArrayList<String>();
        String mehuChaxunSql = "SELECT * FROM zhwiki.pagemapline where name like '%" + name + "%';";
        Connection conn = opMysql.connSQL(Contant.zhmysqlurl);
        ResultSet resultSet = opMysql.selectSQL(conn, mehuChaxunSql);
        while (resultSet.next()) {
            String sqlname = resultSet.getString("name");
            titles.add(sqlname);
        }
        opMysql.deconnSQL(conn);
        if (!titles.isEmpty()) {
            ArrayList<String> ranktitles = rankTitles(titles, fileContent);
            wcresult = getranktitles(ranktitles, Contant.CandidatePageNum); //取4个作为wc ->we的候选
        } else {
            wcresult.add("查无此词");
        }
        return wcresult;

    }

    private ArrayList<String> getranktitles(ArrayList<String> ranktitles, int num) {
        ArrayList<String> result = new ArrayList<String>();
        int size = ranktitles.size();
        if (size <= num) {
            for (String ranktitle : ranktitles) {

                System.out.println(ranktitle);
                result.add(ranktitle);
            }
        } else {
            for (int i = 0; i < num; i++) {
                System.out.println(ranktitles.get(i));
                result.add(ranktitles.get(i));
            }
        }
        return result;
    }


    public String writeOneNameSVMtrainningData(String name, String filecontent, ECDic E_CDic) throws SQLException, IOException, ClassNotFoundException {

        long timeTestStart = System.currentTimeMillis();
        boolean pre_inDic = MDicContain(name);
        if (pre_inDic) {
            String outputPath = Constant.QCWEinKCL + name + Constant.NAMETAILE;
            String enName = getEnNameInMDic(name);
            printenName(enName, outputPath);
            return enName;
        }
        ArrayList<String> results = createCandidate_wc(filecontent, name);
//        因为我把rank改掉了，所以这里的result有score信息在。
        System.out.println(results);
        boolean inDic = false;
        if (results.size() > 0) {
            for (int i = 0; i < results.size(); i++) {
                String zhWikiTitle = results.get(i).split("\t")[0];
                inDic = MDicContain(zhWikiTitle);
                if (inDic) {
                    String outputPath = Constant.QCWEinKCL + zhWikiTitle + Constant.NAMETAILE;
                    String enName = getEnNameInMDic(zhWikiTitle);
                    printenName(enName, outputPath);
                    return enName;
                }
            }
            if (!inDic) {
                for (int i = 0; i < results.size(); i++) {
                    String result = results.get(i).split("\t")[0];
                    if (!result.equals("查无此词")) {

                        wc2we f2 = new wc2we();
                        String output1 = Constant.QCWE_NAME + name + "_" + i + Constant.NAMETAILE;
                        String output2 = Constant.QCWE_FEATURE + name + "_" + i + Constant.FEATURETAILE;

                        String enName = f2.myFangAn(result, output1, output2, E_CDic, name);
                        return enName;

                    }
                }
            }
        }
        long timeTestEnd = System.currentTimeMillis();
        System.out.println("运行时间是" + (timeTestEnd - timeTestStart));
        return "";
    }

    private String getEnNameInMDic(String zhWikiTitle) {

        ArrayList<String> lines = Myutil.readByLine(Contant.MDicInputPath);

        for (String line : lines) {

            String[] name = line.split("=");
            String enName = name[0];
            String zhName = name[1];
            if (zhName.equals(zhWikiTitle)) {
                return enName;
            }
        }

        return "";
    }


    private boolean MDicContain(String zhWikiTitle) {
        ArrayList<String> lines = Myutil.readByLine(Contant.MDicInputPath);
        for (String line : lines) {

            String[] name = line.split("=");
            String enName = name[0];
            String zhName = name[1];
            if (zhName.equals(zhWikiTitle)) {
                return true;
            }
        }

        return false;
    }

    private void printenName(String enName, String output) throws IOException {

        FileWriter fw = new FileWriter(output);
        fw.write(enName + "\n");
        fw.flush();
        fw.close();
    }

}
