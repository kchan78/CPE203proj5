import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class MinerFull extends Miners {

    public MinerFull(
            String id,
            Point position,
            List<PImage> images,
            int resourceLimit,
            int actionPeriod,
            int animationPeriod)
    {
        super(position, images, actionPeriod, animationPeriod, id, resourceLimit);

    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> fullTarget =
                world.findNearest(getPosition(), Blacksmith.class);

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
        ActiveEntity miner = Factory.createMinerNotFull(getId(), getResourceLimit(),
                 getPosition(), getActionPeriod(),
                getAnimationPeriod(),
                getImages());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        miner.scheduleActions(scheduler, world, imageStore);
    }

    public void moveToHelper(
            WorldModel world,
            Entity target,
            EventScheduler scheduler){}
    
}
