package ast;
import compiler.Failure;

/** Abstract syntax for the <= comparison operator.
 */
public class Lte extends RelBinExpr {

    /** Default constructor.
     */
    public Lte(Expr left, Expr right) {
        super(left, right);
    }

    /** Return a string that provides a simple description of this
     *  particular type of operator node.
     */
    String label() { return "Lte"; }

    /** Generate assembly language code for this expression that will
     *  evaluate the expression when it is executed and leave the result
     *  in the next free register, as specified by the layout.
     */
    public void compileExpr(Assembly a, Frame f) {
        // Assume integer comparison:
        compileCondValue(a, f, "jle");
    }

    /** Generate code that will evaluate this (boolean-valued) expression
     *  and jump to the specified label if the result is true.
     */
    void branchTrue(Assembly a, Frame f, String lab) {
        compileCond(a, f);
        a.emit("jle", lab);
    }

    /** Generate code that will evaluate this (boolean-valued) expression
     *  and jump to the specified label if the result is false.
     */
    void branchFalse(Assembly a, Frame f, String lab) {
        compileCond(a, f);
        a.emit("jg", lab);
    }
}
