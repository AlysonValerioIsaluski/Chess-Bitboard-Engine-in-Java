package chess;

public class GameLogic {
    private Board board;
    private final int boardSize;
    private final long[][] bitboard;

    private char selectedPiece = '0';
    private long selectedPiecePossibleMoves, enemyPieces;
    private int selectedPieceRow, selectedPieceColumn;

    public GameLogic(Board board) {
        this.board = board;
        this.boardSize = board.getBoardSize();
        this.bitboard = board.getBitboard();
    }

    public void handleTileSelection(int row, int column) {
        // Check if tile clicked is bounded inside the board
        if (row > 0 && row <= boardSize && column > 0 && column <= boardSize) {
            selectSquare(row-1, column-1);
            System.out.println("Tile selected: (" + (row-1) + ", " + (column-1) + ")");
        }
        else
            selectedPiece = '0';
    }

    private void selectSquare(int row, int column) {
        long whitePieces = board.getWhitePieces();
        long blackPieces = board.getBlackPieces();
        long playerPieces;

        if (board.isWhiteTurn()) {
            playerPieces = whitePieces;
            this.enemyPieces = blackPieces;

            if((playerPieces & bitboard[row][column]) != 0) {
                if(((playerPieces & board.getWhitePawns()) & bitboard[row][column]) != 0)
                    this.selectedPiece = 'p';
                else if(((playerPieces & board.getWhiteKnights()) & bitboard[row][column]) != 0)
                    this.selectedPiece = 'n';
                else if(((playerPieces & board.getWhiteBishops()) & bitboard[row][column]) != 0)
                    this.selectedPiece = 'b';
                else if(((playerPieces & board.getWhiteRooks()) & bitboard[row][column]) != 0)
                    this.selectedPiece = 'r';
                else if(((playerPieces & board.getWhiteQueens()) & bitboard[row][column]) != 0)
                    this.selectedPiece = 'q';
                else if(((playerPieces & board.getWhiteKing()) & bitboard[row][column]) != 0)
                    this.selectedPiece = 'k';
                
                this.selectedPiecePossibleMoves = MoveGenerator.calculatePossibleMoves(this.selectedPiece, this.board, row, column, 'w');
            }
            else if((this.selectedPiecePossibleMoves & bitboard[row][column]) != 0) {
                // Capture logic for White's turn
                if((bitboard[row][column] & enemyPieces) != 0) {
                    char capturedPieceType = '0', capturedColor = 'b';

                    if((board.getBlackPawns() & bitboard[row][column]) != 0) capturedPieceType = 'p';
                    else if((board.getBlackKnights() & bitboard[row][column]) != 0) capturedPieceType = 'n';
                    else if((board.getBlackBishops() & bitboard[row][column]) != 0) capturedPieceType = 'b';
                    else if((board.getBlackRooks() & bitboard[row][column]) != 0) capturedPieceType = 'r';
                    else if((board.getBlackQueens() & bitboard[row][column]) != 0) capturedPieceType = 'q';
                    else if((board.getBlackKing() & bitboard[row][column]) != 0) capturedPieceType = 'k';

                    if(capturedPieceType != '0')
                        removePiece(capturedPieceType, row, column, capturedColor);
                }

                // Moves selected piece to target tile that is a possible move for that selected piece
                movePiece(this.selectedPiece, selectedPieceRow, selectedPieceColumn, row, column, 'w');
                board.setWhiteTurn(false);
            }
        }
        else {
            playerPieces = blackPieces;
            this.enemyPieces = whitePieces;

            if((playerPieces & bitboard[row][column]) != 0) {
                if(((playerPieces & board.getBlackPawns()) & bitboard[row][column]) != 0)
                    this.selectedPiece = 'p';
                else if(((playerPieces & board.getBlackKnights()) & bitboard[row][column]) != 0)
                    this.selectedPiece = 'n';
                else if(((playerPieces & board.getBlackBishops()) & bitboard[row][column]) != 0)
                    this.selectedPiece = 'b';
                else if(((playerPieces & board.getBlackRooks()) & bitboard[row][column]) != 0)
                    this.selectedPiece = 'r';
                else if(((playerPieces & board.getBlackQueens()) & bitboard[row][column]) != 0)
                    this.selectedPiece = 'q';
                else if(((playerPieces & board.getBlackKing()) & bitboard[row][column]) != 0)
                    this.selectedPiece = 'k';
                
                this.selectedPiecePossibleMoves = MoveGenerator.calculatePossibleMoves(this.selectedPiece, this.board, row, column, 'b');
            }
            else if((this.selectedPiecePossibleMoves & bitboard[row][column]) != 0) {
                // Capture logic for Black's turn
                if((bitboard[row][column] & enemyPieces) != 0) {
                    char capturedPieceType = '0', capturedColor = 'w';

                    if((board.getWhitePawns() & bitboard[row][column]) != 0) capturedPieceType = 'p';
                    else if((board.getWhiteKnights() & bitboard[row][column]) != 0) capturedPieceType = 'n';
                    else if((board.getWhiteBishops() & bitboard[row][column]) != 0) capturedPieceType = 'b';
                    else if((board.getWhiteRooks() & bitboard[row][column]) != 0) capturedPieceType = 'r';
                    else if((board.getWhiteQueens() & bitboard[row][column]) != 0) capturedPieceType = 'q';
                    else if((board.getWhiteKing() & bitboard[row][column]) != 0) capturedPieceType = 'k';

                    if(capturedPieceType != '0')
                        removePiece(capturedPieceType, row, column, capturedColor);
                }
                
                // Moves selected piece to target tile that is a possible move for that selected piece
                movePiece(this.selectedPiece, selectedPieceRow, selectedPieceColumn, row, column, 'b');
                board.setWhiteTurn(true);
            }
        }

        this.selectedPieceRow = row;
        this.selectedPieceColumn = column;
    }

    public void movePiece(char pieceType, int rowFrom, int columnFrom, int rowTo, int columnTo, char color) {
        // Mask that has previous and next piece tile placement
        long moveMask = bitboard[rowFrom][columnFrom] | bitboard[rowTo][columnTo];

        if (color == 'w') {
            switch (pieceType) {
                case 'p' -> board.setWhitePawns(board.getWhitePawns() ^ moveMask);
                case 'n' -> board.setWhiteKnights(board.getWhiteKnights() ^ moveMask);
                case 'b' -> board.setWhiteBishops(board.getWhiteBishops() ^ moveMask);
                case 'r' -> board.setWhiteRooks(board.getWhiteRooks() ^ moveMask);
                case 'q' -> board.setWhiteQueens(board.getWhiteQueens() ^ moveMask);
                case 'k' -> board.setWhiteKing(board.getWhiteKing() ^ moveMask);
            }
        }
        else {
            switch (pieceType) {
                case 'p' -> board.setBlackPawns(board.getBlackPawns() ^ moveMask);
                case 'n' -> board.setBlackKnights(board.getBlackKnights() ^ moveMask);
                case 'b' -> board.setBlackBishops(board.getBlackBishops() ^ moveMask);
                case 'r' -> board.setBlackRooks(board.getBlackRooks() ^ moveMask);
                case 'q' -> board.setBlackQueens(board.getBlackQueens() ^ moveMask);
                case 'k' -> board.setBlackKing(board.getBlackKing() ^ moveMask);
            }
        }

    this.selectedPiecePossibleMoves = 0L;
    this.selectedPiece = '0';
    }

    public void removePiece(char pieceType, int row, int column, char color) {
        // Mask that has target tile
        long removeMask = bitboard[row][column];

        if (color == 'w') {
            switch (pieceType) {
                case 'p' -> board.setWhitePawns(board.getWhitePawns() ^ removeMask);
                case 'n' -> board.setWhiteKnights(board.getWhiteKnights() ^ removeMask);
                case 'b' -> board.setWhiteBishops(board.getWhiteBishops() ^ removeMask);
                case 'r' -> board.setWhiteRooks(board.getWhiteRooks() ^ removeMask);
                case 'q' -> board.setWhiteQueens(board.getWhiteQueens() ^ removeMask);
                case 'k' -> board.setWhiteKing(board.getWhiteKing() ^ removeMask);
            }
        }
        else {
            switch (pieceType) {
                case 'p' -> board.setBlackPawns(board.getBlackPawns() ^ removeMask);
                case 'n' -> board.setBlackKnights(board.getBlackKnights() ^ removeMask);
                case 'b' -> board.setBlackBishops(board.getBlackBishops() ^ removeMask);
                case 'r' -> board.setBlackRooks(board.getBlackRooks() ^ removeMask);
                case 'q' -> board.setBlackQueens(board.getBlackQueens() ^ removeMask);
                case 'k' -> board.setBlackKing(board.getBlackKing() ^ removeMask);
            }
        }
    }

    public char getSelectedPiece() {
        return selectedPiece;
    }

    public long getSelectedPiecePossibleMoves() {
        return selectedPiecePossibleMoves;
    }

    public long getEnemyPieces() {
        return enemyPieces;
    }
    
    public int getSelectedPieceRow() {
        return selectedPieceRow;
    }

    public int getSelectedPieceColumn() {
        return selectedPieceColumn;
    }
}
