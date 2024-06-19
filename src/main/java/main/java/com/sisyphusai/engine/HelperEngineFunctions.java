package main.java.com.sisyphusai.engine;

public class HelperEngineFunctions {

    // static int getPieceValue(int piece) {
    // piece = Math.abs(piece);

    // switch (Character.toLowerCase(piece)) {
    // case 1:
    // return PAWN_VALUE;
    // case 2:
    // return KNIGHT_VALUE;
    // case 3:
    // return BISHOP_VALUE;
    // case 4:
    // return ROOK_VALUE;
    // case 5:
    // return QUEEN_VALUE;
    // case 6:
    // return KING_VALUE;
    // default:
    // return 0;
    // }
    // }

    public static char[] parseFEN(String fen) {
        // Split the FEN string into its six parts
        String[] parts = fen.split(" ");

        // Calculate the length required for the parsed FEN array
        int boardLength = 64;
        int detailsLength = 1 + 4 + 2 + 4 + 4; // color + castling + en passant + halfmove + fullmove
        char[] parsedFen = new char[boardLength + detailsLength];

        int index = 0;

        // Parse piece placement from the first part of the FEN string
        for (int i = 0; i < parts[0].length(); i++) {
            char c = parts[0].charAt(i);
            if (Character.isDigit(c)) {
                int emptySquares = Character.getNumericValue(c);
                for (int j = 0; j < emptySquares; j++) {
                    parsedFen[index++] = '1'; // Use '1' to denote empty squares
                }
            } else if (c != '/') {
                parsedFen[index++] = c;
            }
        }

        // Parse active color
        parsedFen[boardLength] = parts[1].charAt(0);

        // Parse castling availability
        String castling = parts[2];
        parsedFen[boardLength + 1] = castling.contains("K") ? 'K' : '0';
        parsedFen[boardLength + 2] = castling.contains("Q") ? 'Q' : '0';
        parsedFen[boardLength + 3] = castling.contains("k") ? 'k' : '0';
        parsedFen[boardLength + 4] = castling.contains("q") ? 'q' : '0';

        // Parse en passant target square
        if (parts[3].equals("-")) {
            parsedFen[boardLength + 5] = '0';
            parsedFen[boardLength + 6] = '0';
        } else {
            parsedFen[boardLength + 5] = parts[3].charAt(0);
            parsedFen[boardLength + 6] = parts[3].charAt(1);
        }

        // Parse halfmove clock
        int halfmoveClock = Integer.parseInt(parts[4]);
        String halfmoveString = String.format("%04d", halfmoveClock);
        for (int i = 0; i < 4; i++) {
            parsedFen[boardLength + 7 + i] = halfmoveString.charAt(i);
        }

        // Parse fullmove number
        int fullmoveNumber = Integer.parseInt(parts[5]);
        String fullmoveString = String.format("%04d", fullmoveNumber);
        for (int i = 0; i < 4; i++) {
            parsedFen[boardLength + 11 + i] = fullmoveString.charAt(i);
        }

        return parsedFen;
    }

    public static int materialEvaluator(char[] parsedFen) {
        final int PAWN_VALUE = 100;
        final int KNIGHT_VALUE = 320;
        final int BISHOP_VALUE = 330;
        final int ROOK_VALUE = 500;
        final int QUEEN_VALUE = 900;
        final int KING_VALUE = 20000;

        int totalEvaluation = 0;

        // Iterate over the board part of the parsed FEN
        for (int i = 0; i < 64; i++) {
            char piece = parsedFen[i];
            switch (piece) {
                case 'P':
                    totalEvaluation += PAWN_VALUE;
                    break;
                case 'N':
                    totalEvaluation += KNIGHT_VALUE;
                    break;
                case 'B':
                    totalEvaluation += BISHOP_VALUE;
                    break;
                case 'R':
                    totalEvaluation += ROOK_VALUE;
                    break;
                case 'Q':
                    totalEvaluation += QUEEN_VALUE;
                    break;
                case 'p':
                    totalEvaluation -= PAWN_VALUE;
                    break;
                case 'n':
                    totalEvaluation -= KNIGHT_VALUE;
                    break;
                case 'b':
                    totalEvaluation -= BISHOP_VALUE;
                    break;
                case 'r':
                    totalEvaluation -= ROOK_VALUE;
                    break;
                case 'q':
                    totalEvaluation -= QUEEN_VALUE;
                    break;
            }
        }

        return totalEvaluation;
    }
}
