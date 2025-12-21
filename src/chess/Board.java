package chess;

public class Board {
    // Prevention of magic number
    private final int boardSize;

    // Bitmap for tiles in board
    private final long[][] bitboard;

    // Bitmap for the location of those pieces on the board
    private long whitePawns;
    private long whiteKnights;
    private long whiteBishops;
    private long whiteRooks;
    private long whiteQueens;
    private long whiteKing;
    
    private long blackPawns;
    private long blackKnights;
    private long blackBishops;
    private long blackRooks;
    private long blackQueens;
    private long blackKing;

    // Additional rules
    private char turn; // 'w' if it's white, 'b' if it's black
    private long enPassantTiles; // Tile behind capturable pawn used as reference
    private long castlingRightsTiles; // Tile two tiles away from king used as reference

    public Board() {
        this.boardSize = 8;
        this.bitboard = this.initializeBitboard();
        
        // Setting pawns
        for(int column = 0; column < boardSize; column++) {
            this.whitePawns |= this.bitboard[6][column];
            this.blackPawns |= this.bitboard[1][column];
        }
        
        // Setting knights
        this.whiteKnights = this.bitboard[7][1] | this.bitboard[7][6];
        this.blackKnights = this.bitboard[0][1] | this.bitboard[0][6];

        // Setting bishops
        this.whiteBishops = this.bitboard[7][2] | this.bitboard[7][5];
        this.blackBishops = this.bitboard[0][2] | this.bitboard[0][5];

        // Setting rooks
        this.whiteRooks = this.bitboard[7][0] | this.bitboard[7][7];
        this.blackRooks = this.bitboard[0][0] | this.bitboard[0][7];

        // Setting queens
        this.whiteQueens = this.bitboard[7][3];
        this.blackQueens = this.bitboard[0][3];

        // Setting kings
        this.whiteKing = this.bitboard[7][4];
        this.blackKing = this.bitboard[0][4];
        
        // Additional rules
        this.turn = 'w';
        this.enPassantTiles = 0L;
        this.castlingRightsTiles = (this.whiteKing << 2 | this.whiteKing >> 2) | (this.blackKing << 2 | this.blackKing >> 2);
    }

    public Board(long whitePawns, long whiteKnights, long whiteBishops,
    long whiteRooks, long whiteQueens, long whiteKing,
    long blackPawns, long blackKnights, long blackBishops, 
    long blackRooks, long blackQueens, long blackKing, 
    char turn, long enPassantTiles, long castlingRightsTiles) {
        this.boardSize = 8;
        this.bitboard = this.initializeBitboard();
        
        // White pieces
        this.whitePawns = whitePawns;
        this.whiteKnights = whiteKnights;
        this.whiteBishops = whiteBishops;
        this.whiteRooks = whiteRooks;
        this.whiteQueens = whiteQueens;
        this.whiteKing = whiteKing;

        // Black pieces
        this.blackPawns = blackPawns;
        this.blackKnights = blackKnights;
        this.blackBishops = blackBishops;
        this.blackRooks = blackRooks;
        this.blackQueens = blackQueens;
        this.blackKing = blackKing;

        // Additional rules
        this.turn = turn;
        this.enPassantTiles = enPassantTiles;
        this.castlingRightsTiles = castlingRightsTiles;
    }

    public Board(Board other) {
    this(other.getWhitePawns(), other.getWhiteKnights(), other.getWhiteBishops(),
        other.getWhiteRooks(), other.getWhiteQueens(), other.getWhiteKing(),
        other.getBlackPawns(), other.getBlackKnights(), other.getBlackBishops(), 
        other.getBlackRooks(), other.getBlackQueens(), other.getBlackKing(), 
        other.getTurn(), other.getEnpassantTiles(), other.getCastlingRightsTiles());
}

    private long[][] initializeBitboard() {
        long tileAmmount = 0;
        long[][] bitBoard = new long[this.boardSize][this.boardSize];
        
        for(int row = 0; row < boardSize; row++) {
            for(int column = 0; column < boardSize; column++) {
                bitBoard[row][column] = 1L << tileAmmount;
                tileAmmount++;
            }
        }

        return bitBoard;
    }

    public long[][] getBitboard() {
        return this.bitboard;
    }

    public long getWhitePawns() {
        return this.whitePawns;
    }
    
    public long getWhiteKnights() {
        return this.whiteKnights;
    }

    public long getWhiteBishops() {
        return this.whiteBishops;
    }
    
    public long getWhiteRooks() {
        return this.whiteRooks;
    }
    
    public long getWhiteQueens() {
        return this.whiteQueens;
    }
    
    public long getWhiteKing() {
        return this.whiteKing;
    }

    public long getWhitePieces() {
        return this.whitePawns | this.whiteKnights | this.whiteBishops |
        this.whiteRooks | this.whiteQueens | this.whiteKing;
    }

    public long getBlackPawns() {
        return this.blackPawns;
    }

    public long getBlackKnights() {
        return this.blackKnights;
    }
    
    public long getBlackBishops() {
        return this.blackBishops;
    }
    
    public long getBlackRooks() {
        return this.blackRooks;
    }
    
    public long getBlackQueens() {
        return this.blackQueens;
    }
    
    public long getBlackKing() {
        return this.blackKing;
    }

    public long getBlackPieces() {
        return this.blackPawns | this.blackKnights | this.blackBishops |
        this.blackRooks | this.blackQueens | this.blackKing;
    }
    
    public char getTurn() {
        return this.turn;
    }

    public long getCastlingRightsTiles() {
        return this.castlingRightsTiles;
    }

    public long getEnpassantTiles() {
        return this.enPassantTiles;
    }

    public int getBoardSize() {
        return this.boardSize;
    }



    public void setWhitePawns(long whitePawns) {
        this.whitePawns = whitePawns;
    }

    public void setWhiteKnights(long whiteKnights) {
        this.whiteKnights = whiteKnights;
    }

    public void setWhiteBishops(long whiteBishops) {
        this.whiteBishops = whiteBishops;
    }

    public void setWhiteRooks(long whiteRooks) {
        this.whiteRooks = whiteRooks;
    }

    public void setWhiteQueens(long whiteQueens) {
        this.whiteQueens = whiteQueens;
    }

    public void setWhiteKing(long whiteKing) {
        this.whiteKing = whiteKing;
    }

    public void setBlackPawns(long blackPawns) {
        this.blackPawns = blackPawns;
    }

    public void setBlackKnights(long blackKnights) {
        this.blackKnights = blackKnights;
    }

    public void setBlackBishops(long blackBishops) {
        this.blackBishops = blackBishops;
    }

    public void setBlackRooks(long blackRooks) {
        this.blackRooks = blackRooks;
    }

    public void setBlackQueens(long blackQueens) {
        this.blackQueens = blackQueens;
    }

    public void setBlackKing(long blackKing) {
        this.blackKing = blackKing;
    }

    public void setTurn(char turn) {
        this.turn = turn;
    }

    public void setCastlingRightsTiles(long castlingRightsTiles) {
        this.castlingRightsTiles = castlingRightsTiles;
    }

    public void setEnpassantTiles(long enPassantTiles) {
        this.enPassantTiles = enPassantTiles;
    }
}
