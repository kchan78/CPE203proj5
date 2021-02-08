import processing.core.PImage;

public interface Entity
{
    public PImage getCurrentImage();

    public Point getPosition();

    public void setPosition(Point point);
}
