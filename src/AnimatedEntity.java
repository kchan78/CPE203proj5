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
        return images.get(imageIndex);
    }

 //   public int getCurrentImageHelper() {return imageIndex;}

    public int getAnimationPeriod() {
        return this.animationPeriod;
    }

    public void nextImage() {
        imageIndex = (imageIndex + 1) % images.size() ;
    }

    public void setPosition(Point point) {
        this.position = point;
    }
}
