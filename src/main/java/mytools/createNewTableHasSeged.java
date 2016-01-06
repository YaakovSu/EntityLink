package mytools;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
import edu.util.Contant;
import edu.util.Myutil;
import edu.util.opMysql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CBD_O9 on 2014-12-21.
 */
public class createNewTableHasSeged {

//    static Connection enconn  = opMysql.connSQL(Contant.enmysqlurl);
//    static Connection zhconn = opMysql.connSQL(Contant.zhmysqlurl);

    /**
     * 应用于中文的清理，去掉特殊符号和英文
     *
     * @param fileContent
     * @return
     */
    private static String cleantext(String fileContent) {

        fileContent = fileContent.replaceAll("\\d+", "");
        String pattern = "([-+*/^()\\]\\[])";
        fileContent = fileContent.replaceAll(pattern, "");
        fileContent = fileContent.replace(".", "");

        fileContent = Myutil.getCleanText(fileContent);
        return fileContent;
    }


    public ArrayList<String> getTextSeg_Zh_Jianjie(String text1) throws IOException, ClassNotFoundException {

        ArrayList<String> text1MingCi = new ArrayList<String>();

        String[] text1Lines = text1.split("\n");


        /**
         * 只使用简介部分来进行处理
         */

        for (String line : text1Lines) {

            if (line.contains("'''") && (!line.startsWith("{")) && (!line.startsWith(" [")) && (!line.startsWith(" |")) && (!line.startsWith("[")) && (!line.startsWith(" {"))) {
                text1 = line;
                break;
            }
        }

//        System.exit(0);

        text1 = cleantext(text1);

        List<Term> results = NlpAnalysis.parse(text1);

        for (Term result : results) {
            if (result.getNatureStr().equals("n")) {  // W要改，改成名称的标识符
                System.out.print(result.getName() + " ");
                text1MingCi.add(result.getName());

            }

        }

        return text1MingCi;

    }

    public ArrayList<String> getTextSeg_En_Jianjie(String text2, MaxentTagger tagger) throws IOException, ClassNotFoundException {

        ArrayList<String> text2MingCi = new ArrayList<String>();


        String[] text2Lines = text2.split("\n");
//        System.out.println(text1);
//        System.exit(0);


        /**
         * 只使用简介部分来进行处理
         */


        for (String line : text2Lines) {
            if (line.contains("'''") && (!line.startsWith("{")) && (!line.startsWith(" [")) && (!line.startsWith(" |")) && (!line.startsWith("[")) && (!line.startsWith(" {"))) {
                text2 = line;
                break;
            }
        }


        String tagged = tagger.tagString(text2);
//        System.out.println(tagged);
        String[] taggedWords = tagged.split(" ");
        for (String taggedWord : taggedWords) {
            if (taggedWord.endsWith("/NN")) {
                if (taggedWord.length() < 20) {
                    String nun = taggedWord.split("/NN")[0];
//                    System.out.println(nun);
                    text2MingCi.add(nun);
                }

            }
        }

        return text2MingCi;

    }


    public ArrayList<String> getTextSeg_Zh_all(String text1) throws IOException, ClassNotFoundException {

        ArrayList<String> text1MingCi = new ArrayList<String>();


        text1 = cleantext(text1);

        List<Term> results = NlpAnalysis.parse(text1);

        for (Term result : results) {
            if (result.getNatureStr().equals("n")) {  // W要改，改成名称的标识符
                System.out.print(result.getName() + " ");
                text1MingCi.add(result.getName());

            }

        }

        return text1MingCi;

    }

    public ArrayList<String> getTextSeg_En_all(String text2, MaxentTagger tagger) throws IOException, ClassNotFoundException {

        ArrayList<String> text2MingCi = new ArrayList<String>();


        String tagged = tagger.tagString(text2);
//        System.out.println(tagged);
        String[] taggedWords = tagged.split(" ");
        for (String taggedWord : taggedWords) {
            if (taggedWord.endsWith("/NN")) {
                if (taggedWord.length() < 20) {
                    String nun = taggedWord.split("/NN")[0];
//                    System.out.println(nun);
                    text2MingCi.add(nun);
                }

            }
        }

        return text2MingCi;

    }


    public static String reformat(String message) {
        StringBuffer sb = new StringBuffer("");

        message = message.replace("&", "")
                .replace("<", "")
                .replace(">", "").replace("\"", "")
                .replace("'", "").replace("^", "").replace("\b", "");

        for (int i = 0; i < message.length(); i++) {
            char ch = message.charAt(i);
            if ((ch == 0x9) || (ch == 0xA) || (ch == 0xD)
                    || ((ch >= 0x20) && (ch <= 0xD7FF))
                    || ((ch >= 0xE000) && (ch <= 0xFFFD))
                    || ((ch >= 0x10000) && (ch <= 0x10FFFF)))
                sb.append(ch);
        }
        return sb.toString();
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        String model = "Project_EntityLinking/models/english-left3words-distsim.tagger";

        MaxentTagger tagger = new MaxentTagger(model);

        createNewTableHasSeged create = new createNewTableHasSeged();
        Connection enconn1 = opMysql.connSQL(Contant.enmysqlurl);
        Connection enconn = opMysql.connSQL(Contant.enmysqlurl);

        String enSelectsql1 = "SELECT * FROM enwiki.pagemapline;";

        ResultSet resultSet = opMysql.selectSQL(enconn1, enSelectsql1);
        int count = 1;
        String enSelectsql2 = null;
        ResultSet resultSet1 = null;

        String name = "";
        String redirectName = "";
        int pageId = -1;
        String text = "";
        StringBuilder textDesSeg = null;
        StringBuilder textAllSeg = null;
        ArrayList<String> desSeg = null;
        ArrayList<String> allseg = null;
        while (resultSet.next()) {
            if (count >= 5000) {               ////////把这个地方改成已完成后面的条数即可！！！！！！！！！！！！！！

                int id = resultSet.getInt("id");

//            String enSelectsql2 = "SELECT * FROM enwiki.pagemapline as a,enwiki.page as b where a.id="+id+" and a.pageID=b.pageId;";

                enSelectsql2 = "SELECT a.name as redirectName,b.pageId,b.name,b.text FROM enwiki.pagemapline as a,enwiki.page as b where a.id=" + id + " and a.pageID=b.pageId;";
                resultSet1 = opMysql.selectSQL(enconn, enSelectsql2);
                name = "";
                redirectName = "";
                pageId = -1;
                text = "";
                textDesSeg = new StringBuilder();
                textAllSeg = new StringBuilder();
                while (resultSet1.next()) {
                    pageId = resultSet1.getInt("pageId");
                    redirectName = resultSet1.getString("redirectName");
                    name = resultSet1.getString("name");
                    text = resultSet1.getString("text");


                    desSeg = create.getTextSeg_En_Jianjie(text, tagger);
                    allseg = create.getTextSeg_En_all(text, tagger);
                    text = reformat(text);
                    text = text.replace("\\'", "");

                    for (String seg : desSeg) {
                        seg = reformat(seg);
                        seg = seg.replace("\\'", "");


                        textDesSeg.append(" " + seg);

                    }

                    for (String seg : allseg) {
                        seg = reformat(seg);
                        seg.replace("\\'", "");

                        textAllSeg.append(" " + seg);
                    }

                }
//            String enInsertSql = "insert into enwiki.page_seg values("+id+","+pageId+",\""+redirectName+"\",'"+text+"','"+textDesSeg+"','"+textAllSeg+"');";

                String enInsertSql = "insert into enwiki.page_seg values(" + id + "," + pageId + ",\"" + redirectName + "\",\"" + name + "\",'" + text + "','" + textDesSeg + "','" + textAllSeg + "');";
                opMysql.insertSQL(enconn, enInsertSql);
                System.out.println("已完成：" + count);


//            if(count%50==0){
//
//                resultSet1.close();
//                enconn.close();
//                enconn = opMysq.connSQL(Contant.enmysqlurl);
//            }


            }
            count++;


        }
        enconn.close();

    }

}
