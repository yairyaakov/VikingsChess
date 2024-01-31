public class Pawn extends ConcretePiece {
    //Define the symbol of pawn
    private final String m_pawnUnicode = "♙";
    //Define a kill counter
    private int killCounter=0;
    //Define king by id and owner
    public Pawn(Player p, String id) {
        super.owner = p;
        super.id=id;

    }
    //This is a function that return how many pieces the pawn killed
    public int getKillCounter() {
        return killCounter;
    }
    //This is a function that restart the kill counter
    public void restart_Kill() {
        this.killCounter=0;
    }
    //This is a function that add 1 to the kill counter
    public void add_Kill() {
        this.killCounter++;
    }
    //This is a function that reduce by 1 the kill counter(if you undo your last move for example)
    public void reduce_Kill() {
        this.killCounter--;
    }

    public String getType() {
        return this.m_pawnUnicode;
    }
}