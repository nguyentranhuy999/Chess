package button;

import main.Panel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class PromotionButton extends Button {
    BufferedImage whiteQueen, blackQueen, whiteRook, blackRook, whiteKnight, blackKnight, whiteBishop, blackBishop;
    public PromotionButton(Panel panel, int x, int y, int width, int height) {
        super(panel, x, y, width, height);
        getImage1();
    }

    @Override
    public void setImageName() {
        this.name1 = "Promotion1";
        this.name2 = "Promotion2";
    }

    public void getImage1(){
        try {
            whiteQueen = ImageIO.read((getClass().getResourceAsStream("/image/White_Queen.png")));
            whiteRook = ImageIO.read((getClass().getResourceAsStream("/image/White_Rook.png")));
            whiteKnight = ImageIO.read((getClass().getResourceAsStream("/image/White_Knight.png")));
            whiteBishop = ImageIO.read((getClass().getResourceAsStream("/image/White_Bishop.png")));
            blackQueen = ImageIO.read((getClass().getResourceAsStream("/image/Black_Queen.png")));
            blackRook = ImageIO.read((getClass().getResourceAsStream("/image/Black_Rook.png")));
            blackKnight = ImageIO.read((getClass().getResourceAsStream("/image/Black_Knight.png")));
            blackBishop = ImageIO.read((getClass().getResourceAsStream("/image/Black_Bishop.png")));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void functionUpdate() {

    }

    @Override
    public void draw(Graphics2D g2D) {
        g2D.drawImage(image, x, y, width, height, null);
        if (this.x == 6 * panel.tileSize){
            if (this.y == 1 * panel.tileSize){
                g2D.drawImage(whiteQueen, x, y, width, height, null);
            }
            else if (this. y == 10 * panel.tileSize){
                g2D.drawImage(blackQueen, x, y, width, height, null);
            }
        }
        else if (this.x == 7 * panel.tileSize){
            if (this.y == 1 * panel.tileSize){
                g2D.drawImage(whiteRook, x, y, width, height, null);
            }
            else if (this. y == 10 * panel.tileSize){
                g2D.drawImage(blackRook, x, y, width, height, null);
            }
        }
        else if (this.x == 8 * panel.tileSize){
            if (this.y == 1 * panel.tileSize){
                g2D.drawImage(whiteKnight, x, y, width, height, null);
            }
            else if (this. y == 10 * panel.tileSize){
                g2D.drawImage(blackKnight, x, y, width, height, null);
            }
        }
        else if (this.x == 9 * panel.tileSize){
            if (this.y == 1 * panel.tileSize){
                g2D.drawImage(whiteBishop, x, y, width, height, null);
            }
            else if (this. y == 10 * panel.tileSize){
                g2D.drawImage(blackBishop, x, y, width, height, null);
            }
        }
    }
}
