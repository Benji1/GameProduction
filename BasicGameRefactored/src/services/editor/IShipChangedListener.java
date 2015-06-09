/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package services.editor;

import Modules.BasicModule;

public interface IShipChangedListener {
    
    public void onShipChanged(BasicModule[][] modules);
    public int getPlayerId();
    
}
