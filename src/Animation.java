public class Animation implements Action {

    private final EntityAnimated entity;
    private final int repeatCount;

    public Animation(
            EntityAnimated entity,
            int repeatCount)
    {
        this.entity = entity;
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler)
    {
        this.entity.nextImage();

        if (this.repeatCount != 1) {
            scheduler.scheduleEvent((EntityActive) entity,
                    Factory.createAnimationAction(this.entity,
                            Math.max(this.repeatCount - 1,
                                    0)),
                    (entity).getAnimationPeriod());
        }
    }

}
