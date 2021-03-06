import processing.core.PImage;

import java.util.List;
import java.util.function.Predicate;

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


    protected Predicate<Point> nextPositionHelper(WorldModel world) {
        return p ->  world.withinBounds(p) && !world.isOccupied(p);
    }

//    protected Point nextPosition(WorldModel world, Point destPos) {
//        int horiz = Integer.signum(destPos.x - getPosition().x);
//        Point newPos = new Point(getPosition().x + horiz, getPosition().y);
//
//        if (horiz == 0 || world.isOccupied(newPos)) {
//            int vert = Integer.signum(destPos.y -  getPosition().y);
//            newPos = new Point( getPosition().x,  getPosition().y + vert);
//
//            if (vert == 0 || world.isOccupied(newPos)) {
//                newPos =  getPosition();
//            }
//        }
//        return newPos;
//    }

    protected int getResourceLimit() { return resourceLimit; }

    protected String getId() { return id; }

}
