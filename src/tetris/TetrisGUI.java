package tetris;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import tetris.gui.TetrisRenderer;

public class TetrisGUI extends JFrame {
    private static final int DELAY_MS = 33;
    
    private Timer timer;
    private javax.swing.Timer gameTimer;

    public TetrisGUI(TetrisGame game) {
        this.add(mainPanel(game));
        this.setBackground(Color.BLACK);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Tetris");
        this.setResizable(true);
        this.setSize(800, 800);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        timer = new Timer();
        // ~30 fps
        gameTimer = new javax.swing.Timer(DELAY_MS, e -> {
            game.update(timer.getTimeElapsed());
            repaint();
        });
        gameTimer.start();
        System.out.println(this.getContentPane().getSize());
    }

    // we might use multiple panels for the different screens
    private JPanel mainPanel(TetrisGame game) {
        return new TetrisRenderer(game, new Dimension(800, 800));
    }
}
