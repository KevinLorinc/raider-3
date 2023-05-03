package raider; 

import de.gurkenlabs.litiengine.*;
import de.gurkenlabs.litiengine.entities.Spawnpoint;
import de.gurkenlabs.litiengine.resources.Resources;
import ui.InGameScreen;
import ui.MenuScreen;

/**
 * runs the game Raiders
 * @author Kevin Lorinc
 *
 */
public class Main {
	/**
	 * the main program that executes raiders
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		Game.info().setName("Raider");
		Game.info().setVersion("v1.0.2");
			
		Game.init(args);
		
		RaidersLogic.init();
		PlayerInput.init();
		
		
		Resources.load("gamev4.litidata");
		Game.screens().add(new MenuScreen());
		Game.screens().add(new InGameScreen());
		
		//Game.world().loadEnvironment("tutorial.tmx");
		 
		Game.start();
	}

}