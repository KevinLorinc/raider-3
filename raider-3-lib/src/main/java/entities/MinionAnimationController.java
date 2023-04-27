package entities;

import de.gurkenlabs.litiengine.entities.behavior.EntityNavigator;
import de.gurkenlabs.litiengine.graphics.animation.CreatureAnimationController;

public class MinionAnimationController extends CreatureAnimationController<Minion>{
	
	public MinionAnimationController(Minion minion) {
		super(minion, true);
	}
}
