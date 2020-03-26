package amazons;

import org.junit.Test;
import static amazons.Piece.*;
import static org.junit.Assert.*;
import ucb.junit.textui;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/** The suite of all JUnit tests for the amazons package.
 *  @author Danielle Saypoff
 */
public class UnitTest {

    /** Run the JUnit tests in this package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        textui.runClasses(UnitTest.class);
    }


    @Test
    public void testcopy() {
        Board b = new Board();
        Board t = new Board();
        b.put(SPEAR, Square.sq("c3"));
        b.put(SPEAR, Square.sq("c4"));
        t.copy(b);
        assertEquals(b.toString(), t.toString());

    }


    @Test
    public void testWinner() {
        Board b = new Board();
        b.put(SPEAR, Square.sq("c", "1"));
        b.put(SPEAR, Square.sq("c", "2"));
        b.put(SPEAR, Square.sq("d", "2"));
        b.put(SPEAR, Square.sq("e", "1"));
        b.put(SPEAR, Square.sq("e", "2"));
        b.put(SPEAR, Square.sq("f", "1"));
        b.put(SPEAR, Square.sq("f", "2"));
        b.put(SPEAR, Square.sq("g", "2"));
        b.put(SPEAR, Square.sq("h", "1"));
        b.put(SPEAR, Square.sq("h", "2"));
        b.put(SPEAR, Square.sq("j", "3"));
        b.put(SPEAR, Square.sq("i", "3"));
        b.put(SPEAR, Square.sq("i", "4"));
        b.put(SPEAR, Square.sq("j", "5"));
        b.put(SPEAR, Square.sq("i", "5"));
        b.put(SPEAR, Square.sq("a", "3"));
        b.put(SPEAR, Square.sq("b", "3"));
        b.put(SPEAR, Square.sq("b", "4"));
        b.put(SPEAR, Square.sq("b", "5"));
        b.put(SPEAR, Square.sq("a", "5"));
        assertNotEquals(b.winner(), WHITE);
        assertEquals(b.winner(), BLACK);
    }



    @Test
    public void testReachableFromIterater() {
        Board b = new Board();
        Board w = new Board();
        Board t = new Board();
        Board skip = new Board();
        b.put(WHITE, Square.sq("d", "4"));
        b.put(SPEAR, Square.sq("d", "6"));
        b.put(SPEAR, Square.sq("f", "6"));
        b.put(SPEAR, Square.sq("g", "4"));
        b.put(SPEAR, Square.sq("f", "2"));
        b.put(SPEAR, Square.sq("b", "2"));
        b.put(SPEAR, Square.sq("b", "4"));
        b.put(SPEAR, Square.sq("b", "6"));
        Iterator<Square> store =
                b.reachableFrom(Square.sq("d", "4"), Square.sq("d", "4"));
        Iterator<Square> store2 =
                w.reachableFrom(Square.sq("i", "9"), Square.sq("i", "9"));
        Iterator<Square> store3 =
                t.reachableFrom(Square.sq("g", "10"), Square.sq("g", "10"));
        Iterator<Square> store4 =
                skip.reachableFrom(Square.sq("d", "1"), Square.sq("d", "1"));
        assertEquals(store.next(), Square.sq("d", "5"));
        assertEquals(store.next(), Square.sq("e", "5"));
        assertEquals(store.next(), Square.sq("e", "4"));
        assertEquals(store.next(), Square.sq("f", "4"));
        assertEquals(store.next(), Square.sq("e", "3"));
        assertEquals(store.next(), Square.sq("d", "3"));
        assertEquals(store.next(), Square.sq("d", "2"));
        assertEquals(store.next(), Square.sq("c", "3"));
        assertEquals(store.next(), Square.sq("c", "4"));
        assertTrue(store.hasNext());
        assertEquals(store.next(), Square.sq("c", "5"));
        assertFalse(store.hasNext());
        w.put(WHITE, Square.sq("i", "9"));
        assertEquals(store2.next(), Square.sq("i", "10"));
        assertEquals(store2.next(), Square.sq("j", "10"));
        assertEquals(store2.next(), Square.sq("j", "9"));
        assertEquals(store2.next(), Square.sq("j", "8"));
        assertEquals(store2.next(), Square.sq("i", "8"));
        assertEquals(store2.next(), Square.sq("i", "7"));
        assertEquals(store3.next(), Square.sq("h", "10"));
        skip.put(SPEAR, Square.sq("d", "3"));
        skip.put(SPEAR, Square.sq("e", "2"));
        skip.put(SPEAR, Square.sq("b", "1"));
        skip.put(SPEAR, Square.sq("b", "3"));
        assertEquals(store4.next(), Square.sq("d", "2"));
        assertEquals(store4.next(), Square.sq("e", "1"));
        assertEquals(store4.next(), Square.sq("f", "1"));
        assertEquals(store4.next(), Square.sq("c", "1"));
        assertEquals(store4.next(), Square.sq("c", "2"));




    }

    @Test
    public void testMakeMove() {
        Board b = new Board();
        b.makeMove(Square.sq(3, 0), Square.sq(3, 5), Square.sq(4, 3));
        assertTrue(b.get(4, 3) == SPEAR
                && b.get(3, 5) == WHITE && b.get(3, 0) == EMPTY);
        b.makeMove(Square.sq(6, 9), Square.sq(6, 2), Square.sq(6, 7));
        assertTrue(b.get(6, 7) == SPEAR
                && b.get(6, 2) == BLACK && b.get(6, 9) == EMPTY);

    }

    @Test
    public void testUndo() {
        Board b = new Board();
        Board w = new Board();
        b.makeMove(Square.sq(3, 0), Square.sq(3, 5), Square.sq(4, 3));
        b.undo();
        assertEquals(w.toString(), b.toString());
        Board t = new Board();
        Board t1 = new Board();
        t.makeMove(Square.sq(3, 0), Square.sq(3, 2), Square.sq(3, 3));
        t.makeMove(Square.sq(6, 9), Square.sq(6, 7), Square.sq(6, 8));
        t.undo();
        t1.makeMove(Square.sq(3, 0), Square.sq(3, 2), Square.sq(3, 3));

        assertEquals(t1.toString(), t.toString());


    }

    @Test
    public void testIsUnblockedMove() {
        Board b = new Board();
        b.put(BLACK, Square.sq("d", "4"));
        b.put(BLACK, Square.sq("d",  "6"));
        b.put(BLACK, Square.sq("d",  "2"));
        b.put(BLACK, Square.sq("f",  "4"));
        b.put(BLACK, Square.sq("b",  "4"));
        b.put(BLACK, Square.sq("f",  "6"));
        b.put(BLACK, Square.sq("b",  "6"));
        b.put(BLACK, Square.sq("b",  "2"));
        b.put(BLACK, Square.sq("f",  "2"));
        assertTrue(b.isUnblockedMove(Square.sq("f", "6"),
                Square.sq("j", "10"), Square.sq("f", "6")));
        assertTrue(b.isUnblockedMove(Square.sq("d", "4"),
                Square.sq("d", "5"), Square.sq("d", "4")));
        assertFalse(b.isUnblockedMove(Square.sq("d", "4"),
                Square.sq("d", "6"), Square.sq("d", "4")));
        assertFalse(b.isUnblockedMove(Square.sq("d", "4"),
                Square.sq("d", "7"), Square.sq("d", "4")));
        assertTrue(b.isUnblockedMove(Square.sq("d", "4"),
                Square.sq("d", "7"), Square.sq("d", "6")));
        assertTrue(b.isUnblockedMove(Square.sq("d", "4"),
                Square.sq("d", "3"), Square.sq("d", "4")));
        assertFalse(b.isUnblockedMove(Square.sq("d", "4"),
                Square.sq("d", "2"), Square.sq("d", "4")));
        assertFalse(b.isUnblockedMove(Square.sq("d", "4"),
                Square.sq("d", "1"), Square.sq("d", "4")));
        assertTrue(b.isUnblockedMove(Square.sq("d", "4"),
                Square.sq("e", "4"), Square.sq("d", "4")));
        assertFalse(b.isUnblockedMove(Square.sq("d", "4"),
                Square.sq("f", "4"), Square.sq("d",  "4")));
        assertFalse(b.isUnblockedMove(Square.sq("d", "4"),
                Square.sq("g", "4"), Square.sq("d", "4")));
        assertTrue(b.isUnblockedMove(Square.sq("d", "4"),
                Square.sq("c", "4"), Square.sq("d", "4")));
        assertFalse(b.isUnblockedMove(Square.sq("d", "4"),
                Square.sq("b", "4"), Square.sq("d", "4")));
        assertFalse(b.isUnblockedMove(Square.sq("d", "4"),
                Square.sq("a", "4"), Square.sq("d", "4")));
        assertTrue(b.isUnblockedMove(Square.sq("d", "4"),
                Square.sq("c", "5"), Square.sq("d", "4")));
        assertFalse(b.isUnblockedMove(Square.sq("d", "4"),
                Square.sq("b", "6"), Square.sq("d", "4")));
        assertFalse(b.isUnblockedMove(Square.sq("a", "4"),
                Square.sq("a", "9"), Square.sq("a", "4")));

    }

    @Test
    public void testIsUnblockedMove2() {
        Board b = new Board();
        b.put(BLACK, Square.sq("d", "4"));
        b.put(BLACK, Square.sq("d",  "6"));
        b.put(BLACK, Square.sq("d",  "2"));
        b.put(BLACK, Square.sq("f",  "4"));
        b.put(BLACK, Square.sq("b",  "4"));
        b.put(BLACK, Square.sq("f",  "6"));
        b.put(BLACK, Square.sq("b",  "6"));
        b.put(BLACK, Square.sq("b",  "2"));
        b.put(BLACK, Square.sq("f",  "2"));
        assertFalse(b.isUnblockedMove(Square.sq("d", "4"),
                Square.sq("a", "7"), Square.sq("d", "4")));
        assertTrue(b.isUnblockedMove(Square.sq("d", "4"),
                Square.sq("c", "3"), Square.sq("d", "4")));
        assertFalse(b.isUnblockedMove(Square.sq("d", "4"),
                Square.sq("b", "2"), Square.sq("d", "4")));
        assertFalse(b.isUnblockedMove(Square.sq("d", "4"),
                Square.sq("a", "1"), Square.sq("d",  "4")));
        assertTrue(b.isUnblockedMove(Square.sq("d", "4"),
                Square.sq("e", "5"), Square.sq("d",  "4")));
        assertFalse(b.isUnblockedMove(Square.sq("d", "4"),
                Square.sq("f", "6"), Square.sq("d",  "4")));
        assertFalse(b.isUnblockedMove(Square.sq("d", "4"),
                Square.sq("g", "7"), Square.sq("d",  "4")));
        assertTrue(b.isUnblockedMove(Square.sq("d", "4"),
                Square.sq("e", "3"), Square.sq("d",  "4")));
        assertFalse(b.isUnblockedMove(Square.sq("d", "4"),
                Square.sq("f", "2"), Square.sq("d",  "4")));
        assertFalse(b.isUnblockedMove(Square.sq("d", "4"),
                Square.sq("g", "1"), Square.sq("d",  "4")));

    }

    @Test
    public void testSquare1() {
        Board b = new Board();
        b.put(BLACK, Square.sq("d", "6"));
        assertEquals(BLACK, b.get(3, 5));
        b.put(BLACK, Square.sq("g", "3"));
        assertEquals(BLACK, b.get(6, 2));
        b.put(BLACK, Square.sq("j", "10"));
        assertEquals(BLACK, b.get(9, 9));
    }

    @Test
    public void testSquare2() {
        Board b = new Board();
        b.put(BLACK, Square.sq("d6"));
        assertEquals(BLACK, b.get(3, 5));
        b.put(BLACK, Square.sq("g3"));
        assertEquals(BLACK, b.get(6, 2));
        b.put(BLACK, Square.sq("j10"));
        assertEquals(BLACK, b.get(9, 9));
    }

    @Test
    public void testQueenMove() {
        Board b = new Board();
        b.put(EMPTY, Square.sq("d", "6"));
        b.put(EMPTY, Square.sq("g", "7"));
        b.put(EMPTY, Square.sq("a", "1"));
        b.put(EMPTY, Square.sq("j", "10"));
        assertEquals(Square.sq("d", "6").queenMove(0, 1), Square.sq("d7"));
        assertEquals(Square.sq("d", "6").queenMove(0, 8), null);
        assertEquals(Square.sq("d", "6").queenMove(4, 1), Square.sq("d5"));
        assertEquals(Square.sq("d", "6").queenMove(4, 10), null);
        assertEquals(Square.sq("d", "6").queenMove(2, 1), Square.sq("e6"));
        assertEquals(Square.sq("d", "6").queenMove(2, 8), null);
        assertEquals(Square.sq("d", "6").queenMove(6, 1), Square.sq("c6"));
        assertEquals(Square.sq("d", "6").queenMove(6, 10), null);
        assertEquals(Square.sq("d", "6").queenMove(1, 2), Square.sq("f8"));
        assertEquals(Square.sq("d", "6").queenMove(3, 2), Square.sq("f4"));
        assertEquals(Square.sq("d", "6").queenMove(5, 2), Square.sq("b4"));
        assertEquals(Square.sq("d", "6").queenMove(7, 2), Square.sq("b8"));
        assertEquals(Square.sq("d", "6").queenMove(1, 10), null);
        assertEquals(Square.sq("d", "6").queenMove(3, 10), null);
        assertEquals(Square.sq("d", "6").queenMove(5, 10), null);
        assertEquals(Square.sq("d", "6").queenMove(7, 10), null);
    }



    /** Tests basic correctness of put and get on the initialized board. */
    @Test
    public void testBasicPutGet() {
        Board b = new Board();
        b.put(BLACK, Square.sq(3, 5));
        assertEquals(BLACK, b.get(3, 5));
        b.put(WHITE, Square.sq(9, 9));
        assertEquals(WHITE, b.get(9, 9));
        b.put(EMPTY, Square.sq(3, 5));
        assertEquals(EMPTY, b.get(3, 5));
    }

    /** Tests proper identification of legal/illegal queen moves. */
    @Test
    public void testIsQueenMove() {
        assertFalse(Square.sq(1, 5).isQueenMove(Square.sq(1, 5)));
        assertFalse(Square.sq(1, 5).isQueenMove(Square.sq(2, 7)));
        assertFalse(Square.sq(0, 0).isQueenMove(Square.sq(5, 1)));
        assertTrue(Square.sq(1, 1).isQueenMove(Square.sq(9, 9)));
        assertTrue(Square.sq(2, 7).isQueenMove(Square.sq(8, 7)));
        assertTrue(Square.sq(3, 0).isQueenMove(Square.sq(3, 4)));
        assertTrue(Square.sq(7, 9).isQueenMove(Square.sq(0, 2)));
    }

    /** Tests toString for initial board state and a smiling board state. :) */
    @Test
    public void testToString() {
        Board b = new Board();
        assertEquals(INIT_BOARD_STATE, b.toString());
        makeSmile(b);
        assertEquals(SMILE, b.toString());
    }

    private void makeSmile(Board b) {
        b.put(EMPTY, Square.sq(0, 3));
        b.put(EMPTY, Square.sq(0, 6));
        b.put(EMPTY, Square.sq(9, 3));
        b.put(EMPTY, Square.sq(9, 6));
        b.put(EMPTY, Square.sq(3, 0));
        b.put(EMPTY, Square.sq(3, 9));
        b.put(EMPTY, Square.sq(6, 0));
        b.put(EMPTY, Square.sq(6, 9));
        for (int col = 1; col < 4; col += 1) {
            for (int row = 6; row < 9; row += 1) {
                b.put(SPEAR, Square.sq(col, row));
            }
        }
        b.put(EMPTY, Square.sq(2, 7));
        for (int col = 6; col < 9; col += 1) {
            for (int row = 6; row < 9; row += 1) {
                b.put(SPEAR, Square.sq(col, row));
            }
        }
        b.put(EMPTY, Square.sq(7, 7));
        for (int lip = 3; lip < 7; lip += 1) {
            b.put(WHITE, Square.sq(lip, 2));
        }
        b.put(WHITE, Square.sq(2, 3));
        b.put(WHITE, Square.sq(7, 3));
    }

    static final String INIT_BOARD_STATE =
            "   - - - B - - B - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   B - - - - - - - - B\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   W - - - - - - - - W\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - W - - W - - -\n";

    static final String SMILE =
            "   - - - - - - - - - -\n"
                    + "   - S S S - - S S S -\n"
                    + "   - S - S - - S - S -\n"
                    + "   - S S S - - S S S -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - W - - - - W - -\n"
                    + "   - - - W W W W - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n";


    @Test
    public void testReachableFrom() {
        Board b = new Board();
        buildBoard(b, REACHABLEFROMTESTBOARD);
        int numSquares = 0;
        Set<Square> squares = new HashSet<>();
        Iterator<Square> reachableFrom = b.reachableFrom(Square.sq(5, 5), null);
        while (reachableFrom.hasNext()) {
            Square s = reachableFrom.next();
            assertTrue(REACHABLEFROMTESTSQUARES.contains(s));
            numSquares += 1;
            squares.add(s);
        }
        assertEquals(REACHABLEFROMTESTSQUARES.size(), numSquares);
        assertEquals(REACHABLEFROMTESTSQUARES.size(), squares.size());
    }

    /** Tests legalMovesIterator to make sure it returns all legal Moves.
     *  This method needs to be finished and may need to be changed
     *  based on your implementation. */
    @Test
    public void testLegalMoves() {
        Board b = new Board();
        buildBoard(b, LEGALMOVESTESTBOARD);
        int numMoves = 0;
        Set<Move> moves = new HashSet<>();
        Iterator<Move> legalMoves = b.legalMoves(Piece.WHITE);
        while (legalMoves.hasNext()) {
            Move m = legalMoves.next();
            assertTrue(LEGALMOVESTESTMOVES.contains(m));
            numMoves += 1;
            moves.add(m);
        }
        assertEquals(LEGALMOVESTESTMOVES.size(), numMoves);
        assertEquals(LEGALMOVESTESTMOVES.size(), moves.size());
    }

    /** Tests legalMovesIterator to make sure it returns all legal Moves
     * when the board is empty.*/
    @Test
    public void testLegalMovesemptyboard() {
        Board b = new Board();
        int numMoves = 0;
        Set<Move> moves = new HashSet<>();
        Iterator<Move> legalMoves = b.legalMoves(Piece.WHITE);
        while (legalMoves.hasNext()) {
            Move m = legalMoves.next();
            numMoves += 1;
            moves.add(m);
        }
        assertEquals(2176, numMoves);
        assertEquals(2176, moves.size());
    }


    /** One more legal moves iterator test because why not. */
    @Test
    public void testLegalMoves2() {
        Board b = new Board();
        buildBoard(b, LEGALMOVESTESTBOARD2);
        int numMoves = 0;
        Set<Move> moves = new HashSet<>();
        Iterator<Move> legalMoves = b.legalMoves(Piece.WHITE);
        while (legalMoves.hasNext()) {
            Move m = legalMoves.next();
            assertTrue(LEGALMOVESTESTMOVES2.contains(m));
            numMoves += 1;
            moves.add(m);
        }
        assertEquals(LEGALMOVESTESTMOVES2.size(), numMoves);
        assertEquals(LEGALMOVESTESTMOVES2.size(), moves.size());
    }


    private void buildBoard(Board b, Piece[][] target) {
        for (int col = 0; col < Board.SIZE; col++) {
            for (int row = 0; row < Board.SIZE; row++) {
                Piece piece = target[row][col];
                b.put(piece, Square.sq(col, row));
            }
        }
    }

    static final Piece E = Piece.EMPTY;

    static final Piece W = Piece.WHITE;

    static final Piece B = Piece.BLACK;

    static final Piece S = Piece.SPEAR;

    static final Piece[][] REACHABLEFROMTESTBOARD =
    {
        { E, E, E, E, E, E, E, E, E, E },
        { E, E, E, E, E, E, E, E, W, W },
        { E, E, E, E, E, E, E, S, E, S },
        { E, E, E, S, S, S, S, E, E, S },
        { E, E, E, S, E, E, E, E, B, E },
        { E, E, E, S, E, W, E, E, B, E },
        { E, E, E, S, S, S, B, W, B, E },
        { E, E, E, E, E, E, E, E, E, E },
        { E, E, E, E, E, E, E, E, E, E },
        { E, E, E, E, E, E, E, E, E, E },
    };

    static final Set<Square> REACHABLEFROMTESTSQUARES =
            new HashSet<>(Arrays.asList(
                    Square.sq(5, 4),
                    Square.sq(4, 4),
                    Square.sq(4, 5),
                    Square.sq(6, 5),
                    Square.sq(7, 5),
                    Square.sq(6, 4),
                    Square.sq(7, 3),
                    Square.sq(8, 2)));


    static final Piece[][] LEGALMOVESTESTBOARD =
    {
            { E, S, W, S, W, W, W, S, E, E },
            { E, S, E, S, E, S, S, S, E, E },
            { E, S, E, S, S, S, E, E, E, E },
            { E, S, S, S, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
    };

    static final Piece[][] LEGALMOVESTESTBOARD2 =
    {
            { E, S, W, S, W, S, E, S, E, S },
            { E, S, E, S, E, S, S, S, S, E },
            { E, S, E, S, S, S, E, E, S, W },
            { S, S, S, S, E, E, E, E, S, E },
            { S, W, S, E, E, E, E, E, S, S },
            { S, S, S, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
    };

    static final Set<Move> LEGALMOVESTESTMOVES =
            new HashSet<>(Arrays.asList(
                    Move.mv(Square.sq("c1"), Square.sq("c2"), Square.sq("c3")),
                    Move.mv(Square.sq("c1"), Square.sq("c2"), Square.sq("c1")),
                    Move.mv(Square.sq("c1"), Square.sq("c3"), Square.sq("c2")),
                    Move.mv(Square.sq("c1"), Square.sq("c3"), Square.sq("c1")),
                    Move.mv(Square.sq("f1"), Square.sq("e2"), Square.sq("f1")),
                    Move.mv(Square.sq("e1"), Square.sq("e2"),
                            Square.sq("e1"))));

    static final Set<Move> LEGALMOVESTESTMOVES2 =
            new HashSet<>(Arrays.asList(
                    Move.mv(Square.sq("c1"), Square.sq("c2"), Square.sq("c3")),
                    Move.mv(Square.sq("c1"), Square.sq("c2"), Square.sq("c1")),
                    Move.mv(Square.sq("c1"), Square.sq("c3"), Square.sq("c2")),
                    Move.mv(Square.sq("c1"), Square.sq("c3"), Square.sq("c1")),
                    Move.mv(Square.sq("e1"), Square.sq("e2"), Square.sq("e1")),
                    Move.mv(Square.sq("j3"), Square.sq("j4"), Square.sq("j3")),
                    Move.mv(Square.sq("j3"), Square.sq("j4"), Square.sq("j2")),
                    Move.mv(Square.sq("j3"), Square.sq("j2"), Square.sq("j3")),
                    Move.mv(Square.sq("j3"), Square.sq("j2"), Square.sq("i1")),
                    Move.mv(Square.sq("j3"), Square.sq("j2"),
                            Square.sq("j4"))));


}


