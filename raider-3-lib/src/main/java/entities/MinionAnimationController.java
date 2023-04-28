package entities;

import de.gurkenlabs.litiengine.entities.behavior.EntityNavigator;
import de.gurkenlabs.litiengine.graphics.animation.Animation;
import de.gurkenlabs.litiengine.graphics.animation.CreatureAnimationController;
import de.gurkenlabs.litiengine.resources.Resources;

public class MinionAnimationController extends CreatureAnimationController<Minion>{
	
	public MinionAnimationController(Minion minion) {
		super(minion, new Animation(Resources.spritesheets().get("minion-idle-right"),false));
	}
}
