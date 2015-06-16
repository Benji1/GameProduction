/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netserver.services.editor;

import netserver.modules.BasicModule;

public interface IShipChangedListener {
    
    public void onShipChanged(BasicModule[][] modules);
    public int getShipId();
    
}
