package comp1110.ass2.gui;

import comp1110.ass2.Metro;
import comp1110.ass2.Placement;
import javafx.animation.PathTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;

public class AddElement {
    private static final int SQUARE_SIZE = 70;
    private static final String URI_BASE = "assets/";

    /**
     * This method is written to make it eaiser to add text group. By passing needed parameters,
     * text group can be added to the children of specific group.
     *
     * @param root     the root group which add the text group as children
     * @param text     a String represnting the text information
     * @param layoutX  X coordinate of the text group
     * @param layoutY  y coordinate of the text group
     * @param fontSize font size of the text
     * @param color    color of the text
     * @author Jiawei Fan
     */
    public static void addText(Group root, String text, Double layoutX, Double layoutY, int fontSize, Color color) {
        Text text1 = new Text(text);
        text1.setLayoutX(layoutX * SQUARE_SIZE);
        text1.setLayoutY(layoutY * SQUARE_SIZE);
        text1.setFill(color);
        text1.setFont(Font.font("Segoe UI", fontSize));
        root.getChildren().add(text1);
    }

    /**
     * This method is to add the game over text, it will be used when there is no more
     * tile in the deck. The code only contains an addText method, the class name is
     * just to make it more clear.
     *
     * @param root
     * @author Jiawei Fan
     */
    public static void addGameOver(Group root) {
        addText(root, "Game Over!!", 3.0, 5.0, 120, Color.BLACK);
    }

    /**
     * This method is to add a flag to indicate this is which player's turn of placing
     * tile, the flag is designed to be a pink rectangle which is placed next to the
     * player.
     *
     * @param root      root Group which adds the flag as children
     * @param flagGroup Flag group which conntains the flag
     * @param turnIndex an int represting the turn
     * @author Jiawei Fan
     */
    public static void addFlag(Group root, Group flagGroup, int turnIndex) {
        root.getChildren().remove(flagGroup);//firstly remove this group,
        flagGroup.getChildren().clear();//reomove the children, this line of code might not be useful, but just to ensure flag group childen has been removed,
        Rectangle r = new Rectangle(20, 20);
        r.setFill(Color.LIGHTPINK);
        r.setLayoutX(11.5 * SQUARE_SIZE);
        r.setLayoutY(1.3 * (turnIndex + 0.8) * SQUARE_SIZE);
        flagGroup.getChildren().add(r);
        root.getChildren().add(flagGroup);
    }

    /**
     * This method is to add the score information. Everytime a player places a
     * tile, this method will be called to update the scores of each player.
     *
     * @param root           root group which contains the score group
     * @param scoreGroup     scoreGroup which contains several score text
     * @param placement      a string representing the placement
     * @param numberOfPlayer a int representing the number of player
     * @author Jiawei Fan
     */
    public static void addScore(Group root, Group scoreGroup, String placement, int numberOfPlayer) {
        root.getChildren().remove(scoreGroup);//Firstly remove the score Group
        scoreGroup.getChildren().clear();
        int[] scoreArray = Metro.getScore(placement, numberOfPlayer);
        for (int i = 0; i < numberOfPlayer; i++) {
            String scoreString = scoreArray[i] + "";
            addText(scoreGroup, scoreString, 12.3, 1.3 * (i + 1), 20, Color.DARKCYAN);
        }
        root.getChildren().add(scoreGroup);
    }

    /**
     * A simple method transform a string array to a string.
     *
     * @param stringArray the string array to be transformed
     * @return A string which is transformed from the string array
     * @author Jiawei Fan
     */
    public static String stringArrayToString(String[] stringArray) {
        StringBuilder string = new StringBuilder();
        for (String s : stringArray) {
            if (s != null)
                string.append(s);
        }
        return string.toString();
    }

    /**
     * This method is orinally to get every rectangle place on the board,
     * but later another method is used to get the valid rectangle place of the
     * tile, then this method is used to draw the board instead.
     *
     * @return all the rectangles
     * @author Jiawei Fan
     */
    public static ArrayList<Rectangle> getExistingRectangle() {
        ArrayList<Rectangle> rectangles = new ArrayList<>();
        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                if (!((row == 4 && col == 4) || (row == 4 && col == 5) || (row == 5 && col == 4) || (row == 5 && col == 5))) {//centre stations are not drawn
                    Rectangle rectangle = new Rectangle(SQUARE_SIZE, SQUARE_SIZE);
                    rectangle.setLayoutX(col * SQUARE_SIZE);
                    rectangle.setLayoutY(row * SQUARE_SIZE);
                    rectangle.setStrokeWidth(1.0);
                    rectangle.setStroke(Color.WHITE);
                    rectangle.setFill(Color.rgb(0, 0, 0, 0.08));
                    rectangles.add(rectangle);
                }
            }
        }
        return rectangles;
    }

    /**
     * This method is used to get the cloest rectangle by passing a coordinate and the valid places.
     * This method is written according to the learning from the triangle example in the tutorial.
     *
     * @param x          a double representing the x coordinate
     * @param y          a double representing the y coordinate
     * @param validPlace the coordinate of valid places which tile can be put on. Each valid place is represented by a string of length 2.
     * @return The closet Rectangle
     * @author Jiawei Fan
     */
    public static Rectangle cloestRectangle(double x, double y, ArrayList<String> validPlace) {
        Rectangle cloest = null;
        ArrayList<Rectangle> rectangles = getValidRectangles(validPlace);
        double closestDistance = Double.MAX_VALUE;
        for (Rectangle rectangle : rectangles) {
            double tx = rectangle.getLayoutX();
            double ty = rectangle.getLayoutY();
            double dx = tx - x;
            double dy = ty - y;
            double distanceSquare = Math.pow(dx, 2) + Math.pow(dy, 2);
            if (distanceSquare < closestDistance) {
                closestDistance = distanceSquare;
                cloest = rectangle;
            }
        }
        return cloest;
    }

    /**
     * This method is used to get the array of rectangles based an array of valid coordinates,
     * validPlace a array of string, each string is of length two, representing the coordinates
     * of this valid places.
     *
     * @param validPlace an Arraylist representing the valid coordinates
     * @return An arraylist of rectangles which match with the valid places
     * @author Jiawei Fan
     */
    public static ArrayList<Rectangle> getValidRectangles(ArrayList<String> validPlace) {
        ArrayList<Rectangle> rectangles = new ArrayList<>();
        for (String each : validPlace) {
            int row = Integer.parseInt(each.substring(0, 1)) + 1;//the difference between the valid place coordinate and the actual row in the screen is 1
            int col = Integer.parseInt(each.substring(1, 2)) + 1;
            Rectangle r = new Rectangle();
            r.setHeight(SQUARE_SIZE);
            r.setWidth(SQUARE_SIZE);
            r.setLayoutX(col * SQUARE_SIZE);
            r.setLayoutY(row * SQUARE_SIZE);
            r.setFill(Color.LIGHTPINK);
            r.setOpacity(0.5);
            rectangles.add(r);
        }
        return rectangles;
    }

    /**
     * This method is to draw the valid rectangles, which is the valid places where a tile can be put
     * on when player is placing it. Therefore, players can visualise the valid places to place the tile.
     *
     * @param validRectangle a group which used to contains valid rectangles
     * @param validPlace     Arraylist of valid places
     * @author Jiawei Fan
     */
    public static void drawValidRectangles(Group validRectangle, ArrayList<String> validPlace) {
        for (String each : validPlace) {
            int row = Integer.parseInt(each.substring(0, 1)) + 1;
            int col = Integer.parseInt(each.substring(1, 2)) + 1;
            Rectangle r = new Rectangle();
            r.setHeight(SQUARE_SIZE);
            r.setWidth(SQUARE_SIZE);
            r.setLayoutX(col * SQUARE_SIZE);
            r.setLayoutY(row * SQUARE_SIZE);
            r.setFill(Color.LIGHTPINK);
            r.setOpacity(0.5);
            validRectangle.getChildren().add(r);
        }
    }

    /**
     * This method is used to help AI decide which place is the best to place a tile to get the highest score
     * We assume an additional score of 5 to be a good placement, so if the placing the tile can get more than
     * 5 score, it is a good placement. This number is decided by doing a lot of tests, and averaging the result.
     *
     * @param root                   root group
     * @param drawnTile              the drawn tile which is going to be placed
     * @param placementStringBuilder a StringBuilder which contains the placement information
     * @param numberOfPlayer         a int representing the number of player
     * @return a String representing the coordinate of the best placement
     * @author Jiawei Fan
     */
    public static String getHighestAdditionalScoreCoordinates(Group root, String drawnTile, StringBuilder placementStringBuilder, int numberOfPlayer) {
        int index = 0;
        ArrayList<String> validPlace = Placement.getFinalValidPlace(placementStringBuilder.toString(), drawnTile, numberOfPlayer);//get valid places to put the tile based on the current placement and the drawn tile
        int additionalScore = 0;
        int currentPlayerIndex = (placementStringBuilder.length() / Placement.LENGTH_OF_ONE_PlACEMENT) % numberOfPlayer;
        int scoreArray[] = Metro.getScore(placementStringBuilder.toString(), numberOfPlayer);
        int currentScore = scoreArray[currentPlayerIndex];
        if (validPlace.size() == 0) {//game over if there is no valid place to place the tile
            addGameOver(root);
            return "";
        } else {
            for (int i = 0; i < validPlace.size(); i++) {
                String coordinate = validPlace.get(i);//test each coordinate
                String nextPlacement = placementStringBuilder.toString() + drawnTile + coordinate;//get the tested placement
                int nextMoveScore = Metro.getScore(nextPlacement, numberOfPlayer)[currentPlayerIndex];
                if (nextMoveScore - currentScore > additionalScore) {
                    additionalScore = nextMoveScore - currentScore;
                    index = i;
                }
            }
            if (additionalScore > 5) {//this is the decision point, if the highest additional score is greater than 5, then this is decided to be a good placement
                return validPlace.get(index);
            } else return "";//return "" if  additional score is less than 5
        }
    }

    /**
     * This method comes to the placement stage for the advande AI player. This method will place the chosen tile to the board.
     * This method contains some animations which makes the placement process look nicer.
     *
     * @param root                       a root group
     * @param drawnTile                  the tile to be placed
     * @param placementStringBuilder     current placement string
     * @param numberOfPlayer             a int representing number of player
     * @param putTile                    ImageView of the drawn tile
     * @param additionalScoreCoordinates the coordinate of place where the drawn tile is going to be placed
     * @author Jiawei Fan
     */
    public void giveGoodPlacement(Group root, String drawnTile, StringBuilder placementStringBuilder, int numberOfPlayer, ImageView putTile, String additionalScoreCoordinates) {
        ArrayList<String> validPlace = Placement.getFinalValidPlace(placementStringBuilder.toString(), drawnTile, numberOfPlayer);
        if (additionalScoreCoordinates.equals("")) {
            givePlacement(root, drawnTile, placementStringBuilder, numberOfPlayer, putTile);
        } else {
            for (String coordinate : validPlace) {
                if (coordinate.equals(additionalScoreCoordinates)) {//firstly find the coordinate
                    int layoutX = Integer.parseInt(coordinate.substring(1, 2)) + 1;
                    int layoutY = Integer.parseInt(coordinate.substring(0, 1)) + 1;
                    putTile.setLayoutX(11 * SQUARE_SIZE);//give a start point of the animation of placement
                    putTile.setLayoutY(8.5 * SQUARE_SIZE);
                    root.getChildren().add(putTile);
                    Path path = new Path();
                    path.getElements().add(new MoveTo(0, 0));
                    path.getElements().add(new LineTo(layoutX * SQUARE_SIZE - 11 * SQUARE_SIZE + 0.5 * SQUARE_SIZE, layoutY * SQUARE_SIZE - 8.5 * SQUARE_SIZE + 0.5 * SQUARE_SIZE));
                    PathTransition pathTransition = new PathTransition();
                    pathTransition.setDuration(Duration.seconds(2));
                    pathTransition.setNode(putTile);
                    pathTransition.setPath(path);
                    pathTransition.setCycleCount(1);
                    pathTransition.play();
                    placementStringBuilder.append(drawnTile);//change the placement to be a new one because new tile has been placed on board
                    placementStringBuilder.append(coordinate);
                    break;
                }
            }
        }
    }

    /**
     * This method is also used to give a placement to the board, but this method is for simple AI.
     * A simple AI will randomly choose a valid place to place the drawn tile.
     *
     * @param root                   root group
     * @param drawnTile              the tile to be placed
     * @param placementStringBuilder current placement information
     * @param numberOfPlayer         an int representing the numebr of integers
     * @param putTile                ImageView of the drawn tile
     * @author Jiawei Fan
     */
    public static void givePlacement(Group root, String drawnTile, StringBuilder placementStringBuilder, int numberOfPlayer, ImageView putTile) {
        ArrayList<String> validPlace = Placement.getFinalValidPlace(placementStringBuilder.toString(), drawnTile, numberOfPlayer);
        if (validPlace.size() == 0) {
            addGameOver(root);
        } else {
            String movement = Metro.generateMove(placementStringBuilder.toString(), drawnTile, numberOfPlayer);//randomly generate a momentment
            int layoutX = Integer.parseInt(movement.substring(5, 6)) + 1;
            int layoutY = Integer.parseInt(movement.substring(4, 5)) + 1;
            putTile.setLayoutX(11 * SQUARE_SIZE);
            putTile.setLayoutY(8.5 * SQUARE_SIZE);
            root.getChildren().add(putTile);
            Path path = new Path();
            path.getElements().add(new MoveTo(0, 0));
            path.getElements().add(new LineTo(layoutX * SQUARE_SIZE - 11 * SQUARE_SIZE + 0.5 * SQUARE_SIZE, layoutY * SQUARE_SIZE - 8.5 * SQUARE_SIZE + 0.5 * SQUARE_SIZE));
            PathTransition pathTransition = new PathTransition();
            pathTransition.setDuration(Duration.seconds(2));
            pathTransition.setNode(putTile);
            pathTransition.setPath(path);
            pathTransition.setCycleCount(1);
            pathTransition.play();
            placementStringBuilder.append(movement);//update the placement string information
        }
    }


    public static class DraggableRectangle extends ImageView {
        double mouseX;
        double mouseY;

        public DraggableRectangle(Group root, String tile, StringBuilder placementStringBuilder, int numberOfPlayer, Group validRectangle, String[] totalHandArray, int index, Button button1, Group scoreGroup, Group flagGroup) {
            validRectangle.getChildren().clear();
            Button button = new Button("Draw again");
            button.setStyle("-fx-base: pink;");
            Image image = new Image(this.getClass().getResource(URI_BASE + tile + ".jpg").toString());
            this.setFitHeight(SQUARE_SIZE);
            this.setFitWidth(SQUARE_SIZE);
            this.setImage(image);
            this.setLayoutX(11 * SQUARE_SIZE);
            this.setLayoutY(8.5 * SQUARE_SIZE);
            this.setOnMousePressed(event -> {
                mouseX = this.getLayoutX() - event.getSceneX();
                mouseY = this.getLayoutY() - event.getSceneY();
                ArrayList<String> validPlace = Placement.getFinalValidPlace(placementStringBuilder.toString(), tile, numberOfPlayer);
                if (validPlace.size() == 0) {
                    addGameOver(root);
                }
                drawValidRectangles(validRectangle, validPlace);
            });

            this.setOnMouseDragged(event ->
            {
                this.setLayoutX(event.getSceneX() + mouseX);
                this.setLayoutY(event.getSceneY() + mouseY);
                validRectangle.getChildren().remove(button);
            });

            this.setOnMouseReleased(event ->
            {
                Rectangle closest = cloestRectangle(this.getLayoutX(), this.getLayoutY(), Placement.getFinalValidPlace(placementStringBuilder.toString(), tile, numberOfPlayer));
                this.setLayoutX(closest.getLayoutX());
                this.setLayoutY(closest.getLayoutY());
                validRectangle.getChildren().clear();
                ImageView image1 = new ImageView(this.getClass().getResource(URI_BASE + tile + ".jpg").toString());
                image1.setLayoutX(closest.getLayoutX());
                image1.setLayoutY(closest.getLayoutY());
                image1.setFitWidth(SQUARE_SIZE);
                image1.setFitHeight(SQUARE_SIZE);
                root.getChildren().add(image1);
                totalHandArray[index] = null;
                this.setOnMousePressed(null);
                this.setOnMouseDragged(null);
                this.setOnMouseReleased(null);
                String row = ((int) (this.getLayoutY() / SQUARE_SIZE - 1)) + "";
                String col = ((int) (this.getLayoutX() / SQUARE_SIZE - 1)) + "";
                placementStringBuilder.append(tile);
                placementStringBuilder.append(row);
                placementStringBuilder.append(col);
                int turnIndex = (placementStringBuilder.length() / 6) % numberOfPlayer;
                addFlag(root, flagGroup, turnIndex);
                if (totalHandArray[turnIndex] != null) {
                    validRectangle.getChildren().add(new AddElement.DraggableRectangle(root, totalHandArray[turnIndex], placementStringBuilder, numberOfPlayer, validRectangle, totalHandArray, turnIndex, button1, scoreGroup, flagGroup));
                } else {
                    validRectangle.getChildren().add(button1);
                }
                addScore(root, scoreGroup, placementStringBuilder.toString(), numberOfPlayer);
            });


            String drawnTile = Metro.drawFromDeck(placementStringBuilder.toString(), AddElement.stringArrayToString(totalHandArray));
            if (drawnTile.equals("")) {
                addGameOver(root);
            }
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    validRectangle.getChildren().clear();
                    DraggableRectangle2 a = new DraggableRectangle2(root, drawnTile, placementStringBuilder, numberOfPlayer, validRectangle, totalHandArray, button1, button, scoreGroup, flagGroup);
                    validRectangle.getChildren().add(a);
                }
            });
            button.setLayoutX(12.5 * SQUARE_SIZE);
            button.setLayoutY(10 * SQUARE_SIZE);
            validRectangle.getChildren().add(button);
        }
    }

    public static class DraggableRectangle2 extends ImageView {
        double mouseX;
        double mouseY;

        public DraggableRectangle2(Group root, String tile, StringBuilder placementStringBuilder, int numberOfPlayer, Group validRectangle, String[] totalHandArray, Button button1, Button button, Group scoreGroup, Group flagGroup) {
            validRectangle.getChildren().clear();
            Image image = new Image(this.getClass().getResource(URI_BASE + tile + ".jpg").toString());
            this.setFitHeight(SQUARE_SIZE);
            this.setFitWidth(SQUARE_SIZE);
            this.setImage(image);
            this.setLayoutX(12.5 * SQUARE_SIZE);
            this.setLayoutY(8.5 * SQUARE_SIZE);
            this.setOnMousePressed(event -> {
                mouseX = this.getLayoutX() - event.getSceneX();
                mouseY = this.getLayoutY() - event.getSceneY();
                ArrayList<String> validPlace = Placement.getFinalValidPlace(placementStringBuilder.toString(), tile, numberOfPlayer);
                if (validPlace.size() == 0) {
                    addGameOver(root);
                }
                drawValidRectangles(validRectangle, validPlace);
            });

            this.setOnMouseDragged(event ->
            {
                this.setLayoutX(event.getSceneX() + mouseX);
                this.setLayoutY(event.getSceneY() + mouseY);
                validRectangle.getChildren().remove(button);
            });

            this.setOnMouseReleased(event ->
            {
                Rectangle closest = cloestRectangle(this.getLayoutX(), this.getLayoutY(), Placement.getFinalValidPlace(placementStringBuilder.toString(), tile, numberOfPlayer));
                ImageView image1 = new ImageView(this.getClass().getResource(URI_BASE + tile + ".jpg").toString());
                image1.setLayoutX(closest.getLayoutX());
                image1.setLayoutY(closest.getLayoutY());
                image1.setFitWidth(SQUARE_SIZE);
                image1.setFitHeight(SQUARE_SIZE);
                root.getChildren().add(image1);
                this.setLayoutX(closest.getLayoutX());
                this.setLayoutY(closest.getLayoutY());
                validRectangle.getChildren().clear();
                this.setOnMousePressed(null);
                this.setOnMouseDragged(null);
                this.setOnMouseReleased(null);
                String row = ((int) (this.getLayoutY() / SQUARE_SIZE - 1)) + "";
                String col = ((int) (this.getLayoutX() / SQUARE_SIZE - 1)) + "";
                placementStringBuilder.append(tile);
                placementStringBuilder.append(row);
                placementStringBuilder.append(col);
                int turnIndex = (placementStringBuilder.length() / 6) % numberOfPlayer;
                addScore(root, scoreGroup, placementStringBuilder.toString(), numberOfPlayer);
                addFlag(root, flagGroup, turnIndex);
                if (totalHandArray[turnIndex] != null) {
                    validRectangle.getChildren().clear();
                    validRectangle.getChildren().remove(button1);
                    validRectangle.getChildren().add(new AddElement.DraggableRectangle(root, totalHandArray[turnIndex], placementStringBuilder, numberOfPlayer, validRectangle, totalHandArray, turnIndex, button1, scoreGroup, flagGroup));
                } else {
                    validRectangle.getChildren().add(button1);
                }

            });

        }
    }


    public static class DraggableRectangle3 extends ImageView {
        double mouseX;
        double mouseY;

        public DraggableRectangle3(Group root, String tile, StringBuilder placementStringBuilder, int numberOfPlayer, Group validRectangle, String[] totalHandArray, int index, Button button1, Group scoreGroup, Group flagGroup, Boolean isAdvancedBot) {
            validRectangle.getChildren().clear();
            Button button = new Button("Draw again");
            button.setStyle("-fx-base: pink;");
            Image image = new Image(this.getClass().getResource(URI_BASE + tile + ".jpg").toString());
            this.setFitHeight(SQUARE_SIZE);
            this.setFitWidth(SQUARE_SIZE);
            this.setImage(image);
            this.setLayoutX(11 * SQUARE_SIZE);
            this.setLayoutY(8.5 * SQUARE_SIZE);
            this.setOnMousePressed(event -> {
                mouseX = this.getLayoutX() - event.getSceneX();
                mouseY = this.getLayoutY() - event.getSceneY();
                ArrayList<String> validPlace = Placement.getFinalValidPlace(placementStringBuilder.toString(), tile, numberOfPlayer);
                if (validPlace.size() == 0) {
                    addGameOver(root);
                }
                drawValidRectangles(validRectangle, validPlace);
            });

            this.setOnMouseDragged(event ->
            {
                this.setLayoutX(event.getSceneX() + mouseX);
                this.setLayoutY(event.getSceneY() + mouseY);
                validRectangle.getChildren().remove(button);
            });

            this.setOnMouseReleased(event ->
            {
                Rectangle closest = cloestRectangle(this.getLayoutX(), this.getLayoutY(), Placement.getFinalValidPlace(placementStringBuilder.toString(), tile, numberOfPlayer));
                this.setLayoutX(closest.getLayoutX());
                this.setLayoutY(closest.getLayoutY());
                validRectangle.getChildren().clear();
                ImageView image1 = new ImageView(this.getClass().getResource(URI_BASE + tile + ".jpg").toString());
                image1.setLayoutX(closest.getLayoutX());
                image1.setLayoutY(closest.getLayoutY());
                image1.setFitWidth(SQUARE_SIZE);
                image1.setFitHeight(SQUARE_SIZE);
                root.getChildren().add(image1);
                totalHandArray[index] = null;
                this.setOnMousePressed(null);
                this.setOnMouseDragged(null);
                this.setOnMouseReleased(null);
                String row = ((int) (this.getLayoutY() / SQUARE_SIZE - 1)) + "";
                String col = ((int) (this.getLayoutX() / SQUARE_SIZE - 1)) + "";
                placementStringBuilder.append(tile);
                placementStringBuilder.append(row);
                placementStringBuilder.append(col);
                int turnIndex = (placementStringBuilder.length() / 6) % numberOfPlayer;
                addFlag(root, flagGroup, turnIndex);
                addScore(root, scoreGroup, placementStringBuilder.toString(), numberOfPlayer);
                //recursive function n-1times
                AddElement a = new AddElement();
                System.out.println(11111);
                a.addComputerPlacement(root, placementStringBuilder, numberOfPlayer, validRectangle, totalHandArray, button1, scoreGroup, flagGroup, isAdvancedBot);
            });


            String drawnTile = Metro.drawFromDeck(placementStringBuilder.toString(), AddElement.stringArrayToString(totalHandArray));
            if (drawnTile.equals("")) {
                addGameOver(root);
            }
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    validRectangle.getChildren().clear();
                    DraggableRectangle4 a = new DraggableRectangle4(root, drawnTile, placementStringBuilder, numberOfPlayer, validRectangle, totalHandArray, button1, button, scoreGroup, flagGroup, isAdvancedBot);
                    validRectangle.getChildren().add(a);
                }
            });
            button.setLayoutX(12.5 * SQUARE_SIZE);
            button.setLayoutY(10 * SQUARE_SIZE);
            validRectangle.getChildren().add(button);
        }
    }

    public static class DraggableRectangle4 extends ImageView {
        double mouseX;
        double mouseY;

        public DraggableRectangle4(Group root, String tile, StringBuilder placementStringBuilder, int numberOfPlayer, Group validRectangle, String[] totalHandArray, Button button1, Button button, Group scoreGroup, Group flagGroup, Boolean isAdvancedBot) {
            validRectangle.getChildren().clear();
            Image image = new Image(this.getClass().getResource(URI_BASE + tile + ".jpg").toString());
            this.setFitHeight(SQUARE_SIZE);
            this.setFitWidth(SQUARE_SIZE);
            this.setImage(image);
            this.setLayoutX(12.5 * SQUARE_SIZE);
            this.setLayoutY(8.5 * SQUARE_SIZE);
            this.setOnMousePressed(event -> {
                mouseX = this.getLayoutX() - event.getSceneX();
                mouseY = this.getLayoutY() - event.getSceneY();
                ArrayList<String> validPlace = Placement.getFinalValidPlace(placementStringBuilder.toString(), tile, numberOfPlayer);
                if (validPlace.size() == 0) {
                    addGameOver(root);
                }
                drawValidRectangles(validRectangle, validPlace);
            });

            this.setOnMouseDragged(event ->
            {
                this.setLayoutX(event.getSceneX() + mouseX);
                this.setLayoutY(event.getSceneY() + mouseY);
                validRectangle.getChildren().remove(button);
            });

            this.setOnMouseReleased(event ->
            {
                Rectangle closest = cloestRectangle(this.getLayoutX(), this.getLayoutY(), Placement.getFinalValidPlace(placementStringBuilder.toString(), tile, numberOfPlayer));
                ImageView image1 = new ImageView(this.getClass().getResource(URI_BASE + tile + ".jpg").toString());
                image1.setLayoutX(closest.getLayoutX());
                image1.setLayoutY(closest.getLayoutY());
                image1.setFitWidth(SQUARE_SIZE);
                image1.setFitHeight(SQUARE_SIZE);
                root.getChildren().add(image1);
                this.setLayoutX(closest.getLayoutX());
                this.setLayoutY(closest.getLayoutY());
                validRectangle.getChildren().clear();
                this.setOnMousePressed(null);
                this.setOnMouseDragged(null);
                this.setOnMouseReleased(null);
                String row = ((int) (this.getLayoutY() / SQUARE_SIZE - 1)) + "";
                String col = ((int) (this.getLayoutX() / SQUARE_SIZE - 1)) + "";
                placementStringBuilder.append(tile);
                placementStringBuilder.append(row);
                placementStringBuilder.append(col);
                int turnIndex = (placementStringBuilder.length() / 6) % numberOfPlayer;
                addScore(root, scoreGroup, placementStringBuilder.toString(), numberOfPlayer);
                addFlag(root, flagGroup, turnIndex);
                AddElement a = new AddElement();
                a.addComputerPlacement(root, placementStringBuilder, numberOfPlayer, validRectangle, totalHandArray, button1, scoreGroup, flagGroup, isAdvancedBot);
            });

        }
    }

    public void addComputerPlacement(Group root, StringBuilder placementStringBuilder, int numberOfPlayer, Group validRectangle, String[] totalHandArray, Button button1, Group scoreGroup, Group flagGroup, Boolean isAdvancedBot) throws NullPointerException {
        System.out.println(22222);
        Random random = new Random();
        int numberOfPlacement = placementStringBuilder.length() / 6;
        int turnIndex = numberOfPlacement % numberOfPlayer;
        if (turnIndex == 0) {
            if (totalHandArray[turnIndex] == null) {
                validRectangle.getChildren().add(button1);
            } else {
                DraggableRectangle3 b = new DraggableRectangle3(root, totalHandArray[turnIndex], placementStringBuilder, numberOfPlayer, validRectangle, totalHandArray, turnIndex, button1, scoreGroup, flagGroup, isAdvancedBot);
                validRectangle.getChildren().add(b);
            }
        } else {
            if (totalHandArray[turnIndex] == null) {
                String drawnTile = Metro.drawFromDeck(placementStringBuilder.toString(), AddElement.stringArrayToString(totalHandArray));
                if (drawnTile.equals("")) {
                    AddElement.addGameOver(root);
                }
                if (!isAdvancedBot) {
                    compChoice(root, placementStringBuilder, numberOfPlayer, validRectangle, totalHandArray, button1, scoreGroup, flagGroup, random, drawnTile, false);
                } else {
                    System.out.println(33332);
                    compChoice2(root, placementStringBuilder, numberOfPlayer, validRectangle, totalHandArray, button1, scoreGroup, flagGroup, drawnTile, true);
                }
            } else {
                if (!isAdvancedBot) {
                    compChoice(root, placementStringBuilder, numberOfPlayer, validRectangle, totalHandArray, button1, scoreGroup, flagGroup, random, totalHandArray[turnIndex], false);
                } else {
                    System.out.println(33333);
                    compChoice2(root, placementStringBuilder, numberOfPlayer, validRectangle, totalHandArray, button1, scoreGroup, flagGroup, totalHandArray[turnIndex], true);
                }
            }
        }
    }

    private void compChoice(Group root, StringBuilder placementStringBuilder, int numberOfPlayer, Group validRectangle, String[] totalHandArray, Button button1, Group scoreGroup, Group flagGroup, Random random, String drawnTile, Boolean isAdvancedBot) throws NullPointerException {
        int turnIndex = (placementStringBuilder.length() / 6) % numberOfPlayer;
        ImageView putTile = new ImageView();
        putTile.setImage(new Image(this.getClass().getResource(URI_BASE + drawnTile + ".jpg").toString()));
        putTile.setFitWidth(SQUARE_SIZE);
        putTile.setFitHeight(SQUARE_SIZE);
        putTile.setLayoutX(11 * SQUARE_SIZE);
        putTile.setLayoutY(8.5 * SQUARE_SIZE);
        validRectangle.getChildren().add(putTile);
        int wetherDrawAgain = random.nextInt(2);
        if (wetherDrawAgain == 1) {
            validRectangle.getChildren().clear();
            totalHandArray[turnIndex] = drawnTile;
            String drawnTile2 = Metro.drawFromDeck(placementStringBuilder.toString(), AddElement.stringArrayToString(totalHandArray));
            if (drawnTile2.equals("")) {
                AddElement.addGameOver(root);
            }
            ImageView putTile2 = new ImageView();
            putTile2.setImage(new Image(this.getClass().getResource(URI_BASE + drawnTile2 + ".jpg").toString()));
            putTile2.setFitWidth(SQUARE_SIZE);
            putTile2.setFitHeight(SQUARE_SIZE);
            givePlacement(root, drawnTile2, placementStringBuilder, numberOfPlayer, putTile2);
        } else {
            givePlacement(root, drawnTile, placementStringBuilder, numberOfPlayer, putTile);
            totalHandArray[turnIndex] = null;
            validRectangle.getChildren().clear();
            //place this tile
        }
        turnIndex = (placementStringBuilder.length() / 6) % numberOfPlayer;
        addScore(root, scoreGroup, placementStringBuilder.toString(), numberOfPlayer);
        addFlag(root, flagGroup, turnIndex);
        checkIndex(root, placementStringBuilder, numberOfPlayer, validRectangle, totalHandArray, button1, scoreGroup, flagGroup, isAdvancedBot);
    }

    private void compChoice2(Group root, StringBuilder placementStringBuilder, int numberOfPlayer, Group validRectangle, String[] totalHandArray, Button button1, Group scoreGroup, Group flagGroup, String drawnTile, Boolean isAdvancedBot) throws NullPointerException {
        int turnIndex = (placementStringBuilder.length() / 6) % numberOfPlayer;
        System.out.println(44444);
        ImageView putTile = new ImageView();
        putTile.setImage(new Image(this.getClass().getResource(URI_BASE + drawnTile + ".jpg").toString()));
        putTile.setFitWidth(SQUARE_SIZE);
        putTile.setFitHeight(SQUARE_SIZE);
        putTile.setLayoutX(11 * SQUARE_SIZE);
        putTile.setLayoutY(8.5 * SQUARE_SIZE);
        validRectangle.getChildren().add(putTile);
        String highestAdditionalScoreCoordinates = getHighestAdditionalScoreCoordinates(root, drawnTile, placementStringBuilder, numberOfPlayer);
        if (highestAdditionalScoreCoordinates.equals("")) {
            System.out.println(66666);
            validRectangle.getChildren().clear();
            totalHandArray[turnIndex] = drawnTile;
            String drawnTile2 = Metro.drawFromDeck(placementStringBuilder.toString(), AddElement.stringArrayToString(totalHandArray));
            if (drawnTile2.equals("")) {
                AddElement.addGameOver(root);
            }
            ImageView putTile2 = new ImageView();
            putTile2.setImage(new Image(this.getClass().getResource(URI_BASE + drawnTile2 + ".jpg").toString()));
            putTile2.setFitWidth(SQUARE_SIZE);
            putTile2.setFitHeight(SQUARE_SIZE);
            System.out.println(77777);
            String highestAdditionalScoreCoordinates2 = getHighestAdditionalScoreCoordinates(root, drawnTile2, placementStringBuilder, numberOfPlayer);
            giveGoodPlacement(root, drawnTile2, placementStringBuilder, numberOfPlayer, putTile2, highestAdditionalScoreCoordinates2);
        } else {
            giveGoodPlacement(root, drawnTile, placementStringBuilder, numberOfPlayer, putTile, highestAdditionalScoreCoordinates);
            totalHandArray[turnIndex] = null;
            validRectangle.getChildren().clear();
        }
        turnIndex = (placementStringBuilder.length() / 6) % numberOfPlayer;
        addScore(root, scoreGroup, placementStringBuilder.toString(), numberOfPlayer);
        addFlag(root, flagGroup, turnIndex);
        checkIndex(root, placementStringBuilder, numberOfPlayer, validRectangle, totalHandArray, button1, scoreGroup, flagGroup, isAdvancedBot);
    }

    private void checkIndex(Group root, StringBuilder placementStringBuilder, int numberOfPlayer, Group validRectangle, String[] totalHandArray, Button button1, Group scoreGroup, Group flagGroup, Boolean isAdvancedBot) {
        int turnIndex = (placementStringBuilder.length() / 6) % numberOfPlayer;
        if (turnIndex == 0) {
            if (totalHandArray[turnIndex] == null) {
                validRectangle.getChildren().add(button1);
            } else {
                DraggableRectangle3 d = new DraggableRectangle3(root, totalHandArray[0], placementStringBuilder, numberOfPlayer, validRectangle, totalHandArray, turnIndex, button1, scoreGroup, flagGroup, isAdvancedBot);
                validRectangle.getChildren().add(d);
            }
        } else {
            addComputerPlacement(root, placementStringBuilder, numberOfPlayer, validRectangle, totalHandArray, button1, scoreGroup, flagGroup, isAdvancedBot);
        }
    }
}