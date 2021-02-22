import processing.core.PImage;

import java.util.List;
import java.util.Random;

public abstract class DisappearingEntity extends ActiveEntity{



    private final Random rand = new Random();

    public DisappearingEntity(
            Point position,
            List<PImage> images,
            int actionPeriod) {

        super(position, images, actionPeriod);
    }

    protected Random getRandom(){ return rand; }

    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                Factory.createActivityAction(this, world, imageStore),
                getActionPeriod());
    }
}
