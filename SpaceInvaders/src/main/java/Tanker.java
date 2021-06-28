import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

/**
 * Represents a simple ball that crosses the screen over and over again
 * @author Bernardo Copstein and Rafael Copstein
 */
public class Tanker extends BasicElement{
    private Canhao alvo;

    public Tanker(int px,int py,Canhao canhao){
        super(px,py);
        alvo = canhao;
        setEnemy(true);
        altura=64;
        largura=64;
        setSpeed(1);
        setDirH(1);
    }

    @Override
    public void start(){
        setDirH(1);
    }


    @Override
    public void Update(long deltaTime){
        if (jaColidiu()){
            Game.getInstance().incPontos();
            deactivate();
        }else{
            setPosX(getX() + getDirH() * getSpeed());
         
            if (getX() >= getLMaxH() || getX() <= getLMinH()){
              
                setDirH(getDirH() * (-1));
               
            }
        }
    }

    public void Draw(GraphicsContext graphicsContext){
        graphicsContext.setFill(Paint.valueOf("#FFFF00")); 
        graphicsContext.fillOval(getX(), getY(), getLargura(), getAltura());
    }
}
