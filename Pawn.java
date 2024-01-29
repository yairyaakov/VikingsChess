public class Pawn extends ConcretePiece {
    private final String m_pawnUnicode = "♙";
    protected int killCounter=0;

    public Pawn(Player p, String id) {
        super.owner = p;
        super.id=id;

    }

    public int getKillCounter() {
        return killCounter;
    }

    public void add_Kill() {
        this.killCounter++;
    }
    public void reduce_Kill() {
        this.killCounter--;
    }

    public String getType() {
        return this.m_pawnUnicode;
    }
}