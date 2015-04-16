package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/**
 * test
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {

        BasicShip s = new BasicShip();

        s.print();



        s.addModule(new Armor(s), s.modules.length / 2, s.modules.length / 2);
        s.addModule(new Armor(s), s.modules.length / 2 - 1, s.modules.length / 2);

        s.addModule(new Weapon(s), s.modules.length / 2 - 2, s.modules.length / 2);

        
        EnergyGenerator eg = new EnergyGenerator(s);
        
        s.addModule(eg, s.modules.length / 2 + 1, s.modules.length / 2);



        System.out.println();
        System.out.println();
        s.print();
        
        eg.printModules();
        
        /*
         Box b = new Box(1, 1, 1);
         Geometry geom = new Geometry("Box", b);

         Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
         mat.setColor("Color", ColorRGBA.Blue);
         geom.setMaterial(mat);

         rootNode.attachChild(geom);*/
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
