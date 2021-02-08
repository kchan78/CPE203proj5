import processing.core.PImage;
import java.util.List;


public class Obstacle implements Entity {
    private Point position;
    private final List<PImage> images;
    private int imageIndex;

    public Obstacle(
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

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point point) {
        this.position = point;
    }
}
