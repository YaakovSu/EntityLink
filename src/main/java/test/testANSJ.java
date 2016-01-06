package test;


import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by youngsu on 14-10-28.
 */
public class testANSJ {
    public static void testGuanjianCi() {
        KeyWordComputer kwc = new KeyWordComputer(10);
        String title = "苹果";
        String content = "剧情概述\n" +
                "刘苹果（范冰冰）与丈夫安坤（佟大为）是进城打工的民工，感情要好，生活愉快。刘苹果为脚底按摩服务员，某天因被脚底按摩店老板林东（梁家辉）强奸而怀孕。刘苹果的丈夫安坤不服，因而向林东勒索。\n" +
                "\n" +
                "禁映\n" +
                "2007年2月，在柏林电影节举行首映，并未放映经中国电影局审查通过的删减版，放映了完整版，其中包括范冰冰和佟大为的全裸镜头及赌博场景。制片人方励在接受采访时解释，由于时间紧迫，德语和英语字幕的删节版拷贝未能制作出来导致的。11月30日，《苹果》在中国内地上映，内容引起争议。该片在香港被定为三级片，在中国大陆上映时剪除了露点部份。 [1]\n" +
                "\n" +
                "2008年1月4日，因为该电影被广电总局认为涉嫌传播色情内容，因此在中国全面禁映。吊销影片的《电影片公映许可证》。没收未经审查通过的影片拷贝及相关素材，制片单位15天内将拷贝等送达总局电影局；停止该片在影院发行、放映；停止其网络传播；建议有关行政部门停止其音像制品的发行。取消北京劳雷影视文化有限责任公司两年内摄制电影的资格；该公司的法定代表人方励，两年内禁止从事相关电影业务；将负有相关责任、参与投资拍摄的两家公司，通报批评，责令其限期整改。对参与该片拍摄的制片人、导演及相关演员，进行严肃批评教育，并要求他们作出深刻检查。\n" +
                "\n";
        Collection<Keyword> result = kwc.computeArticleTfidf(content);
        Iterator<Keyword> iterator = result.iterator();
//        while (iterator.hasNext()){
//            Keyword word = iterator.next();
//            String name = word.getName();
//            double score = word.getScore();
//            System.out.println(name);
//        }
        System.out.println(result);
    }

    public static void testFenci() {
        String content = "剧情概述\n" +
                "刘苹果（范冰冰）与丈夫安坤（佟大为）是进城打工的民工，感情要好，生活愉快。刘苹果为脚底按摩服务员，某天因被脚底按摩店老板林东（梁家辉）强奸而怀孕。刘苹果的丈夫安坤不服，因而向林东勒索。\n" +
                "\n" +
                "禁映\n" +
                "2007年2月，在柏林电影节举行首映，并未放映经中国电影局审查通过的删减版，放映了完整版，其中包括范冰冰和佟大为的全裸镜头及赌博场景。制片人方励在接受采访时解释，由于时间紧迫，德语和英语字幕的删节版拷贝未能制作出来导致的。11月30日，《苹果》在中国内地上映，内容引起争议。该片在香港被定为三级片，在中国大陆上映时剪除了露点部份。 [1]\n" +
                "\n" +
                "2008年1月4日，因为该电影被广电总局认为涉嫌传播色情内容，因此在中国全面禁映。吊销影片的《电影片公映许可证》。没收未经审查通过的影片拷贝及相关素材，制片单位15天内将拷贝等送达总局电影局；停止该片在影院发行、放映；停止其网络传播；建议有关行政部门停止其音像制品的发行。取消北京劳雷影视文化有限责任公司两年内摄制电影的资格；该公司的法定代表人方励，两年内禁止从事相关电影业务；将负有相关责任、参与投资拍摄的两家公司，通报批评，责令其限期整改。对参与该片拍摄的制片人、导演及相关演员，进行严肃批评教育，并要求他们作出深刻检查。\n" +
                "\n";

        List<Term> parse = NlpAnalysis.parse(content);
        System.out.println(parse);
    }

    public static void main(String[] args) {
        testGuanjianCi();
    }
}
