package pieces;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class TetrominoFactory {
    private int boardWidth, boardHeight;

    public TetrominoFactory(int boardWidth, int boardHeight){
        this.boardHeight = boardHeight;
        this.boardWidth = boardWidth;
    }

    public ArrayList<Tetromino> getPieces() {
        ArrayList<Tetromino> pieces = new ArrayList<>();
        pieces.add(this.createIPiece());
        pieces.add(this.createJPiece());
        pieces.add(this.createLPiece());
        pieces.add(this.createOPiece());
        pieces.add(this.createSPiece());
        pieces.add(this.createTPiece());
        pieces.add(this.createZPiece());

        return pieces;
    }

    public Tetromino createIPiece() {
        Block[] points = new Block[4];
        for (int i = 0; i < points.length; i++) {
            points[i] = new Block(i, 2, Color.CYAN);
        }

        float centerPos = (points[0].getX() + points[points.length - 1].getX()) / 2;
        Point2D.Float center = new Point2D.Float(centerPos, centerPos);
        Point2D.Float position = new Point2D.Float(boardWidth / 2 - 1, boardHeight - 3);

        return new Tetromino("I", points, position, center);
    }

    public Tetromino createJPiece() {
        Block[] points = new Block[4];

        points[0] = new Block(0, 2, Color.BLUE);
        for (int i = 1; i < points.length; i++) {
            points[i] = new Block(i - 1, 1, Color.BLUE);
        }

        Point2D.Float center = new Point2D.Float();
        center.setLocation(points[2].getX(), points[2].getY());

        Point2D.Float position = new Point2D.Float(boardWidth / 2 - 1, boardHeight - 2);

        return new Tetromino("J", points, position, center);
    }

    public Tetromino createLPiece() {
        Block[] points = new Block[4];

        for (int i = 0; i < points.length - 1; i++) {
            points[i] = new Block(i, 1, Color.ORANGE);
        }
        points[points.length - 1] = new Block(2, 2, Color.ORANGE);

        Point2D.Float center = new Point2D.Float();
        center.setLocation(points[1].getX(), points[1].getY());

        Point2D.Float position = new Point2D.Float(boardWidth / 2 - 1, boardHeight - 2);

        return new Tetromino("L", points, position, center);
    }

    public Tetromino createOPiece() {
        Block[] points = new Block[4];
        for (int i = 0; i < points.length; i++) {
            points[i] = new Block(i / 2, i % 2, Color.YELLOW);
        }

        float centerPos = (points[0].getX() + points[points.length - 1].getX()) / 2;
        Point2D.Float center = new Point2D.Float(centerPos, centerPos);

        Point2D.Float position = new Point2D.Float(boardWidth / 2 - 1, boardHeight - 1);
        
        return new Tetromino("O", points, position, center);
    }

    public Tetromino createSPiece() {
        Block[] points = new Block[4];

        for (int i = 0; i < points.length; i++) {
            points[i] = new Block((i / 2) + (i % 2), i / 2 + 1, Color.GREEN);
        }

        Point2D.Float center = new Point2D.Float(points[1].getX(), points[1].getY());
        Point2D.Float position = new Point2D.Float(boardWidth / 2 - 1, boardHeight - 2);

        return new Tetromino("S", points, position, center);
    }
    
    public Tetromino createTPiece() {
        Block[] points = new Block[4];
        Color tColor = new Color((153 << 16) | (0 << 8) | 255); 
        points[0] = new Block(1, 2, tColor);
        for (int i = 1; i < points.length; i++) {
            points[i] = new Block(i - 1, 1, tColor);
        }

        Point2D.Float center = new Point2D.Float(points[2].getX(), points[2].getY());
        Point2D.Float position = new Point2D.Float(boardWidth / 2 - 1, boardHeight - 2);

        return new Tetromino("T", points, position, center);
    }

    public Tetromino createZPiece() {
        Block[] points = new Block[4];

        for (int i = points.length - 1; i >= 0 ; i--) {
            points[points.length - i - 1] = new Block(((points.length - i - 1) / 2) + ((points.length - i - 1) % 2), i / 2 + 1, Color.RED);
        }

        Point2D.Float center = new Point2D.Float(points[2].getX(), points[2].getY());
        Point2D.Float position = new Point2D.Float(boardWidth / 2 - 1, boardHeight - 2);

        return new Tetromino("Z", points, position, center);
    }

    public Tetromino createCustomPiece(String name, Block[] points, Point2D.Float center, Color color) {
        // Point2D.Float center = new Point2D.Float();
        // Point2D.Float position = new Point2D.Float();
        return null;
    }
}
