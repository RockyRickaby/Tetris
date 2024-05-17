package tetris;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import enums.Actions;
import pieces.Tetromino;
import pieces.TetrominoFactory;

public class TetrisGame {
    private ArrayList<Tetromino> pieces;
    private Tetromino nextPiece;
    private TetrisBoard board;
    private Random rand;

    private long elapsedTimeAccumulator;
    private long updateInterval;
    private HashSet<Tetromino> usedPieces;

    private boolean isOver;

    public TetrisGame(TetrisBoard board, long updateIntervalInNanoSeconds) {
        this.nextPiece = null;
        this.board = board;
        this.rand = new Random();

        TetrominoFactory fac = new TetrominoFactory(board.getWidth(), board.getHeight());
        this.pieces = fac.getPieces();

        this.setPieces();

        this.elapsedTimeAccumulator = 0;
        this.updateInterval = updateIntervalInNanoSeconds;
        this.isOver = false;
    }

    public boolean toggleGhostPiece() {
        return this.board.toggleGhostPiece();
    }

    public Tetromino getNextPiece() {
        return this.nextPiece;
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

    private void setPieces() {
        nextPiece = nextPiece == null ? getRandomPiece() : nextPiece;
        // Tetromino aux = new TetrominoFactory(board.getWidth(), board.getHeight()).createLPiece();
        if (!board.setCurrentTetromino(nextPiece)) {
            this.reset();
            return;
        }

        Tetromino aux = getRandomPiece();
        String nextPieceName = nextPiece.getTetrominoName();
        while (aux.getTetrominoName().equals(nextPieceName)) {
            aux = getRandomPiece();
        }
        nextPiece = aux;
    }

    private Tetromino getRandomPiece() {
        return pieces.get(rand.nextInt(pieces.size()));
    }

    private boolean clearRows() {
        int boardRows = board.getHeight();
        int boardColumns = board.getWidth();
        ArrayList<Integer> rowsToClear = new ArrayList<>();
        for (int i = 1, piecesPerRow = 0; i <= boardRows && (piecesPerRow = board.getAmountOfBlocksInRow(i)) != 0; i++) {
            if (piecesPerRow == boardColumns) {
                rowsToClear.add(i);
            }
        }
        if (rowsToClear.isEmpty()) {
            return false;
        }
        board.clearRows(rowsToClear);
        return true;
    }

    public boolean moveCurrentTetromino(Actions movement) {
        switch (movement) {
            case HARD_DROP:
                board.hardDropCurrTetromino();
                board.placeCurrTetromino();
                clearRows();
                setPieces();
                this.elapsedTimeAccumulator = 0;
                return true;
            case MOVE_DOWN:
                if (!board.moveCurrTetrominoDown()) {
                    board.placeCurrTetromino();
                    clearRows();
                    setPieces();
                    return false;
                }
                this.elapsedTimeAccumulator = 0;
                return true;
            case MOVE_LEFT:
                return board.moveCurrTetrominoLeft();
            case MOVE_RIGHT:
                return board.moveCurrTetrominoRight();
            case ROTATE_CLOCKWISE:
                return board.rotateCurrTetrominoClockwise();
            case ROTATE_COUNTERCLOCKWISE:
                return board.rotateCurrTetrominoCounterclockwise();
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
    }

    public void reset() {
        this.board.clearAll();
        this.setPieces();
        this.isOver = false;
    }
}
