package piece;

import button.NextEat;
import button.NextMove;
import button.PromotionButton;
import main.GamePanel;
import pair.Pair;

import java.awt.*;

public class Pawn extends ChessMan{
    public Pawn(GamePanel panel, int x, int y, boolean white) {
        super(panel, x, y, white);
    }

    @Override
    public void setValue() {
        this.value = 1;
    }

    @Override
    public void setImageName() {
        this.name = "Pawn";
    }

    @Override
    public void functionUpdate() {
        if (white) {
            if (this.moveTurn == 0) {
                if (panel.Board[i - 2][j] == 0 && panel.Board[i - 1][j] == 0) {
                    Pair<Integer,Integer> pair = new Pair<>(this.i - 2, this.j);
                    pair.special1 = true;
                    moves.add(pair);
                }
            }
            if (this.i - 1 > -1) {
                if (panel.Board[i - 1][j] == 0) {
                    Pair<Integer, Integer> pair = new Pair<>(this.i - 1, this.j);
                    if (i - 1 == 0){
                        pair.special4 = true;
                    }
                    moves.add(pair);
                }
            }

            if(this.j - 1 > -1){
                if (panel.Board[i][j - 1] == -1){
                    if (panel.blackPawn[j - 1].check){
                        Pair<Integer,Integer> pair = new Pair<>(this.i, this.j - 1);
                        pair.special2 = true;
                        eats.add(pair);
                    }
                }
            }
            if(this.j + 1 < 8){
                if (panel.Board[i][j + 1] == -1){
                    if (panel.blackPawn[j + 1].check){
                        Pair<Integer,Integer> pair = new Pair<>(this.i, this.j + 1);
                        pair.special2 = true;
                        eats.add(pair);
                    }
                }
            }

            if (this.i - 1 > -1 && this.j - 1 > -1) {
                if (panel.Board[i - 1][j - 1] * value < 0) {
                    Pair<Integer, Integer> pair = new Pair<>(this.i - 1, this.j - 1);
                    if (i - 1 == 0){
                        pair.special4 = true;
                    }
                    eats.add(pair);
                }
            }
            if (this.i - 1 > -1 && this.j + 1 < 8) {
                if (panel.Board[i - 1][j + 1] * value < 0) {
                    Pair<Integer, Integer> pair = new Pair<>(this.i - 1, this.j + 1);
                    if (i - 1 == 0){
                        pair.special4 = true;
                    }
                    eats.add(pair);
                }
            }
        }
        else{
            if (this.moveTurn == 0) {
                if (panel.Board[i + 2][j] == 0  && panel.Board[i + 1][j] == 0) {
                    Pair<Integer,Integer> pair = new Pair<>(this.i + 2, this.j);
                    pair.special1 = true;
                    moves.add(pair);
                }
            }
            if (this.i + 1 < 8) {
                if (panel.Board[i + 1][j] == 0) {
                    Pair<Integer, Integer> pair = new Pair<>(this.i + 1, this.j);
                    if (i + 1 == 7){
                        pair.special4 = true;
                    }
                    moves.add(pair);
                }
            }

            if(this.j - 1 > -1){
                if (panel.Board[i][j - 1] == 1){
                    if (panel.whitePawn[j - 1].check){
                        Pair<Integer,Integer> pair = new Pair<>(this.i, this.j - 1);
                        pair.special2 = true;
                        eats.add(pair);
                    }
                }
            }
            if(this.j + 1 < 8){
                if (panel.Board[i][j + 1] == 1){
                    if (panel.whitePawn[j + 1].check){
                        Pair<Integer,Integer> pair = new Pair<>(this.i, this.j + 1);
                        pair.special2 = true;
                        eats.add(pair);
                    }
                }
            }

            if (this.i + 1 < 8 && this.j - 1 > -1) {
                if (panel.Board[i + 1][j - 1] * value < 0) {
                    Pair<Integer, Integer> pair = new Pair<>(this.i + 1, this.j - 1);
                    if (i + 1 == 7){
                        pair.special4 = true;
                    }
                    eats.add(pair);
                }
            }
            if (this.i + 1 < 8 && this.j + 1 < 8) {
                if (panel.Board[i + 1][j + 1] * value < 0) {
                    Pair<Integer, Integer> pair = new Pair<>(this.i + 1, this.j + 1);
                    if (i + 1 == 7){
                        pair.special4 = true;
                    }
                    eats.add(pair);
                }
            }
        }
        if (panel.promotion && !panel.moving){
            if (panel.turn == 1 && this.i == 0){
                for (int a = 0; a < 4; a++){
                    panel.promotionButtons[a].update();
                    if (panel.promotionButtons[a].button){
                        this.alive = false;
                        panel.promotion = false;
                        panel.promotionButtons[a].button = false;
                        if (a == 0) {
                            panel.Board[this.i][this.j] = 9;
                            panel.chessMans.add(new Queen(panel, this.x, this.y, true));
                        }
                        else if (a == 1){
                            panel.Board[this.i][this.j] = 5;
                            panel.chessMans.add(new Rook(panel, this.x, this.y, true));
                        }
                        else if (a == 2){
                            panel.Board[this.i][this.j] = 3;
                            panel.chessMans.add(new Knight(panel, this.x, this.y, true));
                        }
                        else if (a == 3){
                            panel.Board[this.i][this.j] = 3;
                            panel.chessMans.add(new Bishop(panel, this.x, this.y, true));
                        }
                        panel.turn = panel.turn * -1;
                    }
                }
            }
            else if (panel.turn == -1 && this.i == 7){
                for (int a = 4; a < 8; a++){
                    panel.promotionButtons[a].update();
                    if (panel.promotionButtons[a].button){
                        this.alive = false;
                        panel.promotion = false;
                        panel.promotionButtons[a].button = false;
                        if (a == 4) {
                            panel.Board[this.i][this.j] = -9;
                            panel.chessMans.add(new Queen(panel, this.x, this.y, false));
                        }
                        else if (a == 5){
                            panel.Board[this.i][this.j] = -5;
                            panel.chessMans.add(new Rook(panel, this.x, this.y, false));
                        }
                        else if (a == 6){
                            panel.Board[this.i][this.j] = -3;
                            panel.chessMans.add(new Knight(panel, this.x, this.y, false));
                        }
                        else if (a == 7){
                            panel.Board[this.i][this.j] = -3;
                            panel.chessMans.add(new Bishop(panel, this.x, this.y, false));
                        }
                        panel.turn = panel.turn * -1;
                    }
                }
            }
        }
    }
}
