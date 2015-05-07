/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;


/**
 *
 * @author Chris
 */
public class PhysicsWorld {
    //public static PhysicsWorld instance = new PhysicsWorld();
    public static World world = new World(new Vec2(0.0f, 0.0f), true);

    
    private PhysicsWorld() {
    	
    }
    
    /*
    public static PhysicsWorld getInstance()
    {
       return instance;
    }//*/
}
