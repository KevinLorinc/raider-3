package raider;

import java.util.HashMap;
import java.util.LinkedList;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.CollisionBox;
import de.gurkenlabs.litiengine.entities.MapArea;
import de.gurkenlabs.litiengine.entities.Spawnpoint;
import de.gurkenlabs.litiengine.entities.behavior.AStarGrid;
import de.gurkenlabs.litiengine.entities.behavior.AStarNode;
import de.gurkenlabs.litiengine.environment.Environment;
import de.gurkenlabs.litiengine.environment.EnvironmentListener;
import de.gurkenlabs.litiengine.graphics.Camera;
import de.gurkenlabs.litiengine.graphics.PositionLockCamera;
import entities.Minion;
import entities.Player;
import entities.Reaper;
import ui.MenuScreen;

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
	
	private static MapArea transitionArea;
	
	private static final LinkedList<MapArea> chestArea = new LinkedList<MapArea>();
	
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
		spawnEvents.put("boss1", new LinkedList<EnemySpawnEvent>());
		spawnEvents.get("boss1").add(new EnemySpawnEvent("orbSpawn"));
		
		//adding trigger areas for chests
		chestArea.add(new MapArea(270,15,40,30));
		chestArea.add(new MapArea(785,530,50,40));
		chestArea.add(new MapArea(850,-10,40,30));
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
				//Player.instance().getHitPoints().setToMax();
		        Player.instance().setIndestructible(false);
		        Player.instance().setCollision(true);

		        Game.world().camera().setFocus(e.getCenter());
		        Spawnpoint spawn = e.getSpawnpoint("enter");
		        if (spawn != null) {
		          setState(GameState.INGAME);
		          spawn.spawn(Player.instance());
		        }

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
		transitionArea = new MapArea(1820,192,78,128);
	    Game.world().environment().add(transitionArea);
	    if(Game.screens().current() instanceof MenuScreen)
	    	Game.screens().remove(Game.screens().current());
	}
	
	
	/**
	 * checks if raider is in transition area
	 * @return a boolean indicating if player is in transition area
	 */
	public static boolean isInTransitionsArea() {
		if(transitionArea.getX() <= Player.instance().getX() && transitionArea.getY() <= Player.instance().getY()) {
			return true;
	    }
		return false;
	}
	
	/**
	 * checks if raider is in a chest area
	 * @return a int indicating if its in chest area and which chest it is
	 */
	public static int isInChestArea() {
		for(int i = 0; i<chestArea.size();i++) {
			if((chestArea.get(i).getX() <= Player.instance().getX()) && ((chestArea.get(i).getX()+ 40) >= Player.instance().getX())
				&& (chestArea.get(i).getY() <= Player.instance().getY()) && ((chestArea.get(i).getY()+ 30) >= Player.instance().getY())) return i+1;
		}
		return -1;
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
	 * transitions game to a new environment
	 * @param newEnvironment the environment to be transitioned to
	 */
	public static void transition(String newEnvironment) {
		Game.window().getRenderComponent().fadeOut(1000);
				
		Game.loop().perform(1000, () -> {
			  Game.world().unloadEnvironment();
			  Game.world().loadEnvironment(newEnvironment);
			  Camera camera = new PositionLockCamera(Player.instance());
		      camera.setClampToMap(true);
			  Game.world().setCamera(camera);
		      Player.instance().setIndestructible(false);
		      Player.instance().setCollision(true);

		      Game.world().camera().setFocus(Game.world().environment().getCenter());
		      Spawnpoint spawn = Game.world().environment().getSpawnpoint("enter");
		      if (spawn != null) {
		          setState(GameState.INGAME);
		          spawn.spawn(Player.instance());
		      }
		      
			  Game.window().getRenderComponent().fadeIn(1500);
		});
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
	    spawnEnemy(event);
	  }
	}
	
	/**
	 * resets all spawns
	 */
	public static void resetSpawns() {			
		for(EnemySpawnEvent event: spawnEvents.get(Game.world().environment().getMap().getName())) {
			event.finished = false;
		}
		Game.world().unloadEnvironment();
		Game.world().loadEnvironment("boss1.tmx");
		for(EnemySpawnEvent event: spawnEvents.get(Game.world().environment().getMap().getName())) {
			event.finished = false;
		}
		Game.world().unloadEnvironment();
		Game.world().loadEnvironment("tutorial.tmx");
	}
	
	/**
	 * spawns enemies given an event which attaches a name to the spawn
	 * @param event the spawn event that you want to execute
	 */
	private static void spawnEnemy(EnemySpawnEvent event) {//will have to change once we add more
	    event.finished = true;

	    Spawnpoint spawn = Game.world().environment().getSpawnpoint(event.spawnPoint);
	    if (spawn == null) {
	      System.out.println(Game.world().environment().getSpawnpoints());
	      System.out.println("Spawn " + event.spawnPoint + " could not be found on map " + Game.world().environment().getMap().getName());
	      return;
	    }
	    if(Game.world().environment().getMap().getName().equals("tutorial")) {
	    	spawn.spawn(new Minion(spawn));
	    }
	    if(Game.world().environment().getMap().getName().equals("boss1")) {
	    	Reaper boss = new Reaper(spawn);
	    	spawn.spawn(boss);
	    }
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
