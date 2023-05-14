package entities;

import java.awt.geom.Point2D;

import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.behavior.AStarGrid;
import de.gurkenlabs.litiengine.entities.behavior.AStarPathFinder;
import de.gurkenlabs.litiengine.entities.behavior.EntityNavigator;
import de.gurkenlabs.litiengine.graphics.Spritesheet;
import de.gurkenlabs.litiengine.graphics.animation.Animation;
import de.gurkenlabs.litiengine.physics.Force;
import de.gurkenlabs.litiengine.physics.MovementController;
import de.gurkenlabs.litiengine.resources.Resources;
import de.gurkenlabs.litiengine.util.geom.GeometricUtilities;
import entities.Enemy.EnemyState;
import raider.RaidersLogic;
import raider.RaidersLogic.GameState;

/**
 * a class that handles movement for the minion enemy
 * @author Kevin Lorinc
 */
public class MinionController extends MovementController<Minion>{
	private static final int NAVIGATE_DELAY = 1000;
	private static final int HIT_DELAY = 100;
	private EntityNavigator navi;
	private long lastNavigate;
	private Minion thisMinion;

	private Point2D applyPoint;
	private long applyTime;
	private boolean removed;
	
	/**
	 * creates a new movement controller for a minion
	 * @param movingEntity the minion to create the movement controller for
	 */
	public MinionController(Minion movingEntity) {
		super(movingEntity);
		AStarGrid grid = RaidersLogic.getCurrentGrid();
		
		thisMinion = movingEntity;
		
	    this.navi = new EntityNavigator(movingEntity, new AStarPathFinder(grid));
	}
	
	/**
	 * changes the way the movement controller updates
	 */
	@Override
	public void update(){
		this.handleMovement();
	    this.handleForces();

	    if (RaidersLogic.getState() != GameState.INGAME) {
	      return;
	    }

	    if (this.getEntity().getTarget() == null || this.removed) {
	      return;
	    }
	    
	    if(this.getEntity().getHitPoints().getRelativeCurrentValue() <= 0) {
	    	removed = true;
	    	thisMinion.animations().play("minion-death-right");
	        Game.loop().perform(600, () -> {
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
	    
	    
	    if(dist < 150 && !this.navi.isNavigating()) { //will have to change this to account for hit box and what we want the range of minion to be
	    	this.navi.navigate(this.getEntity().getTarget().getCenter());
	    } else  if (this.navi.isNavigating()){
	    	this.navi.stop();
	    	if(thisMinion.getEnemyState() == EnemyState.HIT) {
	    		if(thisMinion.getFacingDirection() == Direction.LEFT) thisMinion.animations().play("minion-damaged-left");
	    		else thisMinion.animations().play("minion-damaged-right");
	    		this.apply(new Force(thisMinion.getLocation(),20,5));//50 1
	    		thisMinion.setEnemyState(EnemyState.ROAMING);
	    	}
	    	else if (this.getEntity().getMinionAttack().canCast() && dist < 20) {
		    	this.getEntity().getMinionAttack().cast();
		    	if(thisMinion.getFacingDirection() == Direction.RIGHT)
		    		thisMinion.animations().play("minion-attack-right");
		    	else
		    		thisMinion.animations().play("minion-attack-left");
	    	}
	    	
	    }
	}
	
	public void setApplyPoint(Point2D point) {
		applyPoint = point;
	}
	
	public void setApplyTime(long time) {
		applyTime = time;
	}
	
	private void handleForces(){
		// clean up forces
	    this.getActiveForces().forEach(x -> {
	      if (x.hasEnded()) {
	        this.getActiveForces().remove(x);
	      }
	    });

	    if (this.getActiveForces().isEmpty()) {
	      return;
	    }

	    this.getEntity().setTurnOnMove(false);
	    try {
	      double deltaX = 0;
	      double deltaY = 0;
	      for (final Force force : this.getActiveForces()) {
	        if (force.cancelOnReached() && force.hasReached(this.getEntity())) {
	          force.end();
	          continue;
	        }

	        final Point2D collisionBoxCenter = this.getEntity().getCollisionBoxCenter();
	        final double angle = GeometricUtilities.calcRotationAngleInDegrees(collisionBoxCenter, applyPoint) + 180;
	        final double strength = Game.loop().getDeltaTime() * 0.005f * force.getStrength() * Game.loop().getTimeScale();
	        deltaX += GeometricUtilities.getDeltaX(angle, strength);
	        deltaY += GeometricUtilities.getDeltaY(angle, strength);
	      }

	      final Point2D target = new Point2D.Double(this.getEntity().getX() + deltaX, this.getEntity().getY() + deltaY);
	      final boolean success = Game.physics().move(this.getEntity(), target);
	      if (!success) {
	        for (final Force force : this.getActiveForces()) {
	          if (force.cancelOnCollision()) {
	            force.end();
	          }
	        }
	      }
	      for (final Force force : this.getActiveForces()) {
	          if (Game.time().since(applyTime) >= 340) {
	            force.end();
	          }
	        }
	    } finally {
	      this.getEntity().setTurnOnMove(true);
	    }
	  }
}
