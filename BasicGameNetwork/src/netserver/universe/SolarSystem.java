package netserver.universe;

import com.jme3.math.Vector3f;
import com.jme3.network.Filters;
import com.jme3.network.HostedConnection;

import netclient.WJSFClient;
import netserver.WJSFServer;
import netserver.universe.Abs_ChunkNode;
import netserver.universe.CBNameGenerator;
import netserver.universe.Planet;
import netserver.universe.Sun;
import netserver.universe.Abs_ChunkNode.ChunkNodeType;

public class SolarSystem extends Abs_ChunkNode {
	private Sun sun;
	private Planet[] planets;
	public float radius;
	private static float MaxRadius = 110f;
	private static float MinRadius = 85f;
	public float timescale = 100f;
	public float sizescale = 1f;
	
	public SolarSystem(WJSFServer app, Vector3f pos) {
		super(app, CBNameGenerator.getName(), ChunkNodeType.SolarSystems, true, pos);
		this.init();
	}
	
	private void init(){
		sun = new Sun(app, this);
		this.attachChild(sun);
		int numPlanets = (int)(Math.random()*9+5);
		//System.out.println(numPlanets);
		this.radius = (float) (((Math.random()*(MaxRadius-MinRadius))+MinRadius)*numPlanets);
		//this.radius = numPlanets*MaxRadius;
		planets = new Planet[numPlanets];
		float radRemain = this.radius - MinRadius / 3f;
		for (int i = 0; i < planets.length; i++){
			planets[i] = new Planet(app, this);
			this.attachChild(planets[i]);
			float distance = (float) ((radRemain/(float)(numPlanets-i))*((Math.random()*0.75f)+0.25f)) + this.radius-radRemain;
			//System.out.println(distance);
			radRemain += (radius - radRemain) - distance;
			planets[i].setTransform(distance, (float) (Math.random()*360f));			
		}
	}
	
	public void broadcastSpawn(){
		this.app.getServer().broadcast(sun.getSpawnMessage());
		for (Planet p:planets)
			this.app.getServer().broadcast(p.getSpawnMessage());
	}
	
	public void broadcastSpawnTo(HostedConnection player){
		this.app.getServer().broadcast(Filters.in(player), sun.getSpawnMessage());
		for (Planet p:planets)
			this.app.getServer().broadcast(Filters.in(player), p.getSpawnMessage());
	}
	
	public void update(float tpf){
		sun.update(tpf);
		for (Planet p:planets)
			p.update(tpf*timescale);
	}
}
