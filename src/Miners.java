import processing.core.PImage;

import java.util.List;

public abstract class Miners extends EntityMoving{

    private final String id;
    private final int resourceLimit;

    public Miners(
            Point position,
            List<PImage> images,
            int actionPeriod,
            int animationPeriod,
            String id,
            int resourceLimit) {
        super(position, images, actionPeriod, animationPeriod);
        this.id = id;
        this.resourceLimit = resourceLimit;
    }

    public Point nextPositionHelper(WorldModel world, Point destPos, Point newPos, int horiz) {
        if (horiz == 0 || world.isOccupied(newPos)) {
            int vert = Integer.signum(destPos.y -  getPosition().y);
            newPos = new Point( getPosition().x,  getPosition().y + vert);

            if (vert == 0 || world.isOccupied(newPos)) {
                newPos =  getPosition();
            }
        }
        return newPos;
    }

    protected int getResourceLimit() { return resourceLimit; }

    protected String getId() { return id; }

}
