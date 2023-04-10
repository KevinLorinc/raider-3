package entities;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.CollisionInfo;
import de.gurkenlabs.litiengine.entities.Prop;

/**
 * a class describing the healing potion object
 * @author Kevin Lorinc
 *
 */
@CollisionInfo(collision = false)
public class HealingPotion extends Prop implements IUpdateable {
	/**
	 * creates a healing pot with its spriteSheet
	 * @param spriteSheetName the name of the sprite that goes with the object
	 */
	public HealingPotion(String spriteSheetName){
		super(spriteSheetName);
	}
	
	/**
	 * allows healing potions placed in the environment and for a player to pick it up if he interacts with it.
	 */
	@Override
	  public void update() {
	    if (this.getBoundingBox().intersects(Player.instance().getCollisionBox()) && 
	    	Player.instance().getHitPoints().getRelativeCurrentValue() < Player.instance().getHitPoints().getMax()) {
	    	
	      Game.world().environment().remove(this);
	      Player.instance().getHitPoints().setToMax();
	    }
	  }
}
