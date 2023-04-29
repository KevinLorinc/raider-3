package entities;

import de.gurkenlabs.litiengine.entities.behavior.EntityNavigator;
import de.gurkenlabs.litiengine.graphics.Spritesheet;
import de.gurkenlabs.litiengine.graphics.animation.Animation;
import de.gurkenlabs.litiengine.graphics.animation.CreatureAnimationController;
import de.gurkenlabs.litiengine.resources.Resources;

public class MinionAnimationController extends CreatureAnimationController<Minion>{
	public MinionAnimationController(Minion minion,Animation defaultAnim) {
		super(minion,defaultAnim);
	}
}
