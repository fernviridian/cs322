package ast;
import compiler.Failure;

/** Abstract syntax for do while statements.
 */
public class DoWhile extends Stmt {

    /** The body of this loop.
     */
    private Stmt body;

    /** The test expression.
     */
    private Expr test;

    /** Default constructor.
     */
    public DoWhile(Stmt body, Expr test) {
        this.body = body;
        this.test = test;
    }

    /** Print an indented description of this abstract syntax node,
     *  including a name for the node itself at the specified level
     *  of indentation, plus more deeply indented descriptions of
     *  any child nodes.
     */
    public void indent(IndentOutput out, int n) {
        out.indent(n, "DoWhile");
        body.indent(out, n+1);
        test.indent(out, n+1);
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
        a.emitLabel(lab1);
        body.compile(a, f);
        a.emitLabel(lab2);
        test.branchTrue(a, f, lab1);
        return true;
          
          /*
            jmp lab2
         lab1:
            #body
         lab2:
            cmpl cond
            je   lab1


          #dowhile
          lab1:
            #body
          lab2:
            cmpl cond
            je lab1


          */

    }
}
