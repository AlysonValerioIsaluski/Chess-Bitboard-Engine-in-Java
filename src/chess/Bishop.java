package chess;

abstract public class Bishop {
    // Base value of the bishop according to the game evaluation system
    public static final double BASE_VALUE = 3.0;

    public static long calculatePossibleMoves(Board board, int bishopRow, int bishopColumn, char color) {
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

        // Upper-Left Diagonal
        int row = bishopRow;
        int column = bishopColumn;
        while(row > 0 && column > 0) {
            row--; column--;

            if((blockedPieces & bitboard[row][column]) != 0)
                break;

            if(GameLogic.isMoveIllegal(board, color, 'b', bishopRow, bishopColumn, row, column))
                continue;

            possibleMoves |= bitboard[row][column];

            if((capturablePieces & bitboard[row][column]) != 0)
                break;
        }

        // Upper-Right Diagonal
        row = bishopRow;
        column = bishopColumn;
        while(row > 0 && column < boardSize-1) {
            row--; column++;

            if((blockedPieces & bitboard[row][column]) != 0)
                break;

            if(GameLogic.isMoveIllegal(board, color, 'b', bishopRow, bishopColumn, row, column))
                continue;
            
            possibleMoves |= bitboard[row][column];
            
            if((capturablePieces & bitboard[row][column]) != 0)
                break;
        }

        // Lower-Left Diagonal
        row = bishopRow;
        column = bishopColumn;
        while(row < boardSize-1 && column > 0) {
            row++; column--;

            if((blockedPieces & bitboard[row][column]) != 0)
                break;

            if(GameLogic.isMoveIllegal(board, color, 'b', bishopRow, bishopColumn, row, column))
                continue;

            possibleMoves |= bitboard[row][column];

            if((capturablePieces & bitboard[row][column]) != 0)
                break;
        }

        // Lower-Right Diagonal
        row = bishopRow;
        column = bishopColumn;
        while(row < boardSize-1 && column < boardSize-1) {
            row++; column++;

            if((blockedPieces & bitboard[row][column]) != 0)
                break;

            if(GameLogic.isMoveIllegal(board, color, 'b', bishopRow, bishopColumn, row, column))
                continue;

            possibleMoves |= bitboard[row][column];

            if((capturablePieces & bitboard[row][column]) != 0)
                break;
        }

        return possibleMoves;
    }

    public static long calculatePossibleCaptures(Board board, int bishopRow, int bishopColumn, char color) {
        long[][] bitboard = board.getBitboard();
        long possibleCaptures = 0L;

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

        // Upper-Left Diagonal
        int row = bishopRow;
        int column = bishopColumn;
        while(row > 0 && column > 0) {
            row--; column--;

            if((blockedPieces & bitboard[row][column]) != 0)
                break;

            possibleCaptures |= bitboard[row][column];

            if((capturablePieces & bitboard[row][column]) != 0)
                break;
        }

        // Upper-Right Diagonal
        row = bishopRow;
        column = bishopColumn;
        while(row > 0 && column < boardSize-1) {
            row--; column++;

            if((blockedPieces & bitboard[row][column]) != 0)
                break;
            
            possibleCaptures |= bitboard[row][column];
            
            if((capturablePieces & bitboard[row][column]) != 0)
                break;
        }

        // Lower-Left Diagonal
        row = bishopRow;
        column = bishopColumn;
        while(row < boardSize-1 && column > 0) {
            row++; column--;

            if((blockedPieces & bitboard[row][column]) != 0)
                break;

            possibleCaptures |= bitboard[row][column];

            if((capturablePieces & bitboard[row][column]) != 0)
                break;
        }

        // Lower-Right Diagonal
        row = bishopRow;
        column = bishopColumn;
        while(row < boardSize-1 && column < boardSize-1) {
            row++; column++;

            if((blockedPieces & bitboard[row][column]) != 0)
                break;

            possibleCaptures |= bitboard[row][column];

            if((capturablePieces & bitboard[row][column]) != 0)
                break;
        }

        return possibleCaptures;
    }
}
