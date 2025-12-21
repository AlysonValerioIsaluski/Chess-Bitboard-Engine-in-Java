package chess;

abstract public class Queen {
    // Base value of the queen according to the game evaluation system
    public static final double BASE_VALUE = 9.0;

    public static long calculatePossibleMoves(Board board, int queenRow, int queenColumn, char color) {
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

        // Left
        int column = queenColumn;
        while(column > 0) {
            column--;

            if((blockedPieces & bitboard[queenRow][column]) != 0)
                break;
            
            if(GameLogic.isMoveIllegal(board, color, 'q', queenRow, queenColumn, queenRow, column))
                continue;

            possibleMoves |= bitboard[queenRow][column];

            if((capturablePieces & bitboard[queenRow][column]) != 0)
                break;
        }

        // Right
        column = queenColumn;
        while(column < boardSize-1) {
            column++;

            if((blockedPieces & bitboard[queenRow][column]) != 0)
                break;

            if(GameLogic.isMoveIllegal(board, color, 'q', queenRow, queenColumn, queenRow, column))
                continue;

            possibleMoves |= bitboard[queenRow][column];

            if((capturablePieces & bitboard[queenRow][column]) != 0)
                break;
        }

        // Up
        int row = queenRow;
        while(row > 0) {
            row--;

            if((blockedPieces & bitboard[row][queenColumn]) != 0)
                break;

            if(GameLogic.isMoveIllegal(board, color, 'q', queenRow, queenColumn, row, queenColumn))
                continue;

            possibleMoves |= bitboard[row][queenColumn];

            if((capturablePieces & bitboard[row][queenColumn]) != 0)
                break;
        }

        // Down
        row = queenRow;
        while(row < boardSize-1) {
            row++;

            if((blockedPieces & bitboard[row][queenColumn]) != 0)
                break;

            if(GameLogic.isMoveIllegal(board, color, 'q', queenRow, queenColumn, row, queenColumn))
                continue;

            possibleMoves |= bitboard[row][queenColumn];

            if((capturablePieces & bitboard[row][queenColumn]) != 0)
                break;
        }

        // Upper-Left Diagonal
        row = queenRow;
        column = queenColumn;
        while(row > 0 && column > 0) {
            row--; column--;

            if((blockedPieces & bitboard[row][column]) != 0)
                break;

            if(GameLogic.isMoveIllegal(board, color, 'q', queenRow, queenColumn, row, column))
                continue;

            possibleMoves |= bitboard[row][column];

            if((capturablePieces & bitboard[row][column]) != 0)
                break;
        }

        // Upper-Right Diagonal
        row = queenRow;
        column = queenColumn;
        while(row > 0 && column < boardSize-1) {
            row--; column++;

            if((blockedPieces & bitboard[row][column]) != 0)
                break;

            if(GameLogic.isMoveIllegal(board, color, 'q', queenRow, queenColumn, row, column))
                continue;
            
            possibleMoves |= bitboard[row][column];
            
            if((capturablePieces & bitboard[row][column]) != 0)
                break;
        }

        // Lower-Left Diagonal
        row = queenRow;
        column = queenColumn;
        while(row < boardSize-1 && column > 0) {
            row++; column--;

            if((blockedPieces & bitboard[row][column]) != 0)
                break;

            if(GameLogic.isMoveIllegal(board, color, 'q', queenRow, queenColumn, row, column))
                continue;

            possibleMoves |= bitboard[row][column];

            if((capturablePieces & bitboard[row][column]) != 0)
                break;
        }

        // Lower-Right Diagonal
        row = queenRow;
        column = queenColumn;
        while(row < boardSize-1 && column < boardSize-1) {
            row++; column++;

            if((blockedPieces & bitboard[row][column]) != 0)
                break;

            if(GameLogic.isMoveIllegal(board, color, 'q', queenRow, queenColumn, row, column))
                continue;

            possibleMoves |= bitboard[row][column];

            if((capturablePieces & bitboard[row][column]) != 0)
                break;
        }

        return possibleMoves;
    }

    public static long calculatePossibleCaptures(Board board, int queenRow, int queenColumn, char color) {
        long possibleCaptures;
        
        // The queen moves like the combination of a bishop and a rook
        possibleCaptures = MoveGenerator.calculatePossibleCaptures('b', board, queenRow, queenColumn, color) |
                        MoveGenerator.calculatePossibleCaptures('r', board, queenRow, queenColumn, color);

        return possibleCaptures;
    }
}

