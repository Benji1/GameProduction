/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import org.dyn4j.dynamics.World;

/**
 *
 * @author Chris
 */
public class PhysicsWorld {
    //public static PhysicsWorld instance = new PhysicsWorld();
    public static World world = new World();
    
    private PhysicsWorld() {}
    
    /*
    public static PhysicsWorld getInstance()
    {
       return instance;
    }//*/
}
