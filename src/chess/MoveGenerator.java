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
}
