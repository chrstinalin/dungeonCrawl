package game;
import java.io.BufferedReader;
import java.io.FileReader;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import java.io.FileNotFoundException;
import java.io.IOException;

public class main extends StateBasedGame {

    boolean saveExists=false;
    int money, health, weaponType, atkPower, atkSpeed, spdBst, atkBst;
    String boost;
    
     public static void main(String[] args) throws SlickException {
       AppGameContainer appgc = new AppGameContainer(new main("Hollow Knight"));
       appgc.setIcon("asset\\icon.png");
       appgc.setDisplayMode(1280, 720, false);
       appgc.setTargetFrameRate(60);
       appgc.setShowFPS(false);
       appgc.setAlwaysRender(true);
       appgc.start();
    }

    public main(String name) {
        super(name);
    }

    @Override
    public void initStatesList(GameContainer gc) throws SlickException {
        
        try {
            BufferedReader read = new BufferedReader(new FileReader("save.txt"));
            if(read.readLine().equals("SAVE")){
                money = Integer.parseInt(read.readLine());
                health = Integer.parseInt(read.readLine());
                weaponType = Integer.parseInt(read.readLine());
                atkPower = Integer.parseInt(read.readLine());
                atkSpeed = Integer.parseInt(read.readLine());
                spdBst = Integer.parseInt(read.readLine());
                atkBst = Integer.parseInt(read.readLine());
                boost = read.readLine();
                saveExists=true;
            }
        } catch (FileNotFoundException ex) {} catch (IOException ex) {}
        
        this.addState(new mainMenu(0, saveExists)); //state 0: main menu/controls
        this.addState(new mapPrinter(1,0,5,0,1,1000,0,0,"none")); //state 1: new
        if(saveExists) this.addState(new mapPrinter(2, money, health, weaponType, atkPower, atkSpeed, spdBst, atkBst, boost)); //state 2: load 
    }
}
