package chess;

abstract public class Bishop {
    // Base value of the bishop according to the game evaluation system
    public static final double BASE_VALUE = 3.0;

    public static long calculatePossibleMoves(Board board, int bishopRow, int bishopColumn, char color) {
        long[][] bitboard = board.getBitboard();
        long possibleMoves = 0L;

        // Defines tiles with pieces of the same color
        long whitePieces = board.getWhitePawns() | board.getWhiteKnights() | board.getWhiteBishops() |
        board.getWhiteRooks() | board.getWhiteQueens() | board.getWhiteKing();
            
        // Defines tiles with enemy pieces
        long blackPieces = board.getBlackPawns() | board.getBlackKnights() | board.getBlackBishops() |
        board.getBlackRooks() | board.getBlackQueens() | board.getBlackKing();

        if (color == 'w') {
            long blockedPieces = whitePieces;
            long capturablePieces = blackPieces;
        }
        else {
            long blockedPieces = blackPieces;
            long capturablePieces = whitePieces;
        }

        int boardSize = board.getBoardSize();
        
        for(int row = 0; row < boardSize; row++) {
            for(int column = 0; column < boardSize; column++) {
                if (Math.abs(row - bishopRow) == Math.abs(column - bishopColumn) && row != bishopRow && column != bishopColumn) {
                    possibleMoves |= bitboard[row][column];
                }
            }
        }
        
        return possibleMoves;
    }
}
