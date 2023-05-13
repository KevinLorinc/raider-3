package entities;

import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.behavior.AStarGrid;
import de.gurkenlabs.litiengine.entities.behavior.AStarPathFinder;
import de.gurkenlabs.litiengine.entities.behavior.EntityNavigator;
import de.gurkenlabs.litiengine.physics.MovementController;
import entities.Enemy.EnemyState;
import raider.RaidersLogic;
import raider.RaidersLogic.GameState;

/**
 * a class that handles movement for the minion enemy
 * @author Kevin Lorinc
 */
public class ReaperController extends MovementController<Reaper>{
	private static final int NAVIGATE_DELAY = 1000;
	private static final int HIT_DELAY = 100;
	private EntityNavigator navi;
	private long lastNavigate;
	private Reaper thisReaper;
	
	private boolean removed;
	
	/**
	 * creates a new movement controller for a minion
	 * @param movingEntity the minion to create the movement controller for
	 */
	public ReaperController(Reaper movingEntity) {
		super(movingEntity);
		AStarGrid grid = RaidersLogic.getCurrentGrid();
		
		thisReaper = movingEntity;
		
	    this.navi = new EntityNavigator(movingEntity, new AStarPathFinder(grid));
	}
	
	/**
	 * changes the way the movement controller updates
	 */
	@Override
	public void update(){
		super.update();
		
		if(Player.instance().getY()-thisReaper.getY()<100 && thisReaper.getEnemyState()==EnemyState.NOTSPAWNED) {
			thisReaper.animations().play("orb-spawn");
			thisReaper.setEnemyState(EnemyState.ORB);
		}
		
		System.out.println(thisReaper.getEnemyState());
		
	    if (RaidersLogic.getState() != GameState.INGAME) {
	      return;
	    }

	    if (this.getEntity().getTarget() == null || this.removed) {
	      return;
	    }
	    
	    if(this.getEntity().getHitPoints().getRelativeCurrentValue() <= 0) {
	    	removed = true;
	        Game.loop().perform(1000, () -> {
	          Game.world().environment().remove(this.getEntity());
	        });
	        return;
	    }
	    
	    if (this.getEntity().getTarget().isDead()) {
	        return;
	    }
	    
	    if (Game.time().since(this.lastNavigate) < NAVIGATE_DELAY) {
	        return;
	    }
	}
}
