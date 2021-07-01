import javax.swing.text.StyledEditorKit.BoldAction;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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

        Menu(root,scene);

        final GraphicsContext gc = canvas.getGraphicsContext2D();

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
                    Game.createNewInstance().Start();
                    registerInputs(scene);
                    Main.isPaused = true;
                    Menu(root,scene);
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
    public void registerInputs(Scene scene){
        scene.setOnKeyPressed((KeyEvent event) -> {
            Game.getInstance().OnInput(event.getCode(), true);
        });

        scene.setOnKeyReleased((KeyEvent event) -> {
            Game.getInstance().OnInput(event.getCode(), false);
        });
    }

    public void setMainMenu(GridPane g){

    }
    
    public void setPlayMenu(GridPane g){
        g.getChildren().clear();
        TextField nome = new TextField();
        Button play = new Button();
        play.setText("Jogar");
        GridPane.setConstraints(nome, 0, 0);
        GridPane.setConstraints(play, 0, 1);
        g.getChildren().add(nome);
        g.getChildren().add(play);
    }

    public void Menu(Group root,Scene scene){

        VBox nr = new VBox(10);

        GridPane grid = new GridPane();


        //grid.setBackground(new Background(new BackgroundFill(Color.WHEAT, CornerRadii.EMPTY, Insets.EMPTY)));

        grid.setMinSize(Params.WINDOW_WIDTH, Params.WINDOW_HEIGHT); 

        grid.setPadding(new Insets(10, 10, 10, 10));
        
        grid.setVgap(5);
        grid.setHgap(5); 

        Button start = new Button("INICIAR NOVO JOGO");
        Button rank = new Button("RANKING");

        cssButton(start);
        cssButton(rank);

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
                            Game.createNewInstance().Start();
                            registerInputs(scene);
                            Main.jogador = nome.getText();
                            grid.getChildren().clear();
                            Main.isPaused = false;
                        }
                        
                    });
                 }
             });
             rank.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                    public void handle(ActionEvent e) {
                        grid.getChildren().clear();
                    
                        Text title = new Text("RANKING");
                        title.setFill(Color.WHITE);
                        title.setFont(Font.font(null, FontWeight.BOLD, 55));
                        
                        grid.getChildren().add(title);
                    
                     }
                 });

        
        grid.addColumn(0, start, rank);
        grid.setAlignment(Pos.CENTER);
        nr.getChildren().add(grid);
        root.getChildren().add(nr);
    }

    public void cssButton(Button b){
        b.setStyle("-fx-max-width: infinity;");
        b.setMinWidth(Params.WINDOW_WIDTH/2);
        b.setMinHeight(40);
        b.setCursor(Cursor.HAND);
        b.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        b.setTextFill(Color.WHITE);
        b.setFont(Font.font(null, FontWeight.BOLD, 20));
        b.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        b.setOnMouseEntered(e -> b.setStyle(Params.HOVERED_BUTTON_STYLE));
        b.setOnMouseExited(e -> b.setStyle(Params.IDLE_BUTTON_STYLE));
    }
    
    public static void runBackgroundAnimation(Image img,GraphicsContext gc){
        gc.drawImage(img,0,0,Params.WINDOW_WIDTH, Params.WINDOW_HEIGHT);
    }

    public static void main(String args[]) {
        launch();

    }
}
