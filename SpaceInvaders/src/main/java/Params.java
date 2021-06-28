import java.util.Random;

public class Params {
    public static final String WINDOW_TITLE = "My Game V1.0";
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;
    public static final int LEFT_BORDER = 10;
    public static final int RIGHT_BORDER = WINDOW_WIDTH - LEFT_BORDER;

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
