package entities;

import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.entities.ICollisionEntity;
import de.gurkenlabs.litiengine.entities.Spawnpoint;
import de.gurkenlabs.litiengine.environment.Environment;
import de.gurkenlabs.litiengine.util.geom.GeometricUtilities;

/**
 * a parent class for all enemies in the game that changes some elements of creature that are common to all enemies
 * @author Kevin Lorinc
 */
public abstract class Enemy extends Creature {
	/**
	 * creates a new enemy with a specified spawn point
	 * @param spawn the spawn point
	 */
	public Enemy(Spawnpoint spawn) {
		super(spawn.getName());
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
	 * updates the target for an enemy when it is loaded or in the update method
	 */
	public abstract void updateTarget();
}
