package raider;

import java.awt.event.KeyEvent;

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
		  
		  Player.instance().getMeleeAttack().cast();
		  Player.instance().animations().play("raider-idleSwordAttack-right");
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

