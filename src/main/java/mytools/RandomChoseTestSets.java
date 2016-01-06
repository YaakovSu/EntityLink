package mytools;

import edu.util.Contant;
import edu.util.Myutil;
import edu.util.opMysql;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * 随机从Disambigation词典和redirect词典中各选择100条记录
 * Created by youngsu on 14-10-31.
 */
public class RandomChoseTestSets {
    public static ArrayList<Integer> Randomfind(int size, int N) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        int count = 0;
        while (count < N) {
            int x = (int) (Math.random() * size);
            if (!result.contains(x)) {
                count++;
                result.add(x);
            }
        }
        return result;

    }

    private static ArrayList<String> getRandomDisambigationTitles(String filePath, int N) {
        ArrayList<String> result = new ArrayList<String>();

        ArrayList<String> lines = Myutil.readByLine(filePath);
        int size = lines.size();
        ArrayList<Integer> testRandom = Randomfind(size, N);
        for (int i : testRandom) {
            String line = lines.get(i);
            String text = line.split("\t")[1];
            ArrayList<String> words = Myutil.usePatterns_firstListWord(text);
            ArrayList<Integer> random = Randomfind(words.size(), 1);
            String word = words.get(random.get(0));
//            System.out.println(word);
            result.add(word);
        }
        return result;
    }


    public static void writeDisamTitles(String filepath) throws SQLException, IOException {
        FileWriter fw = new FileWriter(filepath);
        ArrayList<String> disamWords = getRandomDisambigationTitles(Contant.combin_disambigation_redirectOutput_Path, 55);
        System.out.println(disamWords.size());
        Connection conn = opMysql.connSQL(Contant.zhmysqlurl);
        for (String disamWord : disamWords) {
            String sql = "insert into zhwiki.testdisamsets(name,text) select b.name,b.text from zhwiki.pagemapline as a,page as b where a.name='" + disamWord + "' and a.id=b.id;";
            opMysql.insertSQL(conn, sql);

        }

        String selectsql = "SELECT * FROM zhwiki.testDisamSets;";
        ResultSet resultSet = opMysql.selectSQL(conn, selectsql);
        while (resultSet.next()) {
            String title = resultSet.getString("name");
            fw.write(title + "\n");

        }
        fw.close();
        opMysql.deconnSQL(conn);

    }


    /**
     * 随机选取redirect页面
     *
     * @param filePath
     * @param N
     * @return
     */
    private static ArrayList<String> getRandomRedirectTitles(String filePath, int N) {
        ArrayList<String> result = new ArrayList<String>();

        ArrayList<String> lines = Myutil.readByLine(filePath);
        int size = lines.size();
        ArrayList<Integer> testRandom = Randomfind(size, N);
        for (int i : testRandom) {
            String line = lines.get(i);
            String name = line.split("\t")[0];
            result.add(name);
        }
        return result;
    }


    public static void writeRedirectTitles(String filepath) throws SQLException, IOException {
        FileWriter fw = new FileWriter(filepath);
        ArrayList<String> disamWords = getRandomRedirectTitles(Contant.redirectOutput_Path, 55);
        System.out.println(disamWords.size());
        Connection conn = opMysql.connSQL(Contant.zhmysqlurl);
        for (String disamWord : disamWords) {
            String sql = "insert into zhwiki.testRedirectSets(name,text) select b.name,b.text from zhwiki.pagemapline as a,page as b where a.name='" + disamWord + "' and a.id=b.id;";
            opMysql.insertSQL(conn, sql);
//            fw.write(disamWord+"\n");
        }


        String selectsql = "SELECT * FROM zhwiki.testRedirectSets;";
        ResultSet resultSet = opMysql.selectSQL(conn, selectsql);
        while (resultSet.next()) {
            String title = resultSet.getString("name");
            fw.write(title + "\n");

        }
        conn.close();
        fw.close();

    }


    public static void insertWiKi_BaiKe_NewsToTables(String wkFirstColum, String baike, String baikeFirstColum, String News, String title) throws SQLException {
        String updatesql = "update zhwiki.testredirectSets set wkFirstColum = ?,Baike=?, BaikeFirstColum=?,News=? where name = ?";
        Connection conn = opMysql.connSQL(Contant.zhmysqlurl);
        opMysql.updateSQL(conn, updatesql, wkFirstColum, baike, baikeFirstColum, News, title);
    }


    public static void main(String[] args) throws IOException, SQLException {
//        String fileDisam = "Project_EntityLinking/output/test_DisambigationTitles_55.output";
//        writeDisamTitles(fileDisam);
//        writeRedirectTitles(fileDisam);

        String name = "表演必须继续";


        String wkFirstColum = "《表演必须继续》（英语：The Show Must Go On） 是英国摇滚乐队皇后乐队的歌曲，首次发表于其第十四张原创专辑Innuendo(1991年2月5日)中，为专辑最终曲，由乐团四人共同创作。\n" +
                "\n" +
                "歌名引用了古老的西方谚语。歌词内容表达一名表演者在面临死亡时却笑看生死，继续豪气表演直到最后的情况。[1]由于主唱Freddie Mercury此时确实面临着死亡，且英国媒体自1980年代末以来就一直对他的健康状况强烈好奇，故本曲常被视为他在向外界表达看待自己生命尽头的态度。";


        String baikeFirstColum = "NA";


        String baike = "NA";


        String News = "NA";


        insertWiKi_BaiKe_NewsToTables(wkFirstColum, baike, baikeFirstColum, News, name);


    }
}
