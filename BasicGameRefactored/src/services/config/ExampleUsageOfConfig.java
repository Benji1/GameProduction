/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package services.config;

import java.util.Map;
import services.ServiceManager;

/**
 *
 * @author 1337
 */
public class ExampleUsageOfConfig {

    /* Example JSON:
     * 
     * "ConfigExamples": {
     *   "Example1": 1,
     *   "Example2": "Example",
     *   "Example3": {
     *       "Example3Secret": "Psst!"
     *   }
     * }
     * 
     * To get Example1, you have to first get the "ConfigExamples" Map
     * (everything within the {} is treated like entries in a Java Map)
     * 
     * for that, there is:
     * ConfigReader.getBaseMap("ConfigExamples")
     * which gets you the ConfigExamples Map from the overall JSON Map.
     * 
     * now you want to get the value in "Example1", to get this there is this function:
     * 
     * ConfigReader.getFromMap(fromWhichMap, whatVariableYouWant, whichTypeShouldItBe)
     * = ConfigReader.getFromMap(fromWhichMap, "Example1", int.class)
     * 
     * and the Map from which you will receive the "Example1" is the "ConfigExamples" Map, so it is:
     * 
     * ConfigReader.getFromMap(ConfigReader.getBaseMap("ConfigExamples"), "Example1", int.class);
     */
    ConfigReader cr = ServiceManager.getConfigReader();
    private int example1 = cr.getFromMap(cr.getBaseMap("ConfigExamples"), "Example1", int.class);
    private String example2 = cr.getFromMap(cr.getBaseMap("ConfigExamples"), "Example2", String.class);
    
    
    /* Nested Config Example
     * Unfortunately you have to cast here to a Map
     */
    private String example3 = cr.getFromMap((Map) cr.getFromMap(cr.getBaseMap("ConfigExamples"), "Example3", Map.class), "Example3Secret", String.class);

    public ExampleUsageOfConfig () {
        System.out.println(example1);
        System.out.println(example2);
        System.out.println(example3);
    }
}
