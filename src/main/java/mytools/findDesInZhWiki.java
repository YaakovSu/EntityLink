package mytools;

/**
 * Created by CBD_O9 on 2015-04-08.
 */

import edu.util.Myutil;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by CBD_O9 on 2014-12-07.
 * <p>
 * <p>
 * 该类获得一个中文文本和一个英文文本中的名词的相似度得分，作为第7个feature
 */
public class findDesInZhWiki {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        findDesInZhWiki test = new findDesInZhWiki();

        String text1 = "{{nofootnotes|time=2012-03-28T10:47:01+00:00}}\n" +
                " {{About}}\n" +
                " {{noteTA\n" +
                " |1=zh-hans:澳洲青苹;zh-hant:澳洲青蘋;\n" +
                " }}\n" +
                " {{Taxobox\n" +
                " | color = lightgreen\n" +
                " | name = 苹果\n" +
                " | image = Malus domestica - Köhler–s Medizinal-Pflanzen-108.jpg\n" +
                " | image_width = 250px\n" +
                " | image_caption = 《科勒藥用植物》(1897), <br>''Pirus malus''\n" +
                " | regnum = [[植物界]] Plantae\n" +
                " | phylum = [[被子植物门]] Magnoliophyta\n" +
                " | classis = [[双子叶植物纲]] Magnoliopsida\n" +
                " | ordo = [[蔷薇目]] Rosales\n" +
                " | familia = [[蔷薇科]] Rosaceae\n" +
                " | subfamilia = [[苹果亚科]] Maloideae\n" +
                " | genus = [[苹果属]] ''Malus''\n" +
                " | species = '''苹果 ''M. domestica'''''\n" +
                " | binomial = ''Malus domestica''\n" +
                " | binomial_authority = [[Moritz Balthasar Borkhausen|Borkh.]]\n" +
                " }}\n" +
                " {{营养值 | name=生苹果<small>（去皮）</small>| water =86.67 g| kJ =200| protein =0.27 g| fat =0.13 g| carbs =12.76 g| fiber =1.3 g| sugars =10.1 g| calcium_mg =5| iron_mg =0.07| magnesium_mg =4| phosphorus_mg =11| potassium_mg =90| sodium_mg =0| zinc_mg =0.05| manganese_mg =0.038| selenium_μg =0| vitC_mg =4| thiamin_mg =0.019| riboflavin_mg =0.028| niacin_mg =0.091| pantothenic_mg =0.071| vitB6_mg=0.037| folate_ug =0| choline_mg =3.4| vitB12_ug =0| vitA_ug =2| betacarotene_ug =17| lutein_ug =18| vitE_mg =0.05| vitD_iu =0| vitK_ug =0.6| satfat =0.021 g| monofat =0.005 g| polyfat =0.037 g| tryptophan =0.001 g| threonine =0.006 g| isoleucine =0.006 g| leucine =0.014 g| lysine =0.013 g| methionine =0.001 g| cystine =0.001 g| phenylalanine =0.007 g| tyrosine =0.001 g| valine =0.012 g| arginine =0.006 g| histidine =0.005 g| alanine =0.012 g| aspartic acid =0.074 g| glutamic acid =0.026 g| glycine =0.009 g| proline =0.006 g| serine =0.011 g| right=1 | source_usda=1 }}\n" +
                " '''苹果'''（[[双名法|学名]]：''Malus domestica''）是[[蔷薇科]][[苹果亚科]][[苹果属]][[植物]]，[[落叶乔木]]。苹果的[[果实]]富含[[矿物质]]和[[维生素]]，为人们最常食用的[[水果]]之一。\n" +
                " \n" +
                " ==特點==\n" +
                " 蘋果是落葉[[喬木]]，通常樹木可高至15米，但栽培樹木一般只高3-5米左右。樹幹呈灰褐色，樹皮有一定程度的脫落。[[果實]]一般呈紅色，但需視乎品種而定。\n" +
                " \n" +
                " 蘋果[[開花]]期是基於各地氣候而定，但一般集中在4-5月份。蘋果是異花授粉植物，大部分品種自花不能結成果實。\n" +
                " \n" +
                " 一般蘋果栽種後，於2-3年才開始結出果實。果實成長期之長短，一般早熟品種為65-87天，中熟品種為90-133天，晚熟品種則為137-168天。在一般情形下，栽種後蘋果可有15-50年壽命。\n" +
                " \n" +
                " == 营养 ==\n" +
                " [[File:Pommes d amour.jpg|thumb|苹果做的甜点（糖苹果）]]\n" +
                " 苹果含有大量的果膠，這種可溶性纖維質可以降低膽固醇及壞膽固醇的含量。一中等大小未削皮的蘋果可提供3.5克的纖維質（即使削了皮，也含2.7克的纖維質），是營養專家建議的每日攝取量的百分之10以上，而且僅含80[[卡路里]]的熱量。《美國生理學期刊》的研究指出，在深紅色的生果皮，如蘋果及提子等，均驗出有[[白藜蘆醇]]（Resveraltrol）。該物質可減少呼吸系統包括氣管及肺部等的發炎，從而控制哮喘及慢性阻塞性肺炎等病症，保護肺部免受空氣中灰塵和煙塵的影響。\n" +
                " \n" +
                " 蘋果中的[[果膠]]和[[鞣酸]]有收斂作用，可將腸道內積聚的毒素和廢物排出體外。其中的粗纖維能鬆軟糞便，利於[[排泄]]；有機酸能刺激[[腸壁]]，增加蠕動作用；而[[維生素C]]更有效保護心血管、心臟病患者的健康元素。蘋果皮中富含多酚，多酚含有極佳的抗氧化作用，有助於抗老防癌。\n" +
                " \n" +
                " 蘋果富含鉀，可排出體內吸收過多的鈉元素，還有調節體內的水分平衡，促進排尿的作用。\n" +
                " \n" +
                " 吃蘋果既能減肥，又能幫助消化，且蘋果中含有多種維生素、礦物質、糖類、脂肪等，是構成大腦所必須的營養成分。 \n" +
                " \n" +
                " 蘋果中的纖維，對兒童的生長發育有益，能促進生長及發育。蘋果中的鋅對兒童的記憶有益，能增強兒童的記憶力。但蘋果中的酸能腐蝕牙齒，吃完蘋果後最好漱漱口。\n" +
                " \n" +
                " == 历史 ==\n" +
                " 苹果原產於[[欧洲]]和[[中亞細亞]]。[[哈薩克]]的[[阿拉木圖]]与新疆[[阿力麻里]]有蘋果城的美譽。中国古代的[[林檎]]、柰、[[花红 (植物)|花红]]等水果被认为是中国土生苹果品种或与苹果相似的水果。苹果在中国的栽培记录可以追溯至[[西汉]]时期，[[汉武帝]]时，上林苑中曾栽培林檎和柰，当时多用于薰香衣裳等，亦有置于床头当香熏或置于衣服，最初作为香囊，较少食用。但也有看法认为，林檎和柰是现在的[[沙果]]，曾被誤認為蘋果，真正意义上的苹果是[[元朝]]时期从中亚地区传入中国，当时只有在宫廷才可享用。\n" +
                " \n" +
                " [[现代汉语]]所说的“苹果”一词源于[[梵语]]，为[[古印度]][[佛經]]中所說的一种水果，最早被称为“频婆”，后被汉语借用，并有“平波”、“苹婆”等写法。[[明朝]][[万历]]年间的農書《群芳谱·果谱》中，有“苹果”词条，称：“苹果，出北地，燕赵者尤佳。接用林檎体。树身耸直，叶青，似林檎而大，果如梨而圆滑。生青，熟则半红半白，或全红，光洁可爱玩，香闻数步。味甘松，未熟者食如棉絮，过熟又沙烂不堪食，惟八九分熟者最佳”。许多中国农学史、果树史专家认为这是汉语中最早使用“苹果”一词。\n" +
                " \n" +
                " 中國土生苹果属植物在古代又稱「'''柰'''」<ref>「柰」，音「奈」，[[汉语拼音]]：nài，[[注音]]：ㄋㄞˋ</ref>或「'''林檎'''」。[[李時珍]]說：「柰與林檎，一類二種也，樹實皆似林檎而大。有白、赤、青三色，白者為素柰，赤者為丹柰，青者為綠柰」和「林檎，即柰之小而圓者，其類有金林檎、紅林檎、水林檎、蜜林檎、黑林檎，皆以色味立名。」。而《[[食性本草]]》中亦有說「林檎有三種，大長者為柰，圓者林檎，小者味澀為梣。」 「林檎」一詞後來傳至日本，現時[[日語]]的「林檎」仍保留蘋果的意思。\n" +
                " \n" +
                " 中国土生苹果品种在清朝以前曾在今[[河北]]、[[山东]]等地广泛种植，其特点是产量少、果实小、皮薄、味道甜美，但不耐储存，容易破损，因此价格昂贵，清朝时期北京[[旗人]]用其当作贡果。清朝末年，美国人在山东[[烟台]]等地引进西洋品种苹果，日本在[[日俄战争]]之后，也在[[关东州]]的熊岳设立农业试验基地，引进西洋苹果并进行杂交改良。[[烟台]]和[[大连]]也因此成为今日著名的苹果产地。民国时期以后，西洋品种苹果逐渐在中国市场上占据主要地位，中国土生品种苹果逐渐被果农淘汰，种植范围不断缩小，最后仅[[河北省]]怀来地区有少量保存，但这些果树也于1970年代前后在中国灭绝。\n" +
                " \n" +
                " \n" +
                " \n" +
                " \n" +
                " \n" +
                " === 通俗文化 ===\n" +
                " 不少公司、企业都以苹果为公司名字甚至品牌名字，如[[苹果公司]]、[[苹果日报]]、苹果牌牛仔裤。\n" +
                " \n" +
                " == 图片 ==\n" +
                " <gallery>\n" +
                " File:Apples.jpg|各式各样的苹果\n" +
                " File:GreenApple.png|绿苹果\n" +
                " File:Granny smith closeup.jpg|绿苹果近图\n" +
                " File:Brimley Apples.jpg|Brimley 绿色大苹果\n" +
                " File:Fuji apple.jpg|富士苹果\n" +
                " File:Golden delicious apple.jpg|香蕉苹果\n" +
                " File:Jonagold.jpg|乔纳金苹果\n" +
                " File:Yellow Transparent.jpg|黄魁苹果\n" +
                " File:Arkansas Black apples.jpg|阿肯色黑苹果\n" +
                " File:Malus-Ananasrenette.jpg|[[:de:Ananasrenette|Ananasrenette]](品种)苹果\n" +
                " File:Malus Goldrenette F. v. Berlepsch.jpg|[[:de:Berlepsch|Berlepsch]](品种)苹果\n" +
                " File:Goudreinet.jpg|[[:nl:Goudreinet|Goudreinet]](品种)苹果\n" +
                " File:Apple juice with 3apples.jpg|苹果与[[苹果汁]]\n" +
                " File:Sterappel dwarsdrsn.jpg|苹果横切面图\n" +
                " </gallery>\n" +
                " \n" +
                " == 參考文獻 ==\n" +
                " {{reflist}}\n" +
                " \n" +
                " == 参看 ==\n" +
                " {{commons|Apple}}\n" +
                " * [[蘋果汁]]\n" +
                " * [[花红 (植物)|花红]]\n" +
                " * [[海棠 (苹果属)]]\n" +
                " * [[水果]]\n" +
                " * [[果树]]\n" +
                " * [[水果列表]]\n" +
                " \n" +
                " == 外部链接 ==\n" +
                " * [http://www.nutritionno1.com/article/archive/food/y2k-food/html/y2k-food03.html 今日營養 - 蘋果 (Apple)]\n" +
                " * [http://www.aomori-apple.jp/ 來自日本的青森蘋果]\n" +
                " * 张帆：《[http://agri-history.ihns.ac.cn/scholars/zhangfan1.htm 频婆果考——中国苹果栽培史之一斑]》《国学研究》第13卷，北京大学出版社，2004年6月\n" +
                " \n" +
                " \n" +
                " {{苹果}}\n" +
                " {{果实汇总}}\n" +
                " \n" +
                " [[Category:苹果属]]\n" +
                " [[Category:水果]]\n" +
                " [[Category:梵語詞彙|P]]\n" +
                " \n" +
                " {{Link GA|id}}\n" +
                " {{Link GA|en}}\n" +
                " {{Link FA|ta}}\n" +
                " \n" +
                " [[de:Kulturapfel#Früchte]]\n" +
                " [[fi:Tarhaomenapuu]]\n" +
                " [[gl:Maceira]]";


        String text = test.getTextDes(text1);
        System.out.println(text);

    }


    /**
     * 计算两个简介的名词相似度
     *
     * @param text1
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public String getTextDes(String text1) throws IOException, ClassNotFoundException {

        ArrayList<String> text1MingCi = new ArrayList<String>();

        String[] text1Lines = text1.split("\n");

        /**
         * 只使用简介部分来进行处理
         */

        for (String line : text1Lines) {

            if (line.contains("'''") && (!line.startsWith("{")) && (!line.startsWith(" [")) && (!line.startsWith(" |")) && (!line.startsWith("[")) && (!line.startsWith(" {"))) {
                text1 = line;
                break;
            }
        }
        text1 = cleantext(text1);
        return text1;

    }


    private static String cleantext(String fileContent) {

        fileContent = fileContent.replaceAll("\\d+", "");
        String pattern = "([-+*/^()\\]\\[])";
        fileContent = fileContent.replaceAll(pattern, "");
        fileContent = fileContent.replace(".", "");

        fileContent = Myutil.getCleanText(fileContent);
        return fileContent;
    }
}

