package entities;

import java.awt.Color;

import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.graphics.CreatureShadowImageEffect;
import de.gurkenlabs.litiengine.graphics.Spritesheet;
import de.gurkenlabs.litiengine.graphics.animation.Animation;
import de.gurkenlabs.litiengine.graphics.animation.CreatureAnimationController;
import de.gurkenlabs.litiengine.resources.Resources;

public class ReaperAnimationController extends CreatureAnimationController<Reaper>{
	public ReaperAnimationController(Reaper reaper,Animation defaultAnim) {
		super(reaper,defaultAnim);

    	Spritesheet attackR = Resources.spritesheets().get("reaper-attack-right");
    	Spritesheet attackL = Resources.spritesheets().get("reaper-attack-left");
    	Spritesheet death = Resources.spritesheets().get("reaper-death");
    	Spritesheet frenzyR = Resources.spritesheets().get("reaper-frenzy-right");
    	Spritesheet frenzyL = Resources.spritesheets().get("reaper-frenzy-left");
    	Spritesheet laughR = Resources.spritesheets().get("reaper-laugh-right");
    	Spritesheet laughL = Resources.spritesheets().get("reaper-laugh-left");
    	Spritesheet idleR = Resources.spritesheets().get("reaper-idle-right");
    	Spritesheet idleL = Resources.spritesheets().get("reaper-idle-left");
    	Spritesheet phaseR = Resources.spritesheets().get("reaper-phase-right");
    	Spritesheet phaseL = Resources.spritesheets().get("reaper-phase-left");
    	Spritesheet spawn = Resources.spritesheets().get("reaper-spawn");
    	
    	this.add(new Animation(attackR,false));
    	this.add(new Animation(attackL,false));
    	this.add(new Animation(death,false));
    	this.add(new Animation(frenzyR,false));
    	this.add(new Animation(frenzyL,false));
    	this.add(new Animation(laughR,false));
    	this.add(new Animation(laughL,false));
    	this.add(new Animation(idleR,false));
    	this.add(new Animation(idleL,false));
    	this.add(new Animation(phaseR,false));
    	this.add(new Animation(phaseL,false));
    	this.add(new Animation(spawn,false));
    	
    	this.addRule(x -> (reaper.getFacingDirection() == Direction.RIGHT) && !reaper.isIdle(), x -> "reaper-idle-right");
    	this.addRule(x -> (reaper.getFacingDirection() == Direction.LEFT) && !reaper.isIdle(), x -> "reaper-idle-left");
    	
    	CreatureShadowImageEffect effect = new CreatureShadowImageEffect(reaper, new Color(24, 30, 28, 100));
	    effect.setOffsetY(1);
	    this.add(effect);
	}
}