import java.util.List;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;

public final class Entity
{
    private final EntityKind kind;
    private final String id;
    private Point position;
    private final List<PImage> images;
    private int imageIndex;
    private final int resourceLimit;
    private int resourceCount;
    private final int actionPeriod;
    private final int animationPeriod;

    private final Random rand = new Random();
    private static final String BLOB_KEY = "blob";
    private static final String BLOB_ID_SUFFIX = " -- blob";
    private static final int BLOB_PERIOD_SCALE = 4;
    private static final int BLOB_ANIMATION_MIN = 50;
    private static final int BLOB_ANIMATION_MAX = 150;
    private static final String ORE_ID_PREFIX = "ore -- ";
    private static final int ORE_CORRUPT_MIN = 20000;
    private static final int ORE_CORRUPT_MAX = 30000;
    private static final String QUAKE_KEY = "quake";
    private static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;

    public Entity(
            EntityKind kind,
            String id,
            Point position,
            List<PImage> images,
            int resourceLimit,
            int resourceCount,
            int actionPeriod,
            int animationPeriod)
    {
        this.kind = kind;
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }

    public PImage getCurrentImage() {
            return (images.get(imageIndex));
    }

    public void executeMinerFullActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> fullTarget =
                world.findNearest(this.position, EntityKind.BLACKSMITH);

        if (fullTarget.isPresent() && moveToFull(world,
                fullTarget.get(), scheduler))
        {
            transformFull(world, scheduler, imageStore);
        }
        else {
            scheduler.scheduleEvent(this,
                    Factory.createActivityAction(this, world, imageStore),
                    this.actionPeriod);
        }
    }

    public void executeMinerNotFullActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> notFullTarget =
                world.findNearest(this.position, EntityKind.ORE);

        if (!notFullTarget.isPresent() || !moveToNotFull(world,
                notFullTarget.get(),
                scheduler)
                || !transformNotFull(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    Factory.createActivityAction(this, world, imageStore),
                    this.actionPeriod);
        }
    }
    public void executeOreActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Point pos = this.position;

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        Entity blob = Factory.createOreBlob(this.id + BLOB_ID_SUFFIX, pos,
                this.actionPeriod / BLOB_PERIOD_SCALE,
                BLOB_ANIMATION_MIN + rand.nextInt(
                        BLOB_ANIMATION_MAX
                                - BLOB_ANIMATION_MIN),
                imageStore.getImageList(BLOB_KEY));

        world.addEntity(blob);
        blob.scheduleActions(scheduler, world, imageStore);
    }
    public void executeOreBlobActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> blobTarget =
                world.findNearest(this.position, EntityKind.VEIN);
        long nextPeriod = this.actionPeriod;

        if (blobTarget.isPresent()) {
            Point tgtPos = blobTarget.get().position;

            if (moveToOreBlob(world, blobTarget.get(), scheduler)) {
                Entity quake = Factory.createQuake(tgtPos,
                        imageStore.getImageList(QUAKE_KEY));

                world.addEntity(quake);
                nextPeriod += this.actionPeriod;
                quake.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                Factory.createActivityAction(this, world, imageStore),
                nextPeriod);
    }

    public void executeVeinActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround(this.position);

        if (openPt.isPresent()) {
            Entity ore = Factory.createOre(ORE_ID_PREFIX + this.id, openPt.get(),
                    ORE_CORRUPT_MIN + rand.nextInt(
                            ORE_CORRUPT_MAX - ORE_CORRUPT_MIN),
                    imageStore.getImageList(Functions.ORE_KEY));
            world.addEntity(ore);
            ore.scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                Factory.createActivityAction(this, world, imageStore),
                this.actionPeriod);
    }

    public void executeQuakeActivity(
            WorldModel world,
            EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }

    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {
        switch (this.kind) {
            case MINER_FULL:
                scheduler.scheduleEvent(this,
                        Factory.createActivityAction(this, world, imageStore),
                        this.actionPeriod);
                scheduler.scheduleEvent(this,
                        Factory.createAnimationAction(this, 0),
                        getAnimationPeriod());
                break;

            case MINER_NOT_FULL:
                scheduler.scheduleEvent(this,
                        Factory.createActivityAction(this, world, imageStore),
                        this.actionPeriod);
                scheduler.scheduleEvent(this,
                        Factory.createAnimationAction(this, 0),
                        getAnimationPeriod());
                break;

            case ORE:
                scheduler.scheduleEvent(this,
                        Factory.createActivityAction(this, world, imageStore),
                        this.actionPeriod);
                break;

            case ORE_BLOB:
                scheduler.scheduleEvent(this,
                        Factory.createActivityAction(this, world, imageStore),
                        this.actionPeriod);
                scheduler.scheduleEvent(this,
                        Factory.createAnimationAction(this, 0),
                        getAnimationPeriod());
                break;

            case QUAKE:
                scheduler.scheduleEvent(this,
                        Factory.createActivityAction(this, world, imageStore),
                        this.actionPeriod);
                scheduler.scheduleEvent(this, Factory.createAnimationAction(this,
                        QUAKE_ANIMATION_REPEAT_COUNT),
                        getAnimationPeriod());
                break;

            case VEIN:
                scheduler.scheduleEvent(this,
                        Factory.createActivityAction(this, world, imageStore),
                        this.actionPeriod);
                break;

            default:
        }
    }


    public int getAnimationPeriod() {
        switch (this.kind) {
            case MINER_FULL:
            case MINER_NOT_FULL:
            case ORE_BLOB:
            case QUAKE:
                return this.animationPeriod;
            default:
                throw new UnsupportedOperationException(
                        String.format("getAnimationPeriod not supported for %s",
                                this.kind));
        }
    }

    public void nextImage() {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }

    private boolean transformNotFull(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (this.resourceCount >= this.resourceLimit) {
            Entity miner = Factory.createMinerFull(this.id, this.resourceLimit,
                    this.position, this.actionPeriod,
                    this.animationPeriod,
                    this.images);

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            miner.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    private void transformFull(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        Entity miner = Factory.createMinerNotFull(this.id, this.resourceLimit,
                this.position, this.actionPeriod,
                this.animationPeriod,
                this.images);

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        miner.scheduleActions(scheduler, world, imageStore);
    }
    private Point nextPositionMiner(
            WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.x - this.position.x);
        Point newPos = new Point(this.position.x + horiz, this.position.y);

        if (horiz == 0 || world.isOccupied(newPos)) {
            int vert = Integer.signum(destPos.y - this.position.y);
            newPos = new Point(this.position.x, this.position.y + vert);

            if (vert == 0 || world.isOccupied(newPos)) {
                newPos = this.position;
            }
        }

        return newPos;
    }

    private Point nextPositionOreBlob(
            WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.x - this.position.x);
        Point newPos = new Point(this.position.x + horiz, this.position.y);

        Optional<Entity> occupant = world.getOccupant(newPos);

        if (horiz == 0 || (occupant.isPresent() && !(occupant.get().kind
                == EntityKind.ORE)))
        {
            int vert = Integer.signum(destPos.y - this.position.y);
            newPos = new Point(this.position.x, this.position.y + vert);
            occupant = world.getOccupant(newPos);

            if (vert == 0 || (occupant.isPresent() && !(occupant.get().kind
                    == EntityKind.ORE)))
            {
                newPos = this.position;
            }
        }

        return newPos;
    }


    private boolean moveToOreBlob(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (this.position.adjacent(target.position)) {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else {
            Point nextPos = this.nextPositionOreBlob(world, target.position);

            if (!this.position.equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    private boolean moveToNotFull(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (this.position.adjacent(target.position)) {
            this.resourceCount += 1;
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

            return true;
        }
        else {
            Point nextPos = this.nextPositionMiner(world, target.position);

            if (!this.position.equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    private boolean moveToFull(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (this.position.adjacent(target.position)) {
            return true;
        }
        else {
            Point nextPos = this.nextPositionMiner(world, target.position);

            if (!this.position.equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }


    public EntityKind getKind() {
        return kind;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point point) {
        this.position = point;
    }
}
