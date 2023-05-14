package entities;

import java.awt.Color;

import abilities.MinionAttack;
import abilities.ReaperAttack;
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
@CombatInfo(hitpoints = 100, team = 2)
public class Reaper extends Enemy implements IUpdateable{
	private final Spawnpoint spawn;
	private final ReaperAttack reaperAttack = new ReaperAttack(this);
	private boolean isSpawned;
	
	/**
	 * creates a new minion with a given spawn point
	 * @param spawn the spawn point
	 */
	public Reaper(Spawnpoint spawn) {
	    super(spawn);
	
	    this.spawn = spawn;
	    this.addTag("reaper");
	    this.addTag("enemy");
	    this.addTag("boss");
	    
	    isSpawned=false;
	    
	    this.setEnemyState(EnemyState.NOTSPAWNED);
	}
	
	/**
	 * creates the movement controller for the minion
	 */
    @Override
    protected IMovementController createMovementController() {
      return new ReaperController(this);
    }

    /**
     * creates the animation controller for this class
     */
    @Override
    protected IEntityAnimationController<?> createAnimationController() {
    	Spritesheet reaper = Resources.spritesheets().get("reaper-idle-left");
    	
    	IEntityAnimationController<?> animationController = new ReaperAnimationController(this,new Animation(reaper,true));
		
	    return animationController;
    }
	
	/**
	 * gets the spawn point
	 * @return the spawn point
	 */
	public Spawnpoint getSpawn() {
	    return spawn;
	}
	
	public boolean getIsSpawned() {
		return isSpawned;
	}
	
	public void setIsSpawned(boolean state) {
		isSpawned=state;
	}

	/**
	 * checks if this enemy is in range of players
	 */
	@Override
	public void updateTarget() {
		this.setTarget(Player.instance());
	}
	
	public void update() {
			if(Player.instance().getX()<this.getX()) this.setFacingDirection(Direction.LEFT);
			else this.setFacingDirection(Direction.RIGHT);
	}
	
	/**
	 * gets the reaper's attack ability
	 * @return the attack ability
	 */
	public ReaperAttack getReaperAttack() {
	    return reaperAttack;
	  }
}
