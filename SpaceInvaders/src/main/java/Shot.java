import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

//José Eduardo Rodrigues Serpa - 20200311-7
//Henrique Barcellos Lima - 20204006-9
 

public class Shot extends BasicElement{
    public Object shooter;

    public Shot(int px,int py, Object shooter){
        super(px,py);
        this.shooter = shooter;
        setLargAlt(4, 16);
    }

    @Override
    public void start(){
        if(shooter instanceof Canhao){
            setDirV(-1);
        }else
        if(shooter instanceof Berserker){
            setDirV(1);
            setLargAlt(2, 10);
        }else 
        if(shooter instanceof Tanker){
            setDirV(1);
            setLargAlt(6, 32);
        }
        
        
        setSpeed(6);
    }

    @Override
    public void testaColisao(Character outro){
        // Não verifica colisão de um tiro com outro tiro
        if (outro instanceof Shot){
            return;
        }else{
            super.testaColisao(outro);
        }
    }

    @Override
    public void Update(long deltaTime){
        if (jaColidiu()){
            deactivate();
        }else{
            setPosY(getY() + getDirV() * getSpeed());
            // Se chegou na parte superior da tela ...
            if (getY() <= getLMinV() || getY() >= getLMaxV()){
                // Desaparece
                deactivate();
            }
        }
    }


    public void Draw(GraphicsContext graphicsContext){
        graphicsContext.setFill(Paint.valueOf("#FF0000"));
        graphicsContext.fillRect(getX(), getY(), getLargura(), getAltura());
    }
}
