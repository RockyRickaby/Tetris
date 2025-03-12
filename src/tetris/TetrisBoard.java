package tetris;

import java.awt.geom.Point2D;

import java.util.function.BiPredicate;

import pieces.Block;
import pieces.Tetromino;

/**
 * The only thing this class manages automatically is the position of the ghost piece.
 * The rest is intended to be managed by other classes/objects.
 */
public class TetrisBoard {
    private static final int WALLKICK[][][] =
    {
        {{-1, 0}, {-1, 1}, {0, -2}, {-1, -2}},	// 0>>1
        {{1, 0}, {1, -1}, {0, 2}, {1, 2}},	// 1>>2
        {{1, 0}, {1, 1}, {0, -2}, {1, -2}},	// 2>>3
        {{-1, 0}, {-1, -1}, {0, 2}, {-1, 2}},	// 3>>0
    };

	private static final int WALLKICK_I[][][] =
    {
        {{-2, 0}, {1, 0}, {-2, -1}, {1, 2}},
        {{-1, 0}, {2, 0}, {-1, 2}, {2, -1}},
        {{2, 0}, {-1, 0}, {2, 1}, {-1, -2}},
        {{1, 0}, {-2, 0}, {1, -2}, {-2, 1}},
    };

    private Tetromino currentTetromino;
    private Tetromino ghostPiece;

    private boolean toggleGhostPiece;

    private Block[][] board;
    private int[] blocksPerRow;
    private int[] blocksPerColumn;
    private int height, width;

    public TetrisBoard() {
        this(10, 24, true);
    }

    public TetrisBoard(int width, int height) {
        this(width, height, true);
    }

    public TetrisBoard(int width, int height, boolean enableGhostPiece) {
        this.width = width;
        this.height = height;

        board = new Block[height][width];
        blocksPerColumn = new int[width];
        blocksPerRow = new int[height];

        currentTetromino = null;
        ghostPiece = null;

        this.toggleGhostPiece = enableGhostPiece;
    }

    public boolean toggleGhostPiece() {
        this.toggleGhostPiece = !this.toggleGhostPiece;
        if (toggleGhostPiece) {
            setGhostPiece();
            updateGhostPiece();
        } else {
            this.ghostPiece = null;
        }
        return this.toggleGhostPiece;
    }

    public boolean isDownwardsMovementObstructed() {
        return checkCollisions(this.currentTetromino, 0, -1, (x, y) -> y <= 1);
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    public int getAmountOfBlocksInRow(int row) {
        if (row < 1 || row > height) {
            return -1;
        }
        return blocksPerRow[row - 1];
    }

    public int getAmountOfBlocksInColumn(int column) {
        if (column < 1 || column > width) {
            return -1;
        }
        return blocksPerColumn[column - 1];
    }

    private boolean validIndex(int x, int y) {
        return x >= 1 && x <= width && y >= 1 && y <= height;
    }

    public Block getBlockAtCell(int x, int y) {
        if (!validIndex(x, y)) {
            return null;
        }
        return this.board[y - 1][x - 1];
    }

    public Tetromino getCurrentTetromino() {
        return this.currentTetromino;
    }

    public boolean setCurrentTetromino(Tetromino next) {
        if (this.checkCollisions(next, 0, 0, (x, y) -> false)) {
            return false;
        }
        this.currentTetromino = next;
        this.setGhostPiece();
        this.updateGhostPiece();
        return true;
    }

    public Tetromino getGhostPiece() {
        return this.ghostPiece;
    }

    private void setGhostPiece() {
        if (!toggleGhostPiece) {
            return;
        }
        this.ghostPiece = currentTetromino.copy();
    }

    private void updateGhostPiece() {
        if (!toggleGhostPiece) {
            return;
        }

        this.ghostPiece.setPosition(this.currentTetromino.getPosition());
        while (!checkCollisions(this.ghostPiece, 0, -1, (x, y) -> y <= 1)) {
            this.ghostPiece.moveDown();
        }
    }

    public boolean moveCurrTetrominoDown() {
        if (currentTetromino == null) {
            return false;
        }

        if (checkCollisions(this.currentTetromino, 0, -1, (x, y) -> y <= 1)) {
            return false;
        }

        currentTetromino.moveDown();
        this.updateGhostPiece();
        return true;
    }
    
    public boolean moveCurrTetrominoLeft() {
        if (currentTetromino == null) {
            return false;
        }

        if (checkCollisions(this.currentTetromino, -1, 0, (x, y) -> x <= 1)) {
            return false;
        }

        currentTetromino.moveLeft();
        if (toggleGhostPiece) {
            ghostPiece.moveLeft();
        }
        this.updateGhostPiece();
        return true;
    }
    
    public boolean moveCurrTetrominoRight() {
        if (currentTetromino == null) {
            return false;
        }

        if (checkCollisions(this.currentTetromino, 1, 0, (x, y) -> x >= width)) {
            return false;
        }

        currentTetromino.moveRight();
        if (toggleGhostPiece) {
            ghostPiece.moveRight();
        }
        this.updateGhostPiece();
        return true;
    }

    public void hardDropCurrTetromino() {
        if (currentTetromino == null) {
            return;
        }
        while (moveCurrTetrominoDown()) {
            // :D :3 uwu owo :| :/ :> qwq
        }
    }

    public boolean rotateCurrTetrominoClockwise() {
        if (currentTetromino == null) {
            return false;
        }
        currentTetromino.rotateClockwise();

        if (this.checkCollisions(this.currentTetromino, 0, 0, (x, y) -> false)) {
            if (!wallKick()) {
                currentTetromino.rotateCounterclockwise();
                return false;
            }
        }
        if (toggleGhostPiece) {
            ghostPiece.rotateClockwise();
        }
        this.updateGhostPiece();
        return true;
    }
    
    public boolean rotateCurrTetrominoCounterclockwise() {
        if (currentTetromino == null) {
            return false;
        }
        currentTetromino.rotateCounterclockwise();

        if (this.checkCollisions(this.currentTetromino, 0, 0, (x, y) -> false)) {
            if (!wallKick()) {
                currentTetromino.rotateClockwise();
                return false;
            }
        }
        if (toggleGhostPiece) {
            ghostPiece.rotateCounterclockwise();
        }
        this.updateGhostPiece();
        return true;
    }

    private boolean wallKick() {
        int prevRot = this.currentTetromino.getPreviousRotation();
        int currRot = this.currentTetromino.getCurrentRotation();
        if (prevRot <= -1) {
            return false;
        }

        int wise = 0;
        if (currRot == 3) {
            if (prevRot == 0) {
                wise = -1;
            } else {
                wise = 1;
            }
        } else if (currRot == 0) {
            if (prevRot == 3) {
                wise = 1;
            } else {
                wise = -1;
            }
        } else {
            wise = currRot - prevRot;
        }

        int[][][] wallkickData = WALLKICK;
        if (this.currentTetromino.getTetrominoName().equalsIgnoreCase("I")) {
            wallkickData = WALLKICK_I;
        }

        for (int i = 0; i < 4; i++) {
            int x = wallkickData[prevRot][i][0] * wise;
            int y = wallkickData[prevRot][i][1] * wise;
            if (!this.checkCollisions(this.currentTetromino, x, y, (x1, y1) -> !validIndex(x1, y1))) {
                Point2D.Float pos = this.currentTetromino.getPosition();
                this.currentTetromino.setPosition(pos.x + x, pos.y + y);
                return true;
            }
        }
        return false;
    }

    public void placeCurrTetromino() {
        if (currentTetromino == null) {
            return;
        }
        Block[] cells = currentTetromino.getBody();
        Point2D.Float pos = currentTetromino.getPosition();

        for (Block b : cells) {
            int x = (int) (b.getX() + pos.x) - 1,
                y = (int) (b.getY() + pos.y) - 1;
            
            b.setX(x);
            b.setY(y);
            board[y][x] = b;
            blocksPerColumn[x]++;
            blocksPerRow[y]++;
        }
        currentTetromino.resetPiece();
        currentTetromino = null;
        ghostPiece = null;
    }

    private boolean checkCollisions(Tetromino piece, int xOffset, int yOffset, BiPredicate<Integer, Integer> extraTestForXAndY) {
        if (piece == null) {
            return false;
        }
        Block[] cells = piece.getBody();
        Point2D.Float pos = piece.getPosition();
        for (Block block : cells) {
            int blockY = (int) (block.getY() + pos.y);
            int blockX = (int) (block.getX() + pos.x);
            if (!validIndex(blockX + xOffset, blockY + yOffset) || extraTestForXAndY.test(blockX, blockY) || (board[blockY - 1 + yOffset][blockX - 1 + xOffset] != null)) {
                return true;
            }
        }
        return false;
    }

    public void pullAboveBlocksDownFrom(int row) {
        if (!validIndex(1, row)) {
            return;
        }

        for (int rows = row - 1; rows < height - 1; rows++) {
            blocksPerRow[rows] = blocksPerRow[rows + 1];
            for (int col = 0; col < width; col++) {
                board[rows][col] = board[rows + 1][col];
            }
        }
        blocksPerRow[height - 1] = 0;

        for (int col = 0; col < width; col++) {
            board[height - 1][col] = null;
        }
    }

    public void clearRow(int row) {
        if (!validIndex(1, row) || blocksPerRow[row - 1] < width) {
            return;
        }

        blocksPerRow[row - 1] = 0;
        for (int col = 0; col < width; col++) {
            board[row - 1][col] = null;
            blocksPerColumn[col]--;
        }
    }

    public void clearAll() {
        for (int i = 0; i < width; i++) {
            blocksPerColumn[i] = 0;
            for (int j = 0; j < height; j++) {
                board[j][i] = null;
            }
        }
        for (int j = 0; j < height; j++) {
            blocksPerRow[j] = 0;
        }

        this.currentTetromino = null;
        this.ghostPiece = null;
    }
}
