package  piece;

import button.NextMove;
import main.GamePanel;
import main.Panel;
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
    public int i, j, x, y;
    public boolean button;
    public int value;
    ArrayList<Pair> moves = new ArrayList<>();
    ArrayList<NextMove> nextMoves = new ArrayList<>();

    public ChessMan(GamePanel panel, int x, int y){
        this.panel = panel;
        this.i = (y / panel.tileSize) - 2;
        this.j = (x / panel.tileSize) - 4;
        this.x = x;
        this.y = y;
        setValue();
        panel.Board[i][j] = this.value;
        setImageName();
        getImage();
    }

    public abstract void setValue();
    public abstract void setImageName();

    public void getImage(){
        try {
            image = ImageIO.read((getClass().getResourceAsStream("/image/" + name + ".png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract void functionUpdate();
    public void update(){
        if (panel.mouseHandles[i][j].click) {
            this.button = true;
            functionUpdate();
            for (Pair<Integer, Integer> move : moves) {
                nextMoves.add(new NextMove(this.panel, move.first, move.second, panel.tileSize, panel.tileSize));
            }
            panel.mouseHandles[i][j].click = false;
        }
        if(button){
            for (NextMove move : nextMoves){
                move.update();
                if (move.button){
                    panel.Board[i][j] = 0;
                    this.x = move.x;
                    this.y = move.y;
                    this.j = (this.x / panel.tileSize) - 4;
                    this.i = (this.y / panel.tileSize) - 2;
                    panel.Board[i][j] = panel.turn * value;
                    panel.mouseHandles[i][j].click = false;
                    this.button = false;
                    nextMoves = new ArrayList<>();
                    moves = new ArrayList<>();
                    break;
                }
            }
        }
    }

    public void draw(Graphics2D g2D) {
        g2D.drawImage(image, x, y, panel.tileSize, panel.tileSize, null);
        for (NextMove move : nextMoves){
            move.draw(g2D);
        }
    }
}
