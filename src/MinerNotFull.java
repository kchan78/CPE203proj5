import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class MinerNotFull extends Miners {

    private int resourceCount;

    public MinerNotFull(
            String id,
            Point position,
            List<PImage> images,
            int resourceLimit,
            int resourceCount,
            int actionPeriod,
            int animationPeriod)
    {
        super(position, images, actionPeriod, animationPeriod, id, resourceLimit);
        this.resourceCount = resourceCount;
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> notFullTarget =
                world.findNearest(getPosition(), Ore.class);

        if (!notFullTarget.isPresent() || !moveTo(world,
                notFullTarget.get(),
                scheduler)
                || !transformNotFull(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    Factory.createActivityAction(this, world, imageStore),
                    getActionPeriod());
        }
    }


    private boolean transformNotFull(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (this.resourceCount >= getResourceLimit()) {
            ActiveEntity miner = Factory.createMinerFull(getId(), getResourceLimit(),
                    getPosition(), getActionPeriod(),
                    getAnimationPeriod(),
                    getImages());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            miner.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    public void moveToHelper(
            WorldModel world,
            Entity target,
            EventScheduler scheduler){
        this.resourceCount += 1;
        world.removeEntity(target);
        scheduler.unscheduleAllEvents(target);
    }

}
