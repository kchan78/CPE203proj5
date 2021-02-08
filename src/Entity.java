import processing.core.PImage;

public interface Entity
{
    public PImage getCurrentImage();

    public void nextImage();

    // public EntityKind getKind();

    public Point getPosition();

    public void setPosition(Point point);
}
