/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modules;

import com.jme3.math.ColorRGBA;
import java.awt.Point;
import java.util.ArrayList;
import mygame.BasicShip;

/**
 *
 * @author 1337
 */
public class EnergyGenerator extends BasicModule {

    private float energyGeneratedPerSecond = 20;
    private float energyStorageLimit = 1000;
    private float energyStorage = energyStorageLimit;
    private int radius = 3;
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
    public void update() {
        energyStorage = fillNotOverLimit(energyStorage, energyGeneratedPerSecond, energyStorageLimit);

        System.out.println();
        // distribute energy to modules        
        for (InteractiveModule im : modules) {
            if (energyStorage >= im.getEnergyConsumption() && im.getEnergyReceived() < im.getEnergyConsumption()) {

                float deliverEnergy = im.getEnergyConsumption() - im.getEnergyReceived();

                im.receiveEnergy(deliverEnergy);
                energyStorage -= deliverEnergy;
                System.out.println("Energy Gen at: " + ship.getPositionInGrid(this).x + "|" + ship.getPositionInGrid(this).y + " powering: " + im.getModuleName() + " with " + deliverEnergy + " Energy. E-Remaining: " + energyStorage);
            }
        }
    }

    public void printModules() {
        System.out.println("Energy Generator at " + ship.getPositionInGrid(this).x + "|" + ship.getPositionInGrid(this).y + " can power " + modules.size() + " modules:");
        for (InteractiveModule im : modules) {
            System.out.println(im.getModuleName());
        }
        System.out.println();
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
