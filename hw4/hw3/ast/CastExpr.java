package ast;
import compiler.Failure;

/** An abstract base class for cast expressions (i.e.,
 *  expressions that take an operand of one type and
 *  convert the resulting value in to a different type).
 */
public abstract class CastExpr extends Expr {

    /** The operand for the cast.
     */
    protected Expr exp;

    /** Default constructor.
     */
    public CastExpr(Expr exp) {
        this.exp = exp;
    
        // Compute the depth of this expression:
        depth = 1 + exp.getDepth();
    }

    /** Return the depth of this expression as a measure of how complicated
     *  the expression is / how many registers will be needed to evaluate it.
     */
    int getDepth() {
        // Return the depth value that was computed by the constructor
        return depth;
    }

    /** Records the depth of this expression; this value is computed
     *  at the time the constructor is called and then saved here so
     *  that it can be accessed without further computation later on.
     */
    protected int depth;
}
