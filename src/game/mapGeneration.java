package game;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class mapGeneration {
    
        int mapSize = 4;
        Random r = new Random();
        int[][] layout = new int[mapSize][mapSize];
        boolean startRoom = false, specialRoom=false;
        String[][] mapKey;
                
    public platform[][] generate(){
         
        for(int a=0;a<mapSize;a++) for(int b=0;b<mapSize;b++) layout[a][b]=0;
        int y = 0, x = r.nextInt(mapSize), z;
        while(layout[y][x]!=6){
            z = r.nextInt(19)+1; //ROOM TYPE
            if(!startRoom){
                layout[y][x] = 5;
                startRoom = true;
                if(z<10 && x-1>=0 || x+1>=mapSize) x--;
                else x++;
            }
            else if(y-1>=0 && layout[y-1][x]==2){ //TOP IS 2, MUST BE 2/3
                if(x-1>=0 && z<10){ //LEFT
                    layout[y][x] = 3;
                    x--; 
                }
                else if(x+1<mapSize && z<18){ //RIGHT
                    layout[y][x] = 3;
                    x++;
                }
                else if(y+1<mapSize){ //DOWN
                    layout[y][x] = 2;
                    y++;
                }
            }
            else if(z<10 && x-1>=0 && layout[y][x-1]==0){ //TYPE 1, NEXT IS TO THE LEFT
                layout[y][x]=1;
                x--;
            }
            else if(z<19 && x+1<mapSize  && layout[y][x+1]==0){ //TYPE 1, NEXT IS TO THE RIGHT
                layout[y][x]=1;
                x++;
            }
            else if(y+1<mapSize){ //TYPE 2, NEXT IS BELOW
                layout[y][x]=2;
                y++;
            }
            else layout[y][x]=6;
         }

        for(int a=0;a<mapSize;a++) for(int b=0;b<mapSize;b++) if(layout[a][b]==0){ //fill empty
            z=r.nextInt(4);
            if(z!=2)layout[a][b]=z;
            if(layout[a][b]==0 && !specialRoom && r.nextInt(10)+1>7){
                if(r.nextInt(10)>6) layout[a][b] = 4;
                else layout[a][b] = 7;
                specialRoom = true;
            }
        }
        
        List<List<String[][]>> mapY = new ArrayList<>();
        List<String[][]> mapX = new ArrayList<>();
        mapRooms room = new mapRooms();

            for(int a=0;a<mapSize;a++){
                for(int b=0;b<mapSize;b++){
                    switch (layout[a][b]) {
                        case 1: mapX.add(room.typeOne()); break;
                        case 2: mapX.add(room.typeTwo()); break;
                        case 3: mapX.add(room.typeThree()); break;
                        case 4: mapX.add(room.typeFour()); break;
                        case 5: mapX.add(room.typeFive()); break;
                        case 6: mapX.add(room.typeSix()); break;
                        case 7: mapX.add(room.typeSeven()); break;
                        case 0: mapX.add(room.typeZero()); break;

                    }
                }
                mapY.add(clone(mapX, mapSize));
                mapX.clear();
            }
            
            mapKey = new String[42][42];
            for(int c=0;c<42;c++){ //outer border
                mapKey[0][c] = "#";
                mapKey[41][c] = "#";
                mapKey[c][0] = "#";
                mapKey[c][41] = "#";
            }
            
            int xVal=0, yVal=0; String currentPiece;
            for(int Ya=0;Ya<mapSize;Ya++){
                for(int Yb=0;Yb<10;Yb++){
                    for(int Xa=0;Xa<mapSize;Xa++){
                        for(int Xb=0;Xb<10;Xb++){
                            currentPiece = mapY.get(Ya).get(Xa)[Yb][Xb];
                            System.out.print(currentPiece);
                            switch(currentPiece){
                                case "@": if(r.nextInt(10)>3)currentPiece = "#"; else currentPiece = " "; break;
                                case "Q": if(r.nextInt(10)>7)currentPiece = "Q"; else currentPiece = " "; break;
                                case "N": if(r.nextInt(10)>5)currentPiece = "#"; else currentPiece = "V"; break;
                            }
                            mapKey[yVal+1][xVal+1] = currentPiece;
                            if(xVal+1==40){ xVal=0; yVal++;}
                            else xVal++;
                        }
                    }
                    System.out.println();
                }
            }
            
        platform[][] finalMap = new platform[42][42];
        for(int yC=0;yC<42;yC++) for(int xC=0;xC<42;xC++) finalMap[yC][xC] = new platform(xC*127, yC*127, key(xC, yC));
        return finalMap;
    }
    
    public String key(int x, int y){
        String type="";
        if(mapKey[y][x].equals("#")){
            boolean A=false,B=false,C=false,D=false,E=false,F=false,G=false,H=false;
            if(!(y>0) || y>0 && mapKey[y-1][x].equals("#")) B=true;
            if(!(x>0) || x>0 && mapKey[y][x-1].equals("#")) D=true;
            if(!(x<41) || x<41 && mapKey[y][x+1].equals("#")) E=true;
            if(!(y<41)|| y<41 && mapKey[y+1][x].equals("#")) G=true;
            if(!(y>0 && x>0) || y>0 && x>0 && B && D && mapKey[y-1][x-1].equals("#")) A=true;
            if(!(y>0 && x<41) || y>0 && x<41 && B && E && mapKey[y-1][x+1].equals("#")) C=true;
            if(!(y<41 && x>0) || y<41 && x>0 && D && G && mapKey[y+1][x-1].equals("#")) F=true;
            if(!(y<41 && x<41) || y<41 && x<41 && G && E && mapKey[y+1][x+1].equals("#")) H=true;
            if(A) type+="A";
            if(B) type+="B";
            if(C) type+="C";
            if(D) type+="D";
            if(E) type+="E";
            if(F) type+="F";
            if(G) type+="G";
            if(H) type+="H";
            if(type.equals("")) type="NONE";
        }
        else if(mapKey[y][x].equals(".")) type="SAFEAIR";
        else if(mapKey[y][x].equals("X")) type="QUEST";
        else if(mapKey[y][x].equals("O")) type="INVIS";
        else if(mapKey[y][x].equals("Y")) type="MERCHANT";
        else if(mapKey[y][x].equals("S")) type="SPAWN";
        else if(mapKey[y][x].equals("V")) type="THORN";
        else if(mapKey[y][x].equals("E")) type="EXIT";
        else type="AIR";
        
        return type;
    }
    
    public static List<String[][]> clone(List<String[][]> a, int mapSize){
        List<String[][]> b = new ArrayList<>();
        for(int c=0;c<mapSize;c++) b.add(a.get(c));
        return b;
    }
}