package pieces;

import java.awt.Color;
import java.awt.geom.Point2D;

public class Block {
    private Point2D.Float position;
    private Color color;

    public Block(float x, float y, Color color) {
        this.color = color;
        position = new Point2D.Float(x, y);
    }

    public static Block copyOf(Block block) {
        return new Block(block.getX(), block.getY(), block.getColor());
    }

    public Block copy() {
        return copyOf(this);
    }

    public Point2D.Float getPosition() {
        return position;
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public Color getColor() {
        return color;
    }

    public void setX(float x) {
        position.x = x;
    }

    public void setY(float y) {
        position.y = y;
    }

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
