package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.Spawnpoint;
import de.gurkenlabs.litiengine.graphics.Camera;
import de.gurkenlabs.litiengine.graphics.PositionLockCamera;
import de.gurkenlabs.litiengine.gui.ImageComponent;
import de.gurkenlabs.litiengine.gui.Menu;
import de.gurkenlabs.litiengine.gui.screens.GameScreen;
import de.gurkenlabs.litiengine.input.Input;
import entities.Player;
import entities.Player.PlayerState;
import raider.PlayerInput;
import raider.RaidersLogic;
import raider.RaidersLogic.GameState;

/**
 * creates a class for win screen and initializes that screen. Allows for better screen management
 * @author Kevin Lorinc
 *
 */
public class WinScreen extends GameScreen implements IUpdateable{
	  public static final String NAME = "WIN-SCREEN";
	  
	  private Menu mainMenu;
	  
	  /**
	   * creates an menu sreen with the name WIN-SCREEN
	   */
	  public WinScreen() {
	    super(NAME);

	  }
	  
	  /**
	   * adds this to the game loop and prepares it
	   */
	  @Override
	  public void prepare() {
	    super.prepare();
	    Game.loop().attach(this);
	    Game.screens().remove(Game.screens().get("MENU-SCREEN"));
	    
	    if(this.isEnabled()) {
	    	Game.window().getRenderComponent().setBackground(new Color(20,20,20));
		    
		    
		    this.mainMenu.setForwardMouseEvents(false);
		    this.mainMenu.getCellComponents().forEach(comp -> {
		    try {
				comp.setFont(Font.createFont(Font.TRUETYPE_FONT, new File("misc/gameFont.ttf")));
				comp.setFontSize(50);
			} catch (FontFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		      comp.setTextAntialiasing(true);
		      comp.setForwardMouseEvents(false);
		    });

		    this.mainMenu.setEnabled(true);
		    this.mainMenu.getCellComponents().get(0).setHovered(true);
	    }
	  }

	  /**
	   * supspends and removes from game loop
	   */
	  @Override
	  public void suspend() {
	    super.suspend();
	    Game.loop().detach(this);
	  }
	
	  /**
	   * adds various components to this screen
	   */
	  @Override
	  protected void initializeComponents() {
		  if(this.isEnabled()) {
			  super.initializeComponents();
			    final double centerX = Game.window().getResolution().getWidth() / 2.0;
			    final double centerY = Game.window().getResolution().getHeight() * 1 / 2;
			    final double buttonWidth = 450;

			    this.mainMenu = new Menu(centerX - buttonWidth / 2, centerY * 1.3, buttonWidth, centerY / 2, "Play Again", "Exit");

			    Input.keyboard().onKeyReleased(event -> {
			      if (this.isSuspended()) {
			        return;
			      }

			      if (event.getKeyCode() == KeyEvent.VK_UP || event.getKeyCode() == KeyEvent.VK_W) {
			        this.mainMenu.setCurrentSelection(Math.max(0, this.mainMenu.getCurrentSelection() - 1));
			        for (ImageComponent comp : this.mainMenu.getCellComponents()) {
			          comp.setHovered(false);
			        }
			        this.mainMenu.getCellComponents().get(this.mainMenu.getCurrentSelection()).setHovered(true);
			      }

			      if (event.getKeyCode() == KeyEvent.VK_DOWN || event.getKeyCode() == KeyEvent.VK_S) {
			    	int nextSelection = this.mainMenu.getCurrentSelection() + 1;
			    	if(nextSelection > 1) nextSelection = 1;
			        this.mainMenu.setCurrentSelection(nextSelection);
			        for (ImageComponent comp : this.mainMenu.getCellComponents()) {
			          comp.setHovered(false);
			        }
			        this.mainMenu.getCellComponents().get(this.mainMenu.getCurrentSelection()).setHovered(true);
			      }

			      if (event.getKeyCode() == KeyEvent.VK_ENTER) {
			        switch (this.mainMenu.getCurrentSelection()) {
			          case 0 -> this.restartGame();
			          case 1 -> System.exit(0);
			        }

			      }
			    });

			    this.getComponents().add(this.mainMenu);
			    
			    try {
					BufferedImage title1 = null;
					while(title1 == null) {
						title1 = ImageIO.read(new File("images/winImage.png"));
					}
					int w = title1.getWidth();
					int h = title1.getHeight();
					int width = Game.window().getWidth();
					int height = Game.window().getHeight();
					//double renderScale = RaidersMath.getRenderScale(width,height);
					BufferedImage title = new BufferedImage((int)(width*.4), (int)(height*.4), BufferedImage.TYPE_INT_ARGB);
					AffineTransform at = new AffineTransform();
					at.scale((width*.4)/w, (height*.4)/h);
					AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
					title = scaleOp.filter(title1, title);
				    this.getComponents().add(new ImageComponent(520,150,title));
				} catch (IOException e) {
					e.printStackTrace();
				}    
		  }
	  }
	  
	  /**
	   * code for when the play again button is clicked
	   */
	  private void restartGame() {
		  Player.instance().setInstanceNull();
		  this.mainMenu.setEnabled(false);	    
		    
		    Game.loop().perform(3000, () -> {
		       Game.window().getRenderComponent().fadeIn(1500);
			   Game.world().loadEnvironment("tutorial.tmx");
		       Camera camera = new PositionLockCamera(Player.instance());
			   camera.setClampToMap(true);
			   Game.world().setCamera(camera);
			   Game.window().getRenderComponent().fadeIn(1500);
			   Game.world().camera().setFocus(Game.world().environment().getCenter());
			   RaidersLogic.resetSpawns();
			    Spawnpoint spawn = Game.world().environment().getSpawnpoint("enter");
		        if (spawn != null) {
		          RaidersLogic.setState(GameState.INGAME);
		          spawn.spawn(Player.instance());
		        }
			    Player.instance().getHitPoints().setToMax();
			    Player.instance().setIndestructible(false);
		        Player.instance().setCollision(true);
			    Player.instance().setState(PlayerState.CONTROLLABLE);
			    Player.instance().gotItem("swordBlue");
			    
			    for(int i = 0;i<PlayerInput.chestActive.size();i++) {
			    	PlayerInput.chestActive.set(i, true);
			    }
			    	
		    }); 
			Game.screens().remove(Game.screens().get("WIN-SCREEN"));
      }
	
	/**
	 * updates every frame, for testing purposes
	 */
	@Override
	public void update() {
	}

}