public class Animation implements Action {

    private final EntityActive entity;
    private final int repeatCount;

    public Animation(
            EntityActive entity,
            int repeatCount)
    {
        this.entity = entity;
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler)
    {
        this.entity.nextImage();

        if (this.repeatCount != 1) {
            scheduler.scheduleEvent(entity,
                    Factory.createAnimationAction(this.entity,
                            Math.max(this.repeatCount - 1,
                                    0)),
                    ((EntityAnimated) entity).getAnimationPeriod());
        }
    }

}