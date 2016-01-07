package edu.util;

/**
 * Created by youngsu on 14-10-14.
 */
public class Contant {

    //opMysql中的常量
    public static final String zhmysqlurl = "jdbc:mysql://localhost:3306/zhwiki?characterEncoding=UTF-8";
    public static final String enmysqlurl = "jdbc:mysql://localhost:3306/enwiki?characterEncoding=UTF-8";
    public static final String testmysqlurl = "jdbc:mysql://localhost:3306/testwiki?characterEncoding=UTF-8";

    public static final String yagomysqlurl = "jdbc:mysql://localhost:3306/yago?characterEncoding=UTF-8";


    public static final String mysqluser = "root";
    public static final String mysqlpwd = "root";
    public static final String mysqlclassName = "com.mysql.jdbc.Driver";
    public static final String selectWikiRedirect = "SELECT a.id,a.redirects,b.name FROM page_redirects as a,pagemapline as b where a.id=b.id;";

    //    public static final  String selectWikiDisAmbiguation = "SELECT * FROM enwiki.page where name like '%(消歧义)' or name like '%(消歧義)';";
    public static final String selectWikiDisAmbiguation = "SELECT * FROM zhwiki.MyDisambigationPages;";


    //generateRedirectSet测试用输出列表
    public static final String redirectOutput_Path = "C:\\youngsu\\EntityLinkingGitData\\data\\redirect.output";
    public static final String disambigationSemi_Output_Path = "C:\\youngsu\\EntityLinkingGitData\\data\\Semi_disambigation.output";
    public static final String disambigationOutput_Path = "C:\\youngsu\\EntityLinkingGitData\\data\\disambigation.output";

    public static final String clean_disambigationOutput_Path = "C:\\youngsu\\EntityLinkingGitData\\data\\Clean_disambigation.output";

    public static final String combin_disambigation_redirectOutput_Path = "C:\\youngsu\\EntityLinkingGitData\\data\\Combin_disambigation_redirect.output";

    public static final int SetingKeyWords = 25; //选取多少个关键词加入到BOW中
    public static final int CandidatePageNum = 3; //选取多少个维基页面作为候选
    public static final double initVector = 0.0001; //初始vector的值


    public static final String cwlmysqlurl = "jdbc:mysql://localhost:3306/enwordnet30?characterEncoding=UTF-8";

    /**
     * wc2we的一些常量
     */
    public static final String MDicInputPath = "C:\\youngsu\\Entity_LinkingData\\en_zh_wiki_page.output";
    public static final String MDicCategoryInputPath = "C:\\youngsu\\Entity_LinkingData\\en_zh_wiki_category.output";

    public static final String E_CdicPath = "C:\\youngsu\\Entity_LinkingData\\langmanC_E.ld2.output";
    public static final String ENGLIDHStopWords = "Project_EntityLinking/output/stopwords.txt";


    public static final String yagofacts = "yagofacts";
    public static final String yagotypes = "yagotypes";
    public static final String yagowikipediainfo = "yagowikipediainfo";


}
