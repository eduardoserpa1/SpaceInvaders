import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

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
import java.nio.charset.Charset;
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
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;


/**
 * Handles window initialization and primary game setup
 * @author Bernardo Copstein, Rafael Copstein
 */

public class Main extends Application {

    public static boolean isPaused = true;
    public static LinkedHashMap<String,Integer> ranking;
    private boolean saving_score=false;
    private int score_to_save = 0;

    @Override
    public void start(Stage stage) throws Exception {
        // Initialize Window
        stage.setTitle(Params.WINDOW_TITLE);
        stage.setResizable(false);

        ranking = loadRanking();

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

                gc.fillText(" SCORE: "+Game.getInstance().getPontos(), 10, 10);
                Game.getInstance().Draw(gc);

                if (Game.getInstance().isGameOver()){
                    score_to_save = Game.getInstance().getPontos();
                    Game.createNewInstance().Start();
                    registerInputs(scene);
                    Main.isPaused = true;
                    saving_score = true;
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
    
    public void Menu(Group root,Scene scene){

        VBox nr = new VBox(10);

        GridPane grid = new GridPane();

        registerInputs(scene);
        //grid.setBackground(new Background(new BackgroundFill(Color.PURPLE, CornerRadii.EMPTY, Insets.EMPTY)));

        grid.setMinSize(Params.WINDOW_WIDTH, Params.WINDOW_HEIGHT); 

        grid.setPadding(new Insets(10, 10, 10, 10));
        
        grid.setVgap(5);
        grid.setHgap(5); 


        if(!saving_score)
            setMainMenuLayout(grid);
        else
            setGameOverLayout(grid);
        
        grid.setAlignment(Pos.CENTER);
        nr.getChildren().add(grid);
        root.getChildren().add(nr);
    }

    public void setRankingMenuLayout(GridPane g){
        g.getChildren().clear();
        Text title = new Text("RANKING");
        Button back = new Button("VOLTAR AO MENU");
        title.setFill(Color.WHITE);
        title.setFont(Font.font(null, FontWeight.BOLD, 55));
        title.setTextAlignment(TextAlignment.CENTER);
        GridPane.setConstraints(title, 0, 0,1,1,HPos.CENTER,VPos.CENTER);
        g.add(title, 0, 0, 2, 1);
        Text keyHEADER = new Text("PLAYER");
        Text valueHEADER = new Text("SCORE");
        cssText(keyHEADER);
        cssText(valueHEADER);
        cssButton(back);
        GridPane.setConstraints(keyHEADER, 0, 1,1,1,HPos.LEFT,VPos.CENTER);
        GridPane.setConstraints(valueHEADER, 1, 1,1,1,HPos.RIGHT,VPos.CENTER);
        g.add(keyHEADER, 0, 1);
        g.add(valueHEADER, 1, 1);
        
        int i=2;
        for(Map.Entry<String, Integer> entrada : ranking.entrySet()){
            Text key = new Text((i-1)+"- "+entrada.getKey().toUpperCase());
            Text value = new Text( entrada.getValue().toString() );
            cssText(key);
            cssText(value);
            GridPane.setConstraints(key, 0, i,1,1,HPos.LEFT,VPos.CENTER);
            GridPane.setConstraints(value, 1, i,1,1,HPos.RIGHT,VPos.CENTER);
            g.add(key,0,i);
            g.add(value,1,i);
            i++;
        }

        

        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
                public void handle(ActionEvent arg0) {
                    setMainMenuLayout(g);
                }
             });

        g.add(back, 0, i+1,2,1);
    }
    
    public void setSaveScoreMenuLayout(GridPane g){
        g.getChildren().clear();
        TextField nome = new TextField();
        Button save = new Button();
        save.setText("SALVAR");
        cssTextField(nome);
        cssButton(save);
        GridPane.setConstraints(nome, 0, 0);
        GridPane.setConstraints(save, 0, 1);
        g.getChildren().add(nome);
        g.getChildren().add(save);
        save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
                public void handle(ActionEvent arg0) {
                    saveScore(nome.getText(),score_to_save);
                    setRankingMenuLayout(g);
                }
             });
    }
    public void saveScore(String nome, int score){
        
        LinkedList keys = new LinkedList<String>();
        LinkedList values = new LinkedList<Integer>();

        for(Map.Entry<String, Integer> entrada : ranking.entrySet()){
            keys.add(entrada.getKey());
            values.add(entrada.getValue());
        }
        for(Object v:values){
            System.out.println(v.toString());
        }
    }

    public void setGameOverLayout(GridPane g){
        g.getChildren().clear();
        setSaveScoreMenuLayout(g);
    }

    public void setMainMenuLayout(GridPane g){
        g.getChildren().clear();
        Button start = new Button("INICIAR NOVO JOGO");
        Button rank = new Button("RANKING");

        cssButton(start);
        cssButton(rank);

        start.setOnAction(new EventHandler<ActionEvent>() {
            @Override
                public void handle(ActionEvent arg0) {
                    Game.createNewInstance().Start();
                    //Main.jogador = nome.getText();
                    g.getChildren().clear();
                    Main.isPaused = false;
                }
             });
        rank.setOnAction(new EventHandler<ActionEvent>() {
            @Override
                public void handle(ActionEvent e) {
                    setRankingMenuLayout(g);
                }
        });

        
        g.addColumn(0, start, rank);
        
    }

    public void cssText(Text t){
        t.setFont(Font.font(null, FontWeight.BOLD, 25));
        t.setFill(Color.WHITE);
    }

    public void cssTextField(TextField t){
        t.setStyle("-fx-max-width: infinity;");
        t.setMinWidth(Params.WINDOW_WIDTH/2);
        t.setMinHeight(40);
        t.setCursor(Cursor.TEXT);
        t.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        t.setAlignment(Pos.CENTER);
        t.setFont(Font.font(null, FontWeight.BOLD, 20));
        t.setStyle("-fx-text-fill: white;");
        t.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
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

    public void registerInputs(Scene scene){
        scene.setOnKeyPressed((KeyEvent event) -> {
            Game.getInstance().OnInput(event.getCode(), true);
        });

        scene.setOnKeyReleased((KeyEvent event) -> {
            Game.getInstance().OnInput(event.getCode(), false);
        });
    }

    public LinkedHashMap<String,Integer> loadRanking() {

        Path path2 = getPath("ranking");

        LinkedHashMap<String,Integer> map = new LinkedHashMap<String,Integer>();

        try (Scanner sc = new Scanner(Files.newBufferedReader(path2, Charset.defaultCharset()))){
            while(sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] str = line.split("-");
                String key = str[0];
                Integer value = Integer.parseInt(str[1]);
                map.put(key, value);
                System.out.println(key+"-"+value);
            }
        }catch (IOException x){
               System.err.format("Erro de E/S: %s%n", x);
        }
        return map;
    }

    public Path getPath(String file){
        String currDir = Paths.get("").toAbsolutePath().toString();
        String nameComplete = currDir+"\\src\\main\\files\\"+ file +".dat";
        Path path = Paths.get(nameComplete);
        return path;
    }
    
    public static void runBackgroundAnimation(Image img,GraphicsContext gc){
        gc.drawImage(img,0,0,Params.WINDOW_WIDTH, Params.WINDOW_HEIGHT);
    }

    public static void main(String args[]) {
        launch();

    }
}
