package universe;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;

import mygame.Main;

public class Planet extends Abs_ChunkNode {
	Geometry model;
	SolarSystem system;
	float distance = 0;
	float orbit = 0;
	float orbitspeed = 0;
	float ellypsiness = 1.25f;
	
	public Planet(Main app, SolarSystem system){
		super(app, CBNameGenerator.getName(), ChunkNodeType.Universe);
		this.system = system;
		this.init();
	}
	
	private void init(){
		Sphere shape = new Sphere(32, 32, (float) ((Math.random()*4)+1f));
		
		model = new Geometry(this.name+"_model", shape);
		
		
		this.attachChild(model);
	}
	
	public void setTransform(float distance, float orbit){
		//this.setLocalTranslation(distance, 0, 0);
		this.orbitspeed = 10f/(distance*distance);
		this.distance = distance;
		Sphere shape = new Sphere(32, 32, getSize());
		shape.setTextureMode(Sphere.TextureMode.Projected);
		
		Material sphereMat = new Material(app.getAssetManager(), 
				"Common/MatDefs/Light/Lighting.j3md");
		sphereMat.setBoolean("UseMaterialColors", true);
		
		ColorRGBA color = ColorRGBA.randomColor();
		sphereMat.setColor("Diffuse", color);
		sphereMat.setColor("Ambient", color);
		
		
		sphereMat.setTexture("DiffuseMap", getTexture());
		
		model.setMaterial(sphereMat);

		
		
		this.model.setMesh(shape);
		this.orbit = orbit;
		Quaternion rotation = new Quaternion();
		rotation.fromAngles(1.2f, 0, 0);
		this.setLocalRotation(rotation);
	}
	
	private float getSize(){
		float size =  1f;
		size += (float) (Math.random()*2.0f);
		size += (Math.sin((distance)/(double)(system.radius)* 3.4f -0.25f))*10f;
		//size *= (distance*system.sizescale)/(double)(system.radius);
		size *=  system.sizescale;
		return size;
	}
	
	private Texture getTexture(){
		int type = (int)(Math.random()*2+1);
		if (distance/system.radius < 0.3f || distance/system.radius > 0.8f){
			return app.getAssetManager().loadTexture("textures/planet_solid_"+type+".png");
		}
		else
		{
			return app.getAssetManager().loadTexture("textures/planet_gas_"+type+".png");
		}
	}
	
	public void update(float tpf){
		this.orbit += orbitspeed*tpf;
		this.orbit%=360;
		float x = (float) (this.distance *system.sizescale * Math.cos(this.orbit)* this.ellypsiness);
		float y = (float) (this.distance *system.sizescale * Math.sin(this.orbit) / this.ellypsiness);
		this.setLocalTranslation(x, 0, y);
		this.model.rotate(0, 0, orbitspeed*tpf*10);
	}
}
