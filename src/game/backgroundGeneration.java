package game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import java.util.HashMap;
import org.newdawn.slick.Image;
import java.util.Random;

public class backgroundGeneration{
    
    HashMap<Integer, Image> secondaryBacking = new HashMap<>(), primaryBacking = new HashMap<>();
    int[][] Sb = new int[4][3], Pb = new int[4][4];
    int xSb=0, ySb=0, xPb=0, yPb=0;
    
    public backgroundGeneration() {
        
    }

    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        
        secondaryBacking.put(1, new Image("asset\\forest\\secondaryBacking\\1.png"));
        secondaryBacking.put(2, new Image("asset\\forest\\secondaryBacking\\2.png"));
        secondaryBacking.put(3, new Image("asset\\forest\\secondaryBacking\\3.png"));
        secondaryBacking.put(4, new Image("asset\\forest\\secondaryBacking\\4.png"));
        secondaryBacking.put(5, new Image("asset\\forest\\secondaryBacking\\5.png"));
        secondaryBacking.put(6, new Image("asset\\forest\\secondaryBacking\\6.png"));
        secondaryBacking.put(7, new Image("asset\\forest\\secondaryBacking\\7.png"));
        secondaryBacking.put(8, new Image("asset\\forest\\secondaryBacking\\8.png"));
        
        primaryBacking.put(1, new Image("asset\\forest\\primaryBacking\\1.png"));
        primaryBacking.put(2, new Image("asset\\forest\\primaryBacking\\2.png"));
        primaryBacking.put(3, new Image("asset\\forest\\primaryBacking\\3.png"));
        primaryBacking.put(4, new Image("asset\\forest\\primaryBacking\\4.png"));
        primaryBacking.put(5, new Image("asset\\forest\\primaryBacking\\5.png"));
        primaryBacking.put(6, new Image("asset\\forest\\primaryBacking\\6.png"));
        primaryBacking.put(7, new Image("asset\\forest\\primaryBacking\\7.png"));
        primaryBacking.put(8, new Image("asset\\forest\\primaryBacking\\8.png"));
        
        Random r = new Random();
        for(int y=0;y<4;y++) for(int x=0;x<3;x++){
            if(r.nextInt(10)!=0) Sb[y][x] = r.nextInt(8)+1;
            else Sb[y][x]=0;
            if(r.nextInt(10)!=0) Pb[y][x] = r.nextInt(8)+1;
            else Pb[y][x]=0;
        }
    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics grphcs) throws SlickException {
        
        //X +2000, Y +1253 SB
        // x = -63 + (1397*c), 1270 x 1270
        // y = -127 + (1397*c)
        for(int y=0;y<4;y++){
            for(int x=0;x<3;x++){
                if(Sb[y][x]!=0) secondaryBacking.get(Sb[y][x]).draw((2000*x)+xPb, (128+(1253*y))+yPb);
            }
        }
        
        for(int y=0;y<4;y++){
            for(int x=0;x<4;x++){
                if(Pb[y][x]!=0) primaryBacking.get(Pb[y][x]).draw(((1397*x)-63)+xSb, ((1247*y)-127)+ySb);
            }
        }
    }

    public void update(int moveX, int moveY, GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
        xSb = moveX/8;
        ySb= moveY/16;
        xPb = moveX/4;
        yPb = moveY/8;
    }
    
}
