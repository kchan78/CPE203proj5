public class Animation implements Action {

    private final AnimatedEntity entity;
    private final int repeatCount;

    public Animation(
            AnimatedEntity entity,
            int repeatCount)
    {
        this.entity = entity;
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler)
    {
        this.entity.nextImage();

        if (this.repeatCount != 1) {
            scheduler.scheduleEvent((ActiveEntity) entity,
                    Factory.createAnimationAction(this.entity,
                            Math.max(this.repeatCount - 1,
                                    0)),
                    (entity).getAnimationPeriod());
        }
    }

}
