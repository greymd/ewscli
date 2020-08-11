package ewscli.util;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.Properties;

public class ConfigFile {
    private Properties prop;

    public ConfigFile() {
        prop = new Properties();
    }

    public void load(String path) {
        var file = new File(path);
        try (var inputStream = new FileInputStream(file)) {
            prop.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(String path) {
        try {
            prop.store(new FileOutputStream(path), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean delete(String path) {
        var file = new File(path);
        return file.delete();
    }

    public boolean isPropertyExist(String path) {
        var file = new File(path);
        if(!file.exists()){
            return false;
        }
        var tmp = new Properties();
        try (var inputStream = new FileInputStream(file)) {
            tmp.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // return false if there is no configures.
        return tmp.keys().hasMoreElements();
    }

    public boolean hasNecessaryConfigures (String... keys) {
        for (String key: keys) {
            if(StringUtils.isEmpty(getProperty(key))) return false;
        }
        return true;
    }

    public boolean create(String path) {
        var file = new File(path);
        if(file.getParentFile() != null ) {
            if( !file.getParentFile().exists() && !file.getParentFile().mkdirs()) return false;
        }
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setProperty(String key, String value) {
        this.prop.setProperty(key, value);
    }

    public String getProperty(String key) {
        String value = this.prop.getProperty(key);
        return value;
    }
}