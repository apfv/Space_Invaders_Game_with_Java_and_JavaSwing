import javax.swing.*;

public class Frame extends JFrame {
    public Frame() {
        this.setTitle("Space Invaders Game");
        this.setIconImage(new ImageIcon("icon\\plane_speed.png").getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.add(new Panel());
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
