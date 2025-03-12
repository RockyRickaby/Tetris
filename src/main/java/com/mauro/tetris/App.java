package com.mauro.tetris;

import javax.swing.SwingUtilities;

import com.mauro.tetris.tetris.TetrisBoard;
import com.mauro.tetris.tetris.TetrisGUI;
import com.mauro.tetris.tetris.TetrisGame;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TetrisGUI(new TetrisGame(new TetrisBoard(), (long) 4e8));
            }
        });
    }
}
