package piece;

import main.GamePanel;
import pair.Pair;

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
            if (this.firstMove) {
                if (panel.Board[i - 2][j] == 0 && panel.Board[i - 1][j] == 0) {
                    moves.add(new Pair<>(this.i - 2, this.j));
                }
            }
            if (this.i - 1 > -1) {
                if (panel.Board[i - 1][j] == 0) {
                    moves.add(new Pair<>(this.i - 1, this.j));
                }
            }
            if (this.i - 1 > -1 && this.j - 1 > -1) {
                if (panel.Board[i - 1][j - 1] * value < 0) {
                    eats.add(new Pair<>(this.i - 1, this.j - 1));
                }
            }
            if (this.i - 1 > -1 && this.j + 1 < 8) {
                if (panel.Board[i - 1][j + 1] * value < 0) {
                    eats.add(new Pair<>(this.i - 1, this.j + 1));
                }
            }
        }
        else{
            if (this.firstMove) {
                if (panel.Board[i + 2][j] == 0  && panel.Board[i + 1][j] == 0) {
                    moves.add(new Pair<>(this.i + 2, this.j));
                }
            }
            if (this.i + 1 < 8) {
                if (panel.Board[i + 1][j] == 0) {
                    moves.add(new Pair<>(this.i + 1, this.j));
                }
            }
            if (this.i + 1 < 8 && this.j - 1 > -1) {
                if (panel.Board[i + 1][j - 1] * value < 0) {
                    eats.add(new Pair<>(this.i + 1, this.j - 1));
                }
            }
            if (this.i + 1 < 8 && this.j + 1 < 8) {
                if (panel.Board[i + 1][j + 1] * value < 0) {
                    eats.add(new Pair<>(this.i + 1, this.j + 1));
                }
            }
        }
    }
}
