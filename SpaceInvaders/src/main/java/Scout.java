import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

/**
 * Represents a simple ball that crosses the screen over and over again
 * @author Bernardo Copstein and Rafael Copstein
 */
public class Scout extends BasicElement{
    private char pelotao;

    public Scout(int px,int py,int dirH){
        super(px,py);
        setEnemy(true);
        setSpeed(2);
        setPontos(2);
        if(dirH>0){
            pelotao = 'l';
            //setDirH(1);
        }else{
            pelotao = 'r';
            //setDirH(-1); 
        }
    }

    @Override
    public void start(){
        if(pelotao == 'l'){
            setDirH(1);
        }else if(pelotao == 'r'){
            setDirH(-1);
        }
    }


    @Override
    public void Update(long deltaTime){
        if (jaColidiu()){
            Game.getInstance().incPontos(getPontos());
            deactivate();
        }else{
            setPosX(getX() + getDirH() * getSpeed()); 

            if(pelotao == 'l'){
                if(getX()+getLargura() >= getLMaxH()/2 || getX() <= getLMinH())
                    rotaciona();
            }else if(pelotao == 'r'){
                if(getX() <= getLMaxH()/2 || getX()+getLargura() == getLMaxH() )
                    rotaciona();
            }
            
            
        }
    }
    public void rotaciona(){
        setDirH(getDirH() * (-1));
        setPosY(getY() + getAltura() + 5);
    }

    public void Draw(GraphicsContext graphicsContext){
        graphicsContext.setFill(Paint.valueOf("#FF0000")); 
        graphicsContext.fillOval(getX(), getY(), getLargura(), getAltura());
    }
}
