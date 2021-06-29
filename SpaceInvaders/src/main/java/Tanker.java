import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

/**
 * Represents a simple ball that crosses the screen over and over again
 * @author Bernardo Copstein and Rafael Copstein
 */
public class Tanker extends BasicElement{
    private Canhao alvo;
    private int RELOAD_TIME = 60; // Time is in nanoseconds
    private int shot_timer = 0;
    private int life = 5;

    public Tanker(int px,int py,Canhao canhao){
        super(px,py);
        alvo = canhao;
        setEnemy(true);
        altura=64;
        largura=64;
        setSpeed(1);
        //setDirH(1);
    }

    @Override
    public void start(){
        setDirH(1);
    }


    @Override
    public void Update(long deltaTime){
        if (jaColidiu()){
            if(life<=1){
                Game.getInstance().incPontos();
                deactivate();
            }else{
                life--;
                colidiu=false;
                return;
            }
        }else{
            setPosX(getX() + getDirH() * getSpeed());
            
            int px1 = getX();
            int px2 = px1 + getLargura();
            int canhao_px1 = alvo.getX();
            int canhao_px2 = canhao_px1 + alvo.getLargura();
            if(  (px1 >= canhao_px1 && px1 <= canhao_px2)  ||  (px2 >= canhao_px1 && px2 <= canhao_px2)){
                if(shot_timer==0){
                    Game.getInstance().addChar(new Shot(getX() + (getLargura()/2),getY()+getAltura()+5,this));
                    shot_timer = RELOAD_TIME;
                }
            }

            if (getX() >= getLMaxH() || getX() <= getLMinH()){
              
                setDirH(getDirH() * (-1));
               
            }

            if (shot_timer > 0) shot_timer -= 1;

        }
    }

    public void Draw(GraphicsContext graphicsContext){
        graphicsContext.setFill(Paint.valueOf("#FF00FF")); 
        graphicsContext.fillOval(getX(), getY(), getLargura(), getAltura());
    }
}
