package edu.baike;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CBD_O9 on 2016-01-12.
 */
public class JsonResult {
    private Yago yago = new Yago();
    private GraphJson yagoJson = new GraphJson();

    public JsonResult(Yago yago) {
        this.yago = yago;
    }

    public JsonResult(GraphJson yagoJson) {
        this.yagoJson = yagoJson;
    }

    public Yago getYago() {
        return yago;
    }

    public void setYago(Yago yago) {
        this.yago = yago;
    }

    public GraphJson getYagoJson() {
        return yagoJson;
    }

    public void setYagoJson(GraphJson yagoJson) {
        this.yagoJson = yagoJson;
    }
}
