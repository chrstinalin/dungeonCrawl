package game;
import java.util.HashMap;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class mainMenu extends BasicGameState{
    
    Image controls;
    Music menuMusic, theme;
    Sound click, confirm;
    int selection=0; boolean saveExists, control;
    HashMap<Integer, Image> menu = new HashMap<>();
    
    public mainMenu(int id, boolean save) throws SlickException{
        saveExists=save;
        menu.put(0, new Image("asset\\ui\\menu\\main\\NEW.png", false, Image.FILTER_NEAREST));
        menu.put(1, new Image("asset\\ui\\menu\\main\\LOAD.png", false, Image.FILTER_NEAREST));
        menu.put(2, new Image("asset\\ui\\menu\\main\\CONTROLS.png", false, Image.FILTER_NEAREST));
        menu.put(3, new Image("asset\\ui\\menu\\main\\EXIT.png", false, Image.FILTER_NEAREST));
        controls = new Image("asset\\ui\\menu\\main\\controlView.png", false, Image.FILTER_NEAREST);
        menuMusic = new Music("asset\\audio\\theme\\menu.ogg");
        theme = new Music("asset\\audio\\theme\\level.ogg");
        click = new Sound("asset\\audio\\click.wav");
        confirm = new Sound("asset\\audio\\confirm.wav");
        menuMusic.loop();
    }
    
    public int getID() {
        return 0;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        menuMusic.play();
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        g.scale((float)0.75,(float)0.75);
        if(!control)menu.get(selection).draw(-5, 0);
        else controls.draw(-5, 0);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
        Input input = gc.getInput();
        if(control && input.isKeyPressed(Input.KEY_ESCAPE)){
            control=false;
            click.play();
        }
        else{
            if(input.isKeyPressed(Input.KEY_D)){
            selection++;
            if(!saveExists && selection==1) selection++;
            click.play();
        }
        else if(input.isKeyPressed(Input.KEY_A)){
            selection--;
            if(!saveExists && selection==1) selection--;
            click.play();
        }
        if(selection<0) selection=3;
        else if(selection>3) selection=0;
        
        if(input.isKeyPressed(Input.KEY_ENTER)){
            if(selection!=2) menuMusic.fade(800, 0, true);
            confirm.play();
            switch(selection){
                case 0: 
                    theme.play();
                    sbg.enterState(1); break; //new
                case 1: 
                    theme.play();
                    sbg.enterState(2); break; //load
                case 2: control=true; break; //controls
                case 3: gc.exit(); break;
            }
        }
        }
    }
}
