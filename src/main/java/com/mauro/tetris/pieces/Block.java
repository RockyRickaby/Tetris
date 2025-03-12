package com.mauro.tetris.pieces;

import java.awt.Color;
import java.awt.geom.Point2D;

/**
 * The Block class merely represents a colored point
 * within an imaginary plane. Here it's used as a Block,
 * but it's better to consider it
 * as a colored point instead of a Block.
 */
public class Block {
    private Point2D.Float position;
    private Color color;

    /**
     * Creates a new Block with position {@code (x, y)}
     * and color {@code color}.
     * @param x
     * @param y
     * @param color
     */
    public Block(float x, float y, Color color) {
        this.color = color;
        position = new Point2D.Float(x, y);
    }

    /**
     * Creates a copy of the Block {@code block}
     * and returns it.
     * @param block the Block to copy.
     * @return a copy of the Block.
     */
    public static Block copyOf(Block block) {
        return new Block(block.getX(), block.getY(), block.getColor());
    }

    /**
     * Creates and returns a copy of this Block.
     * @return a copy of this Block.
     */
    public Block copy() {
        return copyOf(this);
    }

    /**
     * Returns the position of this Block
     * as a Point2D.Float.
     * @return the position of this Block
     */
    public Point2D.Float getPosition() {
        return position;
    }

    /**
     * Returns the x component of this Block
     * @return x
     */
    public float getX() {
        return position.x;
    }

    /**
     * Returns the y component of this Block.
     * @return y
     */
    public float getY() {
        return position.y;
    }

    /**
     * Returns the color of this Block.
     * @return color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the x component of this Block.
     * @param x
     */
    public void setX(float x) {
        position.x = x;
    }

    /**
     * Sets the y component of this Block.
     * @param y
     */
    public void setY(float y) {
        position.y = y;
    }

    /**
     * Sets the color of this Block.
     * @param color
     */
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "{" + position.toString() + ", " + color.toString() + "}";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((position == null) ? 0 : position.hashCode());
        result = prime * result + ((color == null) ? 0 : color.hashCode());
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
        Block other = (Block) obj;
        if (position == null) {
            if (other.position != null)
                return false;
        } else if (!position.equals(other.position))
            return false;
        if (color == null) {
            if (other.color != null)
                return false;
        } else if (!color.equals(other.color))
            return false;
        return true;
    }
}
