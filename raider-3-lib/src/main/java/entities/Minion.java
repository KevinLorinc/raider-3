package entities;

import java.awt.Color;

import abilities.MinionAttack;
import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.CollisionInfo;
import de.gurkenlabs.litiengine.entities.CombatInfo;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.entities.EntityInfo;
import de.gurkenlabs.litiengine.entities.ICollisionEntity;
import de.gurkenlabs.litiengine.entities.MovementInfo;
import de.gurkenlabs.litiengine.entities.Spawnpoint;
import de.gurkenlabs.litiengine.graphics.CreatureShadowImageEffect;
import de.gurkenlabs.litiengine.graphics.Spritesheet;
import de.gurkenlabs.litiengine.graphics.animation.Animation;
import de.gurkenlabs.litiengine.graphics.animation.CreatureAnimationController;
import de.gurkenlabs.litiengine.graphics.animation.IEntityAnimationController;
import de.gurkenlabs.litiengine.physics.IMovementController;
import de.gurkenlabs.litiengine.resources.Resources;

/**
 * The class for the minion (Blue hood) enemy
 * @author Kevin Lorinc
 */
@EntityInfo(width = 32, height = 32)
@MovementInfo(velocity = 70)
@CollisionInfo(collisionBoxWidth = 10, collisionBoxHeight = 16, collision = true)
@CombatInfo(hitpoints = 10, team = 2)
public class Minion extends Enemy implements IUpdateable{
	private final Spawnpoint spawn;
	private final MinionAttack minionAttack = new MinionAttack(this);
	
	/**
	 * creates a new minion with a given spawn point
	 * @param spawn the spawn point
	 */
	public Minion(Spawnpoint spawn) {
	    super(spawn);
	
	    this.spawn = spawn;
	    this.addTag("minion");
	    this.addTag("enemy");
	}
	
	/**
	 * creates the movement controller for the minion
	 */
    @Override
    protected IMovementController createMovementController() {
      return new MinionController(this);
    }

    /**
     * creates the animation controller for this class
     */
    @Override
    protected IEntityAnimationController<?> createAnimationController() {
    	Spritesheet idle = Resources.spritesheets().get("minion-idle-right");
    	
    	IEntityAnimationController<?> animationController;
    	animationController = new MinionAnimationController(this,new Animation(idle,true));
    	
	    return animationController;
    }
	
	/**
	 * gets the spawn point
	 * @return the spawn point
	 */
	public Spawnpoint getSpawn() {
	    return spawn;
	}

	/**
	 * checks if this enemy is in range of players
	 */
	@Override
	public void updateTarget() {
		this.setTarget(Player.instance());
	}
	
	public void update() {
		double dist = Player.instance().getCenter().distance(this.getCenter());
		if(dist<150) {
			if(Player.instance().getX()<this.getX()) this.setFacingDirection(Direction.LEFT);
			else this.setFacingDirection(Direction.RIGHT);
		}
	}
	
	/**
	 * gets the minions attack ability
	 * @return the attack ability
	 */
	public MinionAttack getMinionAttack() {
	    return minionAttack;
	  }
}
