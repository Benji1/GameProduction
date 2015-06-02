package netserver;

import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import netmsg.NetUtil;
import netmsg.NetUtil.*;

import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector3f;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.scene.Geometry;
import com.jme3.system.JmeContext;


public class GameProductionServer extends SimpleApplication {

    private Server server;

    public static void main(String[] args) {
        NetUtil.initSerializables();

        GameProductionServer app = new GameProductionServer();
        
        app.start(JmeContext.Type.Display); // headless type for servers!
    }

    @Override
    public void simpleInitApp() {
    	this.flyCam.setEnabled(false);
    	this.pauseOnFocus = false;
    	
        try {
            server = Network.createServer(NetUtil.PORT);
            server.start();
        } catch (IOException e) {
            Logger.getLogger(GameProductionServer.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    public void destroy() {
        //... custom code
        server.close();
        super.destroy();
    }

    @Override
    public void simpleUpdate(float tpf) {
    	/*this.counter += tpf;
    	
    	if(this.counter >= 0.5f) {
    		Vector3f newPos = new Vector3f(rnd.nextFloat() * 3, rnd.nextFloat() * 3, rnd.nextFloat() * 3);
    		server.broadcast(new PosMsg(newPos));
    		geom.setLocalTranslation(newPos);
    		this.counter = 0;
    	}
    	
        server.broadcast(new NetMsg("Hello World!!!" + tpf));*/
    }
}