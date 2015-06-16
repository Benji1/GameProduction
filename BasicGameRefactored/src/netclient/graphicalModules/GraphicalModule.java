/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netclient.graphicalModules;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import netclient.ClientShip;
import netclient.WJSFClient;
import netclient.gui.OrientedModule;

/**
 *
 * @author 1337
 */
public class GraphicalModule extends Node{

    protected OrientedModule orientedModule;
    protected Spatial spatial;
    protected Material material;
    protected Material materialActive;
    protected ColorRGBA color = ColorRGBA.Gray;
    protected ColorRGBA colorActive = ColorRGBA.Red;
    protected WJSFClient app;
    protected ClientShip ship;
    protected Node shipRoot;
    protected boolean active;
    protected String modelPath = "3dmodels/armor.obj";
    protected String texturePath = "3dmodels/armor_ao.png";

    public GraphicalModule(OrientedModule orientedModule, Node shipRoot, ClientShip ship, float x, float y, WJSFClient app) {
        this.app = app;
        this.shipRoot = shipRoot;
        this.ship = ship;
        this.orientedModule = orientedModule;
        shipRoot.attachChild(this);
    }

    protected void createMyGraphic(float x, float y) {
        createGraphicFromPath(modelPath, texturePath, x, y);
    }

    protected final void createGraphicFromPath(String modelPath, String texturePath, float x, float y) {
        createGraphicFromPath(modelPath, texturePath, x, y, this);
    }
    
    public final void createGraphicFromPath(String modelPath, String texturePath, float x, float y, Node toAttach) {
        AssetManager a = app.getAssetManager();
        spatial = a.loadModel(modelPath);
        material = new Material(a, "Common/MatDefs/Light/Lighting.j3md");
        Texture t = a.loadTexture(texturePath);
        material.setTexture("DiffuseMap", t);
        spatial.setMaterial(material);
        
        materialActive = new Material(a, "Common/MatDefs/Light/Lighting.j3md");
        materialActive.setBoolean("UseMaterialColors", true);
        materialActive.setColor("Ambient", colorActive);
        materialActive.setColor("Diffuse", colorActive);
        materialActive.setTexture("DiffuseMap", t);
        
        // * 2 because otherwise modules intersect eachother
        toAttach.setLocalTranslation(x * 2, 0, y * 2);
        
        // TODO: Set Rotation
        
        toAttach.attachChild(spatial);
    }
    
    public void activate() {
        active = true;
        spatial.setMaterial(materialActive);
    }
    
    public void deactivate() {
        active = false;
        spatial.setMaterial(material);
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void update() {        
    }
    
    public void remove() {
        this.removeFromParent();
    }
}
