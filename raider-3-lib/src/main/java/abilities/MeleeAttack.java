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
	 * defines the effect of the melee attack ability
	 * @author Kevin Lorinc
	 */
	private static class MeleeAttackEffect extends Effect{
		/**
		 * creates the effect of the Melee attack ability
		 * @param ability the ability the effect is tied with
		 */
		protected MeleeAttackEffect (Ability ability) {
			super(ability, EffectTarget.EXECUTINGENTITY);//will have to change this to find enemies within "range" and then add those as the effect target. might be multiple
		}
		
		/**
		 * changes the way that the effect is applied to do damage to the target
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
