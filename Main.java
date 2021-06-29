import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Handles window initialization and primary game setup
 * @author Bernardo Copstein, Rafael Copstein
 */

public class Main extends Application {
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

        new AnimationTimer()
        {
            long lastNanoTime = System.nanoTime();
            int screenroll=0;
            @Override
            public void handle(long currentNanoTime)
            {
                long deltaTime = currentNanoTime - lastNanoTime;

                Game.getInstance().Update(currentNanoTime, deltaTime);
                
                gc.drawImage(img,0,screenroll,Params.WINDOW_WIDTH, Params.WINDOW_HEIGHT);
                gc.drawImage(img,0, (screenroll - Params.WINDOW_HEIGHT) ,Params.WINDOW_WIDTH, Params.WINDOW_HEIGHT);

                gc.fillText("Pontos: "+Game.getInstance().getPontos(), 10, 10);
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


    public static void main(String args[]) {
        launch();
    }
}
