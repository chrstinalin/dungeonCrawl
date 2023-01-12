package game;
import java.awt.Rectangle;
import java.util.ArrayList;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
    
public class player{

    //Player
    int dir=0, jump=0, dashElapsed=1000, spclElapsed=3000, atkElapsed=500, d=0;
    Rectangle player = new Rectangle(640, 500, 128, 128);
    Rectangle hitBox = new Rectangle(player.x+30, player.y+20, player.width-60, player.height-20);
    int[] rCol, lCol, bCol, dCol;
    double ySpeed=0, oldYSpeed=0, xSpeed=0, spd=1;
    boolean rightSensor=false, leftSensor=false, bottomSensor=false, topSensor=false,
            dashSensorL=false, dashSensorR=false, invincible=false, dead=false,
            facingLeft=false, launchAnim=false, wallJump=false,
            jumpPress=false, jumpDown=false, onGround=false,
            wallClimb=false, crouch=false, justLanded=false,
            dashStop=false, dash=false, oldDash=false,
            attack=false, hitSp=false, hitAt=false, special=false;
    platform[][] map;

    //SPRITES
    SpriteSheet ded, run, crawl, knightLaunch, knightLand, knightSpecial, knightDashS, knightDashH, knightAtk;
    Animation die, runA, crawlA, launch, land, specialAtk, dashS, dashH, knightAtkA;
    Image idle, rising, fall, cling;
    
    //int x, int y, platform[][] key
    public player(int x, int y, platform[][] key){ //spawn location
        player.x = x;
        player.y = y;
        map = key;
    }
    
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException{
        
        idle = new Image("asset\\knight\\knightIdle.png");
        cling = new Image("asset\\knight\\knightCling.png");
        run = new SpriteSheet("asset\\knight\\knightRun.png", 128, 128);
        //crawl = new SpriteSheet("asset\\knight\\crouchMove.png", 128, 128);
        knightAtk = new SpriteSheet("asset\\knight\\knightAtk.png", 128, 128);
        knightSpecial = new SpriteSheet("asset\\knight\\SpecialAtk2.png", 256, 128);
        knightDashS = new SpriteSheet("asset\\knight\\dashStart.png", 256, 128);
        knightDashH = new SpriteSheet("asset\\knight\\dashStop.png", 256, 128);
        knightLaunch= new SpriteSheet("asset\\knight\\knightLaunch.png", 128, 128);
        ded = new SpriteSheet("asset\\knight\\die.png", 128, 128);
        knightLand = new SpriteSheet("asset\\knight\\knightLand.png", 128, 128);
        dashH = new Animation(knightDashH, 50);
        dashS = new Animation(knightDashS, 50);
        knightAtkA = new Animation(knightAtk, 70);
        specialAtk = new Animation(knightSpecial, 50);
        launch = new Animation(knightLaunch, 60);
        land = new Animation(knightLand, 60);
        runA = new Animation(run, 70);
        die = new Animation(ded, 70);
        //crawlA = new Animation(crawl, 70);
        rising = knightLaunch.getSprite(2, 0);
        fall = knightLand.getSprite(0, 0);
        land.setCurrentFrame(2);
        dashS.setLooping(false);
        dashH.setLooping(false);
        knightAtkA.setLooping(false);
        specialAtk.setLooping(false);
        launch.setLooping(false);
        land.setLooping(false);
        die.setLooping(false);
        //temp

    }

       
    public void update(GameContainer gc, int t, String boost, int spclTimer, int atkSpd, int spdBst, boolean rip) throws SlickException {
        dead=rip;
        if(!dead){
            leftSensor = (lCol = collision(hitBox.x+10, hitBox.y+50, 10, hitBox.height-100))[0]!=-1; 
            rightSensor = (rCol = collision(hitBox.x+hitBox.width-20, hitBox.y+50, 10, hitBox.height-100))[0]!=-1;
            bottomSensor = (bCol = collision(hitBox.x+30, hitBox.y+hitBox.height-30, hitBox.width-60, 10))[0]!=-1;
            topSensor = (dCol = collision(hitBox.x+30, hitBox.y-10, hitBox.width-60, 10))[0]!=-1;
            dashSensorL = (dCol = collision(hitBox.x-546, hitBox.y+50, 300, hitBox.height-100))[0]!=-1; //left far
            dashSensorR = (dCol = collision(hitBox.x+hitBox.width+256, hitBox.y+50, 300, hitBox.height-100))[0]!=-1; //right far

            dir=0; jumpPress=false; //DIRECTIONAL
            Input input = gc.getInput();
            if(xSpeed>-10 && xSpeed<10){ //if not dashing
                if (input.isKeyDown(Input.KEY_D)) {
                    dir += 1;
                    facingLeft=false;
                }
                if (input.isKeyDown(Input.KEY_A)){
                    dir -= 1;
                    facingLeft=true;
                }
            }
            if (input.isKeyDown(Input.KEY_W) && input.isKeyPressed(Input.KEY_W) && jump<2){
                jumpPress = true;
                jump+=1;
            }
            if(!wallClimb && input.isKeyDown(Input.KEY_E) && input.isKeyPressed(Input.KEY_E) && atkElapsed>atkSpd){ //atk
                hitAt=true;
                attack=true;
                atkElapsed=0;
            }
            else{
                hitAt=false;
                atkElapsed+=d;
            }
            if((!dashSensorL && dir==-1 || !dashSensorR && dir==1) && input.isKeyPressed(Input.KEY_Q) && input.isKeyDown(Input.KEY_Q) && dashElapsed>1000){ //dash
                xSpeed+=(20*dir)+spdBst;
                dashElapsed=0;
            }
            else dashElapsed+=d;
            if(input.isKeyDown(Input.KEY_F) && input.isKeyPressed(Input.KEY_F) && !wallClimb && spclElapsed>3000-spclTimer){ //special
                hitSp=true;
                spclElapsed=0;
                special=true;
            }
            else{
                hitSp=false;
                spclElapsed+=d;
            }

            if(!bottomSensor){ //if no foot collision, gravity
                onGround=false;
                if(ySpeed<14) ySpeed += 1.5; //gravity
                if(wallClimb=(dir==-1 && leftSensor|| dir==1 && rightSensor)){ //if player is pushing against wall
                    if(ySpeed>0) ySpeed *= 0.5; // slower fall
                    if(!wallJump){
                        wallJump=true;
                        jump=0;
                    }
                }
            }
            else{
                jump=0;
                wallJump=false;
                ySpeed=0;
                if(!onGround){
                    player.y = bCol[1]-player.height+30;
                    onGround=true;
                }
            }

            if(jumpPress && jump<2) ySpeed = -27;

            if(xSpeed<-10 || xSpeed>10){
                xSpeed-=1*dir;
                dash=true;
            }
            else dash=false;
            if(dir==1 && xSpeed < 10 || dir==-1 && xSpeed > -(10)) xSpeed += spd*dir;
            if(dir==0){ //momentum
                if(xSpeed!=0 && xSpeed<0.1) xSpeed+=1; //going left
                else if(xSpeed!=0 && xSpeed>-0.1) xSpeed-=1; //going right
                else xSpeed=0;
            }

            if(dir==-1 && leftSensor){ //left blocked
                player.x = lCol[0] + 128-60;
                xSpeed = 0;  
            }
            if(dir==1 && rightSensor){ //right blocked
                player.x = rCol[0] - player.width+50;
                xSpeed = 0;
            }

            if(topSensor && ySpeed<0) ySpeed=0;

            player.x += xSpeed;
            player.y += ySpeed;
            hitBox.x = player.x+30; hitBox.y = player.y+20;
            d=t;

            if(ySpeed==0 && oldYSpeed>0) justLanded = true;
        }
    }

    public void render(Graphics g, boolean dmg) throws SlickException {
        if(dead) die.draw(player.x, player.y);
        else if(wallClimb){
            if(dmg) cling.getFlippedCopy(facingLeft, false).drawFlash(player.x, player.y);
            else cling.getFlippedCopy(facingLeft, false).draw(player.x, player.y);
        }
        else if(attack){ //landing
            if(dmg) knightAtkA.getCurrentFrame().getFlippedCopy(facingLeft, false).drawFlash(player.x, player.y);
            else knightAtkA.getCurrentFrame().getFlippedCopy(facingLeft, false).draw(player.x, player.y);
            knightAtkA.update(d);
            if(knightAtkA.isStopped()){
                knightAtkA.restart();
                attack=false;
            }
        }
        else if(special){
            specialAtk.getCurrentFrame().getFlippedCopy(facingLeft, false).draw(player.x-64, player.y);
            specialAtk.update(d);
            if(specialAtk.isStopped()){
                specialAtk.restart();
                special=false;
            }
        }
        else if(oldDash && !dash || dashStop){ //stopped dashing
            if(!dashStop) dashStop=true;
            dashH.getCurrentFrame().getFlippedCopy(facingLeft, false).draw(player.x-64, player.y);
            dashH.update(d);
            if(dashH.isStopped()){
                dashH.restart();
                dashS.restart();
                dashStop=false;
            }
        }
        else if(dash){ //dashing
            dashS.getCurrentFrame().getFlippedCopy(facingLeft, false).draw(player.x-64, player.y);
            dashS.update(d);
        }
        else if(ySpeed<0){ //rising
            if(!launchAnim){
                if(dmg) launch.getCurrentFrame().getFlippedCopy(facingLeft, false).drawFlash(player.x, player.y);
                else launch.getCurrentFrame().getFlippedCopy(facingLeft, false).draw(player.x, player.y);
                launch.update(d);
                if(launch.isStopped()){
                    launchAnim=true; land.setCurrentFrame(2);
                    launch.restart();
                }
            }
            else{
                if(dmg) rising.getFlippedCopy(facingLeft, false).drawFlash(player.x, player.y);
                else rising.getFlippedCopy(facingLeft, false).draw(player.x, player.y);
            }
        }
        else if(justLanded){ //landing
            if(dmg) land.getCurrentFrame().getFlippedCopy(facingLeft, false).drawFlash(player.x, player.y);
            else land.getCurrentFrame().getFlippedCopy(facingLeft, false).draw(player.x, player.y);
            land.update(d);
            if(land.isStopped()){
                land.restart();
                justLanded=false;
            }
        }
        else if(ySpeed>0){ //falling
            if(dmg) fall.getFlippedCopy(facingLeft, false).drawFlash(player.x, player.y);
            else fall.getFlippedCopy(facingLeft, false).draw(player.x, player.y);
            launchAnim=false;
        }
        else if(xSpeed>0 || xSpeed<0){
            if(dmg) runA.getCurrentFrame().getFlippedCopy(facingLeft, false).drawFlash(player.x, player.y);
            else runA.getCurrentFrame().getFlippedCopy(facingLeft, false).draw(player.x, player.y);
            runA.update(d);
        }
        else if(xSpeed==0){
            if(dmg) idle.getFlippedCopy(facingLeft, false).drawFlash(player.x, player.y);
            else idle.getFlippedCopy(facingLeft, false).draw(player.x, player.y);
        } //idle
        g.setColor(Color.yellow);
    }
        
    public int attackType(){
        if(hitAt) return 1;
        else if(hitSp) return 2;
        else return 0;
    }
    
    public boolean facing(){
        return facingLeft;
    }
    
    
    public int[] collision(int x, int y, int w, int h){

        Rectangle sensor = new Rectangle(x,y,w,h);
        int[] nope = {-1,-1};
        for(int a=0;a<42;a++){
            for(int b=0; b<42;b++){
                Rectangle p = new Rectangle(map[a][b].x, map[a][b].y,128,128);
                if(!"AIR".equals(map[a][b].blockType) &&
                   !"SAFEAIR".equals(map[a][b].blockType) &&
                   !"THORN".equals(map[a][b].blockType) &&
                   !"SPAWN".equals(map[a][b].blockType) &&
                   !"EXIT".equals(map[a][b].blockType) && p.intersects(sensor)){
                    int[] coordinate = {p.x, p.y};
                    return coordinate;
                }
            }
        }
        return nope;
    }
}