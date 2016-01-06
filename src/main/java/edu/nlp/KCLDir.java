package edu.nlp;

import java.io.*;
import java.util.*;

/**
 * Created by youngsu on 15-12-31.
 * 这个是KCL的字典类
 */
public class KCLDir {
    private HashMap<String, String> dir = new HashMap<String, String>();
    private String filePath;

    public KCLDir() {
    }

    public KCLDir(String filePath) {
        this.filePath = filePath;
        init();
    }

    public HashMap<String, String> getDir() {
        return dir;
    }

    public HashSet<String> getEnSet() {
        HashSet<String> res = new HashSet<String>();
        Iterator<String> iterator = dir.keySet().iterator();
        while (iterator.hasNext()) {
            res.add(iterator.next());
        }
        return res;
    }

    public Collection<String> getZhSet() {
        return dir.values();
    }

    public boolean containsZh(String zhName) {
        return dir.containsValue(zhName);
    }

    public boolean containsEn(String enName) {
        return dir.containsKey(enName);
    }

    public String getEnByZh(String zhName) {
        Iterator<String> iterator = dir.keySet().iterator();
        while (iterator.hasNext()) {
            String enName = iterator.next();
            if (dir.get(enName).equals(zhName)) {
                return enName;
            }
        }
        return null;
    }

    public String getZhByEn(String enName) {
        return dir.get(enName);
    }


    private void init() {
        File file = new File(filePath);
        BufferedReader reader = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            reader = new BufferedReader(isr);
            String tempString = null;
//一次读一行，读入null时文件结束
            int count = 0;
            while ((tempString = reader.readLine()) != null) {
                String[] name = tempString.split("=");
                String enName = name[0];
                String zhName = name[1];
                dir.put(enName, zhName);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }
}
