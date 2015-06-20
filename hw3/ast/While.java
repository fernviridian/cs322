package ast;
import compiler.Failure;

/** Abstract syntax for while statements.
 */
public class While extends Stmt {

    /** The test expression.
     */
    private Expr test;

    /** The body of this loop.
     */
    private Stmt body;

    /** Default constructor.
     */
    public While(Expr test, Stmt body) {
        this.test = test;
        this.body = body;
    }

    /** Print an indented description of this abstract syntax node,
     *  including a name for the node itself at the specified level
     *  of indentation, plus more deeply indented descriptions of
     *  any child nodes.
     */
    public void indent(IndentOutput out, int n) {
        out.indent(n, "While");
        test.indent(out, n+1);
        body.indent(out, n+1);
    }

    /** Type check this statement, using the specified context, with
     *  the given initial typing environment, and returning the typing
     *  environment for a following statement.
     */
    public TypeEnv check(Context ctxt, TypeEnv locals)
      throws Failure {
        try {
            if (!test.typeOf(ctxt, locals).equals(Type.BOOLEAN)) {
                ctxt.report(new Failure("WhileBoolean"));
            }
        } catch (Failure f) {
            ctxt.report(f);
        }
        body.check(ctxt, locals); // discard final environment
        return locals;
    }

    /** Generate code for executing this statement.
     *  Returns true if there is a chance that execution may
     *  continue with the next statement.
     */
    public boolean compile(Assembly a, Frame f) {
        String lab1 = a.newLabel();
        String lab2 = a.newLabel();
        a.emit("jmp", lab2);
        a.emitLabel(lab1);
        body.compile(a, f);
        a.emitLabel(lab2);
        test.branchTrue(a, f, lab1);
        return true;
    }
}
