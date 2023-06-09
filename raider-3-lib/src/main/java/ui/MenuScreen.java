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
import de.gurkenlabs.litiengine.gui.ImageComponent;
import de.gurkenlabs.litiengine.gui.Menu;
import de.gurkenlabs.litiengine.gui.TextFieldComponent;
import de.gurkenlabs.litiengine.gui.screens.GameScreen;
import de.gurkenlabs.litiengine.input.Input;
import raider.RaidersLogic;

/**
 * creates a class for menu screen and initializes that screen. Allows for better screen management
 * @author Kevin Lorinc
 *
 */
public class MenuScreen extends GameScreen implements IUpdateable{
	  public static final String NAME = "MENU-SCREEN";
	  
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
		    	if(nextSelection > 1) nextSelection = 1;
		        this.mainMenu.setCurrentSelection(nextSelection);
		        for (ImageComponent comp : this.mainMenu.getCellComponents()) {
		          comp.setHovered(false);
		        }
		        this.mainMenu.getCellComponents().get(this.mainMenu.getCurrentSelection()).setHovered(true);
		      }

		      if (event.getKeyCode() == KeyEvent.VK_ENTER) {
		        switch (this.mainMenu.getCurrentSelection()) {
		          case 0 -> this.startGame();
		          case 1 -> System.exit(0);
		        }

		      }
		    });

		    this.getComponents().add(this.mainMenu);
		    TextFieldComponent instructions = new TextFieldComponent(Game.window().getWidth()/3.2, Game.window().getHeight()-100,800,100,"Use Arrow Keys Or WASD to navigate and Enter to select on Menu");
		    try {
				Font gameFont = Font.createFont(Font.TRUETYPE_FONT, new File("misc/gameFont.ttf"));
				instructions.setFont(gameFont.deriveFont(Font.TRUETYPE_FONT,20));
				this.getComponents().add(instructions);
			} catch (FontFormatException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		    
		    
			try {
				BufferedImage title1 = null;
				while(title1 == null) {
					title1 = ImageIO.read(new File("images/raiderLogo.png"));
				}
				int w = title1.getWidth();
				int h = title1.getHeight();
				int width = Game.window().getWidth();
				int height = Game.window().getHeight();
				//double renderScale = RaidersMath.getRenderScale(width,height);
				BufferedImage title = new BufferedImage((int)(width*.8), (int)(height*.75), BufferedImage.TYPE_INT_ARGB);
				AffineTransform at = new AffineTransform();
				at.scale((width*.8)/w, (height*.75)/h);
				AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
				title = scaleOp.filter(title1, title);
			    this.getComponents().add(new ImageComponent(190,50,title));
			} catch (IOException e) {
				e.printStackTrace();
			}    
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
	  /**
	   * updates every frame, for testing purposes
	   */
	  public void update() {};
}