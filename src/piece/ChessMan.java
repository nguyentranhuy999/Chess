package  piece;

import button.NextEat;
import button.NextMove;
import button.PromotionButton;
import main.GamePanel;
import pair.Pair;

import javax.imageio.ImageIO;
import javax.swing.text.StyledEditorKit;
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
    public boolean check1;
    public int moveTurn;
    int count;
    public int value;
    public int xSpeed;
    public int ySpeed;
    public int xLock;
    public int yLock;
    ArrayList<Pair<Integer, Integer>> moves = new ArrayList<>();
    ArrayList<Pair<Integer, Integer>> eats = new ArrayList<>();
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
        this.check1 = true;
        setValue();
        if (!white){
            this.value = -this.value;
        }
        panel.Board[i][j] = this.value;
        setImageName();
        if ("King".equals(this.name)){
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

    public void refreshMoveSet() {
        moves = new ArrayList<>();
        eats = new ArrayList<>();
        functionUpdate();
    }

    public ArrayList<Pair<Integer, Integer>> getMoves() {
        return moves;
    }

    public ArrayList<Pair<Integer, Integer>> getEats() {
        return eats;
    }

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

    public void Move(int x1, int y1, boolean special){
        if (!panel.castling) {
            panel.turn = panel.turn * -1;
        }
        panel.Board[i][j] = 0;
        if (special){
            y1 = y1 + panel.tileSize * panel.turn;
        }
        this.xSpeed = (x1 - this.x) / panel.fps;
        this.ySpeed = (y1 - this.y) / panel.fps;
        this.xLock = x1;
        this.yLock = y1;
        this.j = (this.xLock / panel.tileSize) - 4;
        this.i = (this.yLock / panel.tileSize) - 2;
        if (panel.Board[i][j] != 0 || special){
            this.isEat = true;
        }
        else{
            this.isMove = true;
        }
        panel.Board[i][j] = value;
        if (special){
            panel.Board[i - panel.turn][j] = 0;
        }
        panel.moving = true;
        panel.mouseHandles[i][j].click = false;
        this.button = false;
        this.moveTurn++;
    }

    public boolean checkMove(int i1, int j1, boolean special){
        int a = panel.Board[i1][j1];
        panel.Board[i][j] = 0;
        if (special){
            panel.Board[i1][j1] = 0;
            panel.Board[i1 - panel.turn][j1] = this.value;
        }
        else {
            panel.Board[i1][j1] = this.value;
        }

        for (ChessMan chessMan : panel.chessMans){
            if (chessMan.value * this.value < 0) {
                chessMan.moves = new ArrayList<>();
                chessMan.eats = new ArrayList<>();
                if (panel.Board[chessMan.i][chessMan.j] * chessMan.value > 0) {
                    chessMan.functionUpdate();
                }
                for (Pair<Integer, Integer> eat : chessMan.eats) {
                    if (this.value > 0) {
                        if (panel.Board[eat.first][eat.second] == 1000) {
                            panel.Board[i][j] = this.value;
                            panel.Board[i1][j1] = a;
                            if (special){
                                panel.Board[i1 - panel.turn][j1] = 0;
                            }
                            return false;
                        }
                    } else {
                        if (panel.Board[eat.first][eat.second] == -1000) {
                            panel.Board[i][j] = this.value;
                            panel.Board[i1][j1] = a;
                            if (special){
                                panel.Board[i1 - panel.turn][j1] = 0;
                            }
                            return false;
                        }
                    }
                }
            }
        }
        panel.Board[i][j] = this.value;
        panel.Board[i1][j1] = a;
        if (special){
            panel.Board[i1 - panel.turn][j1] = 0;
        }
        return true;
    }

    public void update(){
        if(panel.Board[i][j] * this.value <= 0){
            if (!panel.moving) {
                this.alive = false;
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

        if (this.alive && panel.turn * this.value > 0 && !panel.promotion) {
            if (this.check && !this.king){
                this.check = false;
            }

            if (panel.mouseHandles[i][j].click && !panel.moving) {
                this.button = true;
                nextMoves = new ArrayList<>();
                nextEats = new ArrayList<>();
                for (Pair<Integer, Integer> move : moves) {
                    if (checkMove(move.first, move.second, false)) {
                        NextMove nextMove = new NextMove(this.panel, (move.second + 4) * panel.tileSize, (move.first + 2) * panel.tileSize, panel.tileSize, panel.tileSize);
                        if (move.special1){
                            nextMove.special1 = true;
                        }
                        if (move.special3) {
                            nextMove.special2 = true;
                        }
                        if (move.special4){
                            nextMove.special3 = true;
                        }
                        nextMoves.add(nextMove);
                    }
                }
                for (Pair<Integer, Integer> eat : eats) {
                    if (checkMove(eat.first, eat.second, eat.special2)) {
                        NextEat nextEat = new NextEat(this.panel, (eat.second + 4) * panel.tileSize, (eat.first + 2) * panel.tileSize, panel.tileSize, panel.tileSize);
                        if (eat.special2) {
                            nextEat.special = true;
                        }
                        if (eat.special4){
                            nextEat.special1 = true;
                        }
                        nextEats.add(nextEat);
                    }
                }
            }

            if (button) {
                for (NextMove nextMove : nextMoves) {
                    nextMove.update();
                    if (nextMove.button) {
                        Move(nextMove.x, nextMove.y, false);
                        if (nextMove.special1) {
                            this.check = true;
                        }
                        if (nextMove.special2) {
                            panel.castling = true;
                        }
                        if (nextMove.special3){
                            panel.turn = panel.turn * -1;
                            panel.promotion = true;
                        }
                        break;
                    }
                }
                for (NextEat nextEat : nextEats) {
                    nextEat.update();
                    if (nextEat.button) {
                        Move(nextEat.x, nextEat.y, nextEat.special);
                        if(nextEat.special1){
                            panel.turn = panel.turn * -1;
                            panel.promotion = true;
                        }
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
        if (this.check1) {
            if (this.king && this.check) {
                if (!panel.moving) {
                    panel.end = true;
                    for (ChessMan chessMan : panel.chessMans) {
                        if (chessMan.value * this.value > 0) {
                            chessMan.nextMoves = new ArrayList<>();
                            chessMan.nextEats = new ArrayList<>();
                            if (chessMan.value * panel.Board[chessMan.i][chessMan.j] > 0) {
                                for (Pair<Integer, Integer> move : chessMan.moves) {
                                    if (chessMan.checkMove(move.first, move.second, false)) {
                                        NextMove nextMove = new NextMove(this.panel, (move.second + 4) * panel.tileSize, (move.first + 2) * panel.tileSize, panel.tileSize, panel.tileSize);
                                        if (move.special1) {
                                            nextMove.special1 = true;
                                        }
                                        if (move.special3) {
                                            nextMove.special2 = true;
                                        }
                                        if (move.special4) {
                                            nextMove.special3 = true;
                                        }
                                        chessMan.nextMoves.add(nextMove);
                                    }
                                }
                                for (Pair<Integer, Integer> eat : chessMan.eats) {
                                    if (chessMan.checkMove(eat.first, eat.second, eat.special2)) {
                                        NextEat nextEat = new NextEat(this.panel, (eat.second + 4) * panel.tileSize, (eat.first + 2) * panel.tileSize, panel.tileSize, panel.tileSize);
                                        if (eat.special2) {
                                            nextEat.special = true;
                                        }
                                        if (eat.special4) {
                                            nextEat.special1 = true;
                                        }
                                        chessMan.nextEats.add(nextEat);
                                    }
                                }
                            }
                            if (chessMan.nextMoves.size() > 0 || chessMan.nextEats.size() > 0) {
                                panel.end = false;
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (this.value * panel.turn > 0 && !panel.moving){
            this.check1 = false;
        }
        else {
            check1 = true;
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
