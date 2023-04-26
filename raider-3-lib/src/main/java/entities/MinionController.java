package entities;

import de.gurkenlabs.litiengine.physics.MovementController;

public class MinionController extends MovementController<Minion>{
	public MinionController(Minion movingEntity) {
		super(movingEntity);
	}
}
