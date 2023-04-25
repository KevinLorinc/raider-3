package raider;

import java.awt.event.KeyEvent;

import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.graphics.Spritesheet;
import de.gurkenlabs.litiengine.graphics.animation.Animation;
import de.gurkenlabs.litiengine.input.Input;
import de.gurkenlabs.litiengine.resources.Resources;
import raider.RaidersLogic.GameState;
import entities.Player;
import entities.Player.PlayerState;

/**
 * a class that manages player input. Will eventually control all input such as when attacks are to be cast
 * @author Kevin Lorinc
 *
 */
public class PlayerInput {
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

	      if (RaidersLogic.getState() == GameState.INGAME_MENU) {
	        RaidersLogic.setState(GameState.INGAME);
	      } else if (RaidersLogic.getState() == GameState.INGAME) {
	        RaidersLogic.setState(GameState.INGAME_MENU);
	      }
	  });
	  
	  Input.keyboard().onKeyPressed(KeyEvent.VK_SPACE, e -> {
		  if (Player.instance().getState() == PlayerState.LOCKED || Player.instance().isDead()) {
		    return;
		  }
		  
		  Player.instance().getSpinAttack().cast();
		  if(Player.instance().calcDirection().equals(Direction.RIGHT))
			Player.instance().animations().play("raider-idleSwordAttack-right");
		  else
			Player.instance().animations().play("raider-idleSwordAttack-left");
	  });
	  
	  Input.keyboard().onKeyPressed(KeyEvent.VK_F, e -> {
		  if (Player.instance().getState() == PlayerState.LOCKED || Player.instance().isDead()) {
		    return;
		  }
		  
		  Player.instance().getMeleeAttack().cast();
		  long random = Math.round(Math.random());
		  if(random==1)
			  if(Player.instance().calcDirection().equals(Direction.RIGHT))
		  	Player.instance().animations().play("raider-walkSwordAttack1-right");
			  else
			Player.instance().animations().play("raider-walkSwordAttack1-left");

		  else
			  if(Player.instance().calcDirection().equals(Direction.RIGHT))
			Player.instance().animations().play("raider-walkSwordAttack2-right");
			  else
			Player.instance().animations().play("raider-walkSwordAttack2-left");	  
		  });
	  
	  //used to equip a weapon
	  Input.keyboard().onKeyPressed(KeyEvent.VK_Q, e -> {
		  if (Player.instance().getState() == PlayerState.LOCKED || Player.instance().isDead()) {
		    return;
		  }
		  
		  Player.instance().setEquipped(!Player.instance().getEquipped());
	  });
  	}
}

