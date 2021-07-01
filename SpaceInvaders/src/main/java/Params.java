import java.util.Random;

public class Params {
    public static final String WINDOW_TITLE = "My Game V1.0";
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;
    public static final int LEFT_BORDER = 10;
    public static final int RIGHT_BORDER = WINDOW_WIDTH - LEFT_BORDER;
    public static final int EDGE_Y_TOP = -64;
    public static final int EDGE_Y_DOWN = WINDOW_HEIGHT + 64;
    public static final int EDGE_X_RIGHT = WINDOW_WIDTH + 180;
    public static final int EDGE_X_LEFT = -180;

    public static final String IDLE_BUTTON_STYLE = "-fx-background-color: #000000; -fx-text-fill: #FFFFFF;";
    public static final String HOVERED_BUTTON_STYLE = "-fx-background-color: #FFFFFF; -fx-text-fill: #000000;";

    private static Params params = null;
    private Random rnd;

    private Params(){
        rnd = new Random();
    }

    public static Params getInstance(){
        if (params == null){
            params = new Params();
        }
        return(params);
    }

    public int nextInt(int lim){
        return(rnd.nextInt(lim));
    }
}
