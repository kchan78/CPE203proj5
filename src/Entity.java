import processing.core.PImage;

import java.util.List;

public abstract class Entity {

//    private String id;
    private Point position;
    private final List<PImage> images;
    private int imageIndex;

    public Entity(
//            String id,
            Point position,
            List<PImage> images)
    {
//        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
    }

    public PImage getCurrentImage() {
        return (images.get(imageIndex));
    }

    public List<PImage> getImages() { return images; }

    public int getImageIndex() { return imageIndex; }

    public void setImageIndex(int index) { imageIndex = index; }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point point) {
        this.position = point;
    }
}
