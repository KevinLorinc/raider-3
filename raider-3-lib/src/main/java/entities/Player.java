package entities;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.*;

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
	private boolean equipped;
	
	private MeleeAttack meleeAttack;
	
	/**
	 * creates the instance of the player class and its movement controller
	 */
	private Player() {
		super("raider");
		
		equipped = false;
		
		meleeAttack = new MeleeAttack(this);
		
		this.movement().onMovementCheck(e -> {//this line may cause the whole thing to not work properly
	      return this.getState() == PlayerState.CONTROLLABLE;
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
		
		for(Spritesheet x : Resources.spritesheets().getAll())
			System.out.println(x.getName());
		
		Spritesheet idle = Resources.spritesheets().get("raider-idle-right");
		Spritesheet walk = Resources.spritesheets().get("raider-walk-right");
		
		Spritesheet idleSwordRight = Resources.spritesheets().get("raider-idleSword-right");
		Spritesheet walkSwordRight = Resources.spritesheets().get("raider-walkSword-right");
		
		Spritesheet idleSwordLeft = Resources.spritesheets().get("raider-idleSword-left");
		Spritesheet walkSwordLeft = Resources.spritesheets().get("raider-walkSword-left");
		
		Spritesheet idleAttackRight = Resources.spritesheets().get("raider-idleSwordAttack-right");
		IEntityAnimationController<?> animationController;
		
		//if(!hasSword) {
			animationController = new CreatureAnimationController<Player>(this,new Animation(idle,false));
			animationController.add(new Animation(walk,true));
			animationController.add(new Animation(walkSwordRight,true));
			animationController.add(new Animation(idleSwordRight, true));
			animationController.add(new Animation(idleAttackRight ,false));
			animationController.add(new Animation(idleSwordLeft, true));
			animationController.add(new Animation(walkSwordLeft ,true));
			
			/*
			if(hasSword) {
			animationController.setDefault(new Animation(idleSword,false));
			}
			*/
			
		//}else {
		//	animationController = new CreatureAnimationController<Player>(this,new Animation(idleSword,false));
		//	animationController.add(new Animation(walkSword,true));
		//}
		
			
	    
	    
	    animationController.addRule(x -> (this.calcDirection() == Direction.LEFT) && this.isIdle() && !equipped, x -> "raider-idle-left");
	    animationController.addRule(x -> (this.calcDirection() == Direction.LEFT) && !this.isIdle() && !equipped, x -> "raider-walk-left");
	    animationController.addRule(x -> (this.calcDirection() == Direction.RIGHT) && this.isIdle() && !equipped, x -> "raider-idle-right");
	    animationController.addRule(x -> (this.calcDirection() == Direction.RIGHT) && !this.isIdle() && !equipped, x -> "raider-walk-right");
	    
	    animationController.addRule(x -> (this.calcDirection() == Direction.LEFT) && this.isIdle() && equipped, x -> "raider-idleSword-left");
	    animationController.addRule(x -> (this.calcDirection() == Direction.LEFT) && !this.isIdle() && equipped, x -> "raider-walkSword-left");
	    animationController.addRule(x -> (this.calcDirection() == Direction.RIGHT) && this.isIdle() && equipped, x -> "raider-idleSword-right");
	    animationController.addRule(x -> (this.calcDirection() == Direction.RIGHT) && !this.isIdle() && equipped, x -> "raider-walkSword-right");
	
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
	
	/**
	 * get the melee attack object to call methods on
	 * @return the melee attack instance
	 */
	public MeleeAttack getMeleeAttack() {
		return meleeAttack;
	}
	
	public void setEquipped(boolean set) {
		equipped = set;
	}
	
	public boolean getEquipped() {
		return equipped;
	}
	
	/**
	 * takes the player location and mouse location and uses the raiders math method left or right to get the direction of the mouse relative to the player
	 * if mouse out of bounds default direction right to avoid console nullpointer spam
	 * @return the direction of the mouse relative to the player
	 */
	public Direction calcDirection() {
		Point mousePosition = Game.window().getRenderComponent().getMousePosition();
		Point2D playerLoc = Game.world().camera().getViewportLocation(this);
		
		Direction toReturn = null;
			try {
				toReturn = RaidersMath.getLeftOrRight(mousePosition, playerLoc); //changed temporarily to test left or right
			}catch(NullPointerException e){
				toReturn = Direction.RIGHT;
			}
			return toReturn;
		
	}
	
	/**
	 * updates every frame for testing purposes
	 */
	@Override
	public void update() {
		//System.out.println(this.calcDirection());
	}
}