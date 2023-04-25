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
			return (n) * (mouseX) + 8;
		else 
			return (2*playerY) - (n * mouseX) + 8;
	}
	
	public static double getRenderScale(double w, double h) {
		return w/Game.world().camera().getViewport().getWidth();
	}
	
	public static Direction getAttackDirection(Point mouseLoc, Point2D playerLoc) {
		int width = Game.window().getWidth();
		int height = Game.window().getHeight();
		double renderScale = getRenderScale(width,height);
		double playerX = (playerLoc.getX()+(Player.instance().getWidth()/2)) * renderScale;
		double playerY = (playerLoc.getY()+(Player.instance().getHeight()/2)+10) * renderScale;
		double propConst = playerY/playerX;
		
		//left side
		if(mouseLoc.getX() < playerX) {
			if(mouseLoc.getY() >= heightOfWidth(-1,propConst,mouseLoc.getX(),playerX,playerY)) {
				if(mouseLoc.getY() >= heightOfWidth(1,propConst,mouseLoc.getX(),playerX,playerY)) {
					return Direction.DOWN;
				}
				return Direction.LEFT;
			}
			return Direction.UP;
		}
		
		//checks right side
		if(mouseLoc.getX() > playerX) {
			if(mouseLoc.getY() <= heightOfWidth(-1,propConst,mouseLoc.getX(),playerX,playerY)) {
				if(mouseLoc.getY() <= heightOfWidth(1,propConst,mouseLoc.getX(),playerX,playerY)) {
					return Direction.UP;
				}
				return Direction.RIGHT;
			}
			return Direction.DOWN;
		}
		return null;
	}
	
	public void update() {
		//System.out.println()
	}
}
