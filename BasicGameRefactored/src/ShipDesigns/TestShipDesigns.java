/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ShipDesigns;

import Modules.Armor;
import Modules.Cockpit;
import Modules.EnergyGenerator;
import Modules.FacingDirection;
import Modules.LaserGun;
import Modules.Shield;
import Modules.Storage;
import Modules.Thruster;
import java.awt.Point;
import java.util.ArrayList;
import mygame.BasicShip;
import mygame.Main;

/**
 *
 * @author 1337
 */
public class TestShipDesigns {
    Main main;
    
    ArrayList<String> fwd;
    ArrayList<String> fwdAndLeft;
    ArrayList<String> fwdAndRight;
    ArrayList<String> fwdAndLeftAndRight;
    
    ArrayList<String> right;
    ArrayList<String> left;
    
    ArrayList<String> bckwd;
    ArrayList<String> bckwdAndLeft;
    ArrayList<String> bckwdAndRight;
    ArrayList<String> bckwdAndLeftAndRight;
    
    ArrayList<String> shield;
    ArrayList<String> weapon;
    
    public int CATEGORY_PLAYER = 1;
    public int CATEGORY_ENEMY = 2;
    public int CATEGORY_SCENERY = 3;
    
    public int MASK_PLAYER = CATEGORY_ENEMY | CATEGORY_SCENERY;
    public int MASK_ENEMY = CATEGORY_PLAYER | CATEGORY_SCENERY;
    public int MASK_SCENERY = -1;
    
    public TestShipDesigns(Main main) {
        this.main = main;
        
        fwd = new ArrayList<String>();
        fwd.add("Up");
        
        fwdAndLeft = new ArrayList<String>();
        fwdAndLeft.add("Up");
        fwdAndLeft.add("Left");
        
        fwdAndRight = new ArrayList<String>();
        fwdAndRight.add("Up");
        fwdAndRight.add("Right");
        
        fwdAndLeftAndRight = new ArrayList<String>();
        fwdAndLeftAndRight.add("Up");
        fwdAndLeftAndRight.add("Left");
        fwdAndLeftAndRight.add("Right");
        
        right = new ArrayList<String>();
        right.add("Right");
        
        left = new ArrayList<String>();
        left.add("Left");
        
        bckwd = new ArrayList<String>();
        bckwd.add("Down");
        
        bckwdAndLeft = new ArrayList<String>();
        bckwdAndLeft.add("Down");
        bckwdAndLeft.add("Left");
        
        bckwdAndRight = new ArrayList<String>();
        bckwdAndRight.add("Down");
        bckwdAndRight.add("Right");
        
        bckwdAndLeftAndRight = new ArrayList<String>();
        bckwdAndLeftAndRight.add("Down");
        bckwdAndLeftAndRight.add("Left");
        bckwdAndLeftAndRight.add("Right");
        
        shield = new ArrayList<String>();
        shield.add("Shield");
        
        weapon = new ArrayList<String>();
        weapon.add("Weapon");
    }

    public BasicShip createTestShip1() {
        BasicShip s = new BasicShip(main);
        
        s.setColliderTypeAndWith(CATEGORY_PLAYER, MASK_PLAYER);
        
        s.addModuleAtFromOffset(new Cockpit(), new Point(0, 0));
        s.addModuleAtFromOffset(new Armor(), new Point(-1, 0));
        s.addModuleAtFromOffset(new Armor(), new Point(1, 0));
        s.addModuleAtFromOffset(new LaserGun(weapon, FacingDirection.BACKWARD), new Point(0, 2)); // Backwards is strange
        s.addModuleAtFromOffset(new Thruster(fwd, FacingDirection.BACKWARD), new Point(0, -2));
        s.addModuleAtFromOffset(new Thruster(right, FacingDirection.LEFT), new Point(1, -1));
        s.addModuleAtFromOffset(new Thruster(left, FacingDirection.RIGHT), new Point(-1, -1));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(0, 1));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(0, -1));
        s.addModuleAtFromOffset(new Shield(shield), new Point(-1, 1));
        s.addModuleAtFromOffset(new Storage(), new Point(1, 1));
        
        return s;
    }

    public BasicShip createTestTargetShip() {
        BasicShip s = new BasicShip(main);
        
        s.setColliderTypeAndWith(CATEGORY_ENEMY, MASK_ENEMY);
        
        s.addModuleAtFromOffset(new Cockpit(), new Point(0, 9));
        s.addModuleAtFromOffset(new Armor(), new Point(0, 8));
        s.addModuleAtFromOffset(new Armor(), new Point(0, 7));
        s.addModuleAtFromOffset(new Armor(), new Point(1, 9));
        s.addModuleAtFromOffset(new Armor(), new Point(1, 8));
        s.addModuleAtFromOffset(new Armor(), new Point(1, 7));
        s.addModuleAtFromOffset(new Armor(), new Point(-1, 9));
        s.addModuleAtFromOffset(new Armor(), new Point(-1, 8));
        s.addModuleAtFromOffset(new Armor(), new Point(-1, 7));
        
        return s;
    }
    
    public BasicShip createStickShip() {
        BasicShip s = new BasicShip(main);

        s.setColliderTypeAndWith(CATEGORY_PLAYER, MASK_PLAYER);
        
        s.addModuleAtFromOffset(new Cockpit(), new Point(0, 0));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(-1, 0));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(1, 0));
        s.addModuleAtFromOffset(new Armor(), new Point(-2, 0));
        s.addModuleAtFromOffset(new Armor(), new Point(2, 0));
        
        s.addModuleAtFromOffset(new Thruster(fwdAndLeftAndRight, FacingDirection.BACKWARD), new Point(0, -1));
        s.addModuleAtFromOffset(new Thruster(fwdAndLeft, FacingDirection.BACKWARD), new Point(1, -1));
        s.addModuleAtFromOffset(new Thruster(fwdAndRight, FacingDirection.BACKWARD), new Point(-1, -1));
        s.addModuleAtFromOffset(new Thruster(fwdAndLeft, FacingDirection.BACKWARD), new Point(2, -1));
        s.addModuleAtFromOffset(new Thruster(fwdAndRight, FacingDirection.BACKWARD), new Point(-2, -1));
        
        s.addModuleAtFromOffset(new Thruster(bckwdAndLeftAndRight, FacingDirection.FORWARD), new Point(0, 1));
        s.addModuleAtFromOffset(new Thruster(bckwdAndRight, FacingDirection.FORWARD), new Point(1, 1));
        s.addModuleAtFromOffset(new Thruster(bckwdAndLeft, FacingDirection.FORWARD), new Point(-1, 1));
        s.addModuleAtFromOffset(new Thruster(bckwdAndRight, FacingDirection.FORWARD), new Point(2, 1));
        s.addModuleAtFromOffset(new Thruster(bckwdAndLeft, FacingDirection.FORWARD), new Point(-2, 1));
        
        return s;
    }
}