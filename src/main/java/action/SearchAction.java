package action;

import com.opensymphony.xwork2.ActionSupport;
import edu.Constant;
import edu.baike.Yago;
import edu.util.Myutil;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import rsvm.SearchDriver;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by youngsu on 16-1-6.
 */
public class SearchAction extends ActionSupport {

    private Map<String, Object> dataMap = new HashMap<String, Object>();
    @Action(value = "searchChineseMention", results = @Result(type = "json"))
    public String searchChineseMention() throws Exception {
        dataMap.clear();

//        String name = "人民大学";
//        String filecontent = "北京";
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
        while (iterator.hasNext()){
            String zhName = iterator.next();
            String enName = enNames.get(zhName);
            System.out.println(enName);
            Yago yago = search.getYagoGraph(enName);
            dataMap.put(enName, yago);
            yagos.add(yago);
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
    public Map<String, Object> getDataMap() {
        return dataMap;
    }
}
