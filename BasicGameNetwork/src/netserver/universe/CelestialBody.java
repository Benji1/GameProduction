package netserver.universe;

import netserver.WJSFServer;
import netutil.NetMessages;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.Message;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;

public class CelestialBody extends PhysicsBody {
	
	int ID;
	Geometry model;
	Node node;
	int texture;
	public float radius;
	ColorRGBA color;
	String name;
	private WJSFServer app;
	

	public CelestialBody(float mass, Vector3f position, WJSFServer app) {
		super(mass, position);
		this.ID = CBNameGenerator.getID();
		this.app = app;
		this.init();
	}
	
	private void init(){
		this.radius = getSize();
		this.name = CBNameGenerator.getName();
		this.node = new Node(this.name);
		Sphere shape = new Sphere(32, 32, radius);
		shape.setTextureMode(Sphere.TextureMode.Projected);
		model = new Geometry(this.name+"_model", shape);
		Material sphereMat = new Material(app.getAssetManager(), 
				"Common/MatDefs/Light/Lighting.j3md");
		sphereMat.setBoolean("UseMaterialColors", true);
		
		color = ColorRGBA.randomColor();
		sphereMat.setTexture("DiffuseMap", getTexture());
		sphereMat.setColor("Diffuse", color);
		sphereMat.setColor("Ambient", color);
		
		
		
		
		model.setMaterial(sphereMat);
		node.attachChild(model);
		app.getRootNode().attachChild(node);
		Quaternion rotation = new Quaternion();
		rotation.fromAngles(1.2f, 0, 0);
		model.setLocalRotation(rotation);
		node.setLocalTranslation(position.x, position.y, position.z);
	}
	
	private float getSize(){
		if (this.mass < Universe.PLANETMASS)
			return mass/Universe.MOONMASS * Universe.MOONSIZE;
		if (this.mass >= Universe.SUNMASS)
			return (float) Math.sqrt(this.mass / Universe.SUNMASS) * Universe.SUNSIZE;
		else {
			//this.velocity = new Vector3f(0,0,100f);
			return (float) Math.sqrt(this.mass / Universe.PLANETMASS) * Universe.PLANETSIZE;
		}
	}
	
	public void kickstart(CelestialBody ref){
		Vector3f dis = this.position.subtract(ref.position);
		Vector3f dir = new Vector3f(-dis.z,dis.y,dis.x);
		if (ref.velocity.length() != 0){
			float x = (float) Math.random();
			float y = (float) Math.random();
			dir = new Vector3f(x,y,(-dis.x *x - dis.y *y) / dis.z);
		}
		dir = dir.normalize();
		float vel = (float) Math.sqrt((Universe.G * (ref.mass + this.mass))/Math.abs(dis.length()));
		if(ref.velocity.length() == 0)
			this.velocity = (dir.mult(vel*1f));
		else
			this.velocity = (dir.mult(vel)).add(ref.velocity);
	}
	
	private Texture getTexture(){
		int type = (int)(Math.random()*2+1);
		System.out.println(mass);
		if (mass < Universe.PLANETMASS){
			this.texture = 1;
			color = ColorRGBA.LightGray;
			return app.getAssetManager().loadTexture("textures/planet_solid_1.png");		
		}
		if (mass < Universe.PLANETMASS*20){
			this.texture = type;
			return app.getAssetManager().loadTexture("textures/planet_solid_"+type+".png");
		}
		else if (mass < Universe.SUNMASS)
		{
			this.texture = type+2;
			return app.getAssetManager().loadTexture("textures/planet_gas_"+type+".png");
		}
		else
		{
			color = (ColorRGBA.White).mult(1.4f);
			this.texture = 0;
			return app.getAssetManager().loadTexture("textures/texture_sun.jpg");
		}
	}
	
	public NetMessages.SpawnUniverseEntity getSpawnMessage(){
		return new NetMessages.SpawnUniverseEntity(this.node.getWorldTranslation().x, this.node.getWorldTranslation().y, this.node.getWorldTranslation().z, radius, texture, color, this.texture == 0, ID);
	}
	
	@Override
	public void updatePosition(){
		super.updatePosition();
		this.node.setLocalTranslation(this.position.x, this.position.y, this.position.z);
		this.model.rotate(0, 0, Universe.P_DT*100f/mass);
	}

	public Message getUpdateMessage() {
		return new NetMessages.UpdateUniverseEntity(this.node.getWorldTranslation().x, this.node.getWorldTranslation().y, this.node.getWorldTranslation().z, ID);
	}
	
}
