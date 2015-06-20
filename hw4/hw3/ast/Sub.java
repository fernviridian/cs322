package ast;
import compiler.Failure;

/** Abstract syntax for the subtraction operator, -.
 */
public class Sub extends ArithBinExpr {

    /** Default constructor.
     */
    public Sub(Expr left, Expr right) {
        super(left, right);
    }

    /** Return a string that provides a simple description of this
     *  particular type of operator node.
     */
    String label() { return "Sub"; }

    /** Generate assembly language code for this expression that will
     *  evaluate the expression when it is executed and leave the result
     *  in the next free register, as specified by the layout.
     */
    public void compileExpr(Assembly a, Frame f) {
        compileBin(a, f, "subl", false);
    }
}
