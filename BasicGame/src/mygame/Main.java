package mygame;

import Modules.Weapon;
import Modules.EnergyGenerator;
import Modules.Armor;
import Modules.Cockpit;
import Modules.Shield;
import Modules.Thruster;
import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
import java.awt.Point;


/**
 * test
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    private BasicShip s;

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        s = new BasicShip(assetManager);
        rootNode.attachChild(s);

        s.addModule(new Cockpit(), new Point(s.modules.length / 2, s.modules.length / 2));
        s.addModule(new Armor(), new Point(s.modules.length / 2, s.modules.length / 2 - 1));
        s.addModule(new Armor(), new Point(s.modules.length / 2, s.modules.length / 2 + 1));

        s.addModule(new Weapon(), new Point(s.modules.length / 2 - 2, s.modules.length / 2));
        s.addModule(new Thruster(), new Point(s.modules.length / 2 + 2, s.modules.length / 2));
        s.addModule(new Shield(), new Point(s.modules.length / 2 - 1, s.modules.length / 2));

        EnergyGenerator eg = new EnergyGenerator();
        s.addModule(eg, new Point(s.modules.length / 2 + 1, s.modules.length / 2));

        EnergyGenerator eg2 = new EnergyGenerator();
        s.addModule(eg2, new Point(s.modules.length / 2, s.modules.length / 2 + 2));

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
