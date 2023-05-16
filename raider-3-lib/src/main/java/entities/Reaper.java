package entities;

import abilities.ReaperAttack;
import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.CollisionInfo;
import de.gurkenlabs.litiengine.entities.CombatInfo;
import de.gurkenlabs.litiengine.entities.EntityInfo;
import de.gurkenlabs.litiengine.entities.IEntity;
import de.gurkenlabs.litiengine.entities.MovementInfo;
import de.gurkenlabs.litiengine.entities.Spawnpoint;
import de.gurkenlabs.litiengine.graphics.Spritesheet;
import de.gurkenlabs.litiengine.graphics.animation.Animation;
import de.gurkenlabs.litiengine.graphics.animation.IEntityAnimationController;
import de.gurkenlabs.litiengine.physics.IMovementController;
import de.gurkenlabs.litiengine.resources.Resources;
import ui.WinScreen;

/**
 * The class for the Reaper enemy
 * @author Kevin Lorinc, Kush Vashishtha
 */
@EntityInfo(width = 32, height = 32)
@MovementInfo(velocity = 70)
@CollisionInfo(collisionBoxWidth = 20, collisionBoxHeight = 32, collision = true)
@CombatInfo(hitpoints = 200, team = 2)
public class Reaper extends Enemy implements IUpdateable{
	private final Spawnpoint spawn;
	private final ReaperAttack reaperAttack = new ReaperAttack(this);
	private boolean isSpawned;
	
	/**
	 * creates a new reaper with a given spawn point
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
	 * creates the movement controller for the reaper
	 * @return the movement controller of the reaper
	 */
    @Override
    protected IMovementController createMovementController() {
      return new ReaperController(this);
    }

    /**
     * creates the animation controller for this class
     * @return the animation controller of the reaper
     */
    @Override
    protected IEntityAnimationController<?> createAnimationController() {
    	Spritesheet reaper = Resources.spritesheets().get("reaper-idle-right");
    	
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
	
	/**
	 * returns whether reaper is spawned or not
	 * @return whether reaper is spawned or not
	 */
	public boolean getIsSpawned() {
		return isSpawned;
	}
	
	/**
	 * sets if reaper is spawned
	 * @param state the spawn state to set the reaper in
	 */
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
	
	/**
	 * updates every frame, sets facing direction of reaper
	 */
	public void update() {
			if(Player.instance().getX()<this.getX()) this.setFacingDirection(Direction.LEFT);
			else this.setFacingDirection(Direction.RIGHT);
	}
	
	/**
	 * actions to be handled on death of reaper
	 */
	public void onDead() {
			Game.world().environment().remove(Player.instance());
			for(IEntity entity: Game.world().environment().getEntities()) {
				if(entity instanceof Enemy)
				Game.world().environment().remove(entity);
			}
			Game.world().unloadEnvironment();
			Game.world().loadEnvironment("tutorial.tmx");
			Game.world().environment().remove(Player.instance());
			for(IEntity entity: Game.world().environment().getEntities()) {
				if(entity instanceof Enemy)
				Game.world().environment().remove(entity);
			}
			Game.world().unloadEnvironment();
			Game.screens().add(new WinScreen());
			Game.screens().display("WIN-SCREEN");
	}
	
	/**
	 * gets the reaper's attack ability
	 * @return the attack ability
	 */
	public ReaperAttack getReaperAttack() {
	    return reaperAttack;
	  }
}
