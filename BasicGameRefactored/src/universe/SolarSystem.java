package universe;

import mygame.Main;
import universe.Abs_ChunkNode.ChunkNodeType;

public class SolarSystem extends Abs_ChunkNode {
	private Sun sun;
	private Planet[] planets;
	public int radius;
	private static int MaxRadius = 10;
	private static int MinRadius = 1;
	public float timescale = 200f;
	
	public SolarSystem(Main app){
		super(app, CBNameGenerator.getName(), ChunkNodeType.Universe);
		this.init();
	}
	
	private void init(){
		sun = new Sun(app, this);
		this.attachChild(sun);
		int numPlanets = (int)(Math.random()*9+5);
		System.out.println(numPlanets);
		this.radius = (int) (((Math.random()*(MaxRadius-MinRadius))+MinRadius)*numPlanets);
		planets = new Planet[numPlanets];
		int radRemain = this.radius - MaxRadius*2;
		for (int i = 0; i < planets.length; i++){
			planets[i] = new Planet(app);
			this.attachChild(planets[i]);
			float distance = (float) ((radRemain/(float)(numPlanets-i))*((Math.random()*0.75f)+0.25f)) + this.radius-radRemain;
			radRemain -= distance;
			planets[i].setTransform(distance, (float) (Math.random()*360f));
			
		}
	}
	
	public void update(float tpf){
		sun.update(tpf);
		for (Planet p:planets)
			p.update(tpf*timescale);
	}
}
