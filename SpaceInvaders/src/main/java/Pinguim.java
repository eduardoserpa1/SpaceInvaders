import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Pinguim extends BasicElement {
    private Image image;

    public Pinguim(int px,int py){
        super(px,py);
        try{
            // Carrega a imagem ajustando a altura para 40 pixels
            // mantendo a proporção em ambas dimensões
            image =  new Image( "pinguim.png",0,40,true,true );
        }catch(Exception e){
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void start(){
        setDirH(1);
        setSpeed(5);
    }


    @Override
    public void Update(long deltaTime){
        if (jaColidiu()){
            Game.getInstance().incPontos();
            deactivate();
        }else{
            setPosX(getX() + getDirH() * getSpeed());
            // Se chegou no lado direito da tela ...
            if (getX() >= getLMaxH() || getX() < getLMinH()){
                // Inverte a direção
                setDirH(getDirH()*-1);
                // Sorteia o passo de avanço [1,5]
                setSpeed(Params.getInstance().nextInt(5)+5);
                // Se ainda não chegou perto do chão, desce
                /*if (getY() < 450){ */
                    setPosY(getY()+25);
                //}
            }
        }
    }

    public void Draw(GraphicsContext graphicsContext){
        graphicsContext.drawImage(image, getX(),getY());
    }

    @Override
    public void testaColisao(Character outro){
        if (outro instanceof Pinguim){
            return;
        }else{
            super.testaColisao(outro);
        }
    }
}
