package ast;
import compiler.Failure;

/** Abstract syntax for print statements.
 */
public class Print extends Stmt {

    /** The value that should be printed out.
     */
    private Expr exp;

    /** Default constructor.
     */
    public Print(Expr exp) {
        this.exp = exp;
    }

    /** This attribute will be filled in during static analysis to
     *  record the type of the value that is to be printed.
     */
    private Type type = null;

    /** Print an indented description of this abstract syntax node,
     *  including a name for the node itself at the specified level
     *  of indentation, plus more deeply indented descriptions of
     *  any child nodes.
     */
    public void indent(IndentOutput out, int n) {
        out.indent(n, (type==null) ? "Print" : ("Print " + type));
        exp.indent(out, n+1);
    }

    /** Type check this statement, using the specified context, with
     *  the given initial typing environment, and returning the typing
     *  environment for a following statement.
     */
    public TypeEnv check(Context ctxt, TypeEnv locals)
      throws Failure {
        type = exp.typeOf(ctxt, locals);
        return locals;
    }

    /** Generate code for executing this statement.
     *  Returns true if there is a chance that execution may
     *  continue with the next statement.
     */
    public boolean compile(Assembly a, Frame f) {
        // Print is implemented by calling a runtime library
        // function called "print" that takes one argument:
        CallFrame cf = f.prepareCallFrame(a, 1);
        exp.compileExpr(a, cf);
        cf.saveArg(a);
        cf.call(a, "print");
        f.removeCallFrame(a);
        return true;
    }
}
