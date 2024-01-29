public class King extends ConcretePiece {
    private final String m_kingUnicode = "♔";


    public King (Player p, String id){
        super.id=id;
        super.owner=p;
    }
    public String getSymbol() {
        return m_kingUnicode;
    }
    //public Player getOwner(){

    //}
    public String getType(){
        return this.m_kingUnicode;
    }





}