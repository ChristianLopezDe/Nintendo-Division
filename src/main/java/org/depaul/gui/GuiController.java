package org.depaul.gui;

import org.depaul.logic.data.ViewData;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.depaul.logic.events.EventSource;
import org.depaul.logic.events.EventType;
import org.depaul.logic.events.InputEventListener;
import org.depaul.logic.events.MoveEvent;
import org.depaul.logic.sound.Sound;

import java.net.URL;
import java.util.LinkedList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

public class GuiController implements Initializable {

    private static final int BRICK_SIZE = 20;

    @FXML
    private GridPane gamePanel;

    @FXML
    private Text scoreValue;

    @FXML
    private Text linesCount;

    @FXML
    private Group groupNotification;

    @FXML
    private GridPane nextBrick;

    @FXML
    private GridPane brickPanel;

    @FXML
    private ToggleButton pauseButton;

    @FXML
    private Group gameOverNotification;

    private Rectangle[][] displayMatrix;

    private InputEventListener eventListener;

    private Rectangle[][] rectangles;

    private Timeline timeLine;

    private final BooleanProperty isPause = new SimpleBooleanProperty();

    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    private Sound music = new Sound();

    private int NextMusicTrack = 0;

    private Sound BrickMoveSound = new Sound();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(getClass().getClassLoader().getResource("3X5.TTF").toExternalForm(), 38);
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        music.setFile(6);
        BrickMoveSound.setFile(5);
        music.play();
        music.loop();

//        Key bindings for game panel
gamePanel.setOnKeyPressed(keyEvent -> {
    if (isPause.getValue() == Boolean.FALSE && isGameOver.getValue() == Boolean.FALSE) {
        if (keyEvent.getCode() == KeyCode.SPACE) {
            onUserMove(new MoveEvent(EventType.SPACE, EventSource.THREAD));
            keyEvent.consume();
        }
        else if (keyEvent.getCode() == KeyCode.RIGHT) {
            onUserMove(new MoveEvent(EventType.RIGHT, EventSource.THREAD));
            BrickMoveSound.play();
            keyEvent.consume();
        }
        else if (keyEvent.getCode() == KeyCode.LEFT) {
            onUserMove(new MoveEvent(EventType.LEFT, EventSource.THREAD));
            keyEvent.consume();
        }
        else if (keyEvent.getCode() == KeyCode.UP) {
            onUserMove(new MoveEvent(EventType.UP, EventSource.THREAD));
            keyEvent.consume();
        }
        else if (keyEvent.getCode() == KeyCode.DOWN) {
            onUserMove(new MoveEvent(EventType.DOWN, EventSource.THREAD));
            keyEvent.consume();
        } else if(keyEvent.getCode() == KeyCode.Z) {
            onUserMove(new MoveEvent(EventType.Z, EventSource.THREAD));
            keyEvent.consume();
        }
        else if (keyEvent.getCode() == KeyCode.ESCAPE) {
            isPause.setValue(Boolean.TRUE);
            timeLine.pause();
            pauseButton.setText("Resume");
            keyEvent.consume();
        }
        else {
            // neither of those keys were entered
        }
        if(!BrickMoveSound.isPlaying()) {
            BrickMoveSound.play();
            BrickMoveSound.reset();
        }
    } else if(isPause.getValue() == Boolean.TRUE && isGameOver.getValue() == Boolean.FALSE) {
        if (keyEvent.getCode() == KeyCode.ESCAPE) {
            isPause.setValue(Boolean.FALSE);
            timeLine.play();
            pauseButton.setText("Pause");
            keyEvent.consume();
        }
    }
});

//        GAME OVER panel notification
        gameOverNotification.setVisible(false);

//        PAUSE button
        pauseButton.selectedProperty().bindBidirectional(isPause);
        pauseButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                timeLine.pause();
                pauseButton.setText("Resume");
            } else {
                timeLine.play();
                pauseButton.setText("Pause");
            }
        });

//        SCORE: Setting the reflection style
        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);
        scoreValue.setEffect(reflection);
        linesCount.setEffect(reflection);

    }

    public void initGameView(int[][] boardMatrix, ViewData brick) {
//        displayMatrix is the GUI representation of the current state of the board currentGameMatrix
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - 2);
            }
        }

//        rectangles is the GUI representation of the current state of brick.
        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(brick.getBrickData()[i][j]));
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }
        brickPanel.setLayoutX(160 + gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * BRICK_SIZE);
        brickPanel.setLayoutY(-42 + gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * BRICK_SIZE);

        generateNextBrickPanel(brick.getNextBrickData());


        timeLine = new Timeline(new KeyFrame(
                Duration.millis(500),
                ae -> onUserMove(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }

    private Paint getFillColor(int i) {
        Paint returnPaint = switch (i) {

            case 0 -> Color.TRANSPARENT;
            case 1 -> Color.web("00FFFF", 1); //cyan
            case 2 -> Color.web("00008B", 1); //dark blue
            case 3 -> Color.web("FFA500", 1); //orange
            case 4 -> Color.web("FFFF00", 1); //yellow
            case 5 -> Color.web("32CD32", 1); //lime green
            case 6 -> Color.web("BF40BF", 1); //purple
            case 7 -> Color.web("ff0000", 1); //red
            case 8 -> Color.web("4d908e", 1); 
            case 9 -> Color.web("577590", 1);
            case 10 -> Color.web("277da1", 1);
            default -> Color.web("", 1);
        };
        return returnPaint;
    }

    private void generateNextBrickPanel(int[][] nextBrickData) {

        nextBrick.getChildren().clear();
        for (int i = 0; i < nextBrickData.length; i++) {
            for (int j = 0; j < nextBrickData[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                //Fixing random next brick's color issues Samira (Start)
                setRectangleData(nextBrickData[i][j], rectangle);
                //Fixing random next brick's color issues Samira (End)
                if (nextBrickData[i][j] != 0) {
                    nextBrick.add(rectangle, j, i);
                }
            }
        }
    }

    private void refreshBrick(ViewData brick) {
        if (isPause.getValue() == Boolean.FALSE) {

            brickPanel.setLayoutX(160 + gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * BRICK_SIZE);
            brickPanel.setLayoutY(-42 + gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * BRICK_SIZE);
            for (int i = 0; i < brick.getBrickData().length; i++) {
                for (int j = 0; j < brick.getBrickData()[i].length; j++) {

                    //Fixing random falling brick's color issues Samira (Start)
                    setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);
                    //Fixing random falling brick's color issues Samira (End)

                }
            }

            generateNextBrickPanel(brick.getNextBrickData());
            
        }
    }


    public void refreshGameBackground(int[][] board) {


        int k = 0;
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {

                //Fixing random grid bricks color issues Samira (Start)

                setRectangleData(board[i][j], displayMatrix[i][j]);
                //Fixing random grid bricks color issues Samira (End)


            }

        }


    }

    private void setRectangleData(int color, Rectangle rectangle) {
        rectangle.setFill(getFillColor(color));
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);


    }

    private void randomAction() {

        int action = ThreadLocalRandom.current().nextInt(5); // 0 for game over - anything else for bonus

        if (action != 0) {
            //        Bonus notification
            NotificationPanel notificationPanel = new NotificationPanel("+" + ThreadLocalRandom.current().nextInt(100));
            groupNotification.getChildren().add(notificationPanel);
            notificationPanel.showScore(groupNotification.getChildren());

        } else {
            //        Game Over
            gameOver();
        }
    }

    private void onUserMove(MoveEvent event) {
        if (isPause.getValue() == Boolean.FALSE) {
            ViewData viewData = eventListener.onMoveEvent(event);
            refreshBrick(viewData);
        }
        gamePanel.requestFocus();
    }

    public void ShowScore(int n){
        NotificationPanel notificationPanel = new NotificationPanel("+" + n);
        groupNotification.getChildren().add(notificationPanel);
        notificationPanel.showScore(groupNotification.getChildren());
    }

    private void rotateMove(MoveEvent event) {
        if (isPause.getValue() == Boolean.FALSE) {
            ViewData viewData = eventListener.onRotateEvent(event);
            refreshBrick(viewData);
        }
        gamePanel.requestFocus();
    }

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void bindScore(IntegerProperty integerProperty) {
        scoreValue.textProperty().bind(integerProperty.asString());
    }
    public void bindLines(IntegerProperty integerProperty) {
        linesCount.textProperty().bind(integerProperty.asString());
    }

    public void gameOver() {
        timeLine.stop();
        GameOverPanel gameOverPanel = new GameOverPanel("GameOver!\n" + "Score:\n" + scoreValue.textProperty().getValue());
        gameOverNotification.getChildren().add(gameOverPanel);
        gameOverNotification.setVisible(true);
        isGameOver.setValue(Boolean.TRUE);

    }
  //Game Start CountDown
//     public void countDown(){
//         for(int i = 3; i > 0; i--){
//             NotificationPanel notificationPanel = new NotificationPanel(Integer.toString(i));
//             groupNotification.getChildren().add(notificationPanel);
//             notificationPanel.showScore(groupNotification.getChildren());
//             //waits a second
//             long start = System.currentTimeMillis();
//             while(System.currentTimeMillis()-start<1000){    
//             }
//         }
//     }

    public void newGame(ActionEvent actionEvent) {  //where 'New Game' button is hit
        timeLine.stop();
        music.stop();
        music.close();
        music.setFile(NextMusicTrack);
        if(NextMusicTrack == 0) {
            NextMusicTrack = 1;
        } else if (NextMusicTrack == 1) {
            NextMusicTrack = 6;
        } else {
            NextMusicTrack = 0;
            //THESE TRACKS ARE FIRE!!!!
        }
        music.play();
        music.loop();
        gameOverNotification.setVisible(false);
        eventListener.createNewGame();
        gamePanel.requestFocus();
        //countDown();
        timeLine.play();
        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);
    }

    public void pauseGame(ActionEvent actionEvent) {
        gamePanel.requestFocus();
    }

    public void stopMusic() {
        music.stop();
    }
}
