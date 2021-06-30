import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

/**
 * Represents a simple ball that crosses the screen over and over again
 * @author Bernardo Copstein and Rafael Copstein
 */
public class Tanker extends BasicElement{
    private Canhao alvo;
    private int shot_timer = 0;
    protected int RELOAD_TIME = 60;
    protected int life = 7;

    public Tanker(int px,int py,Canhao canhao){
        super(px,py);

        this.alvo = canhao;
        this.altura=64;
        this.largura=64;

        setEnemy(true);
        setSpeed(1);
        setPontos(24);

        setPosY(getY() - 100);
    }

    @Override
    public void start(){
        setDirH(1);
        setPosY(getY() + 100);
    }


    @Override
    public void Update(long deltaTime){
        if (jaColidiu()){
            if(life<=1){
                Game.getInstance().incPontos(getPontos());
                deactivate();
            }else{
                life--;
                colidiu=false;
                return;
            }
        }else{
            setPosX(getX() + getDirH() * getSpeed());
            
            aim_shot();

            if (getX() >= getLMaxH() || getX()+getLargura() <= getLMinH()){
              
                setDirH(getDirH() * (-1));
               
            }
            if(getY() + getAltura() >= getLMaxV()){
                Game.getInstance().setGameOver();
            }

            if (shot_timer > 0) shot_timer -= 1;

        }
    }

    public void aim_shot(){
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
    }

    public void Draw(GraphicsContext graphicsContext){
        graphicsContext.setFill(Paint.valueOf("#FF00FF")); 
        graphicsContext.fillOval(getX(), getY(), getLargura(), getAltura());
    }
}
