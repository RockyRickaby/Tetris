package pieces;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.Arrays;

import enums.Rotations;

public class Tetromino {
    private String name;
    private Block[] cells;
    private Block[] backup;
    
    private Point2D.Float initialPos;
    private Point2D.Float center;
    private Point2D.Float position;

    public Tetromino(String name, Block[] cells, Point2D.Float position, Point2D.Float center) {
        this.name = name;
        this.cells = cells;
        this.position = position;
        this.initialPos = new Point2D.Float();
        this.initialPos.setLocation(position);
        this.center = center;

        this.backup = new Block[cells.length];
        for (int i = 0; i < cells.length; i++) {
            backup[i] = cells[i].copy();
        }
    }

    public static Tetromino copyOf(Tetromino other) {
        return new Tetromino(other.name, other.getBlocks(), other.getPosition(), other.getCenter());
    }

    public Tetromino copy() {
        return copyOf(this);
    }

    public Color getColor() {
        return cells == null ? null : cells[0].getColor();
    }

    public int size() {
        return cells.length;
    }

    public String getTetrominoName() {
        return this.name;
    }

    public void setTetrominoName(String name) {
        this.name = name;
    }

    public void setColor(Color color) {
        for (Block b : cells) {
            b.setColor(color);
        }
    }

    public Block getRightmostBlock() {
        if (cells == null) {
            return null;
        }
        Block block = cells[0];
        for (Block b : cells) {
            if (b.getX() > block.getX()) {
                block = b;
            }
        }
        return block.copy();
    }

    public Block getLeftmostBlock() {
        if (cells == null) {
            return null;
        }
        Block block = cells[0];
        for (Block b : cells) {
            if (b.getX() < block.getX()) {
                block = b;
            }
        }
        return block.copy();
    }

    public Block getBottomBlock() {
        if (cells == null) {
            return null;
        }
        Block block = cells[0];
        for (Block b : cells) {
            if (b.getY() < block.getY()) {
                block = b;
            }
        }
        return block.copy();
    }

    public Block[] getBlocks() {
        if (cells == null) {
            return null;
        }
        Block[] arr = new Block[cells.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = cells[i].copy();
        }
        return arr;
    }

    public Point2D.Float getCenter() {
        if (center == null) {
            return null;
        }
        Point2D.Float centerCopy = new Point2D.Float();
        centerCopy.setLocation(center);
        return centerCopy;
    }

    public Point2D.Float getPosition() {
        if (position == null) {
            return null;
        }
        Point2D.Float posCopy = new Point2D.Float();
        posCopy.setLocation(position);
        return posCopy;
    }

    public void setPosition(int x, int y) {
        this.position.x = x;
        this.position.y = y;
    }

    public void setPosition(Point2D.Float pos) {
        this.position.setLocation(pos);
    }

    public void resetPiece() {
        for (int i = 0; i < cells.length; i++) {
            while (!cells[i].equals(backup[i])) {
                this.rotateClockwise();
            }
        }
        this.position.setLocation(initialPos);
    }

    public Tetromino moveUp() {
        position.y += 1;
        return this;
    }

    public Tetromino moveDown() {
        position.y -= 1;
        return this;
    }

    public Tetromino moveLeft() {
        position.x -= 1;
        return this;
    }

    public Tetromino moveRight() {
        position.x += 1;
        return this;
    }

    public Tetromino rotateClockwise() {
        return rotate(Rotations.CLOCKWISE);
    }

    public Tetromino rotateCounterclockwise() {
        return rotate(Rotations.COUNTERCLOCKWISE);
    }

    private Tetromino rotate(Rotations r) {
        float f1, f2;
        switch (r) {
            case CLOCKWISE:
                f1 = 1;
                f2 = -1;
                break;
            case COUNTERCLOCKWISE:
                f1 = -1;
                f2 = 1;
                break;
            default:
                return this;            
        }

        for (var block : cells) {
            float x = block.getX();
            float y = block.getY();

            x -= center.x;
            y -= center.y;
            
            float aux = x;
            x = y   * f1;
            y = aux * f2;

            block.setX(x + center.x);
            block.setY(y + center.y);
        }

        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(cells);
        result = prime * result + ((center == null) ? 0 : center.hashCode());
        result = prime * result + ((position == null) ? 0 : position.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Tetromino other = (Tetromino) obj;
        if (!Arrays.equals(cells, other.cells))
            return false;
        if (center == null) {
            if (other.center != null)
                return false;
        } else if (!center.equals(other.center))
            return false;
        if (position == null) {
            if (other.position != null)
                return false;
        } else if (!position.equals(other.position))
            return false;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("Piece name: " + name + "\nPoints: [");
        for (int i = 0; i < cells.length - 1; i++) {
            str.append("(" + cells[i].getX() + ", " + cells[i].getY() + "), ");
        }
        str.append("(" + cells[cells.length - 1].getX() + ", " + cells[cells.length - 1].getY() + ")]\n");

        str.append("Position: ")
           .append("(" + position.x + ", " + position.y + ")\n")

           .append("Center: ")
           .append("(" + center.x + ", " + center.y + ")\n");
        return str.toString();
    }
}
