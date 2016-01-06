package test;

import edu.util.Myutil;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by CBD_O9 on 2014-11-24.
 */
public class FindMethod2SVMDoubt {

    static String featureInput = "Project_EntityLinking/output/QC_WE/1_feature.output";
    static String outputPath = "Project_EntityLinking/output/QC_WE/score.out";

    public static void main(String[] args) throws IOException {
        FileWriter fw = new FileWriter(outputPath);
        ArrayList<String> features = Myutil.readByLine(featureInput);
        ArrayList<Double> scores = new ArrayList<Double>();
        for (String feature : features) {
            double score = ComputeFeatureScore(feature);
            scores.add(score);
            String sc = String.valueOf(score);
            fw.write(sc + "\n");
            fw.flush();
        }
        fw.close();


//        ArrayList<Double> sortScore = sort(scores);
    }

    private static Double ComputeFeatureScore(String feature) {

        String[] line = feature.split(" ");
        String inlink = line[1].split(":")[1];
        String outlink = line[2].split(":")[1];
        String category = line[3].split(":")[1];

        double score = computeScore(inlink, outlink, category);


        return score;
    }

    private static double computeScore(String inlink, String outlink, String category) {
        double in = 0.426;
        double out = 0.361;
        double ca = 0.213;  //系数

        double inlink_double = Double.parseDouble(inlink);
        double outlink_double = Double.parseDouble(outlink);
        double category_double = Double.parseDouble(category);


        double score = in * inlink_double + out * outlink_double + ca * category_double;

        return score;
    }
}
