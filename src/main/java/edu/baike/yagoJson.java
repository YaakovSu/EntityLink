package edu.baike;

import java.util.List;

/**
 * Created by CBD_O9 on 2016-01-12.
 */
public class YagoJson {
    private String name;
    private List<Child> children;

    public YagoJson(String name, List<Child> children) {
        this.name = name;
        this.children = children;
    }

    public YagoJson() {
    }

    public String getName() {
        return name;
    }

    public List<Child> getChildren() {
        return children;
    }
}
