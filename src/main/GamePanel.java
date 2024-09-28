package main;

import button.MenuButton;
import piece.King;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GamePanel extends Panel {
    MenuButton menuButton;
    BufferedImage board, queue;
    double boardScale = 1;
    public int [][] Board = new int[8][8];

    public MouseHandle [][] mouseHandles = new MouseHandle[8][8];

    King king1, king2;

    public GamePanel(Frame frame) {
        super(frame);
        this.menuButton = new MenuButton(this,0,0,2 * tileSize,tileSize);
        this.setBackground(Color.BLACK);
        for (int i = 0; i < 8; i++ ) {
            for (int j = 0; j < 8; j++) {
                mouseHandles[i][j] = new MouseHandle((j + 4) * tileSize, (i + 2) * tileSize, tileSize, tileSize);
                this.addMouseListener(mouseHandles[i][j]);
            }
        }
        this.king1 = new King(this, 4 * tileSize,  2 * tileSize);
        this.king2 = new King(this, 5 * tileSize,  2 * tileSize);
        getImage();
    }

    public void getImage(){
        try {
            board = ImageIO.read((getClass().getResourceAsStream("/image/Board.png")));
            queue = ImageIO.read((getClass().getResourceAsStream("/image/Queue.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(){
        menuButton.update();
        king1.update();
        king2.update();
        System.out.println(Board[2][2]);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2D =(Graphics2D) g;
        menuButton.draw(g2D);
        g2D.drawImage(board, 4 * tileSize, 2 * tileSize, (int) (8 * boardScale * tileSize), (int) (8 * boardScale *tileSize), null);
        for (int i = 2; i < 10; i ++){
            for (int j = 1; j <= 2; j++){
                g2D.drawImage(queue, j * tileSize, i * tileSize, tileSize, tileSize, null);
                g2D.drawImage(queue, (j + 12) * tileSize, i * tileSize, tileSize, tileSize, null);
            }
        }
        king1.draw(g2D);
        king2.draw(g2D);
        g2D.dispose();
    }
}

