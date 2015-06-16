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
import static netclient.gui.ModuleType.ARMOR;
import static netclient.gui.ModuleType.ARMOR_DIAGONAL;
import static netclient.gui.ModuleType.COCKPIT;
import static netclient.gui.ModuleType.ENERGY_GENERATOR;
import static netclient.gui.ModuleType.SHIELD;
import static netclient.gui.ModuleType.STORAGE;
import static netclient.gui.ModuleType.THRUSTER;
import static netclient.gui.ModuleType.WEAPON;
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
    protected Node nodeToAttach;
    protected boolean active;
    protected String modelPath = "3dmodels/armor.obj";
    protected String texturePath = "3dmodels/armor_ao.png";

    public GraphicalModule(OrientedModule orientedModule, Node nodeToAttach, float x, float y, WJSFClient app) {
        this.app = app;
        this.nodeToAttach = nodeToAttach;
        this.orientedModule = orientedModule;
        
        nodeToAttach.attachChild(this);
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
    
    public GraphicalModule createOrientedModuleGraphics(OrientedModule om, Node nodeToAttach, float x, float y) {
        switch (om.moduleType) {
            case ARMOR:
                return new GMArmor(om, nodeToAttach, x, y, app);
            case ARMOR_DIAGONAL:
                return new GMArmorDiagonal(om, nodeToAttach, x, y, app);
            case COCKPIT:
                return new GMCockpit(om, nodeToAttach, x, y, app);
            case ENERGY_GENERATOR:
                return new GMEnergyGenerator(om, nodeToAttach, x, y, app);
            case SHIELD:
                return new GMShieldGenerator(om, nodeToAttach, x, y, app);
            case STORAGE:
                return new GMStorage(om, nodeToAttach, x, y, app);
            case THRUSTER:
                return new GMThruster(om, nodeToAttach, x, y, app);
            case WEAPON:
                return new GMLaserGun(om, nodeToAttach, x, y, app);
            default:
                return new GMArmor(om, nodeToAttach, x, y, app);
        }
    }
}
