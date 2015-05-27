/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modules;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import mygame.BasicShip;

/**
 *
 * @author 1337
 */
public abstract class InteractiveModule extends BasicModule {

    protected float energyConsumptionPerSecond = 0;
    protected float energyConsumptionPerAction = 0;
    protected float energyReceived = 0;
    protected float energyConsumptionTotal = 0;
    protected float energyAvailableInPercent = 0;
    protected boolean active = false;
    protected boolean disabled = false;
    protected ColorRGBA colorActive;
    protected Material materialActive;

    public InteractiveModule() {
 
    }

    // button pressed
    public void activate() {
        if (!disabled) {
            active = true;
            spatial.setMaterial(materialActive);
        }        
    }

    public void deactivate() {
        active = false;
        spatial.setMaterial(material);
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (active) {
            calculateEnergyConsumption();
            onActive();
        }  else {
            this.body.setLinearDamping(0.8f);
        }
    }

    private void calculateEnergyConsumption() {
        energyConsumptionTotal = energyConsumptionPerSecond + energyConsumptionPerAction;

        if (energyConsumptionTotal > 0) {
            energyAvailableInPercent = (energyReceived / energyConsumptionTotal) * 100;
            //System.out.println(this.moduleName + " receiving " + energyAvailableInPercent + "% of needed energy");
        }
        energyReceived = 0;
    }
    
    @Override
    public void onPlaced(BasicShip ship) {
        super.onPlaced(ship);
        materialActive = new Material(ship.assetManager, "Common/MatDefs/Light/Lighting.j3md");
        materialActive.setBoolean("UseMaterialColors",true);
        materialActive.setColor("Ambient", colorActive);
        materialActive.setColor("Diffuse", colorActive);
    }

    protected abstract void onActive();

    public float getEnergyConsumption() {
        return energyConsumptionTotal;
    }

    public float getEnergyReceived() {
        return energyReceived;
    }

    public void receiveEnergy(float energyReceived) {
        this.energyReceived += energyReceived;
    }
    
    public void disable() {
        disabled = true;
        deactivate();
    }
}
