package ast;
import compiler.Failure;

/** Abstract syntax for break statements.
 */
public class Break extends Stmt {

    /** Print an indented description of this abstract syntax node,
     *  including a name for the node itself at the specified level
     *  of indentation, plus more deeply indented descriptions of
     *  any child nodes.
     */
    public void indent(IndentOutput out, int n) {
        out.indent(n, "Break");
    }

    /** Type check this statement, using the specified context, with
     *  the given initial typing environment, and returning the typing
     *  environment for a following statement.
     */
    public TypeEnv check(Context ctxt, TypeEnv locals)
      throws Failure {
        return locals;
    }

    /** Generate code for executing this statement.
     *  Returns true if there is a chance that execution may
     *  continue with the next statement.
     */
    public boolean compile(Assembly a, Frame f) {
        throw new Error("compile not implemented for Break");
    }
}
