package entities;

import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.attributes.Attribute;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.entities.ICollisionEntity;
import de.gurkenlabs.litiengine.entities.Spawnpoint;
import de.gurkenlabs.litiengine.environment.Environment;
import de.gurkenlabs.litiengine.physics.Force;
import de.gurkenlabs.litiengine.util.geom.GeometricUtilities;
import entities.Enemy.EnemyState;

/**
 * a parent class for all enemies in the game that changes some elements of creature that are common to all enemies
 * @author Kevin Lorinc
 */
public abstract class Enemy extends Creature {
	/**
	 * creates an enum to help handle whether a creature should move or not
	 * @author Kevin Lorinc
	 */
	public enum EnemyState {
		NOTSPAWNED,
		ORB,
		ROAMING,
		HIT
	}
	
	private boolean engaged;
	private EnemyState state;
	
	/**
	 * creates a new enemy with a specified spawn point
	 * @param spawn the spawn point
	 */
	public Enemy(Spawnpoint spawn) {
		super(spawn.getName());
		state = EnemyState.ROAMING;
	}
	
	/*
	 * updates the can collide so that enemies don't collide with other instances of enemy
	 */
	@Override
	public boolean canCollideWith(ICollisionEntity otherEntity) {
	  return super.canCollideWith(otherEntity) && !(otherEntity instanceof Enemy);
	}
	
	/**
	 * now gives instructions on what to do when the enemy is loaded
	 */
	@Override
    public void loaded(Environment environment) {
      super.loaded(environment);

      this.updateTarget();
    }
	
	/**
	 * gets facing direction for an enemy generically using angles.
	 */
	@Override
	public Direction getFacingDirection() {
	  double actual = GeometricUtilities.normalizeAngle(this.getAngle());

	  if (actual >= 0 && actual < 90 || actual > 270 && actual <= 360) {
	    return Direction.DOWN;
	  }
	  if (actual == 90) {
	    return Direction.RIGHT;
	  }
	  if (actual > 90 && actual < 270) {
	    return Direction.UP;
	  }
	  if (actual == 270) {
	    return Direction.LEFT;
	  }
	  return Direction.UNDEFINED;
	}
	
	/**
	 * gets the enemys state
	 * @return the enemy's state
	 */
	public EnemyState getEnemyState() {
		return state;
	}
	
	/**
	 * sets enemy's state
	 * @param the enemy state to be set
	 */
	public void setEnemyState(EnemyState newState) {
		state = newState;
	}
	
	public boolean isEngaged() {
		return engaged;
	}
	
	/**
	 * updates the target for an enemy when it is loaded or in the update method
	 */
	public abstract void updateTarget();
}
