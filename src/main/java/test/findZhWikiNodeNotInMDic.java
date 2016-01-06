package test;

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
 * Created by CBD_O9 on 2015-04-25.
 */
public class findZhWikiNodeNotInMDic {
    public static HashSet<String> getMDicZhNames(String MDicPath) {
        HashSet<String> result = new HashSet<String>();
        ArrayList<String> text = Myutil.readByLine(MDicPath);
        for (String line : text) {
            String[] words = line.split("=");
            String zhName = words[1];
            result.add(zhName);
        }
        return result;
    }

    public static HashSet<String> getMDicEnNames(String MDicPath) {
        HashSet<String> result = new HashSet<String>();
        ArrayList<String> text = Myutil.readByLine(MDicPath);
        for (String line : text) {
            String[] words = line.split("=");
            String zhName = words[1];
            result.add(zhName);
        }
        return result;
    }

    public static void findNodeNotinZhWiki(HashSet<String> zhNameInMDic, String outputPath) throws SQLException, IOException {
        FileWriter fw = new FileWriter(outputPath);
        Connection conn = opMysql.connSQL(Contant.zhmysqlurl);
        String selectSql = "SELECT a.name FROM zhwiki.page as a,zhwiki.pagemapline as b where a.id = b.id;";
        ResultSet resultSet = opMysql.selectSQL(conn, selectSql);
        while (resultSet.next()) {
            String name = resultSet.getString("name");
            if (!zhNameInMDic.contains(name)) {
                fw.write(name + "\n");
                fw.flush();
            }
        }
        fw.close();

    }

    public static void main(String[] args) throws IOException, SQLException {
        String outputPath = "C:\\youngsu\\Entity_LinkingData\\zhDataNotInMDic.tsv";
        HashSet<String> zhNameInMDIc = getMDicZhNames(Contant.MDicInputPath);
        findNodeNotinZhWiki(zhNameInMDIc, outputPath);
    }
}
