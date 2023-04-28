package entities;

import java.awt.Color;

import de.gurkenlabs.litiengine.Game;
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
@CollisionInfo(collisionBoxWidth = 12, collisionBoxHeight = 15, collision = true)
@CombatInfo(hitpoints = 10, team = 2)
public class Minion extends Enemy {
	private final Spawnpoint spawn;
	
	/**
	 * creates a new minion with a given spawn point
	 * @param spawn the spawn point
	 */
	public Minion(Spawnpoint spawn) {
	    super(spawn);
	
	    this.spawn = spawn;
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
    	/* For testing purpose manually creating animation controller
      IEntityAnimationController<?> controller = new MinionAnimationController(this);
      return controller;
      */
    	
    	IEntityAnimationController<?> animationController;
    	Spritesheet idle = Resources.spritesheets().get("minion-idle-right");
    	animationController = new CreatureAnimationController<Minion>(this,new Animation(idle,false));
    	
    	CreatureShadowImageEffect effect = new CreatureShadowImageEffect(this, new Color(24, 30, 28, 100));
	    effect.setOffsetY(1);
	    animationController.add(effect);
	    
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
}
