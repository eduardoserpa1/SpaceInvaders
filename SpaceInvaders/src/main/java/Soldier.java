import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

/**
 * Represents a simple ball that crosses the screen over and over again
 * @author Bernardo Copstein and Rafael Copstein
 */
public class Soldier extends BasicElement{
    private int life = 4;

    public Soldier(int px,int py){
        super(px,py);
        setEnemy(true);
        altura=50;
        largura=50;
        setSpeed(2);
        setPontos(6);
    }

    @Override
    public void start(){
        setDirH(1);
    }


    @Override
    public void Update(long deltaTime){
        if (jaColidiu()){
            if(life<=1){
                Game.getInstance().incPontos(this.pontos);
                deactivate();
            }else{
                life--;
                colidiu=false;
                return;
            }
        }else{
            setPosX(getX() + getDirH() * getSpeed());
            // Se chegou no lado direito da tela ...
            if (getX() >= getLMaxH() - getLargura() || getX() <= getLMinH()){
                // Reposiciona no lado esquerdo e ...
                
                setPosY(getY()+64);
                setDirH(getDirH() * (-1));
                setPosX(getX() + getDirH() * 10);

                //setPosX(getLMinH());
                // Sorteia o passo de avanço [1,4]
                //setSpeed(Params.getInstance().nextInt(4)+1);
            }
        }
    }

    public void Draw(GraphicsContext graphicsContext){
        graphicsContext.setFill(Paint.valueOf("#FFFF00")); 
        graphicsContext.fillOval(getX(), getY(), getLargura(), getAltura());
    }
}
