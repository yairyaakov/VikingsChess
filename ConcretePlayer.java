public class ConcretePlayer implements Player {
    private Player player=null;
    private boolean m_isPlayerOne =true;
    private int m_winsCounter=0;

    public ConcretePlayer (boolean isPlayerOne){
        this.m_isPlayerOne= isPlayerOne;
    }

    /**
     * Get the number of wins achieved by the player in the game.
     *
     * @return The total number of wins by the player.
     */
    @Override
    public int getWins() {
        return this.m_winsCounter;
    }

    /**
     * @return true if the player is player 1, false otherwise.
     */
    @Override
    public boolean isPlayerOne() {
        return this.m_isPlayerOne;
    }

    public void addWin(){
        m_winsCounter++;
    }

    //Reset win counter
}
