package ast;
import compiler.Failure;

/** Abstract syntax for the logical OR operator, ||.
 */
public class LOr extends LogBinExpr {

    /** Default constructor.
     */
    public LOr(Expr left, Expr right) {
        super(left, right);
    }

    /** Return a string that provides a simple description of this
     *  particular type of operator node.
     */
    String label() { return "LOr"; }

    /** Generate assembly language code for this expression that will
     *  evaluate the expression when it is executed and leave the result
     *  in the next free register, as specified by the layout.
     */
    public void compileExpr(Assembly a, Frame f) {
        String lab = a.newLabel();
        String r   = f.free32();
        left.compileExpr(a, f);
        a.emit("orl", r, r);
        a.emit("jnz",  lab);
        right.compileExpr(a, f);
        a.emitLabel(lab);
    }

    /** Generate code that will evaluate this (boolean-valued) expression
     *  and jump to the specified label if the result is true.
     */
    void branchTrue(Assembly a, Frame f, String lab) {
        left.branchTrue(a, f, lab);
        right.branchTrue(a, f, lab);
    }

    /** Generate code that will evaluate this (boolean-valued) expression
     *  and jump to the specified label if the result is false.
     */
    void branchFalse(Assembly a, Frame f, String lab) {
        String lab1 = a.newLabel();
        left.branchTrue(a, f, lab1);
        right.branchFalse(a, f, lab);
        a.emitLabel(lab1);
    }
}
