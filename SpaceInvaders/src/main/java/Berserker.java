import java.io.IOException;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

//José Eduardo Rodrigues Serpa - 20200311-7
//Henrique Barcellos Lima - 20204006-9

public class Berserker extends Tanker{

    private Animator anime;

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

        anime = new Animator("berserker");
        try { 
            anime.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void Update(long deltaTime){
        super.Update(deltaTime);
        if (getX()+getLargura() >= getLMaxH() || getX() <= getLMinH()){
              
            setPosY(getY() + getAltura() + 10);
           
        }
    }

    public void Draw(GraphicsContext graphicsContext){
        graphicsContext.drawImage(anime.updateSprite(10),(double)getX(), (double)getY(), (double)largura, (double)altura);
    }
}
