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
import java.util.HashSet;

/**
 * Created by CBD_O9 on 2015-04-14.
 */
public class findZhWordsNotInMDIC {
    public static HashSet<String> getAllMDicData(String MDicPath) {
        ArrayList<String> semiResult = Myutil.readByLine(MDicPath);
        HashSet<String> result = new HashSet<String>();
        for (String line : semiResult) {
            String[] names = line.split("=");
            String zhName = names[1];
            if (!result.contains(zhName)) {
                result.add(zhName);
            }
        }
        return result;
    }

    public static void WriteWordsNotInMDic(HashSet<String> wordsInMDic) throws SQLException, IOException {
        Connection conn = opMysql.connSQL(Contant.zhmysqlurl);
        FileWriter fw = new FileWriter("C:\\youngsu\\Entity_LinkingData\\wordsNotInMDic.tsv");

        String selectSql = "SELECT name FROM zhwiki.page;";
        ResultSet resultSet = opMysql.selectSQL(conn, selectSql);
        while (resultSet.next()) {
            String name = resultSet.getString(1);
            if (!wordsInMDic.contains(name)) {

                fw.write(name + "\n");
                fw.flush();
            }
        }
        fw.close();
    }

    public static void main(String[] args) throws IOException, SQLException {
        String MDicPath = "C:\\youngsu\\Entity_LinkingData\\en_zh_wiki_page.output";
        WriteWordsNotInMDic(getAllMDicData(MDicPath));
    }
}
