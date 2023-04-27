package entities;

import de.gurkenlabs.litiengine.entities.behavior.AStarGrid;
import de.gurkenlabs.litiengine.entities.behavior.AStarPathFinder;
import de.gurkenlabs.litiengine.entities.behavior.EntityNavigator;
import de.gurkenlabs.litiengine.physics.MovementController;
import raider.RaidersLogic;

/**
 * a class that handles movement for the minion enemy
 * @author Kevin Lorinc
 */
public class MinionController extends MovementController<Minion>{
	private static final int NAVIGATE_DELAY = 500;
	private EntityNavigator navi;
	private long lastNavigate;
	
	/**
	 * creates a new movement controller for a minion
	 * @param movingEntity the minion to create the movement controller for
	 */
	public MinionController(Minion movingEntity) {
		super(movingEntity);
		AStarGrid grid = RaidersLogic.getCurrentGrid();

	    this.navi = new EntityNavigator(movingEntity, new AStarPathFinder(grid));
	}
	
	/**
	 * changes the way the movement controller updates
	 */
	@Override
	public void update(){
		
	}
}
