import processing.core.PImage;

import java.util.List;
import java.util.Optional;


public class Vein extends DisappearingEntity {
    private final String id;
    private static final String ORE_ID_PREFIX = "ore -- ";
    private static final int ORE_CORRUPT_MIN = 20000;
    private static final int ORE_CORRUPT_MAX = 30000;

    public Vein(
            String id,
            Point position,
            List<PImage> images,
            int actionPeriod)
    {
        super(position, images, actionPeriod);
        this.id = id;
    }


    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround(getPosition());

        if (openPt.isPresent()) {
            ActiveEntity ore = Factory.createOre(ORE_ID_PREFIX + this.id, openPt.get(),
                    ORE_CORRUPT_MIN + getRandom().nextInt(
                            ORE_CORRUPT_MAX - ORE_CORRUPT_MIN),
                    imageStore.getImageList(WorldLoader.ORE_KEY));
            world.addEntity(ore);
            ore.scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                Factory.createActivityAction(this, world, imageStore),
                getActionPeriod());
    }


}

