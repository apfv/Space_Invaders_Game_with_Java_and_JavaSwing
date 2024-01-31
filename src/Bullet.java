import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.AffineTransform;

public class Bullet {
    private double x;
    private double y;
    private final Shape shape;

    public Bullet(double x, double y) {

        this.x = x;
        this.y = y;

        shape = new Rectangle2D.Double(0, 0, 2, 20);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void draw(Graphics2D graphics2D) {

        AffineTransform affineTransform = graphics2D.getTransform();

        graphics2D.setColor(Color.YELLOW);
        graphics2D.translate(x, y);
        graphics2D.fill(shape);

        graphics2D.setTransform(affineTransform);
    }

    public Area getArea() {
        return new Area(new Rectangle2D.Double(x, y, 2, 20));
    }

    public boolean move() {

        y -= 20;

        return y < 0;
    }
}
