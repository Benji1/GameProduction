package netserver.universe;

import java.util.ArrayList;
import java.util.List;

import com.jme3.math.Vector3f;
import com.jme3.network.Filters;
import com.jme3.network.HostedConnection;



import netserver.BasicShip;
import netserver.WJSFServer;
import netserver.universe.Abs_ChunkNode;
import netserver.universe.CBNameGenerator;

public class SolarSystem extends Abs_ChunkNode {
	private CelestialBody sun;
	private CelestialBody[] planets;
	private List<CelestialBody> bodies;
	public float radius;
	public float timescale = 100f;
	public float sizescale = 1f;
	private float NET_STEP = 0.2f;
	private float networkTimer = 0.2f;
	private float physicsTimer = 5f;
	
	public SolarSystem(WJSFServer app, Vector3f pos) {
		super(app, CBNameGenerator.getName(), ChunkNodeType.SolarSystems, true, pos);
		this.setLocalTranslation(pos);
		this.bodies = new ArrayList<CelestialBody>();
		this.app.textNewChunk.setText(this.app.textNewChunk.getText() + "\nSolarSystem: " + this.getChunkX() + "/" + this.getChunkZ() + " - " + this.getLocalTranslation().toString() + " - " + this.getWorldTranslation().toString());
		this.init();
	}
	
	private void init(){
		float sunmass = (float) (Math.random()*0.4f+1f);
		sunmass = sunmass*sunmass*Universe.SUNMASS;
		sun = new CelestialBody(sunmass, this.getWorldTranslation(), app);
		int numPlanets = (int)(Math.random()*5+5);
		//System.out.println(numPlanets);
		this.radius = (float) (Math.sqrt(sunmass/Universe.SUNMASS) * Universe.SYSTEMRAD * numPlanets);
		//this.radius = numPlanets*MaxRadius;
		planets = new CelestialBody[numPlanets];
		bodies.add(sun);
		float radRemain = 0.85f;
		for (int i = 0; i < planets.length; i++){
			float distance = radius * (1-radRemain);
			float orbit = (float) (Math.random()*Math.PI*2);
			float x = (float) (distance * Math.cos(orbit));
			float y = (float) (distance * Math.sin(orbit));
			Vector3f pos = this.getWorldTranslation().subtract(x, 0, y);
			float mass = (float) (1f + (float) (Math.random()*2.0f) + (Math.sin((distance/radius)*Math.PI*2-Math.PI/2d)+1)*2);
			mass = mass*mass*Universe.PLANETMASS;
			planets[i] = new CelestialBody(mass, pos, app);
			planets[i].kickstart(sun);
			bodies.add(planets[i]);
			if (radRemain < 0.7f){
				int moonstop = 0;
				while (Math.random()< mass/((Universe.PLANETMASS)*(10+moonstop*20))){
					float moonmass = (float) (Math.random()*0.4f+1f);
					moonmass = moonmass*moonmass*Universe.MOONMASS;
					float rad = planets[i].radius/Universe.PLANETSIZE*Universe.MOONDISTANCE;
					float incline = (float) (Math.random()*Math.PI*2);
					orbit = (float) (Math.random()*Math.PI*2);
					x = (float) (rad * Math.sin(incline) * Math.cos(orbit));
					y = (float) (rad * Math.sin(incline) * Math.sin(orbit));
					float z = (float) (rad * Math.cos(incline));
					CelestialBody moon = new CelestialBody(moonmass, pos.add(new Vector3f(x,z,y)), app);
					System.out.println(distance+" "+planets[i].radius);
					bodies.add(moon);
					moon.kickstart(planets[i]);
					moonstop++;
				}
			}
			
			radRemain -= (radRemain/(numPlanets-i))*(Math.random()*0.4f+0.6f);	
		}
		for (CelestialBody p: bodies){
			p.updateForce(bodies);
		}
	}
	
	public void broadcastSpawn(){
		this.app.getServer().broadcast(sun.getSpawnMessage());
		for (CelestialBody p:bodies)
			this.app.getServer().broadcast(p.getSpawnMessage());
	}
	
	public void broadcastSpawnTo(HostedConnection player){
		for (CelestialBody p:bodies)
			this.app.getServer().broadcast(Filters.in(player), p.getSpawnMessage());
	}
	
	public void sendRemoveMsg(HostedConnection player) {
		for (CelestialBody p:bodies)
			this.app.getServer().broadcast(Filters.in(player), p.getRemoveMessage());
	}
	
	public void update(float tpf){
		this.physicsTimer -= tpf;
		while (this.physicsTimer <= 0){
			this.physicsTimer += Universe.P_DT;
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
			
			for (CelestialBody p:bodies) {
				for(int x = -1; x <= 1; x++) {
					for(int z = -1; z <= 1; z++) {
						for(Abs_ChunkNode s : this.app.getUniverse().getChunk(this.getChunkX() + x, this.getChunkZ() + z).getListOfType(ChunkNodeType.PlayerShips)) {
							this.app.getServer().broadcast(Filters.in(((BasicShip)s).getPlayer().con), p.getUpdateMessage());
						}
					}
				}
			}
		}
	}
}
