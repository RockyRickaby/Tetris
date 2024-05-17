package tetris.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import enums.Actions;
import pieces.Block;
import pieces.Tetromino;
import tetris.TetrisBoard;
import tetris.TetrisGame;

public class TetrisRenderer extends JPanel {
    private TetrisGame game;

    private float blockScale;
    private float boardXOffset;

    public TetrisRenderer(TetrisGame game, Dimension preferredSize) {
        this.game = game;

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                float height = getHeight();
                float width = getWidth();

                blockScale = height / game.getTetrisBoard().getHeight();
                boardXOffset =  width / 8; 
            }
        });

        InputMap inputmap = this.getInputMap();
        ActionMap actionmap = this.getActionMap();

        inputmap.put(KeyStroke.getKeyStroke("UP"), "R_CLOCKWISE");
        inputmap.put(KeyStroke.getKeyStroke("X"), "R_CLOCKWISE");
        actionmap.put("R_CLOCKWISE", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                game.moveCurrentTetromino(Actions.ROTATE_CLOCKWISE);
                repaint();
            }
            
        });

        inputmap.put(KeyStroke.getKeyStroke("CTRL"), "R_COUNTERCLOCKWISE");
        inputmap.put(KeyStroke.getKeyStroke("Z"), "R_COUNTERCLOCKWISE");
        actionmap.put("R_COUNTERCLOCKWISE", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                game.moveCurrentTetromino(Actions.ROTATE_COUNTERCLOCKWISE);
                repaint();
            }
            
        });

        inputmap.put(KeyStroke.getKeyStroke("SPACE"), "HARD_DROP");
        actionmap.put("HARD_DROP", new AbstractAction() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                game.moveCurrentTetromino(Actions.HARD_DROP);
                repaint();
            }
            
        });
        
        inputmap.put(KeyStroke.getKeyStroke("LEFT"), "MOVE_LEFT");
        actionmap.put("MOVE_LEFT", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                game.moveCurrentTetromino(Actions.MOVE_LEFT);
                repaint();
            }
        });

        inputmap.put(KeyStroke.getKeyStroke("RIGHT"), "MOVE_RIGHT");
        actionmap.put("MOVE_RIGHT", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                game.moveCurrentTetromino(Actions.MOVE_RIGHT);
                repaint();
            }
        });

        inputmap.put(KeyStroke.getKeyStroke("DOWN"), "MOVE_DOWN");
        actionmap.put("MOVE_DOWN", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                game.moveCurrentTetromino(Actions.MOVE_DOWN);
                repaint();
            }
        });

        this.setPreferredSize(preferredSize);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
        TetrisBoard board = game.getTetrisBoard();

        Rectangle2D.Float rect = new Rectangle2D.Float();
                
        // draws grid
        for (int i = 1; i <= board.getHeight(); i++) {
            for (int j = 1; j <= board.getWidth(); j++) {
                Block block = board.getBlockAtCell(j, i);
                if (block == null) {
                    continue;
                }
                rect.setFrame((j - 1) * blockScale + boardXOffset, (board.getHeight() - i) * blockScale, blockScale, blockScale);

                g2d.setColor(block.getColor());
                g2d.fill(rect);
                g2d.setColor(Color.BLACK);
                g2d.draw(rect);
            }
        }
                
        // draws current Tetromino
        g2d.setColor(Color.BLACK);
        Tetromino piece = board.getCurrentTetromino();
        for (Block b : piece.getBlocks()) {
            float x = b.getX() + piece.getPosition().x;
            float y = b.getY() + piece.getPosition().y;

            rect.setFrame((x - 1) * blockScale + boardXOffset, (board.getHeight() - y) * blockScale, blockScale, blockScale);
            g2d.setColor(b.getColor());
            g2d.fill(rect);
            g2d.setColor(Color.BLACK);
            g2d.draw(rect);
        }

        // draws next piece block
        rect.setFrame(boardXOffset, 0, board.getWidth() * blockScale, board.getHeight() * blockScale);

        g2d.setColor(Color.WHITE);
        g2d.draw(rect);
        g2d.setColor(Color.BLACK);
    }  
}
