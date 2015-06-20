package ast;
import compiler.Failure;

/** An abstract base class for expressions that can be used in a
 *  a statement (i.e., for expressions that might have a side-effect).
 */
public abstract class StmtExpr extends Expr {

    /** Type check this statement expression, using the specified
     *  context and the given typing environment.
     */
    public abstract Type check(Context ctxt, TypeEnv locals)
      throws Failure;
}
