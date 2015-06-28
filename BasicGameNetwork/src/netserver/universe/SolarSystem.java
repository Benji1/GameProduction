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
	private CelestialBody sun;
	private CelestialBody[] planets;
	private CelestialBody[] bodies;
	public float radius;
	private static float MaxRadius = 200f;
	private static float MinRadius = 200f;
	public float timescale = 100f;
	public float sizescale = 1f;
	private float NET_STEP = 0.2f;
	private float networkTimer = 0.2f;
	private float physicsTimer = 5f;
	
	public SolarSystem(WJSFServer app, Vector3f pos) {
		super(app, CBNameGenerator.getName(), ChunkNodeType.SolarSystems, true, pos);
		this.setLocalTranslation(pos);
		System.out.println(this.getLocalTranslation());
		this.init();
	}
	
	/*
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
	}*/
	
	private void init(){
		float sunmass = (float) (Math.random()*0.4f+1f);
		sunmass = sunmass*sunmass*Universe.SUNMASS;
		sun = new CelestialBody(sunmass, this.getWorldTranslation(), app);
		int numPlanets = (int)(Math.random()*5+6);
		//System.out.println(numPlanets);
		this.radius = (float) (Math.sqrt(sunmass/Universe.SUNMASS) * Universe.SYSTEMRAD * numPlanets);
		//this.radius = numPlanets*MaxRadius;
		planets = new CelestialBody[numPlanets];
		bodies = new CelestialBody[numPlanets+1];
		bodies[0] = sun;
		float radRemain = 0.85f;
		for (int i = 0; i < planets.length; i++){
			float distance = radius * (1-radRemain);
			Vector3f pos = this.getWorldTranslation().subtract(distance, 0, 0);
			float mass = (float) (1f + (float) (Math.random()*2.0f) + (Math.sin((distance/radius)*Math.PI*2-Math.PI/2d)+1)*2);
			mass = mass*mass*Universe.PLANETMASS;
			planets[i] = new CelestialBody(mass, pos, app);
			planets[i].kickstart(sun);
			bodies[i+1] = planets[i];
			radRemain -= (radRemain/(numPlanets-i))*(Math.random()*0.4f+0.6f);	
		}
	}
	
	@Override
	public void setLocalTranslation(Vector3f pos){
		
	}
	
	public void broadcastSpawn(){
		this.app.getServer().broadcast(sun.getSpawnMessage());
		for (CelestialBody p:planets)
			this.app.getServer().broadcast(p.getSpawnMessage());
	}
	
	public void broadcastSpawnTo(HostedConnection player){
		this.app.getServer().broadcast(Filters.in(player), sun.getSpawnMessage());
		for (CelestialBody p:planets)
			this.app.getServer().broadcast(Filters.in(player), p.getSpawnMessage());
	}
	
	public void update(float tpf){
		this.physicsTimer -= tpf;
		while (this.physicsTimer <= 0){
			this.physicsTimer += Universe.P_DT;
			
			for (CelestialBody p: bodies){
				p.updateForce(bodies);
			}
			for (CelestialBody p: bodies){
				p.updatePosition();
			}
			for (CelestialBody p: bodies){
				p.updateForce(bodies);
			}
			for (CelestialBody p: bodies){
				p.updateVelocity();
			}
		}


		this.networkTimer -= tpf;
		if (this.networkTimer <= 0){
			this.networkTimer += this.NET_STEP;
			for (CelestialBody p:bodies)
				this.app.getServer().broadcast(p.getUpdateMessage());
		}
	}
}
