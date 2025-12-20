package chess;

abstract public class Rook {
    // Base value of the rook according to the game evaluation system
    public static final double BASE_VALUE = 5.0;

    public static long calculatePossibleMoves(Board board, int rookRow, int rookColumn, char color) {
        long[][] bitboard = board.getBitboard();
        long possibleMoves = 0L;

        long whitePieces = board.getWhitePieces();
        long blackPieces = board.getBlackPieces();
        
        long blockedPieces;
        long capturablePieces;
        if (color == 'w') {
            blockedPieces = whitePieces;
            capturablePieces = blackPieces;
        }
        else {
            blockedPieces = blackPieces;
            capturablePieces = whitePieces;
        }

        int boardSize = board.getBoardSize();

        // Up
        int column = rookColumn;
        while(column > 0) {
            column--;

            if((blockedPieces & bitboard[rookRow][column]) != 0)
                break;

            possibleMoves |= bitboard[rookRow][column];

            if((capturablePieces & bitboard[rookRow][column]) != 0)
                break;
        }

        // Down
        column = rookColumn;
        while(column < boardSize-1) {
            column++;

            if((blockedPieces & bitboard[rookRow][column]) != 0)
                break;

            possibleMoves |= bitboard[rookRow][column];

            if((capturablePieces & bitboard[rookRow][column]) != 0)
                break;
        }

        // Left
        int row = rookRow;
        while(row > 0) {
            row--;

            if((blockedPieces & bitboard[row][rookColumn]) != 0)
                break;

            possibleMoves |= bitboard[row][rookColumn];

            if((capturablePieces & bitboard[row][rookColumn]) != 0)
                break;
        }

        // Right
        row = rookRow;
        while(row < boardSize-1) {
            row++;

            if((blockedPieces & bitboard[row][rookColumn]) != 0)
                break;

            possibleMoves |= bitboard[row][rookColumn];

            if((capturablePieces & bitboard[row][rookColumn]) != 0)
                break;
        }

        return possibleMoves;
    }
}
