package amazons;


/** A Player that takes input as text commands from the standard input.
 *  @author Danielle Saypoff
 */
class TextPlayer extends Player {

    /** A new TextPlayer with no piece or controller (intended to produce
     *  a template). */
    TextPlayer() {
        this(null, null);
    }

    /** A new TextPlayer playing PIECE under control of CONTROLLER. */
    private TextPlayer(Piece piece, Controller controller) {
        super(piece, controller);
    }

    @Override
    Player create(Piece piece, Controller controller) {
        return new TextPlayer(piece, controller);
    }

    @Override
    String myMove() {
        while (true) {
            String line = _controller.readLine();
            if (line != null && !Move.isGrammaticalMove(line)) {
                return line;
            }
            if (line == null) {
                return "quit";
            } else if (Move.mv(line) == null && Move.isGrammaticalMove(line)
                    || !board().isLegal(Move.mv(line))
                    || !board().isUnblockedMove(Move.mv(line).from(),
                    Move.mv(line).to(), Move.mv(line).from())
                    || !board().isUnblockedMove(Move.mv(line).to(),
                    Move.mv(line).spear(), Move.mv(line).from())
            ) {
                _controller.reportError("Invalid move. "
                        + "Please try again.");
                continue;
            } else {
                return line;
            }
        }
    }
}
