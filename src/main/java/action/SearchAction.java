package action;

import com.opensymphony.xwork2.ActionSupport;
import edu.Constant;
import edu.baike.*;
import edu.util.Myutil;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.xpath.SourceTree;
import rsvm.SearchDriver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by youngsu on 16-1-6.
 */
public class SearchAction extends ActionSupport {

    private Map<String, Object> dataMap = new HashMap<String, Object>();




    @Action(value = "searchChineseMention", results = @Result(type = "json"))
    public String searchChineseMention() throws Exception {

//        Myutil.deleteFile(Constant.GRAPHJSON);
//        Thread.sleep(1000);
//        File file = new File(Constant.GRAPHJSON);
//        while (file.exists()){
//            System.out.println("怎么还在");
//        }
        dataMap.clear();
//
//        String name = "华东师范大学";
//        String filecontent = "";
        HttpServletRequest request = ServletActionContext.getRequest();
        String query = request.getParameter("q");
        query = new String(query.getBytes("ISO-8859-1"), "UTF-8");
        System.out.println(query);
        String[] line = query.split("\\|\\|");
        String name = "";
        String filecontent = "";
        if(line.length<2){
            name = line[0];
        }else {
            name = line[0];
            filecontent = line[1];
        }
        System.out.println("name: "+name);
        System.out.println("context: "+filecontent);

        SearchDriver search = new SearchDriver();


        TreeMap<String,String> enNames = search.searchEnWords(name,filecontent);

        ArrayList<Yago> yagos = new ArrayList<Yago>();
        //寻找对应的YAGO子图
        Iterator<String> iterator = enNames.keySet().iterator();
        int count = 0;
        while (iterator.hasNext()){
            String zhName = iterator.next();
            String enName = enNames.get(zhName);
            System.out.println(enName);
            Yago yago = search.getYagoGraph(enName,zhName);
            if(!yago.getName().equals("")){
                String key = "yago"+count;

                if(count==0){
                    String yagoName = yago.getName();
                    ArrayList<String> yagoResult = yago.getResult();
                    ArrayList<String> yagoType = yago.getType();
                    ArrayList<String> yagoWikiInfo = yago.getWikiInfo();
                    HashMap<String,List<Child>> map = new HashMap<String, List<Child>>();

                    for(String word : yagoResult){
                        String relation = word.split(" ")[1];
                        String child = word.split(" ")[2];
                        if(!child.equals(yagoName)){
                            if(map.containsKey(relation)){
                                List<Child> values = map.get(relation);
                                Child myChild = new Child(child,"0");
                                values.add(myChild);
                                map.put(relation,values);
                            }else {
                                List<Child> values = new ArrayList<Child>();
                                Child myChild = new Child(child,"0");
                                values.add(myChild);
                                map.put(relation,values);

                            }
                        }
                    }
                    for(String word : yagoType){
                        String relation = word.split(" ")[1];
                        String child = word.split(" ")[2];
                        if(!child.equals(yagoName)){
                            if(map.containsKey(relation)){
                                List<Child> values = map.get(relation);
                                Child myChild = new Child(child,"0");
                                values.add(myChild);
                                map.put(relation,values);
                            }else {
                                List<Child> values = new ArrayList<Child>();
                                Child myChild = new Child(child,"0");
                                values.add(myChild);
                                map.put(relation,values);

                            }
                        }
                    }
                    for(String word : yagoWikiInfo){
                        String relation = word.split(" ")[1];
                        String child = word.split(" ")[2];
                        if(!child.equals(yagoName)){
                            if(map.containsKey(relation)){
                                List<Child> values = map.get(relation);
                                Child myChild = new Child(child,"0");
                                values.add(myChild);
                                map.put(relation,values);
                            }else {
                                List<Child> values = new ArrayList<Child>();
                                Child myChild = new Child(child,"0");
                                values.add(myChild);
                                map.put(relation,values);

                            }
                        }
                    }


                    List<YagoJson> list= new ArrayList<YagoJson>();
                    Iterator<String> myIterator = map.keySet().iterator();
                    while (myIterator.hasNext()){
                        String relation = myIterator.next();
                        List<Child> children = map.get(relation);
//                        HashMap<String,List<HashMap<String,Object>>> yagoJson = new HashMap<String, List<HashMap<String, Object>>>();
//                        yagoJson.put(relation,children);
                        YagoJson yagoJson = new YagoJson(relation,children);
                        list.add(yagoJson);
                    }
//                    HashMap<String,List<YagoJson>> graphJson =new HashMap<String, List<YagoJson>>();
//                    GraphJson graphJson = new GraphJson(yagoName,list);
//                    HashMap<String, List<HashMap<String,List<HashMap<String,Object>>>>> graphJson = new HashMap<String, List<HashMap<String, List<HashMap<String, Object>>>>>();
//                    graphJson.put(yagoName,list);
                    GraphJson graphJson =new GraphJson(yagoName,list);
//                    graphJson.setName(yagoName);
//                    graphJson.setChildren(list);
//                    JsonResult jr = new JsonResult(graphJson);

                    dataMap.put("graphJson",graphJson);
//                    FileWriter fw = new FileWriter(Constant.GRAPHJSON);
//                    PrintJson(fw, graphJson);
//                    fw.close();
////                    File file = new File(Constant.GRAPHJSON);
//                    Thread.sleep(10000);
                }

//                JsonResult jr = new JsonResult(yago);
                dataMap.put(key, yago);
                count++;
                yagos.add(yago);
            }

        }
        Myutil.deleteFile(Constant.QCWE_PREDICT);
        Myutil.deleteFile(Constant.QCWE_FEATURE);
        Myutil.deleteFile(Constant.QCWE_NAME);


        //查询对应的英文词
//        TreeMap<String,String> enNames = search.searchEnWords(name, filecontent);
//        Iterator<String> iterator = enNames.keySet().iterator();
//        while (iterator.hasNext()){
//            String zhName = iterator.next();
//            String enName = enNames.get(zhName);
//            //寻找对应的YAGO子图
//            Yago yago = search.getYagoGraph(enName);
//            dataMap.put(enName, yago);
//        }
        // 放入一个是否操作成功的标识
//        dataMap.put("success", true);
        // 返回结果
        return SUCCESS;
    }

    private void PrintJson(FileWriter fw, GraphJson graphJson) throws IOException {
        String name = graphJson.getName();
        fw.write("{"+"\n");
        fw.write("\"name\""+": \""+name+"\","+"\n");
        fw.write("\"children\""+": ["+"\n");
        List<YagoJson> yagoJsons = graphJson.getChildren();
        int num = 0;
        for(YagoJson yagoJson :yagoJsons){
            num++;
            String yagoName = yagoJson.getName().replace("\"","");
            fw.write("{"+"\n");
            fw.write("\"name\""+": \""+yagoName+"\","+"\n");
            fw.write("\"children\""+": ["+"\n");
            List<Child> children = yagoJson.getChildren();
            int count = 0;
            for(Child child : children){
                count++;
                String childName = child.getName().replace("\"","");
                fw.write("{"+"\n");
                fw.write("\"name\""+": \""+childName+"\","+"\n");
                fw.write("\"size\""+": "+0+"\n");
                if(count<children.size()){
                    fw.write("},");
                }else {
                    fw.write("}");
                }
            }
            fw.write("]"+"\n");
            if(num<yagoJsons.size()){
                fw.write("},");
            }else {
                fw.write("}");
            }

        }
        fw.write("]"+"\n");
        fw.write("}");



    }

        public Map<String, Object> getDataMap() {
        return dataMap;
    }
}
