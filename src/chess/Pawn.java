package chess;

abstract public class Pawn {
    // Base value of the pawn according to the game evaluation system
    public static final double BASE_VALUE = 1.0;

    public static long calculatePossibleMoves(Board board, int pawnRow, int pawnColumn, char color) {
        long[][] bitboard = board.getBitboard();
        long possibleMoves = 0L;

        // Defines tiles with pieces of the same color
        long whitePieces = board.getWhitePawns() | board.getWhiteKnights() | board.getWhiteBishops() |
        board.getWhiteRooks() | board.getWhiteQueens() | board.getWhiteKing() | board.getEnpassantTiles();
            
        // Defines tiles with enemy pieces
        long blackPieces = board.getBlackPawns() | board.getBlackKnights() | board.getBlackBishops() |
        board.getBlackRooks() | board.getBlackQueens() | board.getBlackKing() | board.getEnpassantTiles();

        long blockedPieces;
        long capturablePieces;
        int boardSize = board.getBoardSize();
        
        if (color == 'w') {
            blockedPieces = whitePieces;
            capturablePieces = blackPieces;
        
            // Checks if there are capturable pieces on the diagonal
            if(pawnColumn != 0 && (capturablePieces & bitboard[pawnRow-1][pawnColumn-1]) != 0)
                possibleMoves |= bitboard[pawnRow-1][pawnColumn-1];
            if(pawnColumn != boardSize && (capturablePieces & bitboard[pawnRow-1][pawnColumn+1]) != 0)
                possibleMoves |= bitboard[pawnRow-1][pawnColumn+1];
            
            // Checks if there is a piece in front
            if((blockedPieces & bitboard[pawnRow-1][pawnColumn]) != 0 || (capturablePieces & bitboard[pawnRow-1][pawnColumn]) != 0)
                return possibleMoves;
            else
                possibleMoves |= bitboard[pawnRow-1][pawnColumn];

            // Checks if pawn is still in starting tile, then it can move two tiles forward
            if(pawnRow >= boardSize-2)
                possibleMoves |= bitboard[pawnRow-2][pawnColumn];
        }
        else {
            blockedPieces = blackPieces;
            capturablePieces = whitePieces;

            // Checks if there are capturable pieces on the diagonal
            if(pawnColumn != 0 && (capturablePieces & bitboard[pawnRow-1][pawnColumn-1]) != 0)
                possibleMoves |= bitboard[pawnRow+1][pawnColumn-1];
            if(pawnColumn != boardSize && (capturablePieces & bitboard[pawnRow-1][pawnColumn+1]) != 0)
                possibleMoves |= bitboard[pawnRow+1][pawnColumn+1];

            // Checks if there is a piece in front
            if((blockedPieces & bitboard[pawnRow+1][pawnColumn]) != 0 || (capturablePieces & bitboard[pawnRow+1][pawnColumn]) != 0)
                return possibleMoves;
            else
                possibleMoves |= bitboard[pawnRow+1][pawnColumn];
            
            // Checks if pawn is still in starting tile, then it can move two tiles forward
            if(pawnRow <= 1)
                possibleMoves |= bitboard[pawnRow+2][pawnColumn];
        }
        
        return possibleMoves;
    }
}
