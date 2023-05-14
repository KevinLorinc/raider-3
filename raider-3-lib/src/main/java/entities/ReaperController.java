package entities;

import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.behavior.AStarGrid;
import de.gurkenlabs.litiengine.entities.behavior.AStarPathFinder;
import de.gurkenlabs.litiengine.entities.behavior.EntityNavigator;
import de.gurkenlabs.litiengine.physics.Force;
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
	private long actionTime;
	
	private boolean removed;
	
	/**
	 * creates a new movement controller for a minion
	 * @param movingEntity the minion to create the movement controller for
	 */
	public ReaperController(Reaper movingEntity) {
		super(movingEntity);
		AStarGrid grid = RaidersLogic.getCurrentGrid();
		
		thisReaper = movingEntity;
		actionTime = 0;
		
	    this.navi = new EntityNavigator(movingEntity, new AStarPathFinder(grid));
	}
	
	/**
	 * changes the way the movement controller updates
	 */
	@Override
	public void update(){
		super.update();
		
		if(actionTime == 0) {
			if(thisReaper.getX()-Player.instance().getX()<150 && thisReaper.getEnemyState()==EnemyState.NOTSPAWNED){
				thisReaper.animations().play("orb-spawn");
				thisReaper.setEnemyState(EnemyState.ORB);
			}
		}
		
		else if(Game.time().since(actionTime) <= 3000) {
			if(thisReaper.getEnemyState()==EnemyState.HIT && !thisReaper.getIsSpawned()) {
				thisReaper.animations().play("reaper-spawn");
				Game.loop().perform(2000, () -> {
					thisReaper.animations().play("raider-laugh-right");
					thisReaper.setIsSpawned(true);
				});
			}else{
				thisReaper.setEnemyState(EnemyState.IDLE);
		    }
		}
		
	    else{
	    	if (RaidersLogic.getState() != GameState.INGAME) 
	  	      return;

	  	    if (this.getEntity().getTarget() == null || this.removed) 
	  	      return;
	  	    
	  	    if(this.getEntity().getHitPoints().getRelativeCurrentValue() <= 0) {
	  	    	removed = true;
	  	    	thisReaper.animations().play("reaper-death");
	  	        Game.loop().perform(2000, () -> {
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
	  	    
	  	    
	  	    if(dist < 400 && !this.navi.isNavigating()) {
	  	    	this.navi.navigate(this.getEntity().getTarget().getCenter());
	  	    } else  if (this.navi.isNavigating()){
	  	    	this.navi.stop();
	  	    	if (this.getEntity().getReaperAttack().canCast() && dist < 20) {
	  		    	this.getEntity().getReaperAttack().cast();
	  		    	if(thisReaper.getFacingDirection() == Direction.RIGHT)
	  		    		thisReaper.animations().play("reaper-attack-right");
	  		    	else
	  		    		thisReaper.animations().play("reaper-attack-left");
	  	    	}
	  	    	
	  	    }
	    }
	    
	}
	
	public void setActionTime(long time) {
		actionTime = time;
	}
}
