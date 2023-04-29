package abilities;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.abilities.Ability;
import de.gurkenlabs.litiengine.abilities.AbilityInfo;
import de.gurkenlabs.litiengine.abilities.effects.Effect;
import de.gurkenlabs.litiengine.abilities.effects.EffectTarget;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.entities.ICombatEntity;
import de.gurkenlabs.litiengine.graphics.Spritesheet;
import de.gurkenlabs.litiengine.graphics.animation.Animation;
import de.gurkenlabs.litiengine.resources.Resources;
import entities.Player;

/**
 * A class for the spin attack ability
 * @author Kevin Lorinc
 */
@AbilityInfo(name = "SpinAttack", cooldown = 700, range = 0, impact = 15, impactAngle = 360, value = 5, duration = 400, multiTarget = true)
public class SpinAttack extends Ability{
	/**
	 * creates a new ability attributed to a specific executor
	 * @param executor the entity executing the ability
	 */
	public SpinAttack(Creature executor) {
		super(executor);
		
		this.addEffect(new SpinAttackEffect(this));
	}
	
	/**
	 * The attack that goes along with the spin attack ability
	 * @author Kevin Lorinc
	 */
	private static class SpinAttackEffect extends Effect{
		/**
		 * creates the effect tied to a specific spin attack
		 * @param ability the ability to tie the effect to
		 */
		protected SpinAttackEffect (Ability ability) {
			super(ability, EffectTarget.ENEMY);//will have to update this to get the correct "range" and execute the effect on all the enemies within range
		}
		
		/**
		 * changes the way that the ability is applied
		 */
		@Override
		protected void apply(ICombatEntity entity) {
			super.apply(entity);
			
			final int damage = this.getAbility().getAttributes().value().get();
			entity.hit(damage,this.getAbility());
			
			//Spritesheet idleAttack = Resources.spritesheets().get("raider-idleSwordAttack-right");
			//Player.instance().animations().add(new Animation(idleAttack ,true));
			//Game.loop().perform(400, () -> Player.instance().animations().add(new Animation(idleAttack ,true)));
			
			//once we add sprite sheets for the attack that code will go here. Reference Pumkin game hit class
		}
	}

}
