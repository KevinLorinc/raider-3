package abilities;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.abilities.Ability;
import de.gurkenlabs.litiengine.abilities.AbilityInfo;
import de.gurkenlabs.litiengine.abilities.effects.Effect;
import de.gurkenlabs.litiengine.abilities.effects.EffectTarget;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.entities.ICombatEntity;
import de.gurkenlabs.litiengine.entities.Prop;
import de.gurkenlabs.litiengine.graphics.Spritesheet;
import de.gurkenlabs.litiengine.graphics.animation.Animation;
import de.gurkenlabs.litiengine.resources.Resources;
import entities.Player;
import entities.Player.PlayerState;
import entities.Reaper;
import entities.ReaperController;
import ui.Hud;
import entities.Enemy;
import entities.Enemy.EnemyState;
import entities.Minion;
import entities.MinionController;

/**
 * creates the melee attack ability that is used by player
 * @author Kevin Lorinc
 */
@AbilityInfo(name = "MeleeAttack", cooldown = 350, range = 0, impact = 15, impactAngle = 360, value = 1, duration = 200, multiTarget = true)
public class MeleeAttack extends Ability{

	/**
	 * creates a new ability
	 * @param executor the creature executing the ability
	 */
	public MeleeAttack(Creature executor) {
		super(executor);
		
		this.addEffect(new MeleeAttackEffect(this));
	}
	
	/**
	 * lets us determine what we define as the impact area
	 */
	@Override
	public Shape calculateImpactArea() {//change arc area to change range of attack
		Direction attackDirection = Player.instance().calcAttackDirection();
		if(attackDirection == Direction.UP) {
			return new Arc2D.Double(Player.instance().getX()+2,Player.instance().getY(),25,25,50,80,Arc2D.PIE);
		}else if(attackDirection == Direction.RIGHT) {
			return new Arc2D.Double(Player.instance().getX() + 8,Player.instance().getY()+8,25,25,-45,90,Arc2D.PIE);
		} else if(attackDirection == Direction.LEFT) {
			return new Arc2D.Double(Player.instance().getX() - 4,Player.instance().getY(),25,25,135,90,Arc2D.PIE);
		} else if(attackDirection == Direction.DOWN) {
			return new Arc2D.Double(Player.instance().getX()+2,Player.instance().getY()+16,15,15,230,90,Arc2D.PIE);
		}
		
		return null;
	}
	
	/**
	 * defines the effect of the melee attack ability
	 * @author Kevin Lorinc
	 */
	private static class MeleeAttackEffect extends Effect{
		/**
		 * creates the effect of the Melee attack ability
		 * @param ability the ability the effect is tied with
		 */
		protected MeleeAttackEffect (Ability ability) {
			super(ability, EffectTarget.ENEMY);//will have to change this to find enemies within "range" and then add those as the effect target. might be multiple
		}
		
		/**
		 * changes the way that the effect is applied to do damage to the target
		 */
		@Override
		public void apply(final Shape impactArea) {
			super.apply(new Rectangle(20,20,20,20));
			
			final int damage = this.getAbility().getAttributes().value().get();
			final List<ICombatEntity> affected = this.lookForAffectedEntities(impactArea);
		    for (final ICombatEntity affectedEntity : affected) {
		      if(affectedEntity instanceof Enemy == false) continue;
		      
		      Enemy hit = (Enemy)affectedEntity;
		      
		      if(hit instanceof Minion) {
		    	  hit.hit(damage);
		        ((MinionController)(hit.movement())).setApplyPoint(Player.instance().getCollisionBoxCenter());
		        ((MinionController)(hit.movement())).setApplyTime(Game.time().now());
		        hit.setEnemyState(EnemyState.HIT);
		      }
		      
		      if(hit instanceof Reaper) {
		    	  Reaper reaper = (Reaper)hit;
		    	 if(reaper.getEnemyState() == EnemyState.ORB) {
	    	      ((ReaperController)(hit.movement())).setActionTime(Game.time().now());
	    	      hit.setEnemyState(EnemyState.HIT);
	    	    }else {
	    	    	int chance = (int)Math.round(Math.random());
	    	    	System.out.println(chance);
	    	    	if(chance==0) hit.hit(damage);
	    	    	else {
	    	    		if(reaper.getFacingDirection()==Direction.RIGHT) reaper.animations().play("reaper-phase-right");
	    	    		else reaper.animations().play("reaper-phase-left");
	    	    	}
	    	    }
		      }		      
		      
		    }
		}
	}
}