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
		  if(!Player.instance().getEquipped().equals("fist"))
			  Player.instance().getSpinAttack().cast();
		  
		  Direction facing = Player.instance().calcFacingDirection();
		  String equipped = Player.instance().getEquipped();
		  
		if(equipped.equals("sword"))  
		  if(facing.equals(Direction.RIGHT))
			Player.instance().animations().play("raider-idle-swordSpin-right");
		  else
			Player.instance().animations().play("raider-idle-swordSpin-left");
		else if(equipped.equals("swordPurple")){
			if(facing.equals(Direction.RIGHT))
				Player.instance().animations().play("raider-idle-swordPurpleSpin-right");
			  else
				Player.instance().animations().play("raider-idle-swordPurpleSpin-left");
		}else if(equipped.equals("swordBlue")) {
			if(facing.equals(Direction.RIGHT))
				Player.instance().animations().play("raider-idle-swordBlueSpin-right");
			  else
				Player.instance().animations().play("raider-idle-swordBlueSpin-left");
		}
	  });
	  
	  Input.mouse().onPressed(e -> {//normal attack
		  if (Player.instance().getState() == PlayerState.LOCKED || Player.instance().isDead()) { // || !Player.instance().getEquipped()
		    return;
		  }
		  Direction attack = Player.instance().calcAttackDirection();
		  Direction facing = Player.instance().calcFacingDirection();
		  
		  System.out.println(attack);
		  System.out.println(Player.instance().getEquipped());
		  
		  Player.instance().getMeleeAttack().cast();
		  
		  if(Player.instance().getEquipped().equals("fist")) {
			  if(facing.equals(Direction.RIGHT)) {
				  Player.instance().animations().play("raider-idle-punch-right");
			  }else if(facing.equals(Direction.LEFT))
				  Player.instance().animations().play("raider-idle-punch-left");
		  
		  }else if(Player.instance().getEquipped().equals("sword")) {
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
		  
		  }else if(Player.instance().getEquipped().equals("swordPurple")) {
			  if(attack.equals(Direction.RIGHT)) {
				  Player.instance().animations().play("raider-idle-swordPurpleMeleeSide-right");
			  }else if(attack.equals(Direction.LEFT)) {
				  Player.instance().animations().play("raider-idle-swordPurpleMeleeSide-left");
			  }else if(attack.equals(Direction.UP)) {
				  if(facing.equals(Direction.RIGHT)) Player.instance().animations().play("raider-idle-swordPurpleMeleeUp-right");
				  else Player.instance().animations().play("raider-idle-swordPurpleMeleeUp-left");
			  }else {
				  if(facing.equals(Direction.RIGHT)) Player.instance().animations().play("raider-idle-swordPurpleMeleeDown-right");
				  else Player.instance().animations().play("raider-idle-swordPurpleMeleeDown-left");
			  }
		  
		  }else if(Player.instance().getEquipped().equals("swordBlue"))
			  if(attack.equals(Direction.RIGHT)) {
				  Player.instance().animations().play("raider-idle-swordBlueMeleeSide-right");
			  }else if(attack.equals(Direction.LEFT)) {
				  Player.instance().animations().play("raider-idle-swordBlueMeleeSide-left");
			  }else if(attack.equals(Direction.UP)) {
				  if(facing.equals(Direction.RIGHT)) Player.instance().animations().play("raider-idle-swordBlueMeleeUp-right");
				  else Player.instance().animations().play("raider-idle-swordBlueMeleeUp-left");
			  }else {
				  if(facing.equals(Direction.RIGHT)) Player.instance().animations().play("raider-idle-swordBlueMeleeDown-right");
				  else Player.instance().animations().play("raider-idle-swordBlueMeleeDown-left");
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

