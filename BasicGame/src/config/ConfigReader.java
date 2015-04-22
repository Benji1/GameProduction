/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import Modules.Thruster;
import com.json.parsers.JSONParser;
import com.json.parsers.JsonParserFactory;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 1337
 */
public class ConfigReader {

    static Map jsonData;

    public static void init() {
        StringBuilder sb = new StringBuilder();
        BufferedReader r;
        try {
            r = new BufferedReader(new FileReader("src/config/config.json"));
            String line;
            while ((line = r.readLine()) != null) {
                sb.append(line).append("\n");
            }

            JsonParserFactory factory = JsonParserFactory.getInstance();
            JSONParser parser = factory.newJsonParser();
            jsonData = parser.parseJson(sb.toString());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Thruster.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Thruster.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static <T> T get(String key, Class type) {
        if (jsonData != null) {
            if (type == float.class || type == Float.class) {
                return (T) new Float(Float.parseFloat((String) jsonData.get(key)));
            } else if (type == int.class || type == Integer.class) {
                return (T) new Integer(Integer.parseInt((String) jsonData.get(key)));
            } else if (type == double.class || type == Double.class) {
                return (T) new Double(Double.parseDouble((String) jsonData.get(key)));
            } else if (type == long.class || type == Long.class) {
                return (T) new Long(Long.parseLong((String) jsonData.get(key)));
            } else if (type == String.class) {
                return (T) (String) jsonData.get(key);
            } else if (type == boolean.class || type == Boolean.class) {
                if (((String) jsonData.get(key)).toLowerCase().equals("true") || ((String) jsonData.get(key)).equals("1")) {
                    return (T) Boolean.TRUE;
                } else {
                    return (T) Boolean.FALSE;
                }
            }
            return (T) jsonData.get(key);
        } else {
            return null;
        }
    }
}