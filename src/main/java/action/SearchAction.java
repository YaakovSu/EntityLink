package action;

import com.opensymphony.xwork2.ActionSupport;
import edu.baike.Yago;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import rsvm.SearchDriver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by youngsu on 16-1-6.
 */
public class SearchAction extends ActionSupport {

    private Map<String, Object> dataMap = new HashMap<String, Object>();
    @Action(value = "searchChineseMention", results = @Result(type = "json"))
    public String searchChineseMention() throws Exception {
        dataMap.clear();

        String name = "人民大学";
        String filecontent = "北京 学校";

        SearchDriver search = new SearchDriver();
        //查询对应的英文词
        String enName = search.searchEnWords(name, filecontent);
        //寻找对应的YAGO子图
        Yago yago = search.getYagoGraph(enName);
        dataMap.put(enName, yago);
//  dataMap.put("dataMap", dataMap);
        // 放入一个是否操作成功的标识
//        dataMap.put("success", true);
        // 返回结果
        return SUCCESS;
    }
    public Map<String, Object> getDataMap() {
        return dataMap;
    }
}
