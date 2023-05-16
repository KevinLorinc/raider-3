package entities;

import java.awt.Color;

import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.graphics.CreatureShadowImageEffect;
import de.gurkenlabs.litiengine.graphics.Spritesheet;
import de.gurkenlabs.litiengine.graphics.animation.Animation;
import de.gurkenlabs.litiengine.graphics.animation.CreatureAnimationController;
import de.gurkenlabs.litiengine.resources.Resources;
import entities.Enemy.EnemyState;

/**
 * the animation controller of the reaper enemy class
 * @author Kush Vashishtha, Kevin Lorinc
 */
public class ReaperAnimationController extends CreatureAnimationController<Reaper>{
	
	/**
	 * 
	 * @param reaper the reaper to add this controller to
	 * @param defaultAnim the default animation
	 */
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
    	Spritesheet orbIdle = Resources.spritesheets().get("orb-idle");
    	Spritesheet orbSpawn = Resources.spritesheets().get("orb-spawn");
    	Spritesheet emptyOrb = Resources.spritesheets().get("orb-empty");
    	Spritesheet walkR = Resources.spritesheets().get("reaper-walk-right");
    	Spritesheet walkL = Resources.spritesheets().get("reaper-walk-left");
    	Spritesheet frenzyWalkL = Resources.spritesheets().get("reaper-frenzyWalk-left");
    	Spritesheet frenzyWalkR = Resources.spritesheets().get("reaper-frenzyWalk-right");
    	
    	this.add(new Animation(attackR,false));
    	this.add(new Animation(attackL,false));
    	this.add(new Animation(death,false));
    	this.add(new Animation(frenzyR,false));
    	this.add(new Animation(frenzyL,false));
    	this.add(new Animation(laughR,true));
    	this.add(new Animation(laughL,true));
    	this.add(new Animation(idleR,true));
    	this.add(new Animation(idleL,true));
    	this.add(new Animation(phaseR,false));
    	this.add(new Animation(phaseL,false));
    	this.add(new Animation(spawn,false));
    	this.add(new Animation(orbIdle,true));
    	this.add(new Animation(orbSpawn,false));
    	this.add(new Animation(emptyOrb,false));
    	this.add(new Animation(walkL,false));
    	this.add(new Animation(walkR,false));
    	this.add(new Animation(frenzyWalkL,false));
    	this.add(new Animation(frenzyWalkR,false));
    	
    	this.addRule(x -> (reaper.getEnemyState()==EnemyState.NOTSPAWNED) , x -> "orb-empty");
    	this.addRule(x -> (reaper.getEnemyState()==EnemyState.ORB) , x -> "orb-idle");
    	//this.addRule(x -> (reaper.getEnemyState()==EnemyState.HIT) && reaper.getIfSpawned()==false, x -> "reaper-spawn");
    	
    	this.addRule(x -> (reaper.getFacingDirection() == Direction.RIGHT) && !reaper.isIdle() && reaper.getHitPoints().getRelativeCurrentValue()>0.35, x -> "reaper-walk-right");//needs to change
    	this.addRule(x -> (reaper.getFacingDirection() == Direction.LEFT) && !reaper.isIdle() && reaper.getHitPoints().getRelativeCurrentValue()>0.35, x -> "reaper-walk-left");
    	
    	this.addRule(x -> (reaper.getFacingDirection() == Direction.LEFT) && !reaper.isIdle() && reaper.getHitPoints().getRelativeCurrentValue()<0.35, x -> "reaper-frenzyWalk-left");
    	this.addRule(x -> (reaper.getFacingDirection() == Direction.RIGHT) && !reaper.isIdle() && reaper.getHitPoints().getRelativeCurrentValue()<0.35, x -> "reaper-frenzyWalk-right");
    	
    	CreatureShadowImageEffect effect = new CreatureShadowImageEffect(reaper, new Color(24, 30, 28, 100));
	    effect.setOffsetY(1);
	    this.add(effect);
	}
}