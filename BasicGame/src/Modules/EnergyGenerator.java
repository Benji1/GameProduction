/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modules;

import static Modules.BasicModule.fillNotOverLimit;
import com.jme3.math.ColorRGBA;
import config.ConfigReader;
import java.awt.Point;
import java.util.ArrayList;
import mygame.BasicShip;

/**
 *
 * @author 1337
 */
public class EnergyGenerator extends BasicModule {

    private float energyGeneratedPerSecond = ConfigReader.getFromMap(ConfigReader.getBaseMap("EnergyGenerator"), "EnergyGeneratedPerSecond", float.class);
    private float energyStorageLimit = ConfigReader.getFromMap(ConfigReader.getBaseMap("EnergyGenerator"), "EnergyStorageLimit", float.class);
    private float energyStorage = energyStorageLimit;
    private int radius = ConfigReader.getFromMap(ConfigReader.getBaseMap("EnergyGenerator"), "Radius", int.class);
    ArrayList<InteractiveModule> modules = new ArrayList<InteractiveModule>();

    public EnergyGenerator() {
        moduleName = "E-Gen";
        color = ColorRGBA.Yellow;
    }

    @Override
    public void onPlaced(BasicShip ship) {
        super.onPlaced(ship);
        refreshModuleList();
    }

    // GIVE ENERGY TO PARTS
    @Override
    public void update(float delta) {
        super.update(delta);
        fillModulesWithEnergy();
    }

    public void fillModulesWithEnergy() {
        energyStorage = fillNotOverLimit(energyStorage, energyGeneratedPerSecond, energyStorageLimit);

        //System.out.println();
        // distribute energy to modules
        
        boolean refresh = false;
        for (InteractiveModule im : modules) {
            if (im != null) {
                if (energyStorage >= im.getEnergyConsumption() && im.getEnergyReceived() < im.getEnergyConsumption()) {

                    float deliverEnergy = im.getEnergyConsumption() - im.getEnergyReceived();

                    im.receiveEnergy(deliverEnergy);
                    energyStorage -= deliverEnergy;
                    //System.out.println("Energy Gen at: " + ship.getPositionInGrid(this).x + "|" + ship.getPositionInGrid(this).y + " powering: " + im.getModuleName() + " with " + deliverEnergy + " Energy. E-Remaining: " + energyStorage);
                }
            } else {
                refresh = true;
            }
        }
        
        if(refresh) {
            refreshModuleList();
        }
    }

    public void printModules() {
        //System.out.println("Energy Generator at " + ship.getPositionInGrid(this).x + "|" + ship.getPositionInGrid(this).y + " can power " + modules.size() + " modules:");
        for (InteractiveModule im : modules) {
            //System.out.println(im.getModuleName());
        }
        //System.out.println();
    }
    
    @Override
    public void otherModulePlaced(BasicModule module, Point p) {
        super.otherModulePlaced(module, p);
        if(module instanceof InteractiveModule) {
            refreshModuleList();
        }
    }

    private void refreshModuleList() {
        modules.clear();

        //Add all modules in radius
        for (int i = -radius; i < radius; i++) {
            for (int j = -radius; j < radius; j++) {
                Point positionInArray = ship.getPositionInGrid(this);
                BasicModule module = ship.getModule(new Point(positionInArray.x + i, positionInArray.y + j));

                if (module instanceof InteractiveModule) {
                    modules.add((InteractiveModule) module);
                }
            }
        }
    }
}
