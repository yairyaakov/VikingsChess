public class MyComparator implements java.util.Comparator<Object> {
    String sortByKind;
    ConcretePlayer winner;

    public MyComparator (String sortByKind, ConcretePlayer winner){
        this.sortByKind=sortByKind;
        this.winner=winner;
    }
    public int compare(Object object1, Object object2){
        if (sortByKind.equals("locations") || sortByKind.equals("killes") || sortByKind.equals("squares")){
            ConcretePiece piece1 = null;
            ConcretePiece piece2 = null;

            if (object1 instanceof ConcretePiece && object2 instanceof ConcretePiece){
                piece1 = (ConcretePiece) object1;
                piece2 = (ConcretePiece) object2;

                if (sortByKind.equals("locations")){
                    if (piece1.getOwner() == piece2.getOwner()){
                        if (piece1.getPositions().size() == piece2.getPositions().size()){
                            return Integer.parseInt(piece1.id.substring(1)) - Integer.parseInt(piece2.id.substring(1));
                        }
                        return piece1.getPositions().size() - piece2.getPositions().size();
                    }
                    if((piece1.getOwner().isPlayerOne() && winner.isPlayerOne()) || (!piece1.getOwner().isPlayerOne() && !winner.isPlayerOne())){
                        return -1;
                    }
                    else {
                        return 1;
                    }
                }

                if (sortByKind.equals("killes") && piece1 instanceof Pawn && piece2 instanceof Pawn){
                    if (((Pawn) piece1).getKillCounter() == ((Pawn) piece2).getKillCounter()){
                        if (Integer.parseInt(piece1.id.substring(1)) == Integer.parseInt(piece2.id.substring(1))){
                            if((piece1.getOwner().isPlayerOne() && winner.isPlayerOne()) || (!piece1.getOwner().isPlayerOne() && !winner.isPlayerOne())){
                                return -1;
                            }
                            else {
                                return 1;
                            }
                        }
                        return Integer.parseInt(piece1.id.substring(1)) - Integer.parseInt(piece2.id.substring(1));
                    }
                    return ((Pawn) piece2).getKillCounter() - ((Pawn) piece1).getKillCounter();

                }
                if (sortByKind.equals("squares")){
                    if (piece1.getSquares() == piece2.getSquares()){
                        if (Integer.parseInt(piece1.id.substring(1)) == Integer.parseInt(piece2.id.substring(1))){
                            if((piece1.getOwner().isPlayerOne() && winner.isPlayerOne()) || (!piece1.getOwner().isPlayerOne() && !winner.isPlayerOne())){
                                return -1;
                            }
                            else {
                                return 1;
                            }
                        }
                        return Integer.parseInt(piece1.id.substring(1)) - Integer.parseInt(piece2.id.substring(1));
                    }
                    return piece2.getSquares() - piece1.getSquares();
                }
            }
        }
        if (sortByKind.equals("stepPieces")){
            Position position1 = (Position) object1;
            Position position2 = (Position) object2;;

            if (position1.getSteps() == position2.getSteps()){
                if (position1.getX() == position2.getX()){
                    return position1.getY()-position2.getY();
                }
                return position1.getX()-position2.getX();
            }
            return position1.getSteps()-position2.getSteps();
        }

        return 0;   //Default case if everything is equal
    }
}
