import processing.core.PImage;

import java.util.List;

public class Blacksmith implements Entity{

    private Point position;
    private final List<PImage> images;
    private int imageIndex;

    public Blacksmith(
            Point position,
            List<PImage> images)
    {
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
    }

    public PImage getCurrentImage() {
        return (images.get(imageIndex));
    }

    public void nextImage() {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }


    public Point getPosition() {
        return position;
    }

    public void setPosition(Point point) {
        this.position = point;
    }
}
