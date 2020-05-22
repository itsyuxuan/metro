package comp1110.ass2;

public class Computer implements PlayerInt {
    private int playerNumber;
    private int[] stationsOwned;

    public Computer(int playerNumber, int[] stationsOwned){
        this.playerNumber = playerNumber;
        this.stationsOwned = stationsOwned;
    }
    public String compMeth(String placementSequence, String totalHands){

        String sr = Metro.drawFromDeck(placementSequence,totalHands);
        //System.out.println(sr);
        System.out.println(Metro.generateMove("",sr,6));
        placementSequence = Metro.generateMove("",sr,6);
        return placementSequence;
    }
}
