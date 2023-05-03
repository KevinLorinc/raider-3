package entities;

import java.awt.Color;

import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.graphics.CreatureShadowImageEffect;
import de.gurkenlabs.litiengine.graphics.Spritesheet;
import de.gurkenlabs.litiengine.graphics.animation.Animation;
import de.gurkenlabs.litiengine.graphics.animation.CreatureAnimationController;
import de.gurkenlabs.litiengine.resources.Resources;

public class MinionAnimationController extends CreatureAnimationController<Minion>{
	public MinionAnimationController(Minion minion,Animation defaultAnim) {
		super(minion,defaultAnim);
		
		//Spritesheet idle = Resources.spritesheets().get("minion-idle-right");
    	Spritesheet walkR = Resources.spritesheets().get("minion-walk-right");
    	Spritesheet walkL = Resources.spritesheets().get("minion-walk-left");
    	Spritesheet damagedR = Resources.spritesheets().get("minion-damaged-right");
    	Spritesheet damagedL = Resources.spritesheets().get("minion-damaged-left");
    	Spritesheet attackR = Resources.spritesheets().get("minion-attack-right");
    	Spritesheet attackL = Resources.spritesheets().get("minion-attack-left");
    	
    	this.add(new Animation(walkR,false));
    	this.add(new Animation(walkL,false));
    	this.add(new Animation(damagedR,false));
    	this.add(new Animation(damagedL,false));
    	this.add(new Animation(attackR,false));
    	this.add(new Animation(attackL,false));
    	
    	this.addRule(x -> (minion.getFacingDirection() == Direction.RIGHT) && !minion.isIdle(), x -> "minion-walk-right");
    	this.addRule(x -> (minion.getFacingDirection() == Direction.LEFT) && !minion.isIdle(), x -> "minion-walk-left");
    	
    	CreatureShadowImageEffect effect = new CreatureShadowImageEffect(minion, new Color(24, 30, 28, 100));
	    effect.setOffsetY(1);
	    this.add(effect);
	}
}