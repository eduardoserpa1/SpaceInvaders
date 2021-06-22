import java.io.IOException;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Paint;

/**
 * Represents the game Gun
 * @author Bernardo Copstein, Rafael Copstein
 */
public class Canhao extends BasicElement implements KeyboardCtrl{
    private int RELOAD_TIME = 100000000; // Time is in nanoseconds
    private int shot_timer = 0;

    private Animator anime;

    public Canhao(int px,int py){
        super(px,py);
        anime = new Animator("C:\\Users\\Eduardo\\Desktop\\Ambiente\\SpaceInvaders\\SpaceInvaders\\src\\main\\resources\\canon");
        try {
            anime.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        setLimH(20,Params.WINDOW_WIDTH-20);
        setLimV(Params.WINDOW_HEIGHT-100,Params.WINDOW_HEIGHT);
    }

    @Override
    public void Update(long deltaTime) {
        if (jaColidiu()){
            Game.getInstance().setGameOver();
        }
        setPosX(getX() + getDirH() * getSpeed());
        if (shot_timer > 0) shot_timer -= deltaTime;
    }

    @Override
    public void OnInput(KeyCode keyCode, boolean isPressed) {
        if (keyCode == KeyCode.LEFT){
            int dh = isPressed ? -1 : 0; 
            setDirH(dh);
        }
        if (keyCode == KeyCode.RIGHT){
            int dh = isPressed ? 1 : 0;
            setDirH(dh);
        }
        if (keyCode == KeyCode.SPACE){
            if (shot_timer <= 0) {
                Game.getInstance().addChar(new Shot(getX()+16,getY()-32));
                shot_timer = RELOAD_TIME;
            }
        }
        //if (keyCode == KeyCode.UP) do nothing
        //if (keyCode == KeyCode.DOWN) do nothing
    }

    @Override
    public int getAltura(){
        return 80;
    }

    @Override
    public int getLargura(){
        return 32;
    }

    @Override
    public void Draw(GraphicsContext graphicsContext) {
        graphicsContext.drawImage(anime.updateCenter(2),(double)getX(), (double)getY()+16, (double)64, (double)64);
        
        //graphicsContext.setFill(Paint.valueOf("#FF0000"));
        //graphicsContext.fillRect(getX(), getY()+16, 32, 32);
        //graphicsContext.fillRect(getX()+8, getY()-16, 16, 48);
        
    }
}
