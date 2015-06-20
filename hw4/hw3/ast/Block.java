package ast;
import compiler.Failure;

/** A block of statements.
 */
public class Block extends Stmt {

    /** The list of zero or more statements that
     *  make up the body of this block.
     */
    private Stmt[] body;

    /** Default constructor.
     */
    public Block(Stmt[] body) {
        this.body = body;
    }

    /** Print an indented description of this abstract syntax node,
     *  including a name for the node itself at the specified level
     *  of indentation, plus more deeply indented descriptions of
     *  any child nodes.
     */
    public void indent(IndentOutput out, int n) {
        out.indent(n, "Block");
        for (int i=0; i<body.length; i++) {
            body[i].indent(out, n+1);
        }
    }

    /** Type check this statement, using the specified context, with
     *  the given initial typing environment, and returning the typing
     *  environment for a following statement.
     */
    public TypeEnv check(Context ctxt, TypeEnv locals)
      throws Failure {
        TypeEnv inner = locals;
        for (int i=0; i<body.length; i++) {
            inner = body[i].check(ctxt, inner);
        }
        return locals;  // Back to original environment outside block
    }

    /** Return true if this statement can be guaranteed to return,
     *  ensuring that any immediately following statement will not
     *  be executed.
     */
    public boolean guaranteedToReturn() {
        // A block is guaranteed to return if any one of the
        // statements in the block is guaranteed to return.
        for (int i=0; i<body.length; i++) {
            if (body[i].guaranteedToReturn()) {
                return true;
            }
        }
        return false;
    }

    /** Generate code for executing this statement.
     *  Returns true if there is a chance that execution may
     *  continue with the next statement.
     */
    public boolean compile(Assembly a, Frame f) {    // { body }
        LocEnv origEnv = f.getEnv();
        for (int i=0; i<body.length; i++) {
            if (!body[i].compile(a, f)) {
                return false;
            }
        }
        f.resetTo(a, origEnv);
        return true;
    }
}
