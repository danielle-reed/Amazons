package amazons;

import java.util.Stack;
import java.util.Iterator;
import java.util.Collections;

import static amazons.Piece.*;


/** The state of an Amazons Game.
 *  @author Danielle Saypoff
 */
class Board {

    /** The number of squares on a side of the board. */
    static final int SIZE = 10;

    /** 2d array representation of the gameboard. */
    private Piece[][] board = new Piece[SIZE][SIZE];

    /** A Stack to keep track of past moves in the form
     * of an array of squares for each move. */
    private Stack<Square[]> oldmoves = new Stack<>();


    /** Initializes a game board with SIZE squares on a side in the
     *  initial position. */
    Board() {
        init();
    }

    /** Initializes a copy of MODEL. */
    Board(Board model) {
        copy(model);
    }

    /** Copies MODEL into me. */
    void copy(Board model) {
        if (model == this) {
            return;
        } else {
            this.board = new Piece[SIZE][SIZE];
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    board[i][j] = model.board[i][j];
                }
            }
            oldmoves = model.oldmoves;
            _winner = model.winner();
            _turn = model.turn();
        }
    }

    /** Clears the board to the initial position. */
    void init() {
        _turn = WHITE;
        _winner = EMPTY;
        oldmoves = new Stack<>();

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                put(EMPTY, j, i);
            }
        }
        put(WHITE, 3, 0);
        put(WHITE, 6, 0);
        put(WHITE, 0, 3);
        put(WHITE, 9, 3);

        put(BLACK, 3, 9);
        put(BLACK, 6, 9);
        put(BLACK, 0, 6);
        put(BLACK, 9, 6);

    }

    /** Return the Piece whose move it is (WHITE or BLACK). */
    Piece turn() {
        return _turn;
    }

    /** Return the number of moves (that have not been undone) for this
     *  board. */
    int numMoves() {
        return oldmoves.size();
    }

    /** Return the winner in the current position, or null if the game is
     *  not yet finished. */
    Piece winner() {
        if (!this.legalMoves(turn()).hasNext()) {
            return turn().opponent();
        }

        return null;
    }

    /** Return the contents the square at S. */
    final Piece get(Square s) {
        return board[SIZE - s.row() - 1][s.col()];
    }

    /** Return the contents of the square at (COL, ROW), where
     *  0 <= COL, ROW <= 9. */
    final Piece get(int col, int row) {
        return board[row][col];
    }

    /** Return the contents of the square at COL ROW. */
    final Piece get(char col, char row) {
        return get(col - 'a', row - '1');
    }

    /** Set square S to P. */
    final void put(Piece p, Square s) {
        int r = s.row();
        int c = s.col();
        put(p, c, r);
    }

    /** Set square (COL, ROW) to P. */
    final void put(Piece p, int col, int row) {
        board[row][col] = p;
        _winner = EMPTY;
    }

    /** Set square COL ROW to P. */
    final void put(Piece p, char col, char row) {
        put(p, col - 'a', row - '1');
    }

    /** Return true iff FROM - TO is an unblocked queen move on the current
     *  board, ignoring the contents of ASEMPTY, if it is encountered.
     *  For this to be true, FROM-TO must be a queen move and the
     *  squares along it, other than FROM and ASEMPTY, must be
     *  empty. ASEMPTY may be null, in which case it has no effect. */
    boolean isUnblockedMove(Square from, Square to, Square asEmpty) {
        if (to == null || !from.isQueenMove(to)) {
            return false;
        }
        int steps;
        int d = from.direction(to);
        int [] whichway = Square.DIR[d];

        if (d == 0 || d == 1 || d == 3 || d == 4 || d == 5 || d == 7) {
            steps = Math.abs(from.row() - to.row());
        } else {
            steps = Math.abs(from.col() - to.col());
        }

        int r = from.row();
        int c = from.col();

        for (int i = 0; i < steps; i += 1) {
            if (board[r + whichway[1]][c + whichway[0]] == EMPTY
                    || Square.sq(c + whichway[0], r + whichway[1]) == asEmpty) {
                c += whichway[0];
                r += whichway[1];
            } else {
                return false;
            }
        } return true;
    }

    /** Return true iff FROM is a valid starting square for a move. */
    boolean isLegal(Square from) {
        if (from.row() < SIZE && from.col() < SIZE
                &&  (board[from.row()][from.col()] == turn())) {
            return true;
        }
        return false;
    }

    /** Return true iff FROM-TO is a valid first part of move, ignoring
     *  spear throwing. */
    boolean isLegal(Square from, Square to) {
        if (isLegal(from) && to.row() < SIZE && to.col() < SIZE
                && (board[to.row() ][to.col()] == EMPTY)) {
            return true;
        }
        return false;
    }

    /** Return true iff FROM-TO(SPEAR) is a legal move in the current
     *  position. */
    boolean isLegal(Square from, Square to, Square spear) {
        if (isLegal(from, to) && spear.row() < SIZE && spear.col() < SIZE
                && (board[spear.row()][spear.col()] == EMPTY
                || board[spear.row()][spear.col()]
                == board[from.row()][from.col()])) {
            return true;
        }
        return false;
    }

    /** Return true iff MOVE is a legal move in the current
     *  position. */
    boolean isLegal(Move move) {
        if (isLegal(move.from(), move.to(), move.spear())) {
            return true;
        }
        return false;
    }

    /** Move FROM-TO(SPEAR), assuming this is a legal move. */
    void makeMove(Square from, Square to, Square spear) {
        Square [] storeposn = new Square[3];
        storeposn[0] = from;
        storeposn[1] = to;
        storeposn[2] = spear;
        oldmoves.push(storeposn);
        if (isLegal(from, to, spear) && isUnblockedMove(from, to, from)) {
            put(turn(), to);
            put(EMPTY, from);
            put(SPEAR, spear);
        }
        if (_turn.equals(WHITE)) {
            _turn = BLACK;
        } else {
            _turn = WHITE;
        }
    }


    /** Move according to MOVE, assuming it is a legal move. */
    void makeMove(Move move) {
        makeMove(move.from(), move.to(), move.spear());
    }

    /** Undo one move.  Has no effect on the initial board. */
    void undo() {
        Square[] last = oldmoves.pop();
        board[last[2].row()][last[2].col()] = EMPTY;
        if (_turn.opponent() == WHITE) {
            board[last[0].row()][last[0].col()] = WHITE;
        } else {
            board[last[0].row()][last[0].col()] = BLACK;
        }
        board[last[1].row()][last[1].col()] = EMPTY;

        if (_turn == WHITE) {
            _turn = BLACK;
        } else {
            _turn = WHITE;
        }
    }

    /** Return an Iterator over the Squares that are reachable by an
     *  unblocked queen move from FROM. Does not pay attention to what
     *  piece (if any) is on FROM, nor to whether the game is finished.
     *  Treats square ASEMPTY (if non-null) as if it were EMPTY.  (This
     *  feature is useful when looking for Moves, because after moving a
     *  piece, one wants to treat the Square it came from as empty for
     *  purposes of spear throwing.) */
    Iterator<Square> reachableFrom(Square from, Square asEmpty) {
        return new ReachableFromIterator(from, asEmpty);
    }

    /** Return an Iterator over all legal moves on the current board. */
    Iterator<Move> legalMoves() {
        return new LegalMoveIterator(_turn);
    }

    /** Return an Iterator over all legal moves on the current board for
     *  SIDE (regardless of whose turn it is). */
    Iterator<Move> legalMoves(Piece side) {
        return new LegalMoveIterator(side);
    }

    /** An iterator used by reachableFrom. */
    private class ReachableFromIterator implements Iterator<Square> {

        /** Iterator of all squares reachable by queen move from FROM,
         *  treating ASEMPTY as empty. */
        ReachableFromIterator(Square from, Square asEmpty) {
            _from = from;
            _dir = 0;
            _steps = 0;
            _asEmpty = asEmpty;
            toNext();
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public Square next() {
            Square temp1 = _from.queenMove(_dir, _steps);
            toNext();
            Square temp = _from.queenMove(_dir, _steps);
            if (get(temp) == EMPTY || temp == _asEmpty) {
                _next = _from.queenMove(_dir, _steps);
            }
            return temp1;
        }

        /** Advance _dir and _steps, so that the next valid Square is
         *  _steps steps in direction _dir from _from. */
        private void toNext() {
            while (true) {
                if (_dir > 7) {
                    next = null;
                    return;
                }
                if (isUnblockedMove(_from, _from.queenMove
                        (_dir, _steps + 1), _asEmpty)) {
                    _steps += 1;
                    next = _from.queenMove(_dir, _steps);
                    return;
                } else if (!isUnblockedMove(_from, _from.queenMove
                                (_dir, _steps + 1), _asEmpty)) {
                    _dir += 1;
                    _steps = 0;
                }
            }


        }


        /** Starting square. */
        private Square _from;
        /** Current direction. */
        private int _dir;
        /** Current distance. */
        private int _steps;
        /** Square treated as empty. */
        private Square _asEmpty;
        /** Square to track next move of iterator. */
        private Square next;
        /** Square to track a queen move. */
        private Square _next;
    }

    /** An iterator used by legalMoves. */
    private class LegalMoveIterator implements Iterator<Move> {

        /** All legal moves for SIDE (WHITE or BLACK). */
        LegalMoveIterator(Piece side) {
            _startingSquares = Square.iterator();
            _spearThrows = NO_SQUARES;
            _pieceMoves = NO_SQUARES;
            _fromPiece = side;
            numPieces = new Square[4];
            int c = 0;
            while ((_startingSquares.hasNext())) {
                Square sidequeen = _startingSquares.next();
                if (get(sidequeen.col(), sidequeen.row()) == _fromPiece) {
                    numPieces[c] = sidequeen;
                    c += 1;
                }
            }
            count = 0;
            _spearThrows = NO_SQUARES;
            _start = numPieces[count];
            _pieceMoves = reachableFrom(_start, null);
            toNext();
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public Move next() {
            Move temp = next;
            toNext();
            return temp;
        }


        /** Advance so that the next valid Move is
         *  _start-_nextSquare(sp), where sp is the next value of
         *  _spearThrows. */
        private void toNext() {
            if (_spearThrows.hasNext()) {
                _nextSpear = _spearThrows.next();
            } else {
                while (!(_pieceMoves.hasNext()) && !_spearThrows.hasNext()) {
                    count += 1;
                    if (count >= 4) {
                        next = null;
                        return;
                    }
                    _start = numPieces[count];
                    _pieceMoves = reachableFrom(_start, null);
                }
                _nextSquare = _pieceMoves.next();
                _spearThrows = reachableFrom(_nextSquare, _start);
                _nextSpear = _spearThrows.next();
            }
            next = Move.mv(_start, _nextSquare, _nextSpear);
        }



        /** Color of side whose moves we are iterating. */
        private Piece _fromPiece;
        /** Current starting square. */
        private Square _start;
        /** Remaining starting squares to consider. */
        private Iterator<Square> _startingSquares;
        /** Current piece's new position. */
        private Square _nextSquare;
        /** Remaining moves from _start to consider. */
        private Iterator<Square> _pieceMoves;
        /** Remaining spear throws from _piece to consider. */
        private Iterator<Square> _spearThrows;

        /** The next spear that is returned from the spear throw iterator. */
        private Square _nextSpear;

        /** keeps track of the next iterator call. */
        private Move next;

        /** An array of squares to keep track of the squares of side. */
        private Square[] numPieces;

        /** Counter for adding pieces to numPieces array to make sure I get all
         * 4 of a side's pieces. */
        private int count;


    }

    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < SIZE; i++) {
            result += "  ";
            for (int j = 0; j < SIZE; j++) {
                result += " " + board[SIZE - i - 1][j];
            }
            result += "\n";
        }
        return result;
    }

    /** An empty iterator for initialization. */
    private static final Iterator<Square> NO_SQUARES =
        Collections.emptyIterator();

    /** Piece whose turn it is (BLACK or WHITE). */
    private Piece _turn;
    /** Cached value of winner on this board, or EMPTY if it has not been
     *  computed. */
    private Piece _winner;
}
