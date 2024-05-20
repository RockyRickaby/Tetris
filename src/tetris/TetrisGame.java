package tetris;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import enums.Actions;
import pieces.Tetromino;
import pieces.TetrominoFactory;

// TODO - CONSIDER IMPLEMENTING A WAY TO SWAP PIECES
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

    public boolean toggleGhostPiece() {
        return this.board.toggleGhostPiece();
    }

    public Tetromino getNextPiece() {
        return this.bag.peek();
    }

    public List<Tetromino> getPieces() {
        return pieces;
    }

    public TetrisBoard getTetrisBoard() {
        return this.board;
    }

    public long getInterval() {
        return this.updateInterval;
    }

    public void setInterval(long intervalInNanoseconds) {
        this.updateInterval = intervalInNanoseconds;
    }

    public Queue<Tetromino> getNextPieceQueue() {
        return this.bag;
    }

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

    private void placeAndSetTetromino() {
        board.placeCurrTetromino();
        clearRows();
        setPieces7bag();
    }

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
