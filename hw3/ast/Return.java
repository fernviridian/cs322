package ast;
import compiler.Failure;

/** Abstract syntax for return statements.
 */
public class Return extends Stmt {

    /** The value that should be returned (or else null).
     */
    private Expr exp;

    /** Default constructor.
     */
    public Return(Expr exp) {
        this.exp = exp;
    }

    /** Print an indented description of this abstract syntax node,
     *  including a name for the node itself at the specified level
     *  of indentation, plus more deeply indented descriptions of
     *  any child nodes.
     */
    public void indent(IndentOutput out, int n) {
        out.indent(n, "Return");
        if (exp==null) {
            out.indent(n, "[no return value]");
        } else {
            exp.indent(out, n+1);
        }
    }

    /** Type check this statement, using the specified context, with
     *  the given initial typing environment, and returning the typing
     *  environment for a following statement.
     */
    public TypeEnv check(Context ctxt, TypeEnv locals)
      throws Failure {
        if (ctxt.retType==null) { // appears in void function
            if (exp!=null) {
                ctxt.report(new Failure("ReturnVoidRequired"));
                exp.typeOf(ctxt, locals);  // ignore result
            }
        } else {          // return in non-void function
            if (exp==null) {
                ctxt.report(new Failure("ReturnValueRequired"));
            } else {
                exp = exp.matchType(ctxt, locals, ctxt.retType, "ReturnType");
            }
        }
        return locals;
    }

    /** Return true if this statement can be guaranteed to return,
     *  ensuring that any immediately following statement will not
     *  be executed.
     */
    public boolean guaranteedToReturn() {
        // Return statements signal an exit from the function
        // in which they appear, so we will never continue on
        // to a subsequent statement after a return.
        return true;
    }

    /** Generate code for executing this statement.
     *  Returns true if there is a chance that execution may
     *  continue with the next statement.
     */
    public boolean compile(Assembly a, Frame f) {
        if (exp!=null) {
           exp.compileExpr(a, f);
        }
        a.emitEpilogue();
        return false;
    }
}
