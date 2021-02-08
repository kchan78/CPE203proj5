import processing.core.PImage;
import java.util.List;
import java.util.Random;

public class Ore implements Entity, EntityActive {

    private final String id;
    private Point position;
    private final List<PImage> images;
    private int imageIndex;
    private final int actionPeriod;

    private final Random rand = new Random();
    private static final String BLOB_KEY = "blob";
    private static final String BLOB_ID_SUFFIX = " -- blob";
    private static final int BLOB_PERIOD_SCALE = 4;
    private static final int BLOB_ANIMATION_MIN = 50;
    private static final int BLOB_ANIMATION_MAX = 150;

    public Ore(
            String id,
            Point position,
            List<PImage> images,
            int actionPeriod)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
    }

    public PImage getCurrentImage() {
        return (images.get(imageIndex));
    }


    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Point pos = this.position;

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        EntityActive blob = Factory.createOreBlob(this.id + BLOB_ID_SUFFIX, pos,
                this.actionPeriod / BLOB_PERIOD_SCALE,
                BLOB_ANIMATION_MIN + rand.nextInt(
                        BLOB_ANIMATION_MAX
                                - BLOB_ANIMATION_MIN),
                imageStore.getImageList(BLOB_KEY));

        world.addEntity((Entity) blob);
        blob.scheduleActions(scheduler, world, imageStore);
    }

    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                Factory.createActivityAction(this, world, imageStore),
                this.actionPeriod);

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
