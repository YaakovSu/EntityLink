package edu.util;

/**
 * Created by youngsu on 14-10-28.
 */
public class Patterns {
    //从文本中抽取歧义页面的patterns
//    public static final String pattern1_1 = "(\\*\\s\\[{2})(.*?)(\\]{2})"; //* [[]]
//    public static final String pattern1_2 = "(\\*\\[{2})(.*?)(\\]{2})"; //*[[]]
//    public static final String pattern2_1 = "(\\*{2}\\[{2})(.*?)(\\]{2})"; //**[[]]
//    public static final String pattern2_2 = "(\\*{2}\\s\\[{2})(.*?)(\\]{2})"; //**[[]]
//    public static final String pattern3_1 = "(\\*'''\\[{2})(.*?)(\\]{2}''')";//*'''[[]]'''
//    public static final String pattern4_1 = "('''\\[{2})(.*?)(\\]{2}''')";//'''[[]]'''
//    public static final String pattern4_2 = "('''\\s\\[{2})(.*?)(\\]{2}''')";//'''[[]]'''
//    public static final String pattern5_1 = "(\\#\\[{2})(.*?)(\\]{2})";//#[[]]
//    public static final String pattern5_2 = "(\\#\\s\\[{2})(.*?)(\\]{2})";//#[[]]
//
//    public static final String pattern6_1 = "(见\\[{2})(.*?)(\\]{2})"; //见[[]]
//    public static final String pattern6_2 = "(見\\[{2})(.*?)(\\]{2})"; //見[[]]
//    public static final String pattern7_1 = "(\\*[u4E00-u9FA5]\\[{2})(.*?)(\\]{2})"; //*汉字[[]]
//    public static final String pattern7_2 = "(：\\[{2})(.*?)(\\]{2})"; //*汉字：[[]]
//
//    public static final String pattern8_1 = "(又称\\[{2})(.*?)(\\]{2})"; //又称[[]]
//    public static final String pattern8_2 = "(又稱\\[{2})(.*?)(\\]{2})"; //又稱[[]]

    //只取中文文本
    public static final String pattern_Zh = "([\u0000-\u007F])"; //匹配中文


}
