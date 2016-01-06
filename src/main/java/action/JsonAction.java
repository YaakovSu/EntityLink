package action;

import com.opensymphony.xwork2.ActionSupport;
import edu.baike.ZhBaike;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by youngsu on 16-1-6.
 */
public class JsonAction extends ActionSupport {
    private Map<String, Object> dataMap = new HashMap<String, Object>();
    @Action(value = "testByAction", results = @Result(type = "json"))
    public String testByAction() throws Exception
    {
        dataMap.clear();
        ArrayList<String> myName = new ArrayList<String>();
        myName.add("susan");
        myName.add("zhaosi");
        User user = new User();
        user.setId("123");
        user.setName(myName);
        dataMap.put("user", user);
//  dataMap.put("dataMap", dataMap);
        // 放入一个是否操作成功的标识
        dataMap.put("success", true);
        // 返回结果
        return SUCCESS;
    }
    public Map<String, Object> getDataMap() {
        return dataMap;
    }
}
