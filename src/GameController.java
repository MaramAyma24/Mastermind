import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;


import java.util.*;


public class GameController {

    private static final int code_length = 4;
    private static final int max_attempts = 10;
    private List<Color> colors = Arrays.asList(
            Color.RED,Color.BLUE,Color.GREEN,Color.YELLOW,Color.ORANGE,Color.BROWN);
    private List<Color> secretCode;
    private int attempts ;
    private Color[] guess = new Color[code_length];
    private VBox mainLayout;
    private GridPane guessGrid;
    private Label feedbackLabel;
    private int seconds;
    private int minutes;
    private Timeline timer;
    private Label timerLabel;
    private Label resultLabel;

    public GameController() {
        attempts = 0;
        generateSecretCode();
    }

    public VBox creatMainLayout() {
        mainLayout = new VBox(20);
        mainLayout.setAlignment(Pos.CENTER);
        timerLabel = new Label("00:00");
        timerLabel.setId("timerLabel");
        resultLabel = new Label();
        resultLabel.setFont( new javafx.scene.text.Font("Arial",16));
//        resultLabel.setTextFill(Color.WHITE);
        resultLabel.setId("resultLabel");
        Label titleLabel = new Label("Welcome To Mastermind Game");
        titleLabel.setFont( new javafx.scene.text.Font("Arial",24));
        Label instructionLabel = new Label("Guess the secret code by selecting 4 colors from 6.\nYou have 10 attempts.");
        instructionLabel.setFont( new javafx.scene.text.Font("Arial",12));
//        instructionLabel.setTextFill(Color.WHITE);
        instructionLabel.setId("instructionLabel");
        guessGrid = new GridPane();
        guessGrid.setHgap(15);
        guessGrid.setVgap(15);
        guessGrid.setAlignment(Pos.CENTER);
        for (int i = 0; i < code_length; i++) {
            Button colorButton = new Button("pick color");
            colorButton.setPrefSize(120,50);
            colorButton.setId("guessButton");
            int index = i;
            colorButton.setOnAction(e -> pickColor(colorButton,index) );
            guessGrid.add(colorButton,i,0);
        }

        Button submitButton = new Button("Submit");
        submitButton.setId("submitButton");
        submitButton.setOnAction(e -> submitGuess());
        feedbackLabel = new Label();
        feedbackLabel.setFont( new javafx.scene.text.Font("Arial",14));
        HBox timerBox = new HBox(20);
        timerBox.setAlignment(Pos.TOP_LEFT);
        timerBox.getChildren().add(timerLabel);
        mainLayout.getChildren().addAll(titleLabel,instructionLabel,timerBox,guessGrid,submitButton,feedbackLabel);
        return mainLayout;

    }

    public void startTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1),e-> {
            seconds++;
            if (seconds == 60) {
                seconds = 0;
                minutes++;
            }
            updateTimerLabel();
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private void updateTimerLabel() {
        String time = String.format("%02d:%02d",minutes,seconds);
        timerLabel.setText(time);
    }

    private void generateSecretCode() {
        secretCode = new ArrayList<>(colors);
        Collections.shuffle(secretCode);
        secretCode = secretCode.subList(0, code_length);
        System.out.println(secretCode);
        System.out.println(secretCode);
    }

    private void pickColor (Button button, int index) {
        Color chosenColor = choseRandomColor();

        String realColor = switch (chosenColor.toString()) {
            case "0xff0000ff" -> "#FF0000"; // red
            case "0x0000ffff" -> "#0000FF"; // blue
            case "0x008000ff" -> "#00FF00"; // green
            case "0xffff00ff" -> "#FFFF00"; // yellow
            case "0xffa500ff" -> "#FF9900"; // orange
            case "0xa52a2aff" -> "#783F04"; // brown
            default -> null;
        };

        button.setStyle("-fx-background-color: " + realColor + ";");
        guess[index] = Color.valueOf(chosenColor.toString());

    }

    private Color choseRandomColor() {
        Random rand = new Random();

        return colors.get(rand.nextInt(colors.size()));

    }

    private void submitGuess() {
        if (attempts < max_attempts) {
            boolean guessed = true;
            for (Color guess: guess) {
                if (guess == null) {
                    guessed = false;
                    break;
                }
            } // make sure that all qubes filled with colors
            if (!guessed) {
                feedbackLabel.setText("You Have Forgot To Guess Some qubes");
                return;
            }
            String feedback = givenFeedBack(guess);
            feedbackLabel.setText(feedback);
            attempts++;
            if (isCorrectGuess(guess)) {
                feedbackLabel.setText("Congratulations! You Won!");
                endGame(true);
            } else if (attempts >= max_attempts) {
                feedbackLabel.setText("Sorry, You Lost");
                endGame(false);
            }
        }
        System.out.println("guess :" + Arrays.toString(guess));
    }

    private void endGame(boolean win) {
        timer.stop();
        if (win) {
            resultLabel.setText("You Won In " + String.format("%02d:%02d",minutes,seconds));
            resultLabel.setTextFill(Color.GREEN);
        } else {
            resultLabel.setText("You Lost After " + String.format("%02d:%02d",minutes,seconds));
            resultLabel.setTextFill(Color.RED);
        }
    }

    private String givenFeedBack(Color[] guess) {
        int correctPosition = 0;
        int correctColor = 0;
        for (int i = 0; i < code_length; i++) {

            if (guess[i] != null && guess[i].equals(secretCode.get(i))) {
                correctPosition++;
            } else if (guess[i] != null && secretCode.contains(guess[i])) {
                correctColor++;
            }

        }

        return "Correct Position: " + correctPosition + " , Correct Color But Wrong Position: " + correctColor;
    }

    private boolean isCorrectGuess(Color[] guess) {
        return Arrays.equals(guess,secretCode.toArray());
    }

}
