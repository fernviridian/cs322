package ast;
import compiler.Failure;

/** An abstract base class for arithmetic binary expressions.
 */
public abstract class ArithBinExpr extends BinExpr {

    /** Default constructor.
     */
    public ArithBinExpr(Expr left, Expr right) {
        super(left, right);
    }

    /** Calculate the type of this expression, using the given context
     *  and type environment.
     */
    public Type typeOf(Context ctxt, TypeEnv locals)
      throws Failure {
        type = matchTypes(ctxt, locals);
        if (type==null || !type.isNumeric()) {
            throw new Failure("ArithBinArgsNumeric");
        }
        return type;
    }
}
