import processing.core.PImage;
import java.util.List;

public class Obstacle extends EntityClass {

    public Obstacle(
            String id,
            Point position,
            List<PImage> images)
    {
//        super(id, position, images);
        super(position, images);
    }
}
