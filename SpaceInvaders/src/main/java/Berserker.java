import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

/**
 * Represents a simple ball that crosses the screen over and over again
 * @author Bernardo Copstein and Rafael Copstein
 */
public class Berserker extends Tanker{

    public Berserker(int px,int py, Canhao canhao){
        super(px,py,canhao);
        this.life = 2;
        this.altura=40;
        this.largura=40;
        this.RELOAD_TIME=40;
        setSpeed(3);
        setEnemy(true);
        setPontos(12);
        setPosY(Params.EDGE_Y_TOP);
    }

    @Override
    public void Update(long deltaTime){
        super.Update(deltaTime);
        if (getX() >= getLMaxH() || getX()+getLargura() <= getLMinH()){
              
            setPosY(getY() + getAltura() + 10);
           
        }
    }

    public void Draw(GraphicsContext graphicsContext){
        graphicsContext.setFill(Paint.valueOf("#FFFFFF")); 
        graphicsContext.fillOval(getX(), getY(), getLargura(), getAltura());
    }
}
