package universe;

import mygame.Main;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
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
    private Main app;
    
    public Background(Main app){
    	this.app = app;
    }
    
    public void initBackground(){
    	Quad bg = new Quad(300, 300);
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
		background = new Geometry("background", bg);
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
		//app.camNode.attachChild(bgm);
		app.getRootNode().attachChild(background);
		app.getRootNode().attachChild(parallaxTopRight);
		app.getRootNode().attachChild(parallaxBottomRight);
		app.getRootNode().attachChild(parallaxBottomLeft);
		app.getRootNode().attachChild(parallaxTopLeft);
		app.getRootNode().attachChild(parallaxTopRight2);
		app.getRootNode().attachChild(parallaxBottomRight2);
		app.getRootNode().attachChild(parallaxBottomLeft2);
		app.getRootNode().attachChild(parallaxTopLeft2);
		background.rotate((float) Math.PI *1.5f, 0, 0);
		parallaxTopRight.rotate((float) Math.PI *1.5f, 0, 0);
		parallaxTopLeft.rotate((float) Math.PI *1.5f, 0, 0);
		parallaxBottomRight.rotate((float) Math.PI *1.5f, 0, 0);
		parallaxBottomLeft.rotate((float) Math.PI *1.5f, 0, 0);
		parallaxTopRight2.rotate((float) Math.PI *1.5f, 0, 0);
		parallaxTopLeft2.rotate((float) Math.PI *1.5f, 0, 0);
		parallaxBottomRight2.rotate((float) Math.PI *1.5f, 0, 0);
		parallaxBottomLeft2.rotate((float) Math.PI *1.5f, 0, 0);
		background.setLocalTranslation(-150,-50,150);
		parallaxTopRight.setLocalTranslation(-150,-49,150);
    }
    
    public void updateBackground(){
    	float scrolling = 10f;
    	float scrollingf = 20f;
    	background.setLocalTranslation(app.camNode.getWorldTranslation().x-150, -50, app.camNode.getWorldTranslation().z+150);
    	parallaxTopRight.setLocalTranslation(   app.camNode.getWorldTranslation().x - ((app.camNode.getWorldTranslation().x/scrolling)%600+600)%600 +150, -48, app.camNode.getWorldTranslation().z - (((app.camNode.getWorldTranslation().z/scrolling)+300)%600+600)%600+450);
    	parallaxTopLeft.setLocalTranslation(    app.camNode.getWorldTranslation().x - ((app.camNode.getWorldTranslation().x/scrolling-300)%600+600)%600 +150, -48, app.camNode.getWorldTranslation().z - (((app.camNode.getWorldTranslation().z/scrolling)+300)%600+600)%600+450);
    	parallaxBottomRight.setLocalTranslation(app.camNode.getWorldTranslation().x - ((app.camNode.getWorldTranslation().x/scrolling)%600+600)%600 +150, -48, app.camNode.getWorldTranslation().z - (((app.camNode.getWorldTranslation().z/scrolling))%600+600)%600+450);
    	parallaxBottomLeft.setLocalTranslation( app.camNode.getWorldTranslation().x - ((app.camNode.getWorldTranslation().x/scrolling-300)%600+600)%600 +150, -48, app.camNode.getWorldTranslation().z - (((app.camNode.getWorldTranslation().z/scrolling))%600+600)%600+450);
    	parallaxTopRight2.setLocalTranslation(app.camNode.getWorldTranslation().x - ((app.camNode.getWorldTranslation().x/scrollingf)%600+600)%600 +150, -49, app.camNode.getWorldTranslation().z - (((app.camNode.getWorldTranslation().z/scrollingf)+300)%600+600)%600+450);
    	parallaxTopLeft2.setLocalTranslation(app.camNode.getWorldTranslation().x - ((app.camNode.getWorldTranslation().x/scrollingf-300)%600+600)%600 +150, -49, app.camNode.getWorldTranslation().z - (((app.camNode.getWorldTranslation().z/scrollingf)+300)%600+600)%600+450);
    	parallaxBottomRight2.setLocalTranslation(app.camNode.getWorldTranslation().x - ((app.camNode.getWorldTranslation().x/scrollingf)%600+600)%600 +150, -49, app.camNode.getWorldTranslation().z - (((app.camNode.getWorldTranslation().z/scrollingf))%600+600)%600+450);
    	parallaxBottomLeft2.setLocalTranslation(app.camNode.getWorldTranslation().x - ((app.camNode.getWorldTranslation().x/scrollingf-300)%600+600)%600 +150, -49, app.camNode.getWorldTranslation().z -  (((app.camNode.getWorldTranslation().z/scrollingf))%600+600)%600+450);
   
    }
}