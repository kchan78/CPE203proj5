public final class Event
{
    private final Action action;
    private final long time;
    private final EntityActive entity;

    public Event(Action action, long time, EntityActive entity) {
        this.action = action;
        this.time = time;
        this.entity = entity;
    }

    public Action getAction() {
        return action;
    }

    public long getTime() {
        return time;
    }

    public EntityActive getEntity() {
        return entity;
    }
}
