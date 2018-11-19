package reversi_gui;

import javafx.application.Application;

import java.io.File;
import java.io.IOException;
import java.util.*;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import reversi.ReversiException;
import reversi2.Board;
import reversi2.NetworkClient;

/**
 * This application is the UI for Reversi.
 *
 * @author Michael Jansen
 */
public class GUI_Client2 extends Application implements Observer {
    private NetworkClient serverConn;
    private Board model;
    private Label moveCount;
    private Label gameStatus;
    private Button[][] buttons;
    private Image emptyBackground;
    private Image p1Background;
    private Image p2Background;

    /**
     * Create the board model, create the network connection based on
     * command line parameters, and use the first message received to
     * allocate the board size the server is also using.
     */
    @Override
    public void init() {
        try {
            // Get host info from command line
            List<String> args = getParameters().getRaw();

            // get host info and username from command line
            String host = args.get(0);
            int port = Integer.parseInt(args.get(1));

            model = new Board();
            serverConn = new NetworkClient(host, port, model);
            model.initializeGame();

            emptyBackground = new Image(getClass().getResourceAsStream("empty.jpg"));
            p1Background = new Image(getClass().getResourceAsStream("p1.jpg"));
            p2Background = new Image(getClass().getResourceAsStream("p2.jpg"));
        }catch (ReversiException e){
            e.printStackTrace();
        }
    }

    @Override
    public void start( Stage mainStage ) {
        model.addObserver(this);
        buttons = new Button[model.getDIM()][model.getDIM()];

        BorderPane layout = new BorderPane();

        GridPane grid = new GridPane();
        for(int i = 0; i < model.getDIM(); i++) {
            for(int j = 0; j < model.getDIM(); j++) {
                int row = i;
                int col = j;
                buttons[row][col] = new Button();
                buttons[row][col].setOnAction((event -> serverConn.sendMove(row, col)));
                grid.add(buttons[row][col], row, col);
            }
        }

        HBox gameInfo = new HBox();
        moveCount =  new Label();
        gameStatus = new Label();
        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        gameInfo.setPadding(new Insets(5));
        gameInfo.getChildren().addAll(moveCount, spacer, gameStatus);

        layout.setCenter(grid);
        layout.setBottom(gameInfo);

        mainStage.setScene(new Scene(layout));
        mainStage.setTitle("Reversi Client");

        this.refreshGUI();
        mainStage.sizeToScene();
        mainStage.setResizable(false);
        mainStage.show();
    }

    /**
     * A method used to update the GUI when the model sends an update
     */
    private void refreshGUI(){
        updateText();
        updateButtons();
    }

    /**
     * Updates the information text to reflect the current game status
     * and number of moves remaining
     */
    private void updateText(){
        moveCount.setText(model.getMovesLeft() + ((model.getMovesLeft() == 1) ? " move remaining. " : " moves remaining."));
        switch (model.getStatus()){
            case I_LOST:
                gameStatus.setText("YOU LOST!");
                break;
            case I_WON:
                gameStatus.setText("YOU WON!");
                break;
            case TIE:
                gameStatus.setText("TIE GAME!");
                break;
            case ERROR:
                gameStatus.setText("ERROR");
                break;
            case NOT_OVER:
                if(model.isMyTurn()){
                    gameStatus.setText("YOUR TURN! MAKE A MOVE!");
                }else{
                    gameStatus.setText("WAITING FOR OPPONENT");
                }
                break;
        }
    }

    /**
     * Update all of the buttons to reflect which ones are valid moves, invalid moves,
     * or occupied by a player
     */
    private void updateButtons(){
        for(int i = 0; i < model.getDIM(); i++){
            for(int j = 0; j < model.getDIM(); j++){
                Board.Move move = model.getContents(i, j);
                buttons[i][j].setDisable(true);
                switch (move){
                    case PLAYER_ONE:
                        buttons[i][j].setGraphic(new ImageView(p1Background));
                        break;
                    case PLAYER_TWO:
                        buttons[i][j].setGraphic(new ImageView(p2Background));
                        break;
                    case NONE:
                        if(model.isValidMove(i, j) && model.isMyTurn()){
                            buttons[i][j].setDisable(false);
                        }
                        buttons[i][j].setGraphic(new ImageView(emptyBackground));
                        break;
                }
            }
        }
    }

    @Override
    public void stop(){
        this.serverConn.close();
    }

    /**
     * Launch the JavaFX GUI.
     *
     * @param args not used, here, but named arguments are passed to the GUI.
     *             <code>--host=<i>hostname</i> --port=<i>portnum</i></code>
     */
    public static void main( String[] args ) {
        if (args.length != 2) {
            System.out.println("Usage: java GUI_Client2 host port");
            System.exit(0);
        } else {
            Application.launch(args);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        Platform.runLater(this::refreshGUI);
    }
}
