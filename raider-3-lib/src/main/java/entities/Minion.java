package entities;

import abilities.MinionAttack;
import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.CollisionInfo;
import de.gurkenlabs.litiengine.entities.CombatInfo;
import de.gurkenlabs.litiengine.entities.EntityInfo;
import de.gurkenlabs.litiengine.entities.MovementInfo;
import de.gurkenlabs.litiengine.entities.Spawnpoint;
import de.gurkenlabs.litiengine.graphics.Spritesheet;
import de.gurkenlabs.litiengine.graphics.animation.Animation;
import de.gurkenlabs.litiengine.graphics.animation.IEntityAnimationController;
import de.gurkenlabs.litiengine.physics.IMovementController;
import de.gurkenlabs.litiengine.resources.Resources;

/**
 * the class for the minion enemy
 * @author Kevin Lorinc, Kush Vashishtha
 */
@EntityInfo(width = 32, height = 32)
@MovementInfo(velocity = 95)
@CollisionInfo(collisionBoxWidth = 10, collisionBoxHeight = 16, collision = true)
@CombatInfo(hitpoints = 15, team = 2)
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
	 * @return the movement controller of this minion
	 */
    @Override
    protected IMovementController createMovementController() {
      return new MinionController(this);
    }

    /**
     * creates the animation controller for this class
     * @return the animation controller for this class
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
	 * checks if this enemy is in range of player
	 */
	@Override
	public void updateTarget() {
		this.setTarget(Player.instance());
	}
	
	/**
	 * updates every frame of game, currently used to set enemy facing direction
	 */
	
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
