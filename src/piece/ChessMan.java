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
    String name;
    String color;
    public int i, j, x, y;
    public boolean button;
    public boolean firstMove;
    public boolean white;
    public boolean alive;
    public int value;
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
        this.button = false;
        this.firstMove = true;
        this.alive = true;
        setValue();
        if (!white){
            this.value = 0 - this.value;
        }
        panel.Board[i][j] = this.value;
        setImageName();
        getImage();
    }

    public abstract void setValue();
    public abstract void setImageName();

    public void getImage(){
        try {
            image = ImageIO.read((getClass().getResourceAsStream("/image/" + color + name + ".png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract void functionUpdate();

    public void update(){
        if(panel.Board[i][j] * this.value < 0){
            this.alive = false;
        }
        if (this.alive && panel.turn * this.value > 0) {
            if (panel.mouseHandles[i][j].click) {
                this.button = true;
                nextMoves = new ArrayList<>();
                moves = new ArrayList<>();
                nextEats = new ArrayList<>();
                eats = new ArrayList<>();
                functionUpdate();
                for (Pair<Integer, Integer> move : moves) {
                    nextMoves.add(new NextMove(this.panel, (move.second + 4) * panel.tileSize, (move.first + 2) * panel.tileSize, panel.tileSize, panel.tileSize));
                }
                for (Pair<Integer, Integer> eat : eats) {
                    nextEats.add(new NextEat(this.panel, (eat.second + 4) * panel.tileSize, (eat.first + 2) * panel.tileSize, panel.tileSize, panel.tileSize));
                }
            }

            if (button) {
                for (NextMove move : nextMoves) {
                    move.update();
                    if (move.button) {
                        panel.turn = panel.turn * -1;
                        panel.Board[i][j] = 0;
                        this.x = move.x;
                        this.y = move.y;
                        this.j = (this.x / panel.tileSize) - 4;
                        this.i = (this.y / panel.tileSize) - 2;
                        panel.Board[i][j] = value;
                        panel.mouseHandles[i][j].click = false;
                        this.button = false;
                        this.firstMove = false;
                        break;
                    }
                }
                for (NextEat eat : nextEats) {
                    eat.update();
                    if (eat.button) {
                        panel.turn = panel.turn * -1;
                        panel.Board[i][j] = 0;
                        this.x = eat.x;
                        this.y = eat.y;
                        this.j = (this.x / panel.tileSize) - 4;
                        this.i = (this.y / panel.tileSize) - 2;
                        panel.Board[i][j] = value;
                        panel.mouseHandles[i][j].click = false;
                        this.button = false;
                        this.firstMove = false;
                        break;
                    }
                }
            }

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

    public void draw(Graphics2D g2D) {
        if(this.alive) {
            g2D.drawImage(image, x, y, panel.tileSize, panel.tileSize, null);
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
