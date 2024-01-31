import java.util.ArrayList;
public abstract class ConcretePiece implements Piece {

    protected Player owner;
    private String pieceType;
    protected String id;
    private ArrayList<String> positions = new ArrayList<String>();
    private int  squares = 0;


    public ConcretePiece(){
    }

    /**
     * Get the player who owns the piece.
     *
     * @return The player who is the owner of this game piece.
     */
    @Override
    //This is a function that return the owner of the piece
    public Player getOwner() {
        return owner;
    }

    /**
     * Get a Unicode character representing the type of the game piece.
     * <a href="https://en.wikipedia.org/wiki/Chess_symbols_in_Unicode">...</a>
     *
     * @return A Unicode character representing the type of this game piece
     * (e.g., ♟ for pawn, ♞ for knight, ♜ for rook, etc.).
     */
    @Override
    //This is a function that return the type of the piece
    public String getType() {
        return this.pieceType;
    }
    // return all the positions the piece have been through
    public ArrayList<String> getPositions() {
        return positions;
    }
    //return the distance the piece made in squares
    public int getSquares() {
        return squares;
    }
    //return the id of the piece
    public String getId() {
        return id;
    }
    //add this position to the positions he made
    public void setPositions (String s){
        positions.add(s);
    }
    //update the squares count
    public void setSquares(int dis) {
        this.squares+=dis;
    }
    //reset the squares counter
    public void restart_Squares(){
        this.squares = 0;
    }
    //reset the positions
    public void restart_Positions(){
        this.positions = new ArrayList<String>();
    }

    //remove this position (in case of undo last move)
    public void removelLoc(String s) {
        this.positions.remove(s);
    }
    //reduce the arraylist by one
    public void removeLastLoc()
    {
        this.positions.remove(this.positions.size()-1);
    }

}