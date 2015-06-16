package netserver.universe;

import netclient.WJSFClient;
import netserver.WJSFServer;
import netserver.universe.Abs_ChunkNode;
import netserver.universe.CBNameGenerator;
import netserver.universe.SolarSystem;
import netserver.universe.Abs_ChunkNode.ChunkNodeType;

import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;

public class Sun extends Abs_ChunkNode {
	Geometry model;
	PointLight light;
        public float radius;
	SolarSystem system;
        
	public Sun(WJSFServer app, SolarSystem parent){
		super(app, CBNameGenerator.getName(), ChunkNodeType.Universe);
		this.system = parent;
		this.init();
	}
	
	private void init(){
		radius = (float) (Math.random()*10f+25f) * system.sizescale;
		Sphere shape = new Sphere(32, 32, radius);
		model = new Geometry(this.name+"_model", shape);
		Material sphereMat = new Material(app.getAssetManager(), 
				"Common/MatDefs/Misc/Unshaded.j3md");
		sphereMat.setColor("Color", (ColorRGBA.White).mult(1.4f));
		sphereMat.setTexture("ColorMap", app.getAssetManager().loadTexture("textures/texture_sun.jpg"));
		model.setMaterial(sphereMat);
		this.attachChild(model);
                Quaternion rotation = new Quaternion();
		rotation.fromAngles(1.2f, 0, 0);
		model.setLocalRotation(rotation);
		light = new PointLight();
		light.setColor((new ColorRGBA(1f, 0.9f,0.6f,0f)).mult(3f));
		light.setRadius(radius*100);
		light.setPosition(this.getWorldTranslation());
		app.getRootNode().addLight(light);
	}
	
	public void update(float tpf){
		light.setPosition(this.getWorldTranslation().add(new Vector3f(0,20,0)));
                this.model.rotate(0, 0, tpf*0.1f);
        }
}
