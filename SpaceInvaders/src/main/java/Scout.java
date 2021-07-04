import java.io.IOException;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

//José Eduardo Rodrigues Serpa - 20200311-7
//Henrique Barcellos Lima - 20204006-9

public class Scout extends BasicElement{
    private char pelotao;
 
    private Animator anime;

    public Scout(int px,int py,int dirH){
        super(px,py);
        setEnemy(true);
        setSpeed(2);
        setPontos(2);
        if(dirH>0){
            pelotao = 'l';
        }else{
            pelotao = 'r';
        }
        anime = new Animator("scout");
        try { 
            anime.load();
        } catch (IOException e) {
            e.printStackTrace();
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
        graphicsContext.drawImage(anime.updateSprite(10),(double)getX(), (double)getY(), (double)largura, (double)altura);
    }
}
