package main.java.com.sisyphusai.engine.helpers;

public class HelperEngineFunctions {
    private static final int PAWN_VALUE = 100;
    private static final int KNIGHT_VALUE = 320;
    private static final int BISHOP_VALUE = 330;
    private static final int ROOK_VALUE = 500;
    private static final int QUEEN_VALUE = 900;
    private static final int KING_VALUE = 20000;

    public static int getPieceValue(char piece) {
        switch (piece) {
            case 'P':
                return PAWN_VALUE;

            case 'N':
                return KNIGHT_VALUE;

            case 'B':
                return BISHOP_VALUE;

            case 'R':
                return ROOK_VALUE;

            case 'Q':
                return QUEEN_VALUE;

            case 'p':
                return PAWN_VALUE;

            case 'n':
                return KNIGHT_VALUE;

            case 'b':
                return BISHOP_VALUE;

            case 'r':
                return ROOK_VALUE;

            case 'q':
                return QUEEN_VALUE;
            default:
                return 0;
        }
    }

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

}