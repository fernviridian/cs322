package ast;
import compiler.Failure;

/** An abstract syntax base class for expressions.
 */
public abstract class Expr {

    /** Print an indented description of this abstract syntax node,
     *  including a name for the node itself at the specified level
     *  of indentation, plus more deeply indented descriptions of
     *  any child nodes.
     */
    public abstract void indent(IndentOutput out, int n);

    /** Calculate the type of this expression, using the given context
     *  and type environment.
     */
    public abstract Type typeOf(Context ctxt, TypeEnv locals)
      throws Failure;

    public Expr matchType(Context ctxt, TypeEnv locals, Type reqd, String err)
      throws Failure {
        Type actual = typeOf(ctxt, locals);
        if (reqd.equals(Type.INT) && actual.equals(Type.DOUBLE)) {
            return new DoubleToInt(this);
        } else if (reqd.equals(Type.DOUBLE) && actual.equals(Type.INT)) {
            return new IntToDouble(this);
        } else if (!reqd.equals(actual)) {
            ctxt.report(new Failure(err));
        }
        return this;
    }

    /** Return the depth of this expression as a measure of how complicated
     *  the expression is / how many registers will be needed to evaluate it.
     */
    abstract int getDepth();

    /** Used as a depth value to indicate an expression that has a
     *  potential side effect, and hence requires order of evaluation
     *  to be preserved.  (The same depth value could, in theory, be
     *  produced as the depth of a stunningly complex but side-effect
     *  free expression; oh well, we'll just miss the attempt to
     *  minimize register usage in such (highly unlikely) cases. :-)
     */
    public static final int DEEP = 1000;

    /** Generate assembly language code for this expression that will
     *  evaluate the expression when it is executed and leave the result
     *  in the next free register, as specified by the layout.
     */
    public abstract void compileExpr(Assembly a, Frame f);

    /** Generate code that will evaluate this (boolean-valued) expression
     *  and jump to the specified label if the result is true.
     */
    void branchTrue(Assembly a, Frame f, String lab) {
        compileExpr(a, f);
        a.emit("orl", f.free32(), f.free32());
        a.emit("jnz", lab);
    }

    /** Generate code that will evaluate this (boolean-valued) expression
     *  and jump to the specified label if the result is false.
     */
    void branchFalse(Assembly a, Frame f, String lab) {
        compileExpr(a, f);
        a.emit("orl", f.free32(), f.free32());
        a.emit("jz", lab);
    }
}
