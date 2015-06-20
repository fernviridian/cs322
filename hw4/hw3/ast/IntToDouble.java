package ast;
import compiler.Failure;

/** Abstract syntax for an int to double cast operation.
 */
public class IntToDouble extends CastExpr {

    /** Default constructor.
     */
    public IntToDouble(Expr exp) {
        super(exp);
    }

    /** Print an indented description of this abstract syntax node,
     *  including a name for the node itself at the specified level
     *  of indentation, plus more deeply indented descriptions of
     *  any child nodes.
     */
    public void indent(IndentOutput out, int n) {
        out.indent(n, "IntToDouble");
        exp.indent(out, n+1);
    }

    /** Calculate the type of this expression, using the given context
     *  and type environment.
     */
    public Type typeOf(Context ctxt, TypeEnv locals)
      throws Failure {
        Type et = exp.typeOf(ctxt, locals);
        if (!et.equals(Type.INT)) {
            ctxt.report(new Failure("CastInt"));
        }
        return Type.DOUBLE;
    }

    /** Generate assembly language code for this expression that will
     *  evaluate the expression when it is executed and leave the result
     *  in the next free register, as specified by the layout.
     */
    public void compileExpr(Assembly a, Frame f) {
        throw new Error("compileExpr not implemented for IntToDouble or DoubleToInt");
    }
}
