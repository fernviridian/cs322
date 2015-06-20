package ast;
import compiler.Failure;

/** Abstract syntax for the addition operator, +.
 */
public class Add extends ArithBinExpr {

    /** Default constructor.
     */
    public Add(Expr left, Expr right) {
        super(left, right);
    }

    /** Return a string that provides a simple description of this
     *  particular type of operator node.
     */
    String label() { return "Add"; }

    /** Generate assembly language code for this expression that will
     *  evaluate the expression when it is executed and leave the result
     *  in the next free register, as specified by the layout.
     */
    public void compileExpr(Assembly a, Frame f) {
        compileBin(a, f, "addl", true);
    }
}
