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
 * @author Kevin Lorinc, Kush Vashishtha
 */
public class ReaperController extends MovementController<Reaper>{
	private static final int NAVIGATE_DELAY = 1000;
	private EntityNavigator navi;
	private long lastNavigate;
	private Reaper thisReaper;
	private long actionTime;
	
	private boolean removed;
	
	/**
	 * creates a new movement controller for a Reaper
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
	 * logic of the controller that handles all reaper states
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
		
		else if(Game.time().since(actionTime) <= 3200) {
			if(thisReaper.getEnemyState()==EnemyState.HIT && !thisReaper.getIsSpawned()) {
				thisReaper.animations().play("reaper-spawn");
				thisReaper.setIsSpawned(true);
				Game.loop().perform(2700, () -> {
					thisReaper.animations().play("raider-laugh-right");
				});
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
	  	          thisReaper.onDead();
	  	        });
	  	        return;
	  	    }
	  	    
	  	    if (this.getEntity().getTarget().isDead()) {
	  	        return;
	  	    }
	  	    
	  	    if (Game.time().since(this.lastNavigate) < NAVIGATE_DELAY) {
	  	        return;
	  	    }
	  	    
	  	    
	  	    
	  	    if(thisReaper.getEnemyState()==EnemyState.IDLE)
	  	    	Game.loop().perform(4000, () -> thisReaper.setEnemyState(EnemyState.ROAMING));
	  	    
	  	    double dist = this.getEntity().getTarget().getCenter().distance(this.getEntity().getCenter());
	  	    
	  	    if(dist < 400 && !this.navi.isNavigating() && thisReaper.getEnemyState() != EnemyState.IDLE) {
	  	    	this.navi.navigate(this.getEntity().getTarget().getCenter());
	  	    } else  if (this.navi.isNavigating() || thisReaper.getEnemyState()==EnemyState.IDLE){
	  	    	this.navi.stop();
	  	    	if (this.getEntity().getReaperAttack().canCast() && dist < 50 && thisReaper.getEnemyState()!=EnemyState.IDLE){
	  	    		Game.loop().perform(250, () -> {
	  	    			this.getEntity().getReaperAttack().cast();
		  		    	thisReaper.setEnemyState(EnemyState.IDLE);
		  		    	if(thisReaper.getHitPoints().getRelativeCurrentValue() < 0.35) {
		  		    		thisReaper.setVelocity(150);
		  		    		if(thisReaper.getFacingDirection()==Direction.RIGHT) thisReaper.animations().play("reaper-frenzy-right");
		  		    		else thisReaper.animations().play("reaper-frenzy-left");
		  		    	}else {
		  		    		thisReaper.setVelocity(80);
		  		    		if(thisReaper.getX() <= Player.instance().getX()) thisReaper.animations().play("reaper-attack-right");
		  		    		else thisReaper.animations().play("reaper-attack-left");
		  		    	}
	  	    		});
	  		    	}
	  	    }
	    } 
	}

	/**
	 * sets a variable used to time spawn animation
	 * @param time when spawn animation starts
	 */
	public void setActionTime(long time) {
		actionTime = time;
	}
}
