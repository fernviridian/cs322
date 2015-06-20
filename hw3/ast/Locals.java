package ast;
import compiler.Failure;

/** Abstract syntax for local variable declarations.
 */
public class Locals extends InitStmt {

    /** The type of the declared variables.
     */
    private Type type;

    /** The names of the declared variables.
     */
    private VarIntro[] vars;

    /** Default constructor.
     */
    public Locals(Type type, VarIntro[] vars) {
        this.type = type;
        this.vars = vars;
    }

    /** Print an indented description of this abstract syntax node,
     *  including a name for the node itself at the specified level
     *  of indentation, plus more deeply indented descriptions of
     *  any child nodes.
     */
    public void indent(IndentOutput out, int n) {
        out.indent(n, "Locals");
        out.indent(n+1, type.toString());
        for (int i=0; i<vars.length; i++) {
           vars[i].indent(out, n+1);
        }
    }

    /** Type check this statement, using the specified context, with
     *  the given initial typing environment, and returning the typing
     *  environment for a following statement.
     */
    public TypeEnv check(Context ctxt, TypeEnv locals)
      throws Failure {
        if (VarIntro.containsRepeats(vars)) {
            ctxt.report(new Failure("LocalsUnique"));
        }
        for (int i=0; i<vars.length; i++) {
           locals = vars[i].check(ctxt, locals, type);
        }
        return locals;
    }

    /** Generate code for executing this statement.
     *  Returns true if there is a chance that execution may
     *  continue with the next statement.
     */
    public boolean compile(Assembly a, Frame f) {
        for (int i=0; i<vars.length; i++) {
           vars[i].compile(a, f);
        }
        //f.dump(a);
        return true;
    }
}
