package raider;

import java.util.HashMap;
import java.util.LinkedList;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.CollisionBox;
import de.gurkenlabs.litiengine.entities.Spawnpoint;
import de.gurkenlabs.litiengine.entities.behavior.AStarGrid;
import de.gurkenlabs.litiengine.entities.behavior.AStarNode;
import de.gurkenlabs.litiengine.environment.Environment;
import de.gurkenlabs.litiengine.environment.EnvironmentListener;
import de.gurkenlabs.litiengine.graphics.Camera;
import de.gurkenlabs.litiengine.graphics.PositionLockCamera;
import entities.Minion;
import entities.Player;

/**
 * a class that handles the logic for the Raiders game
 * @author Kevin Lorinc
 */
public final class RaidersLogic {
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
	
	private static GameState state = GameState.MENU;
	
	private static final HashMap<String, LinkedList<EnemySpawnEvent>> spawnEvents = new HashMap<String, LinkedList<EnemySpawnEvent>>();
	private static final HashMap<String, AStarGrid> grids = new HashMap<String, AStarGrid>();
	
	
	/**
	 * empty constructor
	 */
	private RaidersLogic() {};
	
	static {
		spawnEvents.put("tutorial", new LinkedList<EnemySpawnEvent>());
		for(int i =0;i<10;i++) {
			String spawnPoint = "enemy" + (i+1);
			spawnEvents.get("tutorial").add(new EnemySpawnEvent(spawnPoint));
		}
		
		//Here we will also add spawns for the boss map
	}
	
	/**
	 * initializes the logic for the Raiders game
	 */
	public static void init() {	 
		Game.world().addListener(new EnvironmentListener() {
			/**
			 * makes updates to what happens when the environemnt is initialized
			 */
			@Override
		    public void initialized(Environment e) {
				Camera camera = new PositionLockCamera(Player.instance());
				camera.setClampToMap(true);
				Game.world().setCamera(camera);
				Player.instance().getHitPoints().setToMax();
		        Player.instance().setIndestructible(false);
		        Player.instance().setCollision(true);

		        Game.world().camera().setFocus(e.getCenter());
		        Spawnpoint spawn = e.getSpawnpoint("enter");
		        if (spawn != null) {
		          setState(GameState.INGAME);
		          spawn.spawn(Player.instance());
		        }
		        //i have no clue what this does tbh
		        AStarGrid grid = new AStarGrid(e.getMap().getSizeInPixels(), 8);
		        for (CollisionBox collisionBox : e.getEntities(CollisionBox.class)) {
		          for (AStarNode node : grid.getIntersectedNodes(collisionBox.getBoundingBox())) {
		              node.setPenalty(AStarGrid.PENALTY_STATIC_PROP);
		          }
		        }
		        grid.setAllowCuttingCorners(false);
		        grids.put(e.getMap().getName(), grid);
			}
			
			/**
			 * we'll need to update this when we implement the boss map
			 */
			@Override
			public void loaded(Environment e) {
				
			}
		});
	    
	    Game.loop().attach(RaidersLogic::update);
	}
	
	/**
	 * what happens when you press play in the menu
	 */
	public static void onPlay() {
		setState(GameState.INGAME);
		Game.world().loadEnvironment("tutorial.tmx");
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
	
	/**
	 * when a new environment is loaded, spawn events are handled. added to game loop in the init method
	 */
	public static void update() {
	    if (Game.world().environment() == null || !Game.screens().current().getName().equals("INGAME-SCREEN")) {
	      return;
	    }
	    
	    handleEnemySpawns();
	}
	
	/**
	 * handles all the spawn events and avoids some errors
	 */
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
	
	/**
	 * spawns enemies given an event which attaches a name to the spawn
	 * @param event the spawn event that you want to execute
	 */
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
	
	/**
	 * gets the current grid that is loaded
	 * @return the current grid
	 */
	public static AStarGrid getCurrentGrid() {
	    if (Game.world().environment() == null || !grids.containsKey(Game.world().environment().getMap().getName())) {
	      return null;
	    }

	    return grids.get(Game.world().environment().getMap().getName());
	}
}
