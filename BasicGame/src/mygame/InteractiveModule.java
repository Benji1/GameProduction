/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

/**
 *
 * @author 1337
 */
public abstract class InteractiveModule extends BasicModule {

    private double energyConsumption;
    private double percentOfEnergyConsumptionReceived;

    public InteractiveModule (BasicShip ship) {
        super(ship);
    }
    
    
    // button pressed
    public void activate() {
    }

    public void deactivate() {
    }

    public double getEnergyConsumption() {
        return energyConsumption;
    }

    public double getPercentOfEnergyConsumptionReceived() {
        return percentOfEnergyConsumptionReceived;
    }

    public void receiveEnergy(double energyInPercent) {
        percentOfEnergyConsumptionReceived += energyInPercent;
    }
}
