package abilities;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.util.List;

import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.abilities.Ability;
import de.gurkenlabs.litiengine.abilities.AbilityInfo;
import de.gurkenlabs.litiengine.abilities.effects.Effect;
import de.gurkenlabs.litiengine.abilities.effects.EffectTarget;
import de.gurkenlabs.litiengine.abilities.effects.ForceEffect;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.entities.ICombatEntity;
import de.gurkenlabs.litiengine.entities.IMobileEntity;
import de.gurkenlabs.litiengine.graphics.Spritesheet;
import de.gurkenlabs.litiengine.graphics.animation.Animation;
import de.gurkenlabs.litiengine.physics.Force;
import de.gurkenlabs.litiengine.resources.Resources;
import entities.Enemy;
import entities.Minion;
import entities.Player;
import entities.Enemy.EnemyState;

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
	 * lets us determine what we define as the impact area
	 */
	@Override
	public Shape calculateImpactArea() {//change arc area to change range of attack
		return new Arc2D.Double(Player.instance().getX()+2,Player.instance().getY()+16,30,30,0,360,Arc2D.PIE);
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
		public void apply(final Shape impactArea) {
			if(Game.world().environment() !=null){
				super.apply(new Rectangle(20,20,20,20));
				
				final int damage = this.getAbility().getAttributes().value().get();
				final List<ICombatEntity> affected = this.lookForAffectedEntities(impactArea);
				for (final ICombatEntity affectedEntity : affected) {
			    //playing hit animation, damaging, and very temporarily stunning enemy
			     if(affectedEntity instanceof Enemy == false) continue;
			      Enemy hit = (Enemy)affectedEntity;
			      hit.hit(damage);
			        
			    }
		    }
	    }
	}
}
