/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modules;

import java.util.ArrayList;
import java.util.List;
import mygame.BasicShip;
import mygame.Item;

/**
 *
 * @author 1337
 */
public class Storage extends BasicModule {

    protected ArrayList<Item> itemsInStorage = new ArrayList<Item>();

    public Storage() {
        name = "Storage";
    }
}
