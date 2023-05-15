package ui;

import java.awt.event.KeyEvent;
import java.util.HashMap;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.gui.screens.GameScreen;
import de.gurkenlabs.litiengine.input.Input;
import entities.Player;
import entities.Player.PlayerState;

/**
 * creates a class for ingame screen and initializes that screen. Allows for better screen management
 * @author Kevin Lorinc
 *
 */
public class InGameScreen extends GameScreen implements IUpdateable{
	  public static final String NAME = "INGAME-SCREEN";
	  
	  private Hud hud;
	  
	  /**
	   * creates an ingame sreen with the name INGAME_SCREEN
	   */
	  public InGameScreen() {
	    super(NAME);
	  }
	  
	  @Override
	  public void prepare() {
	    super.prepare();
	    Game.loop().attach(this);

	  }

	  @Override
	  public void suspend() {
	    super.suspend();
	    Game.loop().detach(this);
	  }
	  
	  @Override
	  protected void initializeComponents() {
		  this.hud = new Hud();
		  
		  Input.keyboard().onKeyReleased(KeyEvent.VK_Q, e -> {
			  if (Player.instance().getState() == PlayerState.LOCKED || Player.instance().isDead()) {
			    return;
			  }
			  HashMap<String,Boolean> inventory = Player.instance().getInventory();
			  
			  if(hud.getSlot()<3) hud.setSlot(hud.getSlot() + 1);
			  else hud.setSlot(0);
			  
			  switch(hud.getSlot() + 1) {
			  	case 1: if(inventory.get("fist")) Player.instance().setEquipped("fist");
			  			break;
			  	case 2: if(inventory.get("sword")) Player.instance().setEquipped("sword");
			  			else Player.instance().setEquipped("fist");
			  			break;
			  	case 3: if(inventory.get("swordPurple")) Player.instance().setEquipped("swordPurple");
	  				    else Player.instance().setEquipped("fist");
	  					break;
			  	case 4: if(inventory.get("swordBlue")) Player.instance().setEquipped("swordBlue");
				        else Player.instance().setEquipped("fist");
					    break;
			  }
		  });
		  
		  this.getComponents().add(this.hud);
	  }
	  
	  //we can override the render function here to add cinematic clips if we wish
	  
	  //can use this for audio later on. I need it for this class to compile for now
	  @Override
	  public void update(){};
}
