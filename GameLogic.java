import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class GameLogic implements PlayableLogic {
    private boolean m_isFirstPlayerTurn = false;
    private final int BOARD_SIZE = 11;
    private final ConcretePlayer defender = new ConcretePlayer(true);
    private final ConcretePlayer attacker = new ConcretePlayer(false);
    protected final ConcretePiece[][] piecePosition = new ConcretePiece[BOARD_SIZE][BOARD_SIZE];
    Stack<ConcretePiece> killedPiece = new Stack<>();
    Stack<Position> lastP = new Stack<>();
    Stack<Position> newP = new Stack<>();
    final Position FIRST_KING_POSITION= new Position(5,5);
    private Position kingPosition = new Position(5,5);
    private ArrayList<ConcretePiece> printPiece = new ArrayList<ConcretePiece>();
    private int[][] howManySteps = new int[BOARD_SIZE][BOARD_SIZE];
    private ArrayList<Position> transfer = new ArrayList<Position>();


    //constructor
    public GameLogic() {
        initGame(piecePosition);
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

        if (!piecePosition[a.getX()][a.getY()].getType().equals("♔") && (b.getX()==0 && b.getY()==0 || b.getX()==10 && b.getY()==0 || b.getX()==0 && b.getY()==10 || b.getX()==10 && b.getY()==10)){
            return false;
        }//Pawn cannot be in the corner of the board

        if (piecePosition[a.getX()][a.getY()].getOwner().isPlayerOne() == isSecondPlayerTurn()) {
            return false;
        } //If it is currently the other player's turn, this is illegal

        if (!checkPath(a, b)) {
            return false;
        } //Check if there are pieces in path of progress

        if (a.isEqual(kingPosition)) {
            updatePositionKing(b);
        }
        calculateSquares(a,b); //calculate the distance that the piece has traveled and raise

        piecePosition[b.getX()][b.getY()] = piecePosition[a.getX()][a.getY()]; //Move action

        checkForPosition(b); //checks if this piece has already been at this point. If not, update the array "howManySteps"

        lastP.add(a);

        newP.add(b);

        piecePosition[b.getX()][b.getY()].setPositions("("+b.getX()+","+b.getY()+")"); //Add to ArrayList destination position

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
          kingPosition.set(FIRST_KING_POSITION);
            printStatistics (printPiece, defender);
            return true;
        }
        if(isKingKilled()){
            attacker.addWin();
            printStatistics (printPiece, attacker);
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
        initGame(piecePosition);      //רעיון לטסט: מחשבים כמה פעמים היה ריסט ומשוויםם לחישוב סכום מספר ניצחונות שני הפלייר
    }

    /**
     * Undo the last move made in the game, reverting the board state and turn order.
     */
    @Override
    public void undoLastMove() {
            if(!newP.isEmpty()) {
                while (!killedPiece.isEmpty() && !newP.isEmpty() && piecePosition[newP.peek().getX()][newP.peek().getY()] == null) {
                    //back from killing
                    ConcretePiece TempPiece = killedPiece.pop();
                    if (TempPiece instanceof Pawn) {
                        ((Pawn) TempPiece).reduce_Kill();
                    }
                    piecePosition[newP.peek().getX()][newP.peek().getY()] = killedPiece.pop(); //put the killed piece in his last position "Relive it"

                    newP.pop();
                }
                if (!lastP.isEmpty()  && !newP.isEmpty()){
                    //back to the last position
                    piecePosition[newP.peek().getX()][newP.peek().getY()].remuvelLoc("("+newP.peek().getX()+","+newP.peek().getY()+")");
                    piecePosition[newP.peek().getX()][newP.peek().getY()].removeLastLoc();
                    boolean flag=false;
                    for (int i=0; i<piecePosition[newP.peek().getX()][newP.peek().getY()].getPositions().size(); i++){
                        if (piecePosition[newP.peek().getX()][newP.peek().getY()].getPositions().get(i) == "("+newP.peek().getX()+","+newP.peek().getY()+")"){
                            flag=true;
                        }
                    }
                    if (!flag){
                        howManySteps[newP.peek().getX()][newP.peek().getY()]--;
                    }
                    piecePosition[lastP.peek().getX()][lastP.peek().getY()] = piecePosition[newP.peek().getX()][newP.peek().getY()]; //Return the player who has now moved to the previous location
                    lastP.pop();
                    piecePosition[newP.peek().getX()][newP.peek().getY()] = null; //The location will now be empty
                    newP.pop();
                    m_isFirstPlayerTurn = !m_isFirstPlayerTurn; //change the turn to the other player
                }

            }
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
    private ConcretePiece[][] initGame (ConcretePiece[][] arr) {
        printPiece = new ArrayList<ConcretePiece>();
        howManySteps = new int[BOARD_SIZE][BOARD_SIZE];
        killedPiece = new Stack<>();
        lastP = new Stack<>();
        newP = new Stack<>();

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                howManySteps[i][j]=0;
            }
        }

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                arr[i][j] = null;
            }
        }
        arr[5][5] = new King(defender,"K7");
        printPiece.add(arr[5][5]);
        arr[5][5].setPositions("(5,5)");
        howManySteps[5][5]=1;

        arr[5][3] = new Pawn(defender,"D1");
        printPiece.add(arr[5][3]);
        arr[5][3].setPositions("(5,3)");
        howManySteps[5][3]=1;

        arr[5][4] = new Pawn(defender,"D3");
        printPiece.add(arr[5][4]);
        arr[5][4].setPositions("(5,4)");
        howManySteps[5][4]=1;

        arr[5][6] = new Pawn(defender, "D11");
        printPiece.add(arr[5][6]);
        arr[5][6].setPositions("(5,6)");
        howManySteps[5][6]=1;

        arr[5][7] = new Pawn(defender,"D13");
        printPiece.add(arr[5][7]);
        arr[5][7].setPositions("(5,7)");
        howManySteps[5][7]=1;

        arr[3][5] = new Pawn(defender,"D5" );
        printPiece.add(arr[3][5]);
        arr[3][5].setPositions("(3,5)");
        howManySteps[3][5]=1;

        arr[4][5] = new Pawn(defender,"D6" );
        printPiece.add(arr[4][5]);
        arr[4][5].setPositions("(4,5)");
        howManySteps[4][5]=1;

        arr[6][5] = new Pawn(defender,"D8" );
        printPiece.add(arr[6][5]);
        arr[6][5].setPositions("(6,5)");
        howManySteps[6][5]=1;

        arr[7][5] = new Pawn(defender,"D9" );
        printPiece.add(arr[7][5]);
        arr[7][5].setPositions("(7,5)");
        howManySteps[7][5]=1;

        arr[4][4] = new Pawn(defender,"D2" );
        printPiece.add(arr[4][4]);
        arr[4][4].setPositions("(4,4)");
        howManySteps[4][4]=1;

        arr[6][4] = new Pawn(defender,"D4" );
        printPiece.add(arr[6][4]);
        arr[6][4].setPositions("(6,4)");
        howManySteps[6][4]=1;

        arr[6][6] = new Pawn(defender,"D12" );
        printPiece.add(arr[6][6]);
        arr[6][6].setPositions("(6,6)");
        howManySteps[6][6]=1;

        arr[4][6] = new Pawn(defender,"D10" );
        printPiece.add(arr[4][6]);
        arr[4][6].setPositions("(4,6)");
        howManySteps[4][6]=1;

        arr[0][3] = new Pawn(attacker,"A7");
        printPiece.add(arr[0][3]);
        arr[0][3].setPositions("(0,3)");
        howManySteps[0][3]=1;

        arr[0][4] = new Pawn(attacker,"A9" );
        printPiece.add(arr[0][4]);
        arr[0][4].setPositions("(0,4)");
        howManySteps[0][4]=1;

        arr[0][5] = new Pawn(attacker,"A11" );
        printPiece.add(arr[0][5]);
        arr[0][5].setPositions("(0,5)");
        howManySteps[0][5]=1;

        arr[0][6] = new Pawn(attacker,"A15" );
        printPiece.add(arr[0][6]);
        arr[0][6].setPositions("(0,6)");
        howManySteps[0][6]=1;

        arr[0][7] = new Pawn(attacker,"A17" );
        printPiece.add(arr[0][7]);
        arr[0][7].setPositions("(0,7)");
        howManySteps[0][7]=1;

        arr[1][5] = new Pawn(attacker,"A12" );
        printPiece.add(arr[1][5]);
        arr[1][5].setPositions("(1,5)");
        howManySteps[1][5]=1;

        arr[10][3] = new Pawn(attacker,"A8" );
        printPiece.add(arr[10][3]);
        arr[10][3].setPositions("(10,3)");
        howManySteps[10][3]=1;

        arr[10][4] = new Pawn(attacker,"A10" );
        printPiece.add(arr[10][4]);
        arr[10][4].setPositions("(10,4)");
        howManySteps[10][4]=1;

        arr[10][5] = new Pawn(attacker,"A14" );
        printPiece.add(arr[10][5]);
        arr[10][5].setPositions("(10,5)");
        howManySteps[10][5]=1;

        arr[10][6] = new Pawn(attacker,"A16");
        printPiece.add(arr[10][6]);
        arr[10][6].setPositions("(10,6)");
        howManySteps[10][6]=1;

        arr[10][7] = new Pawn(attacker,"A18" );
        printPiece.add(arr[10][7]);
        arr[10][7].setPositions("(10,7)");
        howManySteps[10][7]=1;

        arr[9][5] = new Pawn(attacker,"A13" );
        printPiece.add(arr[9][5]);
        arr[9][5].setPositions("(9,5)");
        howManySteps[9][5]=1;

        arr[3][0] = new Pawn(attacker,"A1" );
        printPiece.add(arr[3][0]);
        arr[3][0].setPositions("(3,0)");
        howManySteps[3][0]=1;

        arr[4][0] = new Pawn(attacker,"A2" );
        printPiece.add(arr[4][0]);
        arr[4][0].setPositions("(4,0)");
        howManySteps[4][0]=1;

        arr[5][0] = new Pawn(attacker,"A3" );
        printPiece.add(arr[5][0]);
        arr[5][0].setPositions("(5,0)");
        howManySteps[5][0]=1;

        arr[6][0] = new Pawn(attacker,"A4" );
        printPiece.add(arr[6][0]);
        arr[6][0].setPositions("(6,0)");
        howManySteps[6][0]=1;

        arr[7][0] = new Pawn(attacker,"A5" );
        printPiece.add(arr[7][0]);
        arr[7][0].setPositions("(7,0)");
        howManySteps[7][0]=1;

        arr[5][1] = new Pawn(attacker,"A6" );
        printPiece.add(arr[5][1]);
        arr[5][1].setPositions("(5,1)");
        howManySteps[5][1]=1;

        arr[3][10] = new Pawn(attacker,"A20" );
        printPiece.add(arr[3][10]);
        arr[3][10].setPositions("(3,10)");
        howManySteps[3][10]=1;

        arr[4][10] = new Pawn(attacker,"A21" );
        printPiece.add(arr[4][10]);
        arr[4][10].setPositions("(4,10)");
        howManySteps[4][10]=1;

        arr[5][10] = new Pawn(attacker,"A22" );
        printPiece.add(arr[5][10]);
        arr[5][10].setPositions("(5,10)");
        howManySteps[5][10]=1;

        arr[6][10] = new Pawn(attacker,"A23" );
        printPiece.add(arr[6][10]);
        arr[6][10].setPositions("(6,10)");
        howManySteps[6][10]=1;

        arr[7][10] = new Pawn(attacker,"A24" );
        printPiece.add(arr[7][10]);
        arr[7][10].setPositions("(7,10)");
        howManySteps[7][10]=1;

        arr[5][9] = new Pawn(attacker,"A19" );
        printPiece.add(arr[5][9]);
        arr[5][9].setPositions("(5,9)");
        howManySteps[5][9]=1;


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
                for (int i = a.getY() + 1; i <= b.getY(); i++) {
                    if (piecePosition[a.getX()][i] != null) {
                        return false;
                    }
                }
            }
            if (a.getY() > b.getY()) {
                for (int i = a.getY() - 1; i >= b.getY(); i--) {
                    if (piecePosition[a.getX()][i] != null) {
                        return false;
                    }
                }
            }
        }

        if (a.getY() == b.getY()) {
            if (a.getX() < b.getX()) {
                for (int i = a.getX() + 1; i <= b.getX(); i++) {
                    if (piecePosition[i][a.getY()] != null) {
                        return false;
                    }
                }
            }
            if (a.getX() > b.getX()) {
                for (int i = a.getX() - 1; i >= b.getX(); i--) {
                    if (piecePosition[i][a.getY()] != null) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void killPawn(int x, int y) {
        piecePosition[x][y] = null;
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
            ((Pawn)piecePosition[x][y]).add_Kill();

        }

        if (y != 10 && piecePosition[x][y + 1] != null && piecePosition[x][y + 1].getOwner().isPlayerOne() != isTheAttacker && !isKingPosition(x,y+1)) {
            if (y + 1 == 10) {
                killPawn(x, y + 1);
            } else if (piecePosition[x][y + 2] != null && piecePosition[x][y + 2].getOwner().isPlayerOne() == isTheAttacker) {
                killPawn(x, y + 1);
            }
            ((Pawn)piecePosition[x][y]).add_Kill();
        }

        if (x != 10 && piecePosition[x + 1][y] != null && piecePosition[x + 1][y].getOwner().isPlayerOne() != isTheAttacker && !isKingPosition(x+1,y)) {
            if (x + 1 == 10) {
                killPawn(x + 1, y);
            } else if (piecePosition[x + 2][y] != null && piecePosition[x + 2][y].getOwner().isPlayerOne() == isTheAttacker) {
                killPawn(x + 1, y);
            }
            ((Pawn)piecePosition[x][y]).add_Kill();
        }

        if (x != 0 && piecePosition[x - 1][y] != null && piecePosition[x - 1][y].getOwner().isPlayerOne() != isTheAttacker && !isKingPosition(x-1,y)) {
            if (x - 1 == 0) {
                killPawn(x - 1, y);
            } else if (piecePosition[x - 2][y] != null && piecePosition[x - 2][y].getOwner().isPlayerOne() == isTheAttacker) {
                killPawn(x - 1, y);
            }
            ((Pawn)piecePosition[x][y]).add_Kill();
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
                ((Pawn)piecePosition[0][2]).add_Kill();
            }
        } else if (dest.getX() == 2 && dest.getY() == 0) {
            if (piecePosition[1][0] != null && piecePosition[1][0].getOwner() != piecePosition[2][0].getOwner() && !isKingPosition(1,0)) {
                killPawn(1, 0);
                ((Pawn)piecePosition[2][0]).add_Kill();
            }
        } else if (dest.getX() == 10 && dest.getY() == 2) {
            if (piecePosition[10][1] != null && piecePosition[10][1].getOwner() != piecePosition[10][2].getOwner() && !isKingPosition(10,1)) {
                killPawn(10, 1);
                ((Pawn)piecePosition[10][2]).add_Kill();
            }
        } else if (dest.getX() == 8 && dest.getY() == 0) {
            if (piecePosition[9][0] != null && piecePosition[9][0].getOwner() != piecePosition[8][0].getOwner() && !isKingPosition(9,0)) {
                killPawn(9, 0);
                ((Pawn)piecePosition[8][0]).add_Kill();
            }
        } else if (dest.getX() == 10 && dest.getY() == 8) {
            if (piecePosition[10][9] != null && piecePosition[10][9].getOwner() != piecePosition[10][8].getOwner() && !isKingPosition(10,9)) {
                killPawn(10, 9);
                ((Pawn)piecePosition[10][8]).add_Kill();
            }
        } else if (dest.getX() == 8 && dest.getY() == 10) {
            if (piecePosition[9][10] != null && piecePosition[9][10].getOwner() != piecePosition[8][10].getOwner() && !isKingPosition(9,10)) {
                killPawn(9, 10);
                ((Pawn)piecePosition[8][10]).add_Kill();
            }
        } else if (dest.getX() == 0 && dest.getY() == 8) {
            if (piecePosition[0][9] != null && piecePosition[0][9].getOwner() != piecePosition[0][8].getOwner() && !isKingPosition(0,9)) {
                killPawn(0, 9);
                ((Pawn)piecePosition[0][8]).add_Kill();
            }
        } else if (dest.getX() == 2 && dest.getY() == 10) {
            if (piecePosition[1][10] != null && piecePosition[1][10].getOwner() != piecePosition[2][10].getOwner() && !isKingPosition(1,10)) {
                killPawn(1, 10);
                ((Pawn)piecePosition[2][10]).add_Kill();
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
    private void calculateSquares(Position a, Position b){
        if (a.getX()==b.getX()){
            piecePosition[a.getX()][a.getY()].setSquares(Math.abs(a.getY()-b.getY()));
        }
        piecePosition[a.getX()][a.getY()].setSquares(Math.abs(a.getX()-b.getX()));
    }

    /**
     * Checks if piece at this point has already been at this point. If not, update the array "howManySteps" at this point
     * @param dest the destination position at "move" function
     */
    private void checkForPosition(Position dest){
        if (WasThePieceHere(dest)){
            howManySteps[dest.getX()][dest.getY()]++;
        }
    }

    /**
     * A boolean function that help to decide piece at this point has already been at this point
     * @param dest the destination position at "move" function
     * @return true if this piece never been at this position. false if this piece has already been at this position
     */
    public boolean WasThePieceHere (Position dest) {
        boolean result = true;
        for (int i = 0; i < piecePosition[dest.getX()][dest.getY()].getSizePositions(); i++) {
            if (piecePosition[dest.getX()][dest.getY()].getPositions().get(i) == "("+dest.getX()+","+dest.getY()+")") {
                result = false;
            }
        }
        return result;
    }

    private void printStatistics (ArrayList<ConcretePiece> print, ConcretePlayer winner){
        String str;
        Collections.sort(print, new MyComparator("locations", winner));
        for (int i = 0; i<print.size(); i++){
            str=print.get(i).getId() + ": [";
            for (int j = 0; j < print.get(i).getSizePositions(); j++){
                if (str.charAt(str.length()-1)=='['){
                    str = str + print.get(i).positions.get(j);
                }
                else {
                    str = str + ", " + print.get(i).positions.get(j);
                }
            }
            str = str + "]";
            System.out.println(str);
        }
        System.out.println("***************************************************************************");
        Collections.sort(print, new MyComparator("killes", winner));
        for (int i = 0; i<print.size(); i++){
            if (print.get(i) instanceof Pawn){
                str = print.get(i).getId() + ": " + ((Pawn) print.get(i)).getKillCounter() + " kills";
                System.out.println(str);
            }
        }
        System.out.println("***************************************************************************");
        Collections.sort(print, new MyComparator("squares", winner));
        for (int i = 0; i<print.size(); i++){
            str = print.get(i).getId() + ": " + print.get(i).getSquares() + " squares";
            System.out.println(str);
        }
        System.out.println("***************************************************************************");
        for (int i=0; i<howManySteps.length; i++){
            for (int j=0; j<howManySteps.length; j++){
                Position temp = new Position(i,j);
                transfer.add(temp);
                int indexTransfer=transfer.indexOf(temp);
                if(howManySteps[i][j]>1){
                    transfer.get(indexTransfer).updateSteps(howManySteps[i][j]);
                }
                else {
                    transfer.remove(indexTransfer);
                }
            }
        }
        Collections.sort(transfer, new MyComparator("stepPieces", winner));
        for (int i=0; i<transfer.size(); i++){
            System.out.println(transfer.get(i).toString() + transfer.get(i).getSteps() + " pieces");
        }
    }

}
