package game;

import java.awt.Font;
import java.util.HashMap;
import org.newdawn.slick.Color;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.ResourceLoader;

public class playerBar {

    int x=0, y=0, lastMoney;
    Image bar, hlth, money;
    Font font;
    UnicodeFont uniFont;
    HashMap<String, Image> index = new HashMap<>();
    HashMap<Integer, Image> weapon = new HashMap<>();

    public void init() throws SlickException {
        
        try{
            font = Font.createFont(java.awt.Font.TRUETYPE_FONT, ResourceLoader.getResourceAsStream("asset\\font\\Moon Flower Bold.ttf"));
            font = font.deriveFont(Font.PLAIN, 50.f);
            uniFont = new UnicodeFont(font);
            uniFont.getEffects().add(new ColorEffect(java.awt.Color.white));
            uniFont.addAsciiGlyphs();
            uniFont.loadGlyphs();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        bar = new Image("asset\\ui\\bar.png", false, Image.FILTER_NEAREST);
        hlth = new Image("asset\\ui\\healthFull.png", false, Image.FILTER_NEAREST);
        money = new Image("asset\\ui\\money.png", false, Image.FILTER_NEAREST);
        index.put("spd", new Image("asset\\ui\\speed.png", false, Image.FILTER_NEAREST));
        index.put("atk", new Image("asset\\ui\\atk.png", false, Image.FILTER_NEAREST));
        index.put("spcl", new Image("asset\\ui\\special.png", false, Image.FILTER_NEAREST));
        index.put("dfns", new Image("asset\\ui\\defense.png", false, Image.FILTER_NEAREST));
        weapon.put(0, new Image("asset\\ui\\atkspd.png", false, Image.FILTER_NEAREST));
        weapon.put(1, new Image("asset\\ui\\+atk-spd.png", false, Image.FILTER_NEAREST));
        weapon.put(2, new Image("asset\\ui\\-atk+spd.png", false, Image.FILTER_NEAREST));
        weapon.put(3, new Image("asset\\ui\\+atk+spd.png", false, Image.FILTER_NEAREST));
    }

    public void render(int xc, int yc, String boost, int health, int cash, int weaponType, Graphics g) throws SlickException {
        if(lastMoney<cash)lastMoney+=(cash/10);
        if(lastMoney>cash) lastMoney=cash;
        x = xc-650; y = yc-580;
        weapon.get(weaponType).draw(x-125, y);
        bar.draw(x, y);
        if(!boost.equals("none")) index.get(boost).draw(x+13, y+37);
        for(int c=0;c<health;c++) hlth.draw(160+x+(70*c), y+20);
        money.draw(x+160, y+95);
        uniFont.drawString(x+220, y+98, Integer.toString(lastMoney), Color.black);
        uniFont.drawString(x+220, y+95, Integer.toString(lastMoney), Color.white);

    }

    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
        
    }
    
}
