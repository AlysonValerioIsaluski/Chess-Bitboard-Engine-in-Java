package chess;

import java.util.ArrayList;

public class GameLogic {
    private final Board board;
    private final int boardSize;
    private final long[][] bitboard;

    private char selectedPiece;
    private long selectedPiecePossibleMoves, enemyPieces;
    private int selectedPieceRow, selectedPieceColumn;

    private boolean inCheck;
    private boolean inCheckmate;
    private boolean inStalemate;
    private boolean insuficientMaterial;

    public GameLogic(Board board) {
        this.board = board;
        this.boardSize = board.getBoardSize();
        this.bitboard = board.getBitboard();

        this.selectedPiece = '0';
        this.inCheck = false;
        this.inCheckmate = false;
        this.inStalemate = false;
        this.insuficientMaterial = false;
    }

    public void handleTileSelection(int row, int column) {
        // Check if tile clicked is bounded inside the board
        if (row > 0 && row <= boardSize && column > 0 && column <= boardSize) {
            selectTile(row-1, column-1);
            // System.out.println("Tile selected: (" + (row-1) + ", " + (column-1) + ")");
        }
        else {
            this.selectedPiece = '0';
            this.selectedPiecePossibleMoves = 0L;
        }
    }

    private void selectTile(int row, int column) {
        long selectMask = bitboard[row][column];

        long whitePieces = board.getWhitePieces();
        long blackPieces = board.getBlackPieces();
        long playerPieces;
        
        // If it's white's turn
        if (board.getTurn() == 'w') {
            playerPieces = whitePieces;
            this.enemyPieces = blackPieces;
            
            // If player has selected a tile with one of their pieces
            if ((playerPieces & selectMask) != 0) {
                if (((playerPieces & board.getWhitePawns()) & selectMask) != 0)
                    this.selectedPiece = 'p';
                else if (((playerPieces & board.getWhiteKnights()) & selectMask) != 0)
                    this.selectedPiece = 'n';
                else if (((playerPieces & board.getWhiteBishops()) & selectMask) != 0)
                    this.selectedPiece = 'b';
                else if (((playerPieces & board.getWhiteRooks()) & selectMask) != 0)
                    this.selectedPiece = 'r';
                else if (((playerPieces & board.getWhiteQueens()) & selectMask) != 0)
                    this.selectedPiece = 'q';
                else if (((playerPieces & board.getWhiteKing()) & selectMask) != 0)
                    this.selectedPiece = 'k';
                
                this.selectedPiecePossibleMoves = MoveGenerator.calculatePossibleMoves(this.selectedPiece, this.board, row, column, 'w');
            }
            
            // If player has selected a tile which is a possible move for the selected piece 
            else if ((this.selectedPiecePossibleMoves & selectMask) != 0) {
                // If player has selected a tile with a piece capturable by the selected piece
                if ((selectMask & enemyPieces) != 0) {
                    char capturedPieceType = getPieceTypeFromTile(this.board, row, column);

                    if (capturedPieceType != '0')
                        removePiece(this.board, capturedPieceType, selectMask, 'b');
                }

                // Moves selected piece to target tile that is a possible move for that selected piece
                movePiece(this.selectedPiece, selectedPieceRow, selectedPieceColumn, row, column, 'w');
                
                board.setTurn('b');
            }
            
            // If player has selected anything else, the selection will just clear
            else {
                this.selectedPiece = '0';
                this.selectedPiecePossibleMoves = 0L;
            }
        }
        
        // If it's black's turn
        else {
            playerPieces = blackPieces;
            this.enemyPieces = whitePieces;
            
            // If player has selected a tile with one of their pieces
            if((playerPieces & selectMask) != 0) {
                if(((playerPieces & board.getBlackPawns()) & selectMask) != 0)
                    this.selectedPiece = 'p';
                else if(((playerPieces & board.getBlackKnights()) & selectMask) != 0)
                    this.selectedPiece = 'n';
                else if(((playerPieces & board.getBlackBishops()) & selectMask) != 0)
                    this.selectedPiece = 'b';
                else if(((playerPieces & board.getBlackRooks()) & selectMask) != 0)
                    this.selectedPiece = 'r';
                else if(((playerPieces & board.getBlackQueens()) & selectMask) != 0)
                    this.selectedPiece = 'q';
                else if(((playerPieces & board.getBlackKing()) & selectMask) != 0)
                    this.selectedPiece = 'k';
                
                this.selectedPiecePossibleMoves = MoveGenerator.calculatePossibleMoves(this.selectedPiece, this.board, row, column, 'b');
            }
            
            // If player has selected a tile which is a possible move for the selected piece 
            else if((this.selectedPiecePossibleMoves & selectMask) != 0) {
                // If player has selected a tile with a piece capturable by the selected piece
                if((selectMask & enemyPieces) != 0) {
                    char capturedPieceType = GameLogic.getPieceTypeFromTile(this.board, row, column);
                    
                    if(capturedPieceType != '0')
                        removePiece(this.board, capturedPieceType, selectMask, 'w');
                }

                // Moves selected piece to target tile that is a possible move for that selected piece
                movePiece(this.selectedPiece, selectedPieceRow, selectedPieceColumn, row, column, 'b');
                
                board.setTurn('w');
            }
            
            // If player has selected anything else, the selection will just clear
            else {
                this.selectedPiece = '0';
                this.selectedPiecePossibleMoves = 0L;
            }
        }
        
        // Game state updating
        this.selectedPieceRow = row;
        this.selectedPieceColumn = column;
        
        char turn = board.getTurn();

        this.inCheck = isInCheck(this.board, turn);
        this.inCheckmate = isInCheckmate(this.board, turn);
        this.inStalemate = isInStalemate(this.board, turn);
        this.insuficientMaterial = hasInsuficientMaterial(this.board, 'w') && hasInsuficientMaterial(this.board, 'b');
    }
    
    private static char getPieceTypeFromTile(Board board, int row, int column) {
        long selectMask = board.getBitboard()[row][column];

        // Check White Pieces
        if ((board.getWhitePawns() & selectMask) != 0) return 'p';
        if ((board.getWhiteKnights() & selectMask) != 0) return 'n';
        if ((board.getWhiteBishops() & selectMask) != 0) return 'b';
        if ((board.getWhiteRooks() & selectMask) != 0) return 'r';
        if ((board.getWhiteQueens() & selectMask) != 0) return 'q';
        if ((board.getWhiteKing() & selectMask) != 0) return 'k';

        // Check Black Pieces
        if ((board.getBlackPawns() & selectMask) != 0) return 'p';
        if ((board.getBlackKnights() & selectMask) != 0) return 'n';
        if ((board.getBlackBishops() & selectMask) != 0) return 'b';
        if ((board.getBlackRooks() & selectMask) != 0) return 'r';
        if ((board.getBlackQueens() & selectMask) != 0) return 'q';
        if ((board.getBlackKing() & selectMask) != 0) return 'k';

        // No piece found on this tile
        return '0';
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

    public static void removePiece(Board board, char pieceType, long removeMask, char color) {
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

    // If the move would put the king in check, it's illegal
    public static boolean isMoveIllegal(Board board, char color, char pieceType, int rowFrom, int columnFrom, int rowTo, int columnTo) {
        if(rowTo < 0 || rowTo >= board.getBoardSize() || columnTo < 0 || columnTo >= board.getBoardSize())
            return true;
        
        // Creates a copy of the board, since to check legality, the position of the board AFTER moving needs checking
        Board ghostBoard = new Board(board);
        ghostBoard.setTurn(color);

        long[][] bitboard = board.getBitboard();

        long moveMask = bitboard[rowFrom][columnFrom] | bitboard[rowTo][columnTo];

        // Removes hipothetically captured piece, so that it doesn't count for legality
        char enemyColor;
        if(color == 'w')
            enemyColor = 'b';
        else
            enemyColor = 'w';

        char capturedPieceType = getPieceTypeFromTile(board, rowTo, columnTo);
        removePiece(ghostBoard, capturedPieceType, bitboard[rowTo][columnTo], enemyColor);

        if (color == 'w') {
            switch (pieceType) {
                case 'p' -> ghostBoard.setWhitePawns(ghostBoard.getWhitePawns() ^ moveMask);
                case 'n' -> ghostBoard.setWhiteKnights(ghostBoard.getWhiteKnights() ^ moveMask);
                case 'b' -> ghostBoard.setWhiteBishops(ghostBoard.getWhiteBishops() ^ moveMask);
                case 'r' -> ghostBoard.setWhiteRooks(ghostBoard.getWhiteRooks() ^ moveMask);
                case 'q' -> ghostBoard.setWhiteQueens(ghostBoard.getWhiteQueens() ^ moveMask);
                case 'k' -> ghostBoard.setWhiteKing(ghostBoard.getWhiteKing() ^ moveMask);
            }
        }
        else {
            switch (pieceType) {
                case 'p' -> ghostBoard.setBlackPawns(ghostBoard.getBlackPawns() ^ moveMask);
                case 'n' -> ghostBoard.setBlackKnights(ghostBoard.getBlackKnights() ^ moveMask);
                case 'b' -> ghostBoard.setBlackBishops(ghostBoard.getBlackBishops() ^ moveMask);
                case 'r' -> ghostBoard.setBlackRooks(ghostBoard.getBlackRooks() ^ moveMask);
                case 'q' -> ghostBoard.setBlackQueens(ghostBoard.getBlackQueens() ^ moveMask);
                case 'k' -> ghostBoard.setBlackKing(ghostBoard.getBlackKing() ^ moveMask);
            }
        }

        return isInCheck(ghostBoard, color);
    }
    
    // If king can be attack by any enemy piece, the player's king is in check
    public static boolean isInCheck(Board board, char color) {
        if (color == 'w')
            return (board.getWhiteKing() & MoveGenerator.getAttackingTiles(board, 'b')) != 0;
        else
            return (board.getBlackKing() & MoveGenerator.getAttackingTiles(board, 'w')) != 0;
    }

    // Check if king of that color is in checkmate
    private static boolean isInCheckmate(Board board, char color) {
        // If the king is not in check, it definitely is not in checkmate
        if (!isInCheck(board, color))
            return false;

        // Checks if the player has any legal moves, if it doens't, its checkmate
        return !playerHasMoves(board, color);
    }

    // Check if king of that color is in stalemate
    private static boolean isInStalemate(Board board, char color) {
        // If the king is in check, it definitely is not in stalemate
        if (isInCheck(board, color))
            return false;

        // Checks if the player has any legal moves, if it doens't, its stalemate
        return !playerHasMoves(board, color);
    }

    // Check if player of that color has any legal moves
    private static boolean playerHasMoves(Board board, char color) {
        int row, column, boardSize = board.getBoardSize();
        long[][] bitboard = board.getBitboard();
        long movementTiles = 0L;
        
        if (color == 'w') {
            for (row = 0; row < boardSize; row++) {
                for (column = 0; column < boardSize; column++) {
                    if ((board.getWhitePawns() & bitboard[row][column]) != 0)
                        movementTiles = MoveGenerator.calculatePossibleMoves('p', board, row, column, color);
                    else if ((board.getWhiteKnights() & bitboard[row][column]) != 0)
                        movementTiles = MoveGenerator.calculatePossibleMoves('n', board, row, column, color);
                    else if ((board.getWhiteBishops() & bitboard[row][column]) != 0)
                        movementTiles = MoveGenerator.calculatePossibleMoves('b', board, row, column, color);
                    else if ((board.getWhiteRooks() & bitboard[row][column]) != 0)
                        movementTiles = MoveGenerator.calculatePossibleMoves('r', board, row, column, color);
                    else if ((board.getWhiteQueens() & bitboard[row][column]) != 0)
                        movementTiles = MoveGenerator.calculatePossibleMoves('q', board, row, column, color);
                    else if ((board.getWhiteKing() & bitboard[row][column]) != 0)
                        movementTiles = MoveGenerator.calculatePossibleMoves('k', board, row, column, color);

                    if (movementTiles != 0L)
                        return true;
                }
            }
        }
        else {
            for (row = 0; row < boardSize; row++) {
                for (column = 0; column < boardSize; column++) {
                    if ((board.getBlackPawns() & bitboard[row][column]) != 0)
                        movementTiles = MoveGenerator.calculatePossibleMoves('p', board, row, column, color);
                    else if ((board.getBlackKnights() & bitboard[row][column]) != 0)
                        movementTiles = MoveGenerator.calculatePossibleMoves('n', board, row, column, color);
                    else if ((board.getBlackBishops() & bitboard[row][column]) != 0)
                        movementTiles = MoveGenerator.calculatePossibleMoves('b', board, row, column, color);
                    else if ((board.getBlackRooks() & bitboard[row][column]) != 0)
                        movementTiles = MoveGenerator.calculatePossibleMoves('r', board, row, column, color);
                    else if ((board.getBlackQueens() & bitboard[row][column]) != 0)
                        movementTiles = MoveGenerator.calculatePossibleMoves('q', board, row, column, color);
                    else if ((board.getBlackKing() & bitboard[row][column]) != 0)
                        movementTiles = MoveGenerator.calculatePossibleMoves('k', board, row, column, color);

                    if (movementTiles != 0L)
                        return true;
                }
            }
        }

        return false;
    }

    // Checks if the player of that can ever checkmade, given a board position (eg. king and knight can't ever checkmate)
    private static boolean hasInsuficientMaterial(Board board, char color) {
        if (color == 'w') {
            // If white has pawn, rook or queen, a checkmate is possible
            if (board.getWhitePawns() != 0L || board.getWhiteRooks() != 0L || board.getWhiteQueens() != 0L)
                return false;

            ArrayList<ArrayList<Integer>> knightCoordinates = getPieceCoordinates(board, color, 'n');
            ArrayList<ArrayList<Integer>> bishopCoordinates = getPieceCoordinates(board, color, 'b');

            // Checks if white can realistic checkmate with knights and bishops (3+ knights, 2+ bishops, knight and bishop can)
            if (knightCoordinates.size() > 2 || bishopCoordinates.size() > 1 ||
                !(knightCoordinates.isEmpty() || bishopCoordinates.isEmpty()))
                return false;
        }
        else {
            // If black has pawn, rook or queen, a checkmate is possible
            if (board.getBlackPawns() != 0L || board.getBlackRooks() != 0L || board.getBlackQueens() != 0L)
                return false;

            ArrayList<ArrayList<Integer>> knightCoordinates = getPieceCoordinates(board, color, 'n');
            ArrayList<ArrayList<Integer>> bishopCoordinates = getPieceCoordinates(board, color, 'b');

            // Checks if black can realistic checkmate with knights and bishops (3+ knights, 2+ bishops, knight and bishop can)
            if (knightCoordinates.size() > 2 || bishopCoordinates.size() > 1 ||
                !(knightCoordinates.isEmpty() || bishopCoordinates.isEmpty()))
                return false;
        }

        return true;
    }

    // Gets piece's row and column for that color, for every instance of that piece
    public static ArrayList<ArrayList<Integer>> getPieceCoordinates(Board board, char color, char pieceType) {
        int boardSize = board.getBoardSize();
        long[][] bitboard = board.getBitboard();
        long piece;

        // List of Lists of integers, to represent each instance of that piece, and it's coordinates in 2D
        ArrayList<ArrayList<Integer>> pieceCoordinates = new ArrayList<>();

        if (color == 'w') {
            switch (pieceType) {
                case 'p' -> piece = board.getWhitePawns();
                case 'n' -> piece = board.getWhiteKnights();
                case 'b' -> piece = board.getWhiteBishops();
                case 'r' -> piece = board.getWhiteRooks();
                case 'q' -> piece = board.getWhiteQueens();
                case 'k' -> piece = board.getWhiteKing();
                default -> piece = 0L;
            }
        } else {
            switch (pieceType) {
                case 'p' -> piece = board.getBlackPawns();
                case 'n' -> piece = board.getBlackKnights();
                case 'b' -> piece = board.getBlackBishops();
                case 'r' -> piece = board.getBlackRooks();
                case 'q' -> piece = board.getBlackQueens();
                case 'k' -> piece = board.getBlackKing();
                default -> piece = 0L;
            }
        }
        
        for (int row = 0; row < boardSize; row++) {
            for (int column = 0; column < boardSize; column++) {
                if ((piece & bitboard[row][column]) != 0) {
                    ArrayList<Integer> coordinates = new ArrayList<>();
                    coordinates.add(row);
                    coordinates.add(column);
                    pieceCoordinates.add(coordinates);
                }
            }
        }
        
        return pieceCoordinates;
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

    public boolean getCheckStatus() {
        return inCheck;
    }

    public boolean getCheckmateStatus() {
        return inCheckmate;
    }

    public boolean getStalemateStatus() {
        return inStalemate;
    }

    public boolean getInsuficientMaterialStatus() {
        return insuficientMaterial;
    }
}
