package chess;

public class Main {

    public static void main(String[] args) {
        Board board = new Board();
        GameLogic gameLogic = new GameLogic(board);

        @SuppressWarnings("unused")
        GameWindow gameWindow = new GameWindow(board, gameLogic);
    }
}
