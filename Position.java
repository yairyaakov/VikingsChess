public class Position {
    private int x, y;
    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }
    // Getter
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    /**
     * this function checks if the current position two positions near one of 4 corners
     * @return
     */
    public boolean isUniquePosition(){
        if((this.x==0 && this.y==2) || (this.x==2 && this.y==0) ||
                (this.x==8 && this.y==0) || (this.x==10 && this.y==2) ||
                (this.x==10 && this.y==8 )|| (this.x==8 && this.y==10) ||
                (this.x==0 && this.y==8) || (this.x==2 && this.y==10)){
            return true;
        }
        return false;
    }

    public boolean isEqual(Position p){
        if (this.x == p.getX() && this.y == p.getY()){
            return true;
        }
        return false;
    }


}
