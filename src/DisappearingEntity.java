import processing.core.PImage;

import java.util.List;
import java.util.Random;

public abstract class DisappearingEntity extends ActiveEntity{


    private final String id;
    private final Random rand = new Random();

    public DisappearingEntity(
            String id,
            Point position,
            List<PImage> images,
            int actionPeriod) {

        super(position, images, actionPeriod);
        this.id = id;
    }

    protected Random getRandom(){ return rand; }

    protected String getId(){ return id; }

}
