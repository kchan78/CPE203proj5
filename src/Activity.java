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

        entity.executeActivity(this.world, imageStore, scheduler);

    }

}
