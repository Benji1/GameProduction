package universe;

import netclient.GameProductionClient;
import mygame.Main;

public class UniverseGenerator {
	private static int[] size = {3,3};
	private static int density = 20;
	
	
	public static void debugSystem(GameProductionClient app, Universe u){
		SolarSystem sys = new SolarSystem(app);
		app.gameRunState.localRootNode.attachChild(sys);
		sys.setLocalTranslation(-100, -5, 0);
		u.systems.add(sys);
	}
	
	public static void generateUniverse(GameProductionClient app, Universe u){
		int area = (int) (size[0]*size[1]*Universe.CHUNK_SIZE*Universe.CHUNK_SIZE);
		int numSystems = (int) ((area*density*(Math.random()+1))/1000000.0);
		for (int i = 0; i < numSystems; i++){
			SolarSystem sys = new SolarSystem(app);
			app.gameRunState.localRootNode.attachChild(sys);
			sys.setLocalTranslation((float)(Universe.CHUNK_SIZE*size[0]*(Math.random()-0.5)), 0f, (float)(Universe.CHUNK_SIZE*size[1]*(Math.random()-0.5)));
			u.systems.add(sys);
		}
	}
}
