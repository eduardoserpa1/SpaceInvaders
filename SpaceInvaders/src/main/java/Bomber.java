import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

/**
 * Represents a simple ball that crosses the screen over and over again
 * @author Bernardo Copstein and Rafael Copstein
 */
public class Bomber extends BasicElement{
    private char pelotao;
    private Canhao alvo;

    public Bomber(int px,int py,int dirH,Canhao canhao){
        super(px,py);
        setEnemy(true);
        alvo = canhao;
        altura=24;
        largura=24;
        if(dirH>0){
            pelotao = 'l';
            setDirH(1);
        }else{
            pelotao = 'r';
            setDirH(-1);
        }
        setDirV(-1);
    }

    @Override
    public void start(){
        if(pelotao == 'l'){
            setDirH(1);
        }else if(pelotao == 'r'){
            setDirH(-1);
        }
        setDirV(-1);
    }


    @Override
    public void Update(long deltaTime){
        if (jaColidiu()){
            Game.getInstance().incPontos();
            deactivate();
        }else{
            setPosX(getX() + getDirH() * (getSpeed()+1) );
            setPosY(getY() + getDirV() * getSpeed() );
            int px1 = this.getX();
            int px2 = px1 + getLargura();
            int canhao_px1 = alvo.getX();
            int canhao_px2 = canhao_px1 + alvo.getLargura();
            if(  (px1 >= canhao_px1 && px1 <= canhao_px2)  ||  (px2 >= canhao_px1 && px2 <= canhao_px2)){
                setDirH(0);
                setDirV(1);
                setSpeed(5);
            }
        }
    }
    public void rotaciona(){
        setDirH(getDirH() * (-1));
        setPosY(getY() + getAltura() + 5);
    }

    public void Draw(GraphicsContext graphicsContext){
        graphicsContext.setFill(Paint.valueOf("#0000FF")); 
        graphicsContext.fillOval(getX(), getY(), getLargura(), getAltura());
    }
}
