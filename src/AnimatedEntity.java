// parent for MinerFull, MinerNotFull, OreBlob, Quake

import processing.core.PImage;

import java.util.List;

public abstract class AnimatedEntity extends ActiveEntity{
    private final int animationPeriod;
    private int imageIndex;

    public AnimatedEntity(
//                        String id,
                          Point position,
                          List<PImage> images,
                          int actionPeriod,
                          int animationPeriod) {

//        super(id, position, images, actionPeriod);
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


}
