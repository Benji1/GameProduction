package universe;

import java.awt.Color;

import universe.Abs_ChunkNode.ChunkNodeType;
import mygame.Main;

import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;

public class Sun extends Abs_ChunkNode {
	Geometry model;
	PointLight light;
	public Sun(Main app, SolarSystem parent){
		super(app, CBNameGenerator.getName(), ChunkNodeType.Universe);
		//this.parent = parent;
		this.init();
	}
	
	private void init(){
		Sphere shape = new Sphere(32, 32, (float) (Math.random()*2.5f+6f));
		model = new Geometry(this.name+"_model", shape);
		Material sphereMat = new Material(app.getAssetManager(), 
				"Common/MatDefs/Misc/Unshaded.j3md");
		sphereMat.setColor("Color", ColorRGBA.Yellow);
		model.setMaterial(sphereMat);
		this.attachChild(model);
		light = new PointLight();
		light.setColor(ColorRGBA.White);
		light.setRadius(shape.radius*100);
		light.setPosition(this.getWorldTranslation());
		app.getRootNode().addLight(light);
	}
	
	public void update(float tpf){
		light.setPosition(this.getWorldTranslation().add(new Vector3f(0,20,0)));
	}
}
