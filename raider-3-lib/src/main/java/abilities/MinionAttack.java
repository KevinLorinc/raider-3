package abilities;

import de.gurkenlabs.litiengine.abilities.Ability;
import de.gurkenlabs.litiengine.abilities.AbilityInfo;
import de.gurkenlabs.litiengine.abilities.effects.Effect;
import de.gurkenlabs.litiengine.abilities.effects.EffectTarget;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.entities.ICombatEntity;

/**
 * a class that creates the minion attack ability
 * @author Kevin Lorinc
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
	 * a class that describes the effect of the ability
	 * @author Kevin Lorinc
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
		 * describes how this specifc effect is different.
		 */
		@Override
		public void apply(ICombatEntity entity) {
			super.apply(entity);
			final int damage = this.getAbility().getAttributes().value().get();
		    entity.hit(damage);
		}
	}
}
