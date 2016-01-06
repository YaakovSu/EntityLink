package mytools;

import edu.nlp.DisamDic;

import java.util.Iterator;

/**
 * Created by youngsu on 16-1-5.
 */
public class testDisamDic {
    public static void main(String[] args) {
        String filePath = "/home/youngsu/IdeaProjects/paperdata/entitylinkdata/Clean_disambigation.output";
        DisamDic dic = new DisamDic(filePath);
        System.out.println(dic.getDisamPage("金堡"));
    }
}
