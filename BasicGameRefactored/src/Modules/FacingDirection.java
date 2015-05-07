/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modules;

import org.jbox2d.common.Vec2;

/**
 *
 * @author 1337
 */
public enum FacingDirection {
    FORWARD,
    BACKWARD,
    LEFT,
    RIGHT;
    
    public static Vec2 getDirectionVector(FacingDirection fd) {
        Vec2 facingVector;
         switch(fd) {
            case FORWARD: facingVector = new Vec2(0, 1); break;
            case BACKWARD: facingVector = new Vec2(0, -1); break;
            case LEFT: facingVector = new Vec2(-1, 0); break;
            case RIGHT: facingVector = new Vec2(1, 0); break;
            default: facingVector = new Vec2(0, 1);
        }
         return facingVector;
    }
}
