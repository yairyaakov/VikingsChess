public class GameLogic implements PlayableLogic {
    private boolean m_isFirstPlayerTurn = false;
    private final int BOARD_SIZE = 11;
    private final ConcretePlayer defender = new ConcretePlayer(true);
    private final ConcretePlayer attacker = new ConcretePlayer(false);
    protected final ConcretePiece[][] piecePosition = new ConcretePiece[BOARD_SIZE][BOARD_SIZE];
    private Position kingPosition = new Position(5,5);

    //constructor
    public GameLogic() {
        helpFunc(piecePosition);
    }

    /**
     * Attempt to move a piece from one position to another on the game board.
     *
     * @param a The starting position of the piece.
     * @param b The destination position for the piece.
     * @return true if the move is valid and successful, false otherwise.
     */
    @Override
    public boolean move(Position a, Position b) {
        if ((a.getX() != b.getX()) && (a.getY() != b.getY())) {
            return false;
        } //This is illegal to move diagonally

        if (piecePosition[a.getX()][a.getY()].getOwner().isPlayerOne() == isSecondPlayerTurn()) {
            return false;
        } //If it is currently the other player's turn, this is illegal

        if (!checkPath(a, b)) {
            return false;
        } //Check if there are pieces in path of progress
        //לבדוק שהפונקציה אופטימלית********

        if (a.isEqual(kingPosition)) {
            updatePositionKing(b);
        }

        piecePosition[b.getX()][b.getY()] = piecePosition[a.getX()][a.getY()]; //Move action

        piecePosition[a.getX()][a.getY()] = null; //After the piece moved, the last position the piece was in is empty

        if (!getPieceAtPosition(b).getType().equals("♔")) {
            isPossibleToKill(b); //If the one moving is not the king, check for possible kills
        }
        m_isFirstPlayerTurn = !m_isFirstPlayerTurn; //change the turn to the other player

        return true;
    }

    /**
     * Get the piece located at a given position on the game board.
     *
     * @param position The position for which to retrieve the piece.
     * @return The piece at the specified position, or null if no piece is present.
     */
    @Override
    public Piece getPieceAtPosition(Position position) {
        return piecePosition[position.getX()][position.getY()];
    }

    /**
     * Get the first player.
     *
     * @return The first player.
     */
    @Override
    public Player getFirstPlayer() {
        return defender;
    }

    /**
     * Get the second player.
     *
     * @return The second player.
     */
    @Override
    public Player getSecondPlayer() {
        return attacker;
    }

    /**
     * Check if the game has finished, indicating whether a player has won or if it's a draw.
     *
     * @return true if the game has finished, false otherwise.
     */
    @Override
    public boolean isGameFinished() {
        if ((kingPosition.getX()==0 && kingPosition.getY()==0) || (kingPosition.getX()==10 && kingPosition.getY()==0) ||
                (kingPosition.getX()==0 && kingPosition.getY()==10) ||(kingPosition.getX()==10 && kingPosition.getY()==10)) {
            defender.addWin();
            piecePosition[kingPosition.getX()][kingPosition.getY()]=null;
            return true;
        }
        if(isKingKilled()){
            attacker.addWin();
            return true;
        }
        return false;
    }

    /**
     * Check if it is currently the second player's turn.
     *
     * @return true if it's the second player's turn, false if it's the first player's turn.
     */
    @Override
    public boolean isSecondPlayerTurn() {
        return !m_isFirstPlayerTurn;
    }

    /**
     * Reset the game to its initial state, clearing the board and player information.
     */
    @Override
    public void reset() {
        m_isFirstPlayerTurn = false;
        helpFunc(piecePosition);      //רעיון לטסט: מחשבים כמה פעמים היה ריסט ומשוויםם לחישוב סכום מספר ניצחונות שני הפלייר
    }

    /**
     * Undo the last move made in the game, reverting the board state and turn order.
     */
    @Override
    public void undoLastMove() {
        //להחזיר תורות לאחור
        //maybe stack
    }

    /**
     * Get the size of the game board.
     *
     * @return The size of the game board, typically as the number of rows or columns.
     */
    @Override
    public int getBoardSize() {
        return BOARD_SIZE;
    }

    //This function help us build the starting constructor of the game
    private ConcretePiece[][] helpFunc(ConcretePiece[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                arr[i][j] = null;
            }
        }
        arr[5][5] = new King(defender);
        arr[5][3] = new Pawn(defender);
        arr[5][4] = new Pawn(defender);
        arr[5][6] = new Pawn(defender);
        arr[5][7] = new Pawn(defender);
        arr[3][5] = new Pawn(defender);
        arr[4][5] = new Pawn(defender);
        arr[6][5] = new Pawn(defender);
        arr[7][5] = new Pawn(defender);
        arr[4][4] = new Pawn(defender);
        arr[6][4] = new Pawn(defender);
        arr[6][6] = new Pawn(defender);
        arr[4][6] = new Pawn(defender);
        arr[0][3] = new Pawn(attacker);
        arr[0][4] = new Pawn(attacker);
        arr[0][5] = new Pawn(attacker);
        arr[0][6] = new Pawn(attacker);
        arr[0][7] = new Pawn(attacker);
        arr[1][5] = new Pawn(attacker);
        arr[10][3] = new Pawn(attacker);
        arr[10][4] = new Pawn(attacker);
        arr[10][5] = new Pawn(attacker);
        arr[10][6] = new Pawn(attacker);
        arr[10][7] = new Pawn(attacker);
        arr[9][5] = new Pawn(attacker);
        arr[3][0] = new Pawn(attacker);
        arr[4][0] = new Pawn(attacker);
        arr[5][0] = new Pawn(attacker);
        arr[6][0] = new Pawn(attacker);
        arr[7][0] = new Pawn(attacker);
        arr[5][1] = new Pawn(attacker);
        arr[3][10] = new Pawn(attacker);
        arr[4][10] = new Pawn(attacker);
        arr[5][10] = new Pawn(attacker);
        arr[6][10] = new Pawn(attacker);
        arr[7][10] = new Pawn(attacker);
        arr[5][9] = new Pawn(attacker);

        return arr;
    }


    /**
     * This is a function that help find if there are pieces in path of progress
     *
     * @param a The source piece position
     * @param b The destination piece position
     * @return
     */
    private boolean checkPath(Position a, Position b) {
        if (a.getX() == b.getX()) {
            if (a.getY() < b.getY()) {
                for (int i = a.getY() + 1; i < b.getY(); i++) {
                    if (piecePosition[a.getX()][i] != null) {
                        return false;
                    }
                }
            }
            if (a.getY() > b.getY()) {
                for (int i = a.getY() - 1; i > b.getY(); i--) {
                    if (piecePosition[a.getX()][i] != null) {
                        return false;
                    }
                }
            }
        }

        if (a.getY() == b.getY()) {
            if (a.getX() < b.getX()) {
                for (int i = a.getX() + 1; i < b.getX(); i++) {
                    if (piecePosition[i][a.getY()] != null) {
                        return false;
                    }
                }
            }
            if (a.getX() > b.getX()) {
                for (int i = a.getX() - 1; i > b.getX(); i--) {
                    if (piecePosition[i][a.getY()] != null) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void killPawn(int x, int y) {
        System.out.print("problem");
        piecePosition[x][y] = null;
        ;
    }


    /**
     * This function checks if the position one of "unique" point (two positions near the corner)
     * and if the test is positive she sends the position to another check.
     * In addition, the function checks 4 direction of the position to check if they are pawn to kill
     *
     * @param destination The current position
     */
    private void isPossibleToKill(Position destination) {

        if (destination.isUniquePosition()) {
            checkForUniquePosition(destination);
        }

        int x = destination.getX();
        int y = destination.getY();
        boolean isTheAttacker = piecePosition[x][y].getOwner().isPlayerOne();

        if (y != 0 && piecePosition[x][y - 1] != null && piecePosition[x][y - 1].getOwner().isPlayerOne() != isTheAttacker && !isKingPosition(x,y-1)) {
            if (y - 1 == 0) {
                killPawn(x, 0);
            } else if (piecePosition[x][y - 2] != null && piecePosition[x][y - 2].getOwner().isPlayerOne() == isTheAttacker) {
                killPawn(x, y - 1);
            }
        }

        if (y != 10 && piecePosition[x][y + 1] != null && piecePosition[x][y + 1].getOwner().isPlayerOne() != isTheAttacker && !isKingPosition(x,y+1)) {
            if (y + 1 == 10) {
                killPawn(x, y + 1);
            } else if (piecePosition[x][y + 2] != null && piecePosition[x][y + 2].getOwner().isPlayerOne() == isTheAttacker) {
                killPawn(x, y + 1);
            }
        }

        if (x != 10 && piecePosition[x + 1][y] != null && piecePosition[x + 1][y].getOwner().isPlayerOne() != isTheAttacker && !isKingPosition(x+1,y)) {
            if (x + 1 == 10) {
                killPawn(x + 1, y);
            } else if (piecePosition[x + 2][y] != null && piecePosition[x + 2][y].getOwner().isPlayerOne() == isTheAttacker) {
                killPawn(x + 1, y);
            }
        }

        if (x != 0 && piecePosition[x - 1][y] != null && piecePosition[x - 1][y].getOwner().isPlayerOne() != isTheAttacker && !isKingPosition(x-1,y)) {
            if (x - 1 == 0) {
                killPawn(x - 1, y);
            } else if (piecePosition[x - 2][y] != null && piecePosition[x - 2][y].getOwner().isPlayerOne() == isTheAttacker) {
                killPawn(x - 1, y);
            }
        }
    }

    /**
     * This function checks if the current position is near to pawn that exist at the point next to the corner.
     * If the test is positive the function sends it to the kill function
     *
     * @param dest The current position
     */

    private void checkForUniquePosition(Position dest) {
        if (dest.getX() == 0 && dest.getY() == 2) {
            if (piecePosition[0][1] != null && piecePosition[0][1].getOwner() != piecePosition[0][2].getOwner() && !isKingPosition(0,1)) {
                killPawn(0, 1);
            }
        } else if (dest.getX() == 2 && dest.getY() == 0) {
            if (piecePosition[1][0] != null && piecePosition[1][0].getOwner() != piecePosition[2][0].getOwner() && !isKingPosition(1,0)) {
                killPawn(1, 0);
            }
        } else if (dest.getX() == 10 && dest.getY() == 2) {
            if (piecePosition[10][1] != null && piecePosition[10][1].getOwner() != piecePosition[10][2].getOwner() && !isKingPosition(10,1)) {
                killPawn(10, 1);
            }
        } else if (dest.getX() == 8 && dest.getY() == 0) {
            if (piecePosition[9][0] != null && piecePosition[9][0].getOwner() != piecePosition[8][0].getOwner() && !isKingPosition(9,0)) {
                killPawn(9, 0);
            }
        } else if (dest.getX() == 10 && dest.getY() == 8) {
            if (piecePosition[10][9] != null && piecePosition[10][9].getOwner() != piecePosition[10][8].getOwner() && !isKingPosition(10,9)) {
                killPawn(10, 9);
            }
        } else if (dest.getX() == 8 && dest.getY() == 10) {
            if (piecePosition[9][10] != null && piecePosition[9][10].getOwner() != piecePosition[8][10].getOwner() && !isKingPosition(9,10)) {
                killPawn(9, 10);
            }
        } else if (dest.getX() == 0 && dest.getY() == 8) {
            if (piecePosition[0][9] != null && piecePosition[0][9].getOwner() != piecePosition[0][8].getOwner() && !isKingPosition(0,9)) {
                killPawn(0, 9);
            }
        } else if (dest.getX() == 2 && dest.getY() == 10) {
            if (piecePosition[1][10] != null && piecePosition[1][10].getOwner() != piecePosition[2][10].getOwner() && !isKingPosition(1,10)) {
                killPawn(1, 10);
            }
        }
    }
    private void updatePositionKing(Position p){
        kingPosition = new Position(p.getX(),p.getY());
    }

    private boolean isKingPosition (int xPosition, int yPosition){
        return kingPosition.getX() == xPosition && kingPosition.getY() == yPosition;
    }


    /**
     * This function checks if the king can be killed.
     * If the king can, the king position will be null and send true.
     * @return
     */
    private boolean isKingKilled() {
        int xK = kingPosition.getX();
        int yK = kingPosition.getY();

        for (int i = 2; i <= 8; i++) {
            if (xK == i && yK == 0) {
                if (piecePosition[xK + 1][yK] != null && piecePosition[xK + 1][yK].getOwner() == attacker &&
                        piecePosition[xK - 1][yK] != null && piecePosition[xK - 1][yK].getOwner() == attacker &&
                        piecePosition[xK][yK + 1] != null && piecePosition[xK][yK + 1].getOwner() == attacker) {
                    piecePosition[xK][yK] = null;
                    return true;
                }
                return false;
            }
            if (xK == i && yK == 10) {
                if (piecePosition[xK + 1][yK] != null && piecePosition[xK + 1][yK].getOwner() == attacker &&
                        piecePosition[xK - 1][yK] != null && piecePosition[xK - 1][yK].getOwner() == attacker &&
                        piecePosition[xK][yK - 1] != null && piecePosition[xK][yK - 1].getOwner() == attacker) {
                    piecePosition[xK][yK] = null;
                    return true;
                }
                return false;
            }
            if (xK == 0 && yK == i) {
                if (piecePosition[xK + 1][yK] != null && piecePosition[xK + 1][yK].getOwner() == attacker &&
                        piecePosition[xK][yK - 1] != null && piecePosition[xK][yK - 1].getOwner() == attacker &&
                        piecePosition[xK][yK + 1] != null && piecePosition[xK][yK + 1].getOwner() == attacker) {
                    piecePosition[xK][yK] = null;
                    return true;
                }
                return false;
            }
            if (xK == 10 && yK == i) {
                if (piecePosition[xK - 1][yK] != null && piecePosition[xK - 1][yK].getOwner() == attacker &&
                        piecePosition[xK][yK - 1] != null && piecePosition[xK][yK - 1].getOwner() == attacker &&
                        piecePosition[xK][yK + 1] != null && piecePosition[xK][yK + 1].getOwner() == attacker) {
                    piecePosition[xK][yK] = null;
                    return true;
                }
                return false;
            }
        }

        if (piecePosition[xK + 1][yK] != null && piecePosition[xK + 1][yK].getOwner() == attacker &&
                piecePosition[xK - 1][yK] != null && piecePosition[xK - 1][yK].getOwner() == attacker &&
                piecePosition[xK][yK + 1] != null && piecePosition[xK][yK + 1].getOwner() == attacker &&
                piecePosition[xK][yK - 1] != null && piecePosition[xK][yK - 1].getOwner() == attacker) {
            piecePosition[xK][yK] = null;
            return true;
        }
        return false;
    }

}


