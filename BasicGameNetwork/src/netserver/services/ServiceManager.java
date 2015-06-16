/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netserver.services;

import java.util.HashMap;

import netserver.services.config.ConfigReader;
import netserver.services.editor.EditorManager;
import netserver.services.idProvider.IdProvider;
import netserver.services.updater.UpdateableManager;

public class ServiceManager {
    
    private HashMap<Class<? extends Service>, Service> services;
    private static ServiceManager singleton;
    
    private ServiceManager() {
        services = new HashMap<Class<? extends Service>, Service>() {{
            put(UpdateableManager.class, new UpdateableManager());
            put(ConfigReader.class, new ConfigReader());
            put(EditorManager.class, new EditorManager());
            put(IdProvider.class, new IdProvider());
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
    public static EditorManager getEditorManager() {
        return getService(EditorManager.class);
    }
    public static IdProvider getIdProvider() {
        return getService(IdProvider.class);
    }
    
}
