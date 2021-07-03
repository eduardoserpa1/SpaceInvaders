import java.io.IOException;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

//José Eduardo Rodrigues Serpa - 20200311-7
//Henrique Barcellos Lima - 20204006-9


public class Soldier extends BasicElement{
    private int life = 4;

    private Animator anime;

    public Soldier(int px,int py){
        super(px,py);
        setEnemy(true);
        altura=50;
        largura=50;
        setSpeed(2);
        setPontos(6);

        anime = new Animator("soldier");
        try { 
            anime.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

            }
        }
    }

    public void Draw(GraphicsContext graphicsContext){
        graphicsContext.drawImage(anime.updateSprite(10),(double)getX(), (double)getY(), (double)largura, (double)altura);
    }
}
