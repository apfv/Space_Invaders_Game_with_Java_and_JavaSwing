import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.geom.Area;
import java.awt.Graphics2D;
import java.awt.FontMetrics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;
import javax.swing.JPanel;

import java.util.Random;
import java.util.ArrayList;

public class Panel extends JPanel implements ActionListener {
    private int score;
    private boolean stop;
    private boolean shot;
    private final int width;
    private final int height;
    private boolean gameOver;
    private boolean direction;
    private final Sound sound;
    private final Player player;
    private final Random random;
    private final ArrayList<Bullet> bullets;
    private final ArrayList<Rocket> rockets;
    private final ArrayList<Effect> effects;

    public Panel() {

        width = 700;
        height = 650;

        stop = true;
        shot = false;
        gameOver = false;
        direction = true;

        sound = new Sound();
        random = new Random();
        bullets = new ArrayList<>();
        rockets = new ArrayList<>();
        effects = new ArrayList<>();

        player = new Player(width, height);

        new Timer(1, this).start();

        this.setFocusable(true);
        this.setBackground(new Color(50, 50, 50));
        this.setPreferredSize(new Dimension(width, height));
        this.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);

                if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
                    direction = true;
                    stop = false;
                }

                if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
                    direction = false;
                    stop = false;
                }

                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    shot = true;
                    sound.soundShoot();
                }

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (gameOver) {
                        gameOver = false;
                        score = 0;
                        rockets.clear();
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);

                if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
                    stop = true;
                }

                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    shot = false;
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!effects.isEmpty()) {
            for (Effect effect : effects) {
                effect.draw((Graphics2D) g);
            }
        }

        if (rockets.isEmpty()) {
            rockets.add(new Rocket(random.nextInt(0, 14), width, height));
        } else {
            if (rockets.get(rockets.size() - 1).getY() > 50) {
                rockets.add(new Rocket(random.nextInt(0, 14), width, height));
            }
        }

        if (!rockets.isEmpty()) {
            for (Rocket rocket : rockets) {
                rocket.draw((Graphics2D) g);
            }
        }

        if (gameOver) {

            bullets.clear();

            g.setColor(Color.WHITE);
            g.setFont(getFont().deriveFont(Font.BOLD, 50));
            FontMetrics fontMetrics1 = getFontMetrics(g.getFont());
            g.drawString("GAME OVER", width / 2 - fontMetrics1.stringWidth("GAME OVER") / 2, height / 2 - 50);

            g.setFont(getFont().deriveFont(Font.BOLD, 16));
            FontMetrics fontMetrics2 = getFontMetrics(g.getFont());
            g.drawString("Press key enter to Continue ...", width / 2 - fontMetrics2.stringWidth("Press key enter to Continue ...") / 2, height / 2);

        } else {

            player.draw((Graphics2D) g);

            if (shot) {
                Bullet bullet = new Bullet(player.getX() + (player.getPlayerSize() / 2), player.getY() + (player.getPlayerSize() / 2));
                bullet.draw((Graphics2D) g);
                bullets.add(bullet);
            }

            if (!bullets.isEmpty()) {
                for (Bullet bullet : bullets) {
                    bullet.draw((Graphics2D) g);
                }
            }
        }

        g.setColor(Color.WHITE);
        g.setFont(getFont().deriveFont(Font.BOLD, 18));
        g.drawString("Score : " + score, 10, 30);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (!gameOver) {
            player.move(direction, stop);
        }

        if (!bullets.isEmpty()) {
            for (int i = 0; i < bullets.size(); i++) {
                if (i >= 1) {
                    if ((bullets.get(i).getY() - bullets.get(i - 1).getY()) > 80) {
                        if (bullets.get(i).move()) {
                            bullets.remove(bullets.get(i));
                        } else {
                            check(bullets.get(i));
                        }
                    } else {
                        bullets.remove(bullets.get(i));
                    }
                } else {
                    if (bullets.get(i).move()) {
                        bullets.remove(bullets.get(i));
                    } else {
                        check(bullets.get(i));
                    }
                }
            }
        }

        if (!rockets.isEmpty()) {
            for (int i = 0; i < rockets.size(); i++) {
                if (rockets.get(i).move()) {
                    rockets.remove(rockets.get(i));
                } else {

                    if (!gameOver) {

                        Area area = new Area(player.getArea());
                        area.intersect(rockets.get(i).getArea());

                        if (!area.isEmpty()) {

                            gameOver = true;

                            effect(player.getX(), player.getY());

                            sound.soundDestroy();

                            rockets.remove(rockets.get(i));
                        }
                    }
                }
            }
        }

        if (!effects.isEmpty()) {

            for (int i = 0; i < effects.size(); i++) {

                Effect effect = effects.get(i);

                effect.update();

                if (!effect.check()) {
                    effects.remove(effect);
                }
            }
        }

        repaint();
    }

    private void check(Bullet bullet) {

        if (!rockets.isEmpty()) {

            for (int i = 0; i < rockets.size(); i++) {

                Area area = new Area(bullet.getArea());
                area.intersect(rockets.get(i).getArea());

                if (!area.isEmpty()) {

                    score++;

                    effect(bullet.getX(), bullet.getY());

                    sound.soundDestroy();

                    bullets.remove(bullet);
                    rockets.remove(rockets.get(i));
                }
            }
        }
    }

    private void effect(double x, double y) {

        effects.add(new Effect(x, y, 5, 10, 1000, 0.5f, new Color(4, 142, 227)));
        effects.add(new Effect(x, y, 10, 5, 1000, 0.6f, new Color(18, 253, 1)));
        effects.add(new Effect(x, y, 5, 10, 1000, 0.7f, new Color(243, 238, 0)));
        effects.add(new Effect(x, y, 10, 5, 1000, 0.8f, new Color(199, 15, 15)));
        effects.add(new Effect(x, y, 5, 10, 1000, 0.9f, new Color(169, 34, 182)));
        effects.add(new Effect(x, y, 10, 5, 1000, 1.0f, new Color(255, 116, 0)));
    }
}
