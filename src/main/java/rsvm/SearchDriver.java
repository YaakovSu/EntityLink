package rsvm;

import edu.Constant;
import edu.Language;
import edu.baike.Yago;
import edu.nlp.ECDic;
import edu.util.Contant;
import edu.util.Myutil;
import edu.util.opMysql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by CBD_O9 on 2015-08-29.
 */
public class SearchDriver {
    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {

//        String name = "蒋介石";
//        String filecontent = "蒋中正受孙中山赏识而崛起於民国政坛，在孙去世后长期领导中国国民党达半世纪；其於国民政府时代一直居於军政核心，领导中国渡过对日抗战与二次大战，行宪后又连续担任第一至五任中华民国总统长达27年，但其政治手腕与独裁统治亦遭受批评。其从政生涯横跨北伐、训政、国共内战、对日抗战、行宪、民国政府退守台湾及东西方冷战，在中国近代史上有重要地位。";

        String name = "人民大学";
        String filecontent = "北京";

        SearchDriver search = new SearchDriver();
        //查询对应的英文词
//        String enName = search.searchEnWords(name, filecontent);
        TreeMap<String,String> enNames = search.searchEnWords(name, filecontent);
        ArrayList<Yago> yagos = new ArrayList<Yago>();
        //寻找对应的YAGO子图
        Iterator<String> iterator = enNames.keySet().iterator();
        while (iterator.hasNext()){
            String zhName = iterator.next();
            String enName = enNames.get(zhName);
            System.out.println(enName);
            Yago yago = search.getYagoGraph(enName);
            yagos.add(yago);
        }
        Myutil.deleteFile(Constant.QCWE_PREDICT);
        Myutil.deleteFile(Constant.QCWE_FEATURE);
        Myutil.deleteFile(Constant.QCWE_NAME);




    }

    public Yago getYagoGraph(String enName) throws SQLException {
        Connection conn = opMysql.connSQL(Contant.enmysqlurl);
        Yago yago = new Yago();
        yago.setName(enName);

        we2yago we2YagoFact = new we2yago(opMysql.connSQL(Contant.yagomysqlurl));
        ArrayList<String> testResult = we2YagoFact.getAllAssociateNameRelation(enName, Contant.yagofacts);
        ArrayList<String> testType = we2YagoFact.getAllNameBandRelations(enName, Contant.yagotypes);
        ArrayList<String> testWikipediainfo = we2YagoFact.getAllAssociateNameRelation(enName, Contant.yagowikipediainfo);

        yago.setText(findText(conn,enName));
        conn.close();
        yago.setResult(testResult);
        yago.setType(testType);
        yago.setWikiInfo(testWikipediainfo);
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

        return yago;
    }

    private String findText(Connection conn,String title) throws SQLException {
        String text = "";
        text = doTextEn(conn,title);

        return text;
    }

    private String doTextEn(Connection conn,String title) throws SQLException {
        String texten = "";
        if (!title.contains("'")) {
            String sqlen = "SELECT * FROM enwiki.pagemapline as a,enwiki.page as b where a.name='" + title + "' and a.pageId=b.id;";

            ResultSet resultSeten = opMysql.selectSQL(conn, sqlen);
            while (resultSeten.next()) {
                texten = resultSeten.getString("text");
            }
        }

        return texten;
    }

    public TreeMap<String,String> searchEnWords(String wordName, String Context) throws SQLException, IOException, ClassNotFoundException {
        qc2wc cw = new qc2wc();
        ECDic ecDic = new ECDic(Contant.E_CdicPath);

        //查询对应英文词
        TreeMap<String,String> zh_enNames = cw.writeOneNameSVMtrainningData(wordName, Context, ecDic);

//        System.out.println(enName);
        return zh_enNames;


    }

}
