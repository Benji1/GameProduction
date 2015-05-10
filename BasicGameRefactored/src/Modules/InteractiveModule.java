/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modules;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import java.awt.Point;
import java.util.ArrayList;
import mygame.BasicShip;

/**
 *
 * @author 1337
 */
public abstract class InteractiveModule extends BasicModule {

    protected float energyConsumptionPerSecond = 0;
    protected float energyConsumptionPerAction = 0;
    protected boolean active = false;
    protected boolean disabled = false;
    protected ColorRGBA colorActive;
    protected Material materialActive;
    protected ArrayList<String> hotkeys;

    protected ArrayList<EnergyGenerator> eGens;
    protected float energyReceived;
    protected float energyConsumption;
    protected float energyAvailableInPercent;
    
    public InteractiveModule(ArrayList<String> hotkeys) {
        this.hotkeys = hotkeys;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (active) {
            calculateEnergyConsumption(delta);
            if (active) {
                onActive();
            }
        }
    }
    
     private void calculateEnergyConsumption(float delta) {
        energyConsumption = energyConsumptionPerSecond * delta;
        if (energyConsumption > 0) {
            if(eGens.size() > 0) {
                EnergyGenerator mostEnergy = getEnergyGeneratorWithMostEnergy();
                if(mostEnergy.getEnergy() >= energyConsumption) {
                    mostEnergy.reduceEnergy(energyConsumption);
                    energyReceived = energyConsumption;
                    
                }
            }
        }
        energyAvailableInPercent = energyReceived / energyConsumption;
        if (energyReceived < energyConsumption) {
            this.deactivate();
        }
    }

    @Override
    public void onPlaced(BasicShip ship) {
        super.onPlaced(ship);
        materialActive = new Material(ship.getApp().getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        materialActive.setBoolean("UseMaterialColors", true);
        materialActive.setColor("Ambient", colorActive);
        materialActive.setColor("Diffuse", colorActive);
        
        ship.interactiveModules.add(this);
        
        eGens = new ArrayList<EnergyGenerator>();
        addAlreadyExistingEgens();
    }
    
    @Override
    public void onMovedToOtherShip(BasicShip s) {
        super.onMovedToOtherShip(s);
        eGens = new ArrayList<EnergyGenerator>();
        addAlreadyExistingEgens();
    }
    
     @Override
    public void onRemove() {
        super.onRemove();
        ship.interactiveModules.remove(this);
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

    protected abstract void onActive();
    
    public void disable() {
        disabled = true;
        deactivate();
    }

    public ArrayList<String> getHotkeys() {
        return hotkeys;
    }
    
    protected EnergyGenerator getEnergyGeneratorWithMostEnergy() {
        EnergyGenerator mostEnergy = eGens.get(0);
        for(EnergyGenerator eg: eGens) {
            if(eg.getEnergy() > mostEnergy.getEnergy()) {
                mostEnergy = eg;
            }
        }
        return mostEnergy;
    }
    
    @Override
    public void otherModulePlaced(BasicModule module, Point p) {
        super.otherModulePlaced(module, p);
        if (module instanceof EnergyGenerator && withinRadius((EnergyGenerator) module)) {
            eGens.add((EnergyGenerator) module);
        }
    }
    
    @Override
    public void otherModuleRemoved(BasicModule module, Point p) {
        super.otherModulePlaced(module, p);
        if(module instanceof EnergyGenerator && withinRadius((EnergyGenerator) module)) {
            eGens.remove((EnergyGenerator) module);
        }
    }
    
    protected boolean withinRadius(EnergyGenerator eg) {
        Point myPos = ship.getActualPositionInGrid(this);
        Point eGenPos = ship.getActualPositionInGrid(eg);
        if(Math.abs(myPos.x - eGenPos.x) <= eg.getRadius() && Math.abs(myPos.y - eGenPos.y) <= eg.getRadius()) {
            //material.setColor("Ambient", ColorRGBA.Green);
            //material.setColor("Diffuse", ColorRGBA.Green);
            return true;
        }
        return false;
    }
    
    protected void addAlreadyExistingEgens() {
        for (int i = 0; i <= ship.modules.length; i++) {
            for (int j = 0; j <= ship.modules[0].length; j++) {
                BasicModule module = ship.getModule(new Point(i, j));
                if(module != null && module instanceof EnergyGenerator && withinRadius((EnergyGenerator) module)) {
                     eGens.add((EnergyGenerator) module);
                }
            }
        }
    }
    
    protected boolean hasEnoughEnergyForAction() {
        boolean enough = false;
        
        if (energyConsumptionPerAction > 0) {
            if(eGens.size() > 0) {
                EnergyGenerator mostEnergy = getEnergyGeneratorWithMostEnergy();
                if(mostEnergy.getEnergy() >= energyConsumptionPerAction) {
                    mostEnergy.reduceEnergy(energyConsumptionPerAction);
                    enough = true;
                }
            }
        } else {
            enough = true;
        }
        return enough;
    }
}
