/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netserver.services.updater;

import java.util.ArrayList;
import java.util.Stack;

import netserver.services.Service;

public class UpdateableManager extends Service {
    
    private ArrayList<IUpdateable> updateables = new ArrayList<IUpdateable>();
    private Stack<IUpdateable> toRemove = new Stack<IUpdateable>();
    
    private ArrayList<INetworkPosAndRotUpdateable> networkUpdateables = new ArrayList<INetworkPosAndRotUpdateable>();
    private Stack<INetworkPosAndRotUpdateable> netUpdateablesToRemove = new Stack<INetworkPosAndRotUpdateable>();
    
    public void addUpdateable(IUpdateable u) {
        updateables.add(u);
    }
    public void addNetworkUpdateable(INetworkPosAndRotUpdateable u) {
        networkUpdateables.add(u);
    }
    
    public void removeUpdateable(IUpdateable u) {
        toRemove.push(u);
    }
    public void removeNetworkUpdateable(INetworkPosAndRotUpdateable u) {
        netUpdateablesToRemove.push(u);
    }
    
    public void update(float delta) {
        for (int i = 0; i < updateables.size(); i++) {
            if (updateables.get(i) != null) {
                updateables.get(i).update(delta);
            }
        }
        
        removeUpdateables();
    }
    
    private void removeUpdateables() {
        while (!toRemove.empty()) {
            updateables.remove(toRemove.pop());
        }
        while (!netUpdateablesToRemove.empty()) {
            networkUpdateables.remove(netUpdateablesToRemove.pop());
        }
    }
    
    public ArrayList<INetworkPosAndRotUpdateable> getNetorkUpdateables() {
        return networkUpdateables;
    }
    
}
