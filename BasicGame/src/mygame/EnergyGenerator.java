/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 1337
 */
public class EnergyGenerator extends BasicModule {

    private double energyGeneratedPerSecond;
    private double energyStorage;
    private double energyStorageLimit;
    private int radius = 3;
    List<InteractiveModule> modules = new ArrayList<InteractiveModule>();

    public EnergyGenerator(BasicShip ship) {
        super(ship);
        name = "Energy Generator";
    }

    public void onPlaced(int x, int y) {
        modules.clear();

        //Add all modules in radius
        for (int i = -radius; i < radius; i++) {
            for (int j = -radius; j < radius; j++) {

                Point positionInArray = ship.getPositionInGrid(this);
                System.out.println((positionInArray.x + i) + " " + (positionInArray.y + j));
                BasicModule module = ship.getModule(positionInArray.x + i, positionInArray.y + j);

                if (module instanceof InteractiveModule) {

                    modules.add((InteractiveModule) module);
                }
            }
        }
    }

    // GIVE ENERGY TO PARTS
    public void update() {
        // fill up energy
        if (energyStorage + energyGeneratedPerSecond > energyStorageLimit) {
            energyStorage = energyStorageLimit;
        }

        // distribute energy to modules        
        for (InteractiveModule im : modules) {
            if (energyStorage >= im.getEnergyConsumption() * (1 - im.getPercentOfEnergyConsumptionReceived())) {
                im.receiveEnergy(im.getEnergyConsumption() * (1 - im.getPercentOfEnergyConsumptionReceived()));
            }
        }

    }

    public void printModules() {
        System.out.println(modules.size());
        for (InteractiveModule im : modules) {
            System.out.println(im.name);
        }

    }
}
