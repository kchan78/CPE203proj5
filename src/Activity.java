public class Activity implements Action {

    private final Entity entity;
    private final WorldModel world;
    private final ImageStore imageStore;
    private final int repeatCount;

    public Activity(
            Entity entity,
            WorldModel world,
            ImageStore imageStore,
            int repeatCount)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }
    public void executeAction(EventScheduler scheduler)
    {
        if (this.entity instanceof EntityAnimated)
            this.entity.executeActivity(this.world, null, scheduler);

        else
            throw new UnsupportedOperationException(String.format(
                    "executeActivityAction not supported for %s",
                    this.entity.getClass()));
        }
    }


}
