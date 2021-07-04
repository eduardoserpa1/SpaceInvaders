import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import javafx.scene.image.Image;

//José Eduardo Rodrigues Serpa - 20200311-7
//Henrique Barcellos Lima - 20204006-9

public class Animator {
    
    private Path url;
    private String url_string;
    private int coef=0,count=0;
    public LinkedList<Image> frames;

    public Animator(String url){
        frames = new LinkedList<Image>();
        this.url = Paths.get(System.getProperty("user.dir") + "\\src\\main\\resources\\" + url);
        this.url_string = url;
    }


    public void load() throws IOException{
        try ( DirectoryStream<Path> stream = Files.newDirectoryStream(url,"*.png")){
            for(Path p : stream){
               String dir = url_string +"\\"+ p.getFileName();
               frames.add(new Image(dir,32,32,true,true));
            }
          }
    }

    public Image updateSprite(int fps){
        count++;
        if(count%fps==0){
            coef++;
            if(coef>frames.size()-1)
                coef=0;
        }
  
        return frames.get(coef);
    }

}
