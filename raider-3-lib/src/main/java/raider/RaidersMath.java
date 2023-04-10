package raider;

import java.awt.Point;
import java.awt.geom.Point2D;

import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.Game;
import entities.Player;

public class RaidersMath {
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
		if(mouseLoc.getX() < playerLoc.getX()) {
			if(mouseLoc.getY() <= heightOfWidth(-1,propConst,mouseLoc.getX(),playerX,playerY)) {
				if(mouseLoc.getY() >= heightOfWidth(1,propConst,mouseLoc.getX(),playerX,playerY)) {
					return Direction.LEFT;
				}
				return Direction.DOWN;
			}
			return Direction.DOWN;
		}
		//checks right region of the screen
		if(mouseLoc.getX() > playerLoc.getX()) {
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
	
	private static double heightOfWidth(int indicator,double n,double mouseX, double playerX, double playerY) {
		if(indicator == -1)
			return (n) * (mouseX);
		else
			return (2*playerY) - (n * mouseX);
	}
	
	public static double getRenderScale(double w, double h) {
		return w/Game.world().camera().getViewport().getWidth();
	}
}
