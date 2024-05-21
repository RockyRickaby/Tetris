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
import tetris.Timer;

/**
 * The TetrisRenderer class serves to render the TetrisGame
 * class on the screen and to send user input to the game.
 * That's it.
 */
public class TetrisRenderer extends JPanel {
    private static final int NEXT_PIECE_GRID_SIZE = 5;
    private static final int DELAY_MS = 33;
    
    private static final String ACTION_TOGGLE_GHOST_PIECE = "TOGGLE_GHOST"; 
    private static final String ACTION_PAUSE = "PAUSE";

    private static final String ACTION_ROTATE_CCW = "ROTATE_CCW";
    private static final String ACTION_ROTATE_CW = "ROTATE_CW";
    private static final String ACTION_HARD_DROP = "HARD_DROP";
    
    private static final String ACTION_MOVE_RIGHT = "MOVE_RIGHT";
    private static final String ACTION_MOVE_DOWN = "MOVE_DOWN";
    private static final String ACTION_MOVE_LEFT = "MOVE_LEFT";

    private TetrisGame game;
    private TetrisBoard board;

    private javax.swing.Timer gameTimer;
    private Timer timer;

    private float blockScale;
    private float boardXOffset;

    private float nextpieceXOffset;

    /**
     * Creates a new TetrisRenderer for the {@code game} with
     * preferred dimensions as {@code preferredSize}.
     * @param game
     * @param preferredSize
     */
    public TetrisRenderer(TetrisGame game, Dimension preferredSize) {
        this.game = game;
        this.board = game.getTetrisBoard();

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                float height = getHeight();
                float width = getWidth();

                blockScale = height / (board.getHeight() + 2);
                boardXOffset =  width / 6; 

                nextpieceXOffset = boardXOffset + blockScale * (board.getWidth() + 4);
            }
        });

        InputMap inputmap = this.getInputMap();
        ActionMap actionmap = this.getActionMap();

        inputmap.put(KeyStroke.getKeyStroke("UP"), ACTION_ROTATE_CW);
        inputmap.put(KeyStroke.getKeyStroke("X"), ACTION_ROTATE_CW);
        actionmap.put(ACTION_ROTATE_CW, new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameTimer.isRunning()) {
                    return;
                }
                game.moveCurrentTetromino(Actions.ROTATE_CLOCKWISE);
                getRootPane().repaint();
            }
            
        });

        inputmap.put(KeyStroke.getKeyStroke("CTRL"), ACTION_ROTATE_CCW);
        inputmap.put(KeyStroke.getKeyStroke("Z"), ACTION_ROTATE_CCW);
        actionmap.put(ACTION_ROTATE_CCW, new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameTimer.isRunning()) {
                    return;
                }
                game.moveCurrentTetromino(Actions.ROTATE_COUNTERCLOCKWISE);
                getRootPane().repaint();
            }
            
        });

        inputmap.put(KeyStroke.getKeyStroke("SPACE"), ACTION_HARD_DROP);
        actionmap.put(ACTION_HARD_DROP, new AbstractAction() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameTimer.isRunning()) {
                    return;
                }
                game.moveCurrentTetromino(Actions.HARD_DROP);
                getRootPane().repaint();
            }
            
        });
        
        inputmap.put(KeyStroke.getKeyStroke("LEFT"), ACTION_MOVE_LEFT);
        actionmap.put(ACTION_MOVE_LEFT, new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameTimer.isRunning()) {
                    return;
                }
                game.moveCurrentTetromino(Actions.MOVE_LEFT);
                getRootPane().repaint();
            }
        });

        inputmap.put(KeyStroke.getKeyStroke("RIGHT"), ACTION_MOVE_RIGHT);
        actionmap.put(ACTION_MOVE_RIGHT, new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameTimer.isRunning()) {
                    return;
                }
                game.moveCurrentTetromino(Actions.MOVE_RIGHT);
                getRootPane().repaint();
            }
        });

        inputmap.put(KeyStroke.getKeyStroke("DOWN"), ACTION_MOVE_DOWN);
        actionmap.put(ACTION_MOVE_DOWN, new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameTimer.isRunning()) {
                    return;
                }
                game.moveCurrentTetromino(Actions.MOVE_DOWN);
                getRootPane().repaint();
            }
        });

        inputmap.put(KeyStroke.getKeyStroke("G"), ACTION_TOGGLE_GHOST_PIECE);
        actionmap.put(ACTION_TOGGLE_GHOST_PIECE, new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameTimer.isRunning()) {
                    return;
                }
                game.toggleGhostPiece();
                getRootPane().repaint();
            }
            
        });

        inputmap.put(KeyStroke.getKeyStroke("ESCAPE"), ACTION_PAUSE);
        actionmap.put(ACTION_PAUSE, new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameTimer.isRunning()) {
                    gameTimer.stop();
                } else {
                    timer.reset();
                    gameTimer.start();
                }
            }
            
        });

        timer = new Timer();
        // ~30 fps
        gameTimer = new javax.swing.Timer(DELAY_MS, e -> {
            game.update(timer.getTimeElapsed());
            getRootPane().repaint();
        });
        gameTimer.start();

        this.setPreferredSize(preferredSize);
    }

    /**
     * Draws a block as defined by {@code rect}
     * with contour {@code contourColor} and 
     * fill color {@code fillColor}
     * 
     * @param g2d
     * @param rect
     * @param fillColor
     * @param contourColor
     */
    private void drawBlock(Graphics2D g2d, Rectangle2D.Float rect, Color fillColor, Color contourColor) {
        if (fillColor != null) {
            g2d.setColor(fillColor);
            g2d.fill(rect);
        }
        if (contourColor != null) {
            g2d.setColor(contourColor);
            g2d.draw(rect);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int boardWidth = board.getWidth();
        int boardHeight = board.getHeight();

        Rectangle2D.Float rect = new Rectangle2D.Float();
        Color gray = Color.GRAY;

        for (int i = 0; i <= boardHeight + 1; i++) {
            rect.setFrame(boardXOffset - blockScale, i * blockScale, blockScale, blockScale);
            drawBlock(g2d, rect, gray, Color.BLACK);

            rect.setFrame(boardXOffset + boardWidth * blockScale, i * blockScale, blockScale, blockScale);
            drawBlock(g2d, rect, gray, Color.BLACK);
        }

        for (int i = 0; i < boardWidth + 1; i++) {
            rect.setFrame(boardXOffset + i * blockScale, 0, blockScale, blockScale);
            drawBlock(g2d, rect, gray, Color.BLACK);

            rect.setFrame(boardXOffset + i * blockScale, (boardHeight + 1) * blockScale, blockScale, blockScale);
            drawBlock(g2d, rect, gray, Color.BLACK);
        }

        // draws grid
        for (int i = 1; i <= boardHeight; i++) {
            for (int j = 1; j <= boardWidth; j++) {
                Block block = board.getBlockAtCell(j, i);
                if (block == null) {
                    continue;
                }
                rect.setFrame((j - 1) * blockScale + boardXOffset, (boardHeight - i + 1) * blockScale, blockScale, blockScale);
                drawBlock(g2d, rect, block.getColor(), Color.BLACK);
            }
        }

        // draws current Tetromino and GhostPiece
        Tetromino piece = board.getCurrentTetromino();
        Tetromino ghostPiece = board.getGhostPiece();

        if (ghostPiece != null) {
            for (Block b : ghostPiece.getBody()) {
                float x = b.getX() + ghostPiece.getPosition().x;
                float y = b.getY() + ghostPiece.getPosition().y;
    
                rect.setFrame((x - 1) * blockScale + boardXOffset, (boardHeight - y + 1) * blockScale, blockScale, blockScale);
                drawBlock(g2d, rect, null, b.getColor());
            }
        }

        for (Block b : piece.getBody()) {
            float x = b.getX() + piece.getPosition().x;
            float y = b.getY() + piece.getPosition().y;

            rect.setFrame((x - 1) * blockScale + boardXOffset, (boardHeight - y + 1) * blockScale, blockScale, blockScale);
            drawBlock(g2d, rect, b.getColor(), Color.BLACK);
        }
        
        final int countMax = 6;
        int count = 0;
        float yQueueOffset = 0;
        for (Tetromino nextPiece : game.getNextPieceQueue()) {
            if (count >= countMax) {
                break;
            }

            Block[] nextPieceBlocks = nextPiece.getBody();
    
            int maxX = Integer.MIN_VALUE;
            for (Block b : nextPieceBlocks) {
                maxX = Math.max(maxX, (int) b.getX());
            }
            // draws next piece thingy
            float subgridXOffset = (maxX) % 2 == 0 ? 1 : 0.5f;
            float subgridYOffset = 0;
                
            // special case. this one is rendered relatively centralized on the subgrid
            if (nextPiece.getTetrominoName().equals("O")) {
                subgridXOffset = subgridXOffset + 1;
                subgridYOffset = 1;
            }
            
            for (Block b : nextPieceBlocks) {
                float x = b.getX();
                float y = b.getY();
            
                rect.setFrame((x + subgridXOffset) * blockScale + nextpieceXOffset, (NEXT_PIECE_GRID_SIZE / 2 + 2 - y - subgridYOffset + .5 + yQueueOffset) * blockScale, blockScale, blockScale);
                drawBlock(g2d, rect, b.getColor(), Color.BLACK);
            }

            count++;
            yQueueOffset += 3.80;
        }
        yQueueOffset += 3.80;

        rect.setFrame(nextpieceXOffset, blockScale , NEXT_PIECE_GRID_SIZE * blockScale, (yQueueOffset - 3) * blockScale);
        g2d.setStroke(new BasicStroke(3.25F));
        drawBlock(g2d, rect, null, Color.GRAY);
    }  
}
