package piece;

import main.GamePanel;
import pair.Pair;

public class Rook extends ChessMan {
    public Rook(GamePanel panel, int x, int y, boolean white) {
        super(panel, x, y, white);
    }

    @Override
    public void setValue() {
        this.value = 5;
    }

    @Override
    public void setImageName() {
        this.name = "Rook";
    }

    @Override
    public void functionUpdate() {
        for (int a = 1; ; a++){
            if (this.i - a < 0){
                break;
            }
            if (panel.Board[i - a][j] == 0) {
                moves.add(new Pair<>(this.i - a, this.j));
            }
            else if (panel.Board[i - a][j] * value < 0) {
                eats.add(new Pair<>(this.i - a, this.j));
                break;
            }
            else{
                break;
            }
        }
        for (int a = 1; ; a++){
            if (this.i + a > 7){
                break;
            }
            if (panel.Board[i + a][j] == 0) {
                moves.add(new Pair<>(this.i + a, this.j));
            }
            else if (panel.Board[i + a][j] * value < 0) {
                eats.add(new Pair<>(this.i + a, this.j));
                break;
            }
            else{
                break;
            }
        }
        for (int a = 1; ; a++){
            if (this.j + a > 7){
                break;
            }
            if (panel.Board[i][j + a] == 0) {
                moves.add(new Pair<>(this.i, this.j + a));
            }
            else if (panel.Board[i][j + a] * value < 0) {
                eats.add(new Pair<>(this.i, this.j + a));
                break;
            }
            else{
                break;
            }
        }
        for (int a = 1; ; a++){
            if (this.j - a < 0){
                break;
            }
            if (panel.Board[i][j - a] == 0) {
                moves.add(new Pair<>(this.i, this.j - a));
            }
            else if (panel.Board[i][j - a] * value < 0) {
                eats.add(new Pair<>(this.i, this.j - a));
                break;
            }
            else{
                break;
            }
        }
        if (panel.castling){
            if (this.white) {
                if (this.j == 0 && panel.whiteKing.j == 2 && panel.whiteKing.moveTurn == 1) {
                    Move(7 * panel.tileSize, this.y, false);
                    panel.castling = false;
                }
                else if (this.j == 7 && panel.whiteKing.j == 6 && panel.whiteKing.moveTurn == 1) {
                    Move(9 * panel.tileSize, this.y, false);
                    panel.castling = false;
                }
            }

            else {
                if (this.j == 0 && panel.blackKing.j == 2 && panel.blackKing.moveTurn == 1) {
                    Move(7 * panel.tileSize, this.y, false);
                    panel.castling = false;
                }
                else if (this.j == 7 && panel.blackKing.j == 6 && panel.blackKing.moveTurn == 1) {
                    Move(9 * panel.tileSize, this.y, false);
                    panel.castling = false;
                }
            }
        }
    }
}
