import java.awt.Image;
import java.awt.Color;
import java.awt.geom.Area;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.AffineTransform;

import javax.swing.ImageIcon;

public class Rocket {
    private double x;
    private double y;
    private final Image image;
    private final double rocketSize;
    private int width;
    private int height;
    private Area area;

    public Rocket(int rocketPlace, int width, int height) {

        rocketSize = 50;

        this.x = rocketPlace * rocketSize;
        this.y = -50;

        this.width = width;
        this.height = height;

        image = new ImageIcon("icon\\rocket.png").getImage();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRocketSize() {
        return rocketSize;
    }

    public void draw(Graphics2D graphics2D) {

        AffineTransform affineTransform = graphics2D.getTransform();

        graphics2D.translate(x, y);

        AffineTransform affineTransform1 = new AffineTransform();
        affineTransform1.rotate(Math.toRadians(135), (rocketSize / 2), (rocketSize / 2));

        graphics2D.drawImage(image, affineTransform1, null);

        graphics2D.setTransform(affineTransform);

        //graphics2D.setColor(Color.RED);
        graphics2D.draw(getArea().getBounds2D());
    }

    public Area getArea() {

        Rectangle2D rectangle2D = new Rectangle2D.Double();

        rectangle2D.setRect(0, 0, rocketSize, rocketSize);

        AffineTransform affineTransform = new AffineTransform();
        affineTransform.translate(x, y);
        affineTransform.rotate(Math.toRadians(135), (rocketSize / 2), (rocketSize / 2));

        return new Area(affineTransform.createTransformedShape(new Area(rectangle2D)));
    }

    public boolean move() {

        y += 1;

        return y > height;
    }
}
