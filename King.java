public class King extends ConcretePiece {
    //King symbol in game
    private final String m_kingUnicode = "♔";

//Define king by id and owner
    public King (Player p, String id){
        super.id=id;
        super.owner=p;
    }
    //return the symbol of the king
    public String getSymbol() {
        return m_kingUnicode;
    }
    //return this king symbol
    public String getType(){
        return this.m_kingUnicode;
    }





}