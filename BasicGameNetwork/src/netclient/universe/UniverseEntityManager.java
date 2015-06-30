package netclient.universe;

import java.util.ArrayList;
import java.util.List;

import netclient.WJSFClient;
import netclient.states.GameRunningState;

import com.jme3.math.ColorRGBA;

public class UniverseEntityManager {
	private List<UniverseEntity> entities;
	private GameRunningState app;

	
	public UniverseEntityManager(GameRunningState gameRunningState){
		this.app = gameRunningState;
		this.entities = new ArrayList<UniverseEntity>();
	}
	
	public void addEntity(float x, float y, float z, float size, int texture, ColorRGBA color, boolean light, int ID){
		System.out.println("Added entity #" + ID);
		this.entities.add(new UniverseEntity(x,y,z,size,texture,color,light,ID, app));
	}
	
	public void removeEntitiy(int ID) {
		for(int i = 0; i < entities.size(); i++) {
			if(entities.get(i).ID == ID) {
				System.out.println("Removed entity #" + this.entities.get(i).ID);
				this.entities.get(i).removeFromParent();
				entities.remove(i);
			}
		}
	}
	
	public void update(float tpf){
		for (UniverseEntity u: entities){
			u.update(tpf);
		}
	}
	
	public void updatePosition(int ID, float x, float y, float z){
		for (UniverseEntity u: entities){
			if (u.ID == ID){
				u.updatePosition(x, y, z);
			}
		}
	}
}
