package tetris;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import tetris.gui.TetrisRenderer;

/**
 * The TetrisGUI class merely serves as a Frame where
 * we can put the components necessary to make our game work.
 */
public class TetrisGUI extends JFrame {

    /**
     * Creates a new TetrisGUI
     * @param game the TetrisGame to be rendered.
     */
    public TetrisGUI(TetrisGame game) {
        this.add(mainPanel(game));
        this.setBackground(Color.BLACK);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Tetris");
        this.setResizable(true);
        this.setSize(800, 800);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    // we might use multiple panels for the different screens
    private JPanel mainPanel(TetrisGame game) {
        Dimension dim = new Dimension(800, 800);
        JPanel panel = new TetrisRenderer(game, dim);

        return panel;
    }
}
