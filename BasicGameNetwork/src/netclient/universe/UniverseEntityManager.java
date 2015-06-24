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
	
	public void addEntity(float x, float y, float size, int texture, ColorRGBA color, boolean light, int ID){
		System.out.println("Entity #"+ID);
		this.entities.add(new UniverseEntity(x,y,size,texture,color,light,ID, app));
	}
	
	public void update(float tpf){
		for (UniverseEntity u: entities){
			u.update(tpf);
		}
	}
}
