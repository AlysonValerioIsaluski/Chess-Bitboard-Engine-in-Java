package chess;

import java.awt.*;
import java.awt.image.*;
import javax.swing.JPanel;

public class BoardPanel extends JPanel {
    // Prevention of magic number
    private final int boardSize = 8;

    // Bitmap for tiles in board
    private Board board;
    
    private int[][][] tileboard = new int[boardSize][boardSize][2];

    private BufferedImage boardImg;
    private Image wPawnImg, wKnightImg, wBishopImg, wRookImg, wQueenImg, wKingImg;
    private Image bPawnImg, bKnightImg, bBishopImg, bRookImg, bQueenImg, bKingImg;

    public BoardPanel(Board board, BufferedImage boardImg,
        Image wPawnImg, Image wKnightImg, Image wBishopImg,
        Image wRookImg, Image wQueenImg, Image wKingImg,
        Image bPawnImg, Image bKnightImg, Image bBishopImg,
       Image bRookImg, Image bQueenImg, Image bKingImg) {
        
        this.board = board;
        this.boardImg = boardImg;

        this.wPawnImg = wPawnImg;
        this.wKnightImg = wKnightImg;
        this.wBishopImg = wBishopImg;
        this.wRookImg = wRookImg;
        this.wQueenImg = wQueenImg;
        this.wKingImg = wKingImg;

        this.bPawnImg = bPawnImg;
        this.bKnightImg = bKnightImg;
        this.bBishopImg = bBishopImg;
        this.bRookImg = bRookImg;
        this.bQueenImg = bQueenImg;
        this.bKingImg = bKingImg;
        
        // Alignment grid for displaying the pieces on the board
        for(int row = 0; row < boardSize; row++) {
            for(int column = 0; column < boardSize; column++) {
                tileboard[row][column][0] = column*96 + 29; // x coord of tile
                tileboard[row][column][1] = row*96 + 28; // y coord of tile
            }
        }
    }

    // Displays the board and pieces, among other ui elements
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Drawing board
        if (this.boardImg != null) {
            g.drawImage(this.boardImg, 0, 0, this);
        }

        long[][] bitboard = board.getBitboard();

        // Drawing pieces
        // White pawn
        if (this.wPawnImg != null) {
            long whitePawns = board.getWhitePawns();

            for(int row = 0; row < boardSize; row++) {
                for(int column = 0; column < boardSize; column++) {
                    if((whitePawns & bitboard[row][column]) != 0)
                        g.drawImage(this.wPawnImg, tileboard[row][column][0], tileboard[row][column][1], this);
                }
            }            
        }

        // White knight
        if (this.wKnightImg != null) {
            long whiteKnights = board.getWhiteKnights();

            for(int row = 0; row < boardSize; row++) {
                for(int column = 0; column < boardSize; column++) {
                    if((whiteKnights & bitboard[row][column]) != 0)
                        g.drawImage(this.wKnightImg, tileboard[row][column][0], tileboard[row][column][1], this);
                }
            }            
        }

        // White bishop
        if (this.wBishopImg != null) {
            long whiteBishops = board.getWhiteBishops();
            for(int row = 0; row < boardSize; row++) {
                for(int column = 0; column < boardSize; column++) {
                    if((whiteBishops & bitboard[row][column]) != 0)
                        g.drawImage(this.wBishopImg, tileboard[row][column][0], tileboard[row][column][1], this);
                }
            }            
        }

        // White rook
        if (this.wRookImg != null) {
            long whiteRooks = board.getWhiteRooks();
            for(int row = 0; row < boardSize; row++) {
                for(int column = 0; column < boardSize; column++) {
                    if((whiteRooks & bitboard[row][column]) != 0)
                        g.drawImage(this.wRookImg, tileboard[row][column][0], tileboard[row][column][1], this);
                }
            }            
        }

        // White queen
        if (this.wQueenImg != null) {
            long whiteQueens = board.getWhiteQueens();
            for(int row = 0; row < boardSize; row++) {
                for(int column = 0; column < boardSize; column++) {
                    if((whiteQueens & bitboard[row][column]) != 0)
                        g.drawImage(this.wQueenImg, tileboard[row][column][0], tileboard[row][column][1], this);
                }
            }            
        }

        // White king
        if (this.wKingImg != null) {
            long whiteKing = board.getWhiteKing();
            for(int row = 0; row < boardSize; row++) {
                for(int column = 0; column < boardSize; column++) {
                    if((whiteKing & bitboard[row][column]) != 0) {
                        g.drawImage(this.wKingImg, tileboard[row][column][0], tileboard[row][column][1], this);
                        break;
                    }
                }
            }            
        }

        // Black pawn
        if (this.bPawnImg != null) {
            long blackPawns = board.getBlackPawns();
            for(int row = 0; row < boardSize; row++) {
                for(int column = 0; column < boardSize; column++) {
                    if((blackPawns & bitboard[row][column]) != 0)
                        g.drawImage(this.bPawnImg, tileboard[row][column][0], tileboard[row][column][1], this);
                }
            }            
        }

        // Black knight
        if (this.bKnightImg != null) {
            long blackKnights = board.getBlackKnights();
            for(int row = 0; row < boardSize; row++) {
                for(int column = 0; column < boardSize; column++) {
                    if((blackKnights & bitboard[row][column]) != 0)
                        g.drawImage(this.bKnightImg, tileboard[row][column][0], tileboard[row][column][1], this);
                }
            }            
        }

        // Black bishop
        if (this.bBishopImg != null) {
            long blackBishops = board.getBlackBishops();
            for(int row = 0; row < boardSize; row++) {
                for(int column = 0; column < boardSize; column++) {
                    if((blackBishops & bitboard[row][column]) != 0)
                        g.drawImage(this.bBishopImg, tileboard[row][column][0], tileboard[row][column][1], this);
                }
            }            
        }

        // Black rook
        if (this.bRookImg != null) {
            long blackRooks = board.getBlackRooks();
            for(int row = 0; row < boardSize; row++) {
                for(int column = 0; column < boardSize; column++) {
                    if((blackRooks & bitboard[row][column]) != 0)
                        g.drawImage(this.bRookImg, tileboard[row][column][0], tileboard[row][column][1], this);
                }
            }            
        }

        // Black queen
        if (this.bQueenImg != null) {
            long blackQueens = board.getBlackQueens();
            for(int row = 0; row < boardSize; row++) {
                for(int column = 0; column < boardSize; column++) {
                    if((blackQueens & bitboard[row][column]) != 0)
                        g.drawImage(this.bQueenImg, tileboard[row][column][0], tileboard[row][column][1], this);
                }
            }            
        }

        // Black king
        if (this.bKingImg != null) {
            long blackKing = board.getBlackKing();
            for(int row = 0; row < boardSize; row++) {
                for(int column = 0; column < boardSize; column++) {
                    if((blackKing & bitboard[row][column]) != 0) {
                        g.drawImage(this.bKingImg, tileboard[row][column][0], tileboard[row][column][1], this);
                        break;
                    }  
                }
            }            
        }
    }
}
