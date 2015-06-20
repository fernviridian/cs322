package ast;
import compiler.Failure;

/** Abstract syntax for floating point literals.
 */
public class FloatLit extends Expr {

    /** The numeric value of this floating point literal.
     */
    private float val;

    /** Default constructor.
     */
    public FloatLit(float val) {
        this.val = val;
    }

    /** Print an indented description of this abstract syntax node,
     *  including a name for the node itself at the specified level
     *  of indentation, plus more deeply indented descriptions of
     *  any child nodes.
     */
    public void indent(IndentOutput out, int n) {
        out.indent(n, "FloatLit(" + val + ")");
    }

    /** Calculate the type of this expression, using the given context
     *  and type environment.
     */
    public Type typeOf(Context ctxt, TypeEnv locals)
      throws Failure {
        return Type.DOUBLE;
    }

    /** Return the depth of this expression as a measure of how complicated
     *  the expression is / how many registers will be needed to evaluate it.
     */
    int getDepth() {
        return 1;
    }

    /** Generate assembly language code for this expression that will
     *  evaluate the expression when it is executed and leave the result
     *  in the next free register, as specified by the layout.
     */
    public void compileExpr(Assembly a, Frame f) {
        throw new Error("compileExpr not implemented for FloatLit");
    }
}
