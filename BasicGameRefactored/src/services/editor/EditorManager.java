/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package services.editor;

import Modules.BasicModule;
import java.util.ArrayList;
import java.util.Stack;
import mygame.BasicShip;
import mygame.Main;
import mygame.Player;
import services.Service;

public class EditorManager extends Service {
    
    private ArrayList<Player> players;
    
    private ArrayList<IShipChangedListener> shipChangedListeners;
    private Stack<IShipChangedListener> shipChangedListenersToRemove;
    private Stack<IShipChangedListener> shipChangedListenersToAdd;
    
    private Main app;
    
    public EditorManager() {
        players = new ArrayList<Player>();
        
        shipChangedListeners = new ArrayList<IShipChangedListener>();
        shipChangedListenersToAdd = new Stack<IShipChangedListener>();
        shipChangedListenersToRemove = new Stack<IShipChangedListener>();
    }
    
    public void notifyShipChangedListeners(BasicModule[][] modules, int playerId) {
        addShipChangedListeners();
        removeShipChangedListeners();
        
        for (IShipChangedListener listener : shipChangedListeners) {
            if (listener.getPlayerId() == playerId) {
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
    public void addPlayer(Player player) {
        this.players.add(player);
    }
    public void removePlayer(Player player) {
        this.players.remove(player);
    }
    
    public Player getPlayer(int playerId) {
        for (Player player : players) {
            if (player.getPlayerId() == playerId) {
                return player;
            }
        }
        
        return null;
    }    
    
    public void setApp(Main app) {
        this.app = app;
    }
    public Main getApp() {
        return app;
    }
}
