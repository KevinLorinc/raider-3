package ui;

import java.awt.event.KeyEvent;

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
			  
			  if(hud.getSlot()<3) hud.setSlot(hud.getSlot() + 1);
			  else hud.setSlot(0);
			  
			  Player.instance().setEquipped(!Player.instance().getEquipped());//ig we can leave this here for now
		  });
		  
		  this.getComponents().add(this.hud);
	  }
	  
	  //we can override the render function here to add cinematic clips if we wish
	  
	  //can use this for audio later on. I need it for this class to compile for now
	  @Override
	  public void update(){};
}
