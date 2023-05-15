package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import javax.imageio.*;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.gui.GuiComponent;
import de.gurkenlabs.litiengine.resources.Resources;
import entities.Enemy;
import entities.Enemy.EnemyState;
import entities.Player;
import entities.Player.PlayerState;
import raider.PlayerInput;
import raider.RaidersLogic;
import raider.RaidersMath;

/**
 * a class that creates the Hud for the game. This will have arrows, probably inventory, and health related things in it.
 * This will have some animation controllers in it
 * @author Kevin Lorinc
 *
 */
public class Hud extends GuiComponent{
	private static final BufferedImage leftClick1 = Resources.images().get("images/leftClickIcon.png");
	private static final Image leftClick = leftClick1.getScaledInstance((int)(leftClick1.getWidth() * .15), (int)(leftClick1.getHeight() * .15), Image.SCALE_DEFAULT);
	private static final BufferedImage space1 = Resources.images().get("images/spaceBarIcon.png");
	private static final Image space= space1.getScaledInstance((int)(space1.getWidth() * .15), (int)(space1.getHeight() * .15), Image.SCALE_DEFAULT);
	private static final BufferedImage wasd1 = Resources.images().get("images/wasdIcon.png");
	private static final Image wasd = wasd1.getScaledInstance((int)(wasd1.getWidth()*.1), (int)(wasd1.getHeight()*.1 ), Image.SCALE_DEFAULT);
	private static final BufferedImage q1 = Resources.images().get("images/qIcon.png");
	private static final Image q = q1.getScaledInstance((int)(q1.getWidth()*.04), (int)(q1.getHeight()*.04), Image.SCALE_DEFAULT);
	private static final LinkedList<Image> inventoryIcons = new LinkedList<Image>();
	
	static {
		BufferedImage fist1 = Resources.images().get("images/fistIcon.png");
		Image fist = fist1.getScaledInstance((int)(fist1.getWidth()*2.5), (int)(fist1.getHeight()*2.5), Image.SCALE_DEFAULT);
		
		BufferedImage sword1 = Resources.images().get("images/whiteSwordIcon.png");
		Image sword = sword1.getScaledInstance((int)(sword1.getWidth()*2.5), (int)(sword1.getHeight()*2.5), Image.SCALE_DEFAULT);
		
		BufferedImage swordPurple1 = Resources.images().get("images/purpleSwordIcon.png");//doesn't always work
		Image swordPurple = swordPurple1.getScaledInstance((int)(swordPurple1.getWidth()*2.5), (int)(swordPurple1.getHeight()*2.5), Image.SCALE_DEFAULT);
		
		BufferedImage swordBlue1 = Resources.images().get("images/blueSwordIcon.png");
		Image swordBlue = swordBlue1.getScaledInstance((int)(swordBlue1.getWidth()*2.5), (int)(swordBlue1.getHeight()*2.5), Image.SCALE_DEFAULT);
		
		inventoryIcons.add(fist);
		inventoryIcons.add(sword);
		inventoryIcons.add(swordPurple);
		inventoryIcons.add(swordBlue);
	}
	
	
	/**
	 * creates an instance of the Hud class
	 */
	
	private static int slot = 0;
	
	protected Hud() {
		super(0, 0, Game.window().getResolution().getWidth(), Game.window().getResolution().getHeight());
	}

	/**
	 * renders all the components of the hud
	 */
	@Override
	public void render(Graphics2D g){
		
		super.render(g);
		if(Game.world().environment() != null) {
			this.renderHP(g);
			try {
				this.renderEnemyHP(g);
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.renderInventory(g);
			try {
				this.renderControls(g);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * renders the enemies hp which is situated right above them when they are in combat
	 * @param g the graphic to render to
	 * @throws IOException 
	 */
	private void renderEnemyHP(Graphics2D g) throws IOException {
		for (Enemy enemy : Game.world().environment().getByTag(Enemy.class,"enemy")) {
		    if(enemy.hasTag("boss") && !enemy.isDead()) {
		    	final double width = 200;
		        final double height = 16;
		        double x = Game.world().camera().getViewport().getMinX() + 234;//moved so i can see stats
		        double y = Game.world().camera().getViewport().getMinY() + 20;
		        RoundRectangle2D rect = new RoundRectangle2D.Double(x, y, width, height, 1.5, 1.5);
		        
		        final double currentWidth = width * (enemy.getHitPoints().get() / (double) enemy.getHitPoints().getMax());
		        RoundRectangle2D actualRect = new RoundRectangle2D.Double(x, y, currentWidth, height, 1.5, 1.5);

		        if(enemy.getEnemyState() != EnemyState.ORB && enemy.getEnemyState() != EnemyState.NOTSPAWNED) {
		        g.setColor(Color.LIGHT_GRAY);
		        Game.graphics().renderShape(g, rect);

		        g.setColor(new Color(0,29,61));
				Game.graphics().renderShape(g, actualRect);
				
				
					int swidth = Game.window().getWidth();
					int sheight = Game.window().getHeight();
					double renderScale = RaidersMath.getRenderScale(swidth,sheight);
					BufferedImage border1 = ImageIO.read(new File("images/reaperBossHealth.png"));
					Image border = border1.getScaledInstance((int)(border1.getWidth() * renderScale), (int)(border1.getHeight() * renderScale), Image.SCALE_DEFAULT);
					Game.graphics().renderImage(g, border, x-25,y-9);
				}
		    }
			else if (!enemy.isDead()) {
		        final double width = 16;
		        final double height = 2;
		        double x = enemy.getX() - (width - enemy.getWidth()) / 2.0 - 2;//play around to move enemy health bar
		        double y = enemy.getY() + 32 + height;
		        RoundRectangle2D rect = new RoundRectangle2D.Double(x, y, width, height, 1.5, 1.5);

		        final double currentWidth = width * (enemy.getHitPoints().get()/ (double) enemy.getHitPoints().getMax());
		        RoundRectangle2D actualRect = new RoundRectangle2D.Double(x, y, currentWidth, height, 1.5, 1.5);
		        
		        g.setColor(Color.BLACK);
		        Game.graphics().renderShape(g, rect);

		        g.setColor(new Color(228, 59, 68));
		        Game.graphics().renderShape(g, actualRect);
		   }
		}
		
	}

	/**
	 * renders the players hp
	 * @param g the graphic to render the players hp to
	 */
	private void renderHP(Graphics2D g) {
		if(Player.instance().getState() == PlayerState.CONTROLLABLE) {
			
			final double width = 100;
	        final double height = 8;
	        double x = Game.world().camera().getViewport().getMinX() + 268;//moved so i can see stats
	        double y = Game.world().camera().getViewport().getMaxY() - 15;
	        RoundRectangle2D rect = new RoundRectangle2D.Double(x, y, width, height, 1.5, 1.5);
	        
	        final double currentWidth = width * (Player.instance().getHitPoints().get() / (double) Player.instance().getHitPoints().getMax());
	        RoundRectangle2D actualRect = new RoundRectangle2D.Double(x, y, currentWidth, height, 1.5, 1.5);

	        g.setColor(Color.BLACK);
	        Game.graphics().renderShape(g, rect);

	        g.setColor(new Color(228, 59, 68));
			Game.graphics().renderShape(g, actualRect);
		}
	}
	
	/**
	 * adds the inventory slots
	 * @param g the graphic to add it to
	 */
	private void renderInventory(Graphics2D g) {
		final double width = 30;
		final double height = 30;
		double x = Game.world().camera().getViewport().getMinX() + 20;
		double y = Game.world().camera().getViewport().getMaxY() - 40;
		HashMap<String,Boolean> inventory = Player.instance().getInventory();
		
		RoundRectangle2D highlight = new RoundRectangle2D.Double(x-2.5+slot*40, y-2.5,width+5, height+5, 1.5, 1.5);
		Game.graphics().renderShape(g, highlight);
		
		for (int i =0;i<4;i++) {
			RoundRectangle2D rect = new RoundRectangle2D.Double(x + (i*40), y, width, height, 1.5, 1.5);
			g.setColor(Color.LIGHT_GRAY);
			Game.graphics().renderShape(g, rect);
		}
		
		if(inventory.get("fist"))
			Game.graphics().renderImage(g, inventoryIcons.get(0), x+1, y-3);
		if(inventory.get("sword"))
			Game.graphics().renderImage(g, inventoryIcons.get(1), x+1+40, y);
		if(inventory.get("swordPurple"))
			Game.graphics().renderImage(g, inventoryIcons.get(2), x+1+80, y);
		if(inventory.get("swordBlue"))
			Game.graphics().renderImage(g, inventoryIcons.get(3), x+1+120, y);
	}
	
	private void renderControls(Graphics2D g) throws IOException {
		if(Player.instance().getState() == PlayerState.CONTROLLABLE) {
			try {
				Font gameFont = Font.createFont(Font.TRUETYPE_FONT, new File("misc/gameFont.ttf"));
				g.setFont(gameFont.deriveFont(Font.TRUETYPE_FONT,20));
				
				Game.graphics().renderImage(g, wasd, Game.world().camera().getViewport().getMinX()+ 194,Game.world().camera().getViewport().getMaxY() - 40);
				Game.graphics().renderText(g, "move", Game.world().camera().getViewport().getMinX()+ 174,Game.world().camera().getViewport().getMaxY() - 30);
				
				Game.graphics().renderImage(g, q, Game.world().camera().getViewport().getMinX()+ 199,Game.world().camera().getViewport().getMaxY() - 20);
				Game.graphics().renderText(g, "swap", Game.world().camera().getViewport().getMinX()+ 174,Game.world().camera().getViewport().getMaxY() - 13);
				
				Game.graphics().renderImage(g, leftClick, Game.world().camera().getViewport().getMinX()+ 245,Game.world().camera().getViewport().getMaxY() - 38);
				Game.graphics().renderText(g, "attack", Game.world().camera().getViewport().getMinX()+ 220,Game.world().camera().getViewport().getMaxY() - 30);
				
				Game.graphics().renderImage(g, space, Game.world().camera().getViewport().getMinX()+ 245,Game.world().camera().getViewport().getMaxY() - 20);
				Game.graphics().renderText(g, "spin", Game.world().camera().getViewport().getMinX()+ 223,Game.world().camera().getViewport().getMaxY() - 13);
				
				g.setFont(gameFont.deriveFont(Font.TRUETYPE_FONT,15));
				if(RaidersLogic.isInTransitionsArea()) {
					Game.graphics().renderText(g, "Press 'E' to Enter", Player.instance().getX()-5, Player.instance().getY()+40);
				}
				
				if(RaidersLogic.isInChestArea() != -1) {
					if(PlayerInput.chestActive.get(RaidersLogic.isInChestArea()-1))
						Game.graphics().renderText(g, "Press 'E' to Open", Player.instance().getX()-5, Player.instance().getY()+40);
				}
			} catch (FontFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		
	}
	
	public void setSlot(int newSlot) {
		slot = newSlot;
	} 
	
	public int getSlot() {
		return slot;
	}
}