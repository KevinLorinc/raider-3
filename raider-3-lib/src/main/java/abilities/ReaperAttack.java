package abilities;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.util.List;

import de.gurkenlabs.litiengine.abilities.Ability;
import de.gurkenlabs.litiengine.abilities.AbilityInfo;
import de.gurkenlabs.litiengine.abilities.effects.Effect;
import de.gurkenlabs.litiengine.abilities.effects.EffectTarget;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.entities.ICombatEntity;
import entities.Player;
import entities.Reaper;
import entities.Enemy.EnemyState;

/**
 * a class that creates the minion attack ability
 * @author Kevin Lorinc
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
	 * changes impact area
	 */
	@Override
	public Shape calculateImpactArea() {//change arc area to change range of attack
		return new Arc2D.Double(this.getExecutor().getX()+2,this.getExecutor().getY()+16,30,30,0,360,Arc2D.PIE);
	}
	
	/**
	 * a class that describes the effect of the ability
	 * @author Kevin Lorinc
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
		 * describes how this specifc effect is different.
		 */
		/**
		 * changes the way that the ability is applied
		 */
		@Override
		public void apply(final Shape impactArea) {
			super.apply(new Rectangle(20,20,20,20));
			if(thisReaper.getHitPoints().getRelativeCurrentValue()<0.35)
				thisReaper.getReaperAttack().getAttributes().value().setBaseValue(40);
			else
				thisReaper.getReaperAttack().getAttributes().value().setBaseValue(20);
			final int damage = this.getAbility().getAttributes().value().get();
			final List<ICombatEntity> affected = this.lookForAffectedEntities(impactArea);
		    for (final ICombatEntity affectedEntity : affected) {
		    	affectedEntity.hit(damage);
		      if(affectedEntity.isDead())
		    	  ((Player)affectedEntity).onDead();
		    }
		}
	}
}
