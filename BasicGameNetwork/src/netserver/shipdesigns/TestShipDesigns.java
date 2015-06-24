/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netserver.shipdesigns;

import com.jme3.input.KeyInput;
import java.awt.Point;
import java.util.ArrayList;
import netserver.BasicShip;
import netserver.WJSFServer;
import netserver.NetPlayer;
import netserver.modules.Armor;
import netserver.modules.Cockpit;
import netserver.modules.EnergyGenerator;
import netserver.modules.FacingDirection;
import netserver.modules.LaserGun;
import netserver.modules.Shield;
import netserver.modules.Storage;
import netserver.modules.Thruster;
import netserver.modules.WeakThruster;

/**
 *
 * @author 1337
 */
public class TestShipDesigns {
	WJSFServer main;
    
    ArrayList<Integer> fwd;
    ArrayList<Integer> fwdAndLeft;
    ArrayList<Integer> fwdAndRight;
    ArrayList<Integer> fwdAndLeftAndRight;
    
    ArrayList<Integer> right;
    ArrayList<Integer> left;
    
    ArrayList<Integer> bckwd;
    ArrayList<Integer> bckwdAndLeft;
    ArrayList<Integer> bckwdAndRight;
    ArrayList<Integer> bckwdAndLeftAndRight;
    
    ArrayList<Integer> shield;
    ArrayList<Integer> weapon;
    
    ArrayList<Integer> testFire;
    
    public static final int CATEGORY_SHIP = 0x0001;
    public static final int CATEGORY_SCENERY = 0x0004;
    public static final int CATEGORY_PROJECTILE = 0x0008;
    public static final int CATEGORY_SHIELD = 0x0010;
    public static final int CATEGORY_ITEM = 0x0020;
    
    public static final int MASK_SHIP = CATEGORY_SHIP | CATEGORY_SCENERY | CATEGORY_PROJECTILE | CATEGORY_ITEM;
    public static final int MASK_SCENERY = -1;
    public static final int MASK_PROJECTILE = CATEGORY_SHIP | CATEGORY_SHIELD;
    public static final int MASK_SHIELD = CATEGORY_PROJECTILE;
    public static final int MASK_ITEM = CATEGORY_SHIP;
    
    public TestShipDesigns(WJSFServer main) {
        this.main = main;
        
        fwd = new ArrayList<Integer>();
        fwd.add(KeyInput.KEY_UP);
        fwd.add(KeyInput.KEY_W);
        
        fwdAndLeft = new ArrayList<Integer>();
        fwdAndLeft.add(KeyInput.KEY_UP);
        fwdAndLeft.add(KeyInput.KEY_W);
        fwdAndLeft.add(KeyInput.KEY_LEFT);
        fwdAndLeft.add(KeyInput.KEY_A);
        
        fwdAndRight = new ArrayList<Integer>();
        fwdAndRight.add(KeyInput.KEY_UP);
        fwdAndRight.add(KeyInput.KEY_W);
        fwdAndRight.add(KeyInput.KEY_RIGHT);
        fwdAndRight.add(KeyInput.KEY_D);
        
        fwdAndLeftAndRight = new ArrayList<Integer>();
        fwdAndLeftAndRight.add(KeyInput.KEY_UP);
        fwdAndLeftAndRight.add(KeyInput.KEY_W);
        fwdAndLeftAndRight.add(KeyInput.KEY_LEFT);
        fwdAndLeftAndRight.add(KeyInput.KEY_A);
        fwdAndLeftAndRight.add(KeyInput.KEY_RIGHT);
        fwdAndLeftAndRight.add(KeyInput.KEY_D);
        
        right = new ArrayList<Integer>();
        right.add(KeyInput.KEY_RIGHT);
        right.add(KeyInput.KEY_D);
        
        left = new ArrayList<Integer>();
        left.add(KeyInput.KEY_LEFT);
        left.add(KeyInput.KEY_A);
        
        bckwd = new ArrayList<Integer>();
        bckwd.add(KeyInput.KEY_DOWN);
        bckwd.add(KeyInput.KEY_S);
        
        bckwdAndLeft = new ArrayList<Integer>();
        bckwdAndLeft.add(KeyInput.KEY_DOWN);
        bckwdAndLeft.add(KeyInput.KEY_S);
        bckwdAndLeft.add(KeyInput.KEY_LEFT);
        bckwdAndLeft.add(KeyInput.KEY_A);
        
        bckwdAndRight = new ArrayList<Integer>();
        bckwdAndRight.add(KeyInput.KEY_DOWN);
        bckwdAndRight.add(KeyInput.KEY_S);
        bckwdAndRight.add(KeyInput.KEY_RIGHT);
        bckwdAndRight.add(KeyInput.KEY_D);
        
        bckwdAndLeftAndRight = new ArrayList<Integer>();
        bckwdAndLeftAndRight.add(KeyInput.KEY_DOWN);
        bckwdAndLeftAndRight.add(KeyInput.KEY_S);
        bckwdAndLeftAndRight.add(KeyInput.KEY_LEFT);
        bckwdAndLeftAndRight.add(KeyInput.KEY_A);
        bckwdAndLeftAndRight.add(KeyInput.KEY_RIGHT);
        bckwdAndLeftAndRight.add(KeyInput.KEY_D);
        
        shield = new ArrayList<Integer>();
        shield.add(KeyInput.KEY_F);
        
        weapon = new ArrayList<Integer>();
        weapon.add(KeyInput.KEY_SPACE);
        
        testFire = new ArrayList<Integer>();
        testFire.add(KeyInput.KEY_Q);
    }

    public BasicShip createPlayerShip(NetPlayer player) {
        BasicShip s = new BasicShip(main, "PlayerShip", player);
        
        Cockpit cp = new Cockpit();
        s.addModuleAtFromOffset(cp, new Point(0,0));
        s.addModuleAtFromOffset(new Storage(), new Point(-1, 0));
        s.addModuleAtFromOffset(new Storage(), new Point(1, 0));
        s.addModuleAtFromOffset(new LaserGun(weapon, FacingDirection.FORWARD), new Point(0, -2)); // Backwards is strange
        s.addModuleAtFromOffset(new Thruster(fwd, FacingDirection.BACKWARD), new Point(0, 2));
        s.addModuleAtFromOffset(new Thruster(left, FacingDirection.LEFT), new Point(1, 1));
        s.addModuleAtFromOffset(new Thruster(right, FacingDirection.RIGHT), new Point(-1, 1));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(0, -1));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(0, 1));
        s.addModuleAtFromOffset(new Armor(), new Point(-1, -1));
        s.addModuleAtFromOffset(new Armor(), new Point(1, -1));
        s.addModuleAtFromOffset(new Armor(), new Point(-2, -1));
        s.addModuleAtFromOffset(new Armor(), new Point(2, -1));
        s.addModuleAtFromOffset(new Shield(shield), new Point(-2, 0));
        s.addModuleAtFromOffset(new Shield(shield), new Point(2, 0));
        s.cockpit = cp;
        
        return s;
    }
/*
    public BasicShip createTestTargetShip() {
        BasicShip s = new BasicShip(main, "TargetShip", );
        
        s.addModuleAtFromOffset(new Cockpit(), new Point(0, -9));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(0, -8));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(0, -7));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(1, -9));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(1, -8));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(1, -7));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(-1, -9));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(-1, -8));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(-1, -7));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(-2, -9));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(-2, -8));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(-2, -7));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(-3, -9));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(-3, -8));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(-3, -7));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(2, -9));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(2, -8));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(2, -7));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(3, -9));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(3, -8));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(3, -7));
        
        return s;
    }
    
     public BasicShip createTestTargetShip2() {
        BasicShip s = new BasicShip(main, "TargetShip");
        
        s.addModuleAtFromOffset(new Cockpit(), new Point(0, -9));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(0, -8));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(0, -7));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(1, -9));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(1, -8));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(1, -7));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(-1, -9));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(-1, -8));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(-1, -7));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(-2, -9));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(-2, -8));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(-2, -7));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(-3, -9));
        s.addModuleAtFromOffset(new Shield(shield), new Point(-3, -8));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(-3, -7));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(2, -9));
        s.addModuleAtFromOffset(new Shield(shield), new Point(2, -8));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(2, -7));
        //s.activateModules("Shield");
        
        return s;
    }*/
    
    public BasicShip createStickShip(NetPlayer player) {
        BasicShip s = new BasicShip(main, "PlayerShip", player);
        
        s.addModuleAtFromOffset(new Cockpit(), new Point(0, 0));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(-1, 0));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(1, 0));
        s.addModuleAtFromOffset(new Armor(), new Point(-2, 0));
        s.addModuleAtFromOffset(new Armor(), new Point(2, 0));
        
        s.addModuleAtFromOffset(new Thruster(fwdAndLeftAndRight, FacingDirection.BACKWARD), new Point(0, 1));
        s.addModuleAtFromOffset(new Thruster(fwdAndRight, FacingDirection.BACKWARD), new Point(1, 1));
        s.addModuleAtFromOffset(new Thruster(fwdAndLeft, FacingDirection.BACKWARD), new Point(-1, 1));
        s.addModuleAtFromOffset(new Thruster(fwdAndRight, FacingDirection.BACKWARD), new Point(2, 1));
        s.addModuleAtFromOffset(new Thruster(fwdAndLeft, FacingDirection.BACKWARD), new Point(-2, 1));

        s.addModuleAtFromOffset(new Thruster(bckwdAndLeftAndRight, FacingDirection.FORWARD), new Point(0, -1));
        s.addModuleAtFromOffset(new Thruster(bckwdAndLeft, FacingDirection.FORWARD), new Point(1, -1));
        s.addModuleAtFromOffset(new Thruster(bckwdAndRight, FacingDirection.FORWARD), new Point(-1, -1));
        s.addModuleAtFromOffset(new Thruster(bckwdAndLeft, FacingDirection.FORWARD), new Point(2, -1));
        s.addModuleAtFromOffset(new Thruster(bckwdAndRight, FacingDirection.FORWARD), new Point(-2, -1));

        return s;
    }
    /*
    public BasicShip createBasicShip() {
        BasicShip s = new BasicShip(main, "PlayerShip");
        
        s.addModuleAtFromOffset(new Cockpit(), new Point(0, 0));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(0, 1));
        s.addModuleAtFromOffset(new Armor(), new Point(-1, 1));
        s.addModuleAtFromOffset(new Armor(), new Point(1, 1));
 //       s.addModuleAtFromOffset(new Thruster(fwdAndLeftAndRight, FacingDirection.BACKWARD), new Point(0, 1));
        s.addModuleAtFromOffset(new WeakThruster(right, FacingDirection.RIGHT), new Point(-1, 2));
        s.addModuleAtFromOffset(new WeakThruster(left, FacingDirection.LEFT), new Point(1, 2));
        s.addModuleAtFromOffset(new Thruster(fwd, FacingDirection.FORWARD), new Point(0, 2));
        s.addModuleAtFromOffset(new LaserGun(weapon, FacingDirection.FORWARD), new Point(-1, 0)); // Backwards is strange
        s.addModuleAtFromOffset(new LaserGun(weapon, FacingDirection.FORWARD), new Point(1, 0)); // Backwards is strange
        
        return s;
    }
    
    public BasicShip createBasicShip2() {
        BasicShip s = new BasicShip(main, "PlayerShip");
        
        s.addModuleAtFromOffset(new Cockpit(), new Point(0, 0));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(0, 1));
        s.addModuleAtFromOffset(new Armor(), new Point(-1, 1));
        s.addModuleAtFromOffset(new Armor(), new Point(1, 1));
 //       s.addModuleAtFromOffset(new Thruster(fwdAndLeftAndRight, FacingDirection.BACKWARD), new Point(0, 1));
        s.addModuleAtFromOffset(new Thruster(right, FacingDirection.RIGHT), new Point(-1, 2));
        s.addModuleAtFromOffset(new Thruster(left, FacingDirection.LEFT), new Point(1, 2));
        s.addModuleAtFromOffset(new Thruster(fwd, FacingDirection.FORWARD), new Point(0, 2));
        s.addModuleAtFromOffset(new LaserGun(weapon, FacingDirection.FORWARD), new Point(-1, 0)); // Backwards is strange
        s.addModuleAtFromOffset(new LaserGun(weapon, FacingDirection.FORWARD), new Point(1, 0)); // Backwards is strange
        s.addModuleAtFromOffset(new Shield(shield), new Point(2, 1));
        s.addModuleAtFromOffset(new Shield(shield), new Point(-2, 1));
        
        return s;
    }    */
}