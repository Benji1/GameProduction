/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netclient;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import static netclient.gui.ModuleType.ARMOR_DIAGONAL;
import static netclient.gui.ModuleType.COCKPIT;
import static netclient.gui.ModuleType.THRUSTER;
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
    protected Node shipRoot;
    protected boolean active;

    public GraphicalModule(OrientedModule orientedModule, Node shipRoot, float x, float y, WJSFClient app) {
        this.app = app;
        this.shipRoot = shipRoot;
        this.orientedModule = orientedModule;
        shipRoot.attachChild(this);
        createGraphicsBasedOnType(orientedModule, x, y);
    }

    private void createGraphicsBasedOnType(OrientedModule orientedModule, float x, float y) {
        switch (orientedModule.moduleType) {
            case ARMOR:
                createGraphicFromPath("3dmodels/armor.obj", "3dmodels/armor_ao.png", x, y);
                break;
            case ARMOR_DIAGONAL:
                createGraphicFromPath("3dmodels/armor_half.obj", "3dmodels/armor_half_ao.png", x, y);
                break;
            case COCKPIT:
                createGraphicFromPath("3dmodels/cockpit.obj", "3dmodels/cockpit_ao.png", x, y);
                break;
            case ENERGY_GENERATOR:
                createGraphicFromPath("3dmodels/generator.obj", "3dmodels/generator_ao.png", x, y);
                createGraphicFromPath("3dmodels/armor.obj", "3dmodels/armor_ao.png", x, y);
                break;
            case SHIELD:
                createGraphicFromPath("3dmodels/shield_generator.obj", "3dmodels/shield_generator_ao.png", x, y);
                createGraphicFromPath("3dmodels/armor.obj", "3dmodels/armor_ao.png", x, y);
                break;
            case STORAGE:
                createGraphicFromPath("3dmodels/storage.obj", "3dmodels/storage_ao.png", x, y);
                break;
            case THRUSTER:
                createGraphicFromPath("3dmodels/thruster.obj", "3dmodels/thruster_ao.png", x, y);
                break;
            case WEAPON:
                createGraphicFromPath("3dmodels/gun.obj", "3dmodels/gun_ao.png", x, y);
                break;
            default:
                break;
        }
    }

    private void createGraphicFromPath(String modelPath, String texturePath, float x, float y) {
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
        
        // * 2 because otherwise modules intersect eachother
        this.setLocalTranslation(x * 2, 0, y * 2);
        
        // TODO: Set Rotation
        
        this.attachChild(spatial);
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
}
