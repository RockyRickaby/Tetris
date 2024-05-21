package pieces;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.Arrays;

import enums.Rotations;

/**
 * The Tetromino class represents the pieces that exist in a typical
 * Tetris game. By convention, the y-axis associated with every Tetromino
 * instance grows upwards instead of downwards. It is also assumed
 * that the body of the Tetromino has only a single color.
 * <p>
 * Despite the name, any piece of size n can by represented
 * by this class.
 */
public class Tetromino {
    private String name;
    private Block[] cells;
    private Block[] backup;
    
    private Point2D.Float initialPos;
    private Point2D.Float center;
    private Point2D.Float position;

    private int prevRotation, currRotation;

    /**
     * Creates a new Tetromino with name {@code name}, with a body
     * as defined by {@code cells}, with position {@code position} and
     * center {@code center}.
     * @param name the name of this piece.
     * @param cells the body of this piece.
     * @param position the position of this piece in an imaginary plane
     * @param center the center of this piece.
     */
    public Tetromino(String name, Block[] cells, Point2D.Float position, Point2D.Float center) {
        this.name = name;
        this.cells = cells;
        this.position = position;
        this.initialPos = new Point2D.Float();
        this.initialPos.setLocation(position);
        this.center = center;

        this.prevRotation = -1;
        this.currRotation = 0;

        this.backup = new Block[cells.length];
        for (int i = 0; i < cells.length; i++) {
            backup[i] = cells[i].copy();
        }
    }

    /**
     * Creates and returns a copy of {@code other}.
     * @param other the piece to be copied.
     * @return the copy of the piece.
     */
    public static Tetromino copyOf(Tetromino other) {
        return new Tetromino(other.name, other.getBody(), other.getPosition(), other.getCenter());
    }

    /**
     * Creates and returns a copy of this piece.
     * @return a copy of this piece.
     */
    public Tetromino copy() {
        return copyOf(this);
    }

    /**
     * Returns the color of the whole piece, assuming the 
     * cells that make it up all have the same color.
     * @return the color of this Tetromino.
     */
    public Color getColor() {
        return cells == null ? null : cells[0].getColor();
    }

    /**
     * Returns the size (number of cells) of this Tetromino.
     * @return the size.
     */
    public int size() {
        return cells.length;
    }

    /**
     * Returns the name of this Tetromino.
     * @return the name.
     */
    public String getTetrominoName() {
        return this.name;
    }

    /**
     * Sets the name of this Tetromino to {@code name}.
     * @param name
     */
    public void setTetrominoName(String name) {
        this.name = name;
    }

    /**
     * Sets the color of this Tetromino.
     * @param color
     */
    public void setColor(Color color) {
        for (Block b : cells) {
            b.setColor(color);
        }
    }

    /**
     * Returns the rightmost Block of this Tetromino given its
     * current state after possibly having been moved or rotated.
     * @return the rightmost Block.
     */
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

    /**
     * Returns the leftmost Block of this Tetromino given its
     * current state after possibly having been moved or rotated.
     * @return the leftmost Block.
     */
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

    /**
     * Returns the bottom Block of this Tetromino given its
     * current state after possibly having been moved or rotated.
     * @return the bottom Block.
     */
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

    /**
     * Returns a copy of the body of this Tetromino.
     * @return a copy of the body.
     */
    public Block[] getBody() {
        if (cells == null) {
            return null;
        }
        Block[] arr = new Block[cells.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = cells[i].copy();
        }
        return arr;
    }

    /**
     * Returns a copy of the center of this Tetromino.
     * @return the center.
     */
    public Point2D.Float getCenter() {
        if (center == null) {
            return null;
        }
        Point2D.Float centerCopy = new Point2D.Float();
        centerCopy.setLocation(center);
        return centerCopy;
    }

    /**
     * Returns a copy of the position of this Tetromino.
     * @return the position.
     */
    public Point2D.Float getPosition() {
        if (position == null) {
            return null;
        }
        Point2D.Float posCopy = new Point2D.Float();
        posCopy.setLocation(position);
        return posCopy;
    }

    /**
     * Sets the position of this Tetromino.
     * @param x component.
     * @param y component.
     */
    public void setPosition(float x, float y) {
        this.position.x = x;
        this.position.y = y;
    }

    /**
     * Sets the position of this Tetromino to be that of {@code pos}.
     * @param pos the new position.
     */
    public void setPosition(Point2D.Float pos) {
        this.position.setLocation(pos);
    }

    /**
     * Resets this Tetromino to its original position and
     * rotation.
     */
    public void resetPiece() {
        for (int i = 0; i < cells.length; i++) {
            while (!cells[i].equals(backup[i])) {
                this.rotateClockwise();
            }
        }
        this.position.setLocation(initialPos);
        this.prevRotation = -1;
        this.currRotation = 0;
    }

    /**
     * Moves this Tetromino up and returns it
     * @return {@code this} Tetromino.
     */
    public Tetromino moveUp() {
        position.y += 1;
        return this;
    }

    /**
     * Moves this Tetromino down and returns it
     * @return {@code this} Tetromino.
     */
    public Tetromino moveDown() {
        position.y -= 1;
        return this;
    }

    /**
     * Moves this Tetromino left and returns it
     * @return {@code this} Tetromino.
     */
    public Tetromino moveLeft() {
        position.x -= 1;
        return this;
    }

    /**
     * Moves this Tetromino right and returns it
     * @return {@code this} Tetromino.
     */
    public Tetromino moveRight() {
        position.x += 1;
        return this;
    }

    /**
     * Rotates this piece clockwise.
     * @return {@code this} piece.
     */
    public Tetromino rotateClockwise() {
        return rotate(Rotations.CLOCKWISE);
    }

    /**
     * Rotates this piece counterclockwise.
     * @return {@code this} piece.
     */
    public Tetromino rotateCounterclockwise() {
        return rotate(Rotations.COUNTERCLOCKWISE);
    }

    /**
     * Returns the current rotation of this Tetromino as an int.
     * The return values mean the following:
     * <ul>
     *     <li> 0 - initial rotation.
     *     <li> 1 - 1 clockwise rotation.
     *     <li> 2 - 2 rotations to either side.
     *     <li> 3 - 1 counterclockwise rotation.
     * </ul>
     * @return the current rotation.
     */
    public int getCurrentRotation() {
        return this.currRotation;
    }

    /**
     * Returns the previous rotation of this Tetromino as an int.
     * The return values mean the following:
     * <ul>
     *     <li> 0 - initial rotation.
     *     <li> 1 - 1 clockwise rotation.
     *     <li> 2 - 2 rotations to either side.
     *     <li> 3 - 1 counterclockwise rotation.
     * </ul>
     * @return the previous rotation.
     */
    public int getPreviousRotation() {
        return this.prevRotation;
    }

    /**
     * Rotates this Tetromino.
     * @param r
     * @return this Tetromino.
     */
    private Tetromino rotate(Rotations r) {
        float f1, f2;
        switch (r) {
            case CLOCKWISE:
                f1 = 1;
                f2 = -1;
                this.prevRotation = this.currRotation;
                this.currRotation = (this.currRotation + 4 + 1) % 4;
                break;
            case COUNTERCLOCKWISE:
                f1 = -1;
                f2 = 1;
                this.prevRotation = this.currRotation;
                this.currRotation = (this.currRotation + 4 - 1) % 4;
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
