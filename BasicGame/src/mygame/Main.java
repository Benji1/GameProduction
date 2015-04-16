package mygame;

import Modules.Weapon;
import Modules.EnergyGenerator;
import Modules.Armor;
import Modules.Cockpit;
import Modules.Shield;
import Modules.Thruster;
import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
 * test
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    BasicShip s;

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        s = new BasicShip(assetManager);
        rootNode.attachChild(s);

        s.addModule(new Cockpit(), s.modules.length / 2, s.modules.length / 2);
        s.addModule(new Armor(), s.modules.length / 2, s.modules.length / 2 - 1);
        s.addModule(new Armor(), s.modules.length / 2, s.modules.length / 2 + 1);

        s.addModule(new Weapon(), s.modules.length / 2 - 2, s.modules.length / 2);
        s.addModule(new Thruster(), s.modules.length / 2 + 2, s.modules.length / 2);
        s.addModule(new Shield(), s.modules.length / 2 - 1, s.modules.length / 2);

        EnergyGenerator eg = new EnergyGenerator();
        s.addModule(eg, s.modules.length / 2 + 1, s.modules.length / 2);

        EnergyGenerator eg2 = new EnergyGenerator();
        s.addModule(eg2, s.modules.length / 2, s.modules.length / 2 + 2);

        s.print();

        eg.printModules();
        eg2.printModules();
    }

    @Override
    public void simpleUpdate(float tpf) {
        s.update();
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
