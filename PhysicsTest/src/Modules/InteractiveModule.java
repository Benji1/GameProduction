/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modules;

import com.jme3.math.Vector3f;

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

    public boolean isActive() {
        return active;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (active) {
            calculateEnergyConsumption();
            onActive();
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
}
