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

        long whitePieces = board.getWhitePieces();
        long blackPieces = board.getBlackPieces();
        
        long blockedPieces;
        if (color == 'w')
            blockedPieces = whitePieces;
        else
            blockedPieces = blackPieces;
        
        // Testing each target tile individually
        long kingPosition = bitboard[kingRow][kingColumn];

        // Up
        if(!GameLogic.isMoveIllegal(board, color, 'k', kingRow, kingColumn, kingRow-1, kingColumn))
            possibleMoves |= kingPosition >>> 8;

        // Left
        if(!GameLogic.isMoveIllegal(board, color, 'k', kingRow, kingColumn, kingRow, kingColumn-1))
            possibleMoves |= (kingPosition >>> 1) & NOT_H;
    
        // Up-Left
        if(!GameLogic.isMoveIllegal(board, color, 'k', kingRow, kingColumn, kingRow-1, kingColumn-1))
            possibleMoves |= (kingPosition >>> 9) & NOT_H;

        // Up-Right
        if(!GameLogic.isMoveIllegal(board, color, 'k', kingRow, kingColumn, kingRow-1, kingColumn+1))
            possibleMoves |= (kingPosition >>> 7) & NOT_A;

        // Down
        if(!GameLogic.isMoveIllegal(board, color, 'k', kingRow, kingColumn, kingRow+1, kingColumn))
            possibleMoves |= kingPosition << 8;

        // Right
        if(!GameLogic.isMoveIllegal(board, color, 'k', kingRow, kingColumn, kingRow, kingColumn+1))
            possibleMoves |= (kingPosition << 1) & NOT_A;

        // Down-Right
        if(!GameLogic.isMoveIllegal(board, color, 'k', kingRow, kingColumn, kingRow+1, kingColumn+1))
            possibleMoves |= (kingPosition << 9) & NOT_A;

        // Down-Left
        if(!GameLogic.isMoveIllegal(board, color, 'k', kingRow, kingColumn, kingRow+1, kingColumn-1))
            possibleMoves |= (kingPosition << 7) & NOT_H;

        possibleMoves &= ~blockedPieces;
        System.out.println("POSIIBLE KING MOVES (" + kingRow + ", " + kingColumn + "): " + possibleMoves);
        return possibleMoves;
    }

    public static long calculatePossibleCaptures(Board board, int kingRow, int kingColumn, char color) {
        long[][] bitboard = board.getBitboard();
        long possibleCaptures;

        long whitePieces = board.getWhitePieces();
        long blackPieces = board.getBlackPieces();
        
        long blockedPieces;
        if (color == 'w')
            blockedPieces = whitePieces;
        else
            blockedPieces = blackPieces;
        
        // Testing each target tile individually
        long kingPosition = bitboard[kingRow][kingColumn];

        possibleCaptures = (kingPosition << 8) | (kingPosition >>> 8); // Up and Down

        // Left moves, if not on H file
        possibleCaptures |= (kingPosition & NOT_H) << 1;
        possibleCaptures |= (kingPosition & NOT_H) << 9;
        possibleCaptures |= (kingPosition & NOT_H) >>> 7;

        // Right moves, if not on A file
        possibleCaptures |= (kingPosition & NOT_A) >>> 1;
        possibleCaptures |= (kingPosition & NOT_A) >>> 9;
        possibleCaptures |= (kingPosition & NOT_A) << 7;

        return possibleCaptures & ~blockedPieces;
    }
}
