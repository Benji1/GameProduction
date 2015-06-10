/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.jme3.input.KeyInput;
import com.jme3.input.RawInputListener;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;

public class KeyBindingInputHandler implements RawInputListener {
    
    public static final int[] ALLOWED_KEYS = new int[] {
        KeyInput.KEY_0, KeyInput.KEY_1, KeyInput.KEY_2, KeyInput.KEY_3,
        KeyInput.KEY_4, KeyInput.KEY_5, KeyInput.KEY_6, KeyInput.KEY_7,
        KeyInput.KEY_8, KeyInput.KEY_9,
        KeyInput.KEY_A, KeyInput.KEY_B, KeyInput.KEY_C, KeyInput.KEY_D,
        /*KeyInput.KEY_E,*/ KeyInput.KEY_F, KeyInput.KEY_G, KeyInput.KEY_H,
        KeyInput.KEY_I, KeyInput.KEY_J, KeyInput.KEY_K, KeyInput.KEY_L,
        KeyInput.KEY_M, KeyInput.KEY_N, KeyInput.KEY_O, KeyInput.KEY_P,
        KeyInput.KEY_Q, KeyInput.KEY_R, KeyInput.KEY_S, KeyInput.KEY_T,
        KeyInput.KEY_U, KeyInput.KEY_V, KeyInput.KEY_W, KeyInput.KEY_X,
        KeyInput.KEY_Y, KeyInput.KEY_Z,
        KeyInput.KEY_SPACE, 
        KeyInput.KEY_LSHIFT, KeyInput.KEY_RSHIFT, 
        KeyInput.KEY_RCONTROL, KeyInput.KEY_LCONTROL,
        KeyInput.KEY_TAB,
        KeyInput.KEY_LEFT, KeyInput.KEY_RIGHT, KeyInput.KEY_UP, KeyInput.KEY_DOWN
    };
    
    private EditorScreenController editorController;
    private Point modulePos;
    
    public KeyBindingInputHandler(EditorScreenController editorController, Point modulePos) {
        this.editorController = editorController;
        this.modulePos = modulePos;
    }
    
    public static boolean isAllowedKey(int keyCode) {
        for (int i=0; i<ALLOWED_KEYS.length; i++) {
            if (ALLOWED_KEYS[i] == keyCode) {
                return true;
            }
        }
        
        return false;
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
        if (!evt.isRepeating() && evt.isPressed()) {
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
