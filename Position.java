import java.util.ArrayList;

public class Position {
    private int x, y;
    private int steps=0;



    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public void set(Position p) {
        this.x=p.getX();
        this.y=p.getY();
    }

    public int getSteps() {
        return steps;
    }

    public void updateSteps(int newSteps) {
        this.steps=newSteps;
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

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }
}