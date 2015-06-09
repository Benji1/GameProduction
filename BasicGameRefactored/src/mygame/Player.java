/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import Modules.BasicModule;
import com.jme3.input.RawInputListener;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;
import services.ServiceManager;
import services.editor.IShipChangedListener;

public class Player implements IShipChangedListener, RawInputListener {
    
    private static int idCounter = 0;
    
    private BasicShip ship;
    private Inventory inventory;
    private int playerId;
    private Main app;
    
    public Player(Main app) {
        this.inventory = new Inventory(ship);
        this.playerId = idCounter++;
        
        ServiceManager.getEditorManager().addPlayer(this);       
        ServiceManager.getEditorManager().registerAsShipChangedListener(this); 
        
        this.app = app;
        this.app.getInputManager().addRawInputListener(this);
    }
    
    public void setShip(BasicShip ship) {
        this.ship = ship;
    }
    
    public Inventory getInventory() {
        return inventory;
    }
    
    public int getPlayerId() {
        return playerId;
    }
    public BasicShip getShip() {
        return ship;
    }

    public void onShipChanged(BasicModule[][] modules) {
        ship.onShipChanged(modules);
    }

    public void beginInput() {
    }

    public void endInput() {
    }

    public void onJoyAxisEvent(JoyAxisEvent evt) {
    }

    public void onJoyButtonEvent(JoyButtonEvent evt) {
    }

    public void onMouseMotionEvent(MouseMotionEvent evt) {
    }

    public void onMouseButtonEvent(MouseButtonEvent evt) {
    }

    public void onKeyEvent(KeyInputEvent evt) {
        if (!evt.isRepeating()) {
            if (evt.isPressed()) {
                ship.handleKeyPressed(evt.getKeyCode());
            } else {
                ship.handleKeyReleased(evt.getKeyCode());
            }
        }
    }

    public void onTouchEvent(TouchEvent evt) {
    }
    
}
