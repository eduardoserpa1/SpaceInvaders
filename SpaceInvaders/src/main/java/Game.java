import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Handles the game lifecycle and behavior
 * @author Bernardo Copstein and Rafael Copstein
 */
public class Game {
    private static Game game = null;
    private Canhao canhao;
    private List<Character> activeChars;
    private boolean gameOver;
    private int pontos;
    private static Map<String,Integer> ranking;
    private Main inicio;

    private Game(){
        gameOver = false;
        pontos = 0;
        ranking = new HashMap<String,Integer>();
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

    public void incPontos(){
        pontos++;
    }

    public void resetPonts(){
        pontos = 0;
    }

    public static void loadRankings(){
        try {
            List<String> conteudo = Files.readAllLines(Paths.get("","rankings.txt"));
            for(String linha : conteudo){
                String[] nome_score = linha.split(","); //pos 0 = nome, pos 1 = score
                ranking.put(nome_score[0],Integer.parseInt(nome_score[1]));
            }
        }
        catch (IOException e){}
    }

    public static Game getInstance(){
        if (game == null){
            game = new Game();
            loadRankings();
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

    public void Start(Main inicio) {
        this.inicio = inicio;
        // Repositório de personagens
        activeChars = new LinkedList<>();

        // Adiciona o canhao
        canhao = new Canhao(350,510);
        activeChars.add(canhao);

        // Adiciona bolas
        for(int i=0; i<7; i++){
            activeChars.add(new Ball(100+(1*32),60+i*32));
         
            activeChars.add(new Ball(100+(6*32),60+i*32));
        }
        //activeChars.add(new Ball(100,60));

        // Adiciona pinguim
        //activeChars.add(new Pinguim(100,270));
        //activeChars.add(new Pinguim(10,300));

        for(Character c:activeChars){
            c.start();
        }
    }

    public void Update(long currentTime, long deltaTime) {
        if (gameOver){
            return;
        }

        for(int i=0;i<activeChars.size();i++){
            Character este = activeChars.get(i);
            este.Update(deltaTime);
            for(int j =0; j<activeChars.size();j++){
                Character outro = activeChars.get(j);
                if (este != outro){
                    este.testaColisao(outro);
                }
            }
        }
    }

    public void saveGame(){
        Main.isPaused = true;
        ranking.put(Main.jogador, this.getPontos());

        Map<String, Integer> ordenado = new LinkedHashMap<>();
        ranking.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEachOrdered(x -> ordenado.put(x.getKey(), x.getValue()));
                
        String rankings = "";

        for (String jogador : ordenado.keySet()){
            int score = ordenado.get(jogador);
            String linha = jogador + "," + score;
            rankings += linha + "\n";
        }

        try {
            Files.write(Paths.get("","rankings.txt"), rankings.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        inicio.setupMainMenu(inicio.root, this);
    }

    public void OnInput(KeyCode keyCode, boolean isPressed) {
        if(keyCode == KeyCode.ESCAPE && isPressed){
            saveGame();
        }
        else{
           canhao.OnInput(keyCode, isPressed);
        }

    }

    public void Draw(GraphicsContext graphicsContext) {
        if(Main.isPaused) return;
        for(Character c:activeChars){
            c.Draw(graphicsContext);
        }
    }
}
