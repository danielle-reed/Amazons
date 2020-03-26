package amazons;


import java.util.Iterator;

import static java.lang.Math.*;

import static amazons.Piece.*;

/** A Player that automatically generates moves.
 *  @author Danielle Saypoff
 */
class AI extends Player {

    /** A position magnitude indicating a win (for white if positive, black
     *  if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 1;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;

    /** A new AI with no piece or controller (intended to produce
     *  a template). */
    AI() {
        this(null, null);
    }

    /** A new AI playing PIECE under control of CONTROLLER. */
    AI(Piece piece, Controller controller) {
        super(piece, controller);
    }

    @Override
    Player create(Piece piece, Controller controller) {
        return new AI(piece, controller);
    }

    @Override
    String myMove() {
        Move move = findMove();
        _controller.reportMove(move);
        return move.toString();
    }

    /** Return a move for me from the current position, assuming there
     *  is a move. */
    private Move findMove() {
        Board b = new Board(board());
        if (_myPiece == WHITE) {
            findMove(b, maxDepth(b), true, 1, -INFTY, INFTY);
        } else {
            findMove(b, maxDepth(b), true, -1, -INFTY, INFTY);
        }
        return _lastFoundMove;
    }

    /** The move found by the last call to one of the ...FindMove methods
     *  below. */
    private Move _lastFoundMove;

    /** counter that I use when I make my recursive call to track
     * how many moves are being made on my board.*/
    private int numberofmoves = 0;

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _lastFoundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _lastMoveFound. */
    private int findMove(Board board, int depth, boolean saveMove, int sense,
                         int alpha, int beta) {
        if (depth == 0 || board.winner() != null) {
            return staticScore(board);
        }
        int store;
        if (sense == 1) {
            store = -INFTY;
            Iterator<Move> biter = board.legalMoves(board.turn());
            while (biter.hasNext()) {
                Move nextmove = biter.next();
                board.makeMove(nextmove);
                int temp = findMove(board, depth - 1, false, -1, alpha, beta);
                if (saveMove && temp > store) {
                    _lastFoundMove = nextmove;
                    numberofmoves += 1;
                }
                board.undo();
                store = max(store, temp);
                alpha = max(alpha, store);
                if (alpha >= beta) {
                    break;
                }
            }
            return store;

        } else {
            store = INFTY;
            Iterator<Move> biter1 = board.legalMoves(board.turn());
            while (biter1.hasNext()) {
                Move nextmove1 = biter1.next();

                board.makeMove(nextmove1);
                int temp1 = findMove(board, depth - 1, false, 1, alpha, beta);
                if (saveMove && temp1 < store) {
                    _lastFoundMove = nextmove1;
                    numberofmoves += 1;
                }
                board.undo();
                store = min(store, temp1);
                beta = min(beta, store);
                if (alpha >= beta) {
                    break;
                }
            }
            return store;
        }
    }

    /** Return a heuristically determined maximum search depth
     *  based on characteristics of BOARD. */
    private int maxDepth(Board board) {

        int N = board.numMoves();
        if (N < FIRST) {
            return 2;
        } else if (N < SECOND) {
            return 3;
        } else {
            return 5;
        }
    }
    /** Return an integer . */
    private static final int FIRST = 40;
    /** Return a heuristic value for BOARD. */
    private static final int SECOND = 60;


    /** Return a heuristic value for BOARD. */
    private int staticScore(Board board) {
        int finalcounter = 0;

        Piece winner = board.winner();
        if (winner == BLACK) {
            return -WINNING_VALUE;
        } else if (winner == WHITE) {
            return WINNING_VALUE;
        } else {
            Piece opponent = board().turn().opponent();
            for (int r = 0; r < board.SIZE; r++) {
                for (int c = 0; c < board.SIZE; c++) {
                    if (board.get(c, r) == BLACK) {
                        Square temp = Square.sq(c, r);
                        finalcounter -= helper(board, temp);
                    } else if (board.get(c, r) == WHITE) {
                        Square temp = Square.sq(c, r);
                        finalcounter += helper(board, temp);
                    }
                }
            }
        }

        return finalcounter;
    }

    /** helper method for static score that checks the SQUARE one step
     * in every direction on BOARD to see if it has a spear, and counts
     * these spears to return how many. */
    private int helper(Board board, Square square) {
        int dir = 0;
        int counter = 0;
        while (dir < 8) {
            if (square.queenMove(dir, 1) != null
                    && board.get(square.queenMove(dir, 1)) != EMPTY) {
                counter += 1;
            }
            dir += 1;
        }
        return counter;
    }


}
