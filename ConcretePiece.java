import javax.swing.text.Position;
import java.util.ArrayList;
public abstract class ConcretePiece implements Piece {

    protected Player owner;
    protected String pieceType;
    String id;
    protected ArrayList<String> positions = new ArrayList<String>();
    private int  squares = 0;


    public ConcretePiece(){
    }

    public ArrayList<String> getPositions() {
        return positions;
    }

    public void setPositions (String s){
        positions.add(s);
    }
    public int getSizePositions (){
        return this.positions.size();
    }

    public void remuvelLoc (String s) {
        this.positions.remove(s);
    }
    public void removeLastLoc()
    {
        this.positions.remove(this.positions.size()-1);
    }

    public String getId() {
        return id;
    }

    public int getSquares() {
        return squares;
    }

    public void setSquares(int dis) {
        this.squares+=dis;
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
}