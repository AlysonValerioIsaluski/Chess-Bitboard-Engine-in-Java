package chess;

abstract public class Knight {
    // Base value of the knight according to the game evaluation system
    public static final double BASE_VALUE = 3.0;

    // Pre-defined masks to prevend wrap-around when detecting nearby tiles
    static final long FILE_A = 0x0101010101010101L;
    static final long FILE_B = 0x0202020202020202L;
    static final long FILE_G = 0x4040404040404040L;
    static final long FILE_H = 0x8080808080808080L;

    static final long NOT_A = ~FILE_A;
    static final long NOT_AB = ~(FILE_A | FILE_B);
    static final long NOT_H = ~FILE_H;
    static final long NOT_GH = ~(FILE_G | FILE_H);

    public static long calculatePossibleMoves(Board board, int knightRow, int knightColumn, char color) {
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
        long knightPosition = bitboard[knightRow][knightColumn];

        // Up Up Left
        if(!GameLogic.isMoveIllegal(board, color, 'n', knightRow, knightColumn, knightRow-2, knightColumn-1))
            possibleMoves |= (knightPosition >>> 17) & NOT_H;

        // Up Up Right
        if(!GameLogic.isMoveIllegal(board, color, 'n', knightRow, knightColumn, knightRow-2, knightColumn+1))
            possibleMoves |= (knightPosition >>> 15) & NOT_A; 

        // Up Left Left
        if(!GameLogic.isMoveIllegal(board, color, 'n', knightRow, knightColumn, knightRow-1, knightColumn-2))
            possibleMoves |= (knightPosition >>> 10) & NOT_GH; 

        // Up Right Right
        if(!GameLogic.isMoveIllegal(board, color, 'n', knightRow, knightColumn, knightRow-1, knightColumn+2))
            possibleMoves |= (knightPosition >>> 6) & NOT_AB;


        // Down Left Left
        if(!GameLogic.isMoveIllegal(board, color, 'n', knightRow, knightColumn, knightRow+1, knightColumn-2))
            possibleMoves |= (knightPosition << 6) & NOT_GH; 

        // Down Right Right
        if(!GameLogic.isMoveIllegal(board, color, 'n', knightRow, knightColumn, knightRow+1, knightColumn+2))
            possibleMoves |= (knightPosition << 10) & NOT_AB; 

        // Down Down Left
        if(!GameLogic.isMoveIllegal(board, color, 'n', knightRow, knightColumn, knightRow+2, knightColumn-1))
            possibleMoves |= (knightPosition << 15) & NOT_H; 

        // Down Down Right
        if(!GameLogic.isMoveIllegal(board, color, 'n', knightRow, knightColumn, knightRow+2, knightColumn+1))
            possibleMoves |= (knightPosition << 17) & NOT_A;

        return possibleMoves & ~blockedPieces;
    }

    public static long calculatePossibleCaptures(Board board, int knightRow, int knightColumn, char color) {
        long[][] bitboard = board.getBitboard();
        long possibleCaptures = 0L;

        long whitePieces = board.getWhitePieces();
        long blackPieces = board.getBlackPieces();

        long blockedPieces;
        if (color == 'w')
            blockedPieces = whitePieces;
        else
            blockedPieces = blackPieces;


        // Testing each target tile individually
        long knightPosition = bitboard[knightRow][knightColumn];

        // Up Up Left
        possibleCaptures |= (knightPosition >>> 17) & NOT_H;

        // Up Up Right
        possibleCaptures |= (knightPosition >>> 15) & NOT_A;

        // Up Left Left
        possibleCaptures |= (knightPosition >>> 10) & NOT_GH; 

        // Up Right Right
        possibleCaptures |= (knightPosition >>> 6) & NOT_AB; 


        // Down Left Left
        possibleCaptures |= (knightPosition << 6) & NOT_GH; 

        // Down Right Right
        possibleCaptures |= (knightPosition << 10) & NOT_AB; 

        // Down Down Left
        possibleCaptures |= (knightPosition << 15) & NOT_A; 
        
        // Down Down Right
        possibleCaptures |= (knightPosition << 17) & NOT_H;


        return possibleCaptures & ~blockedPieces;
    }
}
