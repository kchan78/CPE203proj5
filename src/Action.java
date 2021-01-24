public final class Action
{
    public ActionKind kind;
    public Entity entity;
    public WorldModel world;
    public ImageStore imageStore;
    public int repeatCount;

    public Action(
            ActionKind kind,
            Entity entity,
            WorldModel world,
            ImageStore imageStore,
            int repeatCount)
    {
        this.kind = kind;
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }
    public void executeActivityAction(EventScheduler scheduler)
    {
        switch (this.entity.kind) {
            case MINER_FULL:
                Functions.executeMinerFullActivity(this.entity, this.world,
                        this.imageStore, scheduler);
                break;

            case MINER_NOT_FULL:
                Functions.executeMinerNotFullActivity(this.entity, this.world,
                        this.imageStore, scheduler);
                break;

            case ORE:
                Functions.executeOreActivity(this.entity, this.world,
                        this.imageStore, scheduler);
                break;

            case ORE_BLOB:
                Functions.executeOreBlobActivity(this.entity, this.world,
                        this.imageStore, scheduler);
                break;

            case QUAKE:
                Functions.executeQuakeActivity(this.entity, this.world,
                        this.imageStore, scheduler);
                break;

            case VEIN:
                Functions.executeVeinActivity(this.entity, this.world,
                        this.imageStore, scheduler);
                break;

            default:
                throw new UnsupportedOperationException(String.format(
                        "executeActivityAction not supported for %s",
                        this.entity.kind));
        }
    }

}
