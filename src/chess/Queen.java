package chess;

abstract public class Queen {
    // Base value of the queen according to the game evaluation system
    public static final double BASE_VALUE = 9.0;

    public static long calculatePossibleMoves(Board board, int queenRow, int queenColumn, char color) {
        long possibleMoves;
        
        // The queen moves like the combination of a bishop and a rook
        possibleMoves = MoveGenerator.calculatePossibleMoves('b', board, queenRow, queenColumn, color) |
                        MoveGenerator.calculatePossibleMoves('r', board, queenRow, queenColumn, color);

        return possibleMoves;
    }
}
