package tetris;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import enums.Actions;
import pieces.Tetromino;
import pieces.TetrominoFactory;
// TODO - CONSIDER IMPLEMENTING A WAY TO SWAP PIECES
public class TetrisGame {
    private static final long LOCK_DELAY = (int) 5e8;

    private ArrayList<Tetromino> pieces;
    private Queue<Tetromino> pieces2;
    private Tetromino nextPiece;
    private TetrisBoard board;
    private Random rand;

    private long elapsedTimeAccumulator;
    private long lockDelayAccumulator;
    private long updateInterval;
    private HashSet<String> usedPieces;

    private boolean isOver;
    private boolean hasJustMoved;

    public TetrisGame(TetrisBoard board, long updateIntervalInNanoSeconds) {
        this.nextPiece = null;
        this.board = board;
        this.rand = new Random();
        this.usedPieces = new HashSet<>();

        TetrominoFactory fac = new TetrominoFactory(board.getWidth(), board.getHeight());
        this.pieces = fac.getPieces();
        this.pieces2 = new LinkedList<>();

        pieces2.addAll(pieces);

        this.setPieces();

        this.elapsedTimeAccumulator = this.lockDelayAccumulator = 0;
        this.updateInterval = updateIntervalInNanoSeconds;
        this.isOver = false;
        this.hasJustMoved = false;
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
        if (!board.setCurrentTetromino(nextPiece)) {
            this.reset(); // TODO - consider changing how the game behaves when the player loses
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
        if (usedPieces.size() == pieces.size()) {
            usedPieces.clear();
        }

        int randMaxVal = pieces.size();

        Tetromino tet = pieces.get(rand.nextInt(randMaxVal));
        while (!usedPieces.add(tet.getTetrominoName())) {
            tet = pieces.get(rand.nextInt(randMaxVal));
        }
        return tet;
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
        setPieces();
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
        this.setPieces();
        this.isOver = false;
    }
}
