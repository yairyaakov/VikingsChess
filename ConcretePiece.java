import javax.swing.text.Position;
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
    public String getType() {
        return this.pieceType;
    }
    public ArrayList<String> getPositions() {
        return positions;
    }
    public int getSizePositions (){
        return this.positions.size();
    }
    public int getSquares() {
        return squares;
    }
    public String getId() {
        return id;
    }
    public void setPositions (String s){
        positions.add(s);
    }
    public void setSquares(int dis) {
        this.squares+=dis;
    }

    public void restart_Squares(){
        this.squares = 0;
    }
    public void restart_Positions(){
        this.positions = new ArrayList<String>();
    }


    public void remuvelLoc (String s) {
        this.positions.remove(s);
    }
    public void removeLastLoc()
    {
        this.positions.remove(this.positions.size()-1);
    }

}