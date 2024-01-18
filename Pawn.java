public class Pawn extends ConcretePiece {
    private final String m_pawnUnicode = "♙";

    public Pawn(Player p) {
        super.owner = p;
    }

    //public Player getOwner() {

    //}

    public String getType() {
        return this.m_pawnUnicode;
    }
}