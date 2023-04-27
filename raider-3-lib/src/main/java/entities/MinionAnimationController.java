package entities;

import de.gurkenlabs.litiengine.entities.behavior.EntityNavigator;
import de.gurkenlabs.litiengine.graphics.animation.CreatureAnimationController;

public class MinionAnimationController extends CreatureAnimationController<Minion>{
	private static final int NAVIGATE_DELAY = 500;
	private EntityNavigator navi;
	
	public MinionAnimationController(Minion minion) {
		super(minion, true);
	}
}
