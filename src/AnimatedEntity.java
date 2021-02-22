// parent for MinerFull, MinerNotFull, OreBlob, Quake

import processing.core.PImage;

import java.util.List;

public abstract class AnimatedEntity extends ActiveEntity{
    private final int animationPeriod;
    private int imageIndex;

    public AnimatedEntity(
                          Point position,
                          List<PImage> images,
                          int actionPeriod,
                          int animationPeriod) {

        super(position, images, actionPeriod);

        this.animationPeriod = animationPeriod;

        this.imageIndex = 0;
    }

    @Override
    public PImage getCurrentImage() {
        return getImages().get(imageIndex);
    }

 //   public int getCurrentImageHelper() {return imageIndex;}

    protected int getAnimationPeriod() {
        return animationPeriod;
    }

    protected void nextImage() {
        imageIndex = (imageIndex + 1) % getImages().size() ;
    }

    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                Factory.createActivityAction(this, world, imageStore),
                getActionPeriod());
        scheduler.scheduleEvent(this,
                Factory.createAnimationAction(this, scheduleActionsHelper()),
                getAnimationPeriod());
    }

    abstract int scheduleActionsHelper();
}
