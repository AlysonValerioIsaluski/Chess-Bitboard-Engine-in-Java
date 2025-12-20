package chess;

import java.awt.Image;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

public class GameWindow extends JFrame {
    private Board board;

    private final int frameWidth;
    private final int frameHeight;

    private final BufferedImage boardImg;
    private final Image wPawnImg, wKnightImg, wBishopImg, wRookImg, wQueenImg, wKingImg;
    private final Image bPawnImg, bKnightImg, bBishopImg, bRookImg, bQueenImg, bKingImg;
    private final Image greenCircleImg, orangeCircleImg, greenSquareImg;

    public GameWindow(Board board) {
        this.board = board;

        frameWidth = 1000;
        frameHeight = 1000;

        // Setting window
        this.setTitle("Alyson's Chess Board");
        this.setSize(frameWidth, frameHeight);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        boardImg = loadSprite("/res/board-8x8.png");

        // Scales the pieces so that they fit correctly in the tiles
        double scaleFactor = 0.8;
        wPawnImg = scaleSprite(loadSprite("/res/white-pawn.png"), scaleFactor);
        wKnightImg = scaleSprite(loadSprite("/res/white-knight.png"), scaleFactor);
        wBishopImg = scaleSprite(loadSprite("/res/white-bishop.png"), scaleFactor);
        wRookImg = scaleSprite(loadSprite("/res/white-rook.png"), scaleFactor);
        wQueenImg = scaleSprite(loadSprite("/res/white-queen.png"), scaleFactor);
        wKingImg = scaleSprite(loadSprite("/res/white-king.png"), scaleFactor);

        bPawnImg = scaleSprite(loadSprite("/res/black-pawn.png"), scaleFactor);
        bKnightImg = scaleSprite(loadSprite("/res/black-knight.png"), scaleFactor);
        bBishopImg = scaleSprite(loadSprite("/res/black-bishop.png"), scaleFactor);
        bRookImg = scaleSprite(loadSprite("/res/black-rook.png"), scaleFactor);
        bQueenImg = scaleSprite(loadSprite("/res/black-queen.png"), scaleFactor);
        bKingImg = scaleSprite(loadSprite("/res/black-king.png"), scaleFactor);
        
        scaleFactor = 0.3;
        greenCircleImg = scaleSprite(loadSprite("/res/green-circle.png"), scaleFactor);
        orangeCircleImg = scaleSprite(loadSprite("/res/orange-circle.png"), scaleFactor);
        scaleFactor = 0.075;
        greenSquareImg = scaleSprite(loadSprite("/res/green-square.png"), scaleFactor);

        BoardPanel boardPanel = new BoardPanel(
        this.board, this.boardImg,
        this.wPawnImg, this.wKnightImg, this.wBishopImg, 
        this.wRookImg, this.wQueenImg, this.wKingImg,
        this.bPawnImg, this.bKnightImg, this.bBishopImg, 
        this.bRookImg, this.bQueenImg, this.bKingImg,
        this.greenCircleImg, this.orangeCircleImg, this.greenSquareImg);

        this.add(boardPanel);

        this.setVisible(true);
    }

    // Scales an image smoothly
    private Image scaleSprite(BufferedImage image, double scaleFactor) {
        if (image == null) {
            return null;
        }
        
        int scaledWidth = (int) (image.getWidth() * scaleFactor);
        int scaledHeight = (int) (image.getHeight() * scaleFactor);
        
        return image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
    }

    // Loads an image by informing the path
    private BufferedImage loadSprite(String path) {
        try {
            InputStream is = getClass().getResourceAsStream(path);
            if (is == null) {
                System.err.println("Error: Could not find resource at " + path);
                return null;
            }
            
            return ImageIO.read(is);
        } catch (IOException e) {
            System.err.println("Error: Failed to load image at " + path);
            return null;
        }
    }
}