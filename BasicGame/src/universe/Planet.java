package universe;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;

import mygame.Main;

public class Planet extends Abs_ChunkNode {
	Geometry model;
	float distance = 0;
	float orbit = 0;
	float orbitspeed = 0;
	float ellypsiness = 1.25f;
	
	public Planet(Main app){
		super(app, CBNameGenerator.getName(), ChunkNodeType.Universe);
		this.init();
	}
	
	private void init(){
		Sphere shape = new Sphere(32, 32, (float) (Math.random()*4)+1f);
		model = new Geometry(this.name+"_model", shape);
		Material sphereMat = new Material(app.getAssetManager(), 
				"Common/MatDefs/Light/Lighting.j3md");
		sphereMat.setBoolean("UseMaterialColors", true);
		sphereMat.setColor("Diffuse", ColorRGBA.randomColor());
		sphereMat.setColor("Ambient", ColorRGBA.randomColor());
		
		model.setMaterial(sphereMat);
		this.attachChild(model);
	}
	
	public void setTransform(float distance, float orbit){
		//this.setLocalTranslation(distance, 0, 0);
		this.orbitspeed = 10f/(distance*distance);
		this.distance = distance;
		this.model.setMesh(new Sphere(32, 32, ((float) (Math.random()*2.5)+2f+distance/10)*0.5f));
		this.orbit = orbit;
	}
	
	public void update(float tpf){
		this.orbit += orbitspeed*tpf;
		this.orbit%=360;
		float x = (float) (this.distance * Math.cos(this.orbit)* this.ellypsiness);
		float y = (float) (this.distance * Math.sin(this.orbit) / this.ellypsiness);
		this.setLocalTranslation(x, 0, y);
	}
}
