import processing.core.PImage;

import java.util.List;

public abstract class Entity {

    protected Point position;
    protected final List<PImage> images;
 //   protected int imageIndex;

    public Entity(
            Point position,
            List<PImage> images)
    {
        this.position = position;
        this.images = images;
   //     this.imageIndex = 0;
    }

    public PImage getCurrentImage() {
        return images.get(0);
    //    return images.get(getCurrentImageHelper());
    }

  //  public int getCurrentImageHelper() {return 0;}

   // public List<PImage> getImages() { return images; }

   // public int getImageIndex() { return imageIndex; }

  //  public void setImageIndex(int index) { imageIndex = index; }

    public Point getPosition() {
        return position;
    }

//    public void setPosition(Point point) {
//        this.position = point;
//    }
}
