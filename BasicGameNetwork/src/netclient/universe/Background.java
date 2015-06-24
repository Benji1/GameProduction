package netclient.universe;

import netclient.WJSFClient;

import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;

public class Background {
    private Geometry background;
    private Geometry parallaxTopRight;
    private Geometry parallaxTopLeft;
    private Geometry parallaxBottomRight;
    private Geometry parallaxBottomLeft;
    private Geometry parallaxTopRight2;
    private Geometry parallaxTopLeft2;
    private Geometry parallaxBottomRight2;
    private Geometry parallaxBottomLeft2;
    private Node fixedBG;
    private Node farBG;
    private Node nearBG;
    private CameraNode cam;
    private WJSFClient app;
    private float quadsize = 450;
    private float height1 = -48;
    private float height2 = -49;
    
    public Background(WJSFClient app){
    	this.app = app;
    	this.fixedBG = new Node("bgnode1");
    	this.farBG = new Node("bgnode2");
    	this.nearBG = new Node("bgnode3");
    }
    
    public void initBackground(){
    	this.cam = app.gameRunState.camNode;
    	
    	Quad bg = new Quad(quadsize, quadsize);
    	Quad bg2 = new Quad(600, 600);
    	Material bgMat = new Material(app.getAssetManager(), 
				"Common/MatDefs/Misc/Unshaded.j3md");
		bgMat.setColor("Color", (ColorRGBA.White).mult(0.5f));
		bgMat.setTexture("ColorMap", app.getAssetManager().loadTexture("textures/background.jpg"));
    	Material pMat = new Material(app.getAssetManager(), 
				"Common/MatDefs/Misc/Unshaded.j3md");
    	pMat.setColor("Color", (ColorRGBA.White).mult(0.8f));
    	pMat.setTexture("ColorMap", app.getAssetManager().loadTexture("textures/stars.png"));
    	pMat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
    	Material pMat2 = new Material(app.getAssetManager(), 
				"Common/MatDefs/Misc/Unshaded.j3md");
    	pMat2.setColor("Color", (ColorRGBA.White).mult(0.6f));
    	pMat2.setTexture("ColorMap", app.getAssetManager().loadTexture("textures/stars_small.png"));
    	pMat2.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		background = new Geometry("background", bg2);
		parallaxTopRight = new Geometry("parallaxTop", bg);
		parallaxTopRight.setQueueBucket(Bucket.Transparent);
		parallaxTopLeft = new Geometry("parallaxBottom", bg);
		parallaxTopLeft.setQueueBucket(Bucket.Transparent);
		parallaxBottomRight = new Geometry("parallaxLeft", bg);
		parallaxBottomRight.setQueueBucket(Bucket.Transparent);
		parallaxBottomLeft = new Geometry("parallaxRight", bg);
		parallaxBottomLeft.setQueueBucket(Bucket.Transparent);
		parallaxTopRight2 = new Geometry("parallaxTop2", bg);
		parallaxTopRight2.setQueueBucket(Bucket.Transparent);
		parallaxTopLeft2 = new Geometry("parallaxBottom2", bg);
		parallaxTopLeft2.setQueueBucket(Bucket.Transparent);
		parallaxBottomRight2 = new Geometry("parallaxLeft2", bg);
		parallaxBottomRight2.setQueueBucket(Bucket.Transparent);
		parallaxBottomLeft2 = new Geometry("parallaxRight2", bg);
		parallaxBottomLeft2.setQueueBucket(Bucket.Transparent);
		
		background.setMaterial(bgMat);
		parallaxTopRight.setMaterial(pMat);
		parallaxTopLeft.setMaterial(pMat);
		parallaxBottomRight.setMaterial(pMat);
		parallaxBottomLeft.setMaterial(pMat);
		parallaxTopRight2.setMaterial(pMat2);
		parallaxTopLeft2.setMaterial(pMat2);
		parallaxBottomRight2.setMaterial(pMat2);
		parallaxBottomLeft2.setMaterial(pMat2);
		//app.gameRunState.camNode.attachChild(bgm);
		fixedBG.attachChild(background);
		farBG.attachChild(parallaxTopRight);
		farBG.attachChild(parallaxBottomRight);
		farBG.attachChild(parallaxBottomLeft);
		farBG.attachChild(parallaxTopLeft);
		nearBG.attachChild(parallaxTopRight2);
		nearBG.attachChild(parallaxBottomRight2);
		nearBG.attachChild(parallaxBottomLeft2);
		nearBG.attachChild(parallaxTopLeft2);
		this.app.getRootNode().attachChild(fixedBG);
		this.app.getRootNode().attachChild(farBG);
		this.app.getRootNode().attachChild(nearBG);
		background.rotate((float) Math.PI *1.5f, 0, 0);
		parallaxTopRight.rotate((float) Math.PI *1.5f, 0, 0);
		parallaxTopLeft.rotate((float) Math.PI *1.5f, 0, 0);
		parallaxBottomRight.rotate((float) Math.PI *1.5f, 0, 0);
		parallaxBottomLeft.rotate((float) Math.PI *1.5f, 0, 0);
		parallaxTopRight2.rotate((float) Math.PI *1.5f, 0, 0);
		parallaxTopLeft2.rotate((float) Math.PI *1.5f, 0, 0);
		parallaxBottomRight2.rotate((float) Math.PI *1.5f, 0, 0);
		parallaxBottomLeft2.rotate((float) Math.PI *1.5f, 0, 0);
		background.setLocalTranslation(-(quadsize*0.5f),-50,(quadsize*0.5f));
		parallaxTopRight.setLocalTranslation(-(quadsize*0.5f),height2,(quadsize*0.5f));
    }
    
    public void updateBackground(){
    	float scrolling = 10f;
    	float scrollingf = 20f;
    	background.setLocalTranslation(cam.getWorldTranslation().x-150, cam.getWorldTranslation().y-200, cam.getWorldTranslation().z+150);
    	nearBG.setLocalTranslation(0, cam.getWorldTranslation().y*0.9f-100, 0);
    	farBG.setLocalTranslation(0, cam.getWorldTranslation().y*0.8f-100, 0);
    	parallaxTopRight.setLocalTranslation(   cam.getWorldTranslation().x - ((cam.getWorldTranslation().x/scrolling)%(quadsize*2)+(quadsize*2))%(quadsize*2) +(quadsize*0.5f), height1, cam.getWorldTranslation().z - (((cam.getWorldTranslation().z/scrolling)+quadsize)%(quadsize*2)+(quadsize*2))%(quadsize*2)+(quadsize+quadsize*0.5f));
    	parallaxTopLeft.setLocalTranslation(    cam.getWorldTranslation().x - ((cam.getWorldTranslation().x/scrolling-quadsize)%(quadsize*2)+(quadsize*2))%(quadsize*2) +(quadsize*0.5f), height1, cam.getWorldTranslation().z - (((cam.getWorldTranslation().z/scrolling)+quadsize)%(quadsize*2)+(quadsize*2))%(quadsize*2)+(quadsize+quadsize*0.5f));
    	parallaxBottomRight.setLocalTranslation(cam.getWorldTranslation().x - ((cam.getWorldTranslation().x/scrolling)%(quadsize*2)+(quadsize*2))%(quadsize*2) +(quadsize*0.5f), height1, cam.getWorldTranslation().z - (((cam.getWorldTranslation().z/scrolling))%(quadsize*2)+(quadsize*2))%(quadsize*2)+(quadsize+quadsize*0.5f));
    	parallaxBottomLeft.setLocalTranslation( cam.getWorldTranslation().x - ((cam.getWorldTranslation().x/scrolling-quadsize)%(quadsize*2)+(quadsize*2))%(quadsize*2) +(quadsize*0.5f), height1, cam.getWorldTranslation().z - (((cam.getWorldTranslation().z/scrolling))%(quadsize*2)+(quadsize*2))%(quadsize*2)+(quadsize+quadsize*0.5f));
    	parallaxTopRight2.setLocalTranslation(cam.getWorldTranslation().x - ((cam.getWorldTranslation().x/scrollingf)%(quadsize*2)+(quadsize*2))%(quadsize*2) +(quadsize*0.5f), height2, cam.getWorldTranslation().z - (((cam.getWorldTranslation().z/scrollingf)+quadsize)%(quadsize*2)+(quadsize*2))%(quadsize*2)+(quadsize+quadsize*0.5f));
    	parallaxTopLeft2.setLocalTranslation(cam.getWorldTranslation().x - ((cam.getWorldTranslation().x/scrollingf-quadsize)%(quadsize*2)+(quadsize*2))%(quadsize*2) +(quadsize*0.5f), height2, cam.getWorldTranslation().z - (((cam.getWorldTranslation().z/scrollingf)+quadsize)%(quadsize*2)+(quadsize*2))%(quadsize*2)+(quadsize+quadsize*0.5f));
    	parallaxBottomRight2.setLocalTranslation(cam.getWorldTranslation().x - ((cam.getWorldTranslation().x/scrollingf)%(quadsize*2)+(quadsize*2))%(quadsize*2) +(quadsize*0.5f), height2, cam.getWorldTranslation().z - (((cam.getWorldTranslation().z/scrollingf))%(quadsize*2)+(quadsize*2))%(quadsize*2)+(quadsize+quadsize*0.5f));
    	parallaxBottomLeft2.setLocalTranslation(cam.getWorldTranslation().x - ((cam.getWorldTranslation().x/scrollingf-quadsize)%(quadsize*2)+(quadsize*2))%(quadsize*2) +(quadsize*0.5f), height2, cam.getWorldTranslation().z -  (((cam.getWorldTranslation().z/scrollingf))%(quadsize*2)+(quadsize*2))%(quadsize*2)+(quadsize+quadsize*0.5f));
   
    }
}