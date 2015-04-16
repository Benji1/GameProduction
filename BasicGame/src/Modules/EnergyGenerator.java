/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modules;

import com.jme3.math.ColorRGBA;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import mygame.BasicShip;

/**
 *
 * @author 1337
 */
public class EnergyGenerator extends BasicModule {

    private double energyGeneratedPerSecond = 20;
    private double energyStorageLimit = 1000;
    private double energyStorage = energyStorageLimit;
    private int radius = 3;
    List<InteractiveModule> modules = new ArrayList<InteractiveModule>();

    public EnergyGenerator() {
        name = "E-Gen";
        color = ColorRGBA.Yellow;
    }

    @Override
    public void onPlaced(int x, int y, BasicShip ship) {
        super.onPlaced(x, y, ship);
        refreshModuleList();
    }

    // GIVE ENERGY TO PARTS
    @Override
    public void update() {
        // fill up energy
        if (energyStorage + energyGeneratedPerSecond > energyStorageLimit) {
            energyStorage = energyStorageLimit;
        } else {
            energyStorage += energyGeneratedPerSecond;
        }

        System.out.println();
        // distribute energy to modules        
        for (InteractiveModule im : modules) {
            if (energyStorage >= im.getEnergyConsumption() && im.getEnergyReceived() < im.getEnergyConsumption()) {

                double deliverEnergy = im.getEnergyConsumption() - im.getEnergyReceived();

                im.receiveEnergy(deliverEnergy);
                energyStorage -= deliverEnergy;
                System.out.println("Energy Gen at: " + ship.getPositionInGrid(this).x + "|" + ship.getPositionInGrid(this).y + " supporting: " + im.name + " with " + deliverEnergy + " Energy. E-Remaining: " + energyStorage);

            }
        }

    }

    public void printModules() {
        System.out.println("Energy Generator powering " + modules.size() + " modules:");
        for (InteractiveModule im : modules) {
            System.out.println(im.name);
        }
        System.out.println();
    }

    private void refreshModuleList() {
        modules.clear();

        //Add all modules in radius
        for (int i = -radius; i < radius; i++) {
            for (int j = -radius; j < radius; j++) {

                Point positionInArray = ship.getPositionInGrid(this);
                BasicModule module = ship.getModule(positionInArray.x + i, positionInArray.y + j);

                if (module instanceof InteractiveModule) {
                    modules.add((InteractiveModule) module);
                }
            }
        }
    }
}
