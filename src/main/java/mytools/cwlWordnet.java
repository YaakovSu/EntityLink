package mytools;

import edu.util.Contant;
import edu.util.opMysql;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by CBD_O9 on 2015-03-17.
 */
public class cwlWordnet {
    public static void main(String[] args) throws IOException, SQLException {
        String relationName = "domain member usage";
        String selectSql = "SELECT * FROM enwordnet30.lexlinks as a,enwordnet30.linktypes as b where a.linkid=b.linkid and b.link='" + relationName + "';";
        String outputPath = "C:\\Users\\CBD_O9\\Desktop\\wordNetLex\\";
        FileWriter fw = new FileWriter(outputPath + relationName + ".txt");

        Connection conn = opMysql.connSQL(Contant.cwlmysqlurl);

        ResultSet resultSet = opMysql.selectSQL(conn, selectSql);

        while (resultSet.next()) {
            String id1 = resultSet.getString("word1id");
            String id2 = resultSet.getString("word2id");
            fw.write(id1 + "\t" + id2 + "\n");
        }
        fw.close();

    }
}
