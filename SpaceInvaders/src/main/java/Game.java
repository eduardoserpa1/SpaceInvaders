import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import java.util.List;
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
    int frame=0;

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

    public void incPontos(){
        pontos++;
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

        // Adiciona o canhao
        canhao = new Canhao(350,510,4);
        activeChars.add(canhao);

        // Adiciona bolas
        for(int i=0; i<20; i++){
            //activeChars.add(new Soldier(50+(i*50),10));
            
        }
        for(int i=0; i<30; i++){
            //activeChars.add(new Enemy2(50+(i*50),100));
        }
       

        for(Character c:activeChars){
            c.start();
        }
    }

    public void Update(long currentTime, long deltaTime) {
        if (gameOver){
            return;
        }

        frame++;

        if(frame%60==0)
            setWave(frame/60);

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
    public void setWave(int sec){
        if(sec%3==0){
            activeChars.add(new Scout(Params.LEFT_BORDER,100,1));
            activeChars.add(new Scout(Params.RIGHT_BORDER-24,100,-1));  
            
            activeChars.add(new Tanker(Params.LEFT_BORDER,10,canhao));
        }
        if(sec%5==0)
            activeChars.add(new Soldier(Params.LEFT_BORDER,10));

        if(sec%2==0){
            activeChars.add(new Bomber(Params.RIGHT_BORDER,500,-1,canhao));
            activeChars.add(new Bomber(Params.LEFT_BORDER,500,1,canhao));
        }
        
        
            
    }

    public void OnInput(KeyCode keyCode, boolean isPressed) {
        canhao.OnInput(keyCode, isPressed);
    }

    public void Draw(GraphicsContext graphicsContext) {
        for(Character c:activeChars){
            c.Draw(graphicsContext);
        }
    }
}
