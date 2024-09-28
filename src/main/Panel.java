package main;

import javax.swing.*;
import java.awt.*;

public abstract class Panel extends JPanel implements Runnable {

    final int originalTileSize = 16;
    final int scale = 3;
    public int tileSize = scale * originalTileSize;
    final int screenCol = 16;
    final int screenRow = 12;
    public int screenWidth = tileSize * screenCol;
    public int screenHeight = tileSize * screenRow;

    public int turn = 1;

    final int fps = 30;

    public Thread thread;

    public Frame frame;

    public Panel(Frame frame){
        this.frame = frame;
        this.setPreferredSize(new Dimension( screenWidth, screenHeight));
        this.setDoubleBuffered(true);
        this.setFocusable(true);
    }

    public void startGameThread(){
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while(thread != null) {
            update();
            repaint();
            try {
                Thread.sleep(1000/fps);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public abstract void update();

    public void paintComponent(Graphics g){
        super.paintComponent(g);
    }
}