import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class OreBlob extends EntityMoving {

    private static final String QUAKE_KEY = "quake";

    //private PathingStrategy strategy = new SingleStepPathingStrategy();
    private PathingStrategy strategy = new AStarPathingStrategy();

    public OreBlob(
            Point position,
            List<PImage> images,
            int actionPeriod,
            int animationPeriod)
    {
        super(position, images, actionPeriod, animationPeriod);
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> blobTarget =
                world.findNearest(getPosition(), Vein.class);
        long nextPeriod = getActionPeriod();

        if (blobTarget.isPresent()) {
            Point tgtPos = blobTarget.get().getPosition();

            if (moveTo(world, blobTarget.get(), scheduler)) {
                ActiveEntity quake = Factory.createQuake(tgtPos,
                        imageStore.getImageList(QUAKE_KEY));

                world.addEntity(quake);
                nextPeriod += getActionPeriod();
                quake.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                Factory.createActivityAction(this, world, imageStore),
                nextPeriod);
    }


    protected Predicate<Point> nextPositionHelper(WorldModel world) {
        return p -> world.withinBounds(p) &&
               (!world.isOccupied(p) || (world.getOccupant(p).get().getClass() == Ore.class));
    }

//    public Point nextPosition(WorldModel world, Point destPos) {
//
//        int horiz = Integer.signum(destPos.x - getPosition().x);
//        Point newPos = new Point(getPosition().x + horiz, getPosition().y);
//
//        Optional<Entity> occupant = world.getOccupant(newPos);
//
//        if (horiz == 0 || (occupant.isPresent() && !(occupant.get().getClass()
//                == Ore.class)))
//        {
//            int vert = Integer.signum(destPos.y - getPosition().y);
//            newPos = new Point(getPosition().x, getPosition().y + vert);
//            occupant = world.getOccupant(newPos);
//
//            if (vert == 0 || (occupant.isPresent() && !(occupant.get().getClass()
//                    == Ore.class)))
//            {
//                newPos = getPosition();
//            }
//        }
//        return newPos;
//    }


    public void moveToHelper(
            WorldModel world,
            Entity target,
            EventScheduler scheduler){

        world.removeEntity(target);
        scheduler.unscheduleAllEvents(target);
    }

}
