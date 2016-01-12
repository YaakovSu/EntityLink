package edu.baike;

import java.util.List;

/**
 * Created by CBD_O9 on 2016-01-12.
 */
public class GraphJson {
    private String name;
    private List<YagoJson> children;

    public GraphJson(String name, List<YagoJson> children) {
        this.name = name;
        this.children = children;
    }

    public GraphJson() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<YagoJson> getChildren() {
        return children;
    }

    public void setChildren(List<YagoJson> children) {
        this.children = children;
    }
}
