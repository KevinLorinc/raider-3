package entities;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.*;

import abilities.SpinAttack;
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
 * @author Kevin Lorinc, Kush Vashishtha
 *
 */
@EntityInfo(width = 32, height = 32)
@MovementInfo(velocity = 500)
@CollisionInfo(collisionBoxWidth = 12, collisionBoxHeight = 15, collision = true)
@CombatInfo(hitpoints = 100, team = 1)
public class Player extends Creature implements IUpdateable{
	/**
	 * various constants for the state of a player which will be more deeply dealt with in the UI package.
	 * @author Kevin Lorinc, Kush Vashishtha
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
	private HashMap<String,Boolean> inventory;
	
	private SpinAttack spinAttack;
	private MeleeAttack meleeAttack;
	
	/**
	 * creates the instance of the player class and its movement controller
	 */
	private Player() {
		super("raider");
		
		equipped = false;
		
		spinAttack = new SpinAttack(this);
		meleeAttack = new MeleeAttack(this);
		
		this.movement().onMovementCheck(e -> {//this line may cause the whole thing to not work properly
	      return this.getState() == PlayerState.CONTROLLABLE;
	    });

		inventory = new HashMap<String,Boolean>();
		inventory.put("sword", false);
		//add new things we want to put in the inventory here
	}
	
	/**
	 * creates a new instance or returns the existing instance
	 * @return the instance of the player class 
	 */
	public static Player instance() {
		if(instance == null) {instance = new Player();}
		
		return instance;
	}
	
	/**
	 * updates the movement controller to make the player move in a new way
	 */
	@Override
	protected IMovementController createMovementController() {
		KeyboardEntityController<Player> movementController = new KeyboardEntityController<>(this);
		movementController.addUpKey(KeyEvent.VK_W);
		movementController.addDownKey(KeyEvent.VK_S);
		movementController.addLeftKey(KeyEvent.VK_A);
		movementController.addRightKey(KeyEvent.VK_D);

		return movementController;
	}
	
	/**
	 * updates the animation controller to make the players animations work
	 */
	@Override
	protected IEntityAnimationController<?> createAnimationController() {
		
//		for(Spritesheet x : Resources.spritesheets().getAll())
//			System.out.println(x.getName());
		
		Spritesheet idle = Resources.spritesheets().get("raider-idle-right");
		Spritesheet walk = Resources.spritesheets().get("raider-walk-right");
		
		Spritesheet idleSwordR = Resources.spritesheets().get("raider-idle-sword-right");
		Spritesheet walkSwordR = Resources.spritesheets().get("raider-walk-sword-right");
		
		Spritesheet idleSwordL = Resources.spritesheets().get("raider-idle-sword-left");
		Spritesheet walkSwordL = Resources.spritesheets().get("raider-walk-sword-left");
		
		Spritesheet swordSpinR = Resources.spritesheets().get("raider-idle-swordSpin-right");
		Spritesheet swordMeleeSideR = Resources.spritesheets().get("raider-idle-swordMeleeSide-right");
		Spritesheet swordMeleeDownR = Resources.spritesheets().get("raider-idle-swordMeleeDown-right");
		Spritesheet swordMeleeUpR = Resources.spritesheets().get("raider-idle-swordMeleeUp-right");
		
		Spritesheet swordSpinL = Resources.spritesheets().get("raider-idle-swordSpin-left");
		Spritesheet swordMeleeSideL = Resources.spritesheets().get("raider-idle-swordMeleeSide-left");
		Spritesheet swordMeleeDownL = Resources.spritesheets().get("raider-idle-swordMeleeDown-left");
		Spritesheet swordMeleeUpL = Resources.spritesheets().get("raider-idle-swordMeleeUp-left");
		
		IEntityAnimationController<?> animationController;
		
		animationController = new CreatureAnimationController<Player>(this,new Animation(idle,false));
		animationController.add(new Animation(walk,true));
		animationController.add(new Animation(walkSwordR,true));
		animationController.add(new Animation(idleSwordR, true));
		animationController.add(new Animation(swordSpinR,false));
		animationController.add(new Animation(walkSwordL,true));
		animationController.add(new Animation(idleSwordL, true));
		animationController.add(new Animation(swordSpinL,false));
		animationController.add(new Animation(swordMeleeSideR, false));
		animationController.add(new Animation(swordMeleeDownR,false));
		animationController.add(new Animation(swordMeleeUpR,false));
		animationController.add(new Animation(swordMeleeSideL, false));
		animationController.add(new Animation(swordMeleeDownL,false));
		animationController.add(new Animation(swordMeleeUpL,false));
			
	    animationController.addRule(x -> (this.calcFacingDirection() == Direction.LEFT) && this.isIdle() && !equipped, x -> "raider-idle-left");
	    animationController.addRule(x -> (this.calcFacingDirection() == Direction.LEFT) && !this.isIdle() && !equipped, x -> "raider-walk-left");
	    animationController.addRule(x -> (this.calcFacingDirection() == Direction.RIGHT) && this.isIdle() && !equipped, x -> "raider-idle-right");
	    animationController.addRule(x -> (this.calcFacingDirection() == Direction.RIGHT) && !this.isIdle() && !equipped, x -> "raider-walk-right");
	    
	    animationController.addRule(x -> (this.calcFacingDirection() == Direction.LEFT) && this.isIdle() && equipped, x -> "raider-idle-sword-left");
	    animationController.addRule(x -> (this.calcFacingDirection() == Direction.LEFT) && !this.isIdle() && equipped, x -> "raider-walk-sword-left");
	    animationController.addRule(x -> (this.calcFacingDirection() == Direction.RIGHT) && this.isIdle() && equipped, x -> "raider-idle-sword-right");
	    animationController.addRule(x -> (this.calcFacingDirection() == Direction.RIGHT) && !this.isIdle() && equipped, x -> "raider-walk-sword-right");
	
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
	 * get the spin attack object to call methods on
	 * @return the spin attack instance
	 */
	public SpinAttack getSpinAttack() {
		return spinAttack;
	}
	
	/**
	 * get the melee attack object to call methods on
	 * @return the melee attack instance
	 */
	public MeleeAttack getMeleeAttack() {
		return meleeAttack;
	}
	
	/**
	 * sets equipped boolean to assigned state
	 * @param set state to set whether sword is equipped or not
	 */
	public void setEquipped(boolean set) {
		equipped = set;
	}
	
	/**
	 * 
	 * @return whether player has sword equipped or not
	 */
	public boolean getEquipped() {
		return equipped;
	}
	
	/**
	 * takes the player and mouse location and uses the raiders math method left or right to get the direction of the mouse relative to the player
	 * if mouse out of bounds default direction right to avoid console nullpointer spam
	 * used for determining which spritesheet to be used
	 * @return the direction of the mouse relative to the player, always Left or Right
	 */
	public Direction calcFacingDirection() {
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
	 * uses player and mouse location to find whether mouse is Up,Down,Left,or Right of the player, which is the attack direction
	 * if mouse out of bounds default direction right to avoid console nullpointer spam
	 * @return the direction of the mouse relative to the player, either Up, Down, Left, or Right
	 */
	public Direction calcAttackDirection() {
		Point mousePosition = Game.window().getRenderComponent().getMousePosition();
		Point2D playerLoc = Game.world().camera().getViewportLocation(this);
		
		Direction toReturn = null;
		try {
			toReturn = RaidersMath.getAttackDirection(mousePosition, playerLoc);
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
	}
}
/*
⣿⣿⣿⣿⣿⣿⣿⣿⣿⠟⠛⢉⢉⠉⠉⠻⣿⣿⣿⣿⣿⣿
⣿⣿⣿⣿⣿⣿⣿⠟⠠⡰⣕⣗⣷⣧⣀⣅⠘⣿⣿⣿⣿⣿
⣿⣿⣿⣿⣿⣿⠃⣠⣳⣟⣿⣿⣷⣿⡿⣜⠄⣿⣿⣿⣿⣿
⣿⣿⣿⣿⡿⠁⠄⣳⢷⣿⣿⣿⣿⡿⣝⠖⠄⣿⣿⣿⣿⣿
⣿⣿⣿⣿⠃⠄⢢⡹⣿⢷⣯⢿⢷⡫⣗⠍⢰⣿⣿⣿⣿⣿
⣿⣿⣿⡏⢀⢄⠤⣁⠋⠿⣗⣟⡯⡏⢎⠁⢸⣿⣿⣿⣿⣿
⣿⣿⣿⠄⢔⢕⣯⣿⣿⡲⡤⡄⡤⠄⡀⢠⣿⣿⣿⣿⣿⣿
⣿⣿⠇⠠⡳⣯⣿⣿⣾⢵⣫⢎⢎⠆⢀⣿⣿⣿⣿⣿⣿⣿
⣿⣿⠄⢨⣫⣿⣿⡿⣿⣻⢎⡗⡕⡅⢸⣿⣿⣿⣿⣿⣿⣿
⣿⣿⠄⢜⢾⣾⣿⣿⣟⣗⢯⡪⡳⡀⢸⣿⣿⣿⣿⣿⣿⣿
⣿⣿⠄⢸⢽⣿⣷⣿⣻⡮⡧⡳⡱⡁⢸⣿⣿⣿⣿⣿⣿⣿
⣿⣿⡄⢨⣻⣽⣿⣟⣿⣞⣗⡽⡸⡐⢸⣿⣿⣿⣿⣿⣿⣿
⣿⣿⡇⢀⢗⣿⣿⣿⣿⡿⣞⡵⡣⣊⢸⣿⣿⣿⣿⣿⣿⣿
⣿⣿⣿⡀⡣⣗⣿⣿⣿⣿⣯⡯⡺⣼⠎⣿⣿⣿⣿⣿⣿⣿
⣿⣿⣿⣧⠐⡵⣻⣟⣯⣿⣷⣟⣝⢞⡿⢹⣿⣿⣿⣿⣿⣿
⣿⣿⣿⣿⡆⢘⡺⣽⢿⣻⣿⣗⡷⣹⢩⢃⢿⣿⣿⣿⣿⣿
⣿⣿⣿⣿⣷⠄⠪⣯⣟⣿⢯⣿⣻⣜⢎⢆⠜⣿⣿⣿⣿⣿
⣿⣿⣿⣿⣿⡆⠄⢣⣻⣽⣿⣿⣟⣾⡮⡺⡸⠸⣿⣿⣿⣿
⣿⣿⡿⠛⠉⠁⠄⢕⡳⣽⡾⣿⢽⣯⡿⣮⢚⣅⠹⣿⣿⣿
⡿⠋⠄⠄⠄⠄⢀⠒⠝⣞⢿⡿⣿⣽⢿⡽⣧⣳⡅⠌⠻⣿
⠁⠄⠄⠄⠄⠄⠐⡐⠱⡱⣻⡻⣝⣮⣟⣿⣻⣟⣻⡺⣊
*/