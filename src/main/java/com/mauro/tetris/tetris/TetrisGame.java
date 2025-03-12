package com.mauro.tetris.tetris;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.mauro.tetris.enums.Actions;
import com.mauro.tetris.pieces.Tetromino;
import com.mauro.tetris.pieces.TetrominoFactory;

/**
 * The TetrisGame class manages the game and its more general
 * rules.
 */
public class TetrisGame {
    private static final long LOCK_DELAY = (long) 5e8;

    private List<Tetromino> pieces;
    private Queue<Tetromino> bag;

    private TetrisBoard board;

    private long elapsedTimeAccumulator;
    private long lockDelayAccumulator;
    private long updateInterval;

    private int nextPieceIdx;

    private boolean isOver;
    private boolean hasJustMoved;

    /**
     * Creates a new TetrisGame with TetrisBoard {@code board}
     * and a specific update interval (given in nanoseconds)
     * in which the piece moves down.
     * @param board
     * @param updateIntervalInNanoSeconds the update interval in nanoseconds
     */
    public TetrisGame(TetrisBoard board, long updateIntervalInNanoSeconds) {
        this.board = board;

        TetrominoFactory fac = new TetrominoFactory(board.getWidth(), board.getHeight());

        this.nextPieceIdx = 0;
        this.pieces = fac.getPieces();
        Collections.shuffle(pieces);
        this.bag = new LinkedList<>(pieces);

        this.setPieces7bag();

        this.elapsedTimeAccumulator = this.lockDelayAccumulator = 0;
        this.updateInterval = updateIntervalInNanoSeconds;
        this.isOver = false;
        this.hasJustMoved = false;
    }

    /**
     * Toggles the ghost piece on/off.
     * @return {@code true} if the ghost piece is toggled on. {@code false}
     * if otherwise.
     */
    public boolean toggleGhostPiece() {
        return this.board.toggleGhostPiece();
    }

    /**
     * Returns the immediate next piece available in the
     * next piece queue (but does not remove it from the queue).
     * @return the immediate next piece available in the queue.
     */
    public Tetromino getNextPiece() {
        return this.bag.peek();
    }

    /**
     * Returns a list containing all the pieces used in this TetrisGame.
     * @return a list of Tetrominoes.
     */
    public List<Tetromino> getPieces() {
        return pieces;
    }

    /**
     * Returns the TetrisBoard used in this TetrisGame.
     * @return the TetrisBoard.
     */
    public TetrisBoard getTetrisBoard() {
        return this.board;
    }

    /**
     * Returns the update interval (in nanoseconds).
     * @return update interval.
     */
    public long getInterval() {
        return this.updateInterval;
    }

    /**
     * Sets the update interval to {@code intervalNanoseconds}
     * @param intervalInNanoseconds the new update interval.
     */
    public void setInterval(long intervalInNanoseconds) {
        this.updateInterval = intervalInNanoseconds;
    }

    /**
     * Returns the next piece queue. The queue contains 7 items.
     * @return the next piece queue.
     */
    public Queue<Tetromino> getNextPieceQueue() {
        return this.bag;
    }

    /**
     * Sets the next piece and manages the piece bags.
     */
    private void setPieces7bag() {
        if (nextPieceIdx <= 0) {
            Collections.shuffle(pieces);
        }

        if (!board.setCurrentTetromino(bag.poll().copy())) {
            this.reset();
            return;
        }

        bag.offer(pieces.get(nextPieceIdx));
        nextPieceIdx = (nextPieceIdx + 1) % pieces.size();
    }

    /**
     * Clears n rows if necessary.
     * @return {@code true} if any rows have been cleared.
     * {@code false} if none have been cleared.
     */
    private boolean clearRows() {
        int boardRows = board.getHeight();
        int boardColumns = board.getWidth();

        int piecesPerRow = -1;
        ArrayList<Integer> rowsToClear = new ArrayList<>();
        for (int i = 1; i <= boardRows; i++) {
            piecesPerRow = board.getAmountOfBlocksInRow(i);
            if (piecesPerRow == boardColumns) {
                rowsToClear.add(i);
                board.clearRow(i);
            }
        }
        if (rowsToClear.isEmpty()) {
            return false;
        }

        // ideally, we'd want to do this in a single pass
        // but this way is working as intended
        rowsToClear.sort(Collections.reverseOrder());
        for (int row : rowsToClear) {
            board.pullAboveBlocksDownFrom(row);
        }
        return true;
    }

    /**
     * Places the current Tetromino and 
     * updates the next ones.
     */
    private void placeAndSetTetromino() {
        board.placeCurrTetromino();
        clearRows();
        setPieces7bag();
    }

    /**
     * Moves the current Tetromino according to {@code movement}.
     * @param movement where/how to move the piece 
     * @return {@code true} if the movement was successful.
     */
    public boolean moveCurrentTetromino(Actions movement) {
        switch (movement) {
            case HARD_DROP:
                board.hardDropCurrTetromino();
                placeAndSetTetromino();
                this.elapsedTimeAccumulator = 0;
                this.lockDelayAccumulator = 0;
                return true;
            case MOVE_DOWN:
                this.elapsedTimeAccumulator = 0;
                return (board.moveCurrTetrominoDown());
            case MOVE_LEFT:
                return (hasJustMoved = board.moveCurrTetrominoLeft());
            case MOVE_RIGHT:
                return (hasJustMoved = board.moveCurrTetrominoRight());
            case ROTATE_CLOCKWISE:
                return (hasJustMoved = board.rotateCurrTetrominoClockwise());
            case ROTATE_COUNTERCLOCKWISE:
                return (hasJustMoved = board.rotateCurrTetrominoCounterclockwise());
            default:
                return false;
        }
    }

    /**
     * Updates the state of the game.
     * @param timeElapsed the elapsed time since the last update.
     */
    public void update(long timeElapsed) {
        this.elapsedTimeAccumulator += timeElapsed;
        if (this.isOver) {
            this.reset();
            return;
        }

        if (this.elapsedTimeAccumulator >= updateInterval) {
            moveCurrentTetromino(Actions.MOVE_DOWN);
        }

        if (board.isDownwardsMovementObstructed() && !hasJustMoved) {
            lockDelayAccumulator += timeElapsed;
            if (lockDelayAccumulator >= LOCK_DELAY) {
                placeAndSetTetromino();
                lockDelayAccumulator = 0;
            }
        } else {
            lockDelayAccumulator = 0;
            hasJustMoved = false;
        }
    }

    /**
     * Resets the whole thing.
     */
    public void reset() {
        this.board.clearAll();

        this.nextPieceIdx = 0;

        this.bag.clear();
        
        Collections.shuffle(pieces);
        this.bag.addAll(pieces);

        this.setPieces7bag();
        // this.setPieces();
        this.isOver = false;
    }
}
