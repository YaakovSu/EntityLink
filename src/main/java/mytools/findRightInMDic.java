package mytools;

import edu.util.Myutil;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * Created by CBD_O9 on 2015-04-01.
 */
public class findRightInMDic {

    public static void main(String[] args) throws IOException {
        String MDicDirPath = "C:\\youngsu\\PracticedMyHands\\Project_EntityLinking\\output\\testTop500_RankSVM_noCoherence2\\test_pre\\feature";
        String markedDataDirPath = "C:\\Users\\CBD_O9\\Desktop\\苏三标数据\\noCoherence\\testSVMZhengQueNames";
        String resultDirPath = "C:\\Users\\CBD_O9\\Desktop\\苏三标数据\\noCoherence_notInMDic\\";

//        String resultDirPathNotInMDic = "C:\\Users\\CBD_O9\\Desktop\\苏三标数据\\noCoherence_notInMDic\\";
        TreeMap<String, String> filesInMDic = Myutil.readfileInDic(MDicDirPath);
        TreeMap<String, String> filesInMarkedDic = Myutil.readfileInDic(markedDataDirPath);
        Iterator<String> iteratorMark = filesInMarkedDic.keySet().iterator();
        while (iteratorMark.hasNext()) {
            String absolutName = iteratorMark.next();
            String semi_name = filesInMarkedDic.get(absolutName);
            String name = semi_name.split("_")[0];
            Iterator<String> iteratorMDic = filesInMDic.keySet().iterator();
//            boolean inMDic = false;
            while (iteratorMDic.hasNext()) {
                String abName = iteratorMDic.next();
                String s_name = filesInMDic.get(abName);
                String mName = s_name.split("_")[0];
                if (name.equals(mName)) {
//                    inMDic = true;
                    FileWriter fw = new FileWriter(resultDirPath + semi_name);

                    ArrayList<String> texts = Myutil.readByLine(absolutName);
                    for (String text : texts) {
                        fw.write(text + "\n");
                    }
                    fw.close();
                }
//                if(!inMDic){
//                    FileWriter fw_no = new FileWriter(resultDirPathNotInMDic+semi_name);
//                    ArrayList<String> texts = Myutil.readByLine(absolutName);
//                    for(String text : texts){
//                        fw_no.write(text+"\n");
//                    }
//                    fw_no.close();
//                    inMDic = false;
//                }
            }

        }

    }
}
