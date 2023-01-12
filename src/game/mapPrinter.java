package game;
import java.awt.Rectangle;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.SpriteSheet;

public class mapPrinter extends BasicGameState {
    
    player knight;
    String boost="none";
    Sound click, confirm;
    playerBar bar = new playerBar();
    boolean select=true, damage=false, exitDoor=false, entranceDoor=false, NPC=false, questView=false, pause=false, dead=false;
    int state=1, money=0, health=5, oldHealth=5, weaponType=0, atkPower=1, atkSpeed=1000, x, bgX=0, bgY=0,
        dmgElapsed=0, spdBst=0, atkBst=0, spclTimer=0;
    
    BufferedWriter write;  
    backgroundGeneration backGen = new backgroundGeneration();
    ArrayList<groundEnemy> gEnemy = new ArrayList<>();
    HashMap<Boolean, Image> pauseMenu = new HashMap<>();
    HashMap<String, Image> index = new HashMap<>();
    ArrayList<int[]> thorn = new ArrayList<>();
    Random r = new Random();
    Image bg, border, ascend, listen, rest;
    SpriteSheet quest, merchant;
    platform[][] mapKey;
    mapGeneration gen;
    Animation q, m;
    merchant merch;
    quest papi = new quest();
    
    public mapPrinter(int id, int mn, int h, int wt, int ap, int as, int sb, int ab, String bst) throws SlickException{
        state=id;
        money=mn; health=h; weaponType=wt; atkPower=ap; atkSpeed=as; spdBst=sb; atkBst=ab; boost=bst;
        pauseMenu.put(true, new Image("asset\\ui\\menu\\pauseResume.png", false, Image.FILTER_NEAREST));
        pauseMenu.put(false, new Image("asset\\ui\\menu\\pauseMain.png", false, Image.FILTER_NEAREST));
        index.put("EGH", new Image("asset\\forest\\tileSet\\EGH.png", false, Image.FILTER_NEAREST));
        index.put("BCDEFGH", new Image("asset\\forest\\tileSet\\BCDEFGH.png", false, Image.FILTER_NEAREST));
        index.put("NONE", new Image("asset\\forest\\tileSet\\NONE.png", false, Image.FILTER_NEAREST));
        index.put("BDFG", new Image("asset\\forest\\tileSet\\BDFG.png", false, Image.FILTER_NEAREST));
        index.put("BD", new Image("asset\\forest\\tileSet\\BD.png", false, Image.FILTER_NEAREST));
        index.put("DEFGH", new Image("asset\\forest\\tileSet\\DEFGH.png", false, Image.FILTER_NEAREST));
        index.put("BCDEGH", new Image("asset\\forest\\tileSet\\BCDEGH.png", false, Image.FILTER_NEAREST));
        index.put("ABDEG", new Image("asset\\forest\\tileSet\\ABDEG.png", false, Image.FILTER_NEAREST));
        index.put("ABDE", new Image("asset\\forest\\tileSet\\ABDE.png", false, Image.FILTER_NEAREST));
        index.put("DFG", new Image("asset\\forest\\tileSet\\DFG.png", false, Image.FILTER_NEAREST));
        index.put("BCE", new Image("asset\\forest\\tileSet\\BCE.png", false, Image.FILTER_NEAREST));
        index.put("BDEGH", new Image("asset\\forest\\tileSet\\BDEGH.png", false, Image.FILTER_NEAREST));
        index.put("BCDE", new Image("asset\\forest\\tileSet\\BCDE.png", false, Image.FILTER_NEAREST));
        index.put("G", new Image("asset\\forest\\tileSet\\G.png", false, Image.FILTER_NEAREST));
        index.put("ABCDE", new Image("asset\\forest\\tileSet\\ABCDE.png", false, Image.FILTER_NEAREST));
        index.put("ABCDEG", new Image("asset\\forest\\tileSet\\ABCDEG.png", false, Image.FILTER_NEAREST));
        index.put("BEG", new Image("asset\\forest\\tileSet\\BEG.png", false, Image.FILTER_NEAREST));
        index.put("ABCDEFG", new Image("asset\\forest\\tileSet\\ABCDEFG.png", false, Image.FILTER_NEAREST));
        index.put("ABD", new Image("asset\\forest\\tileSet\\ABD.png", false, Image.FILTER_NEAREST));
        index.put("BCEG", new Image("asset\\forest\\tileSet\\BCEG.png", false, Image.FILTER_NEAREST));
        index.put("BDEG", new Image("asset\\forest\\tileSet\\BDEG.png", false, Image.FILTER_NEAREST));
        index.put("ABCDEGH", new Image("asset\\forest\\tileSet\\ABCDEGH.png", false, Image.FILTER_NEAREST));
        index.put("B", new Image("asset\\forest\\tileSet\\B.png", false, Image.FILTER_NEAREST));
        index.put("ABDG", new Image("asset\\forest\\tileSet\\ABDG.png", false, Image.FILTER_NEAREST));
        index.put("BDG", new Image("asset\\forest\\tileSet\\BDG.png", false, Image.FILTER_NEAREST));
        index.put("BDEFGH", new Image("asset\\forest\\tileSet\\BDEFGH.png", false, Image.FILTER_NEAREST));
        index.put("BDEFG", new Image("asset\\forest\\tileSet\\BDEFG.png", false, Image.FILTER_NEAREST));
        index.put("DEFG", new Image("asset\\forest\\tileSet\\DEFG.png", false, Image.FILTER_NEAREST));
        index.put("BCEGH", new Image("asset\\forest\\tileSet\\BCEGH.png", false, Image.FILTER_NEAREST));
        index.put("BCDEG", new Image("asset\\forest\\tileSet\\BCDEG.png", false, Image.FILTER_NEAREST));
        index.put("DEGH", new Image("asset\\forest\\tileSet\\DEGH.png", false, Image.FILTER_NEAREST));
        index.put("ABCDEFGH", new Image("asset\\forest\\tileSet\\ABCDEFGH.png", false, Image.FILTER_NEAREST));
        index.put("ABDEFG", new Image("asset\\forest\\tileSet\\ABDEFG.png", false, Image.FILTER_NEAREST));
        index.put("EG", new Image("asset\\forest\\tileSet\\EG.png", false, Image.FILTER_NEAREST));
        index.put("ABDEGH", new Image("asset\\forest\\tileSet\\ABDEGH.png", false, Image.FILTER_NEAREST));
        index.put("ABDFG", new Image("asset\\forest\\tileSet\\ABDFG.png", false, Image.FILTER_NEAREST));
        index.put("E", new Image("asset\\forest\\tileSet\\E.png", false, Image.FILTER_NEAREST));
        index.put("DEG", new Image("asset\\forest\\tileSet\\DEG.png", false, Image.FILTER_NEAREST));
        index.put("BCDEFG", new Image("asset\\forest\\tileSet\\BCDEFG.png", false, Image.FILTER_NEAREST));
        index.put("BG", new Image("asset\\forest\\tileSet\\BG.png", false, Image.FILTER_NEAREST));
        index.put("DE", new Image("asset\\forest\\tileSet\\DE.png", false, Image.FILTER_NEAREST));
        index.put("DG", new Image("asset\\forest\\tileSet\\DG.png", false, Image.FILTER_NEAREST));
        index.put("BE", new Image("asset\\forest\\tileSet\\BE.png", false, Image.FILTER_NEAREST));
        index.put("ABDEFGH", new Image("asset\\forest\\tileSet\\ABDEFGH.png", false, Image.FILTER_NEAREST));
        index.put("D", new Image("asset\\forest\\tileSet\\D.png", false, Image.FILTER_NEAREST));
        index.put("BEGH", new Image("asset\\forest\\tileSet\\BEGH.png", false, Image.FILTER_NEAREST));
        index.put("BDE", new Image("asset\\forest\\tileSet\\BDE.png", false, Image.FILTER_NEAREST));
        index.put("THORN", new Image("asset\\forest\\tileSet\\THORN1.png", false, Image.FILTER_NEAREST));
        index.put("SPAWN", new Image("asset\\forest\\startDoor.png", false, Image.FILTER_NEAREST));
        index.put("EXIT", new Image("asset\\forest\\exitDoor.png", false, Image.FILTER_NEAREST));
        index.put("MERCHANTROOM", new Image("asset\\forest\\NPC\\merchantRoom.png", false, Image.FILTER_NEAREST));
        index.put("QUESTROOM", new Image("asset\\forest\\NPC\\questRoom.png", false, Image.FILTER_NEAREST));
        border = new Image("asset\\forest\\border.png", false, Image.FILTER_NEAREST);
        bg = new Image("asset\\forest\\background.png", false, Image.FILTER_NEAREST);
        merchant = new SpriteSheet("asset\\forest\\NPC\\merchant.png", 231, 292);
        quest = new SpriteSheet("asset\\forest\\NPC\\papi.png", 371, 273);
        ascend = new Image("asset\\ui\\ascend.png", false, Image.FILTER_NEAREST);
        listen = new Image("asset\\ui\\listen.png", false, Image.FILTER_NEAREST);
        rest = new Image("asset\\ui\\rest.png", false, Image.FILTER_NEAREST);
        click = new Sound("asset\\audio\\click.wav");
        confirm = new Sound("asset\\audio\\confirm.wav");
        m = new Animation(merchant, 200);
        q = new Animation(quest, 400);
        q.setPingPong(true);
    }
    
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        
        merch = new merchant();
        gen = new mapGeneration(); //initialize
        mapKey = gen.generate();
        thorn.clear();
        gEnemy.clear();
        for(int y=0;y<42;y++) for(int x=0;x<42;x++){ // locate spawn, thorns
            switch(mapKey[y][x].blockType){
                case "SPAWN":
                    knight = new player(mapKey[y][x].x, mapKey[y][x].y, mapKey);
                    break;
                case "THORN":
                    int[] coord = {mapKey[y][x].x+20, mapKey[y][x].y+70};
                    thorn.add(coord);
                 break;
                default:
                    if(mapKey[y][x].blockType.equals("AIR") &&
                      !mapKey[y+1][x].blockType.equals("THORN") &&
                      !mapKey[y+1][x].blockType.equals("SAFEAIR") &&
                      !mapKey[y+1][x].blockType.equals("INVIS") &&
                      !mapKey[y+1][x].blockType.equals("AIR") &&
                      !mapKey[y+1][x].blockType.contains("B") &&
                      r.nextInt(150)<16){ //if its a ground block, roll for enemy
                        gEnemy.add(new groundEnemy(mapKey[y][x].x, mapKey[y][x].y, mapKey, thorn));
                    }
                    break;
            }
        }
        if(!papi.questActive)papi.init();
        else if(!papi.complete && papi.quest>0) papi.progress++;
        merch.init(weaponType, boost);
        backGen.init(gc, sbg);
        knight.init(gc, sbg);
        bar.init();
        dead=false;
        
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

            //CAMERA
           g.scale((float)0.75,(float)0.75);
           g.translate(((31*gc.getWidth())/50)-(int)knight.player.x,((gc.getHeight())*(float)0.85)-(int)knight.player.y);

            //MAP
            bgX=knight.player.x-950; bgY=knight.player.y-620;
            bg.draw(bgX, bgY); //SKY
            backGen.render(gc, sbg, g); //BACKGROUND

            for(int y=0;y<4;y++) for(int x=0;x<4;x++) { //NPC BACKDROP
                if(gen.layout[y][x]==4) index.get("MERCHANTROOM").draw((1270*x)+100, (1270*y)+100);
                else if(gen.layout[y][x]==7) index.get("QUESTROOM").draw((1270*x)+100, (1270*y)+100);
            }
            for(groundEnemy e: gEnemy) e.render(gc, sbg, g); //ENEMIES
            knight.render(g, damage); //PLAYER

            for(int y=0;y<42;y++) for(int x=0;x<42;x++){ //LEVEL ASSETS
                if(!"AIR".equals(mapKey[y][x].blockType)&& !"SAFEAIR".equals(mapKey[y][x].blockType) && !"INVIS".equals(mapKey[y][x].blockType)){
                    switch (mapKey[y][x].blockType) {
                        case "THORN":
                            index.get((mapKey[y][x].blockType)).draw(mapKey[y][x].x, mapKey[y][x].y+30);
                            break;
                        case "MERCHANT":
                            if(renderDist(mapKey[y][x].x, mapKey[y][x].y-100, 231, 292, bgX, bgY)) m.draw(mapKey[y][x].x, mapKey[y][x].y-100);
                            if(renderDist(mapKey[y][x].x+350, mapKey[y][x].y-100, 131, 292, bgX, bgY) || renderDist(mapKey[y][x].x+525, mapKey[y][x].y-100, 131, 292, bgX, bgY)){
                                merch.renderItems(mapKey[y][x].x+350, mapKey[y][x].y-100, g);
                            }
                            if(!merch.sold.get(0) && popup(mapKey[y][x].x+325, mapKey[y][x].y-100, 131, 292)){ //left item
                                merch.renderPrice(0, money, knight.player.x, knight.player.y, g);
                                if(!pause && merch.price[0]<=money && gc.getInput().isKeyPressed(Input.KEY_S)){
                                    merch.sold.replace(0, true);
                                    money-= merch.price[0];
                                    if(merch.shop[0][0]==0) applyItem(merch.buyBoost(0), 0);
                                    else applyItem("", merch.shop[0][1]);
                                }
                            }
                            if(!merch.sold.get(1) && popup(mapKey[y][x].x+525, mapKey[y][x].y-100, 131, 292)){ //right item
                                merch.renderPrice(1, money, knight.player.x, knight.player.y, g);
                                if(!pause && merch.price[1]<=money && gc.getInput().isKeyPressed(Input.KEY_S)){
                                    merch.sold.replace(1, true);
                                    money-=merch.price[1];
                                    if(merch.shop[1][0]==0) applyItem(merch.buyBoost(1), 0);
                                    else applyItem("", merch.shop[1][1]);
                                }
                            }
                            break;
                        case "QUEST":
                            if(renderDist(mapKey[y][x].x, mapKey[y][x].y-100, 371, 273, bgX, bgY)) q.draw(mapKey[y][x].x, mapKey[y][x].y-100);
                            if(popup(mapKey[y][x].x+350, mapKey[y][x].y-100, 225, 273)){
                                if(questView){
                                    papi.render(gc, mapKey[y][x].x, mapKey[y][x].y-400);
                                    if(!pause && gc.getInput().isKeyPressed(Input.KEY_S)) papi.questActive=true;
                                }
                                else{
                                    listen.draw(knight.player.x-40, knight.player.y-150);
                                    if(!pause && gc.getInput().isKeyPressed(Input.KEY_S)) questView=true;
                                }
                            }
                            else questView=false;
                            break;
                        case "SPAWN":
                            index.get((mapKey[y][x].blockType)).draw(mapKey[y][x].x-30, mapKey[y][x].y-120);
                            if(popup(mapKey[y][x].x-30, mapKey[y][x].y-120, 196, 272)){
                                rest.draw(knight.player.x-40, knight.player.y-150);
                                if(gc.getInput().isKeyPressed(Input.KEY_S)){
                                    try { 
                                    write = new BufferedWriter(new FileWriter("save.txt"));
                                    write.write("SAVE\n");
                                    write.write(Integer.toString(money)+ "\n");
                                    write.write(Integer.toString(health)+ "\n");
                                    write.write(Integer.toString(weaponType)+ "\n");
                                    write.write(Integer.toString(atkPower)+ "\n");
                                    write.write(Integer.toString(atkSpeed)+ "\n");
                                    write.write(Integer.toString(spdBst)+ "\n");
                                    write.write(Integer.toString(atkBst)+ "\n");
                                    write.write(boost);
                                    write.close();
                                    } catch (IOException ex){}
                                    gc.exit();
                                }
                            }
                            break;
                        case "EXIT":
                            index.get((mapKey[y][x].blockType)).draw(mapKey[y][x].x-140, mapKey[y][x].y-660);
                            if(popup(mapKey[y][x].x-140, mapKey[y][x].y-660, 512, 840)){
                                ascend.draw(knight.player.x-40, knight.player.y-150);
                                if(!pause && gc.getInput().isKeyPressed(Input.KEY_S) && gc.getInput().isKeyDown(Input.KEY_S)){
                                    init(gc, sbg);
                                }
                            }
                            break;
                        default:
                            index.get((mapKey[y][x].blockType)).draw(mapKey[y][x].x, mapKey[y][x].y); //DRAW GROUND
                            break;
                    }
                }
            }
            border.draw(-750, -350); //BORDER
            
        if(!pause && !dead) bar.render(knight.player.x, knight.player.y, boost, health, money, weaponType, g);
        if(pause) pauseMenu.get(select).draw(bgX+150, bgY+10);
    }

    public boolean renderDist(int x, int y, int w, int h, int bgX, int bgY){
        Rectangle entity = new Rectangle(x,y,w,h);
        Rectangle back = new Rectangle(bgX, bgY, 2000, 1125);
        return back.intersects(entity);
    }
        
    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
        
        if(!pause){
            oldHealth=health; 
            for(groundEnemy e: gEnemy){
                e.update(knight.player.x, knight.player.y, i);
                if(e.health<=0 &&!e.dropped){
                    if(papi.questActive && e.type==papi.type) papi.progress++;
                    money+=e.money();
                }
            } //aims for knight afterimage
            knight.update(gc, i, boost, spclTimer, atkSpeed, spdBst, dead);
            damage=false;
            if(knight.attackType()!=0){ //if attack
                System.out.println(knight.attackType());
                for(groundEnemy e: gEnemy) e.checkForHit(knight.hitBox.x, knight.hitBox.y, knight.facing(), knight.attackType(), atkPower+atkBst);
            }
            for(groundEnemy e: gEnemy) if(e.health>0){
                if((x=e.attack(knight.hitBox, knight.invincible))!=0){
                    health-=x;
                    damage=true;
                }
            }
            if(dmgElapsed>2000 && thornDmg()){
                dmgElapsed=0;
                damage=true;
            }
            else{
                if(!damage) damage=false;
                dmgElapsed+=i;
            }
            backGen.update(knight.player.x, knight.player.y, gc, sbg, i);

            if(papi.questActive){
                if(papi.goal>papi.progress && (papi.quest==1 && knight.dash ||
                   papi.quest==2 && knight.special ||
                   papi.quest==3 && oldHealth>health)){
                    papi.questActive=false;
                    papi.init();
                }
                if(papi.complete){
                    money+=papi.prize;
                    papi.complete=false;
                    papi.questActive=false;
                    papi.init();
                }
            }
            if(gc.getInput().isKeyPressed(Input.KEY_ESCAPE)){
                pause=true;
                click.play();
            }
            if(health<=0) dead=true;
            if(knight.die.isStopped()){
                try { 
                    write = new BufferedWriter(new FileWriter("save.txt"));
                    write.write("NOSAVE");
                    write.close();
                } catch (IOException ex){}
                gc.exit();
            }
        }
        else{
            Input input = gc.getInput();
            if(input.isKeyPressed(Input.KEY_ENTER) && select || input.isKeyPressed(Input.KEY_ESCAPE)){
                pause=false;
                click.play();
            }
            if(gc.getInput().isKeyDown(Input.KEY_ENTER) && !select){
                confirm.play();
                gc.exit();
            }
            if(input.isKeyPressed(Input.KEY_S) ||input.isKeyPressed(Input.KEY_W)){
                select=!select;
                click.play();
            }
        }
        health=5;
    }
    
    public boolean thornDmg(){
        Rectangle entity = new Rectangle(knight.hitBox.x, knight.hitBox.y, knight.hitBox.width, knight.hitBox.height), thrnBox;
        for(int[] tn: thorn){
            thrnBox = new Rectangle(tn[0], tn[1], 88, 48);
            if(entity.intersects(thrnBox)){
                health--;
                return true;
            }
        }
        return false;
    }
    
    public boolean popup(int x, int y, int w, int h){
        Rectangle entity = new Rectangle(knight.hitBox.x, knight.hitBox.y, knight.hitBox.width, knight.hitBox.height);
        Rectangle npc = new Rectangle(x, y, w, h);
        return npc.intersects(entity);
    }
    
    public void applyItem(String bst, int wpn){
        if(bst.equals("")){
            switch(wpn){
                case 1: 
                    atkPower=3;
                    atkSpeed=1400;
                    weaponType=1;
                    break;
                case 2: 
                    atkPower=1;
                    atkSpeed=600;
                    weaponType=2;
                    break;
                case 3: 
                    atkPower=2;
                    atkSpeed=800;
                    weaponType=3;
                    break;
            }
        }
        else{
            boost=bst;
            if("spd".equals(bst)) spdBst=5; //longer dash
            else spdBst=0;
            if("atk".equals(bst)) atkBst=2;
            else atkBst=0;
            if("spcl".equals(bst)) spclTimer=1000;
            else spclTimer=0;
            if("dfns".equals(bst)) health+=5;
            else if(health>=5) health=5;
        }
    }
    
    @Override
    public int getID() {
        return state;
    }   
}