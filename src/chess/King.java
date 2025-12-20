package chess;

abstract public class King {
    // Base value of the king according to the game evaluation system
    public static final double BASE_VALUE = 10000.0;
    
    // Pre-defined masks to prevend wrap-around when detecting adjacent tiles
    static final long FILE_A = 0x0101010101010101L;
    static final long FILE_H = 0x8080808080808080L;
    static final long NOT_A = ~FILE_A;
    static final long NOT_H = ~FILE_H;

    public static long calculatePossibleMoves(Board board, int kingRow, int kingColumn, char color) {
        long[][] bitboard = board.getBitboard();
        long possibleMoves = 0L;

        // Defines tiles with pieces of the same color
        long whitePieces = board.getWhitePawns() | board.getWhiteKnights() | board.getWhiteBishops() |
        board.getWhiteRooks() | board.getWhiteQueens() | board.getWhiteKing();
            
        // Defines tiles with enemy pieces
        long blackPieces = board.getBlackPawns() | board.getBlackKnights() | board.getBlackBishops() |
        board.getBlackRooks() | board.getBlackQueens() | board.getBlackKing();
        
        long blockedPieces;
        if (color == 'w')
            blockedPieces = whitePieces;
        else
            blockedPieces = blackPieces;
        
        // Testing each target tile individually
        long kingPosition = bitboard[kingRow][kingColumn];

        possibleMoves = (kingPosition << 8) | (kingPosition >>> 8); // Up and Down

        // Left moves, if not on H file
        possibleMoves |= (kingPosition & NOT_H) << 1;
        possibleMoves |= (kingPosition & NOT_H) << 9;
        possibleMoves |= (kingPosition & NOT_H) >>> 7;

        // Right moves, if not on A file
        possibleMoves |= (kingPosition & NOT_A) >>> 1;
        possibleMoves |= (kingPosition & NOT_A) >>> 9;
        possibleMoves |= (kingPosition & NOT_A) << 7;

        return possibleMoves & ~blockedPieces;
    }
}
