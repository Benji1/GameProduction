package netserver.universe;

import com.jme3.math.Vector3f;

import netserver.WJSFServer;

public class UniverseGenerator {
	private static int[] size = {3,3};
	private static int density = 20;
	
	
	public static void debugSystem(WJSFServer app, Universe u){
		SolarSystem sys = new SolarSystem(app, new Vector3f(10050, Universe.Y_LAYER_UNIVERSE, -50));
		SpaceStation s1 = new SpaceStation(app, new Vector3f(20, Universe.Y_LAYER_STATIONS, 20));
	}
	
	public static void generateUniverse(WJSFServer app, Universe u){
		int area = (int) (size[0]*size[1]*Universe.CHUNK_SIZE*Universe.CHUNK_SIZE);
		int numSystems = (int) ((area*density*(Math.random()+1))/1000000.0);
		for (int i = 0; i < numSystems; i++){
			SolarSystem sys = new SolarSystem(app, new Vector3f((float)(Universe.CHUNK_SIZE*size[0]*(Math.random()-0.5)), -10f, (float)(Universe.CHUNK_SIZE*size[1]*(Math.random()-0.5))));
			app.getRootNode().attachChild(sys);
			//sys.setLocalTranslation((float)(Universe.CHUNK_SIZE*size[0]*(Math.random()-0.5)), 0f, (float)(Universe.CHUNK_SIZE*size[1]*(Math.random()-0.5)));
			//u.systems.add(sys);
		}
	}
}
