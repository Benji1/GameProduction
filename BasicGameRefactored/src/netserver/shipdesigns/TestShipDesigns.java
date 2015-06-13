/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netserver.shipdesigns;

import java.awt.Point;
import java.util.ArrayList;

import netclient.WJSFClient;
import netserver.BasicShip;
import netserver.WJSFServer;
import netserver.NetInput.InputTypes;
import netserver.modules.Armor;
import netserver.modules.Cockpit;
import netserver.modules.EnergyGenerator;
import netserver.modules.FacingDirection;
import netserver.modules.LaserGun;
import netserver.modules.Shield;
import netserver.modules.Thruster;
import netserver.modules.WeakThruster;

/**
 *
 * @author 1337
 */
public class TestShipDesigns {
	WJSFServer main;
    
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
    
    ArrayList<String> testFire;
    
    public static final int CATEGORY_PLAYER = 1;
    public static final int CATEGORY_ENEMY = 2;
    public static final int CATEGORY_SCENERY = 3;
    public static final int CATEGORY_PROJECTILE = 4;
    public static final int CATEGORY_SHIELD = 5;
    public static final int CATEGORY_ITEM = 6;
    
    public static final int MASK_PLAYER = CATEGORY_ENEMY | CATEGORY_SCENERY | CATEGORY_PROJECTILE | CATEGORY_ITEM;
    public static final int MASK_ENEMY = CATEGORY_PLAYER | CATEGORY_SCENERY | CATEGORY_PROJECTILE | CATEGORY_ITEM;
    public static final int MASK_SCENERY = -1;
    public static final int MASK_PROJECTILE = CATEGORY_PLAYER | CATEGORY_SHIELD | CATEGORY_ENEMY;
    public static final int MASK_SHIELD = CATEGORY_PROJECTILE;
    public static final int MASK_ITEM = CATEGORY_PLAYER | CATEGORY_ENEMY;
    
    public TestShipDesigns(WJSFServer main) {
        this.main = main;
        
        fwd = new ArrayList<String>();
        fwd.add(InputTypes.MoveUp.toString());
        
        fwdAndLeft = new ArrayList<String>();
        fwdAndLeft.add(InputTypes.MoveUp.toString());
        fwdAndLeft.add(InputTypes.MoveLeft.toString());
        
        fwdAndRight = new ArrayList<String>();
        fwdAndRight.add(InputTypes.MoveUp.toString());
        fwdAndRight.add(InputTypes.MoveRight.toString());
        
        fwdAndLeftAndRight = new ArrayList<String>();
        fwdAndLeftAndRight.add(InputTypes.MoveUp.toString());
        fwdAndLeftAndRight.add(InputTypes.MoveLeft.toString());
        fwdAndLeftAndRight.add(InputTypes.MoveRight.toString());
        
        right = new ArrayList<String>();
        right.add(InputTypes.MoveRight.toString());
        
        left = new ArrayList<String>();
        left.add(InputTypes.MoveLeft.toString());
        
        bckwd = new ArrayList<String>();
        bckwd.add(InputTypes.MoveDown.toString());
        
        bckwdAndLeft = new ArrayList<String>();
        bckwdAndLeft.add(InputTypes.MoveDown.toString());
        bckwdAndLeft.add(InputTypes.MoveLeft.toString());
        
        bckwdAndRight = new ArrayList<String>();
        bckwdAndRight.add(InputTypes.MoveDown.toString());
        bckwdAndRight.add(InputTypes.MoveRight.toString());
        
        bckwdAndLeftAndRight = new ArrayList<String>();
        bckwdAndLeftAndRight.add(InputTypes.MoveDown.toString());
        bckwdAndLeftAndRight.add(InputTypes.MoveLeft.toString());
        bckwdAndLeftAndRight.add(InputTypes.MoveRight.toString());
        
        shield = new ArrayList<String>();
        shield.add(InputTypes.Shield.toString());
        
        weapon = new ArrayList<String>();
        weapon.add(InputTypes.Weapon.toString());
        
        testFire = new ArrayList<String>();
        testFire.add("TestFire");
    }

    public BasicShip createTestShip1() {
        BasicShip s = new BasicShip(main, "PlayerShip");
        
        s.setColliderTypeAndWith(CATEGORY_PLAYER, MASK_PLAYER);
        
        Cockpit cp = new Cockpit();
        s.addModuleAtFromOffset(cp, new Point(0,0));
        s.addModuleAtFromOffset(new Armor(), new Point(-1, 0));
        s.addModuleAtFromOffset(new Armor(), new Point(1, 0));
        s.addModuleAtFromOffset(new LaserGun(weapon, FacingDirection.BACKWARD), new Point(0, -2)); // Backwards is strange
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

    public BasicShip createTestTargetShip() {
        BasicShip s = new BasicShip(main, "TargetShip");
        
        s.setColliderTypeAndWith(CATEGORY_ENEMY, MASK_ENEMY);
        
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
        
        s.setColliderTypeAndWith(CATEGORY_ENEMY, MASK_ENEMY);
        
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
    }
    
    public BasicShip createStickShip() {
        BasicShip s = new BasicShip(main, "PlayerShip");

        s.setColliderTypeAndWith(CATEGORY_PLAYER, MASK_PLAYER);
        
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
    
    public BasicShip createBasicShip() {
        BasicShip s = new BasicShip(main, "PlayerShip");

        s.setColliderTypeAndWith(CATEGORY_PLAYER, MASK_PLAYER);
        
        s.addModuleAtFromOffset(new Cockpit(), new Point(0, 0));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(0, 1));
        s.addModuleAtFromOffset(new Armor(), new Point(-1, 1));
        s.addModuleAtFromOffset(new Armor(), new Point(1, 1));
 //       s.addModuleAtFromOffset(new Thruster(fwdAndLeftAndRight, FacingDirection.BACKWARD), new Point(0, 1));
        s.addModuleAtFromOffset(new WeakThruster(left, FacingDirection.RIGHT), new Point(-1, 2));
        s.addModuleAtFromOffset(new WeakThruster(right, FacingDirection.LEFT), new Point(1, 2));
        s.addModuleAtFromOffset(new Thruster(fwd, FacingDirection.BACKWARD), new Point(0, 2));
        s.addModuleAtFromOffset(new LaserGun(weapon, FacingDirection.BACKWARD), new Point(-1, 0)); // Backwards is strange
        s.addModuleAtFromOffset(new LaserGun(weapon, FacingDirection.BACKWARD), new Point(1, 0)); // Backwards is strange
        
        return s;
    }
    
    public BasicShip createBasicShip2() {
        BasicShip s = new BasicShip(main, "PlayerShip");

        s.setColliderTypeAndWith(CATEGORY_PLAYER, MASK_PLAYER);
        
        s.addModuleAtFromOffset(new Cockpit(), new Point(0, 0));
        s.addModuleAtFromOffset(new EnergyGenerator(), new Point(0, 1));
        s.addModuleAtFromOffset(new Armor(), new Point(-1, 1));
        s.addModuleAtFromOffset(new Armor(), new Point(1, 1));
 //       s.addModuleAtFromOffset(new Thruster(fwdAndLeftAndRight, FacingDirection.BACKWARD), new Point(0, 1));
        s.addModuleAtFromOffset(new Thruster(left, FacingDirection.RIGHT), new Point(-1, 2));
        s.addModuleAtFromOffset(new Thruster(right, FacingDirection.LEFT), new Point(1, 2));
        s.addModuleAtFromOffset(new Thruster(fwd, FacingDirection.BACKWARD), new Point(0, 2));
        s.addModuleAtFromOffset(new LaserGun(weapon, FacingDirection.BACKWARD), new Point(-1, 0)); // Backwards is strange
        s.addModuleAtFromOffset(new LaserGun(weapon, FacingDirection.BACKWARD), new Point(1, 0)); // Backwards is strange
        s.addModuleAtFromOffset(new Shield(shield), new Point(2, 1));
        s.addModuleAtFromOffset(new Shield(shield), new Point(-2, 1));
        
        return s;
    }    
}