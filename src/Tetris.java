import javax.swing.SwingUtilities;

import tetris.TetrisBoard;
import tetris.TetrisGUI;
import tetris.TetrisGame;

public class Tetris {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TetrisGUI(new TetrisGame(new TetrisBoard(), (long) 4e8));
            }
        });
    }
}
