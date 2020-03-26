package amazons;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static amazons.Utils.*;

/** Represents a position on an Amazons board.  Positions are numbered
 *  from 0 (lower-left corner) to 99 (upper-right corner).  Squares
 *  are immutable and unique: there is precisely one square created for
 *  each distinct position.  Clients create squares using the factory method
 *  sq, not the constructor.  Because there is a unique Square object for each
 *  position, you can freely use the cheap == operator (rather than the
 *  .equals method) to compare Squares, and the program does not waste time
 *  creating the same square over and over again.
 *  @author Danielle Saypoff
 */
final class Square {

    /** The regular expression for a square designation (e.g.,
     *  a3). For convenience, it is in parentheses to make it a
     *  group.  This subpattern is intended to be incorporated into
     *  other pattern that contain square designations (such as
     *  patterns for moves). */
    static final String SQ = "([a-j](?:[1-9]|10))";

    /** Return my row position, where 0 is the bottom row. */
    int row() {
        return _row;
    }

    /** Return my column position, where 0 is the leftmost column. */
    int col() {
        return _col;
    }

    /** Return my index position (0-99).  0 represents square a1, and 99
     *  is square j10. */
    int index() {
        return _index;
    }

    /** Return true iff THIS - TO is a valid queen move. */
    boolean isQueenMove(Square to) {
        float c = to.col() - this.col();
        float r = to.row() - this.row();
        return (this != to
            && ((to.col() == this.col())
                || (to.row() == this.row())
                || (Math.abs(c / r) == 1)));
    }

    /** Definitions of direction for queenMove.  DIR[k] = (dcol, drow)
     *  means that to going one step from (col, row) in direction k,
     *  brings us to (col + dcol, row + drow). */
    public static final int[][] DIR = {
        { 0, 1 }, { 1, 1 }, { 1, 0 }, { 1, -1 },
        { 0, -1 }, { -1, -1 }, { -1, 0 }, { -1, 1 }
    };

    /** Return the Square that is STEPS>0 squares away from me in direction
     *  DIR, or null if there is no such square.
     *  DIR = 0 for north, 1 for northeast, 2 for east, etc., up to 7 for.
     *  northwest. If DIR has another value, return null. Thus, unless the.
     *  result is null the resulting square is a queen move away from me. */
    Square queenMove(int dir, int steps) {
        if (dir == 0) {
            if (exists(this.col(), this.row() + steps)) {
                return sq(this.col(), this.row() + steps);
            }
            return null;
        } else if (dir == 4) {
            if (exists(this.col(), this.row() - steps)) {
                return sq(this.col(), this.row() - steps);
            }
            return null;
        } else if (dir == 2) {
            if (exists(this.col() + steps, this.row())) {
                return sq(this.col() + steps, this.row());
            }
            return null;
        } else if (dir == 6) {
            if (exists(this.col() - steps, this.row())) {
                return sq(this.col() - steps, this.row());
            }
            return null;
        } else if (dir == 1) {
            if (exists(this.col() + steps, this.row() + steps)) {
                return sq(this.col() + steps, this.row() + steps);
            }
            return null;
        } else if (dir == 3) {
            if (exists(this.col() + steps, this.row() - steps)) {
                return sq((this.col() + steps), this.row() - steps);
            }
            return null;
        } else if (dir == 5) {
            if (exists(this.col() - steps, this.row() - steps)) {
                return sq((this.col() - steps), this.row() - steps);
            }
            return null;
        } else if (dir == 7) {
            if (exists(this.col() - steps, this.row() + steps)) {
                return sq((this.col() - steps), this.row() + steps);
            }
            return null;
        }
        return this;
    }

    /** Return the direction (an int as defined in the documentation
     *  for queenMove) of the queen move THIS-TO. */
    int direction(Square to) {
        if (this.col() == to.col() && this.row() > to.row()) {
            return 4;
        }
        if (this.col() == to.col() && this.row() < to.row()) {
            return 0;
        }
        if (this.row() == to.row() && this.col() > to.col()) {
            return 6;
        }
        if (this.row() == to.row() && this.col() < to.col()) {
            return 2;
        }
        if (this.row() > to.row() && this.col() > to.col()) {
            return 5;
        }
        if (this.row() < to.row() && this.col() > to.col()) {
            return 7;
        }
        if (this.row() < to.row() && this.col() < to.col()) {
            return 1;
        }
        if (this.row() > to.row() && this.col() < to.col()) {
            return 3;
        }
        assert isQueenMove(to);
        return -1;
    }

    @Override
    public String toString() {
        return _str;
    }

    /** Return true iff COL ROW is a legal square. */
    static boolean exists(int col, int row) {
        return row >= 0 && col >= 0 && row < Board.SIZE && col < Board.SIZE;
    }

    /** Return the (unique) Square denoting COL ROW. */
    static Square sq(int col, int row) {
        if (!exists(row, col)) {
            throw error("row or column out of bounds");
        }
        return sq(row * 10 + col);
    }

    /** Return the (unique) Square denoting the position with index INDEX. */
    static Square sq(int index) {
        return SQUARES[index];
    }

    /** A hashmap to map all of the numbers of columns to the corresponding
     * column letter. */
    private static HashMap<String, Integer> colstonumrev = new HashMap<>();
    static {
        String colsalpha = "a b c d e f g h i j ";
        String[] colsalphalist = colsalpha.trim().split("\\s+");
        for (int i = 0; i < Board.SIZE; i += 1) {
            colstonumrev.put(colsalphalist[i], i);
        }
    }

    /** Return the (unique) Square denoting the position COL ROW, where
     *  COL ROW is the standard text format for a square (e.g., a4). */
    static Square sq(String col, String row) {
        Integer rows = Integer.parseInt(row) - 1;
        Integer cols = colstonumrev.get(col);
        return sq(cols, rows);
    }

    /** Return the (unique) Square denoting the position in POSN, in the
     *  standard text format for a square (e.g. a4). POSN must be a
     *  valid square designation. */
    static Square sq(String posn) {
        assert posn.matches(SQ);
        String[] posnlist = posn.trim().split("", 0);
        String col = posnlist[0];
        if (posnlist.length == 2) {
            return sq(col, posnlist[1]);
        } else {
            return sq(col, posnlist[1] + posnlist[2]);
        }
    }

    /** Return an iterator over all Squares. */
    static Iterator<Square> iterator() {
        return SQUARE_LIST.iterator();
    }


    /** A hashmap to map all of the letters to the number that
     * represent the same column. */
    private static HashMap<Integer, String> colstonum = new HashMap<>();
    static {
        String colsalpha = "a b c d e f g h i j ";
        String[] colsalphalist = colsalpha.trim().split("\\s+");
        for (int i = 0; i < Board.SIZE; i += 1) {
            colstonum.put(i, colsalphalist[i]);
        }
    }


    /** Return the Square with index INDEX. */
    private Square(int index) {
        _index = index;
        _row = index / 10;
        _col =  index % 10;
        _str = String.format(colstonum.get(_col) + (_row + 1));
    }

    /** The cache of all created squares, by index. */
    private static final Square[] SQUARES =
        new Square[Board.SIZE * Board.SIZE];

    /** SQUARES viewed as a List. */
    private static final List<Square> SQUARE_LIST = Arrays.asList(SQUARES);

    static {
        for (int i = Board.SIZE * Board.SIZE - 1; i >= 0; i -= 1) {
            SQUARES[i] = new Square(i);
        }
    }

    /** My index position. */
    private final int _index;

    /** My row and column (redundant, since these are determined by _index). */
    private final int _row, _col;

    /** My String denotation. */
    private final String _str;

}
