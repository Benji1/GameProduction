/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netclient;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import java.util.concurrent.Callable;
import netutil.NetMessages;

/**
 *
 * @author Mo & Sifu
 */
public class PosAndRotMsgHandler implements Callable {
    
    private WJSFClient app;
    private NetMessages.PosAndRotMsg msg;
    
    public PosAndRotMsgHandler(WJSFClient app, NetMessages.PosAndRotMsg msg)
    {
        this.app = app;
        this.msg = msg;
    }
    
    public Object call() throws Exception {
        if (app.gameRunState.playerShip != null && app.gameRunState.playerShip.id == msg.getId()) {
            app.gameRunState.playerShip.handlePosAndRotMsg(msg);
            //Logger.getLogger(WJSFServer.class.getName()).log(Level.INFO, msg.getPos().toString());
        } else {
            for (ClientShip ship : app.gameRunState.clientShips) {
                if (ship.id == msg.getId()) {
                    ship.handlePosAndRotMsg(msg);
                    return null;
                }
            }
        }
        return null;
    }
}
