// for miners and ore blobs

import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public abstract class EntityMoving extends AnimatedEntity {

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
            //helper abstract class
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

    abstract void moveToHelper(
            WorldModel world,
            Entity target,
            EventScheduler scheduler);

    protected Point nextPosition(WorldModel world, Point destPos) {
        int horiz = Integer.signum(destPos.x - getPosition().x);
        Point newPos = new Point(getPosition().x + horiz, getPosition().y);

        return nextPositionHelper(world, destPos, newPos, horiz);
    }

    abstract Point nextPositionHelper(WorldModel world, Point destPos, Point newPos, int horiz);

    public int scheduleActionsHelper() { return 0;}
}
