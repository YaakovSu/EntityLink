package action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by youngsu on 16-1-6.
 */
public class HelloAction extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();

    public String hello() {

        request.setAttribute("hello", "hello world!");

        return SUCCESS;

    }
}
