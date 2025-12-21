package chess;

import java.awt.*;
import java.awt.image.*;
import javax.swing.JPanel;

public class BoardPanel extends JPanel {
    // Bitmap for tiles in board
    private final Board board;
    private final int boardSize;
    private final long[][] bitboard;

    // Saves coordinates in pixels of the tiles in the board
    private final int[][][] tileboard;
    private final int TILE_OFFSET = 64;
    private final int TILE_SIZE = 96;

    private char selectedPiece;
    private long selectedPiecePossibleMoves, enemyPieces;
    private int selectedPieceRow, selectedPieceColumn;

    private boolean inCheck;
    private boolean inCheckmate;
    private boolean inStalemate;

    private final BufferedImage boardImg;
    private final Image wPawnImg, wKnightImg, wBishopImg, wRookImg, wQueenImg, wKingImg;
    private final Image bPawnImg, bKnightImg, bBishopImg, bRookImg, bQueenImg, bKingImg;
    private final Image greenCircleImg, orangeCircleImg, greenSquareImg, redSquareImg;

    public BoardPanel(Board board, GameLogic gameLogic, BufferedImage boardImg,
        Image wPawnImg, Image wKnightImg, Image wBishopImg,
        Image wRookImg, Image wQueenImg, Image wKingImg,
        Image bPawnImg, Image bKnightImg, Image bBishopImg,
        Image bRookImg, Image bQueenImg, Image bKingImg,
        Image greenCircleImg, Image orangeCircleImg,
        Image greenSquareImg, Image redSquareImg) {
       
        this.board = board;
        this.boardSize = board.getBoardSize();
        this.bitboard = board.getBitboard();

        tileboard = new int[boardSize][boardSize][2];

        this.selectedPiece = '0';
        this.inCheck = false;
        this.inCheckmate = false;
        this.inStalemate = false;
        
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

        this.greenCircleImg = greenCircleImg;
        this.orangeCircleImg = orangeCircleImg;
        this.greenSquareImg = greenSquareImg;
        this.redSquareImg = redSquareImg;
        
        // Alignment grid for displaying the pieces on the board
        for(int row = 0; row < boardSize; row++) {
            for(int column = 0; column < boardSize; column++) {
                tileboard[row][column][0] = column*96 + 29; // x coord of tile
                tileboard[row][column][1] = row*96 + 29; // y coord of tile
            }
        }

        // Checks mouse clickes on the board
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // Gets clicked tile
                int column = (e.getX() + TILE_OFFSET) / TILE_SIZE;
                int row = (e.getY() + TILE_OFFSET) / TILE_SIZE;
                
                // Ckecks where the player clicked in the window
                gameLogic.handleTileSelection(row, column);

                selectedPiece = gameLogic.getSelectedPiece();
                selectedPiecePossibleMoves = gameLogic.getSelectedPiecePossibleMoves();
                enemyPieces = gameLogic.getEnemyPieces();
                selectedPieceRow = gameLogic.getSelectedPieceRow();
                selectedPieceColumn = gameLogic.getSelectedPieceColumn();
                inCheck = gameLogic.getCheckStatus();
                inCheckmate = gameLogic.getCheckmateStatus();
                inStalemate = gameLogic.getStalemateStatus();

                repaint();
            }
        });
    }

    // Displays the board and pieces, among other ui elements
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Drawing board
        if (this.boardImg != null) {
            g.drawImage(this.boardImg, 0, 0, this);
        }

        // Erasing pieces if there's an stalemate
        if(this.inStalemate) {
            System.out.println("STALEMATE!");
            return;
        }
        
        // Drawing check marker if king is in check
        if(this.inCheck) {
            int[] kingCoordinates = GameLogic.getKingCoordinates(board, board.getTurn());
            
            g.drawImage(this.redSquareImg, tileboard[kingCoordinates[0]][kingCoordinates[1]][0]+3, tileboard[kingCoordinates[0]][kingCoordinates[1]][1]+3, this);
            
            
            // Drawing markers under all checkmated players pieces if king is in checkmate
            if(this.inCheckmate) {
                System.out.println("CHECKMATE!");
                
                if(board.getTurn() == 'w') {
                    for(int row = 0; row < boardSize; row++) {
                        for(int column = 0; column < boardSize; column++) {
                            if((board.getWhitePawns() & bitboard[row][column]) != 0)
                                g.drawImage(this.redSquareImg, tileboard[row][column][0]+3, tileboard[row][column][1]+3, this);
                            else if((board.getWhiteKnights() & bitboard[row][column]) != 0)
                                g.drawImage(this.redSquareImg, tileboard[row][column][0]+3, tileboard[row][column][1]+3, this);
                            else if((board.getWhiteBishops() & bitboard[row][column]) != 0)
                                g.drawImage(this.redSquareImg, tileboard[row][column][0]+3, tileboard[row][column][1]+3, this);
                            else if((board.getWhiteRooks() & bitboard[row][column]) != 0)
                                g.drawImage(this.redSquareImg, tileboard[row][column][0]+3, tileboard[row][column][1]+3, this);
                            else if((board.getWhiteQueens() & bitboard[row][column]) != 0)
                                g.drawImage(this.redSquareImg, tileboard[row][column][0]+3, tileboard[row][column][1]+3, this);
                        }
                    }
                }
                else {
                    for(int row = 0; row < boardSize; row++) {
                        for(int column = 0; column < boardSize; column++) {
                            if((board.getBlackPawns() & bitboard[row][column]) != 0)
                                g.drawImage(this.redSquareImg, tileboard[row][column][0]+3, tileboard[row][column][1]+3, this);
                            else if((board.getBlackKnights() & bitboard[row][column]) != 0)
                                g.drawImage(this.redSquareImg, tileboard[row][column][0]+3, tileboard[row][column][1]+3, this);
                            else if((board.getBlackBishops() & bitboard[row][column]) != 0)
                                g.drawImage(this.redSquareImg, tileboard[row][column][0]+3, tileboard[row][column][1]+3, this);
                            else if((board.getBlackRooks() & bitboard[row][column]) != 0)
                                g.drawImage(this.redSquareImg, tileboard[row][column][0]+3, tileboard[row][column][1]+3, this);
                            else if((board.getBlackQueens() & bitboard[row][column]) != 0)
                                g.drawImage(this.redSquareImg, tileboard[row][column][0]+3, tileboard[row][column][1]+3, this);
                        }
                    }
                }
            }
        }

        // Drawing selected piece marker
        if(this.selectedPiece != '0') {
            g.drawImage(this.greenSquareImg, tileboard[selectedPieceRow][selectedPieceColumn][0]+3, tileboard[selectedPieceRow][selectedPieceColumn][1]+3, this);
            
            // Drawing markers where the selected piece can move
            for(int row = 0; row < boardSize; row++) {
                for(int column = 0; column < boardSize; column++) {
                    if((this.selectedPiecePossibleMoves & bitboard[row][column]) != 0) {
                        if((this.enemyPieces & bitboard[row][column]) != 0) // capturable
                        g.drawImage(this.orangeCircleImg, tileboard[row][column][0], tileboard[row][column][1], this);
                        else // not capturable
                        g.drawImage(this.greenCircleImg, tileboard[row][column][0], tileboard[row][column][1], this);
                    }
                }
            }
        }
        
        // Drawing pieces
        for(int row = 0; row < boardSize; row++) {
            for(int column = 0; column < boardSize; column++) {
                if((board.getWhitePawns() & bitboard[row][column]) != 0)
                    g.drawImage(this.wPawnImg, tileboard[row][column][0], tileboard[row][column][1], this);
                if((board.getWhiteKnights() & bitboard[row][column]) != 0)
                    g.drawImage(this.wKnightImg, tileboard[row][column][0], tileboard[row][column][1], this);
                if((board.getWhiteBishops() & bitboard[row][column]) != 0)
                    g.drawImage(this.wBishopImg, tileboard[row][column][0], tileboard[row][column][1], this);
                if((board.getWhiteRooks() & bitboard[row][column]) != 0)
                    g.drawImage(this.wRookImg, tileboard[row][column][0], tileboard[row][column][1], this);
                if((board.getWhiteQueens() & bitboard[row][column]) != 0)
                    g.drawImage(this.wQueenImg, tileboard[row][column][0], tileboard[row][column][1], this);
                if((board.getWhiteKing() & bitboard[row][column]) != 0)
                    g.drawImage(this.wKingImg, tileboard[row][column][0], tileboard[row][column][1], this);
                
                if((board.getBlackPawns() & bitboard[row][column]) != 0)
                    g.drawImage(this.bPawnImg, tileboard[row][column][0], tileboard[row][column][1], this);
                if((board.getBlackKnights() & bitboard[row][column]) != 0)
                    g.drawImage(this.bKnightImg, tileboard[row][column][0], tileboard[row][column][1], this);
                if((board.getBlackBishops() & bitboard[row][column]) != 0)
                    g.drawImage(this.bBishopImg, tileboard[row][column][0], tileboard[row][column][1], this);
                if((board.getBlackRooks() & bitboard[row][column]) != 0)
                    g.drawImage(this.bRookImg, tileboard[row][column][0], tileboard[row][column][1], this);
                if((board.getBlackQueens() & bitboard[row][column]) != 0)
                    g.drawImage(this.bQueenImg, tileboard[row][column][0], tileboard[row][column][1], this);
                if((board.getBlackKing() & bitboard[row][column]) != 0)
                    g.drawImage(this.bKingImg, tileboard[row][column][0], tileboard[row][column][1], this);
            }
        }

    }
}

