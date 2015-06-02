package netmsg;


import com.jme3.math.Vector3f;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import com.jme3.network.serializing.Serializer;

public class NetUtil {

    public static final int PORT = 6143;

    public static void initSerializables() {
        Serializer.registerClass(NetMsg.class);
        Serializer.registerClass(PosMsg.class);
        Serializer.registerClass(PosAndDirMsg.class);
    }

    @Serializable
    public static class PosAndDirMsg extends AbstractMessage {
    	
    	private Vector3f pos;
    	private Vector3f dir;
    	
    	public PosAndDirMsg() {}
    	
    	public PosAndDirMsg(Vector3f pos, Vector3f dir) {
    		this.pos = pos;
    		this.dir = dir;
    	}
    	
    	public Vector3f getPos() {
    		return this.pos;
    	}
    	
    	public Vector3f getDir() {
    		return this.dir;
    	}
    }
    
    @Serializable
    public static class PosMsg extends AbstractMessage {
    	
    	private Vector3f pos;
    	
    	public PosMsg() {}
    	
    	public PosMsg(Vector3f pos) {
    		this.pos = pos;
    	}
    	
    	public Vector3f getPos() {
    		return this.pos;
    	}
    }
    
    @Serializable
    public static class NetMsg extends AbstractMessage {

        private String message;

        public NetMsg() {
        }

        public NetMsg(String msg) {
            this.message = msg;
        }

        public String getMessage() {
            return this.message;
        }
    }
}