import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class MinerFull extends EntityMoving {

    private final String id;
    private final int resourceLimit;


    public MinerFull(
            String id,
            Point position,
            List<PImage> images,
            int resourceLimit,
            int actionPeriod,
            int animationPeriod)
    {
        super(position, images, actionPeriod, animationPeriod);
        this.id = id;
        this.resourceLimit = resourceLimit;

    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> fullTarget =
                world.findNearest( getPosition(), Blacksmith.class);

        if (fullTarget.isPresent() && moveTo(world,
                fullTarget.get(), scheduler))
        {
            transformFull(world, scheduler, imageStore);
        }
        else {
            scheduler.scheduleEvent(this,
                    Factory.createActivityAction(this, world, imageStore),
                    getActionPeriod());
        }
    }
    
    private void transformFull(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        ActiveEntity miner = Factory.createMinerNotFull(this.id, this.resourceLimit,
                 getPosition(), getActionPeriod(),
                getAnimationPeriod(),
                getImages());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity((Entity)miner);
        miner.scheduleActions(scheduler, world, imageStore);
    }

    public Point nextPosition(
            WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.x -  getPosition().x);
        Point newPos = new Point( getPosition().x + horiz,  getPosition().y);

        if (horiz == 0 || world.isOccupied(newPos)) {
            int vert = Integer.signum(destPos.y -  getPosition().y);
            newPos = new Point( getPosition().x,  getPosition().y + vert);

            if (vert == 0 || world.isOccupied(newPos)) {
                newPos =  getPosition();
            }
        }

        return newPos;
    }

    public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if ( getPosition().adjacent(target.getPosition())) {
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
    
}
