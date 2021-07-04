import javafx.scene.canvas.GraphicsContext;

//José Eduardo Rodrigues Serpa - 20200311-7
//Henrique Barcellos Lima - 20204006-9
public interface Character {
    int getX();
    int getY();
    int getAltura();
    int getLargura();

    boolean isEnemy();
    void touchBottom();
 
    void testaColisao(Character c);
    boolean jaColidiu();
    void setColidiu();

    void start();
    boolean isActive();
    void Update(long deltaTime);
    void Draw(GraphicsContext graphicsContext);
}
