/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modules;

import mygame.BasicShip;

/**
 *
 * @author 1337
 */
public abstract class InteractiveModule extends BasicModule {

    protected double energyConsumptionPerSecond = 0;
    protected double energyConsumptionPerAction = 0;
    protected double energyReceived = 0;
    protected double energyConsumptionTotal = 0;
    protected double energyAvailableInPercent = 0;
    protected boolean active = true;

    public InteractiveModule() {
    }

    // button pressed
    public void activate() {
        active = true;
    }

    public void deactivate() {
        active = false;
    }

    public void update() {
        if (active) {
            energyConsumptionTotal = energyConsumptionPerSecond + energyConsumptionPerAction;

            if (energyConsumptionTotal > 0) {
                energyAvailableInPercent = (energyReceived / energyConsumptionTotal) * 100;
                System.out.println(this.name + " receiving " + energyAvailableInPercent + "% of needed energy");
            }
            energyReceived = 0;
        }
    }

    public double getEnergyConsumption() {
        return energyConsumptionTotal;
    }

    public double getEnergyReceived() {
        return energyReceived;
    }

    public void receiveEnergy(double energyReceived) {
        this.energyReceived += energyReceived;
    }
}
