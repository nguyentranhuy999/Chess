package piece;

import main.GamePanel;
import main.Panel;
import pair.Pair;

public class King extends ChessMan {
    public boolean check1;

    public King(GamePanel panel, int x, int y, boolean white) {

        super(panel, x, y, white);
        this.check1 = false;
    }

    @Override
    public void setImageName() {
        this.name = "King";
    }

    @Override
    public void setValue() {
        this.value = 1000;
    }

    @Override
    public void functionUpdate() {
        if (this.j + 1 < 8) {
            if (panel.Board[i][j + 1] == 0) {
                moves.add(new Pair<>(this.i, this.j + 1));
            } else if (panel.Board[i][j + 1] * value < 0) {
                eats.add(new Pair<>(this.i, this.j + 1));
            }
        }
        if (this.i + 1 < 8) {
            if (panel.Board[i + 1][j] == 0) {
                moves.add(new Pair<>(this.i + 1, this.j));
            } else if (panel.Board[i + 1][j] * value < 0) {
                eats.add(new Pair<>(this.i + 1, this.j));
            }
        }
        if (this.j + 1 < 8 && this.i + 1 < 8) {
            if (panel.Board[i + 1][j + 1] == 0) {
                moves.add(new Pair<>(this.i + 1, this.j + 1));
            } else if (panel.Board[i + 1][j + 1] * value < 0) {
                eats.add(new Pair<>(this.i + 1, this.j + 1));
            }

        }
        if (this.j - 1 > -1) {
            if (panel.Board[i][j - 1] == 0) {
                moves.add(new Pair<>(this.i, this.j - 1));
            } else if (panel.Board[i][j - 1] * value < 0) {
                eats.add(new Pair<>(this.i, this.j - 1));
            }
        }
        if (this.i - 1 > -1) {
            if (panel.Board[i - 1][j] == 0) {
                moves.add(new Pair<>(this.i - 1, this.j));
            } else if (panel.Board[i - 1][j] * value < 0) {
                eats.add(new Pair<>(this.i - 1, this.j));
            }
        }
        if (this.j - 1 > -1 && this.i - 1 > -1) {
            if (panel.Board[i - 1][j - 1] == 0) {
                moves.add(new Pair<>(this.i - 1, this.j - 1));
            } else if (panel.Board[i - 1][j - 1] * value < 0) {
                eats.add(new Pair<>(this.i - 1, this.j - 1));
            }
        }
        if (this.j + 1 < 8 && this.i - 1 > -1) {
            if (panel.Board[i - 1][j + 1] == 0) {
                moves.add(new Pair<>(this.i - 1, this.j + 1));
            } else if (panel.Board[i - 1][j + 1] * value < 0) {
                eats.add(new Pair<>(this.i - 1, this.j + 1));
            }
        }
        if (this.j - 1 > -1 && this.i + 1 < 8) {
            if (panel.Board[i + 1][j - 1] == 0) {
                moves.add(new Pair<>(this.i + 1, this.j - 1));
            } else if (panel.Board[i + 1][j - 1] * value < 0) {
                eats.add(new Pair<>(this.i + 1, this.j - 1));
            }
        }

        this.check = false;
        for (ChessMan chessMan : panel.chessMans) {
            if (chessMan.value * this.value < 0) {
                for (Pair<Integer, Integer> eat : chessMan.eats) {
                    if (eat.first == this.i && eat.second == this.j) {
                        this.check = true;
                    }
                }
            }
        }

        this.check1 = false;
        if (this.moveTurn == 0) {
            if (this.color == "White_") {
                if (panel.Board[7][1] == 0 && panel.Board[7][2] == 0 && panel.Board[7][3] == 0) {
                    if (panel.whiteRook1.moveTurn == 0 && panel.whiteRook1.alive) {
                        for (ChessMan chessMan : panel.chessMans) {
                            if (chessMan.value * this.value < 0) {
                                for (Pair<Integer, Integer> move : chessMan.moves) {
                                    if (move.first == 7 && move.second == 2) {
                                        this.check1 = true;
                                    }
                                }
                            }
                        }
                        if (!this.check1 && !this.check) {
                            Pair<Integer, Integer> pair = new Pair<>(this.i, this.j - 2);
                            pair.special3 = true;
                            moves.add(pair);
                        }
                    }
                }
                if (panel.Board[7][5] == 0 && panel.Board[7][6] == 0) {
                    if (panel.whiteRook2.moveTurn == 0 && panel.whiteRook2.alive) {
                        for (ChessMan chessMan : panel.chessMans) {
                            if (chessMan.value * this.value < 0) {
                                for (Pair<Integer, Integer> move : chessMan.moves) {
                                    if (move.first == 7 && move.second == 6) {
                                        this.check1 = true;
                                    }
                                }
                            }
                        }
                        if (!this.check1 && !this.check) {
                            Pair<Integer, Integer> pair = new Pair<>(this.i, this.j + 2);
                            pair.special3 = true;
                            moves.add(pair);
                        }
                    }
                }
            }

            if (this.color == "Black_") {
                if (panel.Board[0][1] == 0 && panel.Board[0][2] == 0 && panel.Board[0][3] == 0) {
                    if (panel.blackRook1.moveTurn == 0 && panel.blackRook1.alive) {
                        for (ChessMan chessMan : panel.chessMans) {
                            if (chessMan.value * this.value < 0) {
                                for (Pair<Integer, Integer> move : chessMan.moves) {
                                    if (move.first == 0 && move.second == 2) {
                                        this.check1 = true;
                                    }
                                }
                            }
                        }
                        if (!this.check1 && !this.check) {
                            Pair<Integer, Integer> pair = new Pair<>(this.i, this.j - 2);
                            pair.special3 = true;
                            moves.add(pair);
                        }

                    }
                }
                if (panel.Board[0][5] == 0 && panel.Board[0][6] == 0) {
                    if (panel.blackRook2.moveTurn == 0 && panel.blackRook2.alive) {
                        for (ChessMan chessMan : panel.chessMans) {
                            if (chessMan.value * this.value < 0) {
                                for (Pair<Integer, Integer> move : chessMan.moves) {
                                    if (move.first == 0 && move.second == 6) {
                                        this.check1 = true;
                                    }
                                }
                            }
                            if (!this.check1 && !this.check) {
                                Pair<Integer, Integer> pair = new Pair<>(this.i, this.j + 2);
                                pair.special3 = true;
                                moves.add(pair);
                            }
                        }
                    }
                }
            }
        }
    }
}
