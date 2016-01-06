package action;

import java.util.ArrayList;

/**
 * Created by youngsu on 16-1-6.
 */
public class User {
    private ArrayList<String> name;
    private String id;

    public User() {
    }

    public ArrayList<String> getName() {
        return name;
    }

    public void setName(ArrayList<String> name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
