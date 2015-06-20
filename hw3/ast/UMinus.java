package ast;
import compiler.Failure;

/** Abstract syntax for a unary minus operation.
 */
public class UMinus extends Expr {

    /** The expression to be negated.
     */
    private Expr exp;

    /** Default constructor.
     */
    public UMinus(Expr exp) {
        this.exp = exp;
    
        // Compute the depth of this expression:
        depth = 1 + exp.getDepth();
    }

    /** This attribute will be filled in during static analysis to record
     *  the type of the arguments for this operator (both left and right
     *  arguments are required to have the same type).  This information
     *  will be useful for situations in code generation where we need
     *  to distinguish between using an integer or a floating point
     *  version of an operation.  The type attribute is set to null when
     *  a node is first created to indicate that the type has yet
     *  to be determined.
     */
    protected Type type = null;

    /** Print an indented description of this abstract syntax node,
     *  including a name for the node itself at the specified level
     *  of indentation, plus more deeply indented descriptions of
     *  any child nodes.
     */
    public void indent(IndentOutput out, int n) {
        out.indent(n, "UMinus");
        exp.indent(out, n+1);
    }

    /** Calculate the type of this expression, using the given context
     *  and type environment.
     */
    public Type typeOf(Context ctxt, TypeEnv locals)
      throws Failure {
        type = exp.typeOf(ctxt, locals);
        if (!type.isNumeric()) {
            throw new Failure("UMinusNumeric");
        }
        return type;
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

    /** Generate assembly language code for this expression that will
     *  evaluate the expression when it is executed and leave the result
     *  in the next free register, as specified by the layout.
     */
    public void compileExpr(Assembly a, Frame f) {
      exp.compileExpr(a,f); //put into current free register
      a.emit("negl", f.free32());
    }
}
