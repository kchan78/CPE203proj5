// for miners and ore blobs

import processing.core.PImage;

import java.util.List;

public abstract class EntityMoving extends AnimatedEntity {

    public EntityMoving(
            Point position,
            List<PImage> images,
            int actionPeriod,
            int animationPeriod) {
        super(position, images, actionPeriod, animationPeriod);
    }

    abstract Point nextPosition(
            WorldModel world, Point destPos);

    abstract boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler);

    public int scheduleActionsHelper() { return 0;}
}
