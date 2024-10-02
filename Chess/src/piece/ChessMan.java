package  piece;

import button.NextEat;
import button.NextMove;
import main.GamePanel;
import pair.Pair;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public abstract class ChessMan{
    GamePanel panel;
    BufferedImage image;
    BufferedImage checking;
    String name;
    String color;
    public int i, j, x, y;
    public boolean button;
    public boolean white;
    public boolean alive;
    public boolean king;
    public boolean isMove;
    public boolean isEat;
    public  boolean check;
    public int moveTurn;
    int count;
    public int value;
    public int xSpeed;
    public int ySpeed;
    public int xLock;
    public int yLock;
    ArrayList<Pair> moves = new ArrayList<>();
    ArrayList<Pair> eats = new ArrayList<>();
    ArrayList<NextMove> nextMoves = new ArrayList<>();
    ArrayList<NextEat> nextEats = new ArrayList<>();

    public ChessMan(GamePanel panel, int x, int y, boolean white){
        this.panel = panel;
        this.white = white;
        if(white){
            this.color = "White_";
        }
        else{
            this.color = "Black_";
        }
        this.i = (y / panel.tileSize) - 2;
        this.j = (x / panel.tileSize) - 4;
        this.x = x;
        this.y = y;
        this.xLock = x;
        this.yLock = y;
        this.xSpeed = 0;
        this.ySpeed = 0;
        this.moveTurn  = 0;
        this.count = 0;
        this.button = false;
        this.alive = true;
        this.isMove = false;
        this.isEat = false;
        this.check = false;
        setValue();
        if (!white){
            this.value = -this.value;
        }
        panel.Board[i][j] = this.value;
        setImageName();
        if (this.name == "King"){
            this.king = true;
        }
        else{
            this.king = false;
        }
        getImage();
    }

    public abstract void setValue();

    public abstract void setImageName();

    public void getImage(){
        try {
            image = ImageIO.read((getClass().getResourceAsStream("/image/" + color + name + ".png")));
            checking = ImageIO.read((getClass().getResourceAsStream("/image/Check.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract void functionUpdate();

    public void slowMove(int a){
        if (this.x == this.xLock && this.y == this.yLock) {
            this.xSpeed = 0;
            this.ySpeed = 0;
            panel.moving = false;
            if (a == 1) {
                this.isMove = false;
            }
            else if (a == 0){
                this.isEat = false;
            }
            panel.eventSound(a);
        }
        else {
            this.x = this.x + this.xSpeed;
            this.y = this.y + this.ySpeed;
        }
    }

    public void Move(int x1, int y1){
        if (!panel.castling) {
            panel.turn = panel.turn * -1;
        }
        panel.Board[i][j] = 0;
        this.xSpeed = (x1 - this.x) / panel.fps;
        this.ySpeed = (y1 - this.y) / panel.fps;
        this.xLock = x1;
        this.yLock = y1;
        this.j = (this.xLock / panel.tileSize) - 4;
        this.i = (this.yLock / panel.tileSize) - 2;
        panel.Board[i][j] = value;
        panel.moving = true;
        this.isMove = true;
        panel.mouseHandles[i][j].click = false;
        this.button = false;
        this.moveTurn++;
    }

    public void update(){
        if(panel.Board[i][j] * this.value <= 0){
            if (!panel.moving) {
                this.alive = false;
                if (this.king) {
                    panel.end = true;
                }
            }
        }

        if (this.isMove) {
            slowMove(1);
        }
        if (this.isEat) {
            slowMove(0);
        }

        moves = new ArrayList<>();
        eats = new ArrayList<>();
        functionUpdate();

        if (this.alive && panel.turn * this.value > 0) {
            if (this.check && !this.king){
                this.check = false;
            }

            if (panel.mouseHandles[i][j].click && !panel.moving) {
                this.button = true;
                nextMoves = new ArrayList<>();
                nextEats = new ArrayList<>();
                for (Pair<Integer, Integer> move : moves) {
                    NextMove nextMove = new NextMove(this.panel, (move.second + 4) * panel.tileSize, (move.first + 2) * panel.tileSize, panel.tileSize, panel.tileSize);
                    if (move.special1){
                        nextMove.special1 = true;
                    }
                    if (move.special3){
                        nextMove.special2 = true;
                    }
                    nextMoves.add(nextMove);
                }
                for (Pair<Integer, Integer> eat : eats) {
                    NextEat nextEat = new NextEat(this.panel, (eat.second + 4) * panel.tileSize, (eat.first + 2) * panel.tileSize, panel.tileSize, panel.tileSize);
                    if (eat.special2){
                        nextEat.special = true;
                    }
                    nextEats.add(nextEat);
                }
            }

            if (button) {
                for (NextMove move : nextMoves) {
                    move.update();
                    if (move.button) {
                        Move(move.x, move.y);
                        if (move.special1){
                            this.check = true;
                        }
                        if (move.special2){
                            panel.castling = true;
                        }
                        break;
                    }
                }
                for (NextEat eat : nextEats) {
                    eat.update();
                    if (eat.button) {
                        panel.turn = panel.turn * -1;
                        panel.Board[i][j] = 0;
                        if (eat.special){
                            eat.y = eat.y + panel.turn * panel.tileSize;
                        }
                        this.xSpeed = (eat.x - this.x) / panel.fps;
                        this.ySpeed =  (eat.y - this.y) / panel.fps;
                        this.xLock = eat.x;
                        this.yLock = eat.y;
                        this.j = (this.xLock / panel.tileSize) - 4;
                        this.i = (this.yLock / panel.tileSize) - 2;
                        panel.Board[i][j] = value;
                        if (eat.special){
                            panel.Board[i - panel.turn][j] = 0;
                        }
                        panel.moving = true;
                        this.isEat = true;
                        panel.mouseHandles[i][j].click = false;
                        this.button = false;
                        this.moveTurn++;
                        break;
                    }
                }
            }
            if (!panel.moving) {
                for (int a = 0; a < 8; a++) {
                    for (int b = 0; b < 8; b++) {
                        if (panel.mouseHandles[a][b].click) {
                            if (a == this.i && b == this.j) {
                                this.button = true;
                            } else {
                                this.button = false;
                            }
                        }
                    }
                }
            }
        }
    }

    public void draw(Graphics2D g2D) {
        if(this.alive) {
            g2D.drawImage(image, x, y, panel.tileSize, panel.tileSize, null);
            if (this.check && this.king){
                if (count == 6 || count == 7 || count == 8) {
                    g2D.drawImage(checking, x, y, panel.tileSize, panel.tileSize, null);
                    if (count == 8){
                        count = 0;
                    }
                }
                count++;
            }
            if (button) {
                for (NextMove move : nextMoves) {
                    move.draw(g2D);
                }
                for (NextEat eat : nextEats) {
                    eat.draw(g2D);
                }
            }
        }
    }
}
