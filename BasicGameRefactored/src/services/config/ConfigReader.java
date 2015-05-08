/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package services.config;

import com.json.parsers.JSONParser;
import com.json.parsers.JsonParserFactory;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import services.Service;

/**
 *
 * @author 1337
 */
public class ConfigReader extends Service {

    public Map jsonData;

    public void init() {
        StringBuilder sb = new StringBuilder();
        BufferedReader r;
        try {
            r = new BufferedReader(new FileReader("src/services/config/config.json"));
            
            String line;
            while ((line = r.readLine()) != null) {
                sb.append(line).append("\n");
            }

            JsonParserFactory factory = JsonParserFactory.getInstance();
            JSONParser parser = factory.newJsonParser();
            jsonData = parser.parseJson(sb.toString());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ConfigReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConfigReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Map getBaseMap(String key) {
        return  (Map) jsonData.get(key);
    }
    
    public <T> T getFromMap(Map m, String key, Class type) {
        if (m != null) {
            if (type == float.class || type == Float.class) {
                return (T) new Float(Float.parseFloat((String) m.get(key)));
            } else if (type == int.class || type == Integer.class) {
                return (T) new Integer(Integer.parseInt((String) m.get(key)));
            } else if (type == double.class || type == Double.class) {
                return (T) new Double(Double.parseDouble((String) m.get(key)));
            } else if (type == long.class || type == Long.class) {
                return (T) new Long(Long.parseLong((String) m.get(key)));
            } else if (type == String.class) {
                return (T) (String) m.get(key);
            } else if (type == boolean.class || type == Boolean.class) {
                if (((String) m.get(key)).toLowerCase().equals("true") || ((String) m.get(key)).equals("1")) {
                    return (T) Boolean.TRUE;
                } else {
                    return (T) Boolean.FALSE;
                }
            } else if (type == Map.class) {
                return (T) m.get(key);
            }
            return (T) m.get(key);
        } else {
            return null;
        }
    }
}