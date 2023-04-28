package entities;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.behavior.AStarGrid;
import de.gurkenlabs.litiengine.entities.behavior.AStarPathFinder;
import de.gurkenlabs.litiengine.entities.behavior.EntityNavigator;
import de.gurkenlabs.litiengine.physics.MovementController;
import raider.RaidersLogic;
import raider.RaidersLogic.GameState;

/**
 * a class that handles movement for the minion enemy
 * @author Kevin Lorinc
 */
public class MinionController extends MovementController<Minion>{
	private static final int NAVIGATE_DELAY = 500;
	private EntityNavigator navi;
	private long lastNavigate;
	
	private boolean removed;
	
	/**
	 * creates a new movement controller for a minion
	 * @param movingEntity the minion to create the movement controller for
	 */
	public MinionController(Minion movingEntity) {
		super(movingEntity);
		AStarGrid grid = RaidersLogic.getCurrentGrid();

	    this.navi = new EntityNavigator(movingEntity, new AStarPathFinder(grid));
	}
	
	/**
	 * changes the way the movement controller updates
	 */
	@Override
	public void update(){
		super.update();

	    if (RaidersLogic.getState() != GameState.INGAME) {
	      return;
	    }

	    if (this.getEntity().getTarget() == null || this.removed) {
	      return;
	    }
	    
	    //might have to add code here if we want the navigator to stop if the enemy is hit
	    
	    if(this.getEntity().getHitPoints().getRelativeCurrentValue() >= 0) {
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
	    
	    double dist = this.getEntity().getTarget().getCenter().distance(this.getEntity().getCenter());
	    if(dist > 10 && !this.navi.isNavigating()) { //will have to change this to account for hit box and what we want the range of minion to be
	    	this.navi.navigate(this.getEntity().getTarget().getCenter());
	    } else  if (this.navi.isNavigating()){
	    	this.navi.stop();
	    }/* else if (this.getEntity().getStabAbility().canCast()) {
	    	this.getEntity().getStabAbility().cast();
	    }*///this will be used to cast the ability when the enemy is within a certain range of the player
	}
}
