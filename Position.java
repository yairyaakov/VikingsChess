import java.util.ArrayList;

public class Position {
    private int x, y;
    private int steps=0;


//Define Poisition by int x and int y
    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }
    //This is a function that return us the x of the position
    public int getX() {
        return x;
    }
    //This is a function that return us the y of the position
    public int getY() {
        return y;
    }
    //This is a function that set position
    public void set(Position p) {
        this.x=p.getX();
        this.y=p.getY();
    }

    //This is a function that return the steps on that position
    public int getSteps() {
        return steps;
    }
    //This is a function that update the steps of this position
    public void updateSteps(int newSteps) {
        this.steps=newSteps;
    }

    /**
     * this function checks if the current position two positions near one of 4 corners
     * @return
     */
    //This is a function that define 8 unique positions on the board as unique position
    public boolean isUniquePosition(){
        if((this.x==0 && this.y==2) || (this.x==2 && this.y==0) ||
                (this.x==8 && this.y==0) || (this.x==10 && this.y==2) ||
                (this.x==10 && this.y==8 )|| (this.x==8 && this.y==10) ||
                (this.x==0 && this.y==8) || (this.x==2 && this.y==10)){
            return true;
        }
        return false;
    }
    //This is a function that check if the position is equal
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