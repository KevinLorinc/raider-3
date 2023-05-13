package ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.*;

import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.gui.GuiComponent;
import entities.Enemy;
import entities.Enemy.EnemyState;
import entities.Minion;
import entities.Player;
import entities.Player.PlayerState;
import raider.RaidersMath;

/**
 * a class that creates the Hud for the game. This will have arrows, probably inventory, and health related things in it.
 * This will have some animation controllers in it
 * @author Kevin Lorinc
 *
 */
public class Hud extends GuiComponent{
	//private static Direction dir;
	
	/**
	 * creates an instance of the Hud class
	 */
	
	private int slot = 0;
	
	protected Hud() {
		super(0, 0, Game.window().getResolution().getWidth(), Game.window().getResolution().getHeight());
		//dir = Direction.UP;
		//add amination controllers here
	}

	/**
	 * renders all the components of the hud
	 */
	@Override
	public void render(Graphics2D g){
		
		super.render(g);
		
		this.renderHP(g);
		try {
			this.renderEnemyHP(g);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.renderInventory(g);
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
		        g.setColor(Color.BLACK);
		        Game.graphics().renderShape(g, rect);

		        g.setColor(new Color(228, 59, 68));
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
	
	private void renderInventory(Graphics2D g) {
		final double width = 30;
		final double height = 30;
		double x = Game.world().camera().getViewport().getMinX() + 20;
		double y = Game.world().camera().getViewport().getMaxY() - 40;
		
		RoundRectangle2D highlight = new RoundRectangle2D.Double(x-2.5+slot*40, y-2.5,width+5, height+5, 1.5, 1.5);
		Game.graphics().renderShape(g, highlight);
		
		for (int i =0;i<4;i++) {
			RoundRectangle2D rect = new RoundRectangle2D.Double(x + (i*40), y, width, height, 1.5, 1.5);
			g.setColor(Color.LIGHT_GRAY);
			Game.graphics().renderShape(g, rect);
		}
	}
	
	public void setSlot(int newSlot) {
		slot = newSlot;
	} 
	
	public int getSlot() {
		return slot;
	}
}