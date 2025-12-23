package chess;

public class MoveGenerator {
    // Returns bitmap of possible moves
    public static long calculatePossibleMoves(char pieceType, Board board, int row, int column, char color) {
        return switch (Character.toLowerCase(pieceType)) {
            case 'p' -> Pawn.calculatePossibleMoves(board, row, column, color);
            case 'n' -> Knight.calculatePossibleMoves(board, row, column, color);
            case 'b' -> Bishop.calculatePossibleMoves(board, row, column, color);
            case 'r' -> Rook.calculatePossibleMoves(board, row, column, color);
            case 'q' -> Queen.calculatePossibleMoves(board, row, column, color);
            case 'k' -> King.calculatePossibleMoves(board, row, column, color);

            default -> 0L;
        };
    }

    public static long calculatePossibleCaptures(char pieceType, Board board, int row, int column, char color) {
        return switch (Character.toLowerCase(pieceType)) {
            case 'p' -> Pawn.calculatePossibleCaptures(board, row, column, color);
            case 'n' -> Knight.calculatePossibleCaptures(board, row, column, color);
            case 'b' -> Bishop.calculatePossibleCaptures(board, row, column, color);
            case 'r' -> Rook.calculatePossibleCaptures(board, row, column, color);
            case 'q' -> Queen.calculatePossibleCaptures(board, row, column, color);
            case 'k' -> King.calculatePossibleCaptures(board, row, column, color);

            default -> 0L;
        };
    }

    public static long getAttackingTiles(Board board, char color) {
        int row, column, boardSize = board.getBoardSize();
        long[][] bitboard = board.getBitboard();
        long attackingTiles = 0L;
        
        if(color == 'w') {
            for(row = 0; row < boardSize; row++) {
                for(column = 0; column < boardSize; column++) {
                    if((board.getWhitePawns() & bitboard[row][column]) != 0)
                        attackingTiles |= MoveGenerator.calculatePossibleCaptures('p', board, row, column, color);
                    else if((board.getWhiteKnights() & bitboard[row][column]) != 0)
                        attackingTiles |= MoveGenerator.calculatePossibleCaptures('n', board, row, column, color);
                    else if((board.getWhiteBishops() & bitboard[row][column]) != 0)
                        attackingTiles |= MoveGenerator.calculatePossibleCaptures('b', board, row, column, color);
                    else if((board.getWhiteRooks() & bitboard[row][column]) != 0)
                        attackingTiles |= MoveGenerator.calculatePossibleCaptures('r', board, row, column, color);
                    else if((board.getWhiteQueens() & bitboard[row][column]) != 0)
                        attackingTiles |= MoveGenerator.calculatePossibleCaptures('q', board, row, column, color);
                    else if((board.getWhiteKing() & bitboard[row][column]) != 0)
                        attackingTiles |= MoveGenerator.calculatePossibleCaptures('k', board, row, column, color);
                }
            }
        }
        else {
            for(row = 0; row < boardSize; row++) {
                for(column = 0; column < boardSize; column++) {
                    if((board.getBlackPawns() & bitboard[row][column]) != 0)
                        attackingTiles |= MoveGenerator.calculatePossibleCaptures('p', board, row, column, color);
                    else if((board.getBlackKnights() & bitboard[row][column]) != 0)
                        attackingTiles |= MoveGenerator.calculatePossibleCaptures('n', board, row, column, color);
                    else if((board.getBlackBishops() & bitboard[row][column]) != 0)
                        attackingTiles |= MoveGenerator.calculatePossibleCaptures('b', board, row, column, color);
                    else if((board.getBlackRooks() & bitboard[row][column]) != 0)
                        attackingTiles |= MoveGenerator.calculatePossibleCaptures('r', board, row, column, color);
                    else if((board.getBlackQueens() & bitboard[row][column]) != 0)
                        attackingTiles |= MoveGenerator.calculatePossibleCaptures('q', board, row, column, color);
                    else if((board.getBlackKing() & bitboard[row][column]) != 0)
                        attackingTiles |= MoveGenerator.calculatePossibleCaptures('k', board, row, column, color);
                }
            }
        }
        
        return attackingTiles;
    }
}
