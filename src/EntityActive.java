// for MinerFull, MinerNotFull, Ore, OreBlob, Vein, Quake

public interface EntityActive {

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler);

//    public int getActionPeriod();

    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore);

}
