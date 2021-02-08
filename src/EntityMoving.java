// for miners and ore blobs

public interface EntityMoving {

    public Point nextPosition(
            WorldModel world, Point destPos);

    public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler);

    public void nextImage();
}
