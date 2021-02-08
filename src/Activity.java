public class Activity implements Action {

    private final EntityActive entity;
    private final WorldModel world;
    private final ImageStore imageStore;

    public Activity(
            EntityActive entity,
            WorldModel world,
            ImageStore imageStore)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
    }

    public void executeAction(EventScheduler scheduler)
    {
     //   if (this.entity instanceof EntityActive)
        ((EntityActive) entity).executeActivity(this.world, imageStore, scheduler);
 /*
        else
            throw new UnsupportedOperationException(String.format(
                    "executeActivityAction not supported for %s",
                    this.entity.getClass()));

 */
    }

}
