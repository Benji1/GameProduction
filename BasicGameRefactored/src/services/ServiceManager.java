/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import services.updater.UpdateableManager;
import java.util.HashMap;
import services.config.ConfigReader;

public class ServiceManager {
    
    private HashMap<Class<? extends Service>, Service> services;
    private static ServiceManager singleton;
    
    private ServiceManager() {
        services = new HashMap<Class<? extends Service>, Service>() {{
            put(UpdateableManager.class, new UpdateableManager());
            put(ConfigReader.class, new ConfigReader());
        }};
        
        initServices();
    }
    
    private void initServices() {
        ((ConfigReader)services.get(ConfigReader.class)).init();
    }
    
    private static <T extends Service> T getService(Class<T> className) {
        if (singleton == null) {
            singleton = new ServiceManager();
        }
        return (T)singleton.services.get(className);
    }
    
    public static UpdateableManager getUpdateableManager() {
        return getService(UpdateableManager.class);
    }
    public static ConfigReader getConfigReader() {
        return getService(ConfigReader.class);
    }
    
}
