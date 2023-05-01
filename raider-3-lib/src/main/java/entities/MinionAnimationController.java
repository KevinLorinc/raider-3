package entities;

import java.awt.Color;

import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.entities.behavior.EntityNavigator;
import de.gurkenlabs.litiengine.graphics.CreatureShadowImageEffect;
import de.gurkenlabs.litiengine.graphics.Spritesheet;
import de.gurkenlabs.litiengine.graphics.animation.Animation;
import de.gurkenlabs.litiengine.graphics.animation.CreatureAnimationController;
import de.gurkenlabs.litiengine.graphics.animation.IEntityAnimationController;
import de.gurkenlabs.litiengine.resources.Resources;

public class MinionAnimationController extends CreatureAnimationController<Minion>{
	public MinionAnimationController(Minion minion,Animation defaultAnim) {
		super(minion,defaultAnim);
		
		//Spritesheet idle = Resources.spritesheets().get("minion-idle-right");
    	Spritesheet walk = Resources.spritesheets().get("minion-walk-right");
    	
    	
    	this.add(new Animation(walk,true));
    	
    	this.addRule(x -> (minion.getFacingDirection() == Direction.RIGHT) && !minion.isIdle(), x -> "minion-walk-right");
    	this.addRule(x -> (minion.getFacingDirection() == Direction.LEFT) && !minion.isIdle(), x -> "minion-walk-left");
    	
    	CreatureShadowImageEffect effect = new CreatureShadowImageEffect(minion, new Color(24, 30, 28, 100));
	    effect.setOffsetY(1);
	    this.add(effect);
	}
}
