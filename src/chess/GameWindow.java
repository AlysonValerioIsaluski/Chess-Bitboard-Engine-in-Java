package chess;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

public class GameWindow extends JFrame implements PromotionListener {
    // Declarations used to contain the board
    private final JPanel container;
    private final BoardPanel boardPanel;
    private JPanel promotionPanel;
    
    private final Board board;
    private final GameLogic gameLogic;
    
    private final BufferedImage boardImg;
    private final Image wPawnImg, wKnightImg, wBishopImg, wRookImg, wQueenImg, wKingImg;
    private final Image bPawnImg, bKnightImg, bBishopImg, bRookImg, bQueenImg, bKingImg;
    private final Image greenCircleImg, orangeCircleImg, greenSquareImg, redSquareImg;
    
    public GameWindow(Board board, GameLogic gameLogic) {
        this.board = board;
        this.gameLogic = gameLogic;
        
        // Setting window
        this.setTitle("Alyson's Chess Board");
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
        scaleFactor = 0.032;
        redSquareImg = scaleSprite(loadSprite("/res/red-square.png"), scaleFactor);

        this.boardPanel = new BoardPanel(
            this.board, this.gameLogic, this.boardImg,
            this.wPawnImg, this.wKnightImg, this.wBishopImg, 
            this.wRookImg, this.wQueenImg, this.wKingImg,
            this.bPawnImg, this.bKnightImg, this.bBishopImg, 
            this.bRookImg, this.bQueenImg, this.bKingImg,
            this.greenCircleImg, this.orangeCircleImg,
            this.greenSquareImg, this.redSquareImg, this);

        this.container = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Creating the Layered Pane
        JLayeredPane layeredPane = new JLayeredPane();
        int boardDim = 9*96; // Adjust this to match your actual board pixel size
        layeredPane.setPreferredSize(new Dimension(boardDim, boardDim));

        // Adding the BoardPanel to the bottom layer (Layer 0)
        boardPanel.setBounds(0, 0, boardDim, boardDim);
        layeredPane.add(boardPanel, Integer.valueOf(0));

        // Creating the Promotion Panel (Layer 1)
        promotionPanel = new JPanel(new GridLayout(1, 4));
        promotionPanel.setBackground(new Color(255, 255, 255, 220)); // Semi-transparent white
        promotionPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        // Sizing it
        int tileSize = boardDim / 8;
        promotionPanel.setBounds((boardDim / 2) - (tileSize * 2), 0, tileSize * 4, tileSize);
        promotionPanel.setVisible(false); // Hidden until a pawn hits the last rank

        layeredPane.add(promotionPanel, Integer.valueOf(1));

        // Adding the LayeredPane to the central container
        gbc.gridx = 1; 
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        container.add(layeredPane, gbc);

        this.add(container);
        this.pack();
        this.setLocationRelativeTo(null);
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

    @Override
    public void onPromotionRequest(char color) {
        showPromotionUI(color);
    }

    private void showPromotionUI(char color) {
        // Clearing and re-adding labels with the correct color sprites
        promotionPanel.removeAll();
        Image[] images = (color == 'w') ? 
            new Image[]{wQueenImg, wRookImg, wBishopImg, wKnightImg} : 
            new Image[]{bQueenImg, bRookImg, bBishopImg, bKnightImg};
        char[] types = {'q', 'r', 'b', 'n'};

        for (int i = 0; i < 4; i++) {
            final char pieceType = types[i];
            JLabel label = new JLabel(new ImageIcon(images[i]));
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    gameLogic.handlePromotionSelection(pieceType);
                    promotionPanel.setVisible(false);
                    boardPanel.repaint();
                }
            });
            promotionPanel.add(label);
        }
        promotionPanel.revalidate();
        promotionPanel.setVisible(true);
    }
}