import processing.core.PImage;

import java.util.List;

public abstract class Entity {

    private Point position;
    private final List<PImage> images;
 //   protected int imageIndex;

    public Entity(
            Point position,
            List<PImage> images)
    {
        this.position = position;
        this.images = images;
   //     this.imageIndex = 0;
    }

    protected PImage getCurrentImage() {
        return images.get(0);
    //    return images.get(getCurrentImageHelper());
    }

  //  public int getCurrentImageHelper() {return 0;}

    protected List<PImage> getImages() { return images; }

    protected Point getPosition() {
        return position;
    }

    protected void setPosition(Point point) {
        this.position = point;
    }
}
