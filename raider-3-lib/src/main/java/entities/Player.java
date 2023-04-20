package entities;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;

import abilities.MeleeAttack;
import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.CollisionInfo;
import de.gurkenlabs.litiengine.entities.CombatInfo;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.entities.EntityInfo;
import de.gurkenlabs.litiengine.entities.MovementInfo;
import de.gurkenlabs.litiengine.graphics.CreatureShadowImageEffect;
import de.gurkenlabs.litiengine.graphics.Spritesheet;
import de.gurkenlabs.litiengine.graphics.animation.Animation;
import de.gurkenlabs.litiengine.graphics.animation.CreatureAnimationController;
import de.gurkenlabs.litiengine.graphics.animation.IEntityAnimationController;
import de.gurkenlabs.litiengine.input.KeyboardEntityController;
import de.gurkenlabs.litiengine.physics.IMovementController;
import de.gurkenlabs.litiengine.resources.Resources;
import raider.RaidersMath;

/**
 * the class that creates the entity of player
 * @author Kevin Lorinc
 *
 */
@EntityInfo(width = 32, height = 32)
@MovementInfo(velocity = 100)
@CollisionInfo(collisionBoxWidth = 12, collisionBoxHeight = 15, collision = true)
@CombatInfo(hitpoints = 100, team = 1)
public class Player extends Creature implements IUpdateable{
	/**
	 * various constants for the state of a player which will be more deeply dealt with in the UI package.
	 * @author Kevin Lorinc
	 *
	 */
	public enum PlayerState {
		CONTROLLABLE,
		LOCKED,
		INCOMBAT
	}
	
	private static Player instance;
	private PlayerState state = PlayerState.CONTROLLABLE;//for testing purposes might need to be changed to Controllable once we get litidata in
	private Direction lastFaced;
	private Direction xSpriteSheet;
	
	private MeleeAttack meleeAttack;
	
	/**
	 * creates the instance of the player class and its movement controller
	 */
	private Player() {
		super("raider");
		
		
		meleeAttack = new MeleeAttack(this);
		
		lastFaced = Direction.RIGHT;
		xSpriteSheet = Direction.RIGHT;
		
		this.movement().onMovementCheck(e -> {//this line may cause the whole thing to not work properly
	      return this.getState() == PlayerState.CONTROLLABLE;
	    });
		
		this.onMoved(e -> {
			if(this.getFacingDirection() == Direction.RIGHT)
				lastFaced = Direction.RIGHT;
			else if(this.getFacingDirection() == Direction.LEFT)
				lastFaced = Direction.LEFT;
		});
	}
	
	/**
	 * creates a new instance or returns the existing instance
	 * @return the instance of the player class 
	 */
	public static Player instance() {
		if(instance == null) {instance = new Player();}
		
		return instance;
	}
	
	@Override
	protected IMovementController createMovementController() {
		KeyboardEntityController<Player> movementController = new KeyboardEntityController<>(this);
		movementController.addUpKey(KeyEvent.VK_W);
		movementController.addDownKey(KeyEvent.VK_S);
		movementController.addLeftKey(KeyEvent.VK_A);
		movementController.addRightKey(KeyEvent.VK_D);

		return movementController;
	}
	
	@Override
	protected IEntityAnimationController<?> createAnimationController() {
		Spritesheet idle = Resources.spritesheets().get("raider-idle-right");
		Spritesheet walk = Resources.spritesheets().get("raider-walk-right");
		
		IEntityAnimationController<?> animationController = new CreatureAnimationController<Player>(this,new Animation(idle,false));
		
	    animationController.add(new Animation(walk,true));
	    
		animationController.addRule(x -> (this.getFacingDirection() == Direction.LEFT) && this.isIdle(), x -> "raider-idle-left");
		animationController.addRule(x -> (this.getFacingDirection() == Direction.RIGHT) && this.isIdle(), x -> "raider-idle-right");
		
		animationController.addRule(x -> (this.getFacingDirection() == Direction.UP) && this.isIdle(), x -> {
			if(lastFaced == Direction.RIGHT) return "raider-idle-right";
			else return "raider-idle-left";
		});
		
		animationController.addRule(x -> (this.getFacingDirection() == Direction.UP) && !this.isIdle(), x -> {
			if(lastFaced == Direction.RIGHT) return "raider-walk-right";
			else return "raider-walk-left";
		});
		
		animationController.addRule(x -> (this.getFacingDirection() == Direction.DOWN) && this.isIdle(), x -> {
			if(lastFaced == Direction.RIGHT) return "raider-idle-right";
			else return "raider-idle-left";
		});
		
		animationController.addRule(x -> (this.getFacingDirection() == Direction.DOWN) && !this.isIdle(), x -> {
			if(lastFaced == Direction.RIGHT) return "raider-walk-right";
			else return "raider-walk-left";
		});
		
	    CreatureShadowImageEffect effect = new CreatureShadowImageEffect(this, new Color(24, 30, 28, 100));
	    effect.setOffsetY(1);
	    animationController.add(effect);
	    
	    return animationController;
	}
	
	/**
	 * gets the state of the player
	 * @return the state of the player
	 */
	public PlayerState getState() {
	  return state;
	}
	
	/**
	 * allows you to change the state of the player
	 * @param state the state to change to
	 */
	public void setState(PlayerState state) {
	  this.state = state;
	}
	
	public MeleeAttack getMeleeAttack() {
		return meleeAttack;
	}
	
	public Direction calcDirection() {
		Point mousePosition = Game.window().getRenderComponent().getMousePosition();
		Point2D playerLoc = Game.world().camera().getViewportLocation(this);
		return RaidersMath.getMouseDirection(mousePosition, playerLoc);
		
	}
	/* Uses mouse location to find which spritesheet to use, to be used in update(), most likely will be moved to logic
	public Direction findXSpriteSheet() {
		
	}
*/
	@Override
	public void update() {
		//System.out.println(this.calcDirection());
	}
}