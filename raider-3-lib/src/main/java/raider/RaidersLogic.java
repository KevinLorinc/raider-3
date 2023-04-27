package raider;

import java.util.HashMap;
import java.util.LinkedList;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.Spawnpoint;
import de.gurkenlabs.litiengine.graphics.Camera;
import de.gurkenlabs.litiengine.graphics.PositionLockCamera;
import entities.Minion;
import entities.Player;

/**
 * a class that handles the logic for the Raiders game
 * @author Kevin Lorinc
 */
public final class RaidersLogic{
	/**
	 * creates various constants for the state in a game
	 * @author Kevin Lorinc
	 */
	public enum GameState {
		INGAME,
		MENU,
		INGAME_MENU,
		INVENTORY
	}
	
	private static GameState state;
	
	private static final HashMap<String, LinkedList<EnemySpawnEvent>> spawnEvents = new HashMap<String, LinkedList<EnemySpawnEvent>>();
	
	
	/**
	 * empty constructor
	 */
	private RaidersLogic() {};
	
	static {
		spawnEvents.put("tutorial.tmx", new LinkedList<EnemySpawnEvent>());
		for(int i =0;i<10;i++) {
			String spawnPoint = "enemy" + (i+1);
			spawnEvents.get("tutorial.tmx").add(new EnemySpawnEvent(spawnPoint));
		}
		
		//Here we will also add spawns for the boss map
	}
	
	/**
	 * initializes the logic for the Raiders game
	 */
	public static void init() {	 
	    Game.world().onLoaded(e -> {
	    	Camera camera = new PositionLockCamera(Player.instance());
   		 	camera.setClampToMap(true);
   		 	Game.world().setCamera(camera);
	    	setState(GameState.INGAME);
	        Player.instance().getHitPoints().setToMax();
	        Player.instance().setIndestructible(false);
	        Player.instance().setCollision(true);
	        
	        // spawn the player instance on the spawn point with the name "enter"
	        Spawnpoint enter =  e.getSpawnpoint("enter");
	        if (enter != null) {
	          enter.spawn(Player.instance());
	        }
	      });
	    
	    Game.loop().attach(RaidersLogic::update);
	}
	
	/**
	 * gets the state of the game
	 * @return the game state
	 */
	public static GameState getState() {
	  return state;
	}
	
	/**
	 * allows you to change the state of the game and applies the logic behind it like opening the inventory or menu
	 * @param state the state you wish to change to
	 */
	public static void setState(GameState state) {
		RaidersLogic.state = state;
	}
	
	//may not be needed I'll see how things unfold
	private static void update() {
	    if (Game.world().environment() == null || !Game.screens().current().getName().equals("GAME")) {
	      return;
	    }

	    handleEnemySpawns();
	}
	
	private static void handleEnemySpawns() {
	    if (Game.world().environment() == null) {
	      return;
	    }

	    if (!spawnEvents.containsKey(Game.world().environment().getMap().getName())) {
	      return;
	    }

	    for (EnemySpawnEvent event : spawnEvents.get(Game.world().environment().getMap().getName())) {
	      if (event.finished) {
	        continue;
	      }

	    spawnEnemy(event);//finish
	  }
	}
	
	private static void spawnEnemy(EnemySpawnEvent event) {//will have to change once we add more
	    event.finished = true;

	    Spawnpoint spawn = Game.world().environment().getSpawnpoint(event.spawnPoint);
	    if (spawn == null) {
	      System.out.println("Spawn " + event.spawnPoint + " could not be found on map " + Game.world().environment().getMap().getName());
	      return;
	    }

	    spawn.spawn(new Minion(spawn));
	}
	
	
	/**
	 * creates a class that will help with handling spawns
	 * @author Kevin Lorinc
	 */
	private static class EnemySpawnEvent{
		private final String spawnPoint;
		private boolean finished;
		
		/**
		 * creates the spawn event
		 * @param spawnPoint the name of the spawnpoint associated with the event
		 */
		public EnemySpawnEvent(String spawnPoint) {
		  this.spawnPoint = spawnPoint;
		}
	}
}
