package ast;
import compiler.Failure;

/** Abstract syntax for if-then-else statements.
 */
public class If extends Stmt {

    /** The test expression.
     */
    private Expr test;

    /** The true branch.
     */
    private Stmt ifTrue;

    /** The false branch.
     */
    private Stmt ifFalse;

    /** Default constructor.
     */
    public If(Expr test, Stmt ifTrue, Stmt ifFalse) {
        this.test = test;
        this.ifTrue = ifTrue;
        this.ifFalse = ifFalse;
    }

    /** A special version of the constructor that can be used
     *  when there is no false branch (i.e., no "else" clause),
     *  in which case the null value is used.
     */
    public If(Expr test, Stmt ifTrue) {
        this(test, ifTrue, null);
      }

    /** Print an indented description of this abstract syntax node,
     *  including a name for the node itself at the specified level
     *  of indentation, plus more deeply indented descriptions of
     *  any child nodes.
     */
    public void indent(IndentOutput out, int n) {
        out.indent(n, "If");
        test.indent(out, n+1);
        ifTrue.indent(out, n+1);
        if (ifFalse==null) {
            out.indent(n, "[no else branch]");
        } else {
            ifFalse.indent(out, n+1);
        }
    }

    /** Type check this statement, using the specified context, with
     *  the given initial typing environment, and returning the typing
     *  environment for a following statement.
     */
    public TypeEnv check(Context ctxt, TypeEnv locals)
      throws Failure {
        try {
            if (!test.typeOf(ctxt, locals).equals(Type.BOOLEAN)) {
                ctxt.report(new Failure("IfBoolean"));
            }
        } catch (Failure f) {
            ctxt.report(f);
        }
        ifTrue.check(ctxt, locals);
        if (ifFalse!=null) {
            ifFalse.check(ctxt, locals);
        }
        return locals;
    }

    /** Return true if this statement can be guaranteed to return,
     *  ensuring that any immediately following statement will not
     *  be executed.
     */
    public boolean guaranteedToReturn() {
        // An if statement is only guaranteed to return if it has
        // two branches (a true and a false branch), both of which
        // are guaranteed to return:
        return ifTrue.guaranteedToReturn() &&
               ifFalse!=null &&
               ifFalse.guaranteedToReturn();
    }

    /** Generate code for executing this statement.
     *  Returns true if there is a chance that execution may
     *  continue with the next statement.
     */
    public boolean compile(Assembly a, Frame f) {
        String lab1 = a.newLabel();
        test.branchFalse(a, f, lab1);
        boolean c = ifTrue.compile(a, f);
        if (ifFalse==null) {
            a.emitLabel(lab1);
            return true;
        } if (!c) {
            a.emitLabel(lab1);
            c |= ifFalse.compile(a, f);
        } else {
            String lab2 = a.newLabel();
            a.emit("jmp", lab2);
            a.emitLabel(lab1);
            c |= ifFalse.compile(a, f);
            a.emitLabel(lab2);
        }
        return c;
    }
}
