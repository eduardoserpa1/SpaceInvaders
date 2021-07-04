import java.io.IOException;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

//José Eduardo Rodrigues Serpa - 20200311-7
//Henrique Barcellos Lima - 20204006-9


public class Tanker extends BasicElement{
    private Canhao alvo;
    private int shot_timer = 0;
    protected int RELOAD_TIME = 60;
    protected int life = 7;

    private Animator anime_1;
    private Animator anime_2;
    private Animator anime_3;

    public Tanker(int px,int py,Canhao canhao){
        super(px,py);

        this.alvo = canhao;
        this.altura=64;
        this.largura=64;

        setEnemy(true);
        setSpeed(1);
        setPontos(24);

        setPosY(getY() - 100);

        anime_1 = new Animator("tanker\\stage1");
        anime_2 = new Animator("tanker\\stage2");
        anime_3 = new Animator("tanker\\stage3");
        try { 
            anime_1.load();
            anime_2.load();
            anime_3.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(){
        setDirH(1);
        setPosY(getY() + 100);
    }


    @Override
    public void Update(long deltaTime){
        if (jaColidiu()){
            if(life<=1){
                Game.getInstance().incPontos(getPontos());
                deactivate();
            }else{
                life--;
                colidiu=false;
                return;
            }
        }else{
            setPosX(getX() + getDirH() * getSpeed());
            
            aim_shot();

            if (getX()+getLargura() >= getLMaxH() || getX() <= getLMinH()){
              
                setDirH(getDirH() * (-1));
               
            }
            if(getY() + getAltura() >= getLMaxV()){
                Game.getInstance().setGameOver();
            }

            if (shot_timer > 0) shot_timer -= 1;

        }
    }

    public void aim_shot(){
        int px1 = getX();
        int px2 = px1 + getLargura();
        int canhao_px1 = alvo.getX();
        int canhao_px2 = canhao_px1 + alvo.getLargura();
        if(  (px1 >= canhao_px1 && px1 <= canhao_px2)  ||  (px2 >= canhao_px1 && px2 <= canhao_px2)){
            if(shot_timer==0){
                Game.getInstance().addChar(new Shot(getX() + (getLargura()/2),getY()+getAltura()+5,this));
                shot_timer = RELOAD_TIME;
            }
        }
    }

    public void Draw(GraphicsContext graphicsContext){
        if(life>=5){
            graphicsContext.drawImage(anime_1.updateSprite(10),(double)getX(), (double)getY(), (double)largura, (double)altura);
        }else
        if(life>=3){
            graphicsContext.drawImage(anime_2.updateSprite(10),(double)getX(), (double)getY(), (double)largura, (double)altura);
        }else
        if(life>=1){
            graphicsContext.drawImage(anime_3.updateSprite(10),(double)getX(), (double)getY(), (double)largura, (double)altura);
        }   
    }
}
