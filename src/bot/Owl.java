package bot;

import main.GamePanel;
import pair.Pair;
import piece.Bishop;
import piece.ChessMan;
import piece.Knight;
import piece.Pawn;
import piece.Queen;
import piece.Rook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Owl {

    private static final int PAWN = 1;
    private static final int KNIGHT = 2;
    private static final int BISHOP = 3;
    private static final int ROOK = 4;
    private static final int QUEEN = 5;
    private static final int KING = 6;

    private static final int MAX_PLY = 64;
    private static final int INF = 1_000_000_000;
    private static final int MATE = 1_000_000;

    private static final int TT_EXACT = 0;
    private static final int TT_LOWER = 1;
    private static final int TT_UPPER = 2;

    private static final int[] PIECE_VALUES = {
            0, 100, 320, 330, 500, 900, 20_000
    };

    // Piece-square tables in white perspective (a8 -> h1).
    private static final int[] PST_PAWN = {
            0, 0, 0, 0, 0, 0, 0, 0,
            50, 50, 50, 50, 50, 50, 50, 50,
            10, 10, 20, 30, 30, 20, 10, 10,
            5, 5, 10, 25, 25, 10, 5, 5,
            0, 0, 0, 20, 20, 0, 0, 0,
            5, -5, -10, 0, 0, -10, -5, 5,
            5, 10, 10, -20, -20, 10, 10, 5,
            0, 0, 0, 0, 0, 0, 0, 0
    };

    private static final int[] PST_KNIGHT = {
            -50, -40, -30, -30, -30, -30, -40, -50,
            -40, -20, 0, 0, 0, 0, -20, -40,
            -30, 0, 10, 15, 15, 10, 0, -30,
            -30, 5, 15, 20, 20, 15, 5, -30,
            -30, 0, 15, 20, 20, 15, 0, -30,
            -30, 5, 10, 15, 15, 10, 5, -30,
            -40, -20, 0, 5, 5, 0, -20, -40,
            -50, -40, -30, -30, -30, -30, -40, -50
    };

    private static final int[] PST_BISHOP = {
            -20, -10, -10, -10, -10, -10, -10, -20,
            -10, 5, 0, 0, 0, 0, 5, -10,
            -10, 10, 10, 10, 10, 10, 10, -10,
            -10, 0, 10, 10, 10, 10, 0, -10,
            -10, 5, 5, 10, 10, 5, 5, -10,
            -10, 0, 5, 10, 10, 5, 0, -10,
            -10, 0, 0, 0, 0, 0, 0, -10,
            -20, -10, -10, -10, -10, -10, -10, -20
    };

    private static final int[] PST_ROOK = {
            0, 0, 0, 0, 0, 0, 0, 0,
            5, 10, 10, 10, 10, 10, 10, 5,
            -5, 0, 0, 0, 0, 0, 0, -5,
            -5, 0, 0, 0, 0, 0, 0, -5,
            -5, 0, 0, 0, 0, 0, 0, -5,
            -5, 0, 0, 0, 0, 0, 0, -5,
            -5, 0, 0, 0, 0, 0, 0, -5,
            0, 0, 0, 5, 5, 0, 0, 0
    };

    private static final int[] PST_QUEEN = {
            -20, -10, -10, -5, -5, -10, -10, -20,
            -10, 0, 0, 0, 0, 0, 0, -10,
            -10, 0, 5, 5, 5, 5, 0, -10,
            -5, 0, 5, 5, 5, 5, 0, -5,
            0, 0, 5, 5, 5, 5, 0, -5,
            -10, 5, 5, 5, 5, 5, 0, -10,
            -10, 0, 5, 0, 0, 0, 0, -10,
            -20, -10, -10, -5, -5, -10, -10, -20
    };

    private static final int[] PST_KING = {
            -30, -40, -40, -50, -50, -40, -40, -30,
            -30, -40, -40, -50, -50, -40, -40, -30,
            -30, -40, -40, -50, -50, -40, -40, -30,
            -30, -40, -40, -50, -50, -40, -40, -30,
            -20, -30, -30, -40, -40, -30, -30, -20,
            -10, -20, -20, -20, -20, -20, -20, -10,
            20, 20, 0, 0, 0, 0, 20, 20,
            20, 30, 10, 0, 0, 10, 30, 20
    };

    private final int botSide;
    private final int depth;

    private final Map<Long, TTEntry> transpositionTable;
    private final EngineMove[][] killerMoves;
    private final int[][] historyHeuristic;
    private final long[][] zobristPieces;
    private final long zobristSide;

    public Owl(int botSide, int depth) {
        this.botSide = botSide;
        this.depth = Math.max(1, depth);
        this.transpositionTable = new HashMap<>();
        this.killerMoves = new EngineMove[MAX_PLY][2];
        this.historyHeuristic = new int[7][64];
        this.zobristPieces = new long[13][64];

        Random random = new Random(20260407L);
        for (int piece = 0; piece < 13; piece++) {
            for (int sq = 0; sq < 64; sq++) {
                zobristPieces[piece][sq] = random.nextLong();
            }
        }
        zobristSide = random.nextLong();
    }

    public int getBotSide() {
        return botSide;
    }

    public void update(GamePanel panel) {
        if (panel.end || panel.moving || panel.promotion) {
            return;
        }
        if (panel.turn != botSide) {
            return;
        }
        if (transpositionTable.size() > 250_000) {
            transpositionTable.clear();
        }

        BotMove bestMove = chooseBestMove(panel);
        if (bestMove == null) {
            panel.end = true;
            return;
        }
        executeMove(panel, bestMove);
    }

    private BotMove chooseBestMove(GamePanel panel) {
        List<BotMove> legalMoves = collectLegalMoves(panel, botSide);
        if (legalMoves.isEmpty()) {
            return null;
        }

        int[][] startBoard = buildEngineBoard(panel);

        BotMove openingMove = chooseOpeningMove(startBoard, legalMoves);
        if (openingMove != null) {
            return openingMove;
        }

        orderRootMoves(startBoard, legalMoves);

        BotMove bestMove = legalMoves.get(0);
        Map<BotMove, Integer> rootScores = new HashMap<>();

        for (int currentDepth = 1; currentDepth <= depth; currentDepth++) {
            int bestScore = -INF;
            BotMove depthBestMove = bestMove;

            for (BotMove move : legalMoves) {
                int[][] board = copyBoard(startBoard);
                applyRootMove(board, move, botSide);
                int score = minimax(board, -botSide, currentDepth - 1, -INF, INF, 1);
                rootScores.put(move, score);

                if (depthBestMove == null || score > bestScore) {
                    bestScore = score;
                    depthBestMove = move;
                }
            }

            if (depthBestMove != null) {
                bestMove = depthBestMove;
            }

            legalMoves.sort((a, b) ->
                    Integer.compare(rootScores.getOrDefault(b, -INF), rootScores.getOrDefault(a, -INF)));

            if (Math.abs(bestScore) >= MATE - 1000) {
                break;
            }
        }

        return bestMove;
    }

    private List<BotMove> collectLegalMoves(GamePanel panel, int side) {
        ArrayList<BotMove> result = new ArrayList<>();

        for (ChessMan piece : panel.chessMans) {
            if (!piece.alive || piece.value * side <= 0) {
                continue;
            }
            if (panel.Board[piece.i][piece.j] * piece.value <= 0) {
                continue;
            }

            piece.refreshMoveSet();

            for (Pair<Integer, Integer> pair : piece.getMoves()) {
                int toI = pair.first;
                int toJ = pair.second;
                if (!piece.checkMove(toI, toJ, false)) {
                    continue;
                }
                BotMove move = new BotMove(piece, piece.i, piece.j, toI, toJ);
                move.pawnDouble = pair.special1;
                move.castling = pair.special3;
                move.promotion = pair.special4;
                move.capture = panel.Board[toI][toJ] * side < 0;
                result.add(move);
            }

            for (Pair<Integer, Integer> pair : piece.getEats()) {
                int toI = pair.first;
                int toJ = pair.second;
                if (!piece.checkMove(toI, toJ, pair.special2)) {
                    continue;
                }
                BotMove move = new BotMove(piece, piece.i, piece.j, toI, toJ);
                move.enPassant = pair.special2;
                move.promotion = pair.special4;
                move.capture = true;
                result.add(move);
            }
        }

        return result;
    }

    private void orderRootMoves(int[][] board, List<BotMove> legalMoves) {
        legalMoves.sort((a, b) -> Integer.compare(scoreRootMove(board, b), scoreRootMove(board, a)));
    }

    private int scoreRootMove(int[][] board, BotMove move) {
        int pieceType = pieceTypeFromPiece(move.piece);
        int score = 0;

        if (move.capture) {
            int victimType = PAWN;
            if (!move.enPassant) {
                int victim = board[move.toI][move.toJ];
                if (victim != 0) {
                    victimType = Math.abs(victim);
                }
            }
            score += 20_000 + 10 * PIECE_VALUES[victimType] - PIECE_VALUES[pieceType];
        }

        if (move.promotion) {
            score += 18_000;
        }
        if (move.castling) {
            score += 350;
        }

        int distanceFromCenter = Math.abs(move.toI - 3) + Math.abs(move.toJ - 3);
        score += 25 - 4 * distanceFromCenter;

        if (pieceType == QUEEN) {
            score -= 30;
        }

        return score;
    }

    private BotMove chooseOpeningMove(int[][] board, List<BotMove> legalMoves) {
        int pieceCount = countPieces(board);
        if (pieceCount < 26) {
            return null;
        }

        if (botSide == -1) {
            BotMove move;

            // 1... responses.
            if (board[4][4] == PAWN && board[6][4] == 0) {
                move = firstLegalMove(legalMoves, new int[][]{
                        {1, 2, 3, 2}, // c7c5
                        {1, 4, 3, 4}, // e7e5
                        {1, 4, 2, 4}  // e7e6
                });
                if (move != null) {
                    return move;
                }
            }
            if (board[4][3] == PAWN && board[6][3] == 0) {
                move = firstLegalMove(legalMoves, new int[][]{
                        {0, 6, 2, 5}, // g8f6
                        {1, 3, 3, 3}, // d7d5
                        {1, 6, 2, 6}  // g7g6
                });
                if (move != null) {
                    return move;
                }
            }
            if (board[4][2] == PAWN && board[6][2] == 0) {
                move = firstLegalMove(legalMoves, new int[][]{
                        {1, 4, 3, 4}, // e7e5
                        {0, 6, 2, 5}, // g8f6
                        {1, 2, 3, 2}  // c7c5
                });
                if (move != null) {
                    return move;
                }
            }
            if (board[5][5] == KNIGHT && board[7][6] == 0) {
                move = firstLegalMove(legalMoves, new int[][]{
                        {1, 3, 3, 3}, // d7d5
                        {0, 6, 2, 5}, // g8f6
                        {1, 4, 3, 4}  // e7e5
                });
                if (move != null) {
                    return move;
                }
            }

            // 2... follow ups.
            if (board[3][4] == -PAWN && board[5][5] == KNIGHT) {
                move = firstLegalMove(legalMoves, new int[][]{
                        {0, 1, 2, 2}, // b8c6
                        {0, 6, 2, 5}, // g8f6
                        {1, 0, 2, 0}  // a7a6
                });
                if (move != null) {
                    return move;
                }
            }
            if (board[3][3] == -PAWN && board[4][2] == PAWN) {
                move = firstLegalMove(legalMoves, new int[][]{
                        {1, 4, 2, 4}, // e7e6
                        {1, 2, 2, 2}, // c7c6
                        {0, 6, 2, 5}  // g8f6
                });
                if (move != null) {
                    return move;
                }
            }
            return null;
        }

        return firstLegalMove(legalMoves, new int[][]{
                {6, 4, 4, 4}, // e2e4
                {6, 3, 4, 3}, // d2d4
                {6, 2, 4, 2}, // c2c4
                {7, 6, 5, 5}  // g1f3
        });
    }

    private BotMove firstLegalMove(List<BotMove> legalMoves, int[][] moveOrder) {
        for (int[] move : moveOrder) {
            BotMove found = findRootMove(legalMoves, move[0], move[1], move[2], move[3]);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    private BotMove findRootMove(List<BotMove> legalMoves, int fromI, int fromJ, int toI, int toJ) {
        for (BotMove move : legalMoves) {
            if (move.fromI == fromI && move.fromJ == fromJ && move.toI == toI && move.toJ == toJ) {
                return move;
            }
        }
        return null;
    }

    private void executeMove(GamePanel panel, BotMove move) {
        ChessMan movingPiece = move.piece;
        int expectedSide = movingPiece.value > 0 ? 1 : -1;
        ChessMan pieceOnFrom = findPieceAt(panel, move.fromI, move.fromJ, expectedSide);
        if (pieceOnFrom != null) {
            movingPiece = pieceOnFrom;
        }
        if (movingPiece == null || !movingPiece.alive) {
            return;
        }

        int side = movingPiece.value > 0 ? 1 : -1;
        int landingI = move.enPassant ? move.toI - side : move.toI;
        int landingJ = move.toJ;

        if (move.enPassant) {
            ChessMan captured = findPieceAt(panel, move.toI, move.toJ, -side);
            if (captured != null) {
                captured.alive = false;
            }
        } else if (move.capture) {
            ChessMan captured = findPieceAt(panel, move.toI, move.toJ, -side);
            if (captured != null) {
                captured.alive = false;
            }
        }

        clearSquareForLanding(panel, movingPiece, landingI, landingJ);

        if (move.promotion) {
            executePromotion(panel, move, side, movingPiece);
            return;
        }

        int targetX = (landingJ + 4) * panel.tileSize;
        int targetY = (landingI + 2) * panel.tileSize;
        movingPiece.Move(targetX, targetY, move.enPassant);

        if (Math.abs(movingPiece.value) == PAWN) {
            movingPiece.check = move.pawnDouble;
        }
        if (move.castling) {
            panel.castling = true;
        }
    }

    private void executePromotion(GamePanel panel, BotMove move, int side, ChessMan pawn) {
        int toI = move.toI;
        int toJ = move.toJ;
        int toX = (toJ + 4) * panel.tileSize;
        int toY = (toI + 2) * panel.tileSize;

        if (move.capture) {
            ChessMan captured = findPieceAt(panel, toI, toJ, -side);
            if (captured != null) {
                captured.alive = false;
            }
        }

        panel.Board[pawn.i][pawn.j] = 0;
        pawn.i = toI;
        pawn.j = toJ;
        pawn.x = toX;
        pawn.y = toY;
        pawn.xLock = toX;
        pawn.yLock = toY;
        pawn.check = false;
        pawn.alive = false;
        pawn.button = false;

        panel.Board[toI][toJ] = side * 9;
        panel.chessMans.add(new Queen(panel, toX, toY, pawn.white));
        panel.turn = -panel.turn;
        panel.moving = false;
        panel.castling = false;
        panel.promotion = false;
    }

    private void clearSquareForLanding(GamePanel panel, ChessMan movingPiece, int row, int col) {
        for (ChessMan piece : panel.chessMans) {
            if (!piece.alive || piece == movingPiece) {
                continue;
            }
            if (piece.i == row && piece.j == col) {
                piece.alive = false;
            }
        }
    }

    private ChessMan findPieceAt(GamePanel panel, int row, int col, int side) {
        for (ChessMan piece : panel.chessMans) {
            if (!piece.alive) {
                continue;
            }
            if (piece.i == row && piece.j == col && piece.value * side > 0) {
                return piece;
            }
        }
        return null;
    }

    private int minimax(int[][] board, int sideToMove, int depthLeft, int alpha, int beta, int ply) {
        if (ply >= MAX_PLY - 1) {
            return evaluate(board);
        }

        int alphaOrig = alpha;
        int betaOrig = beta;

        long hash = computeHash(board, sideToMove);
        TTEntry ttEntry = transpositionTable.get(hash);
        EngineMove ttMove = null;
        if (ttEntry != null) {
            ttMove = ttEntry.bestMove;
            if (ttEntry.depth >= depthLeft) {
                if (ttEntry.flag == TT_EXACT) {
                    return ttEntry.score;
                }
                if (ttEntry.flag == TT_LOWER) {
                    alpha = Math.max(alpha, ttEntry.score);
                } else if (ttEntry.flag == TT_UPPER) {
                    beta = Math.min(beta, ttEntry.score);
                }
                if (alpha >= beta) {
                    return ttEntry.score;
                }
            }
        }

        if (depthLeft <= 0) {
            return quiescence(board, sideToMove, alpha, beta, ply);
        }

        List<EngineMove> legalMoves = generateLegalMoves(board, sideToMove);
        if (legalMoves.isEmpty()) {
            if (isKingInCheck(board, sideToMove)) {
                return sideToMove == botSide ? -MATE + ply : MATE - ply;
            }
            return 0;
        }

        orderMoves(board, legalMoves, sideToMove, ply, ttMove);

        boolean maximizing = sideToMove == botSide;
        int bestScore = maximizing ? -INF : INF;
        EngineMove bestMove = null;

        for (EngineMove move : legalMoves) {
            int[][] next = copyBoard(board);
            applyEngineMove(next, move, sideToMove);
            int score = minimax(next, -sideToMove, depthLeft - 1, alpha, beta, ply + 1);

            if (maximizing) {
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = move;
                }
                alpha = Math.max(alpha, bestScore);
                if (alpha >= beta) {
                    if (!isCaptureMove(board, move, sideToMove) && !move.promotion) {
                        updateKillerMove(ply, move);
                        updateHistoryHeuristic(board, move, depthLeft);
                    }
                    break;
                }
            } else {
                if (score < bestScore) {
                    bestScore = score;
                    bestMove = move;
                }
                beta = Math.min(beta, bestScore);
                if (alpha >= beta) {
                    if (!isCaptureMove(board, move, sideToMove) && !move.promotion) {
                        updateKillerMove(ply, move);
                        updateHistoryHeuristic(board, move, depthLeft);
                    }
                    break;
                }
            }
        }

        int flag;
        if (bestScore <= alphaOrig) {
            flag = TT_UPPER;
        } else if (bestScore >= betaOrig) {
            flag = TT_LOWER;
        } else {
            flag = TT_EXACT;
        }
        storeTransposition(hash, depthLeft, bestScore, flag, bestMove);

        return bestScore;
    }

    private int quiescence(int[][] board, int sideToMove, int alpha, int beta, int ply) {
        if (ply >= MAX_PLY - 1) {
            return evaluate(board);
        }

        int standPat = evaluate(board);
        boolean maximizing = sideToMove == botSide;

        if (maximizing) {
            if (standPat >= beta) {
                return standPat;
            }
            alpha = Math.max(alpha, standPat);
        } else {
            if (standPat <= alpha) {
                return standPat;
            }
            beta = Math.min(beta, standPat);
        }

        List<EngineMove> noisyMoves = generateNoisyLegalMoves(board, sideToMove);
        if (noisyMoves.isEmpty()) {
            return standPat;
        }

        orderMoves(board, noisyMoves, sideToMove, ply, null);

        int best = standPat;
        for (EngineMove move : noisyMoves) {
            int[][] next = copyBoard(board);
            applyEngineMove(next, move, sideToMove);
            int score = quiescence(next, -sideToMove, alpha, beta, ply + 1);

            if (maximizing) {
                if (score > best) {
                    best = score;
                }
                alpha = Math.max(alpha, best);
                if (alpha >= beta) {
                    break;
                }
            } else {
                if (score < best) {
                    best = score;
                }
                beta = Math.min(beta, best);
                if (alpha >= beta) {
                    break;
                }
            }
        }

        return best;
    }

    private void orderMoves(int[][] board, List<EngineMove> moves, int side, int ply, EngineMove ttMove) {
        ArrayList<ScoredMove> scoredMoves = new ArrayList<>(moves.size());
        for (EngineMove move : moves) {
            int score = scoreEngineMove(board, move, side, ply, ttMove);
            scoredMoves.add(new ScoredMove(move, score));
        }

        scoredMoves.sort((a, b) -> Integer.compare(b.score, a.score));
        moves.clear();
        for (ScoredMove scoredMove : scoredMoves) {
            moves.add(scoredMove.move);
        }
    }

    private int scoreEngineMove(int[][] board, EngineMove move, int side, int ply, EngineMove ttMove) {
        if (ttMove != null && sameMove(move, ttMove)) {
            return 2_000_000;
        }

        int score = 0;
        int fromPiece = Math.abs(board[move.fromI][move.fromJ]);

        if (isCaptureMove(board, move, side)) {
            int captured = Math.abs(board[move.toI][move.toJ]);
            score += 500_000 + 10 * PIECE_VALUES[captured] - PIECE_VALUES[fromPiece];
        }
        if (move.promotion) {
            score += 450_000;
        }

        if (ply < MAX_PLY) {
            EngineMove killer1 = killerMoves[ply][0];
            EngineMove killer2 = killerMoves[ply][1];
            if (killer1 != null && sameMove(move, killer1)) {
                score += 300_000;
            } else if (killer2 != null && sameMove(move, killer2)) {
                score += 290_000;
            }
        }

        int toSquare = move.toI * 8 + move.toJ;
        score += historyHeuristic[fromPiece][toSquare];

        return score;
    }

    private List<EngineMove> generateLegalMoves(int[][] board, int side) {
        ArrayList<EngineMove> legal = new ArrayList<>();
        ArrayList<EngineMove> pseudo = generatePseudoMoves(board, side);

        for (EngineMove move : pseudo) {
            int[][] next = copyBoard(board);
            applyEngineMove(next, move, side);
            if (!isKingInCheck(next, side)) {
                legal.add(move);
            }
        }

        return legal;
    }

    private List<EngineMove> generateNoisyLegalMoves(int[][] board, int side) {
        ArrayList<EngineMove> legal = new ArrayList<>();
        ArrayList<EngineMove> pseudo = generatePseudoMoves(board, side);

        for (EngineMove move : pseudo) {
            if (!move.promotion && !isCaptureMove(board, move, side)) {
                continue;
            }
            int[][] next = copyBoard(board);
            applyEngineMove(next, move, side);
            if (!isKingInCheck(next, side)) {
                legal.add(move);
            }
        }

        return legal;
    }

    private ArrayList<EngineMove> generatePseudoMoves(int[][] board, int side) {
        ArrayList<EngineMove> moves = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int piece = board[i][j];
                if (piece == 0 || piece * side <= 0) {
                    continue;
                }
                int type = Math.abs(piece);
                if (type == PAWN) {
                    generatePawnMoves(board, side, i, j, moves);
                } else if (type == KNIGHT) {
                    generateKnightMoves(board, side, i, j, moves);
                } else if (type == BISHOP) {
                    generateSlidingMoves(board, side, i, j, moves, true, false);
                } else if (type == ROOK) {
                    generateSlidingMoves(board, side, i, j, moves, false, true);
                } else if (type == QUEEN) {
                    generateSlidingMoves(board, side, i, j, moves, true, true);
                } else if (type == KING) {
                    generateKingMoves(board, side, i, j, moves);
                }
            }
        }

        return moves;
    }

    private void generatePawnMoves(int[][] board, int side, int i, int j, List<EngineMove> out) {
        int dir = side == 1 ? -1 : 1;
        int startRow = side == 1 ? 6 : 1;
        int promotionRow = side == 1 ? 0 : 7;

        int oneForward = i + dir;
        if (inBounds(oneForward, j) && board[oneForward][j] == 0) {
            EngineMove move = new EngineMove(i, j, oneForward, j);
            move.promotion = oneForward == promotionRow;
            out.add(move);

            int twoForward = i + 2 * dir;
            if (i == startRow && inBounds(twoForward, j) && board[twoForward][j] == 0) {
                out.add(new EngineMove(i, j, twoForward, j));
            }
        }

        int left = j - 1;
        int right = j + 1;
        if (inBounds(oneForward, left) && board[oneForward][left] * side < 0) {
            EngineMove move = new EngineMove(i, j, oneForward, left);
            move.promotion = oneForward == promotionRow;
            out.add(move);
        }
        if (inBounds(oneForward, right) && board[oneForward][right] * side < 0) {
            EngineMove move = new EngineMove(i, j, oneForward, right);
            move.promotion = oneForward == promotionRow;
            out.add(move);
        }
    }

    private void generateKnightMoves(int[][] board, int side, int i, int j, List<EngineMove> out) {
        int[][] delta = {
                {-2, -1}, {-2, 1}, {2, -1}, {2, 1},
                {-1, -2}, {-1, 2}, {1, -2}, {1, 2}
        };
        for (int[] d : delta) {
            int ni = i + d[0];
            int nj = j + d[1];
            if (inBounds(ni, nj) && board[ni][nj] * side <= 0) {
                out.add(new EngineMove(i, j, ni, nj));
            }
        }
    }

    private void generateSlidingMoves(int[][] board, int side, int i, int j, List<EngineMove> out,
                                      boolean diagonal, boolean straight) {
        if (diagonal) {
            addRay(board, side, i, j, -1, -1, out);
            addRay(board, side, i, j, -1, 1, out);
            addRay(board, side, i, j, 1, -1, out);
            addRay(board, side, i, j, 1, 1, out);
        }
        if (straight) {
            addRay(board, side, i, j, -1, 0, out);
            addRay(board, side, i, j, 1, 0, out);
            addRay(board, side, i, j, 0, -1, out);
            addRay(board, side, i, j, 0, 1, out);
        }
    }

    private void addRay(int[][] board, int side, int i, int j, int di, int dj, List<EngineMove> out) {
        int ni = i + di;
        int nj = j + dj;
        while (inBounds(ni, nj)) {
            if (board[ni][nj] == 0) {
                out.add(new EngineMove(i, j, ni, nj));
            } else {
                if (board[ni][nj] * side < 0) {
                    out.add(new EngineMove(i, j, ni, nj));
                }
                break;
            }
            ni += di;
            nj += dj;
        }
    }

    private void generateKingMoves(int[][] board, int side, int i, int j, List<EngineMove> out) {
        for (int di = -1; di <= 1; di++) {
            for (int dj = -1; dj <= 1; dj++) {
                if (di == 0 && dj == 0) {
                    continue;
                }
                int ni = i + di;
                int nj = j + dj;
                if (inBounds(ni, nj) && board[ni][nj] * side <= 0) {
                    out.add(new EngineMove(i, j, ni, nj));
                }
            }
        }
    }

    private boolean isKingInCheck(int[][] board, int side) {
        int[] king = findKing(board, side);
        if (king[0] == -1) {
            return true;
        }
        return isSquareAttacked(board, king[0], king[1], -side);
    }

    private int[] findKing(int[][] board, int side) {
        int kingCode = side * KING;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == kingCode) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[]{-1, -1};
    }

    private boolean isSquareAttacked(int[][] board, int row, int col, int attackerSide) {
        int pawnRow = row + (attackerSide == 1 ? 1 : -1);
        if (inBounds(pawnRow, col - 1) && board[pawnRow][col - 1] == attackerSide * PAWN) {
            return true;
        }
        if (inBounds(pawnRow, col + 1) && board[pawnRow][col + 1] == attackerSide * PAWN) {
            return true;
        }

        int[][] knightDelta = {
                {-2, -1}, {-2, 1}, {2, -1}, {2, 1},
                {-1, -2}, {-1, 2}, {1, -2}, {1, 2}
        };
        for (int[] d : knightDelta) {
            int ni = row + d[0];
            int nj = col + d[1];
            if (inBounds(ni, nj) && board[ni][nj] == attackerSide * KNIGHT) {
                return true;
            }
        }

        int[][] bishopDirs = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
        for (int[] d : bishopDirs) {
            int ni = row + d[0];
            int nj = col + d[1];
            while (inBounds(ni, nj)) {
                int p = board[ni][nj];
                if (p != 0) {
                    if (p == attackerSide * BISHOP || p == attackerSide * QUEEN) {
                        return true;
                    }
                    break;
                }
                ni += d[0];
                nj += d[1];
            }
        }

        int[][] rookDirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] d : rookDirs) {
            int ni = row + d[0];
            int nj = col + d[1];
            while (inBounds(ni, nj)) {
                int p = board[ni][nj];
                if (p != 0) {
                    if (p == attackerSide * ROOK || p == attackerSide * QUEEN) {
                        return true;
                    }
                    break;
                }
                ni += d[0];
                nj += d[1];
            }
        }

        for (int di = -1; di <= 1; di++) {
            for (int dj = -1; dj <= 1; dj++) {
                if (di == 0 && dj == 0) {
                    continue;
                }
                int ni = row + di;
                int nj = col + dj;
                if (inBounds(ni, nj) && board[ni][nj] == attackerSide * KING) {
                    return true;
                }
            }
        }

        return false;
    }

    private int evaluate(int[][] board) {
        int score = 0;
        int whiteBishops = 0;
        int blackBishops = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int piece = board[i][j];
                if (piece == 0) {
                    continue;
                }
                int side = piece > 0 ? 1 : -1;
                int type = Math.abs(piece);

                score += side * PIECE_VALUES[type];
                score += side * pieceSquareValue(type, side, i, j);

                if (type == BISHOP) {
                    if (side == 1) {
                        whiteBishops++;
                    } else {
                        blackBishops++;
                    }
                }
            }
        }

        if (whiteBishops >= 2) {
            score += 35;
        }
        if (blackBishops >= 2) {
            score -= 35;
        }

        int mobility = generatePseudoMoves(board, 1).size() - generatePseudoMoves(board, -1).size();
        score += mobility * 4;

        score += evaluateKingSafety(board, 1);
        score -= evaluateKingSafety(board, -1);

        return botSide == 1 ? score : -score;
    }

    private int evaluateKingSafety(int[][] board, int side) {
        int[] king = findKing(board, side);
        int kingRow = king[0];
        int kingCol = king[1];
        if (kingRow == -1) {
            return -5000;
        }

        int enemy = -side;
        int attackedRing = 0;
        for (int di = -1; di <= 1; di++) {
            for (int dj = -1; dj <= 1; dj++) {
                if (di == 0 && dj == 0) {
                    continue;
                }
                int ni = kingRow + di;
                int nj = kingCol + dj;
                if (inBounds(ni, nj) && isSquareAttacked(board, ni, nj, enemy)) {
                    attackedRing++;
                }
            }
        }

        int shield = 0;
        int dir = side == 1 ? -1 : 1;
        int shieldRow1 = kingRow + dir;
        int shieldRow2 = kingRow + 2 * dir;
        for (int fileOffset = -1; fileOffset <= 1; fileOffset++) {
            int file = kingCol + fileOffset;
            if (inBounds(shieldRow1, file) && board[shieldRow1][file] == side * PAWN) {
                shield += 2;
            } else if (inBounds(shieldRow2, file) && board[shieldRow2][file] == side * PAWN) {
                shield += 1;
            }
        }

        int value = shield * 10 - attackedRing * 14;
        if (isSquareAttacked(board, kingRow, kingCol, enemy)) {
            value -= 30;
        }
        return value;
    }

    private int pieceSquareValue(int type, int side, int row, int col) {
        int idx = row * 8 + col;
        if (side == -1) {
            idx = mirrorIndex(idx);
        }

        switch (type) {
            case PAWN:
                return PST_PAWN[idx];
            case KNIGHT:
                return PST_KNIGHT[idx];
            case BISHOP:
                return PST_BISHOP[idx];
            case ROOK:
                return PST_ROOK[idx];
            case QUEEN:
                return PST_QUEEN[idx];
            case KING:
                return PST_KING[idx];
            default:
                return 0;
        }
    }

    private int mirrorIndex(int idx) {
        int row = idx / 8;
        int col = idx % 8;
        return (7 - row) * 8 + col;
    }

    private void applyEngineMove(int[][] board, EngineMove move, int side) {
        int piece = board[move.fromI][move.fromJ];
        board[move.fromI][move.fromJ] = 0;
        if (move.promotion && Math.abs(piece) == PAWN) {
            board[move.toI][move.toJ] = side * QUEEN;
        } else {
            board[move.toI][move.toJ] = piece;
        }
    }

    private int[][] buildEngineBoard(GamePanel panel) {
        int[][] board = new int[8][8];
        for (ChessMan piece : panel.chessMans) {
            if (!piece.alive) {
                continue;
            }
            if (piece.i < 0 || piece.i > 7 || piece.j < 0 || piece.j > 7) {
                continue;
            }
            if (panel.Board[piece.i][piece.j] * piece.value <= 0) {
                continue;
            }
            int side = piece.value > 0 ? 1 : -1;
            board[piece.i][piece.j] = side * pieceTypeFromPiece(piece);
        }
        return board;
    }

    private void applyRootMove(int[][] board, BotMove move, int side) {
        int piece = board[move.fromI][move.fromJ];
        board[move.fromI][move.fromJ] = 0;

        int finalI = move.toI;
        int finalJ = move.toJ;

        if (move.enPassant) {
            board[move.toI][move.toJ] = 0;
            finalI = move.toI - side;
        }

        if (move.castling) {
            if (move.toJ < move.fromJ) {
                board[move.fromI][3] = board[move.fromI][0];
                board[move.fromI][0] = 0;
            } else {
                board[move.fromI][5] = board[move.fromI][7];
                board[move.fromI][7] = 0;
            }
        }

        if (move.promotion) {
            board[finalI][finalJ] = side * QUEEN;
        } else {
            board[finalI][finalJ] = piece;
        }
    }

    private int pieceTypeFromPiece(ChessMan piece) {
        if (piece instanceof Pawn) {
            return PAWN;
        }
        if (piece instanceof Knight) {
            return KNIGHT;
        }
        if (piece instanceof Bishop) {
            return BISHOP;
        }
        if (piece instanceof Rook) {
            return ROOK;
        }
        if (piece instanceof Queen) {
            return QUEEN;
        }
        return KING;
    }

    private boolean isCaptureMove(int[][] board, EngineMove move, int side) {
        return board[move.toI][move.toJ] * side < 0;
    }

    private void updateKillerMove(int ply, EngineMove move) {
        if (ply >= MAX_PLY) {
            return;
        }
        EngineMove first = killerMoves[ply][0];
        if (first != null && sameMove(first, move)) {
            return;
        }
        killerMoves[ply][1] = killerMoves[ply][0];
        killerMoves[ply][0] = move.copy();
    }

    private void updateHistoryHeuristic(int[][] board, EngineMove move, int depthLeft) {
        int piece = Math.abs(board[move.fromI][move.fromJ]);
        int square = move.toI * 8 + move.toJ;
        int bonus = depthLeft * depthLeft;
        historyHeuristic[piece][square] = Math.min(1_000_000, historyHeuristic[piece][square] + bonus);
    }

    private boolean sameMove(EngineMove a, EngineMove b) {
        if (a == null || b == null) {
            return false;
        }
        return a.fromI == b.fromI && a.fromJ == b.fromJ
                && a.toI == b.toI && a.toJ == b.toJ
                && a.promotion == b.promotion;
    }

    private void storeTransposition(long hash, int depth, int score, int flag, EngineMove bestMove) {
        TTEntry old = transpositionTable.get(hash);
        if (old == null || depth >= old.depth || flag == TT_EXACT) {
            transpositionTable.put(hash, new TTEntry(depth, score, flag, bestMove == null ? null : bestMove.copy()));
        }
    }

    private long computeHash(int[][] board, int sideToMove) {
        long hash = 0L;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int piece = board[i][j];
                if (piece == 0) {
                    continue;
                }
                int pieceIndex = piece + 6;
                int square = i * 8 + j;
                hash ^= zobristPieces[pieceIndex][square];
            }
        }
        if (sideToMove == 1) {
            hash ^= zobristSide;
        }
        return hash;
    }

    private int countPieces(int[][] board) {
        int count = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != 0) {
                    count++;
                }
            }
        }
        return count;
    }

    private boolean inBounds(int i, int j) {
        return i >= 0 && i < 8 && j >= 0 && j < 8;
    }

    private int[][] copyBoard(int[][] board) {
        int[][] copy = new int[8][8];
        for (int i = 0; i < 8; i++) {
            System.arraycopy(board[i], 0, copy[i], 0, 8);
        }
        return copy;
    }

    private static class BotMove {
        ChessMan piece;
        int fromI;
        int fromJ;
        int toI;
        int toJ;
        boolean enPassant;
        boolean castling;
        boolean promotion;
        boolean pawnDouble;
        boolean capture;

        BotMove(ChessMan piece, int fromI, int fromJ, int toI, int toJ) {
            this.piece = piece;
            this.fromI = fromI;
            this.fromJ = fromJ;
            this.toI = toI;
            this.toJ = toJ;
        }
    }

    private static class EngineMove {
        int fromI;
        int fromJ;
        int toI;
        int toJ;
        boolean promotion;

        EngineMove(int fromI, int fromJ, int toI, int toJ) {
            this.fromI = fromI;
            this.fromJ = fromJ;
            this.toI = toI;
            this.toJ = toJ;
            this.promotion = false;
        }

        EngineMove copy() {
            EngineMove copy = new EngineMove(fromI, fromJ, toI, toJ);
            copy.promotion = promotion;
            return copy;
        }
    }

    private static class TTEntry {
        int depth;
        int score;
        int flag;
        EngineMove bestMove;

        TTEntry(int depth, int score, int flag, EngineMove bestMove) {
            this.depth = depth;
            this.score = score;
            this.flag = flag;
            this.bestMove = bestMove;
        }
    }

    private static class ScoredMove {
        EngineMove move;
        int score;

        ScoredMove(EngineMove move, int score) {
            this.move = move;
            this.score = score;
        }
    }
}
