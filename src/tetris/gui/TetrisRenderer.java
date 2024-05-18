package tetris.gui;

import java.awt.BasicStroke;
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
    private static final int NEXT_PIECE_GRID_SIZE = 5;

    private TetrisGame game;

    private float blockScale;
    private float boardXOffset;

    private float nextpieceXOffset;
    private float nextpieceYOffset;

    public TetrisRenderer(TetrisGame game, Dimension preferredSize) {
        this.game = game;

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                float height = getHeight();
                float width = getWidth();

                TetrisBoard b = game.getTetrisBoard();

                blockScale = height / (b.getHeight() + 2);
                boardXOffset =  width / 8; 

                nextpieceXOffset = boardXOffset + blockScale * (b.getWidth() + 4);
                nextpieceYOffset = blockScale * (b.getHeight() - b.getHeight() / 2);
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

        inputmap.put(KeyStroke.getKeyStroke("G"), "TOGGLE_GHOST");
        actionmap.put("TOGGLE_GHOST", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                game.toggleGhostPiece();
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

        int boardWidth = board.getWidth();
        int boardHeight = board.getHeight();

        Rectangle2D.Float rect = new Rectangle2D.Float();
        Color gray = Color.GRAY.brighter();

        for (int i = 0; i <= boardHeight + 1; i++) {
            rect.setFrame(boardXOffset - blockScale, i * blockScale, blockScale, blockScale);
            g2d.setColor(gray);
            g2d.fill(rect);
            g2d.setColor(Color.BLACK);
            g2d.draw(rect);

            rect.setFrame(boardXOffset + boardWidth * blockScale, i * blockScale, blockScale, blockScale);
            g2d.setColor(gray);
            g2d.fill(rect);
            g2d.setColor(Color.BLACK);
            g2d.draw(rect);
        }

        for (int i = 0; i < boardWidth + 1; i++) {
            rect.setFrame(boardXOffset + i * blockScale, 0, blockScale, blockScale);
            g2d.setColor(gray);
            g2d.fill(rect);
            g2d.setColor(Color.BLACK);
            g2d.draw(rect);

            rect.setFrame(boardXOffset + i * blockScale, (boardHeight + 1) * blockScale, blockScale, blockScale);
            g2d.setColor(gray);
            g2d.fill(rect);
            g2d.setColor(Color.BLACK);
            g2d.draw(rect);
        }
                
        // draws grid
        for (int i = 1; i <= boardHeight; i++) {
            for (int j = 1; j <= boardWidth; j++) {
                Block block = board.getBlockAtCell(j, i);
                if (block == null) {
                    continue;
                }
                rect.setFrame((j - 1) * blockScale + boardXOffset, (boardHeight - i + 1) * blockScale, blockScale, blockScale);

                g2d.setColor(block.getColor());
                g2d.fill(rect);
                g2d.setColor(Color.BLACK);
                g2d.draw(rect);
            }
        }
                
        // draws current Tetromino and GhostPiece
        g2d.setColor(Color.BLACK);
        Tetromino piece = board.getCurrentTetromino();
        Tetromino ghostPiece = board.getGhostPiece();

        if (ghostPiece != null) {
            for (Block b : ghostPiece.getBlocks()) {
                float x = b.getX() + ghostPiece.getPosition().x;
                float y = b.getY() + ghostPiece.getPosition().y;
    
                rect.setFrame((x - 1) * blockScale + boardXOffset, (boardHeight - y + 1) * blockScale, blockScale, blockScale);
                g2d.setColor(b.getColor());
                // g2d.fill(rect);
                // g2d.setColor(Color.BLACK);
                g2d.draw(rect);
            }
        }

        for (Block b : piece.getBlocks()) {
            float x = b.getX() + piece.getPosition().x;
            float y = b.getY() + piece.getPosition().y;

            rect.setFrame((x - 1) * blockScale + boardXOffset, (boardHeight - y + 1) * blockScale, blockScale, blockScale);
            g2d.setColor(b.getColor());
            g2d.fill(rect);
            g2d.setColor(Color.BLACK);
            g2d.draw(rect);
        }

        Tetromino nextPiece = game.getNextPiece();
        Block[] nextPieceBlocks = nextPiece.getBlocks();

        int maxX = Integer.MIN_VALUE;
        for (Block b : nextPieceBlocks) {
            maxX = Math.max(maxX, (int) b.getX());
        }
        
        // this part has been indented and put under its own scope
        // to express how I don't really like it.
        // I like it so little that I had to make it stand out
        {
            // draws next piece thingy
            float subgridXOffset = (maxX) % 2 == 0 ? 1 : 0.5f;
            float subgridYOffset = 0;
        
            rect.setFrame(nextpieceXOffset, nextpieceYOffset + blockScale, NEXT_PIECE_GRID_SIZE * blockScale, NEXT_PIECE_GRID_SIZE * blockScale);
        
            // special case. this one is rendered relatively centralized on the subgrid
            if (nextPiece.getTetrominoName().equals("O")) {
                subgridXOffset = subgridXOffset + 1;
                subgridYOffset = 1;
            }
        
            for (Block b : nextPieceBlocks) {
                float x = b.getX();
                float y = b.getY();
            
                rect.setFrame((x + subgridXOffset) * blockScale + nextpieceXOffset, (NEXT_PIECE_GRID_SIZE / 2 + 2 - y - subgridYOffset + 1) * blockScale + nextpieceYOffset, blockScale, blockScale);
                g2d.setColor(b.getColor());
                g2d.fill(rect);
                g2d.setColor(Color.BLACK);
                g2d.draw(rect);
            }
        }

        rect.setFrame(nextpieceXOffset, nextpieceYOffset + blockScale, NEXT_PIECE_GRID_SIZE * blockScale, NEXT_PIECE_GRID_SIZE * blockScale);
        
        g2d.setStroke(new BasicStroke(3.25F));
        g2d.setColor(Color.GRAY);
        g2d.draw(rect);

        // draws board's limits
        // rect.setFrame(boardXOffset, blockScale, boardWidth * blockScale, boardHeight * blockScale);
        // g2d.draw(rect);
    }  
}
