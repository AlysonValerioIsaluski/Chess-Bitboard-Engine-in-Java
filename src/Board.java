public class Board {
    // Prevention of magic number
    private final int boardSize = 8;

    // Bitmap for tiles in board
    private long[][] board = new long[boardSize][boardSize];

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
    private boolean whiteTurn; // True if white's turn and False if black's turn
    private long enPassantTiles; // Tile behind capturable pawn used as reference
    private long castlingRightsTiles; // Tile two tiles away from king used as reference

    public Board() {

        // Extracting bitmap for the tiles in the board to an 8x8 matrix
        long tileAmmount = 0;
        for(int row = 0; row < boardSize; row++) {
            for(int column = 0; column < boardSize; column++) {
                this.board[row][column] = 1L << tileAmmount;
                tileAmmount++;
            }
        }
        
        // Setting pawns
        for(int column = 0; column < boardSize; column++) {
            this.whitePawns |= this.board[6][column];
            this.blackPawns |= this.board[1][column];
        }
        
        // Setting knights
        this.whiteKnights = this.board[7][1] | this.board[7][6];
        this.blackKnights = this.board[0][1] | this.board[0][6];

        // Setting bishops
        this.whiteBishops = this.board[7][2] | this.board[7][5];
        this.blackBishops = this.board[0][2] | this.board[0][5];

        // Setting rooks
        this.whiteRooks = this.board[7][0] | this.board[7][7];
        this.blackRooks = this.board[0][0] | this.board[0][7];

        // Setting queens
        this.whiteQueens = this.board[7][4];
        this.blackQueens = this.board[0][4];

        // Setting kings
        this.whiteKing = this.board[7][3];
        this.blackKing = this.board[0][3];
    }
}
