package chess;

abstract public class Pawn {
    // Base value of the pawn according to the game evaluation system
    public static final double BASE_VALUE = 1.0;

    public static long calculatePossibleMoves(Board board, int pawnRow, int pawnColumn, char color) {
        long[][] bitboard = board.getBitboard();
        long possibleMoves = 0L;

        long whitePieces = board.getWhitePieces();
        long blackPieces = board.getBlackPieces();

        long blockedPieces;
        long capturablePieces;
        int boardSize = board.getBoardSize();
        
        if (color == 'w') {
            blockedPieces = whitePieces;
            capturablePieces = blackPieces;
        
            // Checks if there are capturable pieces on the diagonal
            if(pawnColumn != 0 && (capturablePieces & bitboard[pawnRow-1][pawnColumn-1]) != 0)
                possibleMoves |= bitboard[pawnRow-1][pawnColumn-1];
            if(pawnColumn != boardSize-1 && (capturablePieces & bitboard[pawnRow-1][pawnColumn+1]) != 0)
                possibleMoves |= bitboard[pawnRow-1][pawnColumn+1];
            
            // Checks if there is a piece in front
            if((blockedPieces & bitboard[pawnRow-1][pawnColumn]) != 0 || (capturablePieces & bitboard[pawnRow-1][pawnColumn]) != 0)
                return possibleMoves;
            else
                possibleMoves |= bitboard[pawnRow-1][pawnColumn];

            // Checks if pawn is still in starting tile, then it can move two tiles forward
            if(pawnRow >= boardSize-2) {
                // Checks if there is a piece 2 tiles in front
                if((blockedPieces & bitboard[pawnRow-2][pawnColumn]) != 0 || (capturablePieces & bitboard[pawnRow-2][pawnColumn]) != 0)
                    return possibleMoves;
                possibleMoves |= bitboard[pawnRow-2][pawnColumn];
            }
        }
        else {
            blockedPieces = blackPieces;
            capturablePieces = whitePieces;

            // Checks if there are capturable pieces on the diagonal
            if(pawnColumn != 0 && (capturablePieces & bitboard[pawnRow+1][pawnColumn-1]) != 0)
                possibleMoves |= bitboard[pawnRow+1][pawnColumn-1];
            if(pawnColumn != boardSize-1 && (capturablePieces & bitboard[pawnRow+1][pawnColumn+1]) != 0)
                possibleMoves |= bitboard[pawnRow+1][pawnColumn+1];

            // Checks if there is a piece in front
            if((blockedPieces & bitboard[pawnRow+1][pawnColumn]) != 0 || (capturablePieces & bitboard[pawnRow+1][pawnColumn]) != 0)
                return possibleMoves;
            else
                possibleMoves |= bitboard[pawnRow+1][pawnColumn];
            
            // Checks if pawn is still in starting tile, then it can move two tiles forward
            if(pawnRow <= 1) {
                // Checks if there is a piece in front
                if((blockedPieces & bitboard[pawnRow+2][pawnColumn]) != 0 || (capturablePieces & bitboard[pawnRow+2][pawnColumn]) != 0)
                    return possibleMoves;
                possibleMoves |= bitboard[pawnRow+2][pawnColumn];
            }
        }
        
        return possibleMoves;
    }
}
