import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Handles window initialization and primary game setup
 * @author Bernardo Copstein, Rafael Copstein
 */

public class Main extends Application {

    public static boolean isPaused = true;
    public static String jogador = "";
    @Override
    public void start(Stage stage) throws Exception {
        // Initialize Window
        stage.setTitle(Params.WINDOW_TITLE);
        stage.setResizable(false);

        Image img = new Image("background\\bg.png",Params.WINDOW_WIDTH,Params.WINDOW_HEIGHT,true,true);

        Group root = new Group();
        Scene scene = new Scene( root );
        stage.setScene( scene );

        Canvas canvas = new Canvas(Params.WINDOW_WIDTH, Params.WINDOW_HEIGHT );

        root.getChildren().add( canvas );

        // Setup Game object
        Game.getInstance().Start();

        // Register User Input Handler
        scene.setOnKeyPressed((KeyEvent event) -> {
            Game.getInstance().OnInput(event.getCode(), true);
        });

        scene.setOnKeyReleased((KeyEvent event) -> {
            Game.getInstance().OnInput(event.getCode(), false);
        });

        // Register Game Loop
        final GraphicsContext gc = canvas.getGraphicsContext2D();
        //menu
        VBox nr = new VBox(10);
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(250, 0, 0, 340));
        grid.setVgap(5);
        grid.setHgap(5);
        Button start = new Button("Start game");
        Button rank = new Button("Ranking");
        start.setOnAction(new EventHandler<ActionEvent>() {
            @Override
                public void handle(ActionEvent e) {
                    grid.getChildren().clear();
                    TextField nome = new TextField();
                    Button play = new Button();
                    play.setText("Jogar");
                    GridPane.setConstraints(nome, 0, 0);
                    GridPane.setConstraints(play, 1, 0);
                    grid.getChildren().add(nome);
                    grid.getChildren().add(play);
                    play.setOnAction(new EventHandler<ActionEvent>(){

                        @Override
                        public void handle(ActionEvent arg0) {
                            Main.jogador = nome.getText();
                            grid.getChildren().clear();
                            Main.isPaused = false;
                        }
                        
                    });
                 }
             });
        GridPane.setConstraints(start, 0, 0);
        grid.getChildren().add(start);
        GridPane.setConstraints(rank, 0, 1);
        grid.getChildren().add(rank);
        nr.getChildren().add(grid);
        root.getChildren().add(nr);
        //end menu

        new AnimationTimer()
        {
            long lastNanoTime = System.nanoTime();
            int screenroll=0;
            @Override
            public void handle(long currentNanoTime)
            {
                long deltaTime = currentNanoTime - lastNanoTime;

                if (!Main.isPaused) Game.getInstance().Update(currentNanoTime, deltaTime);
                
                gc.drawImage(img,0,screenroll,Params.WINDOW_WIDTH, Params.WINDOW_HEIGHT);
                gc.drawImage(img,0, (screenroll - Params.WINDOW_HEIGHT) ,Params.WINDOW_WIDTH, Params.WINDOW_HEIGHT);

                gc.fillText("Jogador: " + Main.jogador + " Pontos: "+Game.getInstance().getPontos(), 10, 10);
                Game.getInstance().Draw(gc);

                if (Game.getInstance().isGameOver()){
                    stop();

                }

                if(screenroll >= Params.WINDOW_HEIGHT)
                    screenroll=0;
                screenroll++;
                
                lastNanoTime = currentNanoTime;
            }

        }.start();

        // Show window
        stage.show();
    }
    
    public static void runBackgroundAnimation(Image img,GraphicsContext gc){
        gc.drawImage(img,0,0,Params.WINDOW_WIDTH, Params.WINDOW_HEIGHT);
    }

    public static void main(String args[]) {
        launch();

    }
}
