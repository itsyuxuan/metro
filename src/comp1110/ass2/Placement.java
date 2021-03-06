package comp1110.ass2;


import java.util.ArrayList;
import java.util.Map;

/**
 * This class deals with all the things related to the placement and it contains many
 * useful method for the placement.
 *
 * @author Jiawei Fan
 */
public class Placement {
    /* This class is intended to handle the methods and checks related to the placing of tiles*/
    public static final int LENGTH_OF_ONE_PlACEMENT = 6;
    public static final int LENGTH_OF_TILE_TYPE = 4;

    /**
     * This method is to determine if the placement has a length of
     * multiple of 6 or not.
     *
     * @param placement a string representing the placement
     * @return {@code true} if the given String Placement has a
     * length of multiple 6, {@code false} otherwise
     * @author Jiawei Fan
     */
    public static boolean isPlacementLengthValid(String placement) {
        return placement.length() % 6 == 0;
    }

    /**
     * This method is to determine if the length of the placement is
     * valid and if each piece in the placement is well formed or not.
     *
     * @param placement a string representing the placement
     * @return {@code true} if the each piecePlacement is well formed
     * {@code false} otherwise
     * @author Jiawei Fan
     */
    public static boolean isPlacementWellFormed(String placement) {
        if (isPlacementLengthValid(placement)) {
            for (int i = 0; i < placement.length() / LENGTH_OF_ONE_PlACEMENT; i++) {
                String testPiece = placement.substring(LENGTH_OF_ONE_PlACEMENT * i, LENGTH_OF_ONE_PlACEMENT * i + LENGTH_OF_ONE_PlACEMENT);
                if (!Metro.isPiecePlacementWellFormed(testPiece))
                //determine if each piece placement in the placement is valid or not
                {
                    return false;
                }
            }
            return true;//if each piece pass the test, then each piece is well formed
        } else {
            return false;
        }
    }

    /**
     * This method is to check if the placement is valid or not regarding
     * the pieces in it. For any piece x in the placement, there can be
     * no more piece in the board than in the deck.
     *
     * @param placement a string representing the placement
     * @return {@code true} if For any piece x, there can exist no more
     * instances of x on the board than instances of x in the deck.
     * {@code false} otherwise
     * @author Jiawei Fan
     */
    public static boolean noMoreInstance(String placement) {
        ArrayList<String> initialDeck = Deck.getInitialDeck();
        int numberOfPiece = placement.length() / LENGTH_OF_ONE_PlACEMENT;
        for (int i = 0; i < numberOfPiece; i++) {//test each piece to ensure that there are no more instances of one piece on the board than in the deck
            String testPiece = placement.substring(LENGTH_OF_ONE_PlACEMENT * i, LENGTH_OF_ONE_PlACEMENT * i + LENGTH_OF_TILE_TYPE);
            if (initialDeck.indexOf(testPiece) < 0) {// if there is no this piece in the initial deck return false
                return false;
            } else {// once test one piece in the placement,remove this piece from the deck
                initialDeck.remove(testPiece);
            }
        }
        return true;
    }

    /**
     * This method will determine if two tiles are next to each other or not
     * each tile is represented by a string which has length of 6. The first
     * four characters represent its type and the last two character represent
     * its row and column.
     *
     * @param tile1 a string of length 6 representing the placement of a tile
     * @param tile2 a string of length 6 representing the placement of a tile
     * @return return true if two tiles are neighbours of each other
     * otherwise,return false
     * @author Jiawei Fan
     */
    public static boolean isNeighbour(String tile1, String tile2) {
        int row1 = Integer.parseInt(tile1.substring(LENGTH_OF_TILE_TYPE, LENGTH_OF_TILE_TYPE + 1));
        int col1 = Integer.parseInt(tile1.substring(LENGTH_OF_TILE_TYPE + 1, LENGTH_OF_TILE_TYPE + 2));
        int row2 = Integer.parseInt(tile2.substring(LENGTH_OF_TILE_TYPE, LENGTH_OF_TILE_TYPE + 1));
        int col2 = Integer.parseInt(tile2.substring(LENGTH_OF_TILE_TYPE + 1, LENGTH_OF_TILE_TYPE + 2));
        if (row1 == row2 && col1 == col2) {
            //first test if their coordinates are the same, if they overlap, return is false
            return false;
        }
        if (row1 == row2 || col1 == col2) {
            //when row or col of them are equal
            //And distance between them is exactluy one
            return Math.abs(row1 - row2) == 1 || Math.abs(col1 - col2) == 1;
        }
        return false;
    }

    /**
     * This method is in the condition that two tiles are neighbors of each other. It will be
     * called after isNeighbour method returns true later in another main.
     * By passing the placement of two string, this method will return the direction
     * that the second tile is on the first tile
     *
     * @param tile     a 6-length long string representing the placement of the first tile
     * @param testTile a 6-length long string represeting the placement of the second tile
     * @return a one-length long string representing the direction
     * @author Jiawei Fan
     */
    public static String getNeighbourDirection(String tile, String testTile) {
        int row1 = Integer.parseInt(tile.substring(LENGTH_OF_TILE_TYPE, LENGTH_OF_TILE_TYPE + 1));
        int col1 = Integer.parseInt(tile.substring(LENGTH_OF_TILE_TYPE + 1, LENGTH_OF_TILE_TYPE + 2));
        int row2 = Integer.parseInt(testTile.substring(LENGTH_OF_TILE_TYPE, LENGTH_OF_TILE_TYPE + 1));
        int col2 = Integer.parseInt(testTile.substring(LENGTH_OF_TILE_TYPE + 1, LENGTH_OF_TILE_TYPE + 2));
        if (row2 - row1 == -1) {
            return "N";
        }
        if (row2 - row1 == 1) {
            return "S";
        }
        if (col2 - col1 == -1) {
            return "W";
        }
        if (col2 - col1 == 1) {
            return "E";
        }
        return "";//two tiles are not neightbors, actually this will never be reached becaue isNeighbor method will be called as the precondition of this method in another main.
    }

    /**
     * This method is used to decide if the placement containing pieces that
     * are looping back to the edge stations.
     *
     * @param piecePlacement string representing the current placement
     * @return true if there are tiles looping back to edge stations
     * @author Jiawei Fan
     */
    public static boolean isLoopBacktoEdge(String piecePlacement) {
        if (piecePlacement.contains("0") || piecePlacement.contains("7")) {
            String row = piecePlacement.substring(4, 5);
            String column = piecePlacement.substring(5, 6);
            if (row.equals("0") && column.compareTo("7") < 0 && column.compareTo("0") > 0) {
                return piecePlacement.substring(0, 1).equals("d");
            }
            if (column.equals("7") && row.compareTo("7") < 0 && row.compareTo("0") > 0) {
                return piecePlacement.substring(1, 2).equals("d");
            }
            if (row.equals("7") && column.compareTo("0") > 0 && column.compareTo("7") < 0) {
                return piecePlacement.substring(2, 3).equals("d");
            }
            if (column.equals("0") && row.compareTo("0") > 0 && row.compareTo("7") < 0) {
                return piecePlacement.substring(3, 4).equals("d");
            }
            if (row.equals("0") && column.equals("0")) {
                return piecePlacement.substring(3, 4).equals("b") || piecePlacement.substring(0, 1).equals("c") || piecePlacement.substring(3, 4).equals("d") || piecePlacement.substring(0, 1).equals("d");
            }
            if (row.equals("0") && column.equals("7")) {
                return piecePlacement.substring(0, 1).equals("b") || piecePlacement.substring(1, 2).equals("c") || piecePlacement.substring(0, 1).equals("d") || piecePlacement.substring(1, 2).equals("d");
            }
            if (row.equals("7") && column.equals("7")) {
                return piecePlacement.substring(1, 2).equals("b") || piecePlacement.substring(2, 3).equals("c") || piecePlacement.substring(1, 2).equals("d") || piecePlacement.substring(2, 3).equals("d");
            }
            if (row.equals("7") && column.equals("0")) {
                return piecePlacement.substring(2, 3).equals("b") || piecePlacement.substring(3, 4).equals("c") || piecePlacement.substring(2, 3).equals("d") || piecePlacement.substring(3, 4).equals("d");
            }
        }
        return false;
    }

    /**
     * This method is to determine if the piece placement has neighbors in the placement.
     *
     * @param piecePlacement piece placement string of length 6
     * @param placement      current placement string
     * @return true is the piece placement has neighbors, so it is valid
     * @author Jiawei Fan
     */
    public static boolean hasNeighbor(String piecePlacement, String placement) {
        int numberOfPlacements = placement.length() / Placement.LENGTH_OF_ONE_PlACEMENT;
        for (int i = 0; i < numberOfPlacements; i++) {
            String testPlacement = placement.substring(Placement.LENGTH_OF_ONE_PlACEMENT * i, Placement.LENGTH_OF_ONE_PlACEMENT * i + Placement.LENGTH_OF_ONE_PlACEMENT);
            if (isNeighbour(piecePlacement, testPlacement)) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method is to determine if a piece placement is simple valid or not.
     * simple valid means if it is next to edge station it is not loop back to edge station or has a neighbor and if it is not next to edge, it has neighbors
     *
     * @param placement      String representing current placement on the board
     * @param piecePlacement the piece which is going to be placed
     * @return true if it is simple valid, false if not
     * @author Jiawei Fan
     */

    public static boolean isSimpleValid(String placement, String piecePlacement) {
        if (piecePlacement.contains("0") || piecePlacement.contains("7")) {
            return !isLoopBacktoEdge(piecePlacement);
        } else {
            return hasNeighbor(piecePlacement, placement);
        }
    }

    /**
     * This method is going to get the valid place for the tiles to be places,but has not
     * considered the case that the piece can not be placed else where. So it returns initial
     * valid places which need to be refined later.
     *
     * @param placement      string representing the placement
     * @param piece          String of length 4 representing the test piece
     * @param numberOfPlayer int representing the number of players
     * @return ArrayList containg the valid places
     * @author Jiawei Fan
     */
    public static ArrayList<String> getValidMovePlace(String placement, String piece, int numberOfPlayer) {
        ArrayList<String> boardCoordinates = Station.getBoardCoordinates();
        boardCoordinates.removeIf(placement::contains);
        ArrayList<String> aboardCoordinates = new ArrayList<>(boardCoordinates);//get all the coordinates
        for (String coordinate : boardCoordinates) {
            if (!(coordinate.contains("0") || coordinate.contains("7"))) {//then test the coordinates of inner board
                String testPiecePlacement = placement + piece + coordinate;
                if (!Metro.isPlacementSequenceValid(testPiecePlacement)) {//this is i reviewed task 6,innder board case is fine but task 6 does not consider edge carefully
                    // although task6 passes the test, it is not perfect and will fail test where more cases are tested
                    aboardCoordinates.remove(coordinate);
                }
            }
        }
        return aboardCoordinates;
    }

    /**
     * This method is to determine the case that a piece cannot ba placed elsewhere so that it is
     * valid for it to place in some cases(e.g.,looping back to edge)
     *
     * @param placement      string representing the current placement
     * @param piece          piece to be placed, it is a string of length 4
     * @param numberOfPlayer int representing the number of player
     * @return true if the piece cannot be placed anywhere
     * @author Jiawei Fan
     */
    public static boolean cannotPlaceElsewhere(String placement, String piece, int numberOfPlayer) {
        ArrayList<String> validPlaces = getValidMovePlace(placement, piece, numberOfPlayer);
        for (String each : validPlaces) {
            if (isSimpleValid(placement, piece + each)) {//once there is a valid place, it is not the case the tile cannot be placed elsewhere
                return false;
            }
        }
        return true;
    }

    /**
     * This method is to determine the final valid places based to the initial valid places for a piece,
     *
     * @param placement      current placement
     * @param piece          a string representing the type of this tile
     * @param numberOfPlayer an int representing the number of players
     * @return an arraylist of valid places to place this tile\
     * @author Jiawei Fan
     */
    public static ArrayList<String> getFinalValidPlace(String placement, String piece, int numberOfPlayer) {
        ArrayList<String> validMove = getValidMovePlace(placement, piece, numberOfPlayer);
        if (!cannotPlaceElsewhere(placement, piece, numberOfPlayer)) {
            validMove.removeIf(each -> !isSimpleValid(placement, piece + each));
        }
        return validMove;
    }
}


