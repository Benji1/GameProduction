/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

/**
 *
 * @author 1337
 */
public interface BasicModuleInterface {
    
    public void activate();
    public void deactivate();
    
    public void takeDamage(int amount);
    public int getHealth();
    
    
}
