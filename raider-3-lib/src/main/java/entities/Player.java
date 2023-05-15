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
import ui.DeathScreen;

/**
 * the class that creates the entity of player
 * @author Kevin Lorinc, Kush Vashishtha
 *
 */
@EntityInfo(width = 32, height = 32)
@MovementInfo(velocity = 200)
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
	
	private String equipped;
	private HashMap<String,Boolean> inventory;
	
	private SpinAttack spinAttack;
	private MeleeAttack meleeAttack;
	
	/**
	 * creates the instance of the player class and its movement controller
	 */
	private Player() {
		super("raider");
		
		equipped = "fist";
		
		spinAttack = new SpinAttack(this);
		meleeAttack = new MeleeAttack(this);
		
		this.movement().onMovementCheck(e -> {//this line may cause the whole thing to not work properly
	      return this.getState() == PlayerState.CONTROLLABLE;
	    });

		inventory = new HashMap<String,Boolean>();
		inventory.put("fist", true);
		inventory.put("sword", false);
		inventory.put("swordPurple", false);
		inventory.put("swordBlue", false);
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
	
	public void setInstanceNull() {
		instance = null;
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
		
		Spritesheet idleSwordPR = Resources.spritesheets().get("raider-idle-swordPurple-right");
		Spritesheet walkSwordPR = Resources.spritesheets().get("raider-walk-swordPurple-right");
		Spritesheet idleSwordPL = Resources.spritesheets().get("raider-idle-swordPurple-left");
		Spritesheet walkSwordPL = Resources.spritesheets().get("raider-walk-swordPurple-left");
		Spritesheet swordSpinPR = Resources.spritesheets().get("raider-idle-swordPurpleSpin-right");
		Spritesheet swordMeleeSidePR = Resources.spritesheets().get("raider-idle-swordPurpleMeleeSide-right");
		Spritesheet swordMeleeDownPR = Resources.spritesheets().get("raider-idle-swordPurpleMeleeDown-right");
		Spritesheet swordMeleeUpPR = Resources.spritesheets().get("raider-idle-swordPurpleMeleeUp-right");
		Spritesheet swordSpinPL = Resources.spritesheets().get("raider-idle-swordPurpleSpin-left");
		Spritesheet swordMeleeSidePL = Resources.spritesheets().get("raider-idle-swordPurpleMeleeSide-left");
		Spritesheet swordMeleeDownPL = Resources.spritesheets().get("raider-idle-swordPurpleMeleeDown-left");
		Spritesheet swordMeleeUpPL = Resources.spritesheets().get("raider-idle-swordPurpleMeleeUp-left");

		Spritesheet idleSwordBR = Resources.spritesheets().get("raider-idle-swordBlue-right");
		Spritesheet walkSwordBR = Resources.spritesheets().get("raider-walk-swordBlue-right");
		Spritesheet idleSwordBL = Resources.spritesheets().get("raider-idle-swordBlue-left");
		Spritesheet walkSwordBL = Resources.spritesheets().get("raider-walk-swordBlue-left");
		Spritesheet swordSpinBR = Resources.spritesheets().get("raider-idle-swordBlueSpin-right");
		Spritesheet swordMeleeSideBR = Resources.spritesheets().get("raider-idle-swordBlueMeleeSide-right");
		Spritesheet swordMeleeDownBR = Resources.spritesheets().get("raider-idle-swordBlueMeleeDown-right");
		Spritesheet swordMeleeUpBR = Resources.spritesheets().get("raider-idle-swordBlueMeleeUp-right");
		Spritesheet swordSpinBL = Resources.spritesheets().get("raider-idle-swordBlueSpin-left");
		Spritesheet swordMeleeSideBL = Resources.spritesheets().get("raider-idle-swordBlueMeleeSide-left");
		Spritesheet swordMeleeDownBL = Resources.spritesheets().get("raider-idle-swordBlueMeleeDown-left");
		Spritesheet swordMeleeUpBL = Resources.spritesheets().get("raider-idle-swordBlueMeleeUp-left");
		
		Spritesheet death = Resources.spritesheets().get("raider-death");
		
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
		animationController.add(new Animation(death,false));
		
		animationController.add(new Animation(walkSwordPR,true));
		animationController.add(new Animation(idleSwordPR, true));
		animationController.add(new Animation(swordSpinPR,false));
		animationController.add(new Animation(walkSwordPL,true));
		animationController.add(new Animation(idleSwordPL, true));
		animationController.add(new Animation(swordSpinPL,false));
		animationController.add(new Animation(swordMeleeSidePR, false));
		animationController.add(new Animation(swordMeleeDownPR,false));
		animationController.add(new Animation(swordMeleeUpPR,false));
		animationController.add(new Animation(swordMeleeSidePL, false));
		animationController.add(new Animation(swordMeleeDownPL,false));
		animationController.add(new Animation(swordMeleeUpPL,false));

		animationController.add(new Animation(walkSwordBR,true));
		animationController.add(new Animation(idleSwordBR, true));
		animationController.add(new Animation(swordSpinBR,false));
		animationController.add(new Animation(walkSwordBL,true));
		animationController.add(new Animation(idleSwordBL, true));
		animationController.add(new Animation(swordSpinBL,false));
		animationController.add(new Animation(swordMeleeSideBR, false));
		animationController.add(new Animation(swordMeleeDownBR,false));
		animationController.add(new Animation(swordMeleeUpBR,false));
		animationController.add(new Animation(swordMeleeSideBL, false));
		animationController.add(new Animation(swordMeleeDownBL,false));
		animationController.add(new Animation(swordMeleeUpBL,false));
			
	    animationController.addRule(x -> (this.calcFacingDirection() == Direction.LEFT) && this.isIdle() && equipped.equals("fist"), x -> "raider-idle-left");
	    animationController.addRule(x -> (this.calcFacingDirection() == Direction.LEFT) && !this.isIdle() && equipped.equals("fist"), x -> "raider-walk-left");
	    animationController.addRule(x -> (this.calcFacingDirection() == Direction.RIGHT) && this.isIdle() && equipped.equals("fist"), x -> "raider-idle-right");
	    animationController.addRule(x -> (this.calcFacingDirection() == Direction.RIGHT) && !this.isIdle() && equipped.equals("fist"), x -> "raider-walk-right");
	    
	    animationController.addRule(x -> (this.calcFacingDirection() == Direction.LEFT) && this.isIdle() && equipped.equals("sword"), x -> "raider-idle-sword-left");
	    animationController.addRule(x -> (this.calcFacingDirection() == Direction.LEFT) && !this.isIdle() && equipped.equals("sword"), x -> "raider-walk-sword-left");
	    animationController.addRule(x -> (this.calcFacingDirection() == Direction.RIGHT) && this.isIdle() && equipped.equals("sword"), x -> "raider-idle-sword-right");
	    animationController.addRule(x -> (this.calcFacingDirection() == Direction.RIGHT) && !this.isIdle() && equipped.equals("sword"), x -> "raider-walk-sword-right");
	    
	    animationController.addRule(x -> (this.calcFacingDirection() == Direction.LEFT) && this.isIdle() && equipped.equals("swordPurple"), x -> "raider-idle-swordPurple-left");
	    animationController.addRule(x -> (this.calcFacingDirection() == Direction.LEFT) && !this.isIdle() && equipped.equals("swordPurple"), x -> "raider-walk-swordPurple-left");
	    animationController.addRule(x -> (this.calcFacingDirection() == Direction.RIGHT) && this.isIdle() && equipped.equals("swordPurple"), x -> "raider-idle-swordPurple-right");
	    animationController.addRule(x -> (this.calcFacingDirection() == Direction.RIGHT) && !this.isIdle() && equipped.equals("swordPurple"), x -> "raider-walk-swordPurple-right");
	    
	    animationController.addRule(x -> (this.calcFacingDirection() == Direction.LEFT) && this.isIdle() && equipped.equals("swordBlue"), x -> "raider-idle-swordBlue-left");
	    animationController.addRule(x -> (this.calcFacingDirection() == Direction.LEFT) && !this.isIdle() && equipped.equals("swordBlue"), x -> "raider-walk-swordBlue-left");
	    animationController.addRule(x -> (this.calcFacingDirection() == Direction.RIGHT) && this.isIdle() && equipped.equals("swordBlue"), x -> "raider-idle-swordBlue-right");
	    animationController.addRule(x -> (this.calcFacingDirection() == Direction.RIGHT) && !this.isIdle() && equipped.equals("swordBlue"), x -> "raider-walk-swordBlue-right");
	
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
	 * sets equipped to assigned weapon
	 * @param set state to set whether sword is equipped or not
	 */
	public void setEquipped(String set) {
		equipped = set;
	}
	
	/**
	 * if equipped
	 * @return whether player has sword equipped or not
	 */
	public String getEquipped() {
		return equipped;
	}
	
	/**
	 * gets the inventory
	 * @return inventory
	 */
	public HashMap<String,Boolean> getInventory(){
		return inventory;
	}
	
	public void gotItem(String item) {
		inventory.put(item,true);
	}
	
	public boolean hasItem(String item) {
		return inventory.get(item);
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
	
	public void onDead() {
		this.animations().play("raider-death");
		Game.loop().perform(1000, () -> {
			Game.world().environment().remove(Player.instance());
			Game.world().unloadEnvironment();
			Game.screens().add(new DeathScreen());
			Game.screens().display("DEATH-SCREEN");
		});
	}
	
	/**
	 * updates every frame for testing purposes
	 */
	@Override
	public void update() {
	}
}