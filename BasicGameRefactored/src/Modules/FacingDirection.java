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
    RIGHT,
    BACKWARD,
    LEFT;
    
    public static Vec2 getDirectionVector(FacingDirection fd) {
        switch (fd) {
            case FORWARD: return new Vec2(0, 1);
            case RIGHT: return new Vec2(1, 0);
            case BACKWARD: return new Vec2(0, -1);
            case LEFT: return new Vec2(-1, 0);
            default: return new Vec2(0, 1);
        }
    }
    
    private static FacingDirection[] vals = values();
    
    public FacingDirection next()
    {
        return vals[(this.ordinal()+1) % vals.length];
    }
    
    public FacingDirection previous()
    {
        return vals[(this.ordinal()+vals.length-1) % vals.length];
    }
}
