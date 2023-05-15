package raider;

import java.awt.event.KeyEvent;
import java.util.LinkedList;

import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.input.Input;
import raider.RaidersLogic.GameState;
import entities.Player;
import entities.Player.PlayerState;

/**
 * a class that manages player input. Will eventually control all input such as when attacks are to be cast
 * @author Kevin Lorinc
 *
 */
public class PlayerInput {
	public static final LinkedList<Boolean> chestActive = new LinkedList<Boolean>();
	
	static {
		chestActive.add(true);
		chestActive.add(true);
		chestActive.add(true);
	}
	
	/**
	 * empty constructor
	 */
	private PlayerInput() {
	}
	
	/**
	 * initializes player input which will continually be listening for events. 
	 * More events to be added.
	 */
	public static void init() {
	  Input.keyboard().onKeyPressed(KeyEvent.VK_ESCAPE, e -> {// the exiting of the game will be handled somewhere in the ui package.
		  if (Player.instance().getState() == PlayerState.LOCKED || Player.instance().isDead()) {
	        return;
	      }

		  System.exit(0);
	  });
	  
	  Input.keyboard().onKeyPressed(KeyEvent.VK_SPACE, e -> {//spin attack
		  if (Player.instance().getState() == PlayerState.LOCKED || Player.instance().isDead()) {//|| !Player.instance().getEquipped()
		    return;
		  }
		  
		  Player.instance().getSpinAttack().cast();
		  if(Player.instance().calcFacingDirection().equals(Direction.RIGHT))
			Player.instance().animations().play("raider-idle-swordSpin-right");
		  else
			Player.instance().animations().play("raider-idle-swordSpin-left");
	  });
	  
	  Input.mouse().onPressed(e -> {//normal attack
		  if (Player.instance().getState() == PlayerState.LOCKED || Player.instance().isDead()) { // || !Player.instance().getEquipped()
		    return;
		  }
		  
		  Player.instance().getMeleeAttack().cast();
		  
		  Direction attack = Player.instance().calcAttackDirection();
		  Direction facing = Player.instance().calcFacingDirection();
		  
		  if(attack.equals(Direction.RIGHT)) {
			  Player.instance().animations().play("raider-idle-swordMeleeSide-right");
		  }else if(attack.equals(Direction.LEFT)) {
			  Player.instance().animations().play("raider-idle-swordMeleeSide-left");
		  }else if(attack.equals(Direction.UP)) {
			  if(facing.equals(Direction.RIGHT)) Player.instance().animations().play("raider-idle-swordMeleeUp-right");
			  else Player.instance().animations().play("raider-idle-swordMeleeUp-left");
		  }else {
			  if(facing.equals(Direction.RIGHT)) Player.instance().animations().play("raider-idle-swordMeleeDown-right");
			  else Player.instance().animations().play("raider-idle-swordMeleeDown-left");
		  }
		  
		  });
	  
	  Input.keyboard().onKeyPressed(KeyEvent.VK_E, e -> {
		  if (Player.instance().getState() == PlayerState.LOCKED || Player.instance().isDead()) {
		    return;
		  }
		  
		  if(RaidersLogic.isInTransitionsArea()) {
			  RaidersLogic.transition("boss1");
		  }
		  
		  if(RaidersLogic.isInChestArea() == 1) {
			  Player.instance().gotItem("sword");
			  chestActive.set(0, false);
		  }
		  
		  if(RaidersLogic.isInChestArea() == 2) {
			  Player.instance().gotItem("swordPurple");
			  chestActive.set(1, false);
		  }
		  
		  if(RaidersLogic.isInChestArea() == 3) {
			  Player.instance().getHitPoints().setToMax();
			  chestActive.set(2, false);
		  }
	  });
  	}
}

