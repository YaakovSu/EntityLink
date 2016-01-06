package edu.nlp;

import edu.Constant;
import edu.baike.Baike;
import edu.baike.EnBaike;
import edu.baike.KCLpair;
import edu.baike.ZhBaike;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.util.Myutil;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by youngsu on 15-12-29.
 * 中英文关键百科间的操作
 */
public class FeatureParser {
    private ZhBaike zhBaike = new ZhBaike();
    private EnBaike enBaike = new EnBaike();
    private ECDic dic = new ECDic();

    private double in_rate, out_rate, cat_rate;
    private double in_qujian, out_qujian, cat_qujian;
    private double in_co, out_co, cat_co;
    private double desScore, textScore, desOutlinkScore;

    private double in_common, out_common, cat_common;


    public FeatureParser(ZhBaike zhBaike, EnBaike enBaike, ECDic dic) throws IOException, ClassNotFoundException {
        this.zhBaike = zhBaike;
        this.enBaike = enBaike;
        this.dic = dic;
        init();
    }

    public double getIn_rate() {
        return in_rate;
    }

    public double getOut_rate() {
        return out_rate;
    }

    public double getCat_rate() {
        return cat_rate;
    }

    public double getIn_qujian() {
        return in_qujian;
    }

    public double getOut_qujian() {
        return out_qujian;
    }

    public double getCat_qujian() {
        return cat_qujian;
    }

    public double getIn_co() {
        return in_co;
    }

    public double getOut_co() {
        return out_co;
    }

    public double getCat_co() {
        return cat_co;
    }

    public double getDesScore() {
        return desScore;
    }

    public double getTextScore() {
        return textScore;
    }

    public double getDesOutlinkScore() {
        return desOutlinkScore;
    }

    private void init() throws IOException, ClassNotFoundException {
        getCommonLinks(zhBaike, enBaike);
        getRate(zhBaike, enBaike);
        getQujianRate(zhBaike, enBaike);
        getCoherence(zhBaike, enBaike);
        desScore = getTextDesSimilarScore(zhBaike.getText(), enBaike.getText(), dic);
        textScore = getTextSimilarScore(zhBaike.getText(), enBaike.getText(), dic);
        desOutlinkScore = getTextDesOutlinkSimilarScore(zhBaike.getText(), enBaike.getText(), dic);
    }

    private void getCoherence(ZhBaike zhBaike, EnBaike enBaike) {
        in_co = (double) in_common / (zhBaike.getInlinks().size() + enBaike.getInlinks().size() - in_common);
        out_co = (double) out_common / (zhBaike.getOutlinks().size() + enBaike.getOutlinks().size() - out_common);
        cat_co = (double) cat_common / (zhBaike.getCategories().size() + enBaike.getCategories().size() - cat_common);
    }

    private void getQujianRate(ZhBaike zhBaike, EnBaike enBaike) {
        in_qujian = getQujian(in_common, 'i');
        out_qujian = getQujian(out_common, 'o');
        cat_qujian = getQujian(cat_common, 'c');
    }

    private void getRate(ZhBaike zhBaike, EnBaike enBaike) {
        double all_in = zhBaike.getInlinks().size() + enBaike.getInlinks().size();
        double all_out = zhBaike.getOutlinks().size() + enBaike.getOutlinks().size();
        double all_cat = zhBaike.getCategories().size() + enBaike.getCategories().size();
        if (all_in == 0) {
            all_in = 0.0001;
        }
        if (all_out == 0) {
            all_out = 0.0001;
        }
        if (all_cat == 0) {
            all_cat = 0.0001;
        }
        in_rate = (double) 2 * in_common / all_in;
        out_rate = (double) 2 * out_common / all_out;
        cat_rate = (double) 2 * cat_common / all_cat;
    }

    private void getCommonLinks(ZhBaike zhBaike, EnBaike enBaike) {
        int in_num = 0, out_num = 0, cat_num = 0;
        List<KCLpair> zhs = zhBaike.getKCLin();
        List<KCLpair> ens = enBaike.getKCLin();
        for (KCLpair zh : zhs) {
            for (KCLpair en : ens) {
                if (zh.getEnname().equals(en.getEnname())) {
                    in_num++;
                }
            }
        }
        zhs = zhBaike.getKCLout();
        ens = enBaike.getKCLout();
        for (KCLpair zh : zhs) {
            for (KCLpair en : ens) {
                if (zh.getEnname().equals(en.getEnname())) {
                    out_num++;
                }
            }
        }
        zhs = zhBaike.getKCLcat();
        ens = enBaike.getKCLcat();
        for (KCLpair zh : zhs) {
            for (KCLpair en : ens) {
                if (zh.getEnname().equals(en.getEnname())) {
                    cat_num++;
                }
            }
        }
        in_common = in_num;
        out_common = out_num;
        cat_common = cat_num;

    }

    public ZhBaike getZhBaike() {
        return zhBaike;
    }

    public EnBaike getEnBaike() {
        return enBaike;
    }
    /**
     * 计算语义特征
     *
     * 输入改成zhBaike和enBaike两个实体
     */


    /**
     * 计算两个简介的outlink重复度
     */
    private double getTextDesOutlinkSimilarScore(String text1, String text2, ECDic E_CDic) throws IOException, ClassNotFoundException {

        ArrayList<String> text1MingCi = new ArrayList<String>();
        ArrayList<String> text2MingCi = new ArrayList<String>();

        String[] text1Lines = text1.split("\n");
        String[] text2Lines = text2.split("\n");
        /**
         * 只使用简介部分来进行处理
         */

        for (String line : text1Lines) {

            if (line.contains("'''") && (!line.startsWith("{")) && (!line.startsWith(" [")) && (!line.startsWith(" |")) && (!line.startsWith("[")) && (!line.startsWith(" {"))) {
                text1 = line;
                break;
            }
        }
        for (String line : text2Lines) {
            if (line.contains("'''") && (!line.startsWith("{")) && (!line.startsWith(" [")) && (!line.startsWith(" |")) && (!line.startsWith("[")) && (!line.startsWith(" {"))) {
                text2 = line;
                break;
            }
        }

        String[] semiOutlinks = text1.split("\\[\\[");
        if (semiOutlinks.length > 1) {
            for (String semiOutlink : semiOutlinks) {
                String outlinks = semiOutlink.split("\\]\\]")[0];
                text1MingCi.add(outlinks);
            }
        }


        String[] semiEnOutlinks = text2.split("\\[\\[");
        if (semiEnOutlinks.length > 1) {
            for (String semiEnOutlink : semiEnOutlinks) {
                String outlinks = semiEnOutlink.split("\\]\\]")[0];
                text2MingCi.add(outlinks);
            }
        }

        System.out.println(text1MingCi);
        System.out.println(text2MingCi);

        double result = getScore(text1MingCi, text2MingCi, E_CDic);
        return result;

    }


    /**
     * 计算连个全文本的名词相似度
     *
     */
    private double getTextSimilarScore(String text1, String text2, ECDic E_CDic) throws IOException, ClassNotFoundException {

        ArrayList<String> text1MingCi = new ArrayList<String>();
        ArrayList<String> text2MingCi = new ArrayList<String>();

        text1 = cleantext(text1);
        List<Term> results = NlpAnalysis.parse(text1);
        for (Term result : results) {
            if (result.getNatureStr().equals("n")) {  // W要改，改成名称的标识符
//                System.out.print(result.getName() + " ");
                text1MingCi.add(result.getName());
            }

        }
//        System.out.println();
        String model = Constant.CRFMODEL;

        MaxentTagger tagger = new MaxentTagger(model);
        String tagged = tagger.tagString(text2);
        String[] taggedWords = tagged.split(" ");
        for (String taggedWord : taggedWords) {
            if (taggedWord.endsWith("/NN")) {
                if (taggedWord.length() < 20) {
                    String nun = taggedWord.split("/NN")[0];
                    text2MingCi.add(nun);
                }

            }
        }
        double result = getScore(text1MingCi, text2MingCi, E_CDic);
        return result;

    }


    /**
     * 计算两个简介的名词相似度
     *
     */
    private double getTextDesSimilarScore(String text1, String text2, ECDic E_CDic) throws IOException, ClassNotFoundException {

        ArrayList<String> text1MingCi = new ArrayList<String>();
        ArrayList<String> text2MingCi = new ArrayList<String>();

        String[] text1Lines = text1.split("\n");
        String[] text2Lines = text2.split("\n");

        /**
         * 只使用简介部分来进行处理
         */

        for (String line : text1Lines) {

            if (line.contains("'''") && (!line.startsWith("{")) && (!line.startsWith(" [")) && (!line.startsWith(" |")) && (!line.startsWith("[")) && (!line.startsWith(" {"))) {
                text1 = line;
                break;
            }
        }
        for (String line : text2Lines) {
            if (line.contains("'''") && (!line.startsWith("{")) && (!line.startsWith(" [")) && (!line.startsWith(" |")) && (!line.startsWith("[")) && (!line.startsWith(" {"))) {
                text2 = line;
                break;
            }
        }
        text1 = cleantext(text1);
        List<Term> results = NlpAnalysis.parse(text1);
        for (Term result : results) {
            if (result.getNatureStr().equals("n")) {  // W要改，改成名称的标识符
//                System.out.print(result.getName() + " ");
                text1MingCi.add(result.getName());

            }

        }
        String model = Constant.CRFMODEL;

        MaxentTagger tagger = new MaxentTagger(model);
        String tagged = tagger.tagString(text2);
        String[] taggedWords = tagged.split(" ");
        for (String taggedWord : taggedWords) {
            if (taggedWord.endsWith("/NN")) {
                if (taggedWord.length() < 20) {
                    String nun = taggedWord.split("/NN")[0];
                    text2MingCi.add(nun);
                }

            }
        }
//        System.out.println();
        double result = getScore(text1MingCi, text2MingCi, E_CDic);
        return result;

    }

    /**
     * 这个getscore方法是clean版的，将相同的名词去掉
     */

    private double getScore(ArrayList<String> zhlines, ArrayList<String> enlines, ECDic E_CDic) {

        int count = 0;
        HashSet<String> clean_zhlines = new HashSet<String>();
        HashSet<String> clean_enlines = new HashSet<String>();
        for (String zhline : zhlines) {
            if (!clean_zhlines.contains(zhline)) {
                clean_zhlines.add(zhline);
            }
        }

        for (String enline : enlines) {
            if (!clean_enlines.contains(enline)) {
                clean_enlines.add(enline);
            }
        }


        HashSet<String> enTopicNames = new HashSet<String>();
        for (String enline : clean_enlines) {

            enTopicNames.add(enline);
        }


        for (String zhline : clean_zhlines) {

            String zhname = zhline;

            if (!E_CDic.containsKey(zhname)) {
//                System.out.println(zhname + "\t" + "NA");
            } else {

                HashSet<String> candidateEnName = E_CDic.get(zhname);

//                System.out.println("存在:" + zhname);
                boolean hasInEnTopicNames = false;
                for (String candidateName : candidateEnName) {
                    if (enTopicNames.contains(candidateName)) {
                        hasInEnTopicNames = true;
                    }
                }

                if (hasInEnTopicNames) {
                    count++;
                }
            }
        }

        int zhlineSize = clean_zhlines.size();
        int enlinesSize = clean_enlines.size();
        double allLines = zhlineSize + enlinesSize;
        double result = 0;
        if (allLines > 0) {
            result = 2 * count / (double) (zhlineSize + enlinesSize);
        }


        return result;

    }

    private static String cleantext(String fileContent) {

        fileContent = fileContent.replaceAll("\\d+", "");
        String pattern = "([-+*/^()\\]\\[])";
        fileContent = fileContent.replaceAll(pattern, "");
        fileContent = fileContent.replace(".", "");

        fileContent = Myutil.getCleanText(fileContent);
        return fileContent;
    }

    private double getQujian(double countNum, char i) {
        if (i == 'i') {

            if (countNum >= 0 && countNum < 10) {
                return 0.1;
            } else if (countNum >= 10 && countNum < 20) {
                return 0.2;
            } else if (countNum >= 20 && countNum < 30) {
                return 0.3;
            } else if (countNum >= 30 && countNum < 40) {
                return 0.4;
            } else if (countNum >= 40 && countNum < 50) {
                return 0.5;
            } else if (countNum >= 50 && countNum < 60) {
                return 0.6;
            } else if (countNum >= 60 && countNum < 70) {
                return 0.7;
            } else if (countNum >= 70 && countNum < 80) {
                return 0.8;
            } else if (countNum >= 80 && countNum < 90) {
                return 0.9;
            } else if (countNum >= 90) {
                return 1;
            }
        }

        if (i == 'o') {

            if (countNum >= 0 && countNum < 11) {
                return 0.1;
            } else if (countNum >= 11 && countNum < 22) {
                return 0.2;
            } else if (countNum >= 22 && countNum < 33) {
                return 0.3;
            } else if (countNum >= 33 && countNum < 44) {
                return 0.4;
            } else if (countNum >= 44 && countNum < 55) {
                return 0.5;
            } else if (countNum >= 55 && countNum < 66) {
                return 0.6;
            } else if (countNum >= 66 && countNum < 77) {
                return 0.7;
            } else if (countNum >= 77 && countNum < 88) {
                return 0.8;
            } else if (countNum >= 88 && countNum < 99) {
                return 0.9;
            } else if (countNum >= 99) {
                return 1;
            }
        }
        if (i == 'c') {

            if (countNum >= 0 && countNum < 10) {
                return countNum / 10;
            } else {
                return 1;
            }
        }

        return 0;
    }
}
