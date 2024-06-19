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
        return materialEvaluator(parsedFen);
        // return evaluatePosition(parsedFen, true);
    }

    private int materialEvaluator(char[] parsedFen) {

        int totalEvaluation = 0;

        // Iterate over the board part of the parsed FEN
        for (int i = 0; i < 64; i++) {
            char piece = parsedFen[i];

            if (Character.isUpperCase(piece)) {
                totalEvaluation += HelperEngineFunctions.getPieceValue(piece);
            } else {
                totalEvaluation -= HelperEngineFunctions.getPieceValue(piece);
            }
        }

        return totalEvaluation;
    }

    private int evaluatePosition(char[] parsedFen, boolean isEndgame) {
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

    private static int getPieceSquareValue(char piece, int square, boolean isEndgame) {
        switch (piece) {
            case 'P':
                return isEndgame ? PieceSquareTables.eg_pawn_table[square] : PieceSquareTables.mg_pawn_table[square];
            case 'p':
                return isEndgame ? -PieceSquareTables.eg_pawn_table[63 - square]
                        : -PieceSquareTables.mg_pawn_table[63 - square];

            case 'N':
                return isEndgame ? PieceSquareTables.eg_knight_table[square]
                        : PieceSquareTables.mg_knight_table[square];
            case 'n':
                return isEndgame ? -PieceSquareTables.eg_knight_table[63 - square]
                        : -PieceSquareTables.mg_knight_table[63 - square];
            case 'B':
                return isEndgame ? PieceSquareTables.eg_bishop_table[square]
                        : PieceSquareTables.mg_bishop_table[square];
            case 'b':
                return isEndgame ? -PieceSquareTables.eg_bishop_table[63 - square]
                        : -PieceSquareTables.mg_bishop_table[63 - square];
            case 'R':
                return isEndgame ? PieceSquareTables.eg_rook_table[square] : PieceSquareTables.mg_rook_table[square];
            case 'r':
                return isEndgame ? -PieceSquareTables.eg_rook_table[63 - square]
                        : -PieceSquareTables.mg_rook_table[63 - square];
            case 'Q':
                return isEndgame ? PieceSquareTables.eg_queen_table[square] : PieceSquareTables.mg_queen_table[square];
            case 'q':
                return isEndgame ? -PieceSquareTables.eg_queen_table[63 - square]
                        : -PieceSquareTables.mg_queen_table[63 - square];
            case 'K':
                return isEndgame ? PieceSquareTables.eg_king_table[square] : PieceSquareTables.mg_king_table[square];
            case 'k':
                return isEndgame ? -PieceSquareTables.eg_king_table[63 - square]
                        : -PieceSquareTables.mg_king_table[63 - square];
            default:
                return 0;
        }
    }

}
