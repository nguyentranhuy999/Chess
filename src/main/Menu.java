package main;

import button.ContinueButton;
import button.ExitButton;
import button.NewGameButton;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Menu extends Panel{
    NewGameButton newGameButton;
    ContinueButton continueButton;
    ExitButton exitButton;
    BufferedImage background, whiteKing, blackKing;

    public Menu(Frame frame){
        super(frame);
        this.newGameButton = new NewGameButton(this,(13 * tileSize) / 2,4 * tileSize,3 * tileSize, tileSize);
        this.continueButton = new ContinueButton(this,(13 * tileSize)/ 2,  (11 * tileSize) / 2, 3 * tileSize, tileSize);
        this.exitButton = new ExitButton(this, (13 * tileSize)/ 2, 7 * tileSize, 3 * tileSize, tileSize);
        getImage();
    }

    public void getImage(){
        try {
            background = ImageIO.read((getClass().getResourceAsStream("/image/BackGround.png")));
            whiteKing = ImageIO.read((getClass().getResourceAsStream("/image/White_King.png")));
            blackKing = ImageIO.read((getClass().getResourceAsStream("/image/Black_King.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(){
        newGameButton.update();
        continueButton.update();
        exitButton.update();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2D =(Graphics2D) g;
        g2D.drawImage(background,0, 0, screenWidth, screenHeight, null);
        g2D.drawImage(whiteKing,15,125,6 * tileSize, 6 * tileSize, null);
        g2D.drawImage(blackKing,465,125,6 * tileSize, 6 * tileSize, null);
        newGameButton.draw(g2D);
        continueButton.draw(g2D);
        exitButton.draw(g2D);
        g2D.dispose();
    }
}
