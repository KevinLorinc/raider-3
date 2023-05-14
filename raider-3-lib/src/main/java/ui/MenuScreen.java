package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.gui.ImageComponent;
import de.gurkenlabs.litiengine.gui.Menu;
import de.gurkenlabs.litiengine.gui.screens.GameScreen;
import de.gurkenlabs.litiengine.input.Input;
import de.gurkenlabs.litiengine.resources.Resources;
import raider.RaidersLogic;

/**
 * creates a class for menu screen and initializes that screen. Allows for better screen management
 * @author Kevin Lorinc
 *
 */
public class MenuScreen extends GameScreen implements IUpdateable{
	  public static final String NAME = "MENU-SCREEN";
	  
	  private static final BufferedImage title1 = Resources.images().get("images/raiderLogo.png");
      private static final Image title = title1.getScaledInstance((int)(title1.getWidth()), (int)(title1.getHeight()), Image.SCALE_DEFAULT);
	  
	  private Menu mainMenu;
	  
	  /**
	   * creates an menu sreen with the name MENU-SCREEN
	   */
	  public MenuScreen() {
	    super(NAME);
	  }
	  
	  /**
	   * adds this to the game loop and prepares it
	   */
	  @Override
	  public void prepare() {
	    super.prepare();
	    Game.loop().attach(this);
	    
	    Game.window().getRenderComponent().setBackground(Color.BLACK);
	    //Game.window().getRenderComponent().
	    
	    
	    this.mainMenu.setForwardMouseEvents(false);
	    this.mainMenu.getCellComponents().forEach(comp -> {
	    try {
	    	System.out.println(Font.createFont(Font.TRUETYPE_FONT, new File("misc/gameFont.ttf")));
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

	  /**
	   * supspends and removes from game loop
	   */
	  @Override
	  public void suspend() {
	    super.suspend();
	    Game.loop().detach(this);
	  }
	  
	  /**
	   * adds more compenents to the screen
	   */
	  @Override
	  protected void initializeComponents() {
		    super.initializeComponents();
		    final double centerX = Game.window().getResolution().getWidth() / 2.0;
		    final double centerY = Game.window().getResolution().getHeight() * 1 / 2;
		    final double buttonWidth = 450;

		    this.mainMenu = new Menu(centerX - buttonWidth / 2, centerY * 1.3, buttonWidth, centerY / 2, "Play", "Exit");

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
		    	if(nextSelection > 2) nextSelection = 2;
		        this.mainMenu.setCurrentSelection(nextSelection);
		        for (ImageComponent comp : this.mainMenu.getCellComponents()) {
		          comp.setHovered(false);
		        }
		        this.mainMenu.getCellComponents().get(this.mainMenu.getCurrentSelection()).setHovered(true);
		      }

		      if (event.getKeyCode() == KeyEvent.VK_ENTER || event.getKeyCode() == KeyEvent.VK_SPACE) {
		        switch (this.mainMenu.getCurrentSelection()) {
		          case 0 -> this.startGame();
		          case 1 -> System.exit(0);
		        }

		      }
		    });

		    this.getComponents().add(this.mainMenu);
	  }
	  
	  /**
	   * code for when the start button is clicked
	   */
	  private void startGame() {
		    this.mainMenu.setEnabled(false);
		    Game.window().getRenderComponent().fadeOut(1500);

		    Game.loop().perform(1500, () -> {
		      Game.window().getRenderComponent().fadeIn(1500);
		      Game.screens().display("INGAME-SCREEN");
		      RaidersLogic.onPlay();
		    });
      }
	  
	  //we can override the render function here
	  
	  //can use this for audio later on. I need it for this class to compile for now
	  @Override
	  public void update() {};
}