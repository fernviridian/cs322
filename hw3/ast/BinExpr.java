package ast;
import compiler.Failure;

/** An abstract base class for binary expressions (i.e.,
 *  expressions that have a left and a right operand).
 */
public abstract class BinExpr extends Expr {

    /** The left subexpression.
     */
    protected Expr left;

    /** The right subexpression.
     */
    protected Expr right;

    /** Default constructor.
     */
    public BinExpr(Expr left, Expr right) {
        this.left = left;
        this.right = right;
    
        // Compute the depth of this expression:
        depth = 1 + Math.max(left.getDepth(), right.getDepth());
    }

    /** This attribute will be filled in during static analysis to record
     *  the type of the arguments for this operator (both left and right
     *  arguments are required to have the same type).  This information
     *  will be useful for situations in code generation where we need
     *  to distinguish between using an integer or a floating point
     *  version of an operation.  The type attribute is set to null when
     *  a node is first created to indicate that the type has yet
     *  to be determined.
     */
    protected Type type = null;

    /** Print an indented description of this abstract syntax node,
     *  including a name for the node itself at the specified level
     *  of indentation, plus more deeply indented descriptions of
     *  any child nodes.
     */
    public void indent(IndentOutput out, int n) {
        out.indent(n, (type==null) ? label() : (label() + " " + type));
        left.indent(out, n+1);
        right.indent(out, n+1);
    }

    /** Return a string that provides a simple description of this
     *  particular type of operator node.
     */
    abstract String label();

    protected Type matchTypes(Context ctxt, TypeEnv locals)
      throws Failure {
        Type lt = left.typeOf(ctxt, locals);
        Type rt = right.typeOf(ctxt, locals);
        if (lt.equals(rt)) {
            return lt;
        } else if (lt.equals(Type.INT) && rt.equals(Type.DOUBLE)) {
            left = new IntToDouble(left);
            return Type.DOUBLE;
        } else if (lt.equals(Type.DOUBLE) && rt.equals(Type.INT)) {
            right = new IntToDouble(right);
            return Type.DOUBLE;
        }
        return null;
    }

    /** Return the depth of this expression as a measure of how complicated
     *  the expression is / how many registers will be needed to evaluate it.
     */
    int getDepth() {
        // Return the depth value that was computed by the constructor
        return depth;
    }

    /** Records the depth of this expression; this value is computed
     *  at the time the constructor is called and then saved here so
     *  that it can be accessed without further computation later on.
     */
    protected int depth;

    /** Generate code to evalute both of the expressions left and right,
     *  changing the order of evaluation if possible/beneficial to
     *  reduce the number of registers that are required.  The return
     *  boolean indicates the order in which the two expressions have
     *  been evaluated and stored in registers.  A true result indicates
     *  that reg(free) contains the value of left and reg(free+1) contains
     *  the value of right.  A false result indicates that the order has
     *  been reversed.  In both cases, reg(free+1) will need to be
     *  unspilled once the value in that register has been used.
     */
    void compileBin(Assembly a, Frame f, String op, boolean commutative) {
        String r0 = f.free32();
        String r1;
        if (left.getDepth()>=right.getDepth() || right.getDepth()>=DEEP) {
            left.compileExpr(a, f);
            r1 = f.spill32(a);
            right.compileExpr(a, f);
        } else {
            right.compileExpr(a, f);
            r1 = f.spill32(a);
            left.compileExpr(a, f);
            if (!commutative) {
                a.emit("xchgl", r1, r0);
            }
        }
        a.emit(op, r1, r0);
        f.unspill(a);
    }
}
