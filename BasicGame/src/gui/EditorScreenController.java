/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *
 * @author Moritz
 */
public class EditorScreenController implements ScreenController
{
    Nifty nifty;
    
    public void bind(Nifty nifty, Screen screen) {
        System.out.println("bind " + this.getClass().getSimpleName());
        this.nifty = nifty;
    }

    public void onStartScreen() {
        System.out.println("onStartScreen " + this.getClass().getSimpleName());
    }

    public void onEndScreen() {
        System.out.println("onEndScreen " + this.getClass().getSimpleName());
    }
    
    public void quit() {
        System.out.println("quit");
        nifty.gotoScreen("start");
    }
    
}
