import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

/**
 * Represents a simple ball that crosses the screen over and over again
 * @author Bernardo Copstein and Rafael Copstein
 */
public class Enemy2 extends BasicElement{
    private char pelotao;

    public Enemy2(int px,int py,int dirH){
        super(px,py);
        altura=24;
        largura=24;
        if(dirH>0){
            pelotao = 'l';
            setDirH(1);
        }else{
            pelotao = 'r';
            setDirH(-1);
        }
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
            // Se chegou no lado direito da tela ...
            
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