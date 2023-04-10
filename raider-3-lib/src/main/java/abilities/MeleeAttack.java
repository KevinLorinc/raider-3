package abilities;

import de.gurkenlabs.litiengine.abilities.Ability;
import de.gurkenlabs.litiengine.abilities.AbilityInfo;
import de.gurkenlabs.litiengine.abilities.effects.Effect;
import de.gurkenlabs.litiengine.abilities.effects.EffectTarget;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.entities.ICombatEntity;

@AbilityInfo(name = "MeleeAttack", cooldown = 700, range = 0, impact = 15, impactAngle = 360, value = 10, duration = 400, multiTarget = true)
public class MeleeAttack extends Ability{

	public MeleeAttack(Creature executor) {
		super(executor);
		
		this.addEffect(new MeleeAttackEffect(this));
	}
	
	private static class MeleeAttackEffect extends Effect{
		protected MeleeAttackEffect (Ability ability) {
			super(ability, EffectTarget.EXECUTINGENTITY);
		}
		
		@Override
		protected void apply(ICombatEntity entity) {
			super.apply(entity);
			
			final int damage = this.getAbility().getAttributes().value().get();
			entity.hit(damage,this.getAbility());
			
			//once we add sprite sheets for the attack that code will go here. Reference Pumkin game hit class
		}
	}

}
