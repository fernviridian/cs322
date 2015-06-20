package ast;
import compiler.Failure;

/** Abstract syntax for the logical AND operator, &&.
 */
public class LAnd extends LogBinExpr {

    /** Default constructor.
     */
    public LAnd(Expr left, Expr right) {
        super(left, right);
    }

    /** Return a string that provides a simple description of this
     *  particular type of operator node.
     */
    String label() { return "LAnd"; }

    /** Generate assembly language code for this expression that will
     *  evaluate the expression when it is executed and leave the result
     *  in the next free register, as specified by the layout.
     */
    public void compileExpr(Assembly a, Frame f) {
        String lab = a.newLabel();
        String r   = f.free32();
        left.compileExpr(a, f);
        a.emit("orl", r, r);
        a.emit("jz",  lab);
        right.compileExpr(a, f);
        a.emitLabel(lab);
    }

    /** Generate code that will evaluate this (boolean-valued) expression
     *  and jump to the specified label if the result is true.
     */
    void branchTrue(Assembly a, Frame f, String lab) {
        String lab1 = a.newLabel();
        left.branchFalse(a, f, lab1);
        right.branchTrue(a, f, lab);
        a.emitLabel(lab1);
    }

    /** Generate code that will evaluate this (boolean-valued) expression
     *  and jump to the specified label if the result is false.
     */
    void branchFalse(Assembly a, Frame f, String lab) {
        left.branchFalse(a, f, lab);
        right.branchFalse(a, f, lab);
    }
}
