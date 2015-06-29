package netclient.universe;

import netclient.WJSFClient;
import netclient.states.GameRunningState;

import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;

public class UniverseEntity extends Node {
	Geometry model;
	int ID;
	GameRunningState app;
	PointLight light;
	float radius;
	float lastX;
	float lastY;
	float lastZ;
	float nextX;
	float nextY;
	float nextZ;
	float interpolate = 0;
	float interpolTimer = 0f;
	
	public UniverseEntity(float x, float y, float z, float size, int texture, ColorRGBA color, boolean light, int ID, GameRunningState app){
		this.app = app;
		this.ID = ID;
		this.radius = size;
		this.setLocalTranslation(x, -50, y);
		this.lastX = x;
		this.lastY = y;
		this.lastZ = z;
		Sphere shape = new Sphere(32, 32, size);
		shape.setTextureMode(Sphere.TextureMode.Projected);
		model = new Geometry(this.ID+"_model", shape);
		Material sphereMat = new Material(app.app.getAssetManager(), 
				"Common/MatDefs/Light/Lighting.j3md");
		sphereMat.setBoolean("UseMaterialColors", true);
		
		sphereMat.setColor("Diffuse", color);
		sphereMat.setColor("Ambient", color);
		
		
		sphereMat.setTexture("DiffuseMap", getTexture(texture));
		
		model.setMaterial(sphereMat);
        Quaternion rotation = new Quaternion();
		rotation.fromAngles(1.57f, 0, 0);
		this.setLocalRotation(rotation);
		this.attachChild(model);
		this.app.localRootNode.attachChild(this);
		if (light){
			this.light = new PointLight();
			this.light.setColor((new ColorRGBA(1f, 0.9f,0.6f,0f)).mult(2f));
			this.light.setRadius(size*100);
			this.light.setPosition(new Vector3f(this.getWorldTranslation().x, 55, this.getWorldTranslation().z));
			this.app.localRootNode.addLight(this.light);
		}
		

	}
	
	public void update(float tpf){
		this.model.rotate(0, 0, tpf/this.radius+tpf*0.1f);
		this.interpolTimer += tpf;
		this.setLocalTranslation(lastX +nextX*(interpolTimer/interpolate), lastY +nextY*(interpolTimer/interpolate), lastZ +nextZ*(interpolTimer/interpolate));
	}
	
	public void updatePosition(float x, float y, float z){
		this.lastX += nextX;
		this.lastY += nextY;
		this.lastZ += nextZ;
		this.nextZ = z - lastZ;
		this.nextX = x - lastX;
		this.nextY = y - lastY;
		this.interpolate = interpolTimer;
		this.interpolTimer = 0f;
	}
	
	private Texture getTexture(int i){
		switch (i){
		case 0:
			return app.app.getAssetManager().loadTexture("textures/texture_sun.jpg");
		case 1:
			return app.app.getAssetManager().loadTexture("textures/planet_solid_1.png");
		case 2:
			return app.app.getAssetManager().loadTexture("textures/planet_solid_2.png");
		case 3:
			return app.app.getAssetManager().loadTexture("textures/planet_gas_1.png");
		case 4:
		default:
			return app.app.getAssetManager().loadTexture("textures/planet_gas_2.png");
		}
	}
}
