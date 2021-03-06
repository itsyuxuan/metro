package comp1110.ass2;

/**
 * This class reperesents a Manual Player it implements the PlayerInt interface.
 * Instance of this class, creates a Manual player instance. Jiawei suggested to delete this class because it is not actullay useful, but
 * this class is not deleted becasue Ganaraj thinks this is his best code and after negotiation with team, this class is remained.
 *
 * @author: Ganaraj Rao
 */


public class ManualPlayer implements PlayerInt {
    private int playerCount;

    public ManualPlayer(int playerNumber, int[] stationsOwned, int playerCount) {
        this.playerCount = playerCount;
    }

    /**
     * This method describes the play method for a manual player.
     * It draws a tile and places on the board using the drawfromDeck() and
     * generateMove() in Metro class. The functionality is incorrect/incomplete as it shows automation of manual move
     * However, when integrated with Viewer class, its functionality will represent based on checks and token.
     *
     * @params: placementSequence: String describing the current placement of tiles on the board
     * totalHands: String representing all the tiles in hands oll the players.
     * @returns: placementSequence: adds the move performed to the input placementSequence String and returns the
     * updated placement sequence
     * @author: Ganaraj Rao
     */

    public String methodPlay(String placementSequence, String totalHands) {
        String sr = Metro.drawFromDeck(placementSequence, totalHands);
        System.out.println(sr);
        if (Metro.isPlacementSequenceValid(placementSequence + Metro.generateMove(placementSequence, sr, playerCount))) {
            String move = Metro.generateMove(placementSequence, sr, playerCount);
            placementSequence = placementSequence + move;
        }
        return placementSequence;
    }

}
