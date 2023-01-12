package game;
import java.awt.Font;
import java.util.Random;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.ResourceLoader;

public class quest {

    Font font;
    UnicodeFont uniFont;
    Image backing;
    Random r = new Random();
    //0 - kill X of (type)
    //1 - complete X levels without dashing/special
    //2 - pass X levels without taking damage
    //the higher generated X, the higher the award (mult by a factor of X/2?)
    
    
    //method w/ each type of quest, returning -1 if not active, 0 if running, 1 if complete?
    
    String dialogue="", subdialogue="", name="";
    boolean questActive=false, complete=false;
    int progress=0, goal=0, quest=0, type=-1, prize=0;

    public void init() throws SlickException {
        backing = new Image("asset\\forest\\NPC\\questDialogue.png");
        try{
            font = Font.createFont(java.awt.Font.TRUETYPE_FONT, ResourceLoader.getResourceAsStream("asset\\font\\Moon Flower Bold.ttf"));
            font = font.deriveFont(Font.PLAIN, 45.f);
            uniFont = new UnicodeFont(font);
            uniFont.getEffects().add(new ColorEffect(java.awt.Color.white));
            uniFont.addAsciiGlyphs();
            uniFont.loadGlyphs();
        }catch(Exception e){
            e.printStackTrace();
        }
        if(!questActive){
            progress=0; type=-1;
            goal = r.nextInt(8)+2; quest = r.nextInt(4);
            switch(quest){
                case 0: 
                    switch((type = r.nextInt(4))){
                        case 0: name=" beetle knights."; break;
                        case 1: name=" gem beetles."; break;
                        case 2: name=" rolling beetles."; break;
                        case 3: name=" slimes."; break;
                    }
                    dialogue="Defeat " + Integer.toString(goal) + name;
                    break;
                case 1:  dialogue="Complete " + Integer.toString(goal) + " levels without dashing."; break;
                case 2: dialogue="Complete " + Integer.toString(goal) + " levels without your special."; break;
                case 3: dialogue="Complete " + Integer.toString(goal) + " levels without damage. "; break;
            }
            prize = (r.nextInt(301)+100)*goal;
        }
    }

    public void render(GameContainer gc, int x, int y) throws SlickException {
        backing.draw(x, y);
        uniFont.drawString(x+320-(15*(dialogue.length()/2)), y+108, dialogue, Color.black);
        uniFont.drawString(x+320-(15*(dialogue.length()/2)), y+105, dialogue, Color.white); //#9ba0b5
        if(goal<=progress && questActive){
            subdialogue = "complete - press down to accept  ";
            if(gc.getInput().isKeyPressed(Input.KEY_S))complete=true;
        }
        else if(questActive) subdialogue = "in progress - " + Integer.toString(progress) + "/" + Integer.toString(goal) + "  ";
        else subdialogue = "press down to accept";
        uniFont.drawString(x+320-(15*(subdialogue.length()/2)), y+148, subdialogue, Color.decode("#9ba0b5"));
    }
}
