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

/**
 * a class that creates the minion attack ability
 * @author Kevin Lorinc, Kush Vashishtha
 */
@AbilityInfo(name = "MinionAttack", cooldown = 2000, range = 0, impact = 15, impactAngle = 360, value = 10, duration = 200, multiTarget = true)
public class MinionAttack extends Ability{
	/**
	 * creates a new ability
	 * @param executor the creature executing the ability
	 */
	public MinionAttack(Creature executor) {
		super(executor);
		
		this.addEffect(new MinionAttackEffect(this));
	}
	
	/**
	 * determines the impact area of this attack
	 * @return the shape of the impact area
	 */
	@Override
	public Shape calculateImpactArea() {//change arc area to change range of attack
		return new Arc2D.Double(this.getExecutor().getX()+16,this.getExecutor().getY(),30,30,0,360,Arc2D.PIE);
	}
	
	/**
	 * defines the effect of the minion attack ability
	 * @author Kevin Lorinc, Kush Vashishtha
	 */
	private static class MinionAttackEffect extends Effect{
		/**
		 * creates a new effect
		 * @param ability the ability the effect is connected to
		 */
		protected MinionAttackEffect (Ability ability) {
			super(ability, EffectTarget.ENEMY);
		}
		
		/**
		 * changes the way that the effect is applied to do damage to the player
		 * @param impactArea the shape the effect is applied to
		 */
		@Override
		public void apply(final Shape impactArea) {
			super.apply(new Rectangle(20,20,20,20));
			
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
