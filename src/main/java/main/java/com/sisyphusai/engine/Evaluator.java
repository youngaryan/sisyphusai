package main.java.com.sisyphusai.engine;

import main.java.com.sisyphusai.engine.helpers.HelperEngineFunctions;
import main.java.com.sisyphusai.engine.helpers.PieceSquareTables;

public class Evaluator {
    /*
     * Material
     * Piece-Square Tables
     * Pawn Structure
     * Evaluation of Pieces
     * Evaluation Patterns
     * Mobility
     * Center Control
     * Connectivity
     * Trapped Pieces
     * King Safety
     * Space
     * Tempo
     */

    public int evaluatePosFromFen(String fen) {
        char[] parsedFen = HelperEngineFunctions.parseFEN(fen);
        int evaluation = 0;

        int materialEvaluator = materialEvaluator(parsedFen);
        int evaluatePositionByPieceSquareTables = evaluatePositionByPieceSquareTables(parsedFen,
                HelperEngineFunctions.isEndgame(parsedFen));
        int evaluatePawnStructure = evaluatePawnStructure(parsedFen);
        // return materialEvaluator(parsedFen);

        System.out.println("Material: " + materialEvaluator);
        System.out.println("Piece-Square Tables: " + evaluatePositionByPieceSquareTables);
        System.out.println("Pawn Structure: " + evaluatePawnStructure);
        evaluation += materialEvaluator * 1.2 + evaluatePositionByPieceSquareTables *
                1.5 + evaluatePawnStructure * 1.3;
        return evaluation;
        // return evaluatePawnStructure;
    }

    // Material evaluation
    private int materialEvaluator(char[] parsedFen) {

        int totalEvaluation = 0;

        // Iterate over the board part of the parsed FEN
        for (int i = 0; i < 64; i++) {
            char piece = parsedFen[i];
            if (piece != '1') {
                totalEvaluation += HelperEngineFunctions.getPieceValue(piece);
            }
        }

        return totalEvaluation;
    }

    // Piece-Square Tables evaluation
    private int evaluatePositionByPieceSquareTables(char[] parsedFen, boolean isEndgame) {
        int totalEvaluation = 0;

        for (int i = 0; i < 64; i++) {
            char piece = parsedFen[i];
            if (piece != '1') {
                int pieceValue = HelperEngineFunctions.getPieceValue(piece);
                int squareValue = getPieceSquareValue(piece, i, isEndgame);
                totalEvaluation += pieceValue + squareValue;
            }
        }
        return totalEvaluation;
    }

    // Get the value of the piece on the based on squere and piece
    private int getPieceSquareValue(char piece, int square, boolean isEndgame) {
        switch (piece) {
            case 'P':
                return isEndgame ? PieceSquareTables.eg_pawn_table[square] : PieceSquareTables.mg_pawn_table[square];
            case 'p':
                return isEndgame ? -PieceSquareTables.eg_pawn_table[HelperEngineFunctions.mirrorIndex(square)]
                        : -PieceSquareTables.mg_pawn_table[HelperEngineFunctions.mirrorIndex(square)];

            case 'N':
                return isEndgame ? PieceSquareTables.eg_knight_table[square]
                        : PieceSquareTables.mg_knight_table[square];
            case 'n':
                return isEndgame ? -PieceSquareTables.eg_knight_table[HelperEngineFunctions.mirrorIndex(square)]
                        : -PieceSquareTables.mg_knight_table[HelperEngineFunctions.mirrorIndex(square)];
            case 'B':
                return isEndgame ? PieceSquareTables.eg_bishop_table[square]
                        : PieceSquareTables.mg_bishop_table[square];
            case 'b':
                return isEndgame ? -PieceSquareTables.eg_bishop_table[HelperEngineFunctions.mirrorIndex(square)]
                        : -PieceSquareTables.mg_bishop_table[HelperEngineFunctions.mirrorIndex(square)];
            case 'R':
                return isEndgame ? PieceSquareTables.eg_rook_table[square] : PieceSquareTables.mg_rook_table[square];
            case 'r':
                return isEndgame ? -PieceSquareTables.eg_rook_table[HelperEngineFunctions.mirrorIndex(square)]
                        : -PieceSquareTables.mg_rook_table[HelperEngineFunctions.mirrorIndex(square)];
            case 'Q':
                return isEndgame ? PieceSquareTables.eg_queen_table[square] : PieceSquareTables.mg_queen_table[square];
            case 'q':
                return isEndgame ? -PieceSquareTables.eg_queen_table[HelperEngineFunctions.mirrorIndex(square)]
                        : -PieceSquareTables.mg_queen_table[HelperEngineFunctions.mirrorIndex(square)];
            case 'K':
                return isEndgame ? PieceSquareTables.eg_king_table[square] : PieceSquareTables.mg_king_table[square];
            case 'k':
                return isEndgame ? -PieceSquareTables.eg_king_table[HelperEngineFunctions.mirrorIndex(square)]
                        : -PieceSquareTables.mg_king_table[HelperEngineFunctions.mirrorIndex(square)];
            default:
                return 0;
        }
    }

    // Pawn Structure evaluation
    private int evaluatePawnStructure(char[] parsedFen) {
        int totalEvaluation = 0;

        // check pawns are conected
        // check if the pawn is passed
        // check if the pawn is isolated
        // check if the pawn is doubled
        for (int i = 0; i < 64; i++) {
            char piece = parsedFen[i];
            if (piece == 'P') {
                totalEvaluation += evaluateWhitePawnStructure(parsedFen, i);
            } else if (piece == 'p') {
                totalEvaluation += evaluateBlackPawnStructure(parsedFen, i);
            }
        }

        return totalEvaluation;
    }

    private int evaluateBlackPawnStructure(char[] parsedFen, int i) {
        int totalEvaluation = 0;

        final int pawnConnected = -10;
        final int pawnPassed = -20;
        final int pawnIsolated = 10;
        final int pawnDoubled = 20;

        boolean passedPawn = true;

        // check if pawns are conected
        if ((i % 8 != 0 && parsedFen[i - 1] == 'p') || (i % 8 != 7 && parsedFen[i + 1] == 'p')) {
            totalEvaluation += pawnConnected;
        }

        // check if the pawn is passed
        for (int j = i; j < 64; j += 8) {
            if (parsedFen[j] == 'P') {
                passedPawn = false;
                break;
            }
        }

        if (passedPawn) {
            totalEvaluation += pawnPassed;
        }

        // check if the pawn is isolated
        if ((i % 8 == 0 || parsedFen[i - 1] != 'p') && (i % 8 == 7 || parsedFen[i + 1] != 'p')) {
            totalEvaluation += pawnIsolated;
        }
        // check if the pawn is doubled
        for (int j = i + 8; j < 64; j += 8) {
            if (parsedFen[j] == 'p') {
                totalEvaluation += pawnDoubled;
            }
        }

        return totalEvaluation;
    }

    private int evaluateWhitePawnStructure(char[] parsedFen, int i) {
        int totalEvaluation = 0;

        final int pawnConnected = 10;
        final int pawnPassed = 20;
        final int pawnIsolated = -10;
        final int pawnDoubled = -20;

        boolean passedPawn = true;

        // check if pawns are conected
        if ((i % 8 != 0 && parsedFen[i - 1] == 'P') || (i % 8 != 7 && parsedFen[i + 1] == 'P')) {
            totalEvaluation += pawnConnected;
        }

        // check if the pawn is passed
        for (int j = i - 1; j > 0; j -= 8) {
            if (parsedFen[j] == 'p') {
                passedPawn = false;
                break;
            }
        }

        if (passedPawn) {
            totalEvaluation += pawnPassed;
        }

        // check if the pawn is isolated
        if ((i % 8 == 0 || parsedFen[i - 1] != 'P') && (i % 8 == 7 || parsedFen[i + 1] != 'P')) {
            totalEvaluation += pawnIsolated;
        }
        // check if the pawn is doubled
        for (int j = i - 8; j >= 0; j -= 8) {
            if (parsedFen[j] == 'P') {
                totalEvaluation += pawnDoubled;
            }
        }
        return totalEvaluation;
    }

}
