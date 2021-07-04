import java.io.IOException;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

//José Eduardo Rodrigues Serpa - 20200311-7
//Henrique Barcellos Lima - 20204006-9

public class Berserker extends Tanker{

    private Animator anime_1;
    private Animator anime_2;

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

        anime_1 = new Animator("berserker\\stage1");
        anime_2 = new Animator("berserker\\stage2");
        try { 
            anime_1.load();
            anime_2.load();
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
        if(life>1){
            graphicsContext.drawImage(anime_1.updateSprite(10),(double)getX(), (double)getY(), (double)largura, (double)altura);
        }else{
            graphicsContext.drawImage(anime_2.updateSprite(10),(double)getX(), (double)getY(), (double)largura, (double)altura);
        }
    }
}
