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
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.entities.ICombatEntity;
import entities.Enemy;
import entities.Minion;
import entities.MinionController;
import entities.Player;
import entities.Reaper;
import entities.ReaperController;
import entities.Enemy.EnemyState;

/**
 * a class for the spin attack ability
 * @author Kevin Lorinc, Kush Vashishtha
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
	 * determines the impact area of this attack
	 * @return the shape of the impact area
	 */
	@Override
	public Shape calculateImpactArea() {//change arc area to change range of attack
		return new Arc2D.Double(Player.instance().getX()+6,Player.instance().getY(),35,35,0,360,Arc2D.PIE);
	}
	
	/**
	 * the effect that goes along with the spin attack ability
	 * @author Kevin Lorinc, Kush Vashishtha
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
		 * changes the way that the effect is applied to do damage to the player
		 * @param impactArea the shape the effect is applied to
		 */
		@Override
		public void apply(final Shape impactArea) {
			if(Game.world().environment() !=null){
				super.apply(new Rectangle(20,20,20,20));
				
				int damage = calculateDamage();
				final List<ICombatEntity> affected = this.lookForAffectedEntities(impactArea);
				for (final ICombatEntity affectedEntity : affected) {
			    //playing hit animation, damaging, and very temporarily stunning enemy
			     if(affectedEntity instanceof Enemy == false) continue;
			      Enemy hit = (Enemy)affectedEntity;
			      hit.hit(damage);
			      if(hit instanceof Minion) {
			    	  ((MinionController)(hit.movement())).setApplyPoint(Player.instance().getCollisionBoxCenter());
				      ((MinionController)(hit.movement())).setApplyTime(Game.time().now());
			      }
			      
			      if(hit instanceof Reaper) {
			    	  Reaper reaper = (Reaper)hit;
				    	 if(reaper.getEnemyState() == EnemyState.ORB) {
			    	      ((ReaperController)(hit.movement())).setActionTime(Game.time().now());
			    	      hit.setEnemyState(EnemyState.HIT);
			    	    }else {
			    	    	int chance = (int)Math.round(Math.random());
			    	    	if(chance==0) hit.hit(damage);
			    	    	else {
			    	    		if(reaper.getFacingDirection()==Direction.RIGHT) reaper.animations().play("reaper-phase-right");
			    	    		else reaper.animations().play("reaper-phase-left");
			    	    	}
			    	    }
				  }
			      hit.setEnemyState(EnemyState.HIT);
			    }
		    }
	    }
		
		/**
		 * returns damage this attack does based off weapon equipped
		 * @return damage of this attack based of weapon equipped
		 */
		private int calculateDamage() {
			String wep = Player.instance().getEquipped();
			if(wep.equals("fist")) return 0;
			if(wep.equals("sword")) return 5;
			if(wep.equals("swordPurple")) return 10;
			else return 30;
		}
	}
}
