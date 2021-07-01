import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Handles the game lifecycle and behavior
 * @author Bernardo Copstein and Rafael Copstein
 */
public class Game {
    private static Game game = null;
    private Canhao canhao;
    private List<Character> activeChars;
    private Stack<String> preview_wave;
    private boolean gameOver;
    private int pontos;
    private int wave=1;
    private int frame = 0;
    private int spawner = 1;

    private Game(){
        gameOver = false;
        pontos = 0;
    }

    public void setGameOver(){
        gameOver = true;
    }

    public boolean isGameOver(){
        return gameOver;
    }

    public int getPontos(){
        return pontos;
    }

    public void incPontos(int pts){
        pontos += pts;
    }

    public static Game createNewInstance(){
        game = new Game();
        return game;
    }
    public static Game getInstance(){
        if (game == null){
            game = new Game();
        }
        return(game);
    }

    public void addChar(Character c){
        activeChars.add(c);
        c.start();
    }

    public void eliminate(Character c){
        activeChars.remove(c);
    }

    public void Start() {
        // Repositório de personagens
        activeChars = new LinkedList<>();
        preview_wave = new Stack<>();
        // Adiciona o canhao
        canhao = new Canhao(Params.WINDOW_WIDTH/2-32,Params.WINDOW_HEIGHT-90,4);
        activeChars.add(canhao);

        for(Character c:activeChars){
            c.start();
        }

        loadWaves();
    }

    public void Update(long currentTime, long deltaTime) {
        if (gameOver){
            return;
        }

        frame++;
        sinc_wave();

        for(int i=0;i<activeChars.size();i++){
            Character este = activeChars.get(i);
            este.Update(deltaTime);
            for(int j =0; j<activeChars.size();j++){
                Character outro = activeChars.get(j);
                if (este != outro){
                    este.touchBottom();
                    este.testaColisao(outro);
                }
            }
        }
    }
    public void sinc_wave(){

        boolean second025 = frame%15==0;
        boolean second05 = frame%30==0;
        boolean second1 = frame%60==0;
        boolean second2 = frame%120==0;

        Object enemy = activeChars.get(spawner-1);

        if(activeChars.size()==1){
            System.out.println("Wave " + wave + " liberada!");
            generateWave(); 
            wave++;
        }else{
            if(spawner>1){
                if(enemy instanceof Tanker){
                    if(second2){
                        activeChars.get(spawner-1).start();
                        spawner--;
                    }
                }else
                if(enemy instanceof Soldier){
                    if(second05){
                        activeChars.get(spawner-1).start();
                        spawner--;
                    }
                }else
                if(enemy instanceof Scout){
                    if(second025){
                        activeChars.get(spawner-1).start();
                        spawner--;
                    }
                }else 
                if(enemy instanceof Berserker){
                    if(second025){
                        activeChars.get(spawner-1).start();
                        spawner--;
                    }
                }else{
                    if(second05){
                        activeChars.get(spawner-1).start();
                        spawner--;
                    }
                }
                
            }
        }
    }

    public void generateWave(){

        int qtd_berserker,qtd_scout,qtd_soldier,qtd_tanker;

        String[] str = preview_wave.pop().split("-");

        qtd_berserker = Integer.parseInt(str[0]);
        qtd_scout = Integer.parseInt(str[1]);
        qtd_soldier = Integer.parseInt(str[2]);
        qtd_tanker =  Integer.parseInt(str[3]);

        System.out.println(qtd_berserker+" - "+qtd_scout+" - "+qtd_soldier+" - "+qtd_tanker);
        
        
        for (int i = 0; i < qtd_scout; i++) {
            activeChars.add(new Scout(Params.LEFT_BORDER, Params.EDGE_Y_TOP, 1));
            activeChars.add(new Scout(Params.RIGHT_BORDER - 24, Params.EDGE_Y_TOP, -1));
            spawner += 2;
        }
        for (int i = 0; i < qtd_berserker; i++) {
            activeChars.add(new Berserker(Params.LEFT_BORDER, Params.EDGE_Y_TOP, canhao));
            spawner++;
        }
        for (int i = 0; i < qtd_tanker; i++) {
            activeChars.add(new Tanker(Params.LEFT_BORDER, 10, canhao));
            spawner++;
        }
        for (int i = 0; i < qtd_soldier; i++) {
            activeChars.add(new Soldier(Params.LEFT_BORDER, Params.EDGE_Y_TOP));
            spawner++;
        }   
    
    }

    public void loadWaves() {

        Path path2 = getPath("waves");

        try (Scanner sc = new Scanner(Files.newBufferedReader(path2, 
                                            Charset.defaultCharset()))){
            while(sc.hasNextLine()) {
                String line = sc.nextLine();
                preview_wave.push(line);
                System.out.println(line);
            }
        }catch (IOException x){
               System.err.format("Erro de E/S: %s%n", x);
        }
    
    }

    public Path getPath(String file){
        String currDir = Paths.get("").toAbsolutePath().toString();
        String nameComplete = currDir+"\\src\\main\\files\\"+ file +".dat";
        Path path = Paths.get(nameComplete);
        return path;
    }

    public void OnInput(KeyCode keyCode, boolean isPressed) {
        if(canhao != null)
            canhao.OnInput(keyCode, isPressed);
    }

    public void Draw(GraphicsContext graphicsContext) {
        if(Main.isPaused) return;
        for(Character c:activeChars){
            c.Draw(graphicsContext);
        }
    }
}
