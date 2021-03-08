// for miners and ore blobs

import processing.core.PImage;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;


public abstract class EntityMoving extends AnimatedEntity {


    private PathingStrategy strategy = new SingleStepPathingStrategy();
    //private PathingStrategy strategy = new AStarPathingStrategy();

    public EntityMoving(
            Point position,
            List<PImage> images,
            int actionPeriod,
            int animationPeriod) {
        super(position, images, actionPeriod, animationPeriod);
    }

    protected boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler) {
        if ( getPosition().adjacent(target.getPosition())) {
            //abstract helper method
            moveToHelper(world, target, scheduler);

            return true;
        }
        else {
            Point nextPos = this.nextPosition(world, target.getPosition());

            if (! getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    protected abstract void moveToHelper(
            WorldModel world,
            Entity target,
            EventScheduler scheduler);


    protected Point nextPosition(WorldModel world, Point destPos) {

        List<Point> points = strategy.computePath(getPosition(), destPos,
                            nextPositionHelper(world),                  //canPassThrough
                            Point::adjacent,                            // withinReach
                            PathingStrategy.CARDINAL_NEIGHBORS);        // potentialNeighbors

        if (points.size() == 0)
        {
            return getPosition();
        }

        return points.get(0);
    }

    protected abstract Predicate<Point> nextPositionHelper(WorldModel world);

    protected int scheduleActionsHelper() { return 0;}
}
