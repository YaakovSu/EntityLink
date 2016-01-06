import edu.nlp.ECDic;
import edu.util.Contant;
import edu.util.opMysql;
import rsvm.qc2wc;
import rsvm.we2yago;

import java.io.IOException;
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
        String filecontent = "北京 学校";

        SearchDriver search = new SearchDriver();
        //查询对应的英文词
        String enName = search.searchEnWords(name, filecontent);
        //寻找对应的YAGO子图
        search.getYagoGraph(enName);


    }

    public void getYagoGraph(String enName) throws SQLException {
        we2yago we2YagoFact = new we2yago(opMysql.connSQL(Contant.yagomysqlurl));
        ArrayList<String> testResult = we2YagoFact.getAllAssociateNameRelation(enName, Contant.yagofacts);
        ArrayList<String> testType = we2YagoFact.getAllNameBandRelations(enName, Contant.yagotypes);
        ArrayList<String> testWikipediainfo = we2YagoFact.getAllAssociateNameRelation(enName, Contant.yagowikipediainfo);

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

    }

    public String searchEnWords(String wordName, String Context) throws SQLException, IOException, ClassNotFoundException {
        qc2wc cw = new qc2wc();
        ECDic ecDic = new ECDic(Contant.E_CdicPath);

        //查询对应英文词
        String enName = cw.writeOneNameSVMtrainningData(wordName, Context, ecDic);
        System.out.println(enName);
        return enName;


    }

}
