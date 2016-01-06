package mytools;

import edu.util.Myutil;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by CBD_O9 on 2014-11-12.
 */
public class cutTextTo2000 {
    public static void main(String[] args) throws IOException {
        String filepath = "C:\\youngsu\\wikidata\\baikedump\\baikedump-geo\\data.dic";
        String outputpath = "C:\\youngsu\\wikidata\\baikedump\\baikedump-geo\\data";
        FileWriter fw = new FileWriter(outputpath);
        ArrayList<String> lists = Myutil.readByLine(filepath);

        for (int i = 0; i < 2100; i++) {
            String line = lists.get(i);
            fw.write(line + "\n");

        }
        fw.close();
    }
}
