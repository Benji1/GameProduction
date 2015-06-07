/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package services.editor;

import Modules.BasicModule;
import java.util.ArrayList;
import java.util.Stack;
import mygame.BasicShip;
import services.Service;

public class EditorManager extends Service {
    
    private ArrayList<BasicShip> ships;
    
    private ArrayList<IShipChangedListener> shipChangedListeners;
    private Stack<IShipChangedListener> shipChangedListenersToRemove;
    private Stack<IShipChangedListener> shipChangedListenersToAdd;
    
    public EditorManager() {
        ships = new ArrayList<BasicShip>();
        
        shipChangedListeners = new ArrayList<IShipChangedListener>();
        shipChangedListenersToAdd = new Stack<IShipChangedListener>();
        shipChangedListenersToRemove = new Stack<IShipChangedListener>();
    }
    
    public void notifyShipChangedListeners(BasicModule[][] modules, int shipId) {
        addShipChangedListeners();
        removeShipChangedListeners();
        
        for (IShipChangedListener listener : shipChangedListeners) {
            if (listener.getShipId() == shipId) {
                listener.onShipChanged(modules);
            }
        }
    }
    
    public void registerAsShipChangedListener(IShipChangedListener listener) {
        shipChangedListenersToAdd.add(listener);
    }
    public void removeShipChangedListener(IShipChangedListener listener) {
        shipChangedListenersToRemove.remove(listener);
    }
    
    private void addShipChangedListeners() {
        while (!shipChangedListenersToAdd.empty()) {
            shipChangedListeners.add(shipChangedListenersToAdd.pop());
        }
    }
    private void removeShipChangedListeners() {
        while (!shipChangedListenersToRemove.empty()) {
            shipChangedListeners.remove(shipChangedListenersToRemove.pop());
        }
    }
    public void addShip(BasicShip ship) {
        this.ships.add(ship);
    }
    public void removeShip(BasicShip ship) {
        this.ships.remove(ship);
    }
    
    public BasicShip getShip(int shipId) {
        for (BasicShip ship : ships) {
            if (ship.getShipId() == shipId) {
                return ship;
            }
        }
        
        return null;
    }    
}
