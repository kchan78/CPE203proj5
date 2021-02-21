// parent for MinerFull, MinerNotFull, OreBlob, Quake

import processing.core.PImage;

import java.util.List;

public abstract class AnimatedEntity extends ActiveEntity{
    private final int animationPeriod;

    public AnimatedEntity(
//                        String id,
                          Point position,
                          List<PImage> images,
                          int actionPeriod,
                          int animationPeriod) {

//        super(id, position, images, actionPeriod);
        super(position, images, actionPeriod);

        this.animationPeriod = animationPeriod;

    }

     public int getAnimationPeriod() {
        return this.animationPeriod;
    }

    public void nextImage() {
        super.setImageIndex((getImageIndex() + 1) % getImages().size()) ;
    }

}
