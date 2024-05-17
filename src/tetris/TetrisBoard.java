package tetris;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import pieces.Block;
import pieces.Tetromino;

public class TetrisBoard {
    private Tetromino currentTetromino;
    private Tetromino ghostPiece;       // unused for now

    private Block[][] board;
    private int[] blocksPerRow;
    private int[] blocksPerColumn;
    private int height, width;

    public TetrisBoard() {
        this(10, 24);
    }

    public TetrisBoard(int width, int height) {
        this.width = width;
        this.height = height;

        board = new Block[height][width];
        blocksPerColumn = new int[width];
        blocksPerRow = new int[height];

        currentTetromino = null;
        ghostPiece = null;
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
        return true;
    }

    public boolean moveCurrTetrominoDown() {
        if (currentTetromino == null) {
            return false;
        }

        if (checkCollisions(this.currentTetromino, 0, -1, (x, y) -> y <= 1)) {
            return false;
        }

        currentTetromino.moveDown();
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
        return true;
    }

    public void hardDropCurrTetromino() {
        if (currentTetromino == null) {
            return;
        }
        System.out.println("Hard dropping tetromino");
        while (moveCurrTetrominoDown()) {
        }
        System.out.println("Done!");
    }

    public boolean rotateCurrTetrominoClockwise() {
        if (currentTetromino == null) {
            return false;
        }
        currentTetromino.rotateClockwise();

        if (this.checkCollisions(this.currentTetromino, 0, 0, (x, y) -> false)) {
            currentTetromino.rotateCounterclockwise();
            return false;
        }
        return true;
    }
    
    public boolean rotateCurrTetrominoCounterclockwise() {
        if (currentTetromino == null) {
            return false;
        }
        currentTetromino.rotateCounterclockwise();

        if (this.checkCollisions(this.currentTetromino, 0, 0, (x, y) -> false)) {
            currentTetromino.rotateClockwise();
            return false;
        }
        return true;
    }

    public void placeCurrTetromino() {
        if (currentTetromino == null) {
            return;
        }
        Block[] cells = currentTetromino.getBlocks();
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
    }

    private boolean checkCollisions(Tetromino piece, int xOffset, int yOffset, BiPredicate<Integer, Integer> extraTestForXAndY) {
        if (piece == null) {
            return false;
        }
        Block[] cells = piece.getBlocks();
        Point2D.Float pos = piece.getPosition();
        for (Block block : cells) {
            int blockY = (int) (block.getY() + pos.y);
            int blockX = (int) (block.getX() + pos.x);
            if (!validIndex(blockX, blockY) || extraTestForXAndY.test(blockX, blockY) || (board[blockY - 1 + yOffset][blockX - 1 + xOffset] != null)) {
                return true;
            }
        }
        return false;
    }

    private void pullAboveBlocksDownFrom(int row, int times) {
        if (!validIndex(1, row)) {
            return;
        }

        // should I try to optimize this or is this good enough?...
        // it might become a problem with bigger boards (n*m)
        for (int i = 0; i < times; i++) {
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
    }

    // caso (nao tao) excepcional = rows to clear (1,2,4)
    // isso faz com que a fileira 3 nao seja devidamente tratada
    public void clearRows(List<Integer> rows) {
        rows.sort((a, b) -> a - b);
        int lowestRow = Integer.MAX_VALUE;
        for (int row : rows) {
            if (!validIndex(1, row) || blocksPerRow[row - 1] < width) {
                continue;
            }

            lowestRow = Math.min(lowestRow, row);
            System.out.println("Clearing row " + row);
            blocksPerRow[row - 1] = 0;
            for (int col = 0; col < width; col++) {
                board[row - 1][col] = null;
                blocksPerColumn[col]--;
            }
        }
        pullAboveBlocksDownFrom(lowestRow, rows.size());
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
