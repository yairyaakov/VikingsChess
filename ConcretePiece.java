import javax.swing.text.Position;

public abstract class ConcretePiece implements Piece {

    protected Player owner;
    protected String pieceType;


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
}
