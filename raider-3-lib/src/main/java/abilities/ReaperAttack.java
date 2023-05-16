package abilities;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.util.List;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.abilities.Ability;
import de.gurkenlabs.litiengine.abilities.AbilityInfo;
import de.gurkenlabs.litiengine.abilities.effects.Effect;
import de.gurkenlabs.litiengine.abilities.effects.EffectTarget;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.entities.ICombatEntity;
import entities.Player;
import entities.Reaper;

/**
 * a class that creates the Reaper attack ability
 * @author Kevin Lorinc, Kush vashishtha
 */
@AbilityInfo(name = "ReaperAttack", cooldown = 4000, range = 0, impact = 10, impactAngle = 360, value = 20, duration = 200, multiTarget = true)
public class ReaperAttack extends Ability{
	
	private static Reaper thisReaper;
	
	/**
	 * creates a new ability
	 * @param executor the creature executing the ability
	 */
	public ReaperAttack(Creature executor) {
		super(executor);
		thisReaper = (Reaper)executor;
		this.addEffect(new ReaperAttackEffect(this));
	}
	
	/**
	 * determines the impact area of this attack
	 * @return the shape of the impact area
	 */
	@Override
	public Shape calculateImpactArea() {//change arc area to change range of attack
		return new Arc2D.Double(this.getExecutor().getX()+20,this.getExecutor().getY(),80,80,0,360,Arc2D.PIE);
	}
	
	/**
	 * defines the effect of the Reaper attack ability
	 * @author Kevin Lorinc, Kush Vashishtha
	 */
	private static class ReaperAttackEffect extends Effect{
		/**
		 * creates a new effect
		 * @param ability the ability the effect is connected to
		 */
		protected ReaperAttackEffect (Ability ability) {
			super(ability, EffectTarget.ENEMY);
		}
		
		/**
		 * changes the way that the effect is applied to do damage to the player
		 * @param impactArea the shape the effect is applied to
		 */
		@Override
		public void apply(final Shape impactArea) {
			super.apply(new Rectangle(20,20,20,20));
			//damage number based on health of boss
			if(thisReaper.getHitPoints().getRelativeCurrentValue()<0.35)
				thisReaper.getReaperAttack().getAttributes().value().setBaseValue(40);
			else
				thisReaper.getReaperAttack().getAttributes().value().setBaseValue(20);
			final int damage = this.getAbility().getAttributes().value().get();
			final List<ICombatEntity> affected = this.lookForAffectedEntities(impactArea);
		    for (final ICombatEntity affectedEntity : affected) {
		      if(affectedEntity instanceof Player) {
		    	  affectedEntity.hit(damage/2);
			      if(affectedEntity.isDead())
			    	  ((Player)affectedEntity).onDead();
			      Game.loop().perform(700, () -> {
			    	  if(this.getAbility().getExecutor().getTarget().getCenter().distance(this.getAbility().getExecutor().getCenter()) < 50) {
			    		  affectedEntity.hit(damage/2);
			    		  if(affectedEntity.isDead())
			    			  ((Player)affectedEntity).onDead(); 
			    	  }  
			      });
		      }
		    }
		}
	}
}
