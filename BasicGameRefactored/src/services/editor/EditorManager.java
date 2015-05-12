/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package services.editor;

import Modules.BasicModule;
import java.util.ArrayList;
import services.Service;

public class EditorManager extends Service {
    
    private ArrayList<IShipChangedListener> shipChangedListeners;
    
    public EditorManager() {
        shipChangedListeners = new ArrayList<IShipChangedListener>();
    }
    
    public void notifyShipChangedListeners(BasicModule[][] modules, int shipId) {
        for (IShipChangedListener listener : shipChangedListeners) {
            if (listener.getShipId() == shipId) {
                listener.onShipChanged(modules);
            }
        }
    }
    
    public void registerAsShipChangedListener(IShipChangedListener listener) {
        shipChangedListeners.add(listener);
    }
    public void removeShipChangedListener(IShipChangedListener listener) {
        shipChangedListeners.remove(listener);
    }
    
}
