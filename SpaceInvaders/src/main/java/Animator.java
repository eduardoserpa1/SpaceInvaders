import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import javafx.scene.image.Image;

public class Animator {
    
    private Path url;
    private int coef=0,count=0;
    public LinkedList<Image> frames;

    public Animator(String url){
        frames = new LinkedList<Image>();
        System.out.println(System.getProperty("user.dir") + "\\" + url);
        this.url = Paths.get(url);
    }


    public void load() throws IOException{
        try ( DirectoryStream<Path> stream = Files.newDirectoryStream(url,"*.png")){
            for(Path p : stream){
               System.out.println(p.getFileName());
               String dir = "canon" + "\\" + p.getFileName();
               //Image a = new Image(dir,32,32,true,true);
               frames.add(new Image(dir,32,32,true,true));
            }
          }
    }

    public Image updateCenter(int fps){
        count++;
        if(count%fps==0){
            coef++;
            if(coef>frames.size()-1)
                coef=0;
        }
  
        return frames.get(coef);
    }
    
    
    /*
    public Path getPath(String file){
        String currDir = Paths.get("").toAbsolutePath().toString();
        String nameComplete = currDir+"\\"+file;
        Path path = Paths.get(nameComplete);
        return path;
    }*/
}
