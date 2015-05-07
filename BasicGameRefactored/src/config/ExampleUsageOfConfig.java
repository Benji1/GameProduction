/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import java.util.Map;

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
    
    private int example1 = ConfigReader.getFromMap(ConfigReader.getBaseMap("ConfigExamples"), "Example1", int.class);
    private String example2 = ConfigReader.getFromMap(ConfigReader.getBaseMap("ConfigExamples"), "Example2", String.class);
    
    
    /* Nested Config Example
     * Unfortunately you have to cast here to a Map
     */
    private String example3 = ConfigReader.getFromMap((Map) ConfigReader.getFromMap(ConfigReader.getBaseMap("ConfigExamples"), "Example3", Map.class), "Example3Secret", String.class);

    public ExampleUsageOfConfig () {
        System.out.println(example1);
        System.out.println(example2);
        System.out.println(example3);
    }
}
