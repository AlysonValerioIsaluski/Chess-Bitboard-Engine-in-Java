package chess;

import java.awt.*;
import java.awt.image.*;
import javax.swing.JPanel;

public class BoardPanel extends JPanel {
    // Bitmap for tiles in board
    private final Board board;
    private final int boardSize;
    private final int TILE_OFFSET = 64;
    private final int TILE_SIZE = 96;

    private char selectedPiece = '0';
    private long selectedPiecePossibleMoves, enemyPieces;
    private int selectedPieceRow, selectedPieceColumn;

    // Saves coordinates in pixels of the tiles in the board
    private final int[][][] tileboard;

    private final BufferedImage boardImg;
    private final Image wPawnImg, wKnightImg, wBishopImg, wRookImg, wQueenImg, wKingImg;
    private final Image bPawnImg, bKnightImg, bBishopImg, bRookImg, bQueenImg, bKingImg;
    private final Image greenCircleImg, orangeCircleImg, greenSquareImg;

    public BoardPanel(Board board, BufferedImage boardImg,
        Image wPawnImg, Image wKnightImg, Image wBishopImg,
        Image wRookImg, Image wQueenImg, Image wKingImg,
        Image bPawnImg, Image bKnightImg, Image bBishopImg,
        Image bRookImg, Image bQueenImg, Image bKingImg,
        Image greenCircleImg, Image orangeCircleImg, Image greenSquareImg) {
       
        boardSize = board.getBoardSize();
        tileboard = new int[boardSize][boardSize][2];
        
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

        this.greenCircleImg = greenCircleImg;
        this.orangeCircleImg = orangeCircleImg;
        this.greenSquareImg = greenSquareImg;
        
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
                handleMouseClick(e.getX() + TILE_OFFSET, e.getY() + TILE_OFFSET);
            }
        });
    }

    // Displays the board and pieces, among other ui elements
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        long[][] bitboard = board.getBitboard();
        
        // Drawing board
        if (this.boardImg != null) {
            g.drawImage(this.boardImg, 0, 0, this);
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

    // Does click logic depending on click location
    private void handleMouseClick(int mouseX, int mouseY) {
        // Gets clicked tile
        int column = mouseX / TILE_SIZE;
        int row = mouseY / TILE_SIZE;

        // Check if tile clicked is bounded inside the board
        if (row >= 0 && row <= this.boardSize && column >= 0 && column <= this.boardSize) {
            selectSquare(row-1, column-1);
            System.out.println("Tile selected: (" + (row-1) + ", " + (column-1) + ")");
        }
        else
            this.selectedPiecePossibleMoves = 0L;
    }

    private void selectSquare(int row, int column) {
        // Sets selected piece as none
        this.selectedPiece = '0';
        long[][] bitboard = board.getBitboard();

        // Defines tiles with pieces of the same color
        long whitePieces = board.getWhitePieces();
        
        // Defines tiles with enemy pieces
        long blackPieces = board.getBlackPieces();
        long playerPieces;

        if (board.isWhiteTurn()) {
            playerPieces = whitePieces;
            this.enemyPieces = blackPieces;

            if((playerPieces & bitboard[row][column]) != 0) {
                if(((playerPieces & board.getWhitePawns()) & bitboard[row][column]) != 0)
                    this.selectedPiece = 'p';
                else if(((playerPieces & board.getWhiteKnights()) & bitboard[row][column]) != 0)
                    this.selectedPiece = 'n';
                else if(((playerPieces & board.getWhiteBishops()) & bitboard[row][column]) != 0)
                    this.selectedPiece = 'b';
                else if(((playerPieces & board.getWhiteRooks()) & bitboard[row][column]) != 0)
                    this.selectedPiece = 'r';
                else if(((playerPieces & board.getWhiteQueens()) & bitboard[row][column]) != 0)
                    this.selectedPiece = 'q';
                else if(((playerPieces & board.getWhiteKing()) & bitboard[row][column]) != 0)
                    this.selectedPiece = 'k';
                
                this.selectedPiecePossibleMoves = MoveGenerator.calculatePossibleMoves(this.selectedPiece, this.board, row, column, 'w');
            }
        }
        else {
            playerPieces = blackPieces;
            this.enemyPieces = whitePieces;

            if((playerPieces & bitboard[row][column]) != 0) {
                if(((playerPieces & board.getBlackPawns()) & bitboard[row][column]) != 0)
                    this.selectedPiece = 'p';
                else if(((playerPieces & board.getBlackKnights()) & bitboard[row][column]) != 0)
                    this.selectedPiece = 'n';
                else if(((playerPieces & board.getBlackBishops()) & bitboard[row][column]) != 0)
                    this.selectedPiece = 'b';
                else if(((playerPieces & board.getBlackRooks()) & bitboard[row][column]) != 0)
                    this.selectedPiece = 'r';
                else if(((playerPieces & board.getBlackQueens()) & bitboard[row][column]) != 0)
                    this.selectedPiece = 'q';
                else if(((playerPieces & board.getBlackKing()) & bitboard[row][column]) != 0)
                    this.selectedPiece = 'k';
                
                this.selectedPiecePossibleMoves = MoveGenerator.calculatePossibleMoves(this.selectedPiece, this.board, row, column, 'b');
            }
        }
        this.selectedPieceRow = row;
        this.selectedPieceColumn = column;

        repaint();
    }
}

