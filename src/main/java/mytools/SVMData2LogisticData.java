package mytools;

import edu.util.Myutil;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by CBD_O9 on 2014-11-30.
 */
public class SVMData2LogisticData {

    public static void main(String[] args) throws IOException {

        String input1 = "C:\\youngsu\\logistic回归\\trainningData10000RankSVM_1216_6feature_nameScore.output";
        String input2 = "C:\\youngsu\\logistic回归\\trainoutput";

        String outPutPath = "C:\\youngsu\\logistic回归\\trainningData.output";
        ArrayList<String> contentScore = Myutil.readByLine(input1);
        ArrayList<String> svmScore = Myutil.readByLine(input2);

        FileWriter fw = new FileWriter(outPutPath);

        for (int i = 0; i < contentScore.size(); i++) {
            String line = contentScore.get(i);
            String[] words = line.split(" ");
            String tag = words[0];
            String contentRate = words[4];
            String svmRate = svmScore.get(i);
            fw.write(svmRate + " " + contentRate + " " + tag + "\n");
        }
        fw.flush();
        fw.close();


    }

    public static void svmData2LogisticData() throws IOException {
        String inputPath = "C:\\youngsu\\libsvm-3.20\\windows\\trainningData1127_2.output";
        String outputPath = "C:\\youngsu\\libsvm-3.20\\windows\\logisticTrainning.output";
        FileWriter fw = new FileWriter(outputPath);

        ArrayList<String> lines = Myutil.readByLine(inputPath);
        System.out.println("cao " + lines.size());
        for (String line : lines) {
            System.out.println("gaga " + line);
            String[] words = line.split(" ");
            String y = words[0];
            System.out.println("pp " + words[0]);
            String x1 = words[1].split(":")[1];
            String x2 = words[2].split(":")[1];
            String x3 = words[3].split(":")[1];

            fw.write(x1 + " " + x2 + " " + x3 + " " + y + "\n");
            fw.flush();
        }
        fw.close();

    }
}
