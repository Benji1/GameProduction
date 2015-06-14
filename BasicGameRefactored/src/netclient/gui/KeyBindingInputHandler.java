/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netclient.gui;

import com.jme3.input.RawInputListener;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;

public class KeyBindingInputHandler implements RawInputListener {
    
    private EditorScreenController editorController;
    private Point modulePos;
    
    public KeyBindingInputHandler(EditorScreenController editorController, Point modulePos) {
        this.editorController = editorController;
        this.modulePos = modulePos;
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
            try {
                editorController.keyBindCallback(evt, modulePos);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public void onTouchEvent(TouchEvent evt) {
    }
    
}
