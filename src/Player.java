import java.awt.Image;
import java.awt.Color;
import java.awt.geom.Area;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.AffineTransform;

import javax.swing.ImageIcon;

public class Player {
    private double x;
    private double y;
    private double speed;
    private final int width;
    private final int height;
    private final Image image;
    private final double playerSize;

    public Player(int width, int height) {

        this.width = width;
        this.height = height;

        speed = 0;
        playerSize = 64;

        this.x =  (width / 2.0) - (playerSize / 2);
        this.y = (height - playerSize  - 20);

        image = new ImageIcon("icon\\plane.png").getImage();
    }

    public double getPlayerSize() {
        return playerSize;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void draw(Graphics2D graphics2D) {

        AffineTransform affineTransform = graphics2D.getTransform();

        graphics2D.translate(x, y);

        AffineTransform affineTransform1 = new AffineTransform();
        affineTransform1.rotate(Math.toRadians(-45), playerSize / 2, playerSize / 2);

        graphics2D.drawImage(image, affineTransform1, null);

        graphics2D.setTransform(affineTransform);

        //graphics2D.setColor(Color.GREEN);
        graphics2D.draw(getArea().getBounds2D());
    }

    public Area getArea() {

        Rectangle2D rectangle2D = new Rectangle2D.Double();

        rectangle2D.setRect(0, 0, playerSize, playerSize);

        AffineTransform affineTransform = new AffineTransform();
        affineTransform.translate(x, y);
        affineTransform.rotate(Math.toRadians(-45), (playerSize / 2), (playerSize / 2));

        return new Area(affineTransform.createTransformedShape(new Area(rectangle2D)));
    }

    public void move(boolean direction, boolean stop) {

        if (stop) {

            speed -= 0.15;

            if (speed < 0) {
                speed = 0;
            }

        } else {

            speed += 1;

            if (speed > 10) {
                speed = 10;
            }
        }


        if (direction) {
            x += speed;
        } else {
            x -= speed;
        }

        if (x < 0) {
            x = 0;
        }

        if (x > (width - playerSize)) {
            x = (width - playerSize);
        }
    }
}
