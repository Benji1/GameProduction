/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netserver.services.idProvider;

import netserver.services.Service;

public class IdProvider extends Service {
    
    private int id = 0;
    
    public int getFreeId() {
        return id++;
    }
    
}
