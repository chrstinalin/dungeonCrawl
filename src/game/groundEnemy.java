package game;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;
import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.state.StateBasedGame;

public class groundEnemy{
    
    platform[][] map;
    ArrayList<int[]> thorn;
    Random r = new Random();
    Rectangle enemy, hitbox;
    int[] rCol, lCol, bCol, dCol;
    float ySpeed=0, xSpeed=0, lastxSpeed=0;
    int playerX=0, playerY=0, maxSpeed=0, atk=0, health=5, dir=0, type=0, drop=0,
        walkElapsed=0, atkElapsed=0, atkCooldown=300, walkCooldown=0, dmgElapsed=0,
        travelled=0, t=0, xOffset=0, yOffset=0;
    boolean leftSensor=false, rightSensor=false, bottomSensor=false, topSensor=false, dropped=false,
            jumpL=false, jumpR=false, canJump=false, facingLeft=false, onGround=false,
            moveL=false, moveR=false, withinView=false, dmg=false, invincible=false,
            walkAnimation=false, onCooldown=false, despawn=false, atking =false;
    SpriteSheet attacking, dying, idling, walking, walkStart, walkStop;
    Animation attack, die, idle, walk, walkS;
    Image idleImage, jump;
    
    public groundEnemy(int xLoc, int yLoc, platform[][] key, ArrayList<int[]> thrn) throws SlickException{
        map=key; thorn = thrn;
        int roll = r.nextInt(100);
        if(roll<10) type=0; // boss 10%
        else if(roll<35) type=3; //25%
        else if(roll<55) type=2; //25%
        else type=1; //40-50%
        switch(type){
            case 0: //bigKnight
                atk=2;
                health=8;
                maxSpeed=6;
                atkCooldown=350; //atk when in range
                xOffset=137; yOffset=103;
                enemy = new Rectangle(xLoc, yLoc, 382, 240);
                hitbox = new Rectangle(enemy.x+137, enemy.y+108, 92, 121);
                drop=r.nextInt(801)+200;
                jump = new Image("asset\\enemies\\bigKnight\\jump.png", false, Image.FILTER_NEAREST);
                attacking = new SpriteSheet("asset\\enemies\\bigKnight\\attack.png", 382, 240);
                idling = new SpriteSheet("asset\\enemies\\bigKnight\\idle.png", 382, 240);
                walking = new SpriteSheet("asset\\enemies\\bigKnight\\walk.png", 382, 240);
                dying = new SpriteSheet("asset\\enemies\\bigKnight\\die.png", 382, 240);
                attack = new Animation(attacking, 70);
                idle = new Animation(idling, 250);
                walk = new Animation(walking, 250);
                die = new Animation(dying, 100);
                attack.setLooping(false);
                break;
            case 1: //gemBeetle
                atk=1;
                maxSpeed=3; //moving obstacle
                atkCooldown=50;
                health=12;
                xOffset=51; yOffset=93;
                enemy = new Rectangle(xLoc, yLoc, 286, 265);
                hitbox = new Rectangle(enemy.x+51, enemy.y+93, 184, 152);
                drop=r.nextInt(201)+100;
                idleImage = new Image("asset\\enemies\\beetle\\idle.png", false, Image.FILTER_NEAREST);
                walking = new SpriteSheet("asset\\enemies\\beetle\\walk.png", 286, 265);
                dying = new SpriteSheet("asset\\enemies\\beetle\\die.png", 286, 265);
                walk = new Animation(walking, 300);
                die = new Animation(dying, 100);
                break;
            case 2: //rollBeetle
                atk=1;
                health=3;
                maxSpeed=7;
                atkCooldown=50;
                walkCooldown=150; //how long until it can move again
                xOffset=32; yOffset=28;
                enemy = new Rectangle(xLoc, yLoc, 132, 101);
                hitbox = new Rectangle(enemy.x+32, enemy.y+28, 61, 65);
                drop=r.nextInt(401)+100;
                walkStart = new SpriteSheet("asset\\enemies\\rollBeetle\\rollStart.png", 132, 101);
                walking = new SpriteSheet("asset\\enemies\\rollBeetle\\roll.png", 132, 101);
                idling = new SpriteSheet("asset\\enemies\\rollBeetle\\idle.png", 132, 101);
                dying = new SpriteSheet("asset\\enemies\\rollBeetle\\die.png", 132, 101);
                walkS = new Animation(walkStart, 150);
                walk = new Animation(walking, 250);
                idle = new Animation(idling, 250);
                die = new Animation(dying, 100);
                break;
            case 3: //slime
                atk=1;
                health=5;
                maxSpeed=4;
                atkCooldown=50;
                xOffset=95; yOffset=36;
                enemy = new Rectangle(xLoc, yLoc, 243, 132);
                hitbox = new Rectangle(enemy.x+95, enemy.y+36, 100, 94);
                drop=r.nextInt(401)+100;
                idleImage = new Image("asset\\enemies\\slime\\idle.png", false, Image.FILTER_NEAREST);
                jump = new Image("asset\\enemies\\slime\\jump.png", false, Image.FILTER_NEAREST);
                walkStart = new SpriteSheet("asset\\enemies\\slime\\burrowStart.png", 243, 132);
                walkStop = new SpriteSheet("asset\\enemies\\slime\\burrowEnd.png", 243, 132);
                walking = new SpriteSheet("asset\\enemies\\slime\\walk.png", 243, 132);
                dying = new SpriteSheet("asset\\enemies\\slime\\die.png", 243, 132);
                walk = new Animation(walking, 250);
                die = new Animation(dying, 100);
                break;
        }
        die.setLooping(false);
        
    }

    public void update(int X, int Y, int i) throws SlickException {
        playerX=X; playerY=Y; lastxSpeed=xSpeed; dir=0;
        if(health>0 && (renderDist() || !onGround)){
            withinView = (new Rectangle(playerX, playerY, 128, 128)).intersects(new Rectangle(hitbox.x-(hitbox.width*4), hitbox.y-(hitbox.height*3), hitbox.width*9, hitbox.height*7));
            leftSensor = (lCol = mapCollision(hitbox.x-10, hitbox.y+hitbox.height/4, 10, hitbox.height/2))[0]!=-1; 
            rightSensor = (rCol = mapCollision(hitbox.x+hitbox.width, hitbox.y+hitbox.height/4, 10, hitbox.height/2))[0]!=-1;
            bottomSensor = (bCol = mapCollision(hitbox.x+hitbox.width/4, hitbox.y+hitbox.height-10, hitbox.width/2, 10))[0]!=-1;
            topSensor = (dCol = mapCollision(hitbox.x+hitbox.width/4, hitbox.y-10, hitbox.width/2, 10))[0]!=-1;
            jumpL = (mapCollision(hitbox.x-hitbox.height, hitbox.y+20, hitbox.height, 32)[0]!=-1 && mapCollision(hitbox.x-110, hitbox.y-135, 20, 20)[0]==-1); //forward is blocked, but clear to jump
            jumpR = (mapCollision(hitbox.x+hitbox.height, hitbox.y+20, hitbox.height, 32)[0]!=-1 && mapCollision(hitbox.x+100+hitbox.width, hitbox.y-135, 20, 20)[0]==-1);
            moveL = mapCollision(hitbox.x-20, hitbox.y+hitbox.height, 20, 400)[0]!=-1;
            moveR = mapCollision(hitbox.x+hitbox.width, hitbox.y+hitbox.height, 20, 400)[0]!=-1;

            if(!bottomSensor){ //if no foot collision, gravity
                onGround=false;
                if(ySpeed<14) ySpeed += 1.5;
            }
            else{
                if(type!=1) canJump=true;
                hitbox.y = bCol[1]-hitbox.height+10;
                onGround=true;
                ySpeed=0;
            }
            if(withinView){ //if player is within view
                if (playerX>hitbox.x && moveR) {
                    facingLeft=false;
                    dir += 1;
                }
                else if (playerX<hitbox.x && moveL){
                    facingLeft=true;
                    dir -= 1;
                }
            }
            else xSpeed=0;
            
            if(topSensor && ySpeed<0) ySpeed=0;
            if(dir==1 && xSpeed < maxSpeed || dir==-1 && xSpeed > -maxSpeed) xSpeed += dir;
            if(dir==-1 && leftSensor || dir==1 && rightSensor) xSpeed = 0;
            if(!onCooldown && type==2 && travelled>800) onCooldown=true; //roll beetle max dist
                
            if(onCooldown){
                if(walkElapsed>=walkCooldown){
                    travelled=0;
                    onCooldown=false;
                    walkElapsed=0;
                }
                else{
                    walkElapsed++;
                    xSpeed=0;
                }
            }
            
            //too close to player
            if(type!=2 && (facingLeft && hitbox.x%playerX<hitbox.width || !facingLeft && playerX%hitbox.x<hitbox.width)) xSpeed=0; 

            if(xSpeed!=0){
                if((jumpL || jumpR) && canJump){
                    canJump=false;
                    ySpeed = -27;
                }
                if(type==2) invincible=true;
            }
            else if(type==2) invincible=false;
            
            t=i;
            hitbox.x += xSpeed;
            hitbox.y += ySpeed;
            enemy.x = hitbox.x-xOffset;
            enemy.y = hitbox.y-yOffset;
            travelled+=Math.abs(xSpeed);
            
            if(type!=1 && !invincible){ //thorns
                if(dmgElapsed>2000 && thornDmg()){
                    dmgElapsed=0;
                    dmg=true;
                }
                else dmgElapsed+=i;
            }
        }
    }
    
    public int money(){
        dropped=true;
        return drop;
    }
    
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException{
        
        if(!despawn && renderDist()){
            switch(type){
                case 0: renderKnight(enemy.x, enemy.y); break;
                case 1: renderGemBeetle(enemy.x, enemy.y); break;
                case 2: renderRollBeetle(enemy.x, enemy.y); break;
                case 3: renderSlime(enemy.x, enemy.y); break;
            }
            if(dmg) dmg=false;
        }
        else if(health<=0) despawn=true;
    }
    
    public void renderKnight(int x, int y){ //0
        if(health<=0){
            die.getCurrentFrame().getFlippedCopy(facingLeft, false).draw(enemy.x, enemy.y);
            die.update(t);
        }
        else if(atking){
            if(dmg) attack.getCurrentFrame().getFlippedCopy(facingLeft, false).drawFlash(enemy.x, enemy.y);
            else attack.getCurrentFrame().getFlippedCopy(facingLeft, false).draw(enemy.x, enemy.y);
            attack.update(t);
            if(attack.isStopped()){
                attack.restart();
                atking=false;
            }
        }
        else if(!onGround){ //in air
            if(dmg) jump.getFlippedCopy(facingLeft, false).drawFlash(enemy.x, enemy.y);
            else jump.getFlippedCopy(facingLeft, false).draw(enemy.x, enemy.y);
        }
        else if(xSpeed>0 || xSpeed<0){ //walk
            if(dmg) walk.getCurrentFrame().getFlippedCopy(facingLeft, false).drawFlash(enemy.x, enemy.y);
            else walk.getCurrentFrame().getFlippedCopy(facingLeft, false).draw(enemy.x, enemy.y);
            walk.update(t);
        }
        else if(xSpeed==0){ //idle
            if(dmg) idle.getCurrentFrame().getFlippedCopy(facingLeft, false).drawFlash(enemy.x, enemy.y);
            else idle.getCurrentFrame().getFlippedCopy(facingLeft, false).draw(enemy.x, enemy.y);
            idle.update(t);
        }
    }
    
    public void renderGemBeetle(int x, int y){ //1
        if(health<=0){
            die.getCurrentFrame().getFlippedCopy(facingLeft, false).draw(enemy.x, enemy.y);
            die.update(t);
        }
        else if(xSpeed>0 || xSpeed<0){ //walk
            if(dmg) walk.getCurrentFrame().getFlippedCopy(facingLeft, false).drawFlash(enemy.x, enemy.y);
            else walk.getCurrentFrame().getFlippedCopy(facingLeft, false).draw(enemy.x, enemy.y);
            walk.update(t);
        }
        else if(xSpeed==0){ //idle
            if(dmg) idleImage.getFlippedCopy(facingLeft, false).drawFlash(enemy.x, enemy.y);
            else idleImage.getFlippedCopy(facingLeft, false).draw(enemy.x, enemy.y);
        }
    }
    
    public void renderRollBeetle(int x, int y){ //2
        if(health<=0){
            die.getCurrentFrame().getFlippedCopy(facingLeft, false).draw(enemy.x, enemy.y);
            die.update(t);
        }
        else if(xSpeed>0 || xSpeed<0){ //walk
            if(!walkAnimation){
                if(dmg) walkS.getCurrentFrame().getFlippedCopy(facingLeft, false).drawFlash(enemy.x, enemy.y);
                else walkS.getCurrentFrame().getFlippedCopy(facingLeft, false).draw(enemy.x, enemy.y);
                walkS.update(t);
                if(walkS.getFrame()==2){
                    walkS.restart();
                    walkAnimation=true;
                }
            }
            else{
                if(dmg) walk.getCurrentFrame().getFlippedCopy(facingLeft, false).drawFlash(enemy.x, enemy.y);
                else walk.getCurrentFrame().getFlippedCopy(facingLeft, false).draw(enemy.x, enemy.y);
                walk.update(t);
            }
        }
        else if(xSpeed==0){ //idle
            walkAnimation=false;
            if(dmg) idle.getCurrentFrame().getFlippedCopy(facingLeft, false).drawFlash(enemy.x, enemy.y);
            else idle.getCurrentFrame().getFlippedCopy(facingLeft, false).draw(enemy.x, enemy.y);
            idle.update(t);
        }
    }
    
    public void renderSlime(int x, int y){ //3
        if(health<=0){
            die.getCurrentFrame().getFlippedCopy(facingLeft, false).draw(enemy.x, enemy.y);
            die.update(t);
        }
        else if(ySpeed!=0){
            jump.getFlippedCopy(facingLeft, false).draw(enemy.x, enemy.y);
        }
        else if(xSpeed>0 || xSpeed<0){ //walk
            if(dmg) walk.getCurrentFrame().getFlippedCopy(facingLeft, false).drawFlash(enemy.x, enemy.y);
            else walk.getCurrentFrame().getFlippedCopy(facingLeft, false).draw(enemy.x, enemy.y);
            walk.update(t);
        }
        else if(xSpeed==0){ //idle
            if(dmg) idleImage.getFlippedCopy(facingLeft, false).drawFlash(enemy.x, enemy.y);
            else idleImage.getFlippedCopy(facingLeft, false).draw(enemy.x, enemy.y);
        }
    }
    
    public int attack(Rectangle player, boolean invin){
        switch(type){
            case 0: //knight
                if(atkElapsed>=atkCooldown){
                    if(facingLeft && player.intersects(new Rectangle(hitbox.x-100, hitbox.y+hitbox.height/6, 100, hitbox.height/3))) atking = true;
                    else if(player.intersects(new Rectangle(hitbox.x+hitbox.width, hitbox.y+hitbox.height/6, 100, hitbox.height/3))) atking = true;
                    if(atking){
                        atkElapsed=0;
                        if(!invin) return atk;
                    }
                }
                break;
            case 1: //gembeetle
                if(atkElapsed>=atkCooldown && !invin && player.intersects(new Rectangle (hitbox.x, hitbox.y, hitbox.width, 30))){
                    atkElapsed=0;
                    return atk;
                }
                break;
            case 2: //rollbeetle
                if(atkElapsed>=atkCooldown && invincible && !invin && player.intersects(new Rectangle(hitbox.x, hitbox.y, hitbox.width, hitbox.height))){
                    atkElapsed=0;
                    return atk;
                }
                break;
            case 3: //slime
                if(atkElapsed>=atkCooldown && !invin && player.intersects(new Rectangle(hitbox.x, hitbox.y, hitbox.width, hitbox.height))){
                    atkElapsed=0;
                    return atk;
                }
                break;
        }
        if(renderDist()) atkElapsed++;
        return 0;
    }
    
    
    public void checkForHit(int x, int y, boolean facingL, int type, int power){
         switch(type){
             case 1:  //normal
                 if(facingL && hitbox.intersects(new Rectangle(playerX-50, playerY+50, 96, 40))) dmg=true;
                 else if(hitbox.intersects(new Rectangle(playerX+80, playerY+50, 96, 40))) dmg=true;
                 if(!invincible && dmg && health!=0) health-=power;
                 break;
             case 2: //special
                 if(hitbox.intersects(new Rectangle(playerX-55, playerY-30, 240, 100))) dmg=true;
                 if(!invincible && dmg && health!=0) health-=power*1.5;
                 break;
         }
    }
    
    public boolean thornDmg(){
        for(int[] tn: thorn){
            Rectangle thrnBox = new Rectangle(tn[0], tn[1], 88, 48);
            if(hitbox.intersects(thrnBox)){
                if(health>=0)health--;
                return true;
            }
        }
        return false;
    }
    
    public boolean renderDist(){
        Rectangle back = new Rectangle(playerX-950, playerY-620, 2000, 1125);
        return back.intersects(enemy);
    }

    public int[] mapCollision(int x, int y, int w, int h){
        Rectangle sensor = new Rectangle(x,y,w,h);
        int[] nope = {-1,-1};
        for(int a=0;a<42;a++){
            for(int b=0; b<42;b++){
                Rectangle p = new Rectangle(map[a][b].x, map[a][b].y,128,128);
                if(!"AIR".equals(map[a][b].blockType) &&
                   !"SPAWN".equals(map[a][b].blockType) &&
                   !"THORN".equals(map[a][b].blockType) &&
                   !"EXIT".equals(map[a][b].blockType) && p.intersects(sensor)){
                    int[] coordinate = {p.x, p.y};
                    return coordinate;
                }
            }
        }
        return nope;
    }
}