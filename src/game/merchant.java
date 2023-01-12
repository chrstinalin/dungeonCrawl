package game;
import java.awt.Font;
import java.util.HashMap;
import java.util.Random;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.util.ResourceLoader;

public class merchant {

    Font font;
    UnicodeFont uniFont;
    
    Image moneyBar;
    HashMap<Integer, Boolean> sold = new HashMap<>();
    HashMap<Integer, Image> weapon = new HashMap<>();
    HashMap<Integer, Image> boost = new HashMap<>();
    int[][] shop; int bob=0, dir=1;
    int[] price;
    Random r = new Random();
    
    public void init(int currentWeapon, String currentBoost) throws SlickException {

        try{
            font = Font.createFont(java.awt.Font.TRUETYPE_FONT, ResourceLoader.getResourceAsStream("asset\\font\\Moon Flower Bold.ttf"));
            font = font.deriveFont(Font.PLAIN, 40.f);
            uniFont = new UnicodeFont(font);
            uniFont.getEffects().add(new ColorEffect(java.awt.Color.white));
            uniFont.addAsciiGlyphs();
            uniFont.loadGlyphs();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        sold.put(0, false);
        sold.put(1, false);
        moneyBar = new Image("asset\\ui\\moneyBar.png", false, Image.FILTER_NEAREST);
        weapon.put(1, new Image("asset\\ui\\+atk-spdIcon.png", false, Image.FILTER_NEAREST));
        weapon.put(2, new Image("asset\\ui\\-atk+spdIcon.png", false, Image.FILTER_NEAREST));
        weapon.put(3, new Image("asset\\ui\\+atk+spdIcon.png", false, Image.FILTER_NEAREST));
        boost.put(0, new Image("asset\\ui\\speed.png", false, Image.FILTER_NEAREST));
        boost.put(1, new Image("asset\\ui\\atk.png", false, Image.FILTER_NEAREST));
        boost.put(2, new Image("asset\\ui\\special.png", false, Image.FILTER_NEAREST));
        boost.put(3, new Image("asset\\ui\\defense.png", false, Image.FILTER_NEAREST));
        shop = new int[2][2];
        price = new int[2];
        for(int c=0;c<2;c++){
            if(r.nextInt(20)<15){ //spawn boost
                shop[c][0] = 0;
                shop[c][1] = r.nextInt(4);
                price[c] = r.nextInt(2001)+1000;
            }
            else{ //spawn weapon
                shop[c][0] = 1;
                shop[c][1] = r.nextInt(3)+1;
                price[c] = r.nextInt(2001)+2000;
            }
        }
    }

    public void renderItems(int x, int y, Graphics g) throws SlickException {
        for(int c=0;c<2;c++){
            if(c>0) x+=190;
            if(!sold.get(c)) switch(shop[c][0]){
                case 0: 
                    boost.get(shop[c][1]).draw(x, y+(bob/10));
                    break; //draw boost
                case 1: 
                    weapon.get(shop[c][1]).draw(x-5, y-130+(bob/10));
                    break; //draw weapon
            }
        }
        switch (bob) {
            case 100: dir=-1; break;
            case 0: dir=1; break;
            default: break;
        }
        bob+=dir;
    }
    
    public void renderPrice(int item, int money, int x, int y, Graphics g) throws SlickException {
        if(shop[item][0]!=-1){ //if not bought
            moneyBar.draw(x-35, y-50);
            g.setColor(Color.black);
            uniFont.drawString(x+55, y-35, Integer.toString(price[item]), Color.black);
            if(money>=price[item]) uniFont.drawString(x+55, y-37, Integer.toString(price[item]), Color.white);
            else uniFont.drawString(x+55, y-37, Integer.toString(price[item]), Color.decode("#DB3150"));
        }
    }

    public String buyBoost(int item){
        switch(shop[item][1]){
            case 0: return "spd";
            case 1: return "atk";
            case 2: return "spcl";
            case 3: return "dfns";
        }
        return "";
    }
    
    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
       
       // check if bought
    }
    
}
