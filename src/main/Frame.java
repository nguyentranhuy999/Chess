package main;

import javax.swing.*;

public class Frame extends JFrame {
    GamePanel gamePanel;
    Menu menu;
    public int gameState = 1;
    public boolean started = false;

    public Frame(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setTitle("Chess");
        gamePanel = new GamePanel(this);
        menu = new Menu(this);
        this.add(menu);
        menu.startGameThread();
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void update(){
        if(gameState ==1 ){
            if(started) {
                gamePanel.thread = null;
                this.remove(gamePanel);
            }
            this.add(menu);
            if (!started) {
                menu.startGameThread();
            }
            this.revalidate();
            this.repaint();
            menu.requestFocusInWindow();
        }
        else if (gameState == 2){
            this.remove(menu);
            started = true;
            gamePanel.removeAll();
            gamePanel = new GamePanel(this);
            this.add(gamePanel);
            gamePanel.startGameThread();
            this.revalidate();
            this.repaint();
            gamePanel.requestFocusInWindow();
        }
        else if (gameState == 3){
            this.remove(menu);
            this.add(gamePanel);
            gamePanel.startGameThread();
            this.revalidate();
            this.repaint();
            gamePanel.requestFocusInWindow();
        }
    }
}
