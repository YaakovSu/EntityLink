package edu.nlp;

import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import edu.baike.PageKeyWord;
import edu.util.Contant;
import edu.util.Myutil;
import edu.util.opMysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static edu.util.Myutil.sim_BOW;

/**
 * Created by youngsu on 14-10-29.
 */
public class Rank {
    //分析titles对应的mysql中的词条文本内容与要识别的文本之间的关系并排序
    public static ArrayList<String> rankTitles(ArrayList<String> titles, String fileContent) throws SQLException {
        ArrayList<String> jieguo = new ArrayList<String>();
        HashMap<String, Double> result = new HashMap<String, Double>();


        ArrayList<PageKeyWord> pageKeyWords = getPageKeyWords(titles); //PageKeyWord有title和keywords
        ArrayList<Keyword> contentKeyWords = findKeyWords_ANSJ(fileContent);
        HashMap<String, Double> AllKeyWordsTitles = getAllKeyWordsTitles(pageKeyWords, contentKeyWords);
        result = sim_BOW(pageKeyWords, contentKeyWords, AllKeyWordsTitles);

        jieguo = sortHashMap(result);


        return jieguo;
    }

    private static ArrayList<String> sortHashMap(HashMap<String, Double> result) {

        ArrayList<String> jieguo = new ArrayList<String>();
        //对hashmap进行排序
        List<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>();
        list.addAll(result.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            public int compare(Map.Entry<String, Double> obj1, Map.Entry<String, Double> obj2) {//从高往低排序
                if (obj1.getValue() < obj2.getValue())
                    return 1;
                if (obj1.getValue() == obj2.getValue())
                    return 0;
                else
                    return -1;
            }
        });

        for (Iterator<Map.Entry<String, Double>> ite = list.iterator(); ite.hasNext(); ) {
            Map.Entry<String, Double> map = ite.next();
            jieguo.add(map.getKey() + "\t" + map.getValue());
        }
        return jieguo;
    }

    private static HashMap<String, Double> getAllKeyWordsTitles(ArrayList<PageKeyWord> pageKeyWords, ArrayList<Keyword> contentKeyWords) {
        HashMap<String, Double> allKeyWordsTitles = new HashMap<String, Double>();
        for (PageKeyWord pageKeyWord : pageKeyWords) {
            ArrayList<Keyword> keywords = pageKeyWord.getKeywords();
            for (Keyword keyword : keywords) {
                String word = keyword.getName();
                if (!allKeyWordsTitles.containsKey(word)) {
                    allKeyWordsTitles.put(word, Contant.initVector);
                }
            }
        }
        for (Keyword keyword : contentKeyWords) {
            String word = keyword.getName();
            if (!allKeyWordsTitles.containsKey(word)) {
                allKeyWordsTitles.put(word, Contant.initVector);
            }
        }
        return allKeyWordsTitles;
    }

    private static ArrayList<PageKeyWord> getPageKeyWords(ArrayList<String> titles) throws SQLException {
        ArrayList<PageKeyWord> pageKeyWords = new ArrayList<PageKeyWord>();

        Connection conn = opMysql.connSQL(Contant.zhmysqlurl);
        for (String title : titles) {
            String text = getMysqlText(conn, title);
            if (!text.equals("")) {
                text = text.replaceAll("\\d+", "");
                String pattern = "([-+*/^()\\]\\[])";
                text = text.replaceAll(pattern, "");
                text = text.replace(".", "");
                text = Myutil.getCleanText(text);

                ArrayList<Keyword> textKeyWords = findKeyWords_ANSJ(text);
                PageKeyWord pageKeyWord = new PageKeyWord(title, textKeyWords);
                pageKeyWords.add(pageKeyWord);

            }

        }
        opMysql.deconnSQL(conn);
        return pageKeyWords;
    }


    private static String getMysqlText(Connection conn, String title) throws SQLException {

        String selectSql = "SELECT b.text FROM zhwiki.pagemapline as a,zhwiki.page as b where a.name='" + title + "' and a.id=b.id;";
        String text = "";
        System.out.println(selectSql);
        if (!title.contains("\'")) {

            ResultSet resultSet = opMysql.selectSQL(conn, selectSql);
            while (resultSet.next()) {
                text = resultSet.getString("text");
            }

        }


        return text;
    }


    private static ArrayList<Keyword> findKeyWords_ANSJ(String fileContent) {
        ArrayList<Keyword> result = new ArrayList<Keyword>();
        KeyWordComputer kwc = new KeyWordComputer(Contant.SetingKeyWords);
        Collection<Keyword> semi_result = kwc.computeArticleTfidf(fileContent);
        Iterator<Keyword> iterator = semi_result.iterator();
        while (iterator.hasNext()) {
            Keyword keyword = iterator.next();
            result.add(keyword);
        }

        return result;
    }
}
