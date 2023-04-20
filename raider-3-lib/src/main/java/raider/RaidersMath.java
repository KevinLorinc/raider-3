package raider;

import java.awt.Point;
import java.awt.geom.Point2D;

import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import entities.Player;

public class RaidersMath implements IUpdateable{
	/**
	 * takes a mouse location and compares it to the location of player
	 * @param mouseLoc the present location of the mouse
	 * @param playerLoc the present location of the player
	 * @return the relative location of the mouse to the player
	 */
	public static Direction getLeftOrRight(Point mouseLoc, Point2D playerLoc) {
		int width = Game.window().getWidth();
		int height = Game.window().getHeight();
		double playerX = (playerLoc.getX()+(Player.instance().getWidth()/2)) * getRenderScale(width,height);
		
		//checks left region of the screen
		if(mouseLoc.getX() < playerX) {
			return Direction.LEFT;
		}
		//checks right region of the screen
		if(mouseLoc.getX() > playerX) {
			return Direction.RIGHT;
		}
		
		return null;
	}
	
	private static double heightOfWidth(int indicator,double n,double mouseX, double playerX, double playerY) {
		if(indicator == -1)
			return (n) * (mouseX);
		else
			return (2*playerY) - (n * mouseX);
	}
	
	public static double getRenderScale(double w, double h) {
		//System.out.println("Renderscale: " + w/Game.world().camera().getViewport().getWidth()+" "+ h/Game.world().camera().getViewport().getHeight());
		return w/Game.world().camera().getViewport().getWidth();
	}
	
	public static Direction getMouseDirection(Point mouseLoc, Point2D playerLoc) {
		int width = Game.window().getWidth();
		int height = Game.window().getHeight();
		System.out.println(width + "  " + height);
		double playerX = (playerLoc.getX()+(Player.instance().getWidth()/2)) * getRenderScale(width,height);
		double playerY = (playerLoc.getY()+(Player.instance().getHeight()/2)+10) * getRenderScale(width,height);
		double propConst = playerY/playerX;
		System.out.println("Mouse:" + mouseLoc + "  Player:" + playerX + " " + playerY);
		//System.out.println(Player.instance().getLocation());
		//System.out.println(Game.world().camera().getViewport());
		
		//checks left region of the screen
		if(mouseLoc.getX() < playerX) {
			if(mouseLoc.getY() <= heightOfWidth(-1,propConst,mouseLoc.getX(),playerX,playerY)) {
				if(mouseLoc.getY() >= heightOfWidth(1,propConst,mouseLoc.getX(),playerX,playerY)) {
					return Direction.LEFT;
				}
				return Direction.DOWN;
			}
			return Direction.UP;
		}
		//checks right region of the screen
		if(mouseLoc.getX() > playerX) {
			if(mouseLoc.getY() >= heightOfWidth(-1,propConst,mouseLoc.getX(),playerX,playerY)) {
				if(mouseLoc.getY() <= heightOfWidth(1,propConst,mouseLoc.getX(),playerX,playerY)) {
					return Direction.RIGHT;
				}
				return Direction.DOWN;
			}
			return Direction.UP;
		}
		
		return null;
	}
	
	public void update() {
		//System.out.println()
	}
}
