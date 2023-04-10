package ui;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.gui.screens.GameScreen;

//Not sure whether the initialize componenets and render function are implicitly called when I add this screen but ig we'll see

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
		  
		  //this is where we will deal with ingame menu's and death menu's
		  
		  this.getComponents().add(this.hud);
	  }
	  
	  //we can override the render function here to add cinematic clips if we wish
	  
	  //can use this for audio later on. I need it for this class to compile for now
	  @Override
	  public void update() {};
}
